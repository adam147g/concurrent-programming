package org.example;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.ArrayList;

public class Buffer implements CSProcess {
    private final int bufferIdx;
    private final int bufferMaxSize;
    private int current = 0;
    private final int producers;
    private final int consumers;

    private final ArrayList<One2OneChannelInt> bufferToProducer;
    private final ArrayList<One2OneChannelInt> producerToBuffer;
    private final ArrayList<One2OneChannelInt> bufferToConsumer;
    private final ArrayList<One2OneChannelInt> consumerToBuffer;
    private final One2OneChannelInt fromManager;

    public Buffer(int bufferIdx, int bufferMaxSize,
                  int producers, int consumers,
                  ArrayList<One2OneChannelInt> bufferToProducer,
                  ArrayList<One2OneChannelInt> bufferToConsumer,
                  One2OneChannelInt fromManager,
                  ArrayList<One2OneChannelInt> producerToBuffer,
                  ArrayList<One2OneChannelInt> consumerToBuffer) {
        this.bufferIdx = bufferIdx;
        this.bufferMaxSize = bufferMaxSize;
        this.producers = producers;
        this.consumers = consumers;
        this.bufferToProducer = bufferToProducer;
        this.bufferToConsumer = bufferToConsumer;
        this.fromManager = fromManager;
        this.producerToBuffer = producerToBuffer;
        this.consumerToBuffer = consumerToBuffer;
    }

    @Override
    public void run() {
        Guard[] producerGuards = new Guard[producers];
        Guard[] consumerGuards = new Guard[consumers];
        Guard[] managerGuards = new Guard[1];
        for (int i = 0; i < producers; i++) {
            producerGuards[i] = producerToBuffer.get(i).in();
        }
        for (int i = 0; i < consumers; i++) {
            consumerGuards[i] = consumerToBuffer.get(i).in();
        }
        managerGuards[0] = fromManager.in();

        Guard[] guards = new Guard[producers + consumers + 1];
        System.arraycopy(producerGuards, 0, guards, 0, producers);
        System.arraycopy(consumerGuards, 0, guards, producers, consumers);
        System.arraycopy(managerGuards, 0, guards, producers + consumers, 1);

        Alternative alt = new Alternative(guards);

        int item;
        while (true) {
            int index = alt.select();
            if (index < producers) {
                item = producerToBuffer.get(index).in().read();
                if (current + item <= bufferMaxSize) {
                    current += item;
                    bufferToProducer.get(index).out().write(1);
                } else {
                    bufferToProducer.get(index).out().write(-1);
                }

            } else if (index < producers + consumers) {
                int consumerIndex = index - producers;
                item = consumerToBuffer.get(consumerIndex).in().read();
                if (current + item >= 0) {
                    current += item;
                    bufferToConsumer.get(consumerIndex).out().write(1);
                } else {
                    bufferToConsumer.get(consumerIndex).out().write(-1);
                }
            } else {
                item = fromManager.in().read();
                if (item == -2) {
                    break;
                }
            }
        }
        System.out.println("Buffer " + bufferIdx + " ended with " + current + " portions.");
    }
}
