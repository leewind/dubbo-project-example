package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.MessageStatus;
import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
@Description("消息实体")
public class MessageEntity implements Serializable {

  private static final long serialVersionUID = 238912866618593935L;

  @Description("消息id")
  public long id;

  /**
   * 标题
   */
  @Description("标题")
  public String title;

  /**
   * 阅读时间
   */
  @Description("阅读时间")
  public long readTime;

  /**
   * 副标题
   */
  @Description("副标题")
  public String subtitle;

  /**
   * 内容
   */
  @Description("内容")
  public String content;

  /**
   * 状态
   */
  @Description("状态")
  public MessageStatus status;

  /**
   * 系统来源
   */
  @Description("系统来源")
  public String source;

  /**
   * 目标用户
   */
  @Description("目标用户")
  public long target;

  /**
   * 发布时间
   */
  @Description("发布时间")
  public long publishTime;


}
