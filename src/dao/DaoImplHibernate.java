package dao;

import java.util.ArrayList;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.Session;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {
	
	private Session session;
	private SessionFactory sessionFactory;
	
	public DaoImplHibernate() {
		connect();
	}
	
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
			e.printStackTrace();
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
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			for (Product product : products) {
				ProductHistory history = new ProductHistory(product);
				session.save(history);
			}
			
			transaction.commit();
			
			return true;
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			System.out.println("Error: couldn't add products to inventory_history Hibernate");
			return false;
		}
	}

	@Override
	public void addProduct(Product product) {
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.save(product);
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			throw new RuntimeException("Error: couldn't add Product Hibernate");
		}
	}

	@Override
	public void updateProduct(Product product) {
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.update(product);
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			throw new RuntimeException("Error: couldn't update Product Hibernate");
		}
	}

	@Override
	public void deleteProduct(int id) {
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			Product product = session.get(Product.class, id);
			
			if (product != null) {
				session.delete(product);
			} else {
				throw new RuntimeException("Error: product doesn't exist to delete");
			}
			
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			throw new RuntimeException("Error: couldn't delete Product Hibernate");
		}
	}

}
