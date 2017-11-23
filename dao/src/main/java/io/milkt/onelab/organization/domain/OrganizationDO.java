package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class OrganizationDO extends BaseDO {

  private static final long serialVersionUID = -8030052140069527841L;

  /**
   * 组织名称
   */
  private String name;

  /**
   * 地址信息ID
   */
  private Long addressId;

  /**
   * 组织认证照片
   */
  private String attachmentCredentialUrl;

  /**
   * 认证状态
   */
  private String verifyStatus;

  /**
   * 用户Id
   */
  private Long userId;

  /**
   * 注册路径
   */
  private String channel;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getAddressId() {
    return addressId;
  }

  public void setAddressId(Long addressId) {
    this.addressId = addressId;
  }

  public String getAttachmentCredentialUrl() {
    return attachmentCredentialUrl;
  }

  public void setAttachmentCredentialUrl(String attachmentCredentialUrl) {
    this.attachmentCredentialUrl = attachmentCredentialUrl;
  }

  public String getVerifyStatus() {
    return verifyStatus;
  }

  public void setVerifyStatus(String verifyStatus) {
    this.verifyStatus = verifyStatus;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }
}
