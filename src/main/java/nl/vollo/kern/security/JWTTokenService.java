package nl.vollo.kern.security;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import nl.vollo.kern.DatumService;
import nl.vollo.kern.VolloProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Service
final class JWTTokenService implements Clock {

    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    @Autowired
    private DatumService datumService;

    @Autowired
    private VolloProperties volloProperties;

    public String permanent(final Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    public String expiring(final Map<String, String> attributes) {
        return newToken(attributes, getExpiration());
    }

    private String newToken(final Map<String, String> attributes, final long expiresInSec) {
        final ZonedDateTime now = datumService.now();
        final Claims claims = Jwts
                .claims()
                .setIssuer(getIssuer())
                .setIssuedAt(Date.from(now.toInstant()));

        if (expiresInSec > 0) {
            final ZonedDateTime expiresAt = now.plusSeconds(expiresInSec);
            claims.setExpiration(Date.from(expiresAt.toInstant()));
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, getSecretKey())
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(getIssuer())
                .setClock(this)
                .setAllowedClockSkewSeconds(getClockSkew())
                .setSigningKey(getSecretKey());
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    public Map<String, String> untrusted(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(getIssuer())
                .setClock(this)
                .setAllowedClockSkewSeconds(getClockSkew());

        // See: https://github.com/jwtk/jjwt/issues/135
        final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (final Map.Entry<String, Object> e : claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (final IllegalArgumentException | JwtException e) {
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        return Date.from(datumService.now().toInstant());
    }

    private String getIssuer() {
        return volloProperties.getJwt().getIssuer();
    }

    private String getSecretKey() {
        return BASE64.encode(volloProperties.getJwt().getSecret());
    }

    private long getExpiration() {
        return volloProperties.getJwt().getExpiration().getSeconds();
    }

    private long getClockSkew() {
        return volloProperties.getJwt().getClockSkew().getSeconds();
    }

}