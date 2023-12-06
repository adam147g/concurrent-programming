package lab6.timer;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IntBuffer4Conditions implements IIntBuffer {
    public int value = 0;
    public int full = 20;
    public int empty = 0;
    public long seed = 150;
    public MyTimer myTimer = new MyTimer();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition otherProducerCondition = lock.newCondition();
    private final Condition otherConsumerCondition = lock.newCondition();
    private final Condition firstProducerCondition = lock.newCondition();
    private final Condition firstConsumerCondition = lock.newCondition();
    private boolean isFirstElementQueueProducerFull = false;
    private boolean isFirstElementQueueConsumerFull = false;

    @Override
    public void setStartTime() {
        myTimer.setStartRealTime();
    }

    @Override
    public long getFinalTime() {
        return myTimer.getFinalTime();
    }

    @Override
    public MyTimer getMyTimer() {
        return myTimer;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public void producent(int randomInt, int id) throws InterruptedException {
        lock.lock();
        if (!myTimer.isEnd()) {
            while (isFirstElementQueueProducerFull) {
                otherProducerCondition.await();
                if (myTimer.isEnd()) {
                    lock.unlock();
                    return;
                }
            }
            while (value + randomInt > full) {
                isFirstElementQueueProducerFull = true;
                firstProducerCondition.await();
                if (myTimer.isEnd()) {
                    lock.unlock();
                    return;
                }
            }
            myTimer.addOperation();
            value += randomInt;
//            System.out.println("Producent - " + id + " wartość: " + randomInt + " obecny: " + value);
//            System.out.println(myTimer.operations);
            isFirstElementQueueProducerFull = false;
            otherProducerCondition.signal();
            firstConsumerCondition.signal();
        }
        if (myTimer.isEnd()) {
            isFirstElementQueueProducerFull = false;
            otherProducerCondition.signalAll();
            firstConsumerCondition.signalAll();
            otherConsumerCondition.signalAll();
            firstProducerCondition.signalAll();
        }
        lock.unlock();
    }

    @Override
    public void consument(int randomInt, int id) throws InterruptedException {
        lock.lock();
        if (!myTimer.isEnd()) {
            while (isFirstElementQueueConsumerFull) {
                otherConsumerCondition.await();
                if (myTimer.isEnd()) {
                    lock.unlock();
                    return;
                }
            }
            while (value - randomInt < empty) {
                isFirstElementQueueConsumerFull = true;
                firstConsumerCondition.await();
                if (myTimer.isEnd()) {
                    lock.unlock();
                    return;
                }
            }
            myTimer.addOperation();
            value -= randomInt;
//            System.out.println("Konsument - " + id + " wartość: " + randomInt + " obecny: " + value);
//            System.out.println(myTimer.operations);
            isFirstElementQueueConsumerFull = false;
            otherConsumerCondition.signal();
            firstProducerCondition.signal();
        }
        if (myTimer.isEnd()) {
            isFirstElementQueueConsumerFull = false;
            otherConsumerCondition.signalAll();
            firstProducerCondition.signalAll();
            otherProducerCondition.signalAll();
            firstConsumerCondition.signalAll();
        }
        lock.unlock();
    }

    @Override
    public void setFullVal(int value) {
        full = value;
    }
}