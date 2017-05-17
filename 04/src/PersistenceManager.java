/**
 * Persistence manager using the Singleton pattern.
 */
public class PersistenceManager
{
    private static PersistenceManager instance = null;
    
    /**
     * Returns an instance of the persistence manager.
     * @return instance
     */
    public static PersistenceManager getInstance()
    {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        
        return instance;
    }
    
    /**
     * private constructor
     */
    private PersistenceManager()
    {}
    
    /**
     * Begins the transaction and returns the transaction ID.
     * @return transaction ID
     */
    public int beginTransaction()
    {
        return 0;
    }
    
    /**
     * Commits the transaction specified by given ID.
     * @param taid transaction ID
     */
    public void commit(int taid)
    {
    
    }
    
    /**
     * Writes given data into given page in given transaction.
     * @param taid transaction ID
     * @param pageid page ID
     * @param data data
     */
    public void write(int taid, int pageid, String data)
    {
    
    }
}
