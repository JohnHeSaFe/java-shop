package dao;

import java.util.ArrayList;

import com.mongodb.client.MongoClients;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
	private MongoClient mongoClient;
	private MongoDatabase database;
	private MongoCollection<Document> usersCollection;
	private MongoCollection<Document> inventoryCollection;
	private MongoCollection<Document> inventoryHistoricalCollection;
	
	@Override
	public void connect() {
		try {
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			
			database = mongoClient.getDatabase("shop");
			
			createUsersCollectionWithSampleData();
			createInventoryCollectionWithSampleData();
			inventoryHistoricalCollection = database.getCollection("historical_inventory");
		} catch (MongoException e) {
	        System.err.println("Error al conectar con MongoDB.");
	        e.printStackTrace();
	    }
	}
	
	private void createUsersCollectionWithSampleData() {
		if (usersCollection == null) {
			database.createCollection("users");
			usersCollection = database.getCollection("users");
		}
		if (usersCollection.find(Filters.eq("employeeId", 123)).first() == null) {
			Document document = new Document("_id", new ObjectId()).append("employeeId", 123).append("name", "John").append("password", "test");
			usersCollection.insertOne(document);
		}
	}
	
	private void createInventoryCollectionWithSampleData() {
		if (inventoryCollection == null) {
			database.createCollection("inventory");
			inventoryCollection = database.getCollection("inventory");
		}
		
        if (inventoryCollection.countDocuments() == 0) {
            Document applePrice = new Document("value", 10.5).append("currency", "€");
            Document apple = new Document("_id", new ObjectId())
                    .append("name", "Manzana")
                    .append("wholesalerPrice", applePrice)
                    .append("available", true)
                    .append("stock", 10)
                    .append("id", 1);

            Document strawberryPrice = new Document("value", 5.5).append("currency", "€");
            Document strawberry = new Document("_id", new ObjectId())
                    .append("name", "Fresa")
                    .append("wholesalerPrice", strawberryPrice)
                    .append("available", true)
                    .append("stock", 20)
                    .append("id", 2);

            inventoryCollection.insertOne(apple);
            inventoryCollection.insertOne(strawberry);
        }
    }
	
	@Override
	public void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		if (usersCollection == null) {
	        connect();
	    }
		
		
		try {
			Document query = new Document("employeeId", employeeId).append("password", password);
			Document result = usersCollection.find(query).first();
			if (result != null) {
				return new Employee(result.getInteger("employeeId"), result.getString("name"), result.getString("password"));
			}
		} catch (Exception e) {
	        System.err.println("Error: couldn't fetch employee from MongoDB.");
	    }
		
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
	    if (inventoryCollection == null) {
	        connect();
	    }

	    ArrayList<Product> inventory = new ArrayList<>();

	    try {
	        for (Document doc : inventoryCollection.find()) {
	            int id = doc.getInteger("id");
	            String name = doc.getString("name");
	            int stock = doc.getInteger("stock");
	            boolean available = doc.getBoolean("available");

	            Document wholesalerPriceDoc = (Document) doc.get("wholesalerPrice");
	            double priceValue = ((Number) wholesalerPriceDoc.get("value")).doubleValue();
	            String currency = wholesalerPriceDoc.getString("currency");
	            Amount wholesalerPrice = new Amount(priceValue);
	            wholesalerPrice.setCurrency(currency);

	            Product product = new Product(id, name, wholesalerPrice, available, stock);
	            
	            inventory.add(product);
	        }
	    } catch (Exception e) {
	        System.err.println("Error: couldn't fetch inventory from MongoDB.");
	    }

	    return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(int id) {
		// TODO Auto-generated method stub
		
	}

}
