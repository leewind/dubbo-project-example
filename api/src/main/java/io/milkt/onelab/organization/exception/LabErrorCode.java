package io.milkt.onelab.organization.exception;

import net.pocrd.entity.AbstractReturnCode;

/**
 * lab信息错误码
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.13
 */
public class LabErrorCode extends AbstractReturnCode {

  public LabErrorCode(String desc, int code) {
    super(desc, code);
  }

  public final static int _C_ORGANIZATION_TYPE_ERROR = 5000010;
  public final static AbstractReturnCode ORGANIZATION_TYPE_ERROR = new OrganizationErrorCode
      ("apptype不在接受的范围中，只接受3", _C_ORGANIZATION_TYPE_ERROR);

  public final static int _C_ORGANIZAION_NOT_EXIST = 5000020;
  public final static AbstractReturnCode ORGANIZAION_NOT_EXIST = new OrganizationErrorCode
      ("organization不存在", _C_ORGANIZAION_NOT_EXIST);

  public final static int _C_ADDRESS_CREATE_ERROR = 5000030;
  public final static AbstractReturnCode ADDRESS_CREATE_ERROR = new LabErrorCode
      ("address创建失败", _C_ADDRESS_CREATE_ERROR);

  public final static int _C_LAB_CREATE_ERROR = 5000040;
  public final static AbstractReturnCode LAB_CREATE_ERROR = new LabErrorCode
      ("lab创建失败", _C_LAB_CREATE_ERROR);

  public final static int _C_LAB_NOT_EXIST = 5000050;
  public final static AbstractReturnCode LAB_NOT_EXIST = new LabErrorCode
      ("lab不存在", _C_LAB_NOT_EXIST);

  public final static int _C_LAB_NOT_PERMISSION = 5000060;
  public final static AbstractReturnCode LAB_NOT_PERMISSION = new LabErrorCode
      ("lab操作没有权限", _C_LAB_NOT_PERMISSION);
}
