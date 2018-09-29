package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;

@Entity
@Table(name = "ouder_leerlingen")
@DomainType(DomainEntity.OUDER_LEERLING)
public class OuderLeerling extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

    @ManyToOne(targetEntity = Ouder.class)
    @JoinColumn(name = "ouder_id", foreignKey = @ForeignKey(name = "oll_oud_fk"))
    private Ouder ouder;

    @ManyToOne(targetEntity = Leerling.class)
    @JoinColumn(name = "leerling_id", foreignKey = @ForeignKey(name = "oll_llg_fk"))
    private Leerling leerling;

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

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
    }

    public Leerling getLeerling() {
        return leerling;
    }

    public void setLeerling(Leerling leerling) {
        this.leerling = leerling;
    }
}