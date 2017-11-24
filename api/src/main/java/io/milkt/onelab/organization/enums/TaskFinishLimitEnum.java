package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

@Description("任务完成期限")
public enum TaskFinishLimitEnum {

  @Description("招募后3天内")
  AFTER_RECRUIT_THREE_DAY,

  @Description("招募后5天内")
  AFTER_RECRUIT_FIVE_DAY,

  @Description("招募后7天内")
  AFTER_RECRUIT_SEVEN_DAY,
  ;

}
