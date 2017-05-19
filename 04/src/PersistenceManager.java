import org.jetbrains.annotations.Contract;

/**
 * Persistence manager using the Singleton pattern.
 */
public class PersistenceManager
{
    private final static PersistenceManager instance;
    
    static {
        try {
            instance = new PersistenceManager();
        }
        catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * Returns an instance of the persistence manager.
     * @return instance
     */
    @Contract(pure = true)
    public static PersistenceManager getInstance()
    {
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
