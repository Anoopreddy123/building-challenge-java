package com.buildingchallenge.assignment1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * SharedQueue Class
 * Purpose: Implements a thread-safe blocking queue for the producer-consumer pattern.
 * This class provides synchronization mechanisms to ensure safe concurrent access
 * to a shared data structure between producer and consumer threads.
 */

public class SharedQueue<T> {
    private final int maxCapacity;
    
    // Local queue to store items in the SharedQueue class
    private final Queue<T> queue;
    
    /**
     * Constructor - Initializes the shared queue with a specified capacity
     * @param maxCapacity Maximum number of items the queue can hold
     * @throws IllegalArgumentException if maxCapacity is less than 1
     */
    public SharedQueue(int maxCapacity) {
        if (maxCapacity < 1) {
            throw new IllegalArgumentException("Queue capacity must be at least 1");
        }
        this.maxCapacity = maxCapacity;
        this.queue = new LinkedList<>();
    }
    
    /**
     * Adds an item to the queue. Blocks if the queue is full.
     * 
     * Thread Safety: This method uses synchronized block to ensure atomic operations.
     * If queue is full, the thread waits until space becomes available.
     * 
     * @param item The item to be added to the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public synchronized void put(T item) throws InterruptedException {
        /**
         *  We use WHILE loop, NOT IF statement!!!
         * Scenario: Multiple producers waiting, one consumer removes item
         * - Queue is FULL
         * - Producer-1, Producer-2, Producer-3 all call put() and wait
         * - Consumer removes ONE item and calls notifyAll()
         * - notifyAll() wakes ALL 3 producers simultaneously
         * 
         *   // After notifyAll(), all 3 producers wake up
         *   // Producer-1: rechecks, queue has space, exits while, adds item 
         *   // Producer-2: rechecks, queue is NOW FULL, waits again 
         *   // Producer-3: rechecks, queue is NOW FULL, waits again 
         */

        while (queue.size() >= maxCapacity) {
            System.out.println("Queue is full. Producer waiting...");
            wait(); 
        }
        
        // Add item to queue
        queue.offer(item);
        System.out.println("Produced: " + item + " | Queue size: " + queue.size());
        
        // Notify any waiting consumer threads that an item is available
        notifyAll();
    }
    
    /**
     * Removes and returns an item from the queue. Blocks if the queue is empty.
     * 
     * Thread Safety: This method uses synchronized block to ensure atomic operations.
     * If queue is empty, the thread waits until an item becomes available.
     * 
     * @return The item removed from the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     */

    public synchronized T take() throws InterruptedException {
        /**
         * IMPORTANT: We use WHILE loop, NOT IF statement
         * 
         * WHY WHILE LOOP IS ESSENTIAL WITH notifyAll():
         * 
         * Scenario: Multiple consumers waiting, one producer adds item
         * - Queue is EMPTY
         * - Consumer-1, Consumer-2, Consumer-3 all call take() and wait
         * - Producer adds ONE item and calls notifyAll()
         * - notifyAll() wakes ALL 3 consumers simultaneously
         *
         *   // After notifyAll(), all 3 consumers wake up
         *   // Consumer-1: rechecks, queue has item, exits while, polls item
         *   // Consumer-2: rechecks, queue is NOW EMPTY, waits again
         *   // Consumer-3: rechecks, queue is NOW EMPTY, waits again
         */
        while (queue.isEmpty()) {
            System.out.println("Queue is empty. Consumer waiting...");
            wait(); // Releases the lock and waits for notification
        }
        
        T item = queue.poll();
        System.out.println("Consumed: " + item + " | Queue size: " + queue.size());
        
        // Notify any waiting producer threads that space is available
        notifyAll();
        
        return item;
    }
    
    // Below methods are declared for using in the tests [Edge cases].


    /**
     * Returns the current size of the queue
     * Thread Safety: Synchronized to provide consistent view of queue size
     * @return Current number of items in the queue
     */

    public synchronized int size() {
        return queue.size();
    }
    
    /**
     * Checks if the queue is empty
     * Thread Safety: Synchronized to provide consistent state
     * @return true if queue is empty, false otherwise
     */

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    /**
     * Checks if the queue is full 
     * Thread Safety: Synchronized to provide consistent state  
     * @return true if queue is at maximum capacity, false otherwise
     */

    public synchronized boolean isFull() {
        return queue.size() >= maxCapacity;
    }
}


