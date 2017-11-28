package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
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
import io.milkt.onelab.organization.entity.MotionEntity;
import io.milkt.onelab.organization.entity.PageEntity;
import io.milkt.onelab.organization.entity.PageQueryEntity;
import io.milkt.onelab.organization.entity.RequirementDetailEntity;
import io.milkt.onelab.organization.entity.RequirementEntity;
import io.milkt.onelab.organization.entity.RequirementSearchResult;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.enums.MotionStatus;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import io.milkt.onelab.organization.manager.AddressManager;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.MotionManager;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.manager.RequirementManager;
import io.milkt.onelab.organization.service.facade.AddressFacade;
import io.milkt.onelab.user.api.UserHttpService;
import io.milkt.onelab.user.api.UserLoginHttpService;
import io.milkt.onelab.user.entity.gw.SkillCertificationInfo;
import io.milkt.onelab.user.entity.gw.UserIdentificationInfo;
import io.milkt.onelab.user.entity.gw.UserInfo;
import io.milkt.onelab.user.enums.VerifyStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceException;
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

  @Autowired
  private UserHttpService userHttpService;

  @Autowired
  private UserLoginHttpService userLoginHttpService;

  private static final Logger logger = LoggerFactory
      .getLogger(RequirementHttpExportServiceImpl.class);

  private Timestamp getRecruitFinishTime(RecruitTimeLimitEnum recruitTimeLimitEnum) {
    switch (recruitTimeLimitEnum) {
      default:
      case TWENTY_FOUR_HOURS:
        return new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
      case FOURTY_EIGHT_HOURS:
        return new Timestamp(System.currentTimeMillis() + 48 * 60 * 60 * 1000);
      case SENVENTY_TWO_HOURS:
        return new Timestamp(System.currentTimeMillis() + 72 * 60 * 60 * 1000);
    }
  }

  private List<OrganizationDO> queryOrganization(long userId, AppType channel) {
    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);
    factor.setChannel(channel.name());

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    return organizationManager.query(conditions);
  }

  private RequirementEntity buildRequirementEntity(RequirementDO requirementDO) {
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
      RecruitTimeLimitEnum recruitTimeLimit,  int fee,
      TaskFinishLimitEnum taskFinishLimit, List<String> images) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "apptype不在接受的范围内");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST,
          "organization不存在");
    } else {
      OrganizationDO organizationDO = results.get(0);

      LabDO labDO = labManager.getById(labId);
      if (labDO == null) {
        throw new ServiceRuntimeException(RequirementErrorCode.LAB_NOT_EXIST, "lab不存在");
      } else if (labDO.getOrganizationId().longValue() != organizationDO.getId().longValue()) {
        throw new ServiceRuntimeException(RequirementErrorCode.LAB_NOT_PERMISSION, "没有权限操作lab");
      }

      RequirementDO requirementDO = new RequirementDO();

      requirementDO.setType(type.name());
      requirementDO.setLabId(labId);
      requirementDO.setOrganizationId(organizationDO.getId());
      requirementDO.setDescription(description);
      requirementDO.setFee(fee);
      requirementDO.setPublishedTime(new Timestamp(System.currentTimeMillis()));
      requirementDO.setRecruitTimeLimit(recruitTimeLimit.name());
      requirementDO.setRecruitFinishTime(getRecruitFinishTime(recruitTimeLimit));
      requirementDO.setTaskFinishLimit(taskFinishLimit.name());
      requirementDO.setStatus(RequirementStatus.RECRUITING.name());
      requirementDO.setAcceptedUserId(0L);
      requirementDO.setAcceptedMotionId(0L);

      if (images != null && images.size() > 0) {
        requirementDO.setImages(StringUtils.join(images, ";"));
      }

      try {
        requirementManager.insert(requirementDO);
      } catch (DataIntegrityViolationException e) {
        throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_CREATE_ERROR, "任务创建失败", e);
      }

      return requirementDO.getId();
    }
  }

  @Override
  public RequirementSearchResult getMyList(int appid, long userId, RequirementStatus status,
      PageQueryEntity page) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "apptype不在接受的范围内");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST,
          "organization不存在");
    } else {
      OrganizationDO organizationDO = results.get(0);

      RequirementDO factor = new RequirementDO();
      factor.setOrganizationId(organizationDO.getId());
      if (status != null) {
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
      pageEntity.cucrrentNum =
          pageResult.getCurrentPage() * pageResult.getPageSize() < pageResult.getTotalItem()
              ? pageResult.getTotalItem() : pageResult.getCurrentPage() * pageResult.getPageSize();
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
    if (type != null) {
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
    pageEntity.cucrrentNum =
        pageResult.getCurrentPage() * pageResult.getPageSize() < pageResult.getTotalItem()
            ? pageResult.getTotalItem() : pageResult.getCurrentPage() * pageResult.getPageSize();
    pageEntity.pageSize = pageResult.getPageSize();
    pageEntity.totalNum = pageResult.getTotalItem();
    pageEntity.pageNum = pageResult.getCurrentPage();

    requirementSearchResult.page = pageEntity;

    return requirementSearchResult;
  }

  @Override
  public RequirementSearchResult getListByMotionStatus(int appid, long userId, MotionStatus status,
      PageQueryEntity page) {

    MotionDO factor = new MotionDO();
    factor.setUserId(userId);
    if (status != null) {
      factor.setStatus(status.name());
    }

    BaseQuery<MotionDO> conditions = BaseQuery.getInstance(factor);
    conditions.setCurrentPage(page.pageNum);
    conditions.setPageSize(page.pageSize);

    PageResult<MotionDO> pageResult = motionManager.query4Page(conditions);

    RequirementSearchResult requirementSearchResult = new RequirementSearchResult();
    requirementSearchResult.list = new ArrayList<RequirementEntity>();

    for (MotionDO motionDO : pageResult.getResult()) {
      RequirementDO requirementDO = requirementManager.getById(motionDO.getRequirementId());
      requirementSearchResult.list.add(buildRequirementEntity(requirementDO));
    }

    PageEntity pageEntity = new PageEntity();
    pageEntity.cucrrentNum =
        pageResult.getCurrentPage() * pageResult.getPageSize() < pageResult.getTotalItem()
            ? pageResult.getTotalItem() : pageResult.getCurrentPage() * pageResult.getPageSize();
    pageEntity.pageSize = pageResult.getPageSize();
    pageEntity.totalNum = pageResult.getTotalItem();
    pageEntity.pageNum = pageResult.getCurrentPage();

    requirementSearchResult.page = pageEntity;
    return requirementSearchResult;
  }

  @Override
  public RequirementDetailEntity getDetail(int appid, long userId, long requirementId) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);

    if (requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求单不存在");
    } else {

      RequirementDetailEntity requirement = new RequirementDetailEntity();
      requirement.description = requirementDO.getDescription();
      requirement.fee = requirementDO.getFee();
//      requirement.feeRange = FeeRangeEnum.valueOf(requirementDO.getFeeRange());
      requirement.recruitFinishTime = requirementDO.getRecruitFinishTime().getTime();
      requirement.type = RequirementType.valueOf(requirementDO.getType());
      requirement.requirementId = requirementDO.getId();
      if (requirementDO.getImages() != null) {
        requirement.images = Arrays.asList(StringUtils.split(requirementDO.getImages(), ';'));
      }

      LabDO lab = labManager.getById(requirementDO.getLabId());
      if (lab == null) {
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
      if (organizationDO == null) {
        throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST, "组织不存在");
      }
      labEntity.commonVerifyStatus = CommonVerifyStatus.valueOf(organizationDO.getVerifyStatus());

      AddressDO addressDO = addressManager.getById(lab.getAddressId());
      if (addressDO == null) {
        throw new ServiceRuntimeException(RequirementErrorCode.ADDRESS_NOT_EXIST, "地址不存在");
      } else {
        labEntity.address = addressFacade.buildAddressEntity(addressDO);
      }
      requirement.lab = labEntity;

      MotionDO factor = new MotionDO();
      factor.setRequirementId(requirementId);
      BaseQuery<MotionDO> conditions = BaseQuery.getInstance(factor);
      requirement.motionCount = motionManager.count(conditions);
      requirement.isJoin = false;

      List<MotionEntity> motions = new ArrayList<MotionEntity>();
      List<MotionDO> motionDOS = motionManager.query(conditions);
      for (MotionDO motionDO : motionDOS) {
        MotionEntity motionEntity = new MotionEntity();
        motionEntity.description = motionDO.getDescription();
        motionEntity.expectedFee = motionDO.getExpectedFee();
        motionEntity.startTime = motionDO.getMotionTime().getTime();
        motionEntity.status = MotionStatus.valueOf(motionDO.getStatus());
        motionEntity.motionId = motionDO.getId();

        try {
          UserInfo motionUserInfo = userHttpService
              .getUserInfo(AppType.MAINTAINER.getCode(), motionDO.getUserId());
          motionEntity.userIcon = motionUserInfo.headImg;
          motionEntity.userMobile = motionUserInfo.mobile;
          motionEntity.userName = motionUserInfo.nick;

          UserIdentificationInfo userIdentificationInfo = userLoginHttpService
              .getIndentifationInfo(motionUserInfo.userId);

          motionEntity.userCommonVerifyStatus =
              userIdentificationInfo.verifyStatus == VerifyStatus.SUBMITED
                  ? CommonVerifyStatus.APPLY
                  : CommonVerifyStatus.valueOf(userIdentificationInfo.verifyStatus.name());

          List<SkillCertificationInfo> skillCertificationInfos = userHttpService.getSkillCerts(motionUserInfo.userId);
          List<String> skills = new ArrayList<String>();
          for(SkillCertificationInfo skillCertificationInfo: skillCertificationInfos){
            skills.add(skillCertificationInfo.skillName);
          }
          motionEntity.skills = skills;

        } catch (ServiceException e) {
          logger.error(e.getMessage());
        }

        requirement.isJoin = requirement.isJoin || motionDO.getUserId() == userId;

        motions.add(motionEntity);
      }
      requirement.motions = motions;

      try {
        UserInfo userInfo = userHttpService
            .getUserInfo(AppType.PUBLISHER.getCode(), organizationDO.getUserId());
        requirement.mobile = userInfo.mobile;
      } catch (ServiceException e) {
        throw new ServiceRuntimeException(RequirementErrorCode.PUBLISHER_INFO_ERROR, "获取发布人信息错误",
            e);
      }

      requirement.acceptedMotionId = requirementDO.getAcceptedMotionId();
      requirement.status = RequirementStatus.valueOf(requirementDO.getStatus());

      return requirement;
    }
  }

  @Override
  public long apply(int appid, long userId, long requirementId, String mobile, int expectedFee,
      String description) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);
    if (requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求不存在");
    } else if (requirementDO.getRecruitFinishTime().getTime() < System.currentTimeMillis() ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.CLOSE ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.FINISHED) {
      throw new ServiceRuntimeException(RequirementErrorCode.MOTION_NOT_PERMISSION, "需求已关闭，不接受竞价");
    }

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.PUBLISHER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "apptype不在接受的范围内");
    }

    try {
      UserIdentificationInfo userIdentificationInfo = userLoginHttpService
          .getIndentifationInfo(userId);
      if (VerifyStatus.valueOf(userIdentificationInfo.verifyStatus.name()) != VerifyStatus.SUCCESS) {
        throw new ServiceRuntimeException(RequirementErrorCode.USER_NOT_PERMITTED, "用户没有审核通过无法申请");
      }
    } catch (ServiceException e) {
      throw new ServiceRuntimeException(RequirementErrorCode.PUBLISHER_INFO_ERROR, "获取申请人信息错误", e);
    }

    MotionDO factor = new MotionDO();
    factor.setUserId(userId);
    factor.setRequirementId(requirementId);

    BaseQuery<MotionDO> conditions = BaseQuery.getInstance(factor);
    long result = motionManager.count(conditions);
    if (result > 0) {
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

  @Override
  public boolean approve(int appid, long userId, long requirementId, long motionId) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);
    if (requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求不存在");
    } else if (requirementDO.getRecruitFinishTime().getTime() < System.currentTimeMillis() ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.CLOSE ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.FINISHED) {
      throw new ServiceRuntimeException(RequirementErrorCode.MOTION_NOT_PERMISSION, "需求已关闭");
    }

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "apptype不在接受的范围内");
    }

    OrganizationDO organizationDO = organizationManager.getById(requirementDO.getOrganizationId());
    if(organizationDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZAION_NOT_EXIST, "组织不存在");
    }else if(organizationDO.getUserId() != userId){
      throw new ServiceRuntimeException(RequirementErrorCode.USER_NOT_PERMITTED, "用户没有操作权限");
    }

    MotionDO motionDO = motionManager.getById(motionId);
    if (motionDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.MOTION_NOT_EXIST, "竞标不存在");
    }

    requirementDO.setAcceptedMotionId(motionId);
    requirementDO.setAcceptedUserId(motionDO.getUserId());
    requirementDO.setStatus(RequirementStatus.PROCESSING.name());
    requirementManager.update(requirementDO);

    MotionDO factor = new MotionDO();
    factor.setRequirementId(requirementId);

    BaseQuery<MotionDO> condition = BaseQuery.getInstance(factor);
    List<MotionDO> motionList = motionManager.query(condition);

    for(MotionDO motion: motionList) {
      if(motion.getId() == motionId){
        motion.setStatus(MotionStatus.PROCESSING.name());
      }else{
        motion.setStatus(MotionStatus.FAIL.name());
      }
      motionManager.update(motion);
    }

    return true;
  }
}
