package nl.vollo.kern.model;

import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "scores")
@DomainType(DomainEntity.SCORE)
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

    public Toetsafname getToetsafname() {
        return toetsafname;
    }

    public void setToetsafname(Toetsafname toetsafname) {
        this.toetsafname = toetsafname;
    }

    public String getLetterScore() {
        return letterScore;
    }

    public void setLetterScore(String letterScore) {
        this.letterScore = letterScore;
    }

    public BigDecimal getCijferScore() {
        return cijferScore;
    }

    public void setCijferScore(BigDecimal cijferScore) {
        this.cijferScore = cijferScore;
    }
}
