package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 费用范围枚举
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.24 22:16
 */
@Description("费用范围枚举")
public enum FeeRangeEnum {

  @Description("面议")
  TALK_FACE_TO_FACE,

  @Description("500元以内")
  UNDER_FIVE_HUNDRED,

  @Description("500-1000元")
  FIVE_HUNDRED_TO_ONE_THOUSAND,

  @Description("1000-5000元")
  ONE_THOUSAND_TO_FIVE_THOUSAND,

  @Description("5000元以上")
  OVER_FIVE_THOUSAND,
  ;

}
