package nl.vollo.kern.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "toetsafnames")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.toetsafname)
public class Toetsafname extends DomainObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
    @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "omschrijving")
    private String omschrijving;

    @ManyToOne(targetEntity = Toets.class)
    @JoinColumn(name = "toets_id", foreignKey = @ForeignKey(name = "tan_tts_fk"))
    private Toets toets;

    @Column(name = "datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
}
