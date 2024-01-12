package lab6.timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyTimer {
    public int finish = 10000;
    public int operations = 0;
    private long startRealTime;
    private long endRealTime;
    private boolean checked = false;
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
