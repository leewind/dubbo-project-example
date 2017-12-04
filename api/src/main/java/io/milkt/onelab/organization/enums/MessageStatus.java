package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
@Description("消息状态")
public enum MessageStatus {

  @Description("初始化")
  INIT,

  @Description("未读")
  UNREAD,

  @Description("已读")
  READ,

  @Description("关闭")
  CLOSE,
  ;
}
