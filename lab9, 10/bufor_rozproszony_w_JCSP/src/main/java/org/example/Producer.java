package org.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Producer implements CSProcess {
    private final int myIdx;
    private final int maxPortion;
    private final One2OneChannelInt toManager;
    private final One2OneChannelInt fromManager;
    private final ArrayList<One2OneChannelInt> fromBuffer;
    private final ArrayList<One2OneChannelInt> toBuffer;


    public Producer(int myIdx, int maxPortion, One2OneChannelInt toManager, One2OneChannelInt fromManager,
                    ArrayList<One2OneChannelInt> fromBuffer, ArrayList<One2OneChannelInt> toBuffer) {
        this.myIdx = myIdx;
        this.maxPortion = maxPortion;
        this.toManager = toManager;
        this.fromManager = fromManager;
        this.fromBuffer = fromBuffer;
        this.toBuffer = toBuffer;
    }

    @Override
    public void run() {
        int item;
        int bufferIdx;
        for (int k = 0; k < 100; k++) {
            item = (int) (Math.random() * maxPortion) + 1;
            toManager.out().write(item);
            bufferIdx = fromManager.in().read();
            if (bufferIdx >= 0) {
                int result = -1;
                while (result == -1) {
                    toBuffer.get(bufferIdx).out().write(item);
                    result = fromBuffer.get(bufferIdx).in().read();
                }
            }
        }
        toManager.out().write(-1);
        System.out.println("Producer " + myIdx + " ended.");
    }
}
