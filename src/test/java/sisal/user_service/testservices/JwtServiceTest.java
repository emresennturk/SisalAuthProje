package sisal.user_service.testservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import sisal.user_service.services.JwtService;

 class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String secretKey =  "MySuperSecretKeyForJwtTokensWhichIsVerySecure12345";
    private final Long expirationTime = 1000 * 60 * 60 * 10L;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "keyExpirationTime", expirationTime);

        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
     void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
     void testExtractClaim() {
        String token = jwtService.generateToken(userDetails);

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("testUser", subject);
    }

    @Test
     void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
     void testIsTokenExpired() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "keyExpirationTime", 1000 * 60 * 60 * 10L);

        String token = jwtService.generateToken(userDetails);

        //Tokenin süresinin dolması için bekleme
        //Thread.sleep(2);

        assertTrue(jwtService.isTokenValid(token, userDetails));
        //assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
     void testGetExpirationTime() {
        assertEquals(expirationTime, jwtService.getExpirationTime());
    }
}
