package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;
import java.sql.Timestamp;

public class MotionDO extends BaseDO {

  private static final long serialVersionUID = -7735244410104606307L;

  /**
   * 联系手机号
   */
  private String mobile;

  /**
   * 期望费用
   */
  private Integer expectedFee;

  /**
   * 描述
   */
  private String description;

  /**
   * 竞标时间
   */
  private Timestamp motionTime;

  /**
   * 竞标状态
   */
  private String status;

  /**
   * 需求id
   */
  private Long requirementId;

  /**
   * 用户id
   */
  private Long userId;

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Integer getExpectedFee() {
    return expectedFee;
  }

  public void setExpectedFee(Integer expectedFee) {
    this.expectedFee = expectedFee;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getMotionTime() {
    return motionTime;
  }

  public void setMotionTime(Timestamp motionTime) {
    this.motionTime = motionTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getRequirementId() {
    return requirementId;
  }

  public void setRequirementId(Long requirementId) {
    this.requirementId = requirementId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
