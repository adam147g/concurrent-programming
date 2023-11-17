package lab1;

public class ThreadDown extends Thread {
    public MyInt number;
    public ThreadDown(MyInt number_) {
        number = number_;
    }

    @Override
    public void run() {
        for (int i=0;i<1000;i++){
            try {
                number.consument();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}