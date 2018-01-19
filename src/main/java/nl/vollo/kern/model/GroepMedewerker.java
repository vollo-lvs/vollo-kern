package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groep_medewerkers")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GroepMedewerker implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

    @ManyToOne(targetEntity = Groep.class)
    @JoinColumn(name = "groep_id")
    private Groep groep;

    @ManyToOne(targetEntity = Medewerker.class)
    @JoinColumn(name = "medewerker_id")
    private Medewerker medewerker;

    @Column(name = "datum_begin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumBegin;

    @Column(name = "datum_einde")
    @Temporal(TemporalType.DATE)
    private Date datumEinde;

}