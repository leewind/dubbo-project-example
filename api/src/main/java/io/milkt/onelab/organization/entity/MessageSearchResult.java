package io.milkt.onelab.organization.entity;

import java.io.Serializable;
import java.util.List;
import net.pocrd.annotation.Description;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
@Description("消息搜索结果")
public class MessageSearchResult implements Serializable {

  private static final long serialVersionUID = -48079695892557127L;

  @Description("需求列表")
  public List<MessageEntity> list;

  @Description("页码")
  public PageEntity page;
}
