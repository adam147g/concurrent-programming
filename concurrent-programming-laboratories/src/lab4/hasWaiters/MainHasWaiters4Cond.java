package lab4.hasWaiters;

import java.util.ArrayList;

//Rozwiązanie problemu Producenta/Konsumenta z użyciem funkcji hasWaiters()


public class MainHasWaiters4Cond {
    public static void main(String[] args) throws InterruptedException {
        MyInt number = new MyInt();
        ArrayList<Thread> threads = new ArrayList<>();
        int numProducents = number.numProducents;
        int numConsuments = number.numConsuments;
        for (int i = 0; i < numProducents; i++) {
            Thread threadProducent;
            if (i == numProducents - 1) {
                threadProducent = new Producer(number, 10, i);
            } else {
                threadProducent = new Producer(number, i);
            }
            threads.add(threadProducent);
        }
        for (int i = 0; i < numConsuments; i++) {
            Thread threadConsument;
            if (i == numConsuments - 1) {
                threadConsument = new Consumer(number, 10, i);
            } else {
                threadConsument = new Consumer(number, i);
            }
            threads.add(threadConsument);
        }
        for (int i = 0; i < numConsuments + numProducents; i++) {
            threads.get(i).start();
//            Thread.sleep(100);

        }
        for (int i = 0; i < numConsuments + numProducents; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
