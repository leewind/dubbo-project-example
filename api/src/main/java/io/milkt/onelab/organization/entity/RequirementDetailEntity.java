package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import java.io.Serializable;
import java.util.List;
import net.pocrd.annotation.Description;

@Description("需求详细实体")
public class RequirementDetailEntity implements Serializable {

  private static final long serialVersionUID = 4784101835427198654L;

  @Description("订单状态")
  public RequirementStatus status;

  @Description("实验室实体")
  public LabEntity lab;

  @Description("需求id")
  public long requirementId;

  @Description("需求描述")
  public String description;

  @Description("需求类型")
  public RequirementType type;

//  @Description("费用预算")
//  public FeeRangeEnum feeRange;
  @Description("费用预算")
  public int fee;

  @Description("描述图片")
  public List<String> images;

  @Description("竞标结束时间")
  public long recruitFinishTime;

  @Description("竞标数量")
  public long motionCount;

  @Description("发布人联系方式")
  public String mobile;

  @Description("被选中的motionId")
  public long acceptedMotionId;

  @Description("竞价列表")
  public List<MotionEntity> motions;

  @Description("是否参与竞价")
  public boolean isJoin;
}
