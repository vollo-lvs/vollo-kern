package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;

@Entity
@Table(name = "ouder_leerlingen")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
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
}