package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.api.RequirementHttpExportService;
import io.milkt.onelab.organization.domain.LabDO;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.domain.RequirementDO;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.manager.RequirementManager;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;

public class RequirementHttpExportServiceImpl implements RequirementHttpExportService {

  @Resource
  private OrganizationManager organizationManager;

  @Resource
  private RequirementManager requirementManager;

  @Resource
  private LabManager labManager;

  private Timestamp getRecruitFinishTime(RecruitTimeLimitEnum recruitTimeLimitEnum) {
    switch (recruitTimeLimitEnum){
      default:
      case TWENTY_FOUR_HOURS:
        return new Timestamp(System.currentTimeMillis() + 24*60*60*1000);
      case FOURTY_EIGHT_HOURS:
        return new Timestamp(System.currentTimeMillis() + 48*60*60*1000);
      case SENVENTY_TWO_HOURS:
        return new Timestamp(System.currentTimeMillis() + 72*60*60*1000);
    }
  }

  @Override
  public long save(int appid, long userId, long labId, RequirementType type, String description,
      RecruitTimeLimitEnum recruitTimeLimit, FeeRangeEnum feeRange,
      TaskFinishLimitEnum taskFinishLimit, List<String> images) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);
    factor.setChannel(channel.name());

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    List<OrganizationDO> results = organizationManager.query(conditions);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST, "organization不存在");
    } else {
      OrganizationDO organizationDO = results.get(0);

      LabDO labDO = labManager.getById(labId);
      if (labDO == null) {
        throw new ServiceRuntimeException(RequirementErrorCode.LAB_NOT_EXIST, "lab不存在");
      }else if(labDO.getOrganizationId().longValue() != organizationDO.getId().longValue()) {
        throw new ServiceRuntimeException(RequirementErrorCode.LAB_NOT_PERMISSION, "没有权限操作lab");
      }

      RequirementDO requirementDO = new RequirementDO();

      requirementDO.setType(type.name());
      requirementDO.setLabId(labId);
      requirementDO.setOrganizationId(organizationDO.getId());
      requirementDO.setDescription(description);
      requirementDO.setFeeRange(feeRange.name());
      requirementDO.setPublishedTime(new Timestamp(System.currentTimeMillis()));
      requirementDO.setRecruitTimeLimit(recruitTimeLimit.name());
      requirementDO.setRecruitFinishTime(getRecruitFinishTime(recruitTimeLimit));
      requirementDO.setTaskFinishLimit(taskFinishLimit.name());
      requirementDO.setStatus(RequirementStatus.RECRUITING.name());

      if (images != null && images.size() > 0){
        requirementDO.setImages(StringUtils.join(images, ";"));
      }

      try {
        requirementManager.insert(requirementDO);
      }catch (DataIntegrityViolationException e){
        throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_CREATE_ERROR, "任务创建失败");
      }

      return  requirementDO.getId();
    }
  }
}
