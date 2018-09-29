package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "toetsafnames")
@DomainType(DomainEntity.TOETSAFNAME)
public class Toetsafname extends DomainObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "omschrijving")
    private String omschrijving;

    @ManyToOne(targetEntity = Toets.class)
    @JoinColumn(name = "toets_id", foreignKey = @ForeignKey(name = "tan_tts_fk"))
    private Toets toets;

    @Column(name = "datum")
    @Temporal(TemporalType.DATE)
    private Date datum;

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

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Toets getToets() {
        return toets;
    }

    public void setToets(Toets toets) {
        this.toets = toets;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
}
