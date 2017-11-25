package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RequirementType;
import java.io.Serializable;
import java.util.List;
import net.pocrd.annotation.Description;

public class RequirementDetailEntity implements Serializable {

  private static final long serialVersionUID = 4784101835427198654L;

  @Description("实验室实体")
  public LabEntity lab;

  @Description("需求id")
  public long requirementId;

  @Description("需求描述")
  public String description;

  @Description("需求类型")
  public RequirementType type;

  @Description("费用预算")
  public FeeRangeEnum feeRange;

  @Description("描述图片")
  public List<String> images;

  @Description("竞标结束时间")
  public long recruitFinishTime;
}
