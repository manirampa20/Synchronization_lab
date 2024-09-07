import java.util.concurrent.atomic.AtomicInteger;

class AtomicExample {
    private AtomicInteger atomicCounter = new AtomicInteger(0);

    public void increment() {
        atomicCounter.incrementAndGet();
    }

    public int getValue() {
        return atomicCounter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicExample atomicExample = new AtomicExample();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                atomicExample.increment();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final count: " + atomicExample.getValue());
    }
}
