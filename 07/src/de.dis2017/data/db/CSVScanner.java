package de.dis2017.data.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVScanner {
    /**
     * Scans the sales.csv file.
     * @return a list of csv entries
     * @throws FileNotFoundException if file doesn't exist
     */
    public List<String> scan() throws FileNotFoundException {
        List<String> entries = new ArrayList<>();
        
        Scanner scanner = new Scanner(new File("sales.csv"));
        scanner.useDelimiter(";");
        while (scanner.hasNext()) {
            String current = scanner.next();
            entries.add(current);
        }
        scanner.close();
        
        return entries;
    }
}
