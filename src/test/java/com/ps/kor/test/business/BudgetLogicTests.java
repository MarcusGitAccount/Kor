package com.ps.kor.test.business;

import com.ps.kor.TestingConfiguration;
import com.ps.kor.business.BudgetLogic;
import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.BusinessMessage;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.entity.enums.BudgetRoleType;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@Log4j2
@ActiveProfiles("testing")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BudgetLogic.class, TestingConfiguration.class })
public class BudgetLogicTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DailyBudgetRepo dailyBudgetRepo;

    @Autowired
    private BudgetRoleRepo budgetRoleRepo;

    @Autowired
    private AuthenticationUtils authUtils;

    @Autowired
    private BudgetLogic budgetLogic;

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
      testUser.setRoleList(new LinkedList<>());
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

      when(userRepo.save(Mockito.any(User.class)))
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
      BusinessMessage<DailyBudget> message = budgetLogic.create("token lol", budget);
      BudgetRole createdRole = null;

      assertEquals(1, testRoles.size()); // make sure only one instance was created
      createdRole = testRoles.get(0);
      assertEquals(testUser, createdRole.getUser());
      assertEquals(BudgetRoleType.CREATOR.rank, createdRole.getRoleType().rank);
    }
  }
