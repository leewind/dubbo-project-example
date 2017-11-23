package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 认证状态
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.23 10:00
 */
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
