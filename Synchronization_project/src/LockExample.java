import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockExample {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void awaitCondition() {
        lock.lock();
        try {
            System.out.println("Waiting for condition...");
            condition.await(); // Wait until signaled
            System.out.println("Condition met, proceeding...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signalCondition() {
        lock.lock();
        try {
            System.out.println("Signaling condition...");
            condition.signal(); // Signal a waiting thread
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockExample lockExample = new LockExample();

        Thread t1 = new Thread(lockExample::awaitCondition);
        Thread t2 = new Thread(lockExample::signalCondition);

        t1.start();
        Thread.sleep(1000); // Simulate some work
        t2.start();

        t1.join();
        t2.join();
    }
}
