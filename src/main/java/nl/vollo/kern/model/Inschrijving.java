package nl.vollo.kern.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "inschrijvingen")
public class Inschrijving implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@ManyToOne(targetEntity = Leerling.class)
    @JoinColumn(name = "leerling_id")
	private Leerling leerling;

	@Column(name = "datum_inschrijving", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumInschrijving;

	@Column(name = "datum_uitschrijving")
    @Temporal(TemporalType.DATE)
    private Date datumUitschrijving;

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
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null)
			result += "id: " + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Inschrijving)) {
			return false;
		}
		Inschrijving other = (Inschrijving) obj;
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

	public Leerling getLeerling() {
		return this.leerling;
	}

	public void setLeerling(final Leerling leerling) {
		this.leerling = leerling;
	}

    public Date getDatumInschrijving() {
        return datumInschrijving;
    }

    public void setDatumInschrijving(Date datumInschrijving) {
        this.datumInschrijving = datumInschrijving;
    }

    public Date getDatumUitschrijving() {
        return datumUitschrijving;
    }

    public void setDatumUitschrijving(Date datumUitschrijving) {
        this.datumUitschrijving = datumUitschrijving;
    }
}