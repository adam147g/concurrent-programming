package org.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        final int producers = 5;
        final int consumers = 5;
        final int buffers = 5;
        final int bufferMaxSize = 20;

        // Kanały pomiędzy producentami a buforami
        ArrayList<ArrayList<One2OneChannelInt>> producerToBufferChannels = new ArrayList<>();
        ArrayList<ArrayList<One2OneChannelInt>> bufferToProducerChannels = new ArrayList<>();

        // Kanały pomiędzy konsumentami a buforami
        ArrayList<ArrayList<One2OneChannelInt>> consumerToBufferChannels = new ArrayList<>();
        ArrayList<ArrayList<One2OneChannelInt>> bufferToConsumerChannels = new ArrayList<>();

        // Kanały pomiędzy producentami a menadżerem
        ArrayList<One2OneChannelInt> producerToManagerChannels = new ArrayList<>();
        ArrayList<One2OneChannelInt> managerToProducerChannels = new ArrayList<>();

        // Kanały pomiędzy konsumentami a menadżerem
        ArrayList<One2OneChannelInt> consumerToManagerChannels = new ArrayList<>();
        ArrayList<One2OneChannelInt> managerToConsumerChannels = new ArrayList<>();

        // Kanały pomiędzy menadżerem a buforami
        ArrayList<One2OneChannelInt> managerToBufferChannels = new ArrayList<>();


        // Inicjalizacja kanałów
        for (int i = 0; i < producers; i++) {
            producerToBufferChannels.add(new ArrayList<>());
            bufferToProducerChannels.add(new ArrayList<>());
            for (int j = 0; j < buffers; j++) {
                producerToBufferChannels.get(i).add(Channel.one2oneInt());
                bufferToProducerChannels.get(i).add(Channel.one2oneInt());
            }
            producerToManagerChannels.add(Channel.one2oneInt());
            managerToProducerChannels.add(Channel.one2oneInt());
        }

        for (int i = 0; i < consumers; i++) {
            consumerToBufferChannels.add(new ArrayList<>());
            bufferToConsumerChannels.add(new ArrayList<>());
            for (int j = 0; j < buffers; j++) {
                consumerToBufferChannels.get(i).add(Channel.one2oneInt());
                bufferToConsumerChannels.get(i).add(Channel.one2oneInt());
            }
            consumerToManagerChannels.add(Channel.one2oneInt());
            managerToConsumerChannels.add(Channel.one2oneInt());
        }

        // Inicjalizacja kanałów menadżera-bufor
        for (int i = 0; i < buffers; i++) {
            managerToBufferChannels.add(Channel.one2oneInt());
        }

        ArrayList<CSProcess> procList = new ArrayList<>();

        // Dodanie menadżera
        procList.add(new Manager(producers, consumers, buffers, bufferMaxSize,
                producerToManagerChannels, managerToProducerChannels,
                consumerToManagerChannels, managerToConsumerChannels,
                managerToBufferChannels));

        // Dodanie buforów
        ArrayList<One2OneChannelInt> concreteBufferToProducerChannels;
        ArrayList<One2OneChannelInt> consumerToConcreteBufferChannels;
        ArrayList<One2OneChannelInt> concreteBufferToConsumerChannels;
        ArrayList<One2OneChannelInt> producerToConcreteBufferChannels;

        for (int i = 0; i < buffers; i++) {
            producerToConcreteBufferChannels = new ArrayList<>();
            concreteBufferToProducerChannels = new ArrayList<>();
            consumerToConcreteBufferChannels = new ArrayList<>();
            concreteBufferToConsumerChannels = new ArrayList<>();

            for (int j = 0; j < producers; j++) {
                concreteBufferToProducerChannels.add(bufferToProducerChannels.get(j).get(i));
                producerToConcreteBufferChannels.add(producerToBufferChannels.get(j).get(i));
            }

            for (int j = 0; j < consumers; j++) {
                concreteBufferToConsumerChannels.add(bufferToConsumerChannels.get(j).get(i));
                consumerToConcreteBufferChannels.add(consumerToBufferChannels.get(j).get(i));
            }

            procList.add(new Buffer(i, bufferMaxSize, producers, consumers,
                    concreteBufferToProducerChannels, concreteBufferToConsumerChannels, managerToBufferChannels.get(i),
                    producerToConcreteBufferChannels, consumerToConcreteBufferChannels
            ));

        }

        // Dodanie producentów
        for (int i = 0; i < producers; i++) {
            procList.add(new Producer(i, bufferMaxSize / 5, producerToManagerChannels.get(i),
                    managerToProducerChannels.get(i), bufferToProducerChannels.get(i), producerToBufferChannels.get(i)));
        }

        for (int i = 0; i < consumers; i++) {
            procList.add(new Consumer(i, bufferMaxSize / 5, consumerToManagerChannels.get(i),
                    managerToConsumerChannels.get(i), bufferToConsumerChannels.get(i), consumerToBufferChannels.get(i)));
        }

        producerToBufferChannels.clear();
        bufferToProducerChannels.clear();
        consumerToBufferChannels.clear();
        bufferToConsumerChannels.clear();

        CSProcess[] procArray = procList.toArray(new CSProcess[0]);
        Parallel par = new Parallel(procArray);
        par.run();
    }
}