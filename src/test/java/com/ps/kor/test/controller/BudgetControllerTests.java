package com.ps.kor.test.controller;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.controller.BudgetController;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.entity.enums.BudgetRoleType;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.ps.kor.test.util.TestHelpers.getEnclosedString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Class for testing the endpoints of the budget controller.
 */
@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = BudgetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BudgetControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private DailyBudgetRepo dailyBudgetRepo;

  @MockBean
  private BudgetRoleRepo budgetRoleRepo;

  @MockBean
  private AuthenticationUtils authUtils;

  private List<BudgetRole> testRoles;

  private User testUser;

  /**
   * Perform setup for each test:
   * - init containers for objects that are created during each test
   * - mock JPA repositories' methods
   */
  @Before
  public void setupTest() {
    testRoles = new LinkedList<>();

    testUser = new User();
    testUser.setId(UUID.randomUUID());
    testUser.setEmail("mockup@email.com");

    // When a role is created during a test, save it.
    when(budgetRoleRepo.save(Mockito.any(BudgetRole.class)))
        .then(answer -> {
          BudgetRole role = answer.getArgument(0);

          if (testRoles != null) {
            testRoles.add(role);
          }
          return role;
        });

    when(dailyBudgetRepo.save(Mockito.any(DailyBudget.class)))
        .then(answer -> {
          return answer.getArgument(0);
        });

    when(budgetRoleRepo.findById(Mockito.any(UUID.class)))
       .then(answer -> {
         UUID id = answer.getArgument(0);

         if (testRoles == null) {
           return null;
         }
         return testRoles.stream()
             .filter(role -> role.getId().equals(id))
             .findAny();
       });

    when(authUtils.getTokenUser(Mockito.any(String.class))).thenReturn(testUser);
  }

  /**
   * Test the post endpoint of /api/budget
   * Given an authenticated use
   * When trying to create a new DailyBudget
   * Then the following hold true:
   * - the servers responds with CREATED http status
   * - one role has been created for the given user with
   *   the appropriate CREATOR type.
   */
  @Test
  public void testCreateMethod() {
    DailyBudget budget = new DailyBudget();

    budget.setName("Test budget");
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/budget")
        .characterEncoding("utf-8")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("authorization", authUtils.generateToken(testUser.getEmail()))
        .content(String.format("{%s: %s}",
            getEnclosedString("id"),
            getEnclosedString(testUser.getId().toString())));

    try {
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();
      BudgetRole createdRole;

      assertEquals(HttpStatus.CREATED.value(), response.getStatus());
      assertEquals(1, testRoles.size()); // make sure only one instance was created

      createdRole = testRoles.get(0);
      assertEquals(testUser, createdRole.getUser());
      assertEquals(BudgetRoleType.CREATOR.rank, createdRole.getRoleType().rank);
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

}
