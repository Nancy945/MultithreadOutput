package com.company;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 与wait和notify的方法类似。
 * Created by Nancy on 2017/3/21.
 */
public class AwaitAndSignal {
    public static void main(String[] args) {
        Method method = new Method();
        for (int i = 0; i < 10; i++) {
            ThreadA threadA = new ThreadA(method);
            threadA.start();
            ThreadB threadB = new ThreadB(method);
            threadB.start();
            ThreadC threadC = new ThreadC(method);
            threadC.start();
        }


    }

    private static class ThreadA extends Thread {

        private Method method;

        ThreadA(Method method) {
            super();
            this.method = method;
        }

        @Override
        public void run() {
            method.methodA();
        }

    }

    private static class ThreadB extends Thread {

        private Method method;

        ThreadB(Method method) {
            super();
            this.method = method;
        }

        @Override
        public void run() {
            method.methodB();
        }

    }

    private static class ThreadC extends Thread {

        private Method method;

        ThreadC(Method method) {
            super();
            this.method = method;
        }

        @Override
        public void run() {
            method.methodC();
        }

    }

    private static class Method {
        private char prev = 'C';//默认先打印A，所以设置为C
        private ReentrantLock lock = new ReentrantLock();
        //定向唤醒，效率更高
        private Condition conditionA = lock.newCondition();
        private Condition conditionB = lock.newCondition();
        private Condition conditionC = lock.newCondition();

        void methodA() {
            try {
                lock.lock();
                while (prev != 'C') {
                    conditionA.await();
                }

                System.out.print("A");
                prev = 'A';
                conditionB.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }

        void methodB() {
            try {
                lock.lock();
                while (prev != 'A') {
                    conditionB.await();
                }

                System.out.print("B");
                prev = 'B';
                conditionC.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        void methodC() {
            try {
                lock.lock();
                while (prev != 'B') {
                    conditionC.await();
                }

                System.out.println("C");
                prev = 'C';
                conditionA.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


    }

}
