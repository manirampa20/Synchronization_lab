import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LocksAndConditionExample {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isAvailable = false;

    public void produce() {
        lock.lock();
        try {
            while (isAvailable) {
                condition.await();
            }
            System.out.println("Produced item");
            isAvailable = true;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consume() {
        lock.lock();
        try {
            while (!isAvailable) {
                condition.await();
            }
            System.out.println("Consumed item");
            isAvailable = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        LocksAndConditionExample example = new LocksAndConditionExample();

        Thread producer = new Thread(example::produce);
        Thread consumer = new Thread(example::consume);

        producer.start();
        consumer.start();
    }
}
