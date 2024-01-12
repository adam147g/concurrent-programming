package lab5;

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
    private int max_portion;

    public Consumer(MyInt number, int id, int max_portion) {
        this.number = number;
        this.random = new Random(number.seed);
        this.id = id;
        this.max_portion = max_portion;
    }

    public Consumer(MyInt number, int consume, int id, int max_portion) {
        this.number = number;
        this.consume = consume;
        this.haveConsumeDigit = true;
        this.id = id;
        this.max_portion = max_portion;
    }

    @Override
    public void run() {
        startCpuTime = threadMXBean.getThreadCpuTime(threadId);
        while (!number.timesLists.isEnd()) {
            try {
                if (haveConsumeDigit) {
                    number.consument(consume, id);
                } else {
                    number.consument(random.randomInt(1, max_portion), id);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        endCpuTime = threadMXBean.getThreadCpuTime(threadId);
    }

    public long getCPUTime(){
        if (startCpuTime != 0 && endCpuTime != 0) {
            return endCpuTime-startCpuTime;
        } else {
            throw new IllegalStateException("Timer not started or stopped properly.");
        }
    }
}