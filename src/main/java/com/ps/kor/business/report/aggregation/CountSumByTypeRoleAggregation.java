package com.ps.kor.business.report.aggregation;

import com.ps.kor.entity.Expenditure;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
public class CountSumByTypeRoleAggregation implements BaseAggregation {

  private String name;

  private Expenditure.ExpenditureType type;

  private Long count;

  private Long total;

  public CountSumByTypeRoleAggregation(String name, Expenditure.ExpenditureType type,
                                       Long count, Long total) {
    this.name = name;
    this.type = type;
    this.count = count;
    this.total = total;
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("Username", "Count", "Total amount", "Expenditure type");
  }

  @Override
  public List<String> getData() {
    return Arrays.asList(
        name,
        count.toString(),
        total.toString(),
        type.toString());
  }
}
