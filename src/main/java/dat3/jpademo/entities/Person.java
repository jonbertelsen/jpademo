/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dat3.jpademo.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author jobe
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long p_id;
   
    @Column(name = "name", length=50, nullable=false)
    private String name;
    
    @Column(name = "year")
    private int year;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "a_id")
    private Address address;
    
    @OneToMany(mappedBy = "person",  cascade = CascadeType.PERSIST)
    private List<Fee> fees = new ArrayList<>();;
    
    @ManyToMany(mappedBy = "persons",  cascade = CascadeType.PERSIST)
    private List<SwimStyle> styles = new ArrayList<>();;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonTeam> teams = new ArrayList<>();

    public List<SwimStyle> getStyles() {
        return styles;
    }

    public List<PersonTeam> getTeams() {
        return teams;
    }
    
     
    public Person() {
    }

    public Long getP_id() {
        return p_id;
    }

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }
    
    public List<Fee> getFees() {
        return fees;
    }

    public void addFee(Fee fee) {
        this.fees.add(fee);
        if (fee != null){
            fee.setPerson(this);
        }
    }
    
    public void addSwimStyle(SwimStyle style){
        if (style != null){
            this.styles.add(style);
            style.getPersons().add(this);
        }
    }
    
    public void addTeam(Team team, PersonTeam.SwimmerLevel level){
        PersonTeam personTeam = new PersonTeam(this, team, level);
        teams.add(personTeam);
        team.getPersons().add(personTeam);
    }
    
    public void removeTeam(Team team){
        Iterator<PersonTeam> iterator = teams.iterator(); 
        
        while (iterator.hasNext()) {
            PersonTeam pt = iterator.next();
            if (pt.getPerson().equals(this) && pt.getTeam().equals(team)){
                    System.out.println("Flyt: " + pt.getLevel() + ". Name: " + pt.getPerson().getName());
                    iterator.remove();   // fjernes fra teams i personens arrayliste
                    pt.getTeam().getPersons().remove(pt);  // fjern person fra teamets person arrayliste
            }             
                
        }
    }
    
     
    public void removeSwimStyle(SwimStyle swimStyle){
        if (swimStyle != null){
            styles.remove(swimStyle);
            swimStyle.getPersons().remove(this);    
        }
    
    }
   
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if(address != null){
            address.setPerson(this);
            
        }
    }
   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.p_id);
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.p_id, other.p_id)) {
            return false;
        }
        return true;
    }
    
    
    
}
