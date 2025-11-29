package com.buildingchallenge.assignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SharedQueue Unit Tests")
class SharedQueueTest {

    private SharedQueue<String> queue;
    private static final int TEST_CAPACITY = 5;

    @BeforeEach
    void setUp() {
        queue = new SharedQueue<>(TEST_CAPACITY);
    }

    /**
     * Purpose:
     * Proves take() blocks when the queue is empty and only returns after a put().
     *
     * How it works:
     * Start a consumer thread that calls take(). It should wait.
     * Then put one item from the test thread and verify the consumer receives it.
     */
    @Test
    @DisplayName("take blocks when empty and unblocks after put")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void takeBlocksThenUnblocks() throws Exception {
        AtomicBoolean taken = new AtomicBoolean(false);

        Thread consumer = new Thread(() -> {
            try {
                String v = queue.take();
                assertEquals("X", v);
                taken.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Consumer interrupted");
            }
        });

        consumer.start();
        sleep(100);

        // If take() is blocking correctly, it should not have taken anything yet
        assertFalse(taken.get());

        queue.put("X");
        consumer.join(1000);

        assertTrue(taken.get());
        assertTrue(queue.isEmpty());
    }

    /**
     * Purpose:
     * Proves put() blocks when the queue is full and only completes after a take().
     *
     * How it works:
     * Fill queue to capacity.
     * Start a producer thread that tries to put one more item. It should wait.
     * Take one item from the test thread, then verify the producer finishes.
     */
    @Test
    @DisplayName("put blocks when full and unblocks after take")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void putBlocksThenUnblocks() throws Exception {
        for (int i = 0; i < TEST_CAPACITY; i++) {
            queue.put("I" + i);
        }
        assertTrue(queue.isFull());

        AtomicBoolean putDone = new AtomicBoolean(false);

        Thread producer = new Thread(() -> {
            try {
                queue.put("X");
                putDone.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Producer interrupted");
            }
        });

        producer.start();
        sleep(100);

        // If put() is blocking correctly, it should not have completed yet
        assertFalse(putDone.get());

        queue.take(); // frees one slot so the blocked put() can proceed
        producer.join(1000);

        assertTrue(putDone.get());
        assertEquals(TEST_CAPACITY, queue.size());
    }

    /**
     * Purpose:
     * Verifies basic queue correctness: first in first out ordering.
     *
     * How it works:
     * Put three items in a known order and ensure take() returns them in that same order.
     */
    @Test
    @DisplayName("FIFO order")
    void fifoOrder() throws Exception {
        queue.put("A");
        queue.put("B");
        queue.put("C");

        assertEquals("A", queue.take());
        assertEquals("B", queue.take());
        assertEquals("C", queue.take());
        assertTrue(queue.isEmpty());
    }

    private static void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }
}