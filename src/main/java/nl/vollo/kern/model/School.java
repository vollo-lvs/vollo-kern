package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scholen")
@DomainType(DomainEntity.SCHOOL)
public class School extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column(nullable = false)
	private String naam;

	@Embedded
	private Adres adres;

	@ManyToOne(targetEntity = School.class)
	@JoinColumn(name = "hoort_bij_school_id", foreignKey = @ForeignKey(name = "scl_scl_fk"))
	private School hoortBij;

	@JsonIgnore
	@OneToMany(mappedBy = "hoortBij", targetEntity = School.class, fetch = FetchType.LAZY)
	private List<School> bijbehorendeScholen = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "school", targetEntity = Groep.class, fetch = FetchType.LAZY)
	private List<Groep> groepen = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "school", targetEntity = Inschrijving.class, fetch = FetchType.LAZY)
	private List<Inschrijving> inschrijvingen = new ArrayList<>();

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

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public School getHoortBij() {
        return hoortBij;
    }

    public void setHoortBij(School hoortBij) {
        this.hoortBij = hoortBij;
    }

    public List<School> getBijbehorendeScholen() {
        return bijbehorendeScholen;
    }

    public void setBijbehorendeScholen(List<School> bijbehorendeScholen) {
        this.bijbehorendeScholen = bijbehorendeScholen;
    }

    public List<Groep> getGroepen() {
        return groepen;
    }

    public void setGroepen(List<Groep> groepen) {
        this.groepen = groepen;
    }

    public List<Inschrijving> getInschrijvingen() {
        return inschrijvingen;
    }

    public void setInschrijvingen(List<Inschrijving> inschrijvingen) {
        this.inschrijvingen = inschrijvingen;
    }
}