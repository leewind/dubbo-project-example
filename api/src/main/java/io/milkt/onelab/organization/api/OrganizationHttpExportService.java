package io.milkt.onelab.organization.api;

import io.milkt.onelab.organization.exception.OrganizationErrorCode;
import net.pocrd.annotation.ApiAutowired;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.AutowireableParameter;
import net.pocrd.define.SecurityType;

@ApiGroup(name = "organization", minCode = 6000000, maxCode = 7000000, codeDefine = OrganizationErrorCode.class, owner = "leewind")
public interface OrganizationHttpExportService {

  @HttpApi(name = "organization.save", desc = "保存organization返回id", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      OrganizationErrorCode._C_ORGANIZATION_VERIFY_SUCCESS
  })
  public long save(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = true, name = "name", desc = "组织名称") String name,
      @ApiParameter(required = true, name = "attachmentCredentialUrl", desc = "认证图片地址") String attachmentCredentialUrl
  );


}
