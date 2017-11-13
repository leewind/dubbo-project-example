package io.milkt.onelab.organization.exception;

import net.pocrd.entity.AbstractReturnCode;

/**
 * 组织信息错误码
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.13
 */
public class OrganizationErrorCode extends AbstractReturnCode {

  public OrganizationErrorCode(String desc, int code) {
    super(desc, code);
  }
}
