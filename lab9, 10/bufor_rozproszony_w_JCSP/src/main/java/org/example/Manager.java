package org.example;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;
import java.util.Collections;

public class Manager implements CSProcess {
    private final int producers;
    private final int consumers;
    private final int bufferMaxSize;
    private final ArrayList<Integer> buffersState;
    private final ArrayList<One2OneChannelInt> fromProducer;
    private final ArrayList<One2OneChannelInt> toProducer;
    private final ArrayList<One2OneChannelInt> fromConsumer;
    private final ArrayList<One2OneChannelInt> toConsumer;
    private final ArrayList<One2OneChannelInt> toBuffer;
    private int activeProducers;
    private int currentBufferIdx = 0;

    public Manager(int producers, int consumers, int buffers, int bufferMaxSize, ArrayList<One2OneChannelInt> fromProducer,
                   ArrayList<One2OneChannelInt> toProducer, ArrayList<One2OneChannelInt> fromConsumer,
                   ArrayList<One2OneChannelInt> toConsumer, ArrayList<One2OneChannelInt> toBuffer) {
        this.producers = producers;
        this.consumers = consumers;
        this.bufferMaxSize = bufferMaxSize;
        this.buffersState = new ArrayList<>(Collections.nCopies(buffers, 0));
        this.fromProducer = fromProducer;
        this.toProducer = toProducer;
        this.fromConsumer = fromConsumer;
        this.toConsumer = toConsumer;
        this.toBuffer = toBuffer;
        this.activeProducers = producers;
    }

    @Override
    public void run() {
        Guard[] producerGuards = new Guard[producers];
        Guard[] consumerGuards = new Guard[consumers];

        for (int i = 0; i < producers; i++) {
            producerGuards[i] = fromProducer.get(i).in();
        }
        for (int i = 0; i < consumers; i++) {
            consumerGuards[i] = fromConsumer.get(i).in();
        }

        Guard[] guards = new Guard[producers + consumers];
        System.arraycopy(producerGuards, 0, guards, 0, producers);
        System.arraycopy(consumerGuards, 0, guards, producers, consumers);

        Alternative alt = new Alternative(guards);

        while (activeProducers > 0) {
            int index = alt.select();
            if (index < producers) {
                handleProducer(index);
            } else {
                handleConsumer(index - producers);
            }
            currentBufferIdx = (currentBufferIdx + 1) % buffersState.size();
        }

        int consumerIndex;
        for (int i = 0; i < consumers; i++) {
            consumerIndex = alt.select();
            if (consumerIndex >= producers) {
                fromConsumer.get(consumerIndex - producers).in().read();
                toConsumer.get(consumerIndex - producers).out().write(-2);
            }
        }

        for (int i = 0; i < buffersState.size(); i++) {
            toBuffer.get(i).out().write(-2);
        }

        System.out.println("Manager ended.");
    }


    private void handleProducer(int index) {
        int item = fromProducer.get(index).in().read();
        if (item < 0) {
            activeProducers -= 1;
        } else {
            boolean written = false;
            for (int i = 0; i < buffersState.size(); i++) {
                if (buffersState.get(currentBufferIdx) + item <= bufferMaxSize) {
                    buffersState.set(currentBufferIdx, buffersState.get(currentBufferIdx) + item);
                    toProducer.get(index).out().write(currentBufferIdx);
                    written = true;
                    break;
                } else {
                    currentBufferIdx = (currentBufferIdx + 1) % buffersState.size();
                }
            }
            if (!written) {
                toProducer.get(index).out().write(-1);
            }
        }
    }

    private void handleConsumer(int index) {
        int item = fromConsumer.get(index).in().read();
        boolean written = false;
        for (int i = 0; i < buffersState.size(); i++) {
            if (buffersState.get(currentBufferIdx) - item >= 0) {
                buffersState.set(currentBufferIdx, buffersState.get(currentBufferIdx) - item);
                toConsumer.get(index).out().write(currentBufferIdx);
                written = true;
                break;
            } else {
                currentBufferIdx = (currentBufferIdx + 1) % buffersState.size();
            }
        }
        if (!written) {
            toConsumer.get(index).out().write(-1);
        }
    }
}
