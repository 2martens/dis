package de.dis2013.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Small helper class for forms.
 */
public class FormUtil {
    /**
     * Reads a string from the console.
     *
     * @param label
     *         Label that is shown before the input
     * @return read string
     */
    public static String readString(String label) {
        return readString(label, "");
    }
    
    /**
     * Reads a string from the console.
     *
     * @param label
     *         Label that is shown before the input
     * @param defaultValue
     *         the default value in case an empty input is provided
     * @return read string
     */
    public static String readString(String label, String defaultValue) {
        String         ret   = null;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.print(label + (!defaultValue.isEmpty() ? "[" + defaultValue + "]" : "") + ": ");
            ret = stdin.readLine();
            ret = ret.isEmpty() ? defaultValue : ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    /**
     * Zeigt eine Nachricht an und wartet auf Bestätigung des Benutzers
     *
     * @param msg Nachricht
     */
    public static void showMessage(String msg) {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            System.out.print(msg);
            stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Reads a password from the console.
     *
     * @param label the label shown before the input
     * @return the entered password
     */
    public static String readPassword(String label) {
        String password  = "";
        System.out.print(label + ": ");
         if (System.console() != null) {
            password = String.valueOf(System.console().readPassword());
        } else {
            try {
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                password = stdin.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return password;
    }
    
    /**
     * Reads a password from the console.
     *
     * @param label the label shown before the input
     * @param oldPassword the old password
     * @return the entered password
     */
    public static String readPassword(String label, String oldPassword) {
        String password = readPassword(label);
        return password.isEmpty() ? oldPassword : password;
    }
    
    /**
     * Liest ein Datum vom standard input im Format dd.MM.yyyy ein
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return eingelesenes Datum
     */
    public static Date readDate(String label) {
        SimpleDateFormat parser   = new SimpleDateFormat("dd.MM.yyyy");
        Date             ret      = null;
        boolean          finished = false;
        
        while(!finished) {
            String line = readString(label);
            
            try {
                ret = parser.parse(line);
                finished = true;
            } catch (ParseException e) {
                System.err.println("Ungültige Eingabe: Bitte geben Sie ein Datum im Format dd.MM.yyyy an!");
            }
        }
        
        return ret;
    }
    
    /**
     * Reads an integer from the console.
     *
     * @param label
     *         Label that is shown before the input
     * @return read integer
     */
    public static int readInt(String label) {
        return readInt(label, -1);
    }
    
    /**
     * Reads an integer from the console.
     *
     * @param label
     *         Label that is shown before the input
     * @param defaultValue
     *         the default value
     * @return read integer
     */
    public static int readInt(String label, int defaultValue) {
        int     ret             = 0;
        boolean finished        = false;
        String  defaultValueStr = defaultValue == -1 ? "" : String.valueOf(defaultValue);
        
        while (!finished) {
            String line = readString(label, defaultValueStr);
            
            try {
                ret = Integer.parseInt(line);
                finished = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input: Please insert a valid number!");
            }
        }
        
        return ret;
    }
    
    /**
     * Stellt eine Ja/Nein-Frage und gibt das Ergebnis zurück
     *
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @return true, falls ja, false, falls nein
     */
    public static boolean readBoolean(String label) {
        label = label + "(j/n)";
        return readBooleanIntern(label, false, false);
    }
    
    /**
     * Stellt eine Ja/Nein-Frage und gibt das Ergebnis zurück
     *
     * @param label Zeile, die vor der Eingabe gezeigt wird
     * @param defaultValue der Standardwert
     * @return true, falls ja, false, falls nein
     */
    public static boolean readBoolean(String label, boolean defaultValue) {
        label = label + "(j/n) [" + (defaultValue ? "j" : "n") + "]";
        return readBooleanIntern(label, true, defaultValue);
    }
    
    private static boolean readBooleanIntern(String label, boolean withDefault, boolean defaultValue) {
        boolean finished = false;
        String line;
        boolean ret = false;
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    
        try {
            while(!finished) {
                System.out.print(label+": ");
                line = stdin.readLine().toLowerCase();
                switch (line) {
                    case "j":
                    case "ja":
                        ret = true;
                        finished = true;
                        break;
                    case "n":
                    case "nein":
                        ret = false;
                        finished = true;
                        break;
                    case "":
                        if (withDefault) {
                            ret = defaultValue;
                            finished = true;
                        }
                        else {
                            System.err.println("Bitte geben Sie ja oder nein bzw. j oder n ein!");
                        }
                        break;
                    default:
                        System.err.println("Bitte geben Sie ja oder nein bzw. j oder n ein!");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
}
