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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

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
			
			mongoClient = null;
	        database = null;
	        usersCollection = null;
	        inventoryCollection = null;
	        inventoryHistoricalCollection = null;
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
	    if (inventoryHistoricalCollection == null) {
	        connect();
	    }

	    try {
	        java.util.List<Document> documents = new ArrayList<>();
	        
	        java.util.Date createdAt = new java.util.Date(); 

	        for (Product product : products) {
	            Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
	                                     .append("currency", product.getWholesalerPrice().getCurrency());

	            Document doc = new Document("id", product.getId())
	                    .append("name", product.getName())
	                    .append("wholesalerPrice", wholesalerPriceDoc)
	                    .append("available", product.isAvailable())
	                    .append("stock", product.getStock())
	                    .append("created_at", createdAt); 

	            documents.add(doc);
	        }

	        if (!documents.isEmpty()) {
	            inventoryHistoricalCollection.insertMany(documents);
	        }
	        
	        return true;
	    } catch (Exception e) {
	        System.err.println("Error: couldn't export inventory in MongoDB.");
	        return false;
	    }
	}

	@Override
    public void addProduct(Product product) {
        if (inventoryCollection == null) {
            connect();
        }
        try {
            Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
                    .append("currency", product.getWholesalerPrice().getCurrency());

            Document doc = new Document("name", product.getName())
                    .append("wholesalerPrice", wholesalerPriceDoc)
                    .append("available", product.isAvailable())
                    .append("stock", product.getStock())
                    .append("id", product.getId());

            inventoryCollection.insertOne(doc);
        } catch (Exception e) {
        	System.err.println("Error: couldn't add product in MongoDB.");
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (inventoryCollection == null) {
            connect();
        }
        try {
            Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
                    .append("currency", product.getWholesalerPrice().getCurrency());

            inventoryCollection.updateOne(
                Filters.eq("id", product.getId()),
                Updates.combine(
                    Updates.set("name", product.getName()),
                    Updates.set("wholesalerPrice", wholesalerPriceDoc),
                    Updates.set("available", product.isAvailable()),
                    Updates.set("stock", product.getStock())
                )
            );
        } catch (Exception e) {
            System.err.println("Error: couldn't update product in MongoDB.");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int id) {
        if (inventoryCollection == null) {
            connect();
        }
        try {
            inventoryCollection.deleteOne(Filters.eq("id", id));
        } catch (Exception e) {
            System.err.println("Error: couldn't delete product in MongoDB.");
            e.printStackTrace();
        }
    }

}
