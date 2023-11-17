package lab2;

public class ThreadUp extends Thread {
    public MyInt number;

    public ThreadUp(MyInt number_) {
        number = number_;
    }

    @Override
    public void run() {
        while (true) {
            try {
                number.producent();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}