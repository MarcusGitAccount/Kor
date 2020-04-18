package com.ps.kor.business.report.aggregation;

import com.ps.kor.entity.Expenditure;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Data
@ToString
public class CountSumByTypeAggregation implements BaseAggregation{

  private Expenditure.ExpenditureType type;

  private Long count;

  private Long total;

  public CountSumByTypeAggregation(Expenditure.ExpenditureType type, Long count, Long total) {
    this.type = type;
    this.count = count;
    this.total = total;
  }

  @Override
  public List<String> getHeader() {
    return Arrays.asList("Count", "Total amount", "Expenditure type");
  }

  @Override
  public List<String> getData() {
    return Arrays.asList(
        count.toString(),
        total.toString(),
        type.toString());
  }

}
