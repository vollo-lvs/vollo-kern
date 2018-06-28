package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "groep_medewerkers")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.groepMedewerker)
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
}