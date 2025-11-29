package com.buildingchallenge.assignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ProducerConsumerTest Class
 * 
 * Purpose: Comprehensive unit tests for Producer and Consumer classes.
 * Tests thread coordination, data integrity, and synchronization.
 */
@DisplayName("Producer-Consumer Integration Tests")
class ProducerConsumerTest {
    
    // Test fixtures
    private SharedQueue<String> sharedQueue;
    private List<String> sourceContainer;
    private List<String> destinationContainer;
    private static final int QUEUE_CAPACITY = 3;  

    /**
     * Set up test fixtures before each test method
     * This method is called before each test to ensure a clean intialization of the test cases.
     */
    @BeforeEach
    void setUp() {
        sharedQueue = new SharedQueue<>(QUEUE_CAPACITY);
        sourceContainer = new ArrayList<>();
        destinationContainer = new ArrayList<>();
    }
    
    /**
     * Test1: Producer should produce all items from source container.
     * 
     * Verifies that producer correctly processes all items from source container and pushes them onto the shared queue.
     * 
     * SOURCE CONTAINER == SHARED QUEUE
     */
    @Test
    @DisplayName("Producer should produce all items from source")
    void testProducerProducesAllItems() throws InterruptedException {
        // source data
        sourceContainer = Arrays.asList("Item-1", "Item-2", "Item-3");
        
        // Create and start producer
        Producer producer = new Producer(sharedQueue, sourceContainer, "TestProducer");
        Thread producerThread = new Thread(producer);
        
        producerThread.start();
        producerThread.join();
        
        // Verify all items were produced
        assertEquals(sourceContainer.size(), sharedQueue.size(), 
                    "Queue should contain all produced items");
    }
    
    /**
     * Test2: Consumer should consume all items from queue
     * 
     * Verifies that consumer correctly processes all items from the shared queue and stores them in the destination container.
     * 
     * SHARED QUEUE == DESTINATION CONTAINER
     */
    @Test
    @DisplayName("Consumer should consume all items from queue")
    void testConsumerConsumesAllItems() throws InterruptedException {
        // Pre-populate queue with items
        List<String> testItems = Arrays.asList("Item-1", "Item-2", "Item-3");
        for (String item : testItems) {
            sharedQueue.put(item);
        }
        
        // Create and start consumer
        Consumer consumer = new Consumer(sharedQueue, destinationContainer, 
                                       "TestConsumer", testItems.size());
        Thread consumerThread = new Thread(consumer);
        
        consumerThread.start();
        consumerThread.join();
        
        // Verify all items were consumed
        assertEquals(testItems.size(), destinationContainer.size(), 
                    "Destination should contain all consumed items");
        assertTrue(sharedQueue.isEmpty(), "Queue should be empty after consumption");
    }
    
    /**
     * Test3: Producer-Consumer should maintain data integrity
     * 
     * Verifies that all items produced are successfully consumed without loss.
     * 
     * SOURCE CONTAINER == SHARED QUEUE == DESTINATION CONTAINER
     */
    @Test
    @DisplayName("Producer-Consumer should maintain data integrity")
    void testDataIntegrity() throws InterruptedException {
        // Prepare source data
        sourceContainer = Arrays.asList("A", "B", "C", "D", "E");
        
        // Create producer and consumer
        Producer producer = new Producer(sharedQueue, sourceContainer, "Producer-1");
        Consumer consumer = new Consumer(sharedQueue, destinationContainer, 
                                       "Consumer-1", sourceContainer.size());
        
        // Create threads
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        
        // Start both threads
        producerThread.start();
        consumerThread.start();
        
        // Wait for both threads to complete
        producerThread.join();
        consumerThread.join();
        
        // Verify data integrity
        assertEquals(sourceContainer.size(), destinationContainer.size(), 
                    "All items should be consumed");
        
        // Verify all source items are in destination
        for (String sourceItem : sourceContainer) {
            assertTrue(destinationContainer.contains(sourceItem), 
                      "Destination should contain: " + sourceItem);
        }
    }
    
    /**
     * Test: Producer should handle null source container gracefully
     * 
     * Verifies that constructor properly validates input parameters.
     * 
     */
    @Test
    @DisplayName("Producer should reject null source container")
    void testProducerRejectsNullSourceContainer() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producer(sharedQueue, null, "Producer");
        }, "Should throw exception for null source container");
    }
    
    /**
     * Test: Consumer should handle null destination container gracefully
     * 
     * Verifies that constructor properly validates input parameters.
     */
    @Test
    @DisplayName("Consumer should reject null destination container")
    void testConsumerRejectsNullDestinationContainer() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Consumer(sharedQueue, null, "Consumer", 10);
        }, "Should throw exception for null destination container");
    }
    
    /**
     * Test: Producer-Consumer should handle empty source
     * 
     * Verifies that the system handles edge case of empty source container.
     * 
     * Note: Consumer is not tested here because with empty source,
     * consumer would block indefinitely waiting for items
     */
    @Test
    @DisplayName("Producer-Consumer should handle empty source")
    void testHandleEmptySource() throws InterruptedException {
        // Empty source container
        sourceContainer = new ArrayList<>();
        Producer producer = new Producer(sharedQueue, sourceContainer, "Producer");
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();
        assertTrue(sharedQueue.isEmpty(), 
                  "Queue should be empty when source is empty");
        assertEquals(0, sourceContainer.size(), 
                    "Source should have 0 items");
        

    }
    
    /**
     * Test: Multiple producers and consumers should work correctly
     * 
     * Verifies that multiple producer and consumer threads can work together.
     * 
     * SOURCE CONTAINER 1 == SHARED QUEUE == SOURCE CONTAINER 2 == SHARED QUEUE == DESTINATION CONTAINER
     */
    @Test
    @DisplayName("Multiple producers and consumers should work correctly")
    void testMultipleProducersConsumers() throws InterruptedException {

        List<String> source1 = Arrays.asList("P1-A", "P1-B");
        List<String> source2 = Arrays.asList("P2-A", "P2-B");
        
        Producer producer1 = new Producer(sharedQueue, source1, "Producer-1");
        Producer producer2 = new Producer(sharedQueue, source2, "Producer-2");
        
        // Create consumers
        Consumer consumer1 = new Consumer(sharedQueue, destinationContainer, 
                                        "Consumer-1", source1.size() + source2.size());
        
        // Create and start threads
        Thread p1Thread = new Thread(producer1);
        Thread p2Thread = new Thread(producer2);
        Thread c1Thread = new Thread(consumer1);
        
        p1Thread.start();
        p2Thread.start();
        c1Thread.start();
        
        p1Thread.join();
        p2Thread.join();
        c1Thread.join();
        
        // Verify all items were consumed
        assertEquals(source1.size() + source2.size(), destinationContainer.size(), 
                    "All items from both producers should be consumed");
    }
    
    /**
     * Test: Multiple consumers should correctly compete for single item using while loop
     * 
     * CRITICAL TEST: Demonstrates why we MUST use WHILE loop instead of IF statement
     * when using notifyAll() with multiple consumer threads.
     * 
     * SCENARIO:
     * - Queue starts EMPTY
     * - Multiple consumer threads (3) all call take() and go into wait state
     * - Producer adds ONE item and calls notifyAll()
     * - notifyAll() wakes ALL waiting consumer threads simultaneously
     * - All 3 consumers wake up and compete for the lock
     * - Only ONE consumer should successfully poll the item
     * - Other consumers must recheck the condition and wait again 
     * This test verifies that:
     * - Multiple consumers can safely wait for items
     * - When one item is added, only one consumer gets it
     * - Other consumers correctly re-check and wait again
     * - No items are lost or duplicated
     * 
     * SDLC Phase: Testing
     */
}