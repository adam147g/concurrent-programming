package lab4.ProducerConsumer_4_cond;

import java.util.ArrayList;

//Rozwiązanie problemu Producenta/Konsumenta na 4 condition


public class Main4Cond {
    public static void main(String[] args) throws InterruptedException {
        MyInt number = new MyInt();
        ArrayList<Thread> threads = new ArrayList<>();
        int numProducents = number.numProducents;
        int numConsuments = number.numConsuments;
        for (int i = 0; i < numProducents; i++) {
            Thread threadProducent;
            if (i >= numProducents - 5) {
                threadProducent = new Producer(number, 2, i);
            } else {
                threadProducent = new Producer(number, i);
            }
            threads.add(threadProducent);
        }
        for (int i = 0; i < numConsuments; i++) {
            Thread threadConsument;
            if (i >= numConsuments - 5) {
                threadConsument = new Consumer(number, 2, i);
            } else {
                threadConsument = new Consumer(number, i);
            }
            threads.add(threadConsument);
        }

        number.setStartTime();
        long cpuTime = 0;

        for (int i = 0; i < numConsuments + numProducents; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < numConsuments + numProducents; i++) {
            try {
                threads.get(i).join();
                if (threads.get(i) instanceof Producer) {
                    Producer producer = (Producer) threads.get(i);
                    cpuTime += producer.getCPUTime();
                } else if (threads.get(i) instanceof Consumer) {
                    Consumer consumer = (Consumer) threads.get(i);
                    cpuTime += consumer.getCPUTime();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long realTime = number.getFinalTime() / 1_000_000;
        System.out.println("Realny czas wykonania funkcji: " + realTime + " milisekundy");
        System.out.println("Upłynął czas CPU: " + cpuTime/1000000.0 + " milisekundy");
    }
}
