package lab6.timer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IntBuffer3Locks implements IIntBuffer {
    public int value = 0;
    public int full = 20;
    public int empty = 0;
    private long seed = 150;
    public MyTimer myTimer = new MyTimer();
    private final ReentrantLock producerLock = new ReentrantLock();
    private final ReentrantLock consumerLock = new ReentrantLock();
    private final ReentrantLock commonLock = new ReentrantLock();
    private final Condition condition = commonLock.newCondition();

    @Override
    public long getSeed(){
        return seed;
    }
    @Override
    public MyTimer getMyTimer(){
        return myTimer;
    }
    @Override
    public void setStartTime(){
        myTimer.setStartRealTime();
    }

    @Override
    public long getFinalTime() {
        return myTimer.getFinalTime();
    }

    @Override
    public void producent(int randomInt, int id) throws InterruptedException {

            producerLock.lock();
            commonLock.lock();
            if (!myTimer.isEnd()){
                while (value + randomInt > full) {
                    condition.await();
                    if (myTimer.isEnd()){
                        condition.signalAll();
                        break;
                    }
                }
//                można
                if (!myTimer.isEnd()){
                    myTimer.addOperation();
                    value += randomInt;
//                    System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
                }
            }
            condition.signal();
            producerLock.unlock();
            commonLock.unlock();
    }

    @Override
    public void consument(int randomInt, int id) throws InterruptedException {
            consumerLock.lock();
            commonLock.lock();
            if (!myTimer.isEnd()) {
                while (value - randomInt < empty) {
                    condition.await();
                    if (myTimer.isEnd()){
                        condition.signalAll();
                        break;
                    }
                }
                if (!myTimer.isEnd()) {
                    myTimer.addOperation();
                    value -= randomInt;
//                    System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
                }
            }
            condition.signal();
            consumerLock.unlock();
            commonLock.unlock();

    }

    @Override
    public void setFullVal(int value) {
        full = value;
    }
}