package com.ally.interview.order.tracking.allyinterviewordertracking;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderOfItemsRepository orderOfItemsRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderController(OrderOfItemsRepository orderOfItemsRepository, ItemRepository itemRepository) {
        this.orderOfItemsRepository = orderOfItemsRepository;
        this.itemRepository = itemRepository;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> add(@RequestBody List<Item> items) {
    	double totalPrice = 0.00;
    	Map<String, Object> resultMap = new LinkedHashMap<>();
    	
    	OrderOfItems order = orderOfItemsRepository.save(new OrderOfItems(0.00, "WAITING_FOR_PAYMENT"));
    	
    	for(Item item : items) {
    		itemRepository.save(new Item(item.getSku(), order, item.getPrice(), item.getQuantity()));
    		totalPrice = totalPrice + (item.getPrice() * item.getQuantity());
    	}
    	
    	order.setTotalDue(totalPrice);
        orderOfItemsRepository.save(order);
        
        resultMap.put("orderNumber", order.getOrderNumber());
        resultMap.put("totalDue", order.getTotalDue());

        return ResponseEntity.ok(resultMap);

    }
    
    @RequestMapping(value = "/order/{orderNumber}", method = RequestMethod.GET)
    public OrderOfItems getOrder (@PathVariable long orderNumber) {
        return orderOfItemsRepository.findById(orderNumber)
        		.orElseThrow(() -> new ObjectNotFoundException(orderNumber, "Error: Order not found"));
    }
    
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<String> pay(@RequestBody Map<String, Object> paymentOrder) {
    	int orderNumber = (int) paymentOrder.get("orderNumber");
    	double payment = (double) paymentOrder.get("payment");
    	
    	Optional<OrderOfItems> orderOfItemsChecker = orderOfItemsRepository.findById((long) orderNumber);
    	if(orderOfItemsChecker.isPresent() == true) {
    		OrderOfItems orderOfItems = orderOfItemsChecker.get();
    		double oldTotalDue = orderOfItems.getTotalDue();
    		double newTotalDue = oldTotalDue - payment;
    		if(newTotalDue > 0.00) {
    			orderOfItems.setTotalDue(newTotalDue);
    			orderOfItemsRepository.save(orderOfItems);
    			return ResponseEntity.ok("Payment successful");
    		}
    		else if(newTotalDue == 0.00) {
    			orderOfItems.setTotalDue(newTotalDue);
    			orderOfItems.setStatus("SHIPPED");
    			orderOfItemsRepository.save(orderOfItems);
    			return ResponseEntity.ok("Paid in full, items shipped");
    		}
    		else {
    			throw new IllegalArgumentException("Error: Payment is too large or invalid");
    		}
    		
    		
    	}
    	else {
    		throw new ObjectNotFoundException(orderNumber, "Error: Order not found");
    	}
        
    	

    }
    
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getAllOrders() {
    	int numberWaitingForPayment = 0;
        int numberShipped = 0;
        Map<String, Integer> resultMap = new LinkedHashMap<>();
    	Iterable<OrderOfItems> allOrdersOfItems = orderOfItemsRepository.findAll();
        
        
        for(OrderOfItems orderOfItem : allOrdersOfItems) {
        	if(orderOfItem.getStatus() == "WAITING_FOR_PAYMENT") {
        		numberWaitingForPayment++;
        	}
        	else if(orderOfItem.getStatus() == "SHIPPED") {
        		numberShipped++;
        	}
        	else {
        		throw new IllegalStateException("Error: Invalid status detected");
        	}
        }
        resultMap.put("numberWaitingForPayment", numberWaitingForPayment);
        resultMap.put("numberShipped", numberShipped);
        
        return ResponseEntity.ok(resultMap);
    }
}
