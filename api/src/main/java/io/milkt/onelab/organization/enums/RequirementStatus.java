package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * 任务状态
 */
@Description("任务状态")
public enum RequirementStatus {

  @Description("初始化")
  INIT,

  @Description("竞标中")
  RECRUITING,

  @Description("执行中")
  PROCESSING,

  @Description("已完成")
  FINISHED,

  @Description("已关闭")
  CLOSE,
  ;
}
