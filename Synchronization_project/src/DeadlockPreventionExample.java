import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DeadlockPreventionExample {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public void method1() {
        while (true) {
            try {
                // Try to acquire lock1
                if (lock1.tryLock()) {
                    System.out.println("Thread 1: Acquired lock1...");
                    Thread.sleep(100); // Simulate some work

                    // Try to acquire lock2
                    if (lock2.tryLock()) {
                        try {
                            System.out.println("Thread 1: Acquired lock2...");
                            // Perform operations here
                            break;  // Exit loop if both locks are acquired
                        } finally {
                            lock2.unlock();
                        }
                    }
                    // If lock2 is not available, release lock1 and retry
                    lock1.unlock();
                }

                // Sleep for a short time before retrying to avoid busy waiting
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void method2() {
        while (true) {
            try {
                // Try to acquire lock2
                if (lock2.tryLock()) {
                    System.out.println("Thread 2: Acquired lock2...");
                    Thread.sleep(100); // Simulate some work

                    // Try to acquire lock1
                    if (lock1.tryLock()) {
                        try {
                            System.out.println("Thread 2: Acquired lock1...");
                            // Perform operations here
                            break;  // Exit loop if both locks are acquired
                        } finally {
                            lock1.unlock();
                        }
                    }
                    // If lock1 is not available, release lock2 and retry
                    lock2.unlock();
                }

                // Sleep for a short time before retrying to avoid busy waiting
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadlockPreventionExample example = new DeadlockPreventionExample();

        Thread t1 = new Thread(example::method1);
        Thread t2 = new Thread(example::method2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
