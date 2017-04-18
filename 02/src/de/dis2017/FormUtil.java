package de.dis2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Small helper class for forms.
 */
public class FormUtil {
	/**
	 * Reads a string from the console.
	 *
	 * @param label Label that is shown before the input
	 * @return read string
	 */
	static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
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
	 * Reads an integer from the console.
     *
	 * @param label Label that is shown before the input
	 * @return read integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
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
