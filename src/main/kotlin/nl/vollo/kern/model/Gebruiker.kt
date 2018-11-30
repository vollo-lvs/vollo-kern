package nl.vollo.kern.model;

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "gebruikers")
class Gebruiker(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vollo_seq")
        @SequenceGenerator(name = "vollo_seq", sequenceName = "vollo_seq", allocationSize = 1)
        @Column(name = "id", updatable = false, nullable = false)
        override val id: Long? = null,

        @Version
        @Column(name = "version")
        override val version: Int = 0,

        @Column(nullable = false)
        @NotNull
        val gebruikersnaam: String,

        @Column(nullable = false)
        @NotNull
        @JsonIgnore
        val wachtwoord: String,

        @Column
        val rollen: String? = null,

        @ManyToOne(targetEntity = Medewerker::class)
        @JoinColumn(name = "medewerker_id", foreignKey = ForeignKey(name = "geb_mdw_fk"))
        val medewerker: Medewerker? = null,

        @ManyToOne(targetEntity = Ouder::class)
        @JoinColumn(name = "ouder_id", foreignKey = ForeignKey(name = "geb_oud_fk"))
        val ouder: Ouder? = null

) : DomainObject(), UserDetails {

    override val _type: DomainEntity get() = DomainEntity.GEBRUIKER

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
