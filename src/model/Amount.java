package model;

import java.text.DecimalFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Amount {
	@Column(name = "price")
	private double value;	
	@Column(name = "currency")
	private String currency="€";
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public Amount() {
	}
	
	public Amount(double value) {
		super();
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return df.format(value) + currency;
	}
	
}
