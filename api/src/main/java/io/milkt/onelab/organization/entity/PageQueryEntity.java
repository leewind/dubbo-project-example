package io.milkt.onelab.organization.entity;

import java.io.Serializable;
import net.pocrd.annotation.Description;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by leewind on 2017/8/31.
 *
 * @author leewind(leewind19841209@gamil.com)
 * @version v0.1 2017.8.31 10:14
 */
@Description("页码查询参数")
public class PageQueryEntity implements Serializable {

  private static final long serialVersionUID = 8077746754028177641L;

  @Description("页码")
  public int pageNum;

  @Description("每页条目数")
  public int pageSize;

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
