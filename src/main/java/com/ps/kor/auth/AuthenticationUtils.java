package com.ps.kor.auth;

import com.ps.kor.entity.User;
import com.ps.kor.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Log4j2
@Component
public class AuthenticationUtils {

  @Autowired
  private static UserRepo userRepo;

  public static final String SECRET = "E9892A62FB2DED1BF34DF6FB27753E5B7BB38E46A9659EAE273D3F5E5E519EB1";

  public static final String SUBJECT = "authentication";

  public static final Integer LOG_ROUNDS = 10;

  public String generateToken(String email) {
    Calendar calendar = Calendar.getInstance();
    Date expirationDate;

    // 7 days from now on
    calendar.add(Calendar.DATE, 7);
    expirationDate = calendar.getTime();

    return Jwts.builder()
        .setSubject(SUBJECT)
        .claim("email", email)
        .setExpiration(expirationDate)
        .signWith(
            SignatureAlgorithm.HS256,
            TextCodec.BASE64.decode(SECRET)
        )
        .compact();
  }

  public String getEmailFromToken(String token) {
    Jws<Claims> jws = Jwts
        .parser()
        .requireSubject(SUBJECT)
        .setSigningKey(TextCodec.BASE64.decode(SECRET))
        .parseClaimsJws(token);

    return jws.getBody().get("email", String.class);
  }

  @Nullable
  public User getTokenUser(String token) {
    try {
      String email = getEmailFromToken(token);

      return userRepo.findByEmail(email).orElse(null);
    }
    catch (Exception ex) {
      log.error("Error when trying to parse token.");
      return null;
    }
  }
}
