package io.milkt.onelab.organization.api;


import io.milkt.onelab.organization.entity.PageQueryEntity;
import io.milkt.onelab.organization.entity.RequirementDetailEntity;
import io.milkt.onelab.organization.entity.RequirementSearchResult;
import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import java.util.List;
import net.pocrd.annotation.ApiAutowired;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.AutowireableParameter;
import net.pocrd.define.SecurityType;

/**
 * requirement对外暴露的http接口
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.24 18:33
 */
@ApiGroup(name = "requirement", minCode = 8000000, maxCode = 9000000, codeDefine = RequirementErrorCode.class, owner = "leewind")
public interface RequirementHttpExportService {

  @HttpApi(name = "requirement.save", desc = "保存requirement返回id", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      RequirementErrorCode._C_ORGANIZATION_TYPE_ERROR,
      RequirementErrorCode._C_ORGANIZAION_NOT_EXIST,
      RequirementErrorCode._C_LAB_NOT_EXIST,
      RequirementErrorCode._C_LAB_NOT_PERMISSION,
      RequirementErrorCode._C_REQUIREMENT_CREATE_ERROR
  })
  public long save(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = true, name = "labId", desc = "实验室id") long labId,
      @ApiParameter(required = true, name = "type", desc = "任务类型") RequirementType type,
      @ApiParameter(required = true, name = "description", desc = "任务描述") String description,
      @ApiParameter(required = true, name = "recruitTimeLimit", desc = "招募时间限制")RecruitTimeLimitEnum recruitTimeLimit,
      @ApiParameter(required = true, name = "feeRange", desc = "招募时间限制")FeeRangeEnum feeRange,
      @ApiParameter(required = true, name = "taskFinishLimit", desc = "任务完成期限")TaskFinishLimitEnum taskFinishLimit,
      @ApiParameter(required = false, name = "images", desc = "图片")List<String> images
  );

  @HttpApi(name = "requirement.getMyList", desc = "获取我的需求列表", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      RequirementErrorCode._C_ORGANIZATION_TYPE_ERROR,
      RequirementErrorCode._C_ORGANIZAION_NOT_EXIST
  })
  public RequirementSearchResult getMyList(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = false, name = "status", desc = "需求状态") RequirementStatus status,
      @ApiParameter(required = true, name = "page", desc = "页码查询实体")PageQueryEntity page
  );

  @HttpApi(name = "requirement.getList", desc = "获取需求列表", security = SecurityType.None, owner = "leewind")
  @DesignedErrorCode({})
  public RequirementSearchResult getList(
      @ApiParameter(required = false, name = "type", desc = "需求类别") RequirementType type,
      @ApiParameter(required = true, name = "page", desc = "页码查询实体")PageQueryEntity page
  );

  @HttpApi(name = "requirement.getDetail", desc = "获取需求详情", security = SecurityType.None, owner = "leewind")
  @DesignedErrorCode({})
  public RequirementDetailEntity getDetail(
      @ApiParameter(required = false, name = "requirementId", desc = "需求id") long requirementId
  );

  @HttpApi(name = "requirement.apply", desc = "竞标", security = SecurityType.UserLogin, owner = "leewind")
  @DesignedErrorCode({
      RequirementErrorCode._C_REQUIREMENT_NOT_EXIST,
      RequirementErrorCode._C_ORGANIZATION_TYPE_ERROR,
      RequirementErrorCode._C_NOT_REPECT_MOTION,
      RequirementErrorCode._C_MOTION_NOT_PERMISSION
  })
  public long apply(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = true, name = "requirementId", desc = "需求id") long requirementId,
      @ApiParameter(required = true, name = "mobile", desc = "手机号") String mobile,
      @ApiParameter(required = true, name = "expectedFee", desc = "期望费用")int expectedFee,
      @ApiParameter(required = false, name = "description", desc = "描述")String description
  );
}
