package com.ps.kor.test.business;

import com.ps.kor.TestingConfiguration;
import com.ps.kor.business.report.Report;
import com.ps.kor.business.report.ReportFactory;
import com.ps.kor.business.report.impl.CountSumByTypeReport;
import com.ps.kor.business.report.impl.CountSumByTypeRoleReport;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Log4j2
@ActiveProfiles("testing")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ReportFactory.class, TestingConfiguration.class })
public class ReportFactoryTests {

  @Autowired
  private ReportFactory reportFactory;

  /**
   * Given a ReportFactory object
   * When requested a certain report type to be retrieved
   * Then the correct Report instance is returned
   */
  @Test
  public void testFactoryCreation() {
    Report report1 = reportFactory.getInstance(ReportFactory.ReportType.CountAndSumByType);
    Report report2 = reportFactory.getInstance(ReportFactory.ReportType.CountAndSumByTypeRole);

    Assertions.assertTrue(report1 instanceof CountSumByTypeReport);
    Assertions.assertTrue(report2 instanceof CountSumByTypeRoleReport);
  }
}
