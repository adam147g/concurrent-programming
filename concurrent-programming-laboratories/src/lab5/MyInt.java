package lab5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyInt {
    public int value = 0;
    public int full = 20;
    public int empty = 0;
    public int numProducents = 4;
    public int numConsuments = 4;
    private final ReentrantLock producerLock = new ReentrantLock();
    private final ReentrantLock consumerLock = new ReentrantLock();
    private final ReentrantLock commonLock = new ReentrantLock();
    private final Condition condition = commonLock.newCondition();

    public void producent(int randomInt, int id) throws InterruptedException {
        try {
            producerLock.lock();
            commonLock.lock();

            while (value + randomInt > full) {
                condition.await();
            }
            value += randomInt;
            if (id == numProducents - 1) {
                System.out.println("\t SUPER Producent !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            condition.signal();
//            Thread.sleep(1000);
        } finally {
            producerLock.unlock();
            commonLock.unlock();
        }
    }

    public synchronized void consument(int randomInt, int id) throws InterruptedException {
        try {
            consumerLock.lock();
            commonLock.lock();
            while (value - randomInt < empty) {
                condition.await();
            }
            value -= randomInt;
            if (id == numConsuments - 1) {
                System.out.println("\t SUPER Konsument !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            condition.signal();
//            Thread.sleep(1000);
        } finally {
            consumerLock.unlock();
            commonLock.unlock();
        }
    }
}