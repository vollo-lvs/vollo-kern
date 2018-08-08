package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "scores")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.score)
public class Score extends DomainObject {

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
    @JoinColumn(name = "leerling_id", foreignKey = @ForeignKey(name = "sco_llg_fk"))
	private Leerling leerling;

	@ManyToOne(targetEntity = Toetsafname.class)
    @JoinColumn(name = "toetsafname_id", foreignKey = @ForeignKey(name = "sco_tan_fk"))
	private Toetsafname toetsafname;

	@Column(name = "letter_score")
    private String letterScore;

	@Column(name = "cijfer_score")
	private BigDecimal cijferScore;

}
