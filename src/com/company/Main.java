package com.company;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static int philosopher = 5;
    static philosopher philosophers[] = new philosopher[philosopher];
    static Fork forks[] = new Fork[philosopher];

    static class Fork {

        public Semaphore mutex = new Semaphore(1);

        void grab() {
            try {
                mutex.acquire();
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        void release() {
            mutex.release();
        }

        boolean isFree() {
            return mutex.availablePermits() > 0;
        }

    }

    static class philosopher extends Thread {

        public int number;
        public Fork leftFork;
        public Fork rightFork;

        philosopher(int num, Fork left, Fork right) {
            number = num;
            leftFork = left;
            rightFork = right;
        }

        public void run(){

            while (true) {
                leftFork.grab();
                System.out.println("philosopher " + (number+1) + " grabs left fork.");
                rightFork.grab();
                System.out.println("philosopher " + (number+1) + " grabs right fork.");
                eat();
                leftFork.release();
                System.out.println("philosopher " + (number+1) + " releases left fork.");
                rightFork.release();
                System.out.println("philosopher " + (number+1) + " releases right fork.");
            }
        }

        void eat() {
            try {
                int sleepTime = ThreadLocalRandom.current().nextInt(0, 1000);
                System.out.println("philosopher " + (number+1) + " eats for " + sleepTime);
                Thread.sleep(sleepTime);
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

    }

    public static void main(String argv[]) {

        for (int i = 0; i < philosopher; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < philosopher; i++) {
            philosophers[i] = new philosopher(i, forks[i], forks[(i + 1) % philosopher]);
            philosophers[i].start();
        }

        while (true) {
            try {
                // sleep 1 sec
                Thread.sleep(1000);

                // check for deadlock
                boolean deadlock = true;
                for (Fork f : forks) {
                    if (f.isFree()) {
                        deadlock = false;
                        break;
                    }
                }
                if (deadlock) {
                    Thread.sleep(1000);
                    System.out.println("Hurray! There is a deadlock!");
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        System.out.println("Exit The Program!");
        System.exit(0);
    }

}
