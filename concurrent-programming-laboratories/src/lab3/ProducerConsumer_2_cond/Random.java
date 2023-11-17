package lab3.ProducerConsumer_2_cond;

public class Random {
    public int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
