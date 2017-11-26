package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 竞标状态
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.25 20:40
 */
@Description("竞标状态")
public enum MotionStatus {
  @Description("初始状态")
  INIT,

  @Description("申请")
  APPLY,

  @Description("被选中")
  SUCCESS,

  @Description("失败")
  FAIL,

  @Description("关闭")
  CLOSE,
  ;
}
