package org.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

    static void runThreads() {
        try {
            int nThreads;
            int idStart;
            int idEnd;
            int balanceStart;
            int balanceEnd;

            System.out.println("Enter number of threads: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            nThreads = Integer.parseInt(br.readLine());
            System.out.println("Enter id range (a : b), a >= 1: \na: ");
            idStart = Integer.parseInt(br.readLine());
            System.out.println("b: ");
            idEnd = Integer.parseInt(br.readLine());
            System.out.println("Enter balance range (a : b): \na: ");
            balanceStart = Integer.parseInt(br.readLine());
            System.out.println("b: ");
            balanceEnd = Integer.parseInt(br.readLine());
            br.close();

            Thread thread[] = new Thread[nThreads];
            for (int j = idStart; j <= idEnd; j++) {
                for (int k = balanceStart; k <= balanceEnd; k++) {
                    for (int i = 0; i < nThreads; i++) {
                        thread[i] = new Thread(new AccountClient(j, k));
                        thread[i].start();
                    }
                    for (int i = 0; i < nThreads; i++) {
                        thread[i].join();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            int action;
            AccountClient accountClient = new AccountClient();
            System.out.println("1 - run as single thread client\n2 - run as multithreaded test");
            System.out.println("Enter action (1, 2): ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            action = Integer.parseInt(br.readLine());
            switch (action) {
                case 1:
                    accountClient.runAsClient();
                    break;
                case 2:
                    runThreads();
                    break;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
