package io.milkt.onelab.organization.exception;

import net.pocrd.entity.AbstractReturnCode;

public class RequirementErrorCode extends AbstractReturnCode {

  public RequirementErrorCode(String desc, int code) {
    super(desc, code);
  }

  public final static int _C_ORGANIZATION_TYPE_ERROR = 8000010;
  public final static AbstractReturnCode ORGANIZATION_TYPE_ERROR = new OrganizationErrorCode
      ("apptype不在接受的范围中，只接受3", _C_ORGANIZATION_TYPE_ERROR);

  public final static int _C_ORGANIZAION_NOT_EXIST = 8000020;
  public final static AbstractReturnCode ORGANIZAION_NOT_EXIST = new OrganizationErrorCode
      ("organization不存在", _C_ORGANIZAION_NOT_EXIST);

  public final static int _C_LAB_NOT_EXIST = 8000030;
  public final static AbstractReturnCode LAB_NOT_EXIST = new OrganizationErrorCode
      ("lab不存在", _C_LAB_NOT_EXIST);

  public final static int _C_LAB_NOT_PERMISSION = 8000040;
  public final static AbstractReturnCode LAB_NOT_PERMISSION = new OrganizationErrorCode
      ("没有权利选择lab", _C_LAB_NOT_PERMISSION);

  public final static int _C_REQUIREMENT_CREATE_ERROR = 8000050;
  public final static AbstractReturnCode REQUIREMENT_CREATE_ERROR = new OrganizationErrorCode
      ("任务创建失败", _C_REQUIREMENT_CREATE_ERROR);
}
