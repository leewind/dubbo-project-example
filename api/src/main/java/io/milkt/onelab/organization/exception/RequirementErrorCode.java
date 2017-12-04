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

  public final static int _C_REQUIREMENT_NOT_EXIST = 8000060;
  public final static AbstractReturnCode REQUIREMENT_NOT_EXIST = new OrganizationErrorCode
      ("需求不存在", _C_REQUIREMENT_NOT_EXIST);

  public final static int _C_ADDRESS_NOT_EXIST = 8000070;
  public final static AbstractReturnCode ADDRESS_NOT_EXIST = new OrganizationErrorCode
      ("地址不存在", _C_ADDRESS_NOT_EXIST);

  public final static int _C_MOTION_NOT_PERMISSION = 8000080;
  public final static AbstractReturnCode MOTION_NOT_PERMISSION = new OrganizationErrorCode
      ("需求已关闭，不接受竞价", _C_MOTION_NOT_PERMISSION);

  public final static int _C_NOT_REPECT_MOTION = 8000090;
  public final static AbstractReturnCode NOT_REPECT_MOTION = new OrganizationErrorCode
      ("不能重复竞价", _C_NOT_REPECT_MOTION);

  public final static int _C_PUBLISHER_INFO_ERROR = 8000100;
  public final static AbstractReturnCode PUBLISHER_INFO_ERROR = new OrganizationErrorCode
      ("获取发布人信息错误", _C_PUBLISHER_INFO_ERROR);

  public final static int _C_USER_NOT_PERMITTED = 8000110;
  public final static AbstractReturnCode USER_NOT_PERMITTED = new OrganizationErrorCode
      ("用户没有审核通过无法申请", _C_USER_NOT_PERMITTED);

  public final static int _C_MOTION_NOT_EXIST = 8000120;
  public final static AbstractReturnCode MOTION_NOT_EXIST = new OrganizationErrorCode
      ("竞标不存在", _C_MOTION_NOT_EXIST);

  public final static int _C_REQUIREMENT_STATUS_LOCK = 8000130;
  public final static AbstractReturnCode REQUIREMENT_STATUS_LOCK = new OrganizationErrorCode
      ("订单状态已经锁定", _C_REQUIREMENT_STATUS_LOCK);

  public final static int _C_MESSAGE_NOT_EXIST = 8000140;
  public final static AbstractReturnCode MESSAGE_NOT_EXIST = new OrganizationErrorCode
      ("消息不存在", _C_MESSAGE_NOT_EXIST);
}
