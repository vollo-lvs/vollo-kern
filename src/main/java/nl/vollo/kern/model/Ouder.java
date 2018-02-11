package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "ouders")
@XmlRootElement
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Ouder extends DomainObject {

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
    private String voornaam;

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

    public Ouder() {
        super(DomainEntity.ouder);
    }
}
