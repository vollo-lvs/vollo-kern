package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scholen")
@XmlRootElement
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.school)
public class School extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column(nullable = false)
	private String naam;

	@Embedded
	private Adres adres;

	@ManyToOne(targetEntity = School.class)
	@JoinColumn(name = "hoort_bij_school_id", foreignKey = @ForeignKey(name = "scl_scl_fk"))
	private School hoortBij;

	@JsonIgnore
	@OneToMany(mappedBy = "hoortBij", targetEntity = School.class, fetch = FetchType.LAZY)
	private List<School> bijbehorendeScholen = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "school", targetEntity = Groep.class, fetch = FetchType.LAZY)
	private List<Groep> groepen = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "school", targetEntity = Inschrijving.class, fetch = FetchType.LAZY)
	private List<Inschrijving> inschrijvingen = new ArrayList<>();
}