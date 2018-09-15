package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.xml.bind.annotation.XmlRootElement

@Entity
@Table(name = "gebruikers")
@XmlRootElement
data class Gebruiker(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long?,

        @Version
        @Column(name = "version")
        override val version: Int,

        @Column(nullable = false)
        @NotNull
        val gebruikersnaam: String,

        @Column(nullable = false)
        @NotNull
        @JsonIgnore
        val wachtwoord: String,

        @Column
        val rollen: String?,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "geb_mdw_fk"))
        val medewerker: Medewerker?,

        @ManyToOne(targetEntity = Ouder::class)
        @JoinColumn(name = "ouder_id", foreignKey = ForeignKey(name = "geb_oud_fk"))
        val ouder: Ouder?

) : DomainObject(DomainEntity.gebruiker, id, version), UserDetails {

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> =
        if (rollen == null)
            Collections.emptyList()
        else
            rollen.split(",").map {
                SimpleGrantedAuthority(it)
            }


    @JsonIgnore
    override fun getPassword(): String = wachtwoord

    @Override
    override fun getUsername(): String = gebruikersnaam

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @Override
    override fun isEnabled(): Boolean = true

}
