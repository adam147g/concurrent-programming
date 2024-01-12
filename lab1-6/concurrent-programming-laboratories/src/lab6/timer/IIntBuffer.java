package lab6.timer;

public interface IIntBuffer {
    public void producent(int randomInt, int id) throws InterruptedException;
    public void consument(int randomInt, int id) throws InterruptedException;
    public void setFullVal(int value);
    public void setStartTime();
    public long getFinalTime();
    public MyTimer getMyTimer();
    public long getSeed();
}
