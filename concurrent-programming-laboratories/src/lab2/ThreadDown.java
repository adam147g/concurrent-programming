package lab2;

public class ThreadDown extends Thread {
    public MyInt number;
    public ThreadDown(MyInt number_) {
        number = number_;
    }



    @Override
    public void run() {
        while (true){
            try {
                number.consument();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}