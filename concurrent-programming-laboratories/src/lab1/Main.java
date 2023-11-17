package lab1;

// Stwórz dwa wątki, z których jeden inkrementuje 1000 razy podaną liczbę, a drugi tę samą dekrementuje 100 razy
// Bez synchronizacji!!

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread myThread = new MyThread();
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(100);

        MyInt number = new MyInt();
        Thread threadUp = new ThreadUp(number);
        Thread threadDown = new ThreadDown(number);
        threadUp.start();
        threadDown.start();
        try {
            threadUp.join();
            threadDown.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(number.value);
    }
}


