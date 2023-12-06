package lab6.timer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        String nazwaPliku = "dane.csv";
        String[] naglowki = {"buf_size", "producers_consmers", "max_portion", "operations", "3_locki Time", "3_locki CPU"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazwaPliku))) {
            // Zapis nagłówków
            for (int i = 0; i < naglowki.length; i++) {
                writer.append(naglowki[i]);
                if (i < naglowki.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            int numberOfTests = 10;
            int startProdKons = 5;
            int startPortion = 50;
            int buf_size = 600;

            for (int i = 1; i <= 6; i++) {

                int numProducents = startProdKons * i;
                int numConsuments = startProdKons * i;


                int max_portion = startPortion * i;
                int operations = 100000;

                long cpuTime = 0;
                long realTime = 0;

                for (int test = 0; test < numberOfTests; test++) {
                    System.out.println("Test: " + (test + 1));
//                    IIntBuffer number = new IntBuffer3Locks();
                    IIntBuffer number = new IntBuffer4Conditions();
                    number.setFullVal(buf_size);
                    number.getMyTimer().finish = operations;
                    ArrayList<Thread> threads = new ArrayList<>();
                    for (int j = 0; j < numProducents; j++) {
                        Thread threadProducent;
                        threadProducent = new Producer(number, j, max_portion);
                        threads.add(threadProducent);
                    }
                    for (int j = 0; j < numConsuments; j++) {
                        Thread threadConsument;
                        threadConsument = new Consumer(number, j, max_portion);

                        threads.add(threadConsument);
                    }

                    number.setStartTime();
                    long currCpuTime = 0;

                    for (int j = 0; j < numConsuments + numProducents; j++) {
                        threads.get(j).start();
                    }

                    for (int j = 0; j < numConsuments + numProducents; j++) {
                        try {
                            threads.get(j).join();
                            if (threads.get(j) instanceof Producer producer) {
                                currCpuTime += producer.getCPUTime();
                            } else if (threads.get(j) instanceof Consumer consumer) {
                                currCpuTime += consumer.getCPUTime();
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    long currRealTime = number.getFinalTime();
//                    System.out.println("Realny czas wykonania funkcji: " + currRealTime / 1000000 + " milisekundy");
//                    System.out.println("Upłynął czas CPU: " + currCpuTime / 1000000 + " milisekundy");
                    realTime += currRealTime;
                    cpuTime += currCpuTime;
                }

                realTime = realTime / numberOfTests;
                cpuTime = cpuTime / numberOfTests;

                int[] wiersz = {buf_size, numConsuments, max_portion, operations, (int) (realTime / 1000000), (int) (cpuTime / 1000000)};

                for (int k = 0; k < wiersz.length; k++) {
                    writer.append(Integer.toString(wiersz[k]));
                    if (k < wiersz.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
                System.out.println("Dane zapisane do pliku: " + nazwaPliku);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}