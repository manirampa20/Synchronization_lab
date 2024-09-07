import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DeadlockExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    // This method creates a potential deadlock scenario.
    public void method1() {
        lock1.lock();
        System.out.println("Thread 1: Acquired lock1...");
        try {
            Thread.sleep(100); // Simulate some work
            lock2.lock();
            try {
                System.out.println("Thread 1: Acquired lock2...");
            } finally {
                lock2.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
    }

    public void method2() {
        lock2.lock();
        System.out.println("Thread 2: Acquired lock2...");
        try {
            Thread.sleep(100); // Simulate some work
            lock1.lock();
            try {
                System.out.println("Thread 2: Acquired lock1...");
            } finally {
                lock1.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock2.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadlockExample example = new DeadlockExample();

        Thread t1 = new Thread(example::method1);
        Thread t2 = new Thread(example::method2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
