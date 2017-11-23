package io.milkt.onelab.organization.domain;

import com.sfebiz.common.dao.domain.BaseDO;

public class AddressDO extends BaseDO {

  private static final long serialVersionUID = 4627761884547324883L;

  /**
   * 省份ID
   */
  private Long provinceAdcode;

  /**
   * 省份名称
   */
  private String provinceName;

  /**
   * 城市ID
   */
  private Long cityAdcode;

  /**
   * 城市名称
   */
  private String cityName;

  /**
   * 区域ID
   */
  private Long districtAdcode;

  /**
   * 区域名称
   */
  private String districtName;

  /**
   * 详细地址
   */
  private String detailAddress;

  /**
   * 地址经度
   */
  private Float latitude;

  /**
   * 地址纬度
   */
  private Float longitude;

  /**
   * geohash
   */
  private String geohash;

  public Long getProvinceAdcode() {
    return provinceAdcode;
  }

  public void setProvinceAdcode(Long provinceAdcode) {
    this.provinceAdcode = provinceAdcode;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }

  public Long getCityAdcode() {
    return cityAdcode;
  }

  public void setCityAdcode(Long cityAdcode) {
    this.cityAdcode = cityAdcode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Long getDistrictAdcode() {
    return districtAdcode;
  }

  public void setDistrictAdcode(Long districtAdcode) {
    this.districtAdcode = districtAdcode;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public String getDetailAddress() {
    return detailAddress;
  }

  public void setDetailAddress(String detailAddress) {
    this.detailAddress = detailAddress;
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

  public String getGeohash() {
    return geohash;
  }

  public void setGeohash(String geohash) {
    this.geohash = geohash;
  }
}
