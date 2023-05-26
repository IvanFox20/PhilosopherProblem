import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
    private static final int NUM_PHILOSOPHERS = 5; // Количество философов
    private static final int NUM_FORKS = 5; // Количество вилок

    public static void main(String[] args) {
        Semaphore[] forks = new Semaphore[NUM_FORKS];

        for (int i = 0; i < NUM_FORKS; i++) {
            forks[i] = new Semaphore(1); // Инициализация каждой вилки
        }

        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Semaphore leftFork;
            Semaphore rightFork;

            // Изменяем порядок взятия вилок у нечетных философов
            if (i % 2 == 0) {
                leftFork = forks[i];
                rightFork = forks[(i + 1) % NUM_FORKS];
            } else {
                leftFork = forks[(i + 1) % NUM_FORKS];
                rightFork = forks[i];
            }

            philosophers[i] = new Philosopher(i, leftFork, rightFork);
            philosophers[i].start();
        }
    }
}

class Philosopher extends Thread {
    private int id;
    private Semaphore leftFork;
    private Semaphore rightFork;

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void think() throws InterruptedException {
        System.out.println("Философ " + id + " размышляет.");
        Thread.sleep(1000); // Философ размышляет
    }

    private void eat() throws InterruptedException {
        leftFork.acquire(); // Захват левой вилки
        System.out.println("Философ " + id + " взял левую вилку.");
        rightFork.acquire(); // Захват правой вилки
        System.out.println("Философ " + id + " взял правую вилку.");

        System.out.println("Философ " + id + " ест.");
        Thread.sleep(1000); // Философ ест

        leftFork.release(); // Освобождение левой вилки
        System.out.println("Философ " + id + " освободил левую вилку.");
        rightFork.release(); // Освобождение правой вилки
        System.out.println("Философ " + id + " освободил правую вилку.");
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            System.out.println("Философ " + id + " был прерван.");
        }
    }
}