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

  private List<BudgetRole> createdRoles;

  private List<User> testUsers;

  @Before
  public void setupTest() {
    MockitoAnnotations.initMocks(this);

    createdRoles = new LinkedList<>();
    testUsers = new LinkedList<>();

    // When a role is created during a test, save it.
    when(budgetRoleRepo.save(Mockito.any(BudgetRole.class)))
        .then(answer -> {
          BudgetRole role = answer.getArgument(0);

          if (createdRoles != null) {
            createdRoles.add(role);
          }
          return role;
        });

    when(budgetRoleRepo.findById(Mockito.any(UUID.class)))
       .then(answer -> {
         UUID id = answer.getArgument(0);

         if (createdRoles == null) {
           return null;
         }
         return createdRoles.stream()
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
        .header("authorization", user.getId().toString())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(String.format("{\"id\": \"%s\"}", user.getId()));

    try {
      MvcResult result = mockMvc.perform(requestBuilder).andReturn();
      MockHttpServletResponse response = result.getResponse();

      Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
      log.info("test done");
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
  }

}
