package lab2;

public class MyInt {
    public int value = 0;

    public synchronized void producent() throws InterruptedException {
        while (value == 1) {
            System.out.println("producent - wait - " + value);
            this.wait();
        }
        value = 1;
        System.out.println("producent - notfy - " + value);
        this.notify();
    }

    public synchronized void consument() throws InterruptedException {
        while (value == 0) {
            System.out.println("consument  - wait - " + value);
            this.wait();
        }
        value = 0;
        System.out.println("consument - notfy - " + value);
        this.notify();
    }
}
