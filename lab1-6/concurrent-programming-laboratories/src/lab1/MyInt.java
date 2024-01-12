package lab1;

public class MyInt {
    public int value = 0;

    public void producent() throws InterruptedException {
        value += 1;
    }

    public synchronized void consument() throws InterruptedException {
        value -= 1;
    }
}
