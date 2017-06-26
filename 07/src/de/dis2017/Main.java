package de.dis2017;

import de.dis2017.data.db.CSVScanner;
import de.dis2017.data.db.ORM;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    private static ORM _orm;
    
    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        _orm = new ORM();
        extract();
    }
    
    private static void extract()
    {
        try {
            List<List<String>> csvEntries = CSVScanner.scan("sales.csv");
            // TODO extract date values, articleIDs, shopIDs
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
