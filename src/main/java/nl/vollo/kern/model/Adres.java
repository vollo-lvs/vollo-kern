package nl.vollo.kern.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

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

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (straat != null && !straat.trim().isEmpty())
			result += "straat: " + straat;
		if (huisnummer != null && !huisnummer.trim().isEmpty())
			result += ", huisnummer: " + huisnummer;
		if (toevoeging != null && !toevoeging.trim().isEmpty())
			result += ", toevoeging: " + toevoeging;
		if (postcode != null && !postcode.trim().isEmpty())
			result += ", postcode: " + postcode;
		if (plaats != null && !plaats.trim().isEmpty())
			result += ", plaats: " + plaats;
		if (land != null && !land.trim().isEmpty())
			result += ", land: " + land;
		return result;
	}
}