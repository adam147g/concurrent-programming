package lab5;

public class Random {
    private final java.util.Random random;
    public Random(long seed) {
        this.random = new java.util.Random(seed);
    }
    public int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
