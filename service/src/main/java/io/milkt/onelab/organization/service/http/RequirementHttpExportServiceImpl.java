package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.PageResult;
import io.milkt.onelab.organization.api.RequirementHttpExportService;
import io.milkt.onelab.organization.domain.AddressDO;
import io.milkt.onelab.organization.domain.LabDO;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.domain.RequirementDO;
import io.milkt.onelab.organization.entity.PageEntity;
import io.milkt.onelab.organization.entity.PageQueryEntity;
import io.milkt.onelab.organization.entity.RequirementEntity;
import io.milkt.onelab.organization.entity.RequirementSearchResult;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import io.milkt.onelab.organization.manager.AddressManager;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.manager.RequirementManager;
import io.milkt.onelab.organization.service.facade.AddressFacade;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class RequirementHttpExportServiceImpl implements RequirementHttpExportService {

  @Resource
  private OrganizationManager organizationManager;

  @Resource
  private RequirementManager requirementManager;

  @Resource
  private AddressManager addressManager;

  @Resource
  private LabManager labManager;

  @Autowired
  private AddressFacade addressFacade;

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

  private List<OrganizationDO> queryOrganization(long userId, AppType channel) {
    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);
    factor.setChannel(channel.name());

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    return organizationManager.query(conditions);
  }

  private RequirementEntity buildRequirementEntity(RequirementDO requirementDO){
    RequirementEntity requirementEntity = new RequirementEntity();
    requirementEntity.description = requirementDO.getDescription();
    requirementEntity.recruitFinishTime = requirementDO.getRecruitFinishTime().getTime();
    requirementEntity.type = RequirementType.valueOf(requirementDO.getType());
    requirementEntity.requirementId = requirementDO.getId();

    LabDO labDO = labManager.getById(requirementDO.getLabId());
    AddressDO addressDO = addressManager.getById(labDO.getAddressId());

    requirementEntity.address = addressFacade.buildAddressEntity(addressDO);
    return requirementEntity;
  }

  @Override
  public long save(int appid, long userId, long labId, RequirementType type, String description,
      RecruitTimeLimitEnum recruitTimeLimit, FeeRangeEnum feeRange,
      TaskFinishLimitEnum taskFinishLimit, List<String> images) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
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

  @Override
  public RequirementSearchResult getMyList(int appid, long userId, RequirementStatus status, PageQueryEntity page) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST, "organization不存在");
    } else {
      OrganizationDO organizationDO = results.get(0);

      RequirementDO factor = new RequirementDO();
      factor.setOrganizationId(organizationDO.getId());
      if(status != null){
        factor.setStatus(status.name());
      }

      BaseQuery<RequirementDO> conditions = BaseQuery.getInstance(factor);
      conditions.setCurrentPage(page.pageNum);
      conditions.setPageSize(page.pageSize);

      PageResult<RequirementDO> pageResult = requirementManager.query4Page(conditions);

      RequirementSearchResult requirementSearchResult = new RequirementSearchResult();
      requirementSearchResult.list = new ArrayList<RequirementEntity>();

      for (RequirementDO requirementDO : pageResult.getResult()) {
        requirementSearchResult.list.add(buildRequirementEntity(requirementDO));
      }

      PageEntity pageEntity = new PageEntity();
      pageEntity.cucrrentNum = pageResult.getCurrentPage() * pageResult.getPageSize() < pageResult.getTotalItem()? pageResult.getTotalItem() : pageResult.getCurrentPage() * pageResult.getPageSize();
      pageEntity.pageSize = pageResult.getPageSize();
      pageEntity.totalNum = pageResult.getTotalItem();
      pageEntity.pageNum = pageResult.getCurrentPage();

      requirementSearchResult.page = pageEntity;

      return requirementSearchResult;
    }
  }
}
