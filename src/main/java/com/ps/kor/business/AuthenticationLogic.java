package com.ps.kor.business;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.BusinessMesageType;
import com.ps.kor.business.util.BusinessMessage;
import com.ps.kor.entity.AuthJWT;
import com.ps.kor.entity.User;
import com.ps.kor.repo.AuthJWTRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationLogic {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private AuthJWTRepo authJWTRepo;

  @Autowired
  private AuthenticationUtils authUtils;

  /**
   * Given the details of a users, validates it and persists
   * it after generating a new salt and hashed password.
   * @param user
   * @return the persisted user
   */
  public BusinessMessage signup(User user) {
    // TODO: validate user: field constraints, unique email, valid password...
    String salt = BCrypt.gensalt(AuthenticationUtils.LOG_ROUNDS);
    String hashPassword = BCrypt.hashpw(user.getPassword(), salt);
    User created;

    user.setPassword(hashPassword);
    user.setSaltedHash(salt);

    created = userRepo.save(user);
    if (created == null) {
      return new BusinessMessage(BusinessMesageType.USER_CREATION_FAIL);
    }

    created.setPassword(null);
    created.setSaltedHash(null);
    return new BusinessMessage(user, BusinessMesageType.SIGNUP_SUCCESS);
  }

  /**
   * Checks whether the users exists. If so, it continues
   * by trying to match the existing password with the one
   * received. In case of success it creates a jwt token and it
   * persists and return it.
   * @param user
   * @return
   */
  public BusinessMessage login(User user) {
    // Sent data might be uncompleted
    User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
    String password = user.getPassword();

    if (existing == null) {
      return new BusinessMessage(BusinessMesageType.USER_NOT_FOUND);
    }
    if (!BCrypt.hashpw(password, existing.getSaltedHash()).equals(existing.getPassword())) {
      return new BusinessMessage(BusinessMesageType.INVALID_PASSWORD);
    }

    AuthJWT prevToken = authJWTRepo.findByUserEmail(user.getEmail()).orElse(null);
    if (prevToken != null) {
      try {
        // trying to parse it. if parsing fails <=> token expired
        String email = authUtils.getEmailFromToken(prevToken.getToken());

        if (userRepo.findByEmail(email).orElse(null) == null) {
          return new BusinessMessage(BusinessMesageType.USER_NOT_FOUND);
        }
        if (email.equals(prevToken.getUserEmail())) {
          return new BusinessMessage(BusinessMesageType.ALREADY_LOGGED_IN);
        }
      } catch (Exception ex)  {
        // invalid token in db
        authJWTRepo.delete(prevToken);
      }
    }

    String jwtStr = authUtils.generateToken(user.getEmail());
    AuthJWT token = new AuthJWT(jwtStr, user.getEmail());

    authJWTRepo.save(token);
    return new BusinessMessage(token, BusinessMesageType.LOG_IN_SUCCESS);
  }

  /**
   * Logs out a user by deleting its authentication token from the database.
   * @param token
   * @return
   */
  public BusinessMessage logout(String token) {
    if (token == null) {
      return new BusinessMessage(BusinessMesageType.TOKEN_EMPTY);
    }

    AuthJWT jwt = authJWTRepo.findById(token).orElse(null);
    if (jwt == null) {
      return new BusinessMessage(BusinessMesageType.TOKEN_INVALID);
    }

    authJWTRepo.delete(jwt);
    jwt.setToken(null);
      return new BusinessMessage(BusinessMesageType.LOG_OUT_SUCESS);
  }
}
