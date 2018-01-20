package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "groepen")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Groep implements Serializable {

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

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepLeerling.class, fetch = FetchType.LAZY)
    private List<GroepLeerling> groepLeerlingen;

	@JsonIgnore
    @OneToMany(mappedBy = "groep", targetEntity = GroepMedewerker.class, fetch = FetchType.LAZY)
    private List<GroepMedewerker> groepMedewerkers;
}
