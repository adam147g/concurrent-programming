package lab4.hasWaiters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyInt {
    public int value = 0;
    public int full = 20;
    public int empty = 0;
    public int numProducents = 4;
    public int numConsuments = 4;
    private final ReentrantLock lock = new ReentrantLock();

    private final Condition otherProducerCondition = lock.newCondition();
    private final Condition otherConsumerCondition = lock.newCondition();
    private final Condition firstProducerCondition = lock.newCondition();
    private final Condition firstConsumerCondition = lock.newCondition();

    public void producent(int randomInt, int id) throws InterruptedException {
        try {
            // bufor 19
            // P1(10) P2(8)
            lock.lock();
            while (hasWaiters(firstProducerCondition)) {
                otherProducerCondition.await(); //
            }
            while (value + randomInt > full) {
                firstProducerCondition.await(); //
            }
            value += randomInt;
            if (id == numProducents - 1) {
                System.out.println("\t SUPER Producent !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            otherProducerCondition.signal();
            firstConsumerCondition.signal();
//            Thread.sleep(1000);
        } finally {
            lock.unlock();
        }
    }

    public synchronized void consument(int randomInt, int id) throws InterruptedException {
        try {
            //
            lock.lock();
            while (hasWaiters(firstConsumerCondition)) {
                otherConsumerCondition.await(); //
            }
            while (value - randomInt < empty) {
                firstConsumerCondition.await(); // K1(4) K2(6)
            }
            value -= randomInt;
            if (id == numConsuments - 1) {
                System.out.println("\t SUPER Konsument !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            otherConsumerCondition.signal();
            firstProducerCondition.signal();
//            Thread.sleep(1000);
        } finally {
            lock.unlock();
        }
    }

    private boolean hasWaiters(Condition condition) {
        return lock.hasWaiters(condition);
    }
}