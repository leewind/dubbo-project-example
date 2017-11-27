package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.MotionStatus;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;
import java.io.Serializable;
import java.util.List;
import net.pocrd.annotation.Description;

/**
 * 竞价实体
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.25 21:48
 */
@Description("竞价实体")
public class MotionEntity implements Serializable {

  private static final long serialVersionUID = 2764165738128858177L;

  @Description("竞价id")
  public long motionId;

  @Description("用户头像")
  public String userIcon;

  @Description("用户手机号")
  public String userMobile;

  @Description("用户名")
  public String userName;

  @Description("认证状态")
  public CommonVerifyStatus userCommonVerifyStatus;

  @Description("所有技能")
  public List<String> skills;

  @Description("希望薪酬")
  public int expectedFee;

  @Description("描述")
  public String description;

  @Description("竞价时间")
  public long startTime;

  @Description("状态")
  public MotionStatus status;
}
