package nl.vollo.kern.security

import com.google.common.collect.ImmutableMap
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Clock
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS256
import io.jsonwebtoken.impl.TextCodec.BASE64
import io.jsonwebtoken.impl.compression.GzipCompressionCodec
import nl.vollo.kern.DatumService
import nl.vollo.kern.VolloConfig
import org.apache.commons.lang3.StringUtils.substringBeforeLast
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class JWTTokenService() : Clock {
    private val log = LoggerFactory.getLogger(JWTTokenService::class.java)

    @Autowired
    private lateinit var datumService: DatumService

    @Autowired
    private lateinit var volloConfig: VolloConfig

    private val secretKey: String
        get() = BASE64.encode(volloConfig.jwt.secret)

    fun permanent(attributes: Map<String, String>): String {
        return newToken(attributes, 0)
    }

    fun expiring(attributes: Map<String, String>): String {
        return newToken(attributes, volloConfig.jwt.expiration.seconds)
    }

    private fun newToken(attributes: Map<String, String>, expiresInSec: Long): String {
        val now = datumService.now()
        val claims = Jwts
                .claims()
                .setIssuer(volloConfig.jwt.issuer)
                .setIssuedAt(Date.from(now.toInstant()))

        if (expiresInSec > 0) {
            val expiresAt = now.plusSeconds(expiresInSec)
            claims.expiration = Date.from(expiresAt.toInstant())
        }
        claims.putAll(attributes)

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact()
    }

    fun verify(token: String): Map<String, String> {
        val parser = Jwts
                .parser()
                .requireIssuer(volloConfig.jwt.issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(volloConfig.jwt.clockSkew.seconds)
                .setSigningKey(secretKey)
        return parseClaims { parser.parseClaimsJws(token).body }
    }

    fun untrusted(token: String): Map<String, String> {
        val parser = Jwts
                .parser()
                .requireIssuer(volloConfig.jwt.issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(volloConfig.jwt.clockSkew.seconds)

        // See: https://github.com/jwtk/jjwt/issues/135
        val withoutSignature = substringBeforeLast(token, DOT) + DOT
        return parseClaims { parser.parseClaimsJwt(withoutSignature).body }
    }

    override fun now(): Date {
        return Date.from(datumService.now().toInstant())
    }

    companion object {

        private val DOT = "."
        private val COMPRESSION_CODEC = GzipCompressionCodec()

        private fun parseClaims(toClaims: () -> Claims): Map<String, String> {
            try {
                val claims = toClaims.invoke()
                val builder = ImmutableMap.builder<String, String>()
                for ((key, value) in claims) {
                    builder.put(key, value.toString())
                }
                return builder.build()
            } catch (e: IllegalArgumentException) {
                return ImmutableMap.of()
            } catch (e: JwtException) {
                return ImmutableMap.of()
            }

        }
    }
}