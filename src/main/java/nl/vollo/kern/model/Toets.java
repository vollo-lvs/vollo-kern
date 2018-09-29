package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;

@Entity
@Table(name = "toetsen")
@DomainType(DomainEntity.TOETS)
public class Toets extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
    private Long id;

	@Version
	@Column(name = "version")
	private int version;

	// TODO optioneel koppelen aan school

	// TODO waarden?
	@Column(name = "soort")
    private String soort;

    @Column(name = "soort_score")
	@Enumerated(EnumType.STRING)
    private SoortScore soortScore;

	@Column(name = "omschrijving")
    private String omschrijving;

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

    public String getSoort() {
        return soort;
    }

    public void setSoort(String soort) {
        this.soort = soort;
    }

    public SoortScore getSoortScore() {
        return soortScore;
    }

    public void setSoortScore(SoortScore soortScore) {
        this.soortScore = soortScore;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
}