package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;
import java.sql.Timestamp;

public class RequirementDO extends BaseDO {

  private static final long serialVersionUID = -2255977365362642913L;

  /**
   * 发布的任务类型
   */
  private String type;

  /**
   * 选择的实验室ID
   */
  private Long labId;

  /**
   * 组织ID
   */
  private Long organizationId;

  /**
   * 描述
   */
  private String description;

  /**
   * 费用范围
   */
  private String feeRange;

  /**
   * 发布时间
   */
  private Timestamp publishedTime;

  /**
   * 招募时间范围
   */
  private String recruitTimeLimit;

  /**
   * 招募完成时间
   */
  private Timestamp recruitFinishTime;

  /**
   * 任务完成期限
   */
  private String taskFinishLimit;

  /**
   * 维修费用
   */
  private Integer fee;

  /**
   * 招募状态
   */
  private String status;

  /**
   * 图片
   */
  private String images;

  /**
   * 采用人
   */
  private Long acceptedUserId;

  /**
   * 被采用的维修报价id
   */
  private Long acceptedMotionId;

  /**
   * 任务完成时间
   */
  private Timestamp taskFinishTime;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getLabId() {
    return labId;
  }

  public void setLabId(Long labId) {
    this.labId = labId;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFeeRange() {
    return feeRange;
  }

  public void setFeeRange(String feeRange) {
    this.feeRange = feeRange;
  }

  public Timestamp getPublishedTime() {
    return publishedTime;
  }

  public void setPublishedTime(Timestamp publishedTime) {
    this.publishedTime = publishedTime;
  }

  public String getRecruitTimeLimit() {
    return recruitTimeLimit;
  }

  public void setRecruitTimeLimit(String recruitTimeLimit) {
    this.recruitTimeLimit = recruitTimeLimit;
  }

  public Timestamp getRecruitFinishTime() {
    return recruitFinishTime;
  }

  public void setRecruitFinishTime(Timestamp recruitFinishTime) {
    this.recruitFinishTime = recruitFinishTime;
  }

  public String getTaskFinishLimit() {
    return taskFinishLimit;
  }

  public void setTaskFinishLimit(String taskFinishLimit) {
    this.taskFinishLimit = taskFinishLimit;
  }

  public Integer getFee() {
    return fee;
  }

  public void setFee(Integer fee) {
    this.fee = fee;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getImages() {
    return images;
  }

  public void setImages(String images) {
    this.images = images;
  }

  public Long getAcceptedUserId() {
    return acceptedUserId;
  }

  public void setAcceptedUserId(Long acceptedUserId) {
    this.acceptedUserId = acceptedUserId;
  }

  public Long getAcceptedMotionId() {
    return acceptedMotionId;
  }

  public void setAcceptedMotionId(Long acceptedMotionId) {
    this.acceptedMotionId = acceptedMotionId;
  }

  public Timestamp getTaskFinishTime() {
    return taskFinishTime;
  }

  public void setTaskFinishTime(Timestamp taskFinishTime) {
    this.taskFinishTime = taskFinishTime;
  }
}
