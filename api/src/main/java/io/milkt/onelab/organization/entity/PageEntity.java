package io.milkt.onelab.organization.entity;

import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * Created by leewind on 2017/8/31.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.9.22 20:54
 */
@Description("页信息")
public class PageEntity implements Serializable {

  private static final long serialVersionUID = 3227096545969976168L;

  @Description("当前页码")
  public int pageNum;
  /**
   * 当前页行数
   */
  @Description("当前页行数")
  public int cucrrentNum;
  /**
   * 搜索结果总数
   */
  @Description("搜索结果总数")
  public int totalNum;
  /**
   * 当前页码
   */
  @Description("每页行数")
  public int pageSize;
}
