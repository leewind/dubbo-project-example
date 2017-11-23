package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

@Description("认证状态")
public enum VerifyStatus {
  @Description("初始状态")
  INIT,

  @Description("申请认证")
  APPLY,

  @Description("认证成功")
  SUCCESS,

  @Description("认证失败")
  FAIL,
  ;
}
