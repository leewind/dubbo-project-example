package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.VerifyStatus;
import java.io.Serializable;
import net.pocrd.annotation.Description;

@Description("组织实体")
public class OrganizationEntity implements Serializable {

  private static final long serialVersionUID = -125082748447863586L;

  @Description("组织名称")
  public String name;

  @Description("组织认证照片")
  public String attachmentCredentialUrl;

  @Description("认证状态")
  public VerifyStatus verifyStatus;
}
