package model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_history")
public class ProductHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
    private boolean available;
	
	@Column(name = "created_at")
    private LocalDateTime createdAt;
	
	@Column(name = "id_product")
    private int idProduct;
	
	@Column
    private String name;
	
	@Embedded
    private Amount wholesalerPrice;
	
	@Column(name = "stock")
    private int stock;
    
    
    public ProductHistory() {}
    

    public ProductHistory(Product product) {
        this.available = product.isAvailable();
        this.createdAt = LocalDateTime.now(); 
        this.idProduct = product.getId();
        this.name = product.getName();
        this.wholesalerPrice = product.getWholesalerPrice();
        this.stock = product.getStock();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getPublicPrice() {
        return wholesalerPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.wholesalerPrice = publicPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
    	return "ProductHistory [id=" + id + ", idProduct=" + idProduct + ", name=" + name 
                + ", publicPrice=" + wholesalerPrice + ", available=" + available + ", stock=" + stock 
                + ", createdAt=" + createdAt + "]";
    }

}
