import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a client.
 */
public class ClientThread extends Thread {
    private int _id;
    private int _lowerPageBound;
    private int _upperPageBound;
    private PersistenceManager _manager;
    
    /**
     * Initializes the thread.
     * @param id             the ID of this client
     * @param lowerPageBound smalles page ID that can be accessed by this client
     * @param upperPageBound highest page ID that can be accessed by this client
     */
    public ClientThread(int id, int lowerPageBound, int upperPageBound) {
        _id = id;
        _lowerPageBound = lowerPageBound;
        _upperPageBound = upperPageBound;
        _manager = PersistenceManager.getInstance();
    }
    
    /**
     * Runs the thread.
     */
    public void run() {
        while(true) {
            try {
                int taid = beginTransaction();
                int numberOfWrites = ThreadLocalRandom.current().nextInt(0, 10);
                for (int i = 0; i < numberOfWrites; ++i) {
                    write(taid);
                }
                commit(taid);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
    
    /**
     * Begins a transaction and returns the transaction ID.
     * @return new transaction ID
     */
    private int beginTransaction() throws InterruptedException {
        int taid = _manager.beginTransaction();
        Thread.sleep(2000);
        return taid;
    }
    
    /**
     * Commits the given transaction.
     * @param taid the transaction ID
     */
    private void commit(int taid) throws InterruptedException {
        _manager.commit(taid);
        Thread.sleep(2000);
    }
    
    /**
     * Performs a write.
     * @param taid the transaction ID used for this write
     */
    private void write(int taid) throws InterruptedException {
        int pageID = ThreadLocalRandom.current().nextInt(_lowerPageBound, _upperPageBound + 1);
        _manager.write(taid, pageID, "transaction " + taid + " of client " + _id);
        Thread.sleep(2000);
    }
}
