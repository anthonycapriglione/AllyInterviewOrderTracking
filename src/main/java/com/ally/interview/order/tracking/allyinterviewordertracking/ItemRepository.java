package com.ally.interview.order.tracking.allyinterviewordertracking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
}
