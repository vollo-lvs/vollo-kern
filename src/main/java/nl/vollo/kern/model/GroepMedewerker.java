package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "groep_medewerkers")
@DomainType(DomainEntity.GROEP_MEDEWERKER)
public class GroepMedewerker extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

    @ManyToOne(targetEntity = Groep.class)
    @JoinColumn(name = "groep_id", foreignKey = @ForeignKey(name = "gmw_grp_fk"))
    private Groep groep;

    @ManyToOne(targetEntity = Medewerker.class)
    @JoinColumn(name = "medewerker_id", foreignKey = @ForeignKey(name = "gmw_mdw_fk"))
    private Medewerker medewerker;

    @Column(name = "datum_begin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumBegin;

    @Column(name = "datum_einde")
    @Temporal(TemporalType.DATE)
    private Date datumEinde;

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

    public Groep getGroep() {
        return groep;
    }

    public void setGroep(Groep groep) {
        this.groep = groep;
    }

    public Medewerker getMedewerker() {
        return medewerker;
    }

    public void setMedewerker(Medewerker medewerker) {
        this.medewerker = medewerker;
    }

    public Date getDatumBegin() {
        return datumBegin;
    }

    public void setDatumBegin(Date datumBegin) {
        this.datumBegin = datumBegin;
    }

    public Date getDatumEinde() {
        return datumEinde;
    }

    public void setDatumEinde(Date datumEinde) {
        this.datumEinde = datumEinde;
    }
}