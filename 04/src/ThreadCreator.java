/**
 * Used for creating the clients.
 */
public class ThreadCreator {
    public static void main(String[] args) {
        Thread t1 = new ClientThread(1, 1, 10);
        Thread t2 = new ClientThread(2, 11, 20);
        Thread t3 = new ClientThread(3, 21, 30);
        Thread t4 = new ClientThread(4, 31, 40);
        Thread t5 = new ClientThread(5, 41, 50);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();
        t5.interrupt();
    }
}
