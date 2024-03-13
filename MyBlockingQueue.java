
/**
 * Models a thread-safe Blocking Queue.
 *
 * @author Alex Richardson
 * @author Adrian Lopez
 * @author Devin Rollins
 */

import java.util.*;
import java.util.concurrent.Semaphore;

public class MyBlockingQueue<T> {
    private int maxElements;
    Semaphore semBoy = new Semaphore(maxElements);

    LinkedList<T> blockList = new LinkedList<T>();

    public MyBlockingQueue(int x) {
        this.maxElements = x;
    }

    // v - release - incremet semaphore
    // p - aquire - block until positive
    // Takes a generic parameter (element) and adds it to the back of the queue
    public synchronized void add(T element) {
        if (blockList.size() < maxElements) {
            try {
                semBoy.release(); // Grab a token from the semaphore
                blockList.add(element); // Then add it to the
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread Interrupted");
            }

        } else {
            System.out.println("Queue is full");
        }
    }

    // prints out the elements in the queue
    @Override
    public String toString() {
        String output = blockList.toString();
        return output;
    };

    // removes and returns the element at the front of the queue
    public synchronized T remove() {
        System.out.println(blockList.toString());
        try {
            semBoy.acquire();
            return blockList.remove();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread Interrupted");
            return null;
        }
    }

    // returns the number of elements currently stored in the queue
    public int getNumElements() {
        return blockList.size();
    }

    // returns the number of empty spots in the queue
    public int getFreeSpace() {
        return maxElements - blockList.size();
    }
};