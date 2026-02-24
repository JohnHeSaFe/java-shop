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
			inventoryCollection = database.getCollection("inventory");
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
	
	@Override
	public void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		connect();
		Document query = new Document("employeeId", employeeId).append("password", password);
		Document result = usersCollection.find(query).first();
		
		if (result != null) {
			return new Employee(result.getInteger("employeeId"), result.getString("name"), result.getString("password"));
		}
		
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
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
