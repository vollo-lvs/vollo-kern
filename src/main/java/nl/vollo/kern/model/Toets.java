package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;

@Entity
@Table(name = "toetsen")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
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
}