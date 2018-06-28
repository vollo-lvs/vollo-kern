package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.vollo.kern.annotation.DomainType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "gebruikers")
@XmlRootElement
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@DomainType(DomainEntity.gebruiker)
public class Gebruiker extends DomainObject implements UserDetails {

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
    private String gebruikersnaam;

    @Column(nullable = false)
    @NotNull
    @JsonIgnore
    private String wachtwoord;

    @Column
    private String rollen;

    @ManyToOne(targetEntity = Medewerker.class)
    @JoinColumn(name = "medewerker_id", foreignKey = @ForeignKey(name = "geb_mdw_fk"))
    private Medewerker medewerker;

    @ManyToOne(targetEntity = Ouder.class)
    @JoinColumn(name = "ouder_id", foreignKey = @ForeignKey(name = "geb_oud_fk"))
    private Ouder ouder;

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return rollen == null
                ? Collections.emptyList()
                : Stream.of(rollen.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return wachtwoord;
    }

    @Override
    public String getUsername() {
        return gebruikersnaam;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
