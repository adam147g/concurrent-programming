package lab5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimesLists {
    private final List<Integer> producersWaitTimes;
    private final List<Integer> consumersWaitTimes;
    private final int finish = 1000000;
    private int sum = 0;
    private int operations = 0;
    private long startTime;
    private long endTime;
    private boolean checked = false;

    public TimesLists(int numProducents, int numConsuments) {
        producersWaitTimes = new ArrayList<>(Collections.nCopies(numProducents, 0));
        consumersWaitTimes = new ArrayList<>(Collections.nCopies(numConsuments, 0));
    }

    public void setStartTime(){
        startTime = System.nanoTime();
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

    synchronized public boolean isEnd(){
        if (operations >= finish){
            if (!checked){
                endTime = System.nanoTime();

                long czasWykonania = (endTime - startTime) / 1_000_000;
                System.out.println("Czas wykonania funkcji: " + czasWykonania + " milisekundy");
                checked = true;
            }
        }
        return operations >= finish;
    }
}
