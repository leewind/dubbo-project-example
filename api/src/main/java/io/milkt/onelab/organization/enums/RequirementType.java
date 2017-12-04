package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 需求类型
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.24 21:31
 */
@Description("需求类型")
public enum RequirementType {

  @Description("实验室家具")
  FURNITURE,

  @Description("办公家具")
  OFFICE_FURNITURE,

  @Description("净化工程")
  TREATMENT,

  @Description("仪器/耗材")
  OVERALLS,

  @Description("通风系统")
  VENTILATION,

  @Description("供气系统")
  PLUMBER,
  ;
}
