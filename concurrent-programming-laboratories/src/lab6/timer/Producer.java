package lab6.timer;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Producer extends Thread {
    public IIntBuffer number;
    private Random random;
    private int produce;
    private boolean haveProduceDigit = false;
    private int id;
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private long threadId = Thread.currentThread().getId();
    private long startCpuTime;
    private long endCpuTime;
    private int max_portion;

    public Producer(IIntBuffer number, int id, int max_portion) {
        this.number = number;
        this.random = new Random(number.getSeed());
        this.id = id;
        this.max_portion = max_portion;
    }

    public Producer(IIntBuffer number, int produce, int id, int max_portion) {
        this.number = number;
        this.produce = produce;
        this.haveProduceDigit = true;
        this.id = id;
        this.max_portion = max_portion;
    }


    @Override
    public void run() {
        startCpuTime = threadMXBean.getThreadCpuTime(threadId);
        while (!number.getMyTimer().isEnd()) {
            try {
                if (haveProduceDigit) {
                    number.producent(produce, id);
                } else {
                    number.producent(random.randomInt(1,max_portion), id);
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
