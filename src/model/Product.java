package model;

import jakarta.persistence.*;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "product_name")
    private String name;
	
	@Column(name = "public_price")
    private double publicPrice;
	
	@Column(name = "wholesaler_price")
    private double wholesalerPrice;
	
	@Column(name = "is_available")
    private boolean available;
	
	@Column(name = "stock_count")
    private int stock;
    
    @Transient
    private static int totalProducts;
    
    @Transient
    public final static double EXPIRATION_RATE=0.60;
    
    public Product() {}
    
	public Product(String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.id = totalProducts+1;
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = wholesalerPrice * 2;
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}
	
	public Product(int id, String name, double wholesalerPrice, boolean available, int stock) {
		this(name, wholesalerPrice, available, stock);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public double getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(double publicPrice) {
		this.publicPrice = publicPrice;
	}

	public double getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(double wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}
	
	public void expire() {
		this.publicPrice = this.getPublicPrice() * EXPIRATION_RATE;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", available=" + available + ", stock=" + stock + "]";
	}

	
	
	
	
	

    

    
}
