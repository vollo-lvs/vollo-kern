package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "leerlingen")
@XmlRootElement
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.LEERLING)
public class Leerling extends DomainObject {

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
	@NotNull
	private String voornamen;

	@Column(nullable = false)
	@NotNull
	private String roepnaam;

	@Column
	private String tussenvoegsel;

	@Column(nullable = false)
	@NotNull
	private String achternaam;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date geboortedatum;

	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@Embedded
	private Adres adres;

	@JsonIgnore
	@OneToMany(mappedBy = "leerling", targetEntity = Inschrijving.class, fetch = FetchType.LAZY)
    @OrderBy("datumInschrijving")
	private List<Inschrijving> inschrijvingen = new ArrayList<>();
}