package de.dis2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Small helper class for forms.
 */
class FormUtil {
    /**
     * Reads a string from the console.
     *
     * @param label Label that is shown before the input
     * @return read string
     */
    static String readString(String label) {
        return readString(label, "");
    }
    
    /**
	 * Reads a string from the console.
	 *
	 * @param label Label that is shown before the input
     * @param defaultValue the default value in case an empty input is provided
	 * @return read string
	 */
	static String readString(String label, String defaultValue) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
			ret = ret.isEmpty() ? defaultValue : ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
    
    /**
     * Reads a password from the console.
     *
     * @return the entered password
     */
	static String readPassword() {
	    String password;
        System.out.print("Password: ");
        password = String.valueOf(System.console().readPassword());
        return password;
    }
	/**
     * Reads a password from the console.
     *
     * @return the entered password
     */
	static String readPassword(String oldPassword) {
	    String password = readPassword();
	    return password.isEmpty() ? oldPassword : password;
    }
    
    /**
     * Reads an integer from the console.
     *
     * @param label Label that is shown before the input
     * @return read integer
     */
    static int readInt(String label) {
        return readInt(label, -1);
    }
	
	/**
	 * Reads an integer from the console.
     *
	 * @param label Label that is shown before the input
     * @param defaultValue the default value
	 * @return read integer
	 */
	static int readInt(String label, int defaultValue) {
		int ret = 0;
		boolean finished = false;
		String defaultValueStr = defaultValue == -1 ? "" : String.valueOf(defaultValue);

		while(!finished) {
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
}
