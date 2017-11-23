package io.milkt.onelab.organization.enums;

import net.pocrd.annotation.Description;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 注册路径
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.23 10:00
 */
@Description("注册路径")
public enum AppType {

  @Description("发布人")
  PUBLISHER(3, "发布人"),

  @Description("维修人")
  MAINTAINER(4, "维修人"),
  ;

  /**
   * 键值码
   */
  private int code;

  /**
   * 参数业务描述
   */
  private String desc;

  /**
   * 根据键值码返回物流动作类型枚举
   *
   * @param code 键值码
   * @return 系统类型枚举
   */
  public static AppType getEnumByCode(int code) {
    for (AppType each : values()) {
      if (code == each.getCode()) {
        return each;
      }
    }

    return null;
  }

  /**
   * 构造方法
   *
   * @param code 参数名称
   * @param desc 参数含义描述
   */
  AppType(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * Getter method for property <tt>name</tt>.
   *
   * @return property value of name
   */
  public int getCode() {
    return code;
  }

  /**
   * Getter method for property <tt>desc</tt>.
   *
   * @return property value of desc
   */
  public String getDesc() {
    return desc;
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
