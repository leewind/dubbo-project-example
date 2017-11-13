package io.milkt.onelab.organization.exception;

import net.pocrd.entity.AbstractReturnCode;

/**
 * 行政区域信息错误码
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.13
 */
public class RegionErrorCode extends AbstractReturnCode {

  public RegionErrorCode(String desc, int code) {
    super(desc, code);
  }

  public final static int _C_REGION_ADCODE_NOT_FOUND = 7000010;
  public final static AbstractReturnCode REGION_ADCODE_NOT_FOUND = new RegionErrorCode
      ("找不到adcode对应的行政区域", _C_REGION_ADCODE_NOT_FOUND);

  public final static int _C_REGION_PARENT_NOT_EXIST = 7000020;
  public final static AbstractReturnCode REGION_PARENT_NOT_EXIST = new RegionErrorCode
      ("找不到parentAdcode对应的行政区域", _C_REGION_PARENT_NOT_EXIST);
}
