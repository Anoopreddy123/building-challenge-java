package com.buildingchallenge.assignment1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ProducerConsumerDemo Class
 * 
 * Purpose: Main application class that demonstrates the producer-consumer pattern.
 * This class creates and coordinates producer and consumer threads, demonstrating
 * thread synchronization and concurrent programming concepts.
 * @version 1.0
 */
public class ProducerConsumerDemo {
    
    private static final int DEFAULT_QUEUE_CAPACITY = 5;
    
    /**
     * Main method - Entry point of the application
     * 
     * This method sets up the producer-consumer scenario:
     * 1. Creates a shared queue with bounded capacity
     * 2. Prepares source data to be produced
     * 3. Creates destination container for consumed items
     * 4. Starts producer and consumer threads
     * 5. Waits for threads to complete
     * 6. Validates the results
     */
    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("Producer-Consumer Pattern Demo");
        System.out.println("========================================\n");
        
        // Step 1: Create shared queue with bounded capacity
        SharedQueue<String> sharedQueue = new SharedQueue<>(DEFAULT_QUEUE_CAPACITY);
        
        // Step 2: Prepare source data (items to be produced)
        List<String> sourceContainer = new ArrayList<>(Arrays.asList(
            "Item-1", "Item-2", "Item-3", "Item-4", "Item-5",
            "Item-6", "Item-7", "Item-8", "Item-9", "Item-10"
        ));
        
        System.out.println("Source container prepared with " + sourceContainer.size() + " items");
        
        // Step 3: Create destination container (where consumed items will be stored)
        List<String> destinationContainer = new ArrayList<>();
        
        // Step 4: Create producer and consumer instances
        Producer producer = new Producer(sharedQueue, sourceContainer, "Producer-1");
        Consumer consumer = new Consumer(sharedQueue, destinationContainer, "Consumer-1", 
                                        sourceContainer.size());
        
        // Step 5: Create and start threads
        Thread producerThread = new Thread(producer, "ProducerThread");
        Thread consumerThread = new Thread(consumer, "ConsumerThread");
        
        System.out.println("Starting threads...\n");
        
        // Start both threads
        producerThread.start();
        consumerThread.start();
        
        try {
            // Step 6: Wait for threads to complete their work
            producerThread.join();
            consumerThread.join();
            
            System.out.println("\n========================================");
            System.out.println("Threads completed");
            System.out.println("========================================\n");
            
            // Step 7: Validate results
            validateResults(sourceContainer, destinationContainer);
            
        } catch (InterruptedException e) {
            System.err.println("Main thread was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Validates that all produced items were successfully consumed
     * 
     * This method compares the source and destination containers to ensure
     * data integrity in the producer-consumer pattern.
     */
    private static void validateResults(List<String> sourceContainer, 
                                       List<String> destinationContainer) {
        System.out.println("Validation Results:");
        System.out.println("Source items count: " + sourceContainer.size());
        System.out.println("Destination items count: " + destinationContainer.size());
        
        // Checking the size of the source and destination containers
        if (sourceContainer.size() == destinationContainer.size()) {
            System.out.println("All items were successfully consumed");
        } else {
            System.out.println("Item count mismatch!");
        }

        // Checking if all items are present in the destination container
        boolean allItemsPresent = true;
        for (String sourceItem : sourceContainer) {
            if (!destinationContainer.contains(sourceItem)) {
                System.out.println("Missing item: " + sourceItem);
                allItemsPresent = false;
            }
        }
        
        if (allItemsPresent) {
            System.out.println("All items are present in destination");
        }
        
        System.out.println("\nDestination container contents:");
        destinationContainer.forEach(item -> System.out.println("  - " + item));
    }
}
