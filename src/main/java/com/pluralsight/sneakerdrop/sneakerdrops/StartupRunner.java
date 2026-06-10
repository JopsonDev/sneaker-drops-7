package com.pluralsight.sneakerdrop.sneakerdrops;

import com.pluralsight.sneakerdrop.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrop.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrop.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrop.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrop.sneakerdrops.service.DropService;
import com.pluralsight.sneakerdrop.sneakerdrops.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final DropService dropService;
    private final InventoryService inventoryService;
    private final BrandRepository brandRepository;
    private final SneakerRepository sneakerRepository;

    @Autowired
    public StartupRunner(DropService dropService, InventoryService inventoryService, BrandRepository brandRepository, SneakerRepository sneakerRepository) {
        this.dropService = dropService;
        this.inventoryService = inventoryService;
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println(dropService.getStatus());
        System.out.println(inventoryService.getInventory());
        seedData();
        while(true) {
            System.out.println("1). List all shoes");
            System.out.println("2). Find by model");
            System.out.println("3). Find by price");
            System.out.println("4). Find by release year");
            System.out.println("5). Find by ID");
            System.out.println("6). Advance Search");
            System.out.println("0). Quit");
            System.out.print("Input: ");
            int input = scanner.nextInt();
            scanner.nextLine();
            switch(input){
                case 1 -> listSneakers();
                case 2 -> listSneakersByModel(scanner);
                case 3 -> listSneakerByPrice(scanner);
                case 4 -> listSneakerByYear(scanner);
                case 5 -> viewByID(scanner);
                case 6 -> advanceSearch(scanner);
                case 0 -> {
                    return;
                }
                default -> System.out.println("invalid input");

            }
        }
    }

    private void listSneakers(){
        System.out.println("Total = " + sneakerRepository.count());
        for (Sneaker s : sneakerRepository.findAll()) {
            System.out.println(s.getId() + " - " + s.getModel());
        }
    }
    private void listSneakersByModel(Scanner scanner){
        System.out.print("Please enter model: ");
        String input = scanner.nextLine();

        for(Sneaker s : sneakerRepository.findSneakerByModelContaining(input)){
            System.out.println(s);
        }
    }
    private void listSneakerByPrice(Scanner scanner){
        System.out.print("Please enter price: ");
        double input = scanner.nextDouble();
        scanner.nextLine();

        for(Sneaker s : sneakerRepository.findSneakerByPriceGreaterThan(input)){
            System.out.println(s);
        }
    }

    private void listSneakerByYear(Scanner scanner){
        System.out.print("Please enter release year: ");
        int input = scanner.nextInt();
        scanner.nextLine();

        for(Sneaker s : sneakerRepository.findSneakerByReleaseYear(input)){
            System.out.println(s);
        }
    }

    private void viewByID(Scanner scanner){
        System.out.println("Sneaker ID: ");
        long input = scanner.nextInt();
        scanner.nextLine();
        Sneaker sneaker = sneakerRepository.findById(input).orElse(null);
        if(sneaker == null){
            System.out.println("No sneakers with that ID");
        } else {
            System.out.println(sneaker.getId() + " - " + sneaker.getModel());
        }
    }

    private void advanceSearch(Scanner scanner){
        System.out.print("Max price: ");
        double price = scanner.nextDouble();
        System.out.print("Min year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        for(Sneaker s : sneakerRepository.search(price, year)){
            System.out.println(s.getId() + " - " + s.getModel());
        }
    }

    private void seedData() {
        if (brandRepository.count() == 0) {
            brandRepository.save(new Brand("Nike"));
            brandRepository.save(new Brand("Apple"));
            brandRepository.save(new Brand("Samsung"));
        }
        if (sneakerRepository.count() == 0) {
            sneakerRepository.save(new Sneaker("T40", 22.50, 2010));
            sneakerRepository.save(new Sneaker("Aplle 2.0", 100.99, 2026));

        }
    }
}
