package io.milkt.onelab.organization.entity;

import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * 插入地址信息
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.23 17:00
 */
@Description("插入地址信息")
public class AddressInsertEntity implements Serializable {

  private static final long serialVersionUID = -779615008045034999L;

  @Description("省-adcode")
  public long provinceAdcode;

  @Description("省-名称")
  public String provinceName;

  @Description("市-adcode")
  public long cityAdcode;

  @Description("市-名称")
  public String cityName;

  @Description("区-adcode")
  public long districtAdcode;

  @Description("区-名称")
  public String districtName;

  @Description("详细地址")
  public String detailAddress;
}
