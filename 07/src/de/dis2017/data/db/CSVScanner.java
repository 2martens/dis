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
    public List<List<String>> scan() throws FileNotFoundException {
        List<List<String>> entries = new ArrayList<>();
        
        Scanner scanner = new Scanner(new File("sales.csv"));
        while (scanner.hasNextLine()) {
            List<String> line = new ArrayList<>();
            String line_str = scanner.nextLine();
            Scanner line_scanner = new Scanner(line_str);
            line_scanner.useDelimiter(";");
            while (line_scanner.hasNext()) {
                line.add(line_scanner.next());
            }
            entries.add(line);
        }
        scanner.close();
        
        return entries;
    }
}
