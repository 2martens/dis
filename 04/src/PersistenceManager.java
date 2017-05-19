import org.jetbrains.annotations.Contract;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Persistence manager using the Singleton pattern.
 */
public class PersistenceManager {
    private final static PersistenceManager               instance;
    private              Hashtable<Integer, String>       _pageBuffer;
    private              Hashtable<Integer, Set<Integer>> _transactionPageBuffer;
    private              Hashtable<Integer, Boolean>      _transactions;
    private              Hashtable<Integer, Integer>      _pageLSN;
    private              int                              _nextTransactionNumber;
    private              int                              _nextLogSequenceNumber;
    
    static {
        try {
            instance = new PersistenceManager();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * Returns an instance of the persistence manager.
     *
     * @return instance
     */
    @Contract(pure = true)
    public static PersistenceManager getInstance() {
        return instance;
    }
    
    /**
     * private constructor
     */
    private PersistenceManager() {
        _pageBuffer = new Hashtable<>();
        _transactionPageBuffer = new Hashtable<>();
        _transactions = new Hashtable<>();
        _pageLSN = new Hashtable<>();
        _nextTransactionNumber = 1;
        _nextLogSequenceNumber = 1;
    }
    
    /**
     * Begins the transaction and returns the transaction ID.
     *
     * @return transaction ID
     */
    public int beginTransaction() {
        _transactions.put(_nextTransactionNumber, false);
        // return the next transaction number and increase it by one afterwards
        return _nextTransactionNumber++;
    }
    
    /**
     * Commits the transaction specified by given ID.
     *
     * @param taid
     *         transaction ID
     */
    public void commit(int taid) {
        if (!_transactions.containsKey(taid)) {
            throw new IllegalArgumentException("No transaction with given ID exists.");
        }
        // only perform commit actions if transaction isn't commited yet
        if (!_transactions.get(taid)) {
            _transactions.replace(taid, true);
        }
    }
    
    /**
     * Writes given data into given page in given transaction.
     *
     * @param taid
     *         transaction ID
     * @param pageid
     *         page ID
     * @param data
     *         data
     */
    public void write(int taid, int pageid, String data) {
        if (!_transactions.containsKey(taid)) {
            throw new IllegalArgumentException("No transaction with given ID exists.");
        }
        if (_transactions.get(taid)) {
            throw new IllegalArgumentException("Write operations cannot be performed for committed transactions.");
        }
        Set<Integer> pageIDs = _transactionPageBuffer.getOrDefault(taid, new HashSet<>());
        pageIDs.add(pageid);
        _transactionPageBuffer.put(taid, pageIDs);
        _pageBuffer.put(pageid, data);
        // log changes
        int lsn = log(taid, pageid, data);
        _pageLSN.put(pageid, lsn);
        // call persist
        persist();
    }
    
    /**
     * Logs the necessary information to the log file.
     *
     * @param taid
     *         affected transaction
     * @param pageid
     *         modified page
     * @param data
     *         written data
     * @return log sequence number
     */
    private int log(int taid, int pageid, String data) {
        try {
            FileWriter fw = new FileWriter("../data/log.txt", true);
            // TODO properly create redo data
            fw.write("" + _nextLogSequenceNumber + "," + taid + "," + pageid + "," + data);
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return _nextLogSequenceNumber++;
    }
    
    /**
     * Checks for full buffer and persists data of committed transactions to storage if buffer is full.
     */
    private void persist() {
        if (_pageBuffer.size() <= 5) {
            return;
        }
        
        // TODO persist the data of committed transactions
        Enumeration<Integer> transactionIDs       = _transactions.keys();
        List<Integer>        commitedTransactions = new ArrayList<>();
        while (transactionIDs.hasMoreElements()) {
            Integer taid      = transactionIDs.nextElement();
            boolean committed = _transactions.get(taid);
            if (committed) {
                commitedTransactions.add(taid);
            }
        }
        
        for (int taid : commitedTransactions) {
            // TODO persist data of committed transaction
            Set<Integer> pageIDs = _transactionPageBuffer.get(taid);
            for (int pageid : pageIDs) {
                // TODO persist data of pageID
                _pageBuffer.remove(pageid);
            }
            _transactionPageBuffer.remove(taid);
            _transactions.remove(taid);
        }
    }
}
