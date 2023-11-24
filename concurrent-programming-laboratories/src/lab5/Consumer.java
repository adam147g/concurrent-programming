package lab5;

public class Consumer extends Thread {
    public MyInt number;
    private Random random;
    private int consume;
    private boolean haveConsumeDigit = false;
    private int id;

    public Consumer(MyInt number, int id) {
        this.number = number;
        this.random = new Random();
        this.id = id;
    }

    public Consumer(MyInt number, int consume, int id) {
        this.number = number;
        this.consume = consume;
        this.haveConsumeDigit = true;
        this.id = id;
    }

    @Override
    public void run() {
        while (!number.timesLists.isEnd()) {
            try {
                if (haveConsumeDigit) {
                    number.consument(consume, id);
                } else {
                    number.consument(random.randomInt(1, 5), id);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}