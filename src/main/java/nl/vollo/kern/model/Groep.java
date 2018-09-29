package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groepen")
@DomainType(DomainEntity.GROEP)
public class Groep extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String naam;

	@Column
	@Min(1L)
	@Max(8L)
	private int niveau;

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepLeerling.class, fetch = FetchType.LAZY)
    private List<GroepLeerling> groepLeerlingen = new ArrayList<>();

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepMedewerker.class, fetch = FetchType.LAZY)
    private List<GroepMedewerker> groepMedewerkers = new ArrayList<>();

	@ManyToOne(targetEntity = School.class)
	@JoinColumn(name = "school_id", foreignKey = @ForeignKey(name = "grp_scl_fk"), nullable = false)
	private School school;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public List<GroepLeerling> getGroepLeerlingen() {
        return groepLeerlingen;
    }

    public void setGroepLeerlingen(List<GroepLeerling> groepLeerlingen) {
        this.groepLeerlingen = groepLeerlingen;
    }

    public List<GroepMedewerker> getGroepMedewerkers() {
        return groepMedewerkers;
    }

    public void setGroepMedewerkers(List<GroepMedewerker> groepMedewerkers) {
        this.groepMedewerkers = groepMedewerkers;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
