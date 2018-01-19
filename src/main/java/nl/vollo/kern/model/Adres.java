package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
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

}