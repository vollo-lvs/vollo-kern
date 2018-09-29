package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inschrijvingen")
@DomainType(DomainEntity.INSCHRIJVING)
public class Inschrijving extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
    private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@ManyToOne(targetEntity = Leerling.class)
    @JoinColumn(name = "leerling_id", foreignKey = @ForeignKey(name = "ins_llg_fk"))
	private Leerling leerling;

	@Column(name = "datum_inschrijving", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumInschrijving;

	@Column(name = "datum_uitschrijving")
    @Temporal(TemporalType.DATE)
    private Date datumUitschrijving;

	@ManyToOne(targetEntity = School.class)
	@JoinColumn(name = "school_id", foreignKey = @ForeignKey(name = "ins_scl_fk"), nullable = false)
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

    public Leerling getLeerling() {
        return leerling;
    }

    public void setLeerling(Leerling leerling) {
        this.leerling = leerling;
    }

    public Date getDatumInschrijving() {
        return datumInschrijving;
    }

    public void setDatumInschrijving(Date datumInschrijving) {
        this.datumInschrijving = datumInschrijving;
    }

    public Date getDatumUitschrijving() {
        return datumUitschrijving;
    }

    public void setDatumUitschrijving(Date datumUitschrijving) {
        this.datumUitschrijving = datumUitschrijving;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}