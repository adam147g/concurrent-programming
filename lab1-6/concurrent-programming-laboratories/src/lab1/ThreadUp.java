package lab1;

public class ThreadUp extends Thread {
    public MyInt number;

    public ThreadUp(MyInt number_) {
        number = number_;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            try {
                number.producent();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}