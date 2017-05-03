package de.dis2013.core;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.dis2013.data.Haus;
import de.dis2013.data.Immobilie;
import de.dis2013.data.Kaufvertrag;
import de.dis2013.data.Makler;
import de.dis2013.data.Mietvertrag;
import de.dis2013.data.Person;
import de.dis2013.data.Wohnung;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 * 
 * TODO: Aktuell werden alle Daten im Speicher gehalten. Ziel der Übung
 * ist es, schrittweise die Datenverwaltung in die Datenbank auszulagern.
 * Wenn die Arbeit erledigt ist, werden alle Sets dieser Klasse überflüssig.
 */
public class ImmoService {
	// Datensätze im Speicher
	private Set<Makler> makler;
	private Set<Person> personen = new HashSet<Person>();
	private Set<Haus> haeuser = new HashSet<Haus>();
	private Set<Wohnung> wohnungen = new HashSet<Wohnung>();
	private Set<Mietvertrag> mietvertraege = new HashSet<Mietvertrag>();
	private Set<Kaufvertrag> kaufvertraege = new HashSet<Kaufvertrag>();
	
	// Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		// Open Hibernate Session
		Session session = sessionFactory.openSession();
		// GetAll EstateAgents from DB
		session.beginTransaction();	
		List<?> l = session.createCriteria(Makler.class).list();
		List<Makler> l_makler = new ArrayList<Makler>(l.size());
        for (Object o : l) {
            l_makler.add((Makler) o);
        }
		System.out.println(l_makler.size()+" Makler gefunden.");
		makler = new HashSet<>(l_makler);
  
		// getAll Häuser from DB
        l = session.createCriteria(Haus.class).list();
        List<Haus> l_haus = new ArrayList<>(l.size());
        for (Object o : l) {
            l_haus.add((Haus) o);
        }
        haeuser = new HashSet<>(l_haus);
        
        // getAll Wohnungen from DB
        l = session.createCriteria(Wohnung.class).list();
        List<Wohnung> l_wohnung = new ArrayList<>(l.size());
        for (Object o : l) {
            l_wohnung.add((Wohnung) o);
        }
        wohnungen = new HashSet<>(l_wohnung);
        
        // getAll Personen from DB
        l = session.createCriteria(Person.class).list();
        List<Person> l_person = new ArrayList<>(l.size());
        for (Object o : l) {
            l_person.add((Person) o);
        }
        personen = new HashSet<>(l_person);
        
        // getAll Kaufverträge from DB
        l = session.createCriteria(Kaufvertrag.class).list();
        List<Kaufvertrag> l_kaufvertrag = new ArrayList<>(l.size());
        for (Object o : l) {
            l_kaufvertrag.add((Kaufvertrag) o);
        }
        kaufvertraege = new HashSet<>(l_kaufvertrag);
        
        // getAll Mietverträge from DB
        l = session.createCriteria(Mietvertrag.class).list();
        List<Mietvertrag> l_mietvertrag = new ArrayList<>(l.size());
        for (Object o : l) {
            l_mietvertrag.add((Mietvertrag) o);
        }
        mietvertraege = new HashSet<>(l_mietvertrag);
        
		session.getTransaction().commit();
		session.close();
	}
    
    /**
     * Adds an object to the database.
     *
     * @param o Object
     */
	private void add(Object o) {
        // Open Hibernate Session
        Session session = sessionFactory.openSession();
        // Add object to DB
        session.beginTransaction();
        session.save(o);
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Deletes an object from the database.
     *
     * @param o Object
     */
    private void delete(Object o) {
        // Open Hibernate Session
        Session session = sessionFactory.openSession();
        // Delete object from DB
        session.beginTransaction();
        session.delete(o);
        session.getTransaction().commit();
        session.close();
    }
    
    /**
     * Edits the given object in the database.
     *
     * @param o Object
     */
    private void edit(Object o) {
        // Open Hibernate Session
        Session session = sessionFactory.openSession();
        // Update object in DB
        session.beginTransaction();
        session.merge(o);
        session.getTransaction().commit();
        session.close();
    }
	
	/**
	 * Finde einen Makler mit gegebener Id
	 * @param id Die ID des Maklers
	 * @return Makler mit der ID oder null
	 */
	public Makler getMaklerById(int id) {
        
        for (Makler m : makler) {
            if (m.getId() == id) {
                return m;
            }
        }
		
		return null;
	}
	
	/**
	 * Finde einen Makler mit gegebenem Login
	 * @param login Der Login des Maklers
	 * @return Makler mit der ID oder null
	 */
	public Makler getMaklerByLogin(String login) {
        
        for (Makler m : makler) {
            if (m.getLogin().equals(login)) {
                return m;
            }
        }
		
		return null;
	}
	
	/**
	 * Gibt alle Makler zurück
	 */
	public Set<Makler> getAllMakler() {
		return makler;
	}
	
	/**
	 * Finde eine Person mit gegebener Id
	 * @param id Die ID der Person
	 * @return Person mit der ID oder null
	 */
	public Person getPersonById(int id) {
        
        for (Person p : personen) {
            if (p.getId() == id) {
                return p;
            }
        }
		
		return null;
	}
	
	/**
	 * Fügt einen Makler hinzu
	 * @param m Der Makler
	 */
	public void addMakler(Makler m) {
		add(m);
		//Add EstateAgent to local buffer
		makler.add(m);
	}
	
	/**
	 * Löscht einen Makler
	 * @param m Der Makler
	 */
	public void deleteMakler(Makler m) {
		delete(m);
		//Delete EstateAgent from local buffer
		makler.remove(m);
	}
	
	/**
	 * Fügt eine Person hinzu
	 * @param p Die Person
	 */
	public void addPerson(Person p) {
		add(p);
		//Add Person to local buffer
		personen.add(p);
	}
	
	/**
	 * Gibt alle Personen zurück
	 */
	public Set<Person> getAllPersons() {
		return personen;
	}
	
	/**
	 * Löscht eine Person
	 * @param p Die Person
	 */
	public void deletePerson(Person p) {
	    delete(p);
		personen.remove(p);
	}
	
	/**
	 * Fügt ein Haus hinzu
	 * @param h Das Haus
	 */
	public void addHaus(Haus h) {
	    add(h);
		haeuser.add(h);
	}
	
	/**
	 * Gibt alle Häuser eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Häuser, die vom Makler verwaltet werden
	 */
	public Set<Haus> getAllHaeuserForMakler(Makler m) {
		Set<Haus> ret = new HashSet<Haus>();
        
        for (Haus h : haeuser) {
            if (h.getVerwalter().equals(m)) {
                ret.add(h);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet ein Haus mit gegebener ID
	 * @param id Der Makler
	 * @return Das Haus oder null, falls nicht gefunden
	 */
	public Haus getHausById(int id) {
		
		for (Haus h : haeuser) {
			if (h.getId() == id) {
				return h;
			}
		}
		
		return null;
	}
	
	/**
	 * Löscht ein Haus
	 * @param h Das Haus
	 */
	public void deleteHouse(Haus h) {
	    delete(h);
		haeuser.remove(h);
	}
	
	/**
	 * Fügt eine Wohnung hinzu
	 * @param w die Wohnung
	 */
	public void addWohnung(Wohnung w) {
	    add(w);
		wohnungen.add(w);
	}
	
	/**
	 * Gibt alle Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Wohnungen, die vom Makler verwaltet werden
	 */
	public Set<Wohnung> getAllWohnungenForMakler(Makler m) {
		Set<Wohnung> ret = new HashSet<Wohnung>();
        
        for (Wohnung w : wohnungen) {
            if (w.getVerwalter().equals(m)) {
                ret.add(w);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet eine Wohnung mit gegebener ID
	 * @param id Die ID
	 * @return Die Wohnung oder null, falls nicht gefunden
	 */
	public Wohnung getWohnungById(int id) {
        
        for (Wohnung w : wohnungen) {
            if (w.getId() == id) {
                return w;
            }
        }
		
		return null;
	}
	
	/**
	 * Löscht eine Wohnung
	 * @param w Die Wohnung
	 */
	public void deleteWohnung(Wohnung w) {
	    delete(w);
		wohnungen.remove(w);
	}
	
	
	/**
	 * Fügt einen Mietvertrag hinzu
	 * @param m Der Mietvertrag
	 */
	public void addMietvertrag(Mietvertrag m) {
	    add(m);
		mietvertraege.add(m);
	}
	
	/**
	 * Fügt einen Kaufvertrag hinzu
	 * @param k Der Kaufvertrag
	 */
	public void addKaufvertrag(Kaufvertrag k) {
	    add(k);
		kaufvertraege.add(k);
	}
	
	/**
	 * Gibt alle Mietverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Mietverträge, die zu Wohnungen gehören, die vom Makler verwaltet werden
	 */
	public Set<Mietvertrag> getAllMietvertraegeForMakler(Makler m) {
		Set<Mietvertrag> ret = new HashSet<Mietvertrag>();
        
        for (Mietvertrag v : mietvertraege) {
            if (v.getWohnung().getVerwalter().equals(m)) {
                ret.add(v);
            }
        }
		
		return ret;
	}
	
	/**
	 * Gibt alle Kaufverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Kaufverträge, die zu Häusern gehören, die vom Makler verwaltet werden
	 */
	public Set<Kaufvertrag> getAllKaufvertraegeForMakler(Makler m) {
		Set<Kaufvertrag> ret = new HashSet<Kaufvertrag>();
        
        for (Kaufvertrag k : kaufvertraege) {
            if (k.getHaus().getVerwalter().equals(m)) {
                ret.add(k);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet einen Mietvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Mietvertrag oder null, falls nicht gefunden
	 */
	public Mietvertrag getMietvertragById(int id) {
        
        for (Mietvertrag m : mietvertraege) {
            if (m.getId() == id) {
                return m;
            }
        }
		
		return null;
	}
	
	/**
	 * Findet alle Mietverträge, die Wohnungen eines gegebenen Verwalters betreffen
	 * @param m Der Verwalter
	 * @return Set aus Mietverträgen
	 */
	public Set<Mietvertrag> getMietvertragByVerwalter(Makler m) {
		Set<Mietvertrag> ret = new HashSet<Mietvertrag>();
        
        for (Mietvertrag mv : mietvertraege) {
            if (mv.getWohnung().getVerwalter().getId() == m.getId()) {
                ret.add(mv);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet alle Kaufverträge, die Häuser eines gegebenen Verwalters betreffen
	 * @param m Der Verwalter
	 * @return Set aus Kaufverträgen
	 */
	public Set<Kaufvertrag> getKaufvertragByVerwalter(Makler m) {
		Set<Kaufvertrag> ret = new HashSet<Kaufvertrag>();
        
        for (Kaufvertrag k : kaufvertraege) {
            if (k.getHaus().getVerwalter().getId() == m.getId()) {
                ret.add(k);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet einen Kaufvertrag mit gegebener ID
	 * @param id Die ID
	 * @return Der Kaufvertrag oder null, falls nicht gefunden
	 */
	public Kaufvertrag getKaufvertragById(int id) {
        
        for (Kaufvertrag k : kaufvertraege) {
            if (k.getId() == id) {
                return k;
            }
        }
		
		return null;
	}
	
	/**
	 * Löscht einen Mietvertrag
	 * @param m Der Mietvertrag
	 */
	public void deleteMietvertrag(Mietvertrag m) {
	    delete(m);
		wohnungen.remove(m);
	}
	
	/**
	 * Fügt einige Testdaten hinzu
	 */
	public void addTestData() {
				
		Makler m = new Makler();
		m.setName("Max Mustermann");
		m.setAdresse("Am Informatikum 9");
		m.setLogin("max");
		m.setPasswort("max");
		
		//TODO: Dieser Makler wird im Speicher und der DB gehalten
		this.addMakler(m);

		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Person p1 = new Person();
		p1.setAdresse("Informatikum");
		p1.setNachname("Mustermann");
		p1.setVorname("Erika");
		
		
		Person p2 = new Person();
		p2.setAdresse("Reeperbahn 9");
		p2.setNachname("Albers");
		p2.setVorname("Hans");
		
		session.save(p1);
		session.save(p2);
		
		//TODO: Diese Personen werden im Speicher und der DB gehalten
		this.addPerson(p1);
		this.addPerson(p2);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session.beginTransaction();
		Haus h = new Haus();
		h.setOrt("Hamburg");
		h.setPlz(22527);
		h.setStrasse("Vogt-Kölln-Straße");
		h.setHausnummer("2a");
		h.setFlaeche(384);
		h.setStockwerke(5);
		h.setKaufpreis(10000000);
		h.setGarten(true);
		h.setVerwalter(m);
		
		session.save(h);
		
		//TODO: Dieses Haus wird im Speicher und der DB gehalten
		this.addHaus(h);
		session.getTransaction().commit();
		
		//Hibernate Session erzeugen
		session = sessionFactory.openSession();
		session.beginTransaction();
		Makler m2 = (Makler)session.get(Makler.class, m.getId());
		Set<Immobilie> immos = m2.getImmobilien();
		/*Iterator<Immobilie> it = immos.iterator();
		
		while(it.hasNext()) {
			Immobilie i = it.next();
			System.out.println("Immo: "+i.getOrt());
		}*/
		session.close();
		
		Wohnung w = new Wohnung();
		w.setOrt("Hamburg");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);
		
		w = new Wohnung();
		w.setOrt("Berlin");
		w.setPlz(22527);
		w.setStrasse("Vogt-Kölln-Straße");
		w.setHausnummer("3");
		w.setFlaeche(120);
		w.setStockwerk(4);
		w.setMietpreis(790);
		w.setEbk(true);
		w.setBalkon(false);
		w.setVerwalter(m);
		this.addWohnung(w);
		
		Kaufvertrag kv = new Kaufvertrag();
		kv.setHaus(h);
		kv.setVertragspartner(p1);
		kv.setVertragsnummer(9234);
		kv.setDatum(new Date(System.currentTimeMillis()));
		kv.setOrt("Hamburg");
		kv.setAnzahlRaten(5);
		kv.setRatenzins(4);
		this.addKaufvertrag(kv);
		
		Mietvertrag mv = new Mietvertrag();
		mv.setWohnung(w);
		mv.setVertragspartner(p2);
		mv.setVertragsnummer(23112);
		mv.setDatum(new Date(System.currentTimeMillis()-1000000000));
		mv.setOrt("Berlin");
		mv.setMietbeginn(new Date(System.currentTimeMillis()));
		mv.setNebenkosten(65);
		mv.setDauer(36);
		this.addMietvertrag(mv);
	}

	public void editEstateAgent(Makler m) {
		edit(m);
	}
}
