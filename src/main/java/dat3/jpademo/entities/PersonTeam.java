/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dat3.jpademo.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 *
 * @author jobe
 */
@Entity
@Table(name = "link_person_team")
public class PersonTeam implements Serializable {
    
    public enum SwimmerLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private PersonTeamId id;
    
    @ManyToOne
    @MapsId("p_id")
    @JoinColumn(name = "p_id")
    private Person person;
    
    @ManyToOne
    @MapsId("t_id")
    @JoinColumn(name = "t_id")
    private Team team;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "level")
    private SwimmerLevel level;

    public PersonTeam() {
    }

    public PersonTeam(Person person, Team team, SwimmerLevel level) {
        this.person = person;
        this.team = team;
        this.level = level;
        this.id = new PersonTeamId(person.getP_id(), team.getT_id());
    }

    public PersonTeamId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Team getTeam() {
        return team;
    }

    public SwimmerLevel getLevel() {
        return level;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    

    @Override
    public int hashCode() {
        return Objects.hash(person, team);
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
        final PersonTeam other = (PersonTeam) obj;
        if (!Objects.equals(this.person, other.person)) {
            return false;
        }
        if (!Objects.equals(this.team, other.team)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersonTeam{" + "id=" + id + ", person=" + person + ", team=" + team + ", level=" + level + '}';
    }
    
    
   
}
