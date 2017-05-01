package de.dis2013.menu;

import java.util.Iterator;
import java.util.Set;

import de.dis2013.data.Haus;

/**
 * Ein kleines Menü, dass alle Häuser aus einem Set zur Auswahl anzeigt
 */
public class HouseSelectionMenu extends Menu {
	public static final int BACK = -1;
	
	public HouseSelectionMenu(String title, Set<Haus> haeuser) {
		super(title);
		
		for (Haus h : haeuser) {
			addEntry(h.getStrasse() + " " + h.getHausnummer() + ", " + h.getPlz() + " " + h.getOrt(), h.getId());
		}
		addEntry("Zurück", BACK);
	}
}
