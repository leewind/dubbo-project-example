package io.milkt.onelab.organization.api;

import io.milkt.onelab.organization.entity.AddressInsertEntity;
import io.milkt.onelab.organization.entity.LabEntity;
import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.exception.LabErrorCode;
import java.util.List;
import net.pocrd.annotation.ApiAutowired;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.AutowireableParameter;
import net.pocrd.define.SecurityType;

/**
 * lab对外暴露的http接口集合
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.24
 */
@ApiGroup(name = "lab", minCode = 5000000, maxCode = 6000000, codeDefine = LabErrorCode.class, owner = "leewind")
public interface LabHttpExportService {

  @HttpApi(name = "lab.save", desc = "保存lab回id", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      LabErrorCode._C_ORGANIZATION_TYPE_ERROR,
      LabErrorCode._C_ORGANIZAION_NOT_EXIST,
      LabErrorCode._C_LAB_CREATE_ERROR,
      LabErrorCode._C_ADDRESS_CREATE_ERROR
  })
  public long save(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = false, name = "labId", desc = "实验室id") long labId,
      @ApiParameter(required = true, name = "name", desc = "实验室名称") String name,
      @ApiParameter(required = true, name = "type", desc = "实验室类型") LabType type,
      @ApiParameter(required = true, name = "address", desc = "实验室地址信息") AddressInsertEntity address,
      @ApiParameter(required = true, name = "phone", desc = "实验室电话") String phone
  );

  @HttpApi(name = "lab.getList", desc = "获取用户lab列表", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      LabErrorCode._C_ORGANIZAION_NOT_EXIST,
      LabErrorCode._C_ORGANIZATION_TYPE_ERROR
  })
  public List<LabEntity> getList(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId
  );

  @HttpApi(name = "lab.remove", desc = "删除对应id的lab", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      LabErrorCode._C_ORGANIZAION_NOT_EXIST,
      LabErrorCode._C_ORGANIZATION_TYPE_ERROR
  })
  public boolean remove(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = true, name = "labId", desc = "实验室id") long labId
  );
}
