import org.jetbrains.annotations.Contract;

import java.io.*;
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
    private              String                           _dataPath;
    
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
    static PersistenceManager getInstance() {
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
        _dataPath = "/home/jim/git-repos/dis/04/data/";
    }
    
    /**
     * Begins the transaction and returns the transaction ID.
     *
     * @return transaction ID
     */
    synchronized int beginTransaction() {
        log(_nextTransactionNumber, -1, "BOT", "");
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
    void commit(int taid) {
        if (!_transactions.containsKey(taid)) {
            throw new IllegalArgumentException("No transaction with given ID exists.");
        }
        // only perform commit actions if transaction isn't commited yet
        if (!_transactions.get(taid)) {
            log(taid, -1, "COMMIT", "");
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
    void write(int taid, int pageid, String data) {
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
        int lsn = log(taid, pageid, "WRITE", data);
        _pageLSN.put(pageid, lsn);
        // call persist
        persist();
    }
    
    /**
     * Performs the recovery actions.
     */
    synchronized void recovery() {
        try {
            BufferedReader reader     = new BufferedReader(new FileReader(_dataPath + "log.txt"));
            String         line;
            List<Integer>  winner_tas = new ArrayList<>();
            int            lsnMax     = 0;
            int            taidMax    = 0;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols[2].equals("COMMIT")) {
                    winner_tas.add(Integer.valueOf(cols[1]));
                }
                if (Integer.valueOf(cols[0]) >= lsnMax) {
                    lsnMax = Integer.valueOf(cols[0]);
                }
                if (Integer.valueOf(cols[1]) >= taidMax) {
                    taidMax = Integer.valueOf(cols[1]);
                }
            }
            reader.close();
            _nextLogSequenceNumber = lsnMax + 1;
            _nextTransactionNumber = taidMax + 1;
    
            reader = new BufferedReader(new FileReader(_dataPath + "log.txt"));
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                if (!cols[2].equals("WRITE") || !winner_tas.contains(Integer.valueOf(cols[1]))) {
                    continue;
                }
    
                int    lsn    = Integer.valueOf(cols[0]);
                int    pageID = Integer.valueOf(cols[3]);
                String data   = cols[4];
                int pageLSN = -1;
                try {
                    BufferedReader readPage = new BufferedReader(new FileReader(_dataPath + pageID + ".txt"));
                    String         pageLine = readPage.readLine();
                    readPage.close();
                    String[] pageCols = pageLine.split(",");
                    pageLSN = Integer.parseInt(pageCols[1]);
                }
                catch (FileNotFoundException fne) {
                    //;
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                if (pageLSN >= lsn) {
                    continue;
                }
    
                FileWriter writer = new FileWriter(_dataPath + pageID + ".txt");
                writer.write("" + pageID + "," + lsn + "," + data + "\n");
                writer.close();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Logs to the log file.
     *
     * @param taid
     *         affected transaction
     * @param pageid
     *         modified page or -1 if not relevant
     * @param type
     *         log type
     * @param data
     *         written data or empty string if not relevant
     * @return log sequence number
     */
    private synchronized int log(int taid, int pageid, String type, String data) {
        try {
            FileWriter fw = new FileWriter(_dataPath + "log.txt", true);
            fw.write("" + _nextLogSequenceNumber + "," + taid + "," + type + "," + pageid + "," +  data);
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
    private synchronized void persist() {
        if (_pageBuffer.size() <= 50) {
            return;
        }
        
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
            Set<Integer> pageIDs = _transactionPageBuffer.get(taid);
            for (int pageid : pageIDs) {
                int lsn = _pageLSN.get(pageid);
                try {
                    FileWriter fw = new FileWriter(_dataPath + pageid + ".txt", false);
                    fw.write(pageid + "," + lsn + "," + _pageBuffer.get(pageid));
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                _pageLSN.remove(pageid);
                _pageBuffer.remove(pageid);
            }
            _transactionPageBuffer.remove(taid);
            _transactions.remove(taid);
        }
    }
}
