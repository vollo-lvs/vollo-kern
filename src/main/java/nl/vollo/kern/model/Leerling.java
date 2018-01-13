package nl.vollo.kern.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "leerlingen")
@XmlRootElement
public class Leerling implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column(nullable = false)
	private String voornamen;

	@Column(nullable = false)
	private String roepnaam;

	@Column
	private String tussenvoegsel;

	@Column(nullable = false)
	private String achternaam;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date geboortedatum;

	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@Embedded
	private Adres adres;

	@OneToMany(mappedBy = "leerling", targetEntity = Inschrijving.class, fetch = FetchType.LAZY)
    @OrderBy("datumInschrijving")
	private List<Inschrijving> inschrijvingen;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Leerling)) {
			return false;
		}
		Leerling other = (Leerling) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getVoornamen() {
		return voornamen;
	}

	public void setVoornamen(String voornamen) {
		this.voornamen = voornamen;
	}

	public String getRoepnaam() {
		return roepnaam;
	}

	public void setRoepnaam(String roepnaam) {
		this.roepnaam = roepnaam;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public void setTussenvoegsel(String tussenvoegsel) {
		this.tussenvoegsel = tussenvoegsel;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public Date getGeboortedatum() {
		return geboortedatum;
	}

	public void setGeboortedatum(Date geboortedatum) {
		this.geboortedatum = geboortedatum;
	}

	public Geslacht getGeslacht() {
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht) {
		this.geslacht = geslacht;
	}

	public Adres getAdres() {
		return adres;
	}

	public void setAdres(Adres adres) {
		this.adres = adres;
	}

    public List<Inschrijving> getInschrijvingen() {
        return inschrijvingen;
    }

    public void setInschrijvingen(List<Inschrijving> inschrijvingen) {
        this.inschrijvingen = inschrijvingen;
    }

    @Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (voornamen != null && !voornamen.trim().isEmpty())
			result += "voornamen: " + voornamen;
		if (roepnaam != null && !roepnaam.trim().isEmpty())
			result += ", roepnaam: " + roepnaam;
		if (tussenvoegsel != null && !tussenvoegsel.trim().isEmpty())
			result += ", tussenvoegsel: " + tussenvoegsel;
		if (achternaam != null && !achternaam.trim().isEmpty())
			result += ", achternaam: " + achternaam;
		return result;
	}
}