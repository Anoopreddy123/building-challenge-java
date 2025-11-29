package com.buildingchallenge.assignment1;

import java.util.List;

/**
 * Consumer Class
 * 
 * Purpose: Represents a consumer thread that reads items from a shared queue customized for this assignment.
 * and stores them in a destination container. This class demonstrates the consumer
 * role in the producer-consumer pattern.
 * 
 */

public class Consumer implements Runnable {
    
    private final SharedQueue<String> sharedQueue;
    private final List<String> destinationContainer;
    private final String consumerName;
    
    // Maximum number of items this consumer should consume 0 means unlimited
    private final int maxItemsToConsume;
    
    // Flag to control the consumer thread
    private volatile boolean isRunning = true;
    
    /**
     * Initializes the consumer with destination container and shared queue
     * @param sharedQueue The thread-safe queue from where items will be consumed
     * @param destinationContainer List where consumed items will be stored
     * @param consumerName Name identifier for this consumer
     * @param maxItemsToConsume Maximum number of items to consume (0 for unlimited)
     */

    public Consumer(SharedQueue<String> sharedQueue, List<String> destinationContainer, 
                   String consumerName, int maxItemsToConsume) {

        if (sharedQueue == null || destinationContainer == null) {
            throw new IllegalArgumentException("SharedQueue and destinationContainer cannot be null");
        }
        this.sharedQueue = sharedQueue;
        this.destinationContainer = destinationContainer;
        this.consumerName = consumerName;
        this.maxItemsToConsume = maxItemsToConsume;
    }
    
    /** 
     * This method continuously reads items from the shared queue and stores them
     * in the destination container. If the queue is empty, it will block until
     * an item becomes available.
     * 
     * Thread Safety Precautions: Uses the thread-safe SharedQueue.take() method which handles
     * synchronization internally. Uses a while loop to ensure that the consumer thread does not 
     * block indefinitely if the queue is empty.
     */

    @Override
    public void run() {
        System.out.println("Consumer [" + consumerName + "] started");
        
        int itemsConsumed = 0;
        
        try {
            // Continue consuming items while running and within consumption limit
            while (isRunning && (maxItemsToConsume == 0 || itemsConsumed < maxItemsToConsume)) {
                
                // Attempt to retrieve item from queue (may block if queue is empty in the SharedQueue class)
                String item = sharedQueue.take();
                synchronized (destinationContainer) {
                    destinationContainer.add(item);
                }
                itemsConsumed++;
            }
            System.out.println("Consumer [" + consumerName + "] finished consuming " + itemsConsumed + " items");
        } catch (InterruptedException e) {
            System.out.println("Consumer [" + consumerName + "] was interrupted");
            Thread.currentThread().interrupt(); 
        } catch (Exception e) { // for other generic exceptions
            System.err.println("Consumer [" + consumerName + "] encountered an error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the consumer from consuming more items
     * This method can be called to gracefully stop the consumer thread.
     * The current item being processed will complete, but no new items will be consumed.
     */

    public void stop() {
        isRunning = false;
        System.out.println("Consumer [" + consumerName + "] stop requested");
    }
      
    /**
     * Gets the destination container with consumed items
     * @return List containing all consumed items
     */

    public List<String> getDestinationContainer() {
        return destinationContainer;
    }
}
