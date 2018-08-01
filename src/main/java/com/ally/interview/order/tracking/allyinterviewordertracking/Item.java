package com.ally.interview.order.tracking.allyinterviewordertracking;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long itemId;
    
    private String sku;
    
    @JsonIgnore
    @ManyToOne
    private OrderOfItems orderOfItems;

    private double price;
    private double quantity;

    private Item(){};

    public Item(String sku, OrderOfItems orderOfItems, double price, double quantity) {
        this.sku = sku;
    	this.orderOfItems = orderOfItems;
        this.price = price;
        this.quantity = quantity;
    }

	public long getItemId() {
		return itemId;
	}

	public String getSku() {
		return sku;
	}

	public OrderOfItems getOrderOfItems() {
		return orderOfItems;
	}

	public double getPrice() {
		return price;
	}

	public double getQuantity() {
		return quantity;
	}
    
    
}
