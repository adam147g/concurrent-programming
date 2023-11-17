package lab5;


public class Producer extends Thread {
    public MyInt number;
    private Random random;
    private int produce;
    private boolean haveProduceDigit = false;
    private int id;

    public Producer(MyInt number, int id) {
        this.number = number;
        this.random = new Random();
        this.id = id;
    }

    public Producer(MyInt number, int produce, int id) {
        this.number = number;
        this.produce = produce;
        this.haveProduceDigit = true;
        this.id = id;
    }


    @Override
    public void run() {
        while (true) {
            try {
                if (haveProduceDigit) {
                    number.producent(produce, id);
                } else {
                    number.producent(random.randomInt(1, 5), id);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
