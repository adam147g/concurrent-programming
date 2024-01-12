package lab6.timer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String nazwaPliku = "dane.csv";
        String[] naglowki = {"buf_size", "producers_consmers", "max_portion", "operations", "3_locki Time", "3_locki CPU Time", "4_condition Time", "4_condition CPU Time"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazwaPliku))) {
            // Zapis nagłówków
            for (int i = 0; i < naglowki.length; i++) {
                writer.append(naglowki[i]);
                if (i < naglowki.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");
            int numberOfTests = 20;
            int startPortion = 50;
            int buf_size = 800;
            for (int startProdKons = 5; startProdKons <= 20; startProdKons += 15) {
                for (int operations = 10000; operations <= 10000000; operations *= 10) {
                    for (int i = 1; startPortion * i <= buf_size / 2; i++) {
                        int max_portion = startPortion * i;
                        long cpuTime3Locks = 0;
                        long realTime3Locks = 0;
                        long cpuTime4cond = 0;
                        long realTime4cond = 0;

                        for (int test = 0; test < numberOfTests; test++) {
                            for (int choose = 0; choose < 2; choose++) {
                                IIntBuffer number;
                                if (choose == 0) {
                                    number = new IntBuffer3Locks();
                                } else {
                                    number = new IntBuffer4Conditions();
                                }
                                number.setFullVal(buf_size);
                                number.getMyTimer().finish = operations;
                                ArrayList<Thread> threads = new ArrayList<>();
                                for (int j = 0; j < startProdKons; j++) {
                                    Thread threadProducent;
                                    threadProducent = new Producer(number, j, max_portion);
                                    threads.add(threadProducent);
                                }
                                for (int j = 0; j < startProdKons; j++) {
                                    Thread threadConsument;
                                    threadConsument = new Consumer(number, j, max_portion);
                                    threads.add(threadConsument);
                                }
                                number.setStartTime();
                                long currCpuTime3Locks = 0;
                                long currCpuTime4cond = 0;
                                for (int j = 0; j < startProdKons + startProdKons; j++) {
                                    threads.get(j).start();
                                }
                                for (int j = 0; j < startProdKons + startProdKons; j++) {
                                    try {
                                        threads.get(j).join();
                                        if (threads.get(j) instanceof Producer producer) {
                                            if (choose == 0) {
                                                currCpuTime3Locks += producer.getCPUTime();
                                            } else {
                                                currCpuTime4cond += producer.getCPUTime();
                                            }
                                        } else if (threads.get(j) instanceof Consumer consumer) {
                                            if (choose == 0) {
                                                currCpuTime3Locks += consumer.getCPUTime();
                                            } else {
                                                currCpuTime4cond += consumer.getCPUTime();
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (choose == 0) {
                                    realTime3Locks += number.getFinalTime();
                                    cpuTime3Locks += currCpuTime3Locks;
                                } else {
                                    realTime4cond += number.getFinalTime();
                                    cpuTime4cond += currCpuTime4cond;
                                }
                            }
                        }
                        realTime3Locks = realTime3Locks / numberOfTests;
                        cpuTime3Locks = cpuTime3Locks / numberOfTests;
                        realTime4cond = realTime4cond / numberOfTests;
                        cpuTime4cond = cpuTime4cond / numberOfTests;

                        int[] wiersz = {buf_size, startProdKons, max_portion, operations, (int) (realTime3Locks / 1000000), (int) (cpuTime3Locks / 1000000),
                                (int) (realTime4cond / 1000000), (int) (cpuTime4cond / 1000000)};

                        for (int k = 0; k < wiersz.length; k++) {
                            writer.append(Integer.toString(wiersz[k]));
                            if (k < wiersz.length - 1) {
                                writer.append(",");
                            }
                        }
                        writer.append("\n");
                        System.out.println("Dane zapisane do pliku: " + nazwaPliku + " operations " + operations + " time: " + (realTime3Locks * 20) / 1000000000.0 + "s , " + (realTime4cond * 20) / 1000000000.0 + "s");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}