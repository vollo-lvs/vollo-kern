package nl.vollo.kern.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "groepsleden")
public class Groepslid implements Serializable {

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

    @ManyToOne(targetEntity = Leerling.class)
    @JoinColumn(name = "leerling_id")
    private Leerling leerling;

    @Column(name = "datum_begin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datumBegin;

    @Column(name = "datum_einde")
    @Temporal(TemporalType.DATE)
    private Date datumEinde;

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

    public Groep getGroep() {
        return groep;
    }

    public void setGroep(Groep groep) {
        this.groep = groep;
    }

    public Leerling getLeerling() {
        return leerling;
    }

    public void setLeerling(Leerling leerling) {
        this.leerling = leerling;
    }

    public Date getDatumBegin() {
        return datumBegin;
    }

    public void setDatumBegin(Date datumBegin) {
        this.datumBegin = datumBegin;
    }

    public Date getDatumEinde() {
        return datumEinde;
    }

    public void setDatumEinde(Date datumEinde) {
        this.datumEinde = datumEinde;
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
		if (!(obj instanceof Groepslid)) {
			return false;
		}
		Groepslid other = (Groepslid) obj;
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
}