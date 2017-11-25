package io.milkt.onelab.organization.api;


import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
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
  @DesignedErrorCode({})
  public long save(
      @ApiAutowired(AutowireableParameter.appid) int appid,
      @ApiAutowired(AutowireableParameter.userid) long userId,
      @ApiParameter(required = true, name = "labId", desc = "实验室id") long labId,
      @ApiParameter(required = true, name = "type", desc = "任务类型") RequirementType type,
      @ApiParameter(required = false, name = "description", desc = "任务描述") String description,
      @ApiParameter(required = true, name = "recruitTimeLimit", desc = "招募时间限制")RecruitTimeLimitEnum recruitTimeLimit,
      @ApiParameter(required = true, name = "feeRange", desc = "招募时间限制")FeeRangeEnum feeRange,
      @ApiParameter(required = true, name = "taskFinishLimit", desc = "任务完成期限")TaskFinishLimitEnum taskFinishLimit,
      @ApiParameter(required = false, name = "images", desc = "图片")List<String> images
  );
}
