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
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.save(product);
			
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			System.out.println("Error: couldn't add Product Hibernate");
			return false;
		}
	}

	@Override
	public boolean updateProduct(Product product) {
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			session.update(product);
			
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			System.out.println("Error: couldn't update Product Hibernate");
			return false;
		}
	}

	@Override
	public boolean deleteProduct(int id) {
		connect();
		
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			Product product = session.get(Product.class, id);
			
			if (product != null) {
				session.delete(product);
				transaction.commit();
				return true;
			}
			
			return false;
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			
			System.out.println("Error: couldn't delete Product Hibernate");
			return false;
		}
	}

}
