package de.dis2013.core;

import java.util.*;

import org.hibernate.Criteria;
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
import org.hibernate.criterion.Restrictions;

/**
 * Klasse zur Verwaltung aller Datenbank-Entitäten.
 */
public class ImmoService {
	// Hibernate Session
	private SessionFactory sessionFactory;
	
	public ImmoService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Makler.class);
        cr.add(Restrictions.eq("id", id));
        List makler = cr.list();
        session.getTransaction().commit();
        
        for (Object o : makler) {
            if (o instanceof Makler) {
                Makler m = (Makler) o;
                if (m.getId() == id) {
                    return m;
                }
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Makler.class);
        cr.add(Restrictions.eq("login", login));
        List makler = cr.list();
        session.getTransaction().commit();
        
        for (Object o : makler) {
            if (o instanceof Makler) {
                Makler m = (Makler) o;
                if (m.getLogin().equals(login)) {
                    return m;
                }
            }
        }
		
		return null;
	}
	
	/**
	 * Gibt alle Makler zurück
	 */
	public Set<Makler> getAllMakler() {
        // Open Hibernate Session
        Session session = sessionFactory.openSession();
        // GetAll EstateAgents from DB
        session.beginTransaction();
        List<?> l = session.createCriteria(Makler.class).list();
        List<Makler> l_makler = new ArrayList<>(l.size());
        for (Object o : l) {
            l_makler.add((Makler) o);
        }
        session.getTransaction().commit();
        return new HashSet<>(l_makler);
	}
	
	/**
	 * Finde eine Person mit gegebener Id
	 * @param id Die ID der Person
	 * @return Person mit der ID oder null
	 */
	public Person getPersonById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Person.class);
        cr.add(Restrictions.eq("id", id));
        List personen = cr.list();
        session.getTransaction().commit();
        for (Object o : personen) {
            if (o instanceof Person) {
                Person m = (Person) o;
                if (m.getId() == id) {
                    return m;
                }
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
	}
    
    /**
     * Edits an estate agent.
     * @param m Makler
     */
    public void editEstateAgent(Makler m) {
        edit(m);
    }
	
	/**
	 * Löscht einen Makler
	 * @param m Der Makler
	 */
	public void deleteMakler(Makler m) {
		delete(m);
	}
	
	/**
	 * Fügt eine Person hinzu
	 * @param p Die Person
	 */
	public void addPerson(Person p) {
		add(p);
	}
	
	/**
	 * Gibt alle Personen zurück
	 */
	public Set<Person> getAllPersons() {
	    Session session = sessionFactory.openSession();
	    session.beginTransaction();
        List l = session.createCriteria(Person.class).list();
        List<Person> l_person = new ArrayList<>(l.size());
        for (Object o : l) {
            l_person.add((Person) o);
        }
        session.getTransaction().commit();
		return new HashSet<>(l_person);
	}
    
    /**
     * Bearbeitet eine Person in der DB
     * @param p Die Person
     */
    public void editPerson(Person p) {
        edit(p);
    }
	
	/**
	 * Löscht eine Person
	 * @param p Die Person
	 */
	public void deletePerson(Person p) {
	    delete(p);
	}
	
	/**
	 * Fügt ein Haus hinzu
	 * @param h Das Haus
	 */
	public void addHaus(Haus h) {
	    add(h);
	}
	
	/**
	 * Gibt alle Häuser eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Häuser, die vom Makler verwaltet werden
	 */
	public Set<Haus> getAllHaeuserForMakler(Makler m) {
		Set<Haus> ret = new HashSet<>();
		Session session = sessionFactory.openSession();
        session.beginTransaction();
		List l = session.createCriteria(Haus.class).list();
        List<Haus> l_haus = new ArrayList<>(l.size());
        for (Object o : l) {
            l_haus.add((Haus) o);
        }
        session.getTransaction().commit();
        for (Haus h : l_haus) {
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Haus.class);
        cr.add(Restrictions.eq("id", id));
        List haeuser = cr.list();
        session.getTransaction().commit();
        
        for (Object o : haeuser) {
            if (o instanceof Haus) {
                Haus m = (Haus) o;
                if (m.getId() == id) {
                    return m;
                }
            }
        }
		
		return null;
	}
    
    /**
     * Aktualisiert ein Haus in der DB
     * @param h Das Haus
     */
    public void editHaus(Haus h) {
        edit(h);
    }
	
	/**
	 * Löscht ein Haus
	 * @param h Das Haus
	 */
	public void deleteHaus(Haus h) {
	    delete(h);
	}
	
	/**
	 * Fügt eine Wohnung hinzu
	 * @param w die Wohnung
	 */
	public void addWohnung(Wohnung w) {
	    add(w);
	}
	
	/**
	 * Gibt alle Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Wohnungen, die vom Makler verwaltet werden
	 */
	public Set<Wohnung> getAllWohnungenForMakler(Makler m) {
		Set<Wohnung> ret = new HashSet<>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List l = session.createCriteria(Wohnung.class).list();
        List<Wohnung> l_wohnung = new ArrayList<>(l.size());
        for (Object o : l) {
            l_wohnung.add((Wohnung) o);
        }
        session.getTransaction().commit();
		
        for (Wohnung w : l_wohnung) {
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Wohnung.class);
        cr.add(Restrictions.eq("id", id));
        List wohnungen = cr.list();
        session.getTransaction().commit();
        
        for (Object o : wohnungen) {
            if (o instanceof Wohnung) {
                Wohnung m = (Wohnung) o;
                if (m.getId() == id) {
                    return m;
                }
            }
        }
		
		return null;
	}
    
    /**
     * Aktualisiert eine Wohnung in der DB
     * @param w Die Wohnung
     */
    public void editWohnung(Wohnung w) {
        edit(w);
    }
	
	/**
	 * Löscht eine Wohnung
	 * @param w Die Wohnung
	 */
	public void deleteWohnung(Wohnung w) {
	    delete(w);
	}
	
	
	/**
	 * Fügt einen Mietvertrag hinzu
	 * @param m Der Mietvertrag
	 */
	public void addMietvertrag(Mietvertrag m) {
	    add(m);
	}
	
	/**
	 * Fügt einen Kaufvertrag hinzu
	 * @param k Der Kaufvertrag
	 */
	public void addKaufvertrag(Kaufvertrag k) {
	    add(k);
	}
	
	/**
	 * Gibt alle Mietverträge zu Wohnungen eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Mietverträge, die zu Wohnungen gehören, die vom Makler verwaltet werden
	 */
	public Set<Mietvertrag> getAllMietvertraegeForMakler(Makler m) {
		Set<Mietvertrag> ret = new HashSet<>();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
        List l = session.createCriteria(Mietvertrag.class).list();
        List<Mietvertrag> l_mietvertrag = new ArrayList<>(l.size());
        for (Object o : l) {
            l_mietvertrag.add((Mietvertrag) o);
        }
        session.getTransaction().commit();
        for (Mietvertrag v : l_mietvertrag) {
            if (v.getWohnung().getVerwalter().equals(m)) {
                ret.add(v);
            }
        }
		
		return ret;
	}
	
	/**
	 * Gibt alle Kaufverträge zu Häusern eines Maklers zurück
	 * @param m Der Makler
	 * @return Alle Kaufverträge, die zu Häusern gehören, die vom Makler verwaltet werden
	 */
	public Set<Kaufvertrag> getAllKaufvertraegeForMakler(Makler m) {
		Set<Kaufvertrag> ret = new HashSet<>();
  
		Session session = sessionFactory.openSession();
		session.beginTransaction();
        List l = session.createCriteria(Kaufvertrag.class).list();
        List<Kaufvertrag> l_kaufvertrag = new ArrayList<>(l.size());
        for (Object o : l) {
            l_kaufvertrag.add((Kaufvertrag) o);
        }
        session.getTransaction().commit();
        for (Kaufvertrag k : l_kaufvertrag) {
            if (k.getHaus().getVerwalter().equals(m)) {
                ret.add(k);
            }
        }
		
		return ret;
	}
	
	/**
	 * Findet einen Mietvertrag mit gegebener Vertragsnummer
	 * @param vertragsnummer Die Vertragsnummer
	 * @return Der Mietvertrag oder null, falls nicht gefunden
	 */
	public Mietvertrag getMietvertragByVertragsnummer(int vertragsnummer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Mietvertrag.class);
        cr.add(Restrictions.eq("vertragsnummer", vertragsnummer));
        List mietvertraege = cr.list();
        session.getTransaction().commit();
        
        for (Object o : mietvertraege) {
            if (o instanceof Mietvertrag) {
                Mietvertrag m = (Mietvertrag) o;
                if (m.getVertragsnummer() == vertragsnummer) {
                    return m;
                }
            }
        }
		
		return null;
	}
	
	/**
	 * Findet einen Kaufvertrag mit gegebener Vertragsnummer
	 * @param vertragsnummer Die Vertragsnummer
	 * @return Der Kaufvertrag oder null, falls nicht gefunden
	 */
	public Kaufvertrag getKaufvertragByVertragsnummer(int vertragsnummer) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Kaufvertrag.class);
        cr.add(Restrictions.eq("vertragsnummer", vertragsnummer));
        List kaufvertraege = cr.list();
        session.getTransaction().commit();
        
        for (Object o : kaufvertraege) {
            if (o instanceof Kaufvertrag) {
                Kaufvertrag m = (Kaufvertrag) o;
                if (m.getVertragsnummer() == vertragsnummer) {
                    return m;
                }
            }
        }
		
		return null;
	}
    
    /**
     * Aktualisiert einen Kaufvertrag in der DB.
     * @param k Kaufvertrag
     */
	public void editKaufvertrag(Kaufvertrag k) {
	    edit(k);
    }
    
    /**
     * Aktualisiert einen Mietvertrag in der DB.
     * @param m Mietvertrag
     */
    public void editMietvertrag(Mietvertrag m) {
        edit(m);
    }
	
	/**
	 * Löscht einen Mietvertrag
	 * @param m Der Mietvertrag
	 */
	public void deleteMietvertrag(Mietvertrag m) {
	    delete(m);
	}
    
    /**
     * Löscht einen Kaufvertrag
     * @param k Der Kaufvertrag
     */
    public void deleteKaufvertrag(Kaufvertrag k) {
        delete(k);
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
}
