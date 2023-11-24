package lab5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyInt {
    public int value = 0;
    public int full = 20;
    public int empty = 0;
    public int numProducents = 4;
    public int numConsuments = 4;
    public TimesLists timesLists = new TimesLists(numProducents, numConsuments);
    private final ReentrantLock producerLock = new ReentrantLock();
    private final ReentrantLock consumerLock = new ReentrantLock();
    private final ReentrantLock commonLock = new ReentrantLock();
    private final Condition condition = commonLock.newCondition();

    public void setStartTime(){
        timesLists.setStartTime();
    }
    public void producent(int randomInt, int id) throws InterruptedException {

        try {
            producerLock.lock();
            commonLock.lock();
            if (!timesLists.isEnd()){
                while (value + randomInt > full) {
                    condition.await();
                    if (!timesLists.isEnd()){
                        condition.signalAll();
                        break;
                    }
                }
                timesLists.addOperation();
                value += randomInt;
//                if (id == numProducents - 1) {
//                    System.out.println("\t SUPER Producent !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
//                } else {
//                    System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
//                }
                condition.signal();
            }
        } finally {
            producerLock.unlock();
            commonLock.unlock();
        }
    }

    public synchronized void consument(int randomInt, int id) throws InterruptedException {

        try {
            consumerLock.lock();
            commonLock.lock();
            if (!timesLists.isEnd()) {
                while (value - randomInt < empty) {
                    condition.await();
                    if (!timesLists.isEnd()){
                        condition.signalAll();
                        break;
                    }
                }
                timesLists.addOperation();
                value -= randomInt;
//                if (id == numConsuments - 1) {
//                    System.out.println("\t SUPER Konsument !!! - " + id + " wartość: " + randomInt + " obecny: " + value);
//                } else {
//                    System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
//                }
                condition.signal();
            }
        } finally {
            consumerLock.unlock();
            commonLock.unlock();
        }
    }
}