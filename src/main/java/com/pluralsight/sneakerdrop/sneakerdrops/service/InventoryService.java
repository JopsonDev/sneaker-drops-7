package com.pluralsight.sneakerdrop.sneakerdrops.service;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    public String getInventory(){
        return "Inventory module ready.";
    }
}
