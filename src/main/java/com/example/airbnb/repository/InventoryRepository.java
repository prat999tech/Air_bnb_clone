package com.example.airbnb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.airbnb.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
