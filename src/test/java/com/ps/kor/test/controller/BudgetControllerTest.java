package com.ps.kor.test.controller;

import com.ps.kor.controller.BudgetController;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Class for testing the endpoints of the budget controller.
 */
@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = BudgetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BudgetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private DailyBudgetRepo dailyBudgetRepo;

  @MockBean
  private BudgetRoleRepo budgetRoleRepo;

  private List<BudgetRole> testRoles;

  private List<User> testUsers;

  /**
   * @param str
   * @return the string argument enclosed by "
   */
  private String getEnclosedString(String str) {
    return String.format("\"%s\"", str);
  }

  /**
   * Perform setup for each test:
   * - init containers for objects that are created during each test
   * - mock JPA repositories' methods
   */
  @Before
  public void setupTest() {
    testRoles = new LinkedList<>();
    testUsers = new LinkedList<>();

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

    when(userRepo.findById(Mockito.any(UUID.class)))
        .then(answer -> {
          UUID id = answer.getArgument(0);

          if (testUsers == null) {
            return null;
          }
          return testUsers.stream()
              .filter(role -> role.getId().equals(id))
              .findAny();
        });
  }

  /**
   * Test the post endpoint of /api/budget.
   * Asserting the following:
   * - the correct CREATED HttpStatus,
   * - one role has been created for the given used with
   *   the appropriate type.
   */
  @Test
  public void testCreateMethod() {
    User user = new User();
    DailyBudget budget = new DailyBudget();

    user.setId(UUID.randomUUID());
    testUsers.add(user);
    budget.setName("Test budget");

    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/budget")
        .characterEncoding("utf-8")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("authorization", user.getId().toString())
        .content(String.format("{%s: %s}",
            getEnclosedString("id"),
            getEnclosedString(user.getId().toString())));

    try {
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();
      BudgetRole createdRole;


      assertEquals(HttpStatus.CREATED.value(), response.getStatus());
      assertEquals(1, testRoles.size()); // make sure only one instance was created

      createdRole = testRoles.get(0);
      assertEquals(user, createdRole.getUser());
      assertEquals(BudgetRole.BudgetRoleType.CREATOR, createdRole.getRoleType());
    }
    catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

}
