package lab6.timer;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Consumer extends Thread {
    public IIntBuffer number;
    private Random random;
    private int consume;
    private boolean haveConsumeDigit = false;
    private int id;
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private long threadId = Thread.currentThread().getId();
    private long endCpuTime;
    private int max_portion;

    public Consumer(IIntBuffer number, int id, int max_portion) {
        this.number = number;
        this.random = new Random(number.getSeed());
        this.id = id;
        this.max_portion = max_portion;
    }

    @Override
    public void run() {
        while (!number.getMyTimer().isEnd()) {
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
        if (endCpuTime != 0) {
            return endCpuTime;
        } else {
            throw new IllegalStateException("Timer not started or stopped properly.");
        }
    }
}