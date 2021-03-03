/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dat3.jpademo.entities;

import dto.PersonStyleDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author jobe
 */
public class Tester {
    
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Jønke", 1963);
        Person p2 = new Person("Blondie", 1959);
        
        Address a1 = new Address("Store Torv 1", 2323, "Nr. Snede");
        Address a2 = new Address("Langgade 34", 1212, "Valby");
        
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
       
        p1.addFee(f1);
        p1.addFee(f3);
        p2.addFee(f2);
        
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("ButterFly");
        SwimStyle s3 = new SwimStyle("Breast stroke");
        
        p1.addSwimStyle(s1);
        p1.addSwimStyle(s3);
        p2.addSwimStyle(s2);
        
        Team t1 = new Team("Konkurrence");
        Team t2 = new Team("Begyndere");
        p1.addTeam(t1, PersonTeam.SwimmerLevel.HIGH);
        p1.addTeam(t2, PersonTeam.SwimmerLevel.LOW);
        p2.addTeam(t2, PersonTeam.SwimmerLevel.MEDIUM);
        
        em.getTransaction().begin();
            em.persist(t1);
            em.persist(t2);
            em.persist(p1);
            em.persist(p2);
        em.getTransaction().commit();
        
        
        
        
        PersonTeamId pt_id = new PersonTeamId(p1.getP_id(), t1.getT_id() );
        PersonTeam pt_composite = em.find(PersonTeam.class, pt_id);
        if (pt_composite != null){
            System.out.println("Vi har fundet en sammensat nøgle med em.find() for: " + pt_composite.getPerson().getName());
        } else {
            System.out.println("Vi fandt desværre ikke noget!!!! På den sammensatte nøgle");
        }
        
        
        TypedQuery<PersonTeam> q_composite_key = em.createQuery("SELECT pt FROM PersonTeam pt where pt.person = :p1", PersonTeam.class);
        q_composite_key.setParameter("p1", p1);
        List<PersonTeam> q_composite_result = q_composite_key.getResultList();
        for (PersonTeam pt: q_composite_result){
            System.out.println("**** PersonTeam" + pt.getPerson().getName() + ": " + pt.getTeam().getName() + ". Level: " + pt.getLevel());
        }
        
        em.getTransaction().begin();
            //em.remove(s3);
            //p1.removeSwimStyle(s3);
        em.getTransaction().commit();
              
        System.out.println("p1: " + p1.getP_id()+ ", " + p1.getName());
        System.out.println("p2: " + p2.getP_id() + ", " + p2.getName());
        
        
        System.out.println("Jønkes gade: " + p1.getAddress().getStreet());
        
        System.out.println("Lad os se om to-vejs virker: " + a1.getPerson().getName());
        
        System.out.println("Hvem har betalt f2? Det har: " + f2.getPerson().getName());
        
        System.out.println("Hvad er der blevet betalt i alt?");
    
        TypedQuery<Fee> q1 = em.createQuery("SELECT f FROM Fee f", Fee.class);
        List<Fee> fees = q1.getResultList();
        
        for (Fee f: fees){
            System.out.println(f.getPerson().getName() + ": " + f.getAmount() + 
                    " kr. Den: "+ f.getPayDate() + " Adr: " + f.getPerson().getAddress().getCity());
        }
        
        TypedQuery<Person> q2 = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = q2.getResultList();
        
        for (Person p: persons){
            System.out.println("Navn: " + p.getName());
            System.out.println("--Fees:");
            for (Fee f: p.getFees()){
                System.out.println("---- Beløb: " + f.getAmount() + ", " + f.getPayDate().toString() );
            }
            System.out.println("--Styles:");
            for (SwimStyle ss: p.getStyles()){
                System.out.println("---- Style: " + ss.getStyleName());
            }
            System.out.println("Teams");
            for (PersonTeam pt: p.getTeams()){
                System.out.println("---- Team: " + pt.getTeam().getName() + ". Level: " + pt.getLevel());
            }
        }
        
        System.out.println("**** Eksperimenter med JPQL");
        
        TypedQuery<PersonStyleDTO> q4 = em.createQuery("SELECT new dto.PersonStyleDTO(p.name, p.year, s.styleName) FROM Person p JOIN p.styles s", dto.PersonStyleDTO.class);
        List<PersonStyleDTO> personList =  q4.getResultList();
        
        for (PersonStyleDTO p: personList){
            System.out.println(p.getName() + ", " + p.getYear() + ", " + p.getSwimStyle());
        }
        
       
        
        System.out.println("**** Flyt begynderhold for Jønke:");
        
         em.getTransaction().begin();
            //p1.removeTeam(t2);  // fjern begynderholdet
        em.getTransaction().commit();
        
        for (Person p: persons){
            System.out.println("Navn: " + p.getName());
            System.out.println("Teams");
            for (PersonTeam pt: p.getTeams()){
                System.out.println("---- Team: " + pt.getTeam().getName() + ". Level: " + pt.getLevel());
            }
        }
        
        
        
        
        em.close();
        
        
       
    }
    
}
