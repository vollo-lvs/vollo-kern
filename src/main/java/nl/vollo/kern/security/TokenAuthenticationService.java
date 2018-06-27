package nl.vollo.kern.security;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import nl.vollo.kern.model.Gebruiker;
import nl.vollo.kern.repository.GebruikerRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationService implements GebruikerAuthenticationService {
    @NonNull
    JWTTokenService tokens;
    @NonNull
    GebruikerRepository gebruikers;

    @Override
    public Optional<String> login(final String gebruikersnaam, final String wachtwoord) {
        return gebruikers
                .findByGebruikersnaam(gebruikersnaam)
                .filter(gebruiker -> Objects.equals(wachtwoord, gebruiker.getPassword()))
                .map(gebruiker1 -> tokens.expiring(ImmutableMap.of("gebruikersnaam", gebruikersnaam)));
    }

    @Override
    public Optional<Gebruiker> findByToken(final String token) {
        return Optional
                .of(tokens.verify(token))
                .map(map -> map.get("gebruikersnaam"))
                .flatMap(gebruikers::findByGebruikersnaam);
    }

    @Override
    public void logout(final Gebruiker user) {
        // Nothing to do
    }
}
