package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.RegionLevelEnum;
import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * 行政区域实体
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.13
 */
@Description("行政区域实体")
public class RegionEntity implements Serializable {

  private static final long serialVersionUID = -250396155554189011L;

  @Description("ad系统编码")
  public String adcode;

  @Description("纬度")
  public float latitude;

  @Description("经度")
  public float longitude;

  @Description("城市编码")
  public String citycode;

  @Description("行政区域等级")
  public RegionLevelEnum level;

  @Description("名称")
  public String name;

  @Description("父级ad系统编码")
  public String parentAdcode;

  @Description("拼音")
  public String pinyin;
}
