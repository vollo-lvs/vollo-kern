package nl.vollo.kern.security;

import nl.vollo.kern.model.Gebruiker;

import java.util.Optional;

public interface GebruikerAuthenticationService {

  /**
   * Logs in with the given {@code gebruikersnaam} and {@code wachtwoord}.
   *
   * @param gebruikersnaam
   * @param wachtwoord
   * @return an {@link Optional} of a user when login succeeds
   */
  Optional<String> login(String gebruikersnaam, String wachtwoord);

  /**
   * Finds a user by its dao-key.
   *
   * @param token user dao key
   * @return
   */
  Optional<Gebruiker> findByToken(String token);

  /**
   * Logs out the given input {@code user}.
   *
   * @param gebruiker the user to logout
   */
  void logout(Gebruiker gebruiker);
}