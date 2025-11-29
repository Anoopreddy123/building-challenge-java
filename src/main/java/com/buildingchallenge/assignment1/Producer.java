package com.buildingchallenge.assignment1;

import java.util.List;

/**
 * Producer Class
 * 
 * Purpose: Represents a producer thread that reads items from a source container
 * and places them into a shared queue. This class demonstrates the producer role
 * in the producer-consumer pattern.
 */
public class Producer implements Runnable {
    
    private final SharedQueue<String> sharedQueue;
    private final List<String> sourceContainer;
    private final String producerName;
    
    // Flag to control when the producer should stop producing
    private volatile boolean isRunning = true;
    
    /**
     * Constructor - Initializes the producer with source data and shared queue
     * 
     * @param sharedQueue The thread-safe queue where items will be placed
     * @param sourceContainer List of items to be produced
     * @param producerName Name identifier for this producer
     */

    public Producer(SharedQueue<String> sharedQueue, List<String> sourceContainer, String producerName) {
        if (sharedQueue == null || sourceContainer == null) {
            throw new IllegalArgumentException("SharedQueue and sourceContainer cannot be null");
        }
        this.sharedQueue = sharedQueue;
        this.sourceContainer = sourceContainer;
        this.producerName = producerName;
    }
    
    /** 
     * This method iterates through the source container and places each item
     * into the shared queue. If the queue is full, it will block until space
     * becomes available.
     * 
     * Thread Safety Precautions: Uses the thread-safe SharedQueue.put() method which handles
     * synchronization internally.
     */
    @Override
    public void run() {
        System.out.println("Producer [" + producerName + "] started");
        
        int itemIndex = 0;
        
        try {
            // Continue producing items while running and items are available in the source container
            while (isRunning && itemIndex < sourceContainer.size()) {
                String item = sourceContainer.get(itemIndex);
                
                // Attempt to place item in queue (may block if queue is full in the SharedQueue class)
                sharedQueue.put(item);
                itemIndex++;
            }
            
            System.out.println("Producer [" + producerName + "] finished producing " + itemIndex + " items");
            
        } catch (InterruptedException e) {
            // Handle thread interruption gracefully
            System.out.println("Producer [" + producerName + "] was interrupted");
            Thread.currentThread().interrupt(); // Restore interrupt status
        } catch (Exception e) {
            // Handle any other exceptions
            System.err.println("Producer [" + producerName + "] encountered an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the producer from producing more items
     * 
     * This method can be called to gracefully stop the producer thread.
     * The current item being processed will complete, but no new items will be produced.
     */
    public void stop() {
        isRunning = false;
        System.out.println("Producer [" + producerName + "] stop requested");
    }
}
