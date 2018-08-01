package com.ally.interview.order.tracking.allyinterviewordertracking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class OrderOfItems {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long orderNumber;


    @OneToMany(mappedBy = "orderOfItems")
    private List<Item> items;

    private double totalDue;
    private String status;

    private OrderOfItems(){};

    public OrderOfItems(double totalDue, String status) {
        this.totalDue = totalDue;
        this.status = status;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
		this.totalDue = totalDue;
	}

	public String getStatus() {
        return status;
    }

	public void setStatus(String status) {
		this.status = status;
	}
    
}
