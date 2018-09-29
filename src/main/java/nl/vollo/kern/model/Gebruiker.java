package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.vollo.kern.annotation.DomainType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "gebruikers")
@DomainType(DomainEntity.GEBRUIKER)
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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getRollen() {
        return rollen;
    }

    public void setRollen(String rollen) {
        this.rollen = rollen;
    }

    public Medewerker getMedewerker() {
        return medewerker;
    }

    public void setMedewerker(Medewerker medewerker) {
        this.medewerker = medewerker;
    }

    public Ouder getOuder() {
        return ouder;
    }

    public void setOuder(Ouder ouder) {
        this.ouder = ouder;
    }
}
