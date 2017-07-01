package de.dis2017;

import de.dis2017.data.db.ORM;

public class MainAnalysis {
    private static ORM _orm;
    
    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        _orm = new ORM();
        _orm.getSalesCrossTable(2017);
    }
}
