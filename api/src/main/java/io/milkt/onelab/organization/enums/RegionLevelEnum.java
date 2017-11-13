package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 行政区域枚举
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.13
 */
@Description("行政区域等级枚举")
public enum RegionLevelEnum {

  @Description("国家")
  COUNTRY,

  @Description("省份")
  PROVINCE,

  @Description("城市")
  CITY,

  @Description("区")
  DISTRICT,
  ;
}
