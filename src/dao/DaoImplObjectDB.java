package dao;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	@Override
	public void connect() {
		emf = Persistence.createEntityManagerFactory("objects/users.odb");
		em = emf.createEntityManager();
		
		em.getTransaction().begin();
		Employee admin = em.find(Employee.class, 123);
        
        if (admin == null) {
            admin = new Employee(123, "John", "test");
            em.persist(admin);
        }
        em.getTransaction().commit();
	}
	
	@Override
	public void disconnect() {
		if (em != null) em.close();
	    if (emf != null) emf.close();
	}
	
	@Override
	public Employee getEmployee(int employeeId, String password) {
		try {
			TypedQuery<Employee> query = em.createQuery("SELECT e FROM model.Employee e WHERE e.employeeId = :employeeId AND e.password = :password", Employee.class);
			query.setParameter("employeeId", employeeId);
			query.setParameter("password", password);
			    
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null; 
	    }
	}
	
	@Override
	public ArrayList<Product> getInventory() { return null; }
	
	@Override
	public boolean writeInventory(ArrayList<Product> products) { return false; }
	
	@Override
	public void addProduct(Product product) {}
	
	@Override
	public void updateProduct(Product product) {}
	
	@Override
	public void deleteProduct(int id) {}
}