package com.company;

/**
 * 核心思路是利用一个对象的prev变量进行判断
 * Created by Nancy on 2017/3/21.
 */
public class WaitAndNotify {
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

}

class ThreadA extends Thread {

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

class ThreadB extends Thread {

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

class ThreadC extends Thread {

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

class Method {

    volatile private char prev = 'C';//默认先打印A，所以设置为C

    synchronized void methodA() {
        try {
            while (prev != 'C') {
                wait();
            }

            System.out.print("A");

            prev = 'A';
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void methodB() {
        try {
            while (prev != 'A') {
                wait();
            }

            System.out.print("B");

            prev = 'B';
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void methodC() {
        try {
            while (prev != 'B') {
                wait();
            }

            System.out.println("C");

            prev = 'C';
            notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
