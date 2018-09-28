package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groepen")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.GROEP)
public class Groep extends DomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String naam;

	@Column
	@Min(1L)
	@Max(8L)
	private int niveau;

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepLeerling.class, fetch = FetchType.LAZY)
    private List<GroepLeerling> groepLeerlingen = new ArrayList<>();

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepMedewerker.class, fetch = FetchType.LAZY)
    private List<GroepMedewerker> groepMedewerkers = new ArrayList<>();

	@ManyToOne(targetEntity = School.class)
	@JoinColumn(name = "school_id", foreignKey = @ForeignKey(name = "grp_scl_fk"), nullable = false)
	private School school;
}
