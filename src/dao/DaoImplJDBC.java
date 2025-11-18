package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
			createTableInventory();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: Couldn't load DB");
			e.printStackTrace();
		}

	}
	
	private void createTableInventory() {
		String query = "create table if not exists Inventory ("
                + "id int primary key auto_increment, "
                + "name varchar(100), "
                + "wholesaler_price double, "
                + "available boolean, "
                + "stock int"
                + ");";
		
		try (Statement stmt = connection.createStatement()){
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Error: Couldn't create table inventory in DB");
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		String query = "select * from inventory";
		
		Statement stmt = null;
		ResultSet rs = null;
		Product product;
		ArrayList<Product> products = new ArrayList<>();
		
		try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new Amount(rs.getDouble("wholesaler_price")),
                        rs.getBoolean("available"),
                        rs.getInt("stock")
                );
                
                products.add(product);
            }
        } catch (SQLException ex) {
        	System.out.println("Error: Couldn't load data on table inventory in DB");
        	ex.printStackTrace();
        } 
		
		return products;
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
