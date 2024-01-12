package lab4.ProducerConsumer_4_cond;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
public class TimesLists {
    private final List<Integer> producersWaitTimes;
    private final List<Integer> consumersWaitTimes;
    private final int finish = 10000;
    private int sum = 0;
    private int operations = 0;
    private long startRealTime;
    private long endRealTime;
    private boolean checked = false;

    public TimesLists(int numProducents, int numConsuments) {
        producersWaitTimes = new ArrayList<>(Collections.nCopies(numProducents, 0));
        consumersWaitTimes = new ArrayList<>(Collections.nCopies(numConsuments, 0));
    }

    public void setStartRealTime(){
        startRealTime = System.nanoTime();
    }
    public long getFinalTime(){
        if (startRealTime != 0 && endRealTime != 0) {
            return endRealTime - startRealTime;
        } else {
            throw new IllegalStateException("Timer not started or stopped properly.");
        }
    }


    public void addProducerTime(int id) {
        producersWaitTimes.set(id, producersWaitTimes.get(id) + 1);
        sum += 1;
    }

    public void resetProducerTime(int id) {
        sum -= producersWaitTimes.get(id);
        producersWaitTimes.set(id, 0);
    }

    public void addConsumerTime(int id) {
        producersWaitTimes.set(id, consumersWaitTimes.get(id) + 1);
        sum += 1;
    }

    public void resetConsumerTime(int id) {
        sum -= consumersWaitTimes.get(id);
        consumersWaitTimes.set(id, 0);
    }
    public void addOperation(){
        operations +=1 ;
    }

    public boolean isEnd(){
        if (operations >= finish){
            if (!checked){
                endRealTime = System.nanoTime();
                checked = true;
            }
        }
        return operations >= finish;
    }
}

