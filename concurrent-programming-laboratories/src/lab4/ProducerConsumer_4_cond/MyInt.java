package lab4.ProducerConsumer_4_cond;

import lab5.TimesLists;

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
    private TimesLists timesLists = new TimesLists(numProducents, numConsuments);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition otherProducerCondition = lock.newCondition();
    private final Condition otherConsumerCondition = lock.newCondition();
    private final Condition firstProducerCondition = lock.newCondition();
    private final Condition firstConsumerCondition = lock.newCondition();
    private boolean isFirstElementQueueProducerFull = false;
    private boolean isFirstElementQueueConsumerFull = false;
    public void setStartTime(){
        timesLists.setStartTime();
    }

    public void producent(int randomInt, int id) throws InterruptedException {
        try {
            lock.lock();
            while (isFirstElementQueueProducerFull) {
                timesLists.addProducerTime(id);
                otherProducerCondition.await();
            }

            while (value + randomInt > full) {
                isFirstElementQueueProducerFull = true;
                firstProducerCondition.await();
            }
//            timesLists.resetProducerTime(id);
            value += randomInt;
            if (id == numProducents - 1) {
                System.out.println("\t SUPER Producent !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            isFirstElementQueueProducerFull = false;
            otherProducerCondition.signal();
            firstConsumerCondition.signal();
//            Thread.sleep(1000);
        } finally {
            lock.unlock();
        }
    }

    public synchronized void consument(int randomInt, int id) throws InterruptedException {
        try {
            lock.lock();
            while (isFirstElementQueueConsumerFull) {
                timesLists.addConsumerTime(id);
                otherConsumerCondition.await();
            }

            while (value - randomInt < empty) {
                isFirstElementQueueConsumerFull = true;
                firstConsumerCondition.await();
            }
//            timesLists.resetConsumerTime(id);
            value -= randomInt;
            if (id == numConsuments - 1) {
                System.out.println("\t SUPER Konsument !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
            } else {
                System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
            }
            isFirstElementQueueConsumerFull = false;
            otherConsumerCondition.signal();
            firstProducerCondition.signal();
//            Thread.sleep(1000);
        } finally {
            lock.unlock();
        }
    }
}