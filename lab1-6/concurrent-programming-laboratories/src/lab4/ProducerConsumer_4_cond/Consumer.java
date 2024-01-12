package lab4.ProducerConsumer_4_cond;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Consumer extends Thread {
    public MyInt number;
    private Random random;
    private int consume;
    private boolean haveConsumeDigit = false;
    private int id;

    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private long threadId = Thread.currentThread().getId();
    private long startCpuTime;
    private long endCpuTime;

    public Consumer(MyInt number, int id) {
        this.number = number;
        this.random = new Random(number.seed);
        this.id = id;
    }

    public Consumer(MyInt number, int consume, int id) {
        this.number = number;
        this.consume = consume;
        this.haveConsumeDigit = true;
        this.id = id;
    }

    @Override
    public void run() {
        startCpuTime = threadMXBean.getThreadCpuTime(threadId);
        while (!number.timesLists.isEnd()) {
        try {
                if (haveConsumeDigit) {
                    number.consument(consume, id);
                } else {
                    number.consument(random.randomInt(7,9), id);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        endCpuTime = threadMXBean.getThreadCpuTime(threadId);
    }

    public long getCPUTime(){
        if (startCpuTime != 0 && endCpuTime != 0) {
            return endCpuTime - startCpuTime;
        } else {
            throw new IllegalStateException("Timer not started or stopped properly.");
        }
    }
}