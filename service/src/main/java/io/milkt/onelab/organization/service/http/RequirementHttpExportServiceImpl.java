package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.PageResult;
import io.milkt.onelab.organization.api.RequirementHttpExportService;
import io.milkt.onelab.organization.domain.AddressDO;
import io.milkt.onelab.organization.domain.LabDO;
import io.milkt.onelab.organization.domain.MotionDO;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.domain.RequirementDO;
import io.milkt.onelab.organization.entity.LabEntity;
import io.milkt.onelab.organization.entity.PageEntity;
import io.milkt.onelab.organization.entity.PageQueryEntity;
import io.milkt.onelab.organization.entity.RequirementDetailEntity;
import io.milkt.onelab.organization.entity.RequirementEntity;
import io.milkt.onelab.organization.entity.RequirementSearchResult;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.FeeRangeEnum;
import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.enums.MotionStatus;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.enums.VerifyStatus;
import io.milkt.onelab.organization.exception.LabErrorCode;
import io.milkt.onelab.organization.exception.OrganizationErrorCode;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import io.milkt.onelab.organization.manager.AddressManager;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.MotionManager;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.manager.RequirementManager;
import io.milkt.onelab.organization.service.facade.AddressFacade;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

  @Resource
  private MotionManager motionManager;

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
      conditions.addOrderBy("published_time", 0);

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

  @Override
  public RequirementSearchResult getList(RequirementType type, PageQueryEntity page) {

      RequirementDO factor = new RequirementDO();
      if(type != null){
        factor.setType(type.name());
      }

      BaseQuery<RequirementDO> conditions = BaseQuery.getInstance(factor);
      conditions.setCurrentPage(page.pageNum);
      conditions.setPageSize(page.pageSize);
      conditions.addOrderBy("published_time", 0);

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

  @Override
  public RequirementDetailEntity getDetail(long requirementId) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);

    if (requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求单不存在");
    }else{

      RequirementDetailEntity requirement = new RequirementDetailEntity();
      requirement.description = requirementDO.getDescription();
      requirement.feeRange = FeeRangeEnum.valueOf(requirementDO.getFeeRange());
      requirement.recruitFinishTime = requirementDO.getRecruitFinishTime().getTime();
      requirement.type = RequirementType.valueOf(requirementDO.getType());
      requirement.requirementId = requirementDO.getId();
      if(requirementDO.getImages() != null) {
        requirement.images = Arrays.asList(StringUtils.split(requirementDO.getImages(), ';'));
      }

      LabDO lab = labManager.getById(requirementDO.getLabId());
      if (lab  == null){
        throw new ServiceRuntimeException(RequirementErrorCode.LAB_NOT_EXIST, "实验室不存在");
      }

      LabEntity labEntity = new LabEntity();
      labEntity.labId = lab.getId();
      labEntity.name = lab.getName();
      labEntity.phone = lab.getPhone();
      labEntity.organizationId = lab.getOrganizationId();
      labEntity.type = LabType.valueOf(lab.getType());
      labEntity.level = lab.getLevel();

      OrganizationDO organizationDO = organizationManager.getById(lab.getOrganizationId());
      if (organizationDO == null){
        throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST, "组织不存在");
      }
      labEntity.verifyStatus = VerifyStatus.valueOf(organizationDO.getVerifyStatus());

      AddressDO addressDO = addressManager.getById(lab.getAddressId());
      if (addressDO == null){
        throw new ServiceRuntimeException(RequirementErrorCode.ADDRESS_NOT_EXIST, "地址不存在");
      } else {
        labEntity.address = addressFacade.buildAddressEntity(addressDO);
      }
      requirement.lab = labEntity;

      MotionDO factor = new MotionDO();
      factor.setRequirementId(requirementId);
      BaseQuery<MotionDO> conditions = BaseQuery.getInstance(factor);
      requirement.motionCount = motionManager.count(conditions);

      return requirement;
    }
  }

  @Override
  public long apply(int appid, long userId, long requirementId, String mobile, int expectedFee, String description) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);
    if(requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求不存在");
    }else if(requirementDO.getRecruitFinishTime().getTime() < System.currentTimeMillis() ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.CLOSE ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.FINISHED){
      throw new ServiceRuntimeException(RequirementErrorCode.MOTION_NOT_PERMISSION, "需求已关闭，不接受竞价");
    }

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.PUBLISHER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    MotionDO factor = new MotionDO();
    factor.setUserId(userId);
    factor.setRequirementId(requirementId);

    BaseQuery<MotionDO> conditions = BaseQuery.getInstance(factor);
    long result = motionManager.count(conditions);
    if (result > 0){
      throw new ServiceRuntimeException(RequirementErrorCode.NOT_REPECT_MOTION, "不能重复竞价");
    }

    MotionDO motionDO = new MotionDO();
    motionDO.setDescription(description);
    motionDO.setExpectedFee(expectedFee);
    motionDO.setMobile(mobile);
    motionDO.setRequirementId(requirementId);
    motionDO.setMotionTime(new Timestamp(System.currentTimeMillis()));
    motionDO.setStatus(MotionStatus.APPLY.name());
    motionDO.setUserId(userId);

    motionManager.insert(motionDO);
    return motionDO.getId();
  }
}
