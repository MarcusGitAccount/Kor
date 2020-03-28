package com.ps.kor.test.controller;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.controller.AuthenticationController;
import com.ps.kor.entity.User;
import com.ps.kor.repo.AuthJWTRepo;
import com.ps.kor.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;

import static com.ps.kor.test.util.TestHelpers.convert2JsonString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private AuthenticationUtils authUtils;

  @MockBean
  private AuthJWTRepo authJWTRepo;

  private User testUser;

  private List<User> usersContainer;

  /**
   * Perform setup for each test:
   * - init containers for objects that are created during each test
   * - mock JPA repositories' methods
   */
  @Before
  public void setupTest() {
    usersContainer = new LinkedList<>();

    testUser = new User();
    testUser.setEmail("mockup@email.com");
    testUser.setPassword("really hard password to guess omg");

    // When a role is created during a test, save it.
    when(userRepo.save(Mockito.any(User.class)))
        .then(answer -> {
          User user = answer.getArgument(0);

          if (usersContainer != null) {
            usersContainer.add(user);
          }

          return user;
        });
  }

  /**
   * Given an email and a password
   * When signing up the user
   * Then assert the following:
   * - one user was created and persisted
   * - the api responds with a CREATED status
   * - the password is correctly generated
   */
  @Test
  public void testSignUp() {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/auth/signup")
        .characterEncoding("utf-8")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(convert2JsonString(testUser));

    try {
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();
      User createdUser;

      assertEquals(HttpStatus.CREATED.value(), response.getStatus());
      assertEquals(1, usersContainer.size()); // make sure only one instance was created

      createdUser = usersContainer.get(0);
      assertNotNull(createdUser.getPassword());
      assertNotNull(createdUser.getSaltedHash());

      String oldPasswordHash = BCrypt.hashpw(testUser.getPassword(), createdUser.getSaltedHash());

      assertEquals(createdUser.getPassword(), oldPasswordHash);
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }
}
