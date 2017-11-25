package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.RequirementType;
import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * 需求实体
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.24 11:54
 */
@Description("需求实体")
public class RequirementEntity implements Serializable {

  private static final long serialVersionUID = -8422742628918323264L;

  @Description("需求id")
  public long requirementId;

  @Description("需求描述")
  public String description;

  @Description("需求类型")
  public RequirementType type;

  @Description("实验室地址")
  public AddressEntity address;

  @Description("竞标结束时间")
  public long recruitFinishTime;
}
