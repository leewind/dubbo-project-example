package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class RegionDO extends BaseDO {

  private static final long serialVersionUID = -5687320921829372794L;

  /**
   * 高德code
   */
  private String adcode;

  /**
   * 纬度
   */
  private Float latitude;

  /**
   * 经度
   */
  private Float longitude;

  /**
   * 城市code
   */
  private String citycode;

  /**
   * 行政区等级 - 国／省／市
   */
  private String level;

  /**
   * 行政区名称
   */
  private String name;

  /**
   * 父级区域的adcode
   */
  private String parent;

  /**
   * 拼音
   */
  private String pinyin;

  public String getAdcode() {
    return adcode;
  }

  public void setAdcode(String adcode) {
    this.adcode = adcode;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  public String getCitycode() {
    return citycode;
  }

  public void setCitycode(String citycode) {
    this.citycode = citycode;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getPinyin() {
    return pinyin;
  }

  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }
}
