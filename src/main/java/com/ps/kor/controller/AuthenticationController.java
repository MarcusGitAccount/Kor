package com.ps.kor.controller;

import com.ps.kor.auth.AuthenticationUtils;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.AuthJWT;
import com.ps.kor.entity.User;
import com.ps.kor.repo.AuthJWTRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

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
  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody User user) {
    // TODO: validate user: field constraints, unique email, valid password...
    String salt = BCrypt.gensalt(AuthenticationUtils.LOG_ROUNDS);
    String hashPassword = BCrypt.hashpw(user.getPassword(), salt);
    User created;

    user.setPassword(hashPassword);
    user.setSaltedHash(salt);

    created = userRepo.save(user);
    if (created == null) {
      return ResponseEntityFactory.buildErrorResponse(null, "Failed creating user", HttpStatus.BAD_REQUEST);
    }

    created.setPassword(null);
    created.setSaltedHash(null);
    return ResponseEntityFactory.buildSuccesResponse(user, "Succesful signup", HttpStatus.CREATED);
  }

  /**
   * Checks whether the users exists. If so, it continues
   * by trying to match the existing password with the one
   * received. In case of success it creates a jwt token and it
   * persists and return it.
   * @param user
   * @return
   */
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody User user) {
    // Sent data might be uncompleted
    User existing = userRepo.findByEmail(user.getEmail()).orElse(null);
    String password = user.getPassword();

    if (existing == null) {
      return ResponseEntityFactory.buildErrorResponse(null, "User not found", HttpStatus.NOT_FOUND);
    }
    if (!BCrypt.hashpw(password, existing.getSaltedHash()).equals(existing.getPassword())) {
      return ResponseEntityFactory.buildErrorResponse(null, "Invalid password", HttpStatus.BAD_REQUEST);
    }

    AuthJWT prevToken = authJWTRepo.findByUserEmail(user.getEmail()).orElse(null);
    if (prevToken != null) {
      try {
        // trying to parse it. if parsing fails <=> token expired
        String email = authUtils.getEmailFromToken(prevToken.getToken());

        if (userRepo.findByEmail(email).orElse(null) == null) {
          return ResponseEntityFactory.buildErrorResponse(null, "User no longer registerd", HttpStatus.BAD_REQUEST);
        }
        if (email.equals(prevToken.getUserEmail())) {
          return ResponseEntityFactory.buildSuccesResponse(null, "Already logged in", HttpStatus.OK);
        }
      } catch (Exception ex)  {
        // invalid token in db
        authJWTRepo.delete(prevToken);
      }
    }

    String jwtStr = authUtils.generateToken(user.getEmail());
    AuthJWT token = new AuthJWT(jwtStr, user.getEmail());

    authJWTRepo.save(token);
    return ResponseEntityFactory.buildSuccesResponse(token, "Logged in", HttpStatus.ACCEPTED);
  }

  /**
   * Logs out a user by deleting its authentication token from the database.
   * @param token
   * @return
   */
  @PostMapping("/logout")
  public ResponseEntity logout(@RequestHeader("authorization") String token) {
    if (token == null) {
      return ResponseEntityFactory.buildErrorResponse(null, "Token is empty", HttpStatus.BAD_REQUEST);
    }

    AuthJWT jwt = authJWTRepo.findById(token).orElse(null);
    if (jwt == null) {
      return ResponseEntityFactory.buildErrorResponse(null, "Token is invalid", HttpStatus.BAD_REQUEST);
    }

    authJWTRepo.delete(jwt);
    jwt.setToken(null);
    return ResponseEntityFactory.buildSuccesResponse(null, "User logged out", HttpStatus.OK);
  }
}
