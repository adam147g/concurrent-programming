package lab4.ProducerConsumer_4_cond;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Producer extends Thread {
    public MyInt number;
    private Random random;
    private int produce;
    private boolean haveProduceDigit = false;
    private int id;

    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private long threadId = Thread.currentThread().getId();
    private long startCpuTime;
    private long endCpuTime;

    public Producer(MyInt number, int id) {
        this.number = number;
        this.random = new Random(number.seed);
        this.id = id;
    }

    public Producer(MyInt number, int produce, int id) {
        this.number = number;
        this.produce = produce;
        this.haveProduceDigit = true;
        this.id = id;
    }


    @Override
    public void run() {
        startCpuTime = threadMXBean.getThreadCpuTime(threadId);
        while (!number.timesLists.isEnd()) {
            try {
                if (haveProduceDigit) {
                    number.producent(produce, id);
                } else {
                    number.producent(random.randomInt(7,9), id);
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
