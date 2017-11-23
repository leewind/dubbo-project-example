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

  public final static int _C_ORGANIZATION_VERIFY_SUCCESS = 6000010;
  public final static AbstractReturnCode ORGANIZATION_VERIFY_SUCCESS = new OrganizationErrorCode
      ("origanization已经审核通过不能再进行修改", _C_ORGANIZATION_VERIFY_SUCCESS);

}
