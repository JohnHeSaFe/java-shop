package dao;

import java.util.ArrayList;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.Session;
import model.Employee;
import model.Product;

public class DaoImplHibernate implements Dao {
	
	private Session session;
	private SessionFactory sessionFactory;
	
	@Override
	public void connect() {
		try {
			if (sessionFactory == null || sessionFactory.isClosed()) {
				StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					    .configure("hibernate.cfg.xml")
					    .build();
				sessionFactory = new MetadataSources(registry)
					    .getMetadataBuilder()
					    .build()
					    .getSessionFactoryBuilder()
					    .build();
			}
			
			
			if (session == null || !session.isOpen()) {
				session = sessionFactory.openSession();
	        }
		} catch (Exception e) {
			System.out.println("Error: couldn't open Hibernate session.");
		}
	}

	@Override
	public void disconnect() {
		try {
			if (session != null && session.isOpen()) {
				session.close();
	        }
			
			if (sessionFactory != null && !sessionFactory.isClosed()) {
				sessionFactory.close();
			}
		} catch (Exception e) {
			System.out.println("Error: couldn't close Hibernate session.");
		}							
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub		
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		connect();
		
		String hql = "FROM Product";
		
		Query<Product> query = session.createQuery(hql, Product.class);
		
		return new ArrayList<Product>(query.list());
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int id) {
		// TODO Auto-generated method stub
		return false;
	}

}
