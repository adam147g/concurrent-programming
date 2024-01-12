package lab2;

import java.util.ArrayList;

// Układ zdarzeń, który spowoduje zakleszczenie - na wait() i notify() wyłącznie

public class Main {
    public static void main(String[] args) {
        MyInt number = new MyInt();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Thread threadUp = new ThreadUp(number);
            threads.add(threadUp);
        }
        for (int i = 0; i < 1; i++) {
            Thread threadDown = new ThreadDown(number);
            threads.add(threadDown);
        }
        for (int i = 0; i < 5; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < 5; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


