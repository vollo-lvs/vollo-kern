package nl.vollo.kern.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Adres implements Serializable {

	@Column
	private String straat;

	@Column(length = 10)
	private String huisnummer;

	@Column(length = 10)
	private String toevoeging;

	@Column(length = 10)
	private String postcode;

	@Column
	private String plaats;

	@Column
	private String land = "Nederland";

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("straat", straat)
                .append("huisnummer", huisnummer)
                .append("toevoeging", toevoeging)
                .append("postcode", postcode)
                .append("plaats", plaats)
                .append("land", land)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Adres)) return false;
        Adres adres = (Adres) o;
        return Objects.equals(getStraat(), adres.getStraat()) &&
                Objects.equals(getHuisnummer(), adres.getHuisnummer()) &&
                Objects.equals(getToevoeging(), adres.getToevoeging()) &&
                Objects.equals(getPostcode(), adres.getPostcode()) &&
                Objects.equals(getPlaats(), adres.getPlaats()) &&
                Objects.equals(getLand(), adres.getLand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStraat(), getHuisnummer(), getToevoeging(), getPostcode(), getPlaats(), getLand());
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getToevoeging() {
        return toevoeging;
    }

    public void setToevoeging(String toevoeging) {
        this.toevoeging = toevoeging;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPlaats() {
        return plaats;
    }

    public void setPlaats(String plaats) {
        this.plaats = plaats;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}