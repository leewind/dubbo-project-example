package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class LabDO extends BaseDO {

  private static final long serialVersionUID = -4946772887516000461L;

  /**
   * lab名称
   */
  private String name;

  /**
   * lab类型
   */
  private String type;

  /**
   * 地址id
   */
  private Long addressId;

  /**
   * 联系电话
   */
  private String phone;

  /**
   * 组织id
   */
  private Long organizationId;

  /**
   * 评价等级
   */
  private Integer level;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getAddressId() {
    return addressId;
  }

  public void setAddressId(Long addressId) {
    this.addressId = addressId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }
}
