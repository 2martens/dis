package de.dis2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Small helper class for menus.
 * <p>
 * First menu entries have to be created with addEntry. Afterwards the menu can be shown
 * on the console with show(). show() also returns the constant number of the selected menu entry.
 * <p>
 * Example:
 * Menu m = new Menu("Main menu");
 * m.addEntry("Work hard", 0);
 * m.addEntry("Rest", 1);
 * m.addEntry("Go home", 2);
 * int chosenOption = m.show();
 * <p>
 * This results in the following output on the console:
 * Main menu:
 * [1] Work hard
 * [2] Rest
 * [3] Go home
 * --
 */
class Menu {
    private String _title;
    private ArrayList<String>  _labels       = new ArrayList<>();
    private ArrayList<Integer> _returnValues = new ArrayList<>();
    
    /**
     * Initializes the menu object.
     *
     * @param title
     *         Title of the menu (e.g. "Main menu")
     */
    Menu(String title) {
        super();
        _title = title;
    }
    
    /**
     * Adds a new menu entry
     *
     * @param label
     *         Name of the entry
     * @param returnValue
     *         constant number which is returned upon selection this entry
     */
    void addEntry(String label, int returnValue) {
        _labels.add(label);
        _returnValues.add(returnValue);
    }
    
    /**
     * Shows the menu.
     *
     * @return The constant number of the selected menu entry.
     */
    int show() {
        int selection = -1;
        
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        
        while (selection == -1) {
            System.out.println(_title + ":");
            
            for (int i = 0; i < _labels.size(); ++i) {
                System.out.println("[" + (i + 1) + "] " + _labels.get(i));
            }
            
            System.out.print("-- ");
            try {
                selection = Integer.parseInt(stdin.readLine());
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
            
            if (selection < 1 || selection > _returnValues.size()) {
                System.err.println("Invalid input!");
                selection = -1;
            }
        }
        
        return _returnValues.get(selection - 1);
    }
}
