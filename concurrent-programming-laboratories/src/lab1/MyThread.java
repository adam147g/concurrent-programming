package lab1;

class MyThread extends Thread {
    public MyInt number;

    @Override
    public void run() {
        System.out.println("Uruchomiono");
    }
}