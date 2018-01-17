package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.sfebiz.common.dao.domain.BaseQuery;
import com.sfebiz.common.dao.domain.PageResult;
import io.milkt.onelab.organization.api.RequirementHttpExportService;
import io.milkt.onelab.organization.domain.AddressDO;
import io.milkt.onelab.organization.domain.LabDO;
import io.milkt.onelab.organization.domain.MessageDO;
import io.milkt.onelab.organization.domain.MotionDO;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.domain.RequirementDO;
import io.milkt.onelab.organization.entity.LabEntity;
import io.milkt.onelab.organization.entity.MessageEntity;
import io.milkt.onelab.organization.entity.MessageSearchResult;
import io.milkt.onelab.organization.entity.MotionEntity;
import io.milkt.onelab.organization.entity.PageEntity;
import io.milkt.onelab.organization.entity.PageQueryEntity;
import io.milkt.onelab.organization.entity.RequirementDetailEntity;
import io.milkt.onelab.organization.entity.RequirementEntity;
import io.milkt.onelab.organization.entity.RequirementSearchResult;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;
import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.enums.MessageStatus;
import io.milkt.onelab.organization.enums.MotionStatus;
import io.milkt.onelab.organization.enums.RecruitTimeLimitEnum;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.enums.RequirementType;
import io.milkt.onelab.organization.enums.TaskFinishLimitEnum;
import io.milkt.onelab.organization.exception.RequirementErrorCode;
import io.milkt.onelab.organization.manager.AddressManager;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.MessageManager;
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

  @Resource
  private MessageManager messageManager;

  private static final Logger logger = LoggerFactory
      .getLogger(RequirementHttpExportServiceImpl.class);

  private Timestamp getRecruitFinishTime(RecruitTimeLimitEnum recruitTimeLimitEnum) {
    switch (recruitTimeLimitEnum) {
      case FOURTY_EIGHT_HOURS:
        return new Timestamp(System.currentTimeMillis() + 48 * 60 * 60 * 1000);
      case SENVENTY_TWO_HOURS:
        return new Timestamp(System.currentTimeMillis() + 72 * 60 * 60 * 1000);
      case SEVEN_DAY:
        return new Timestamp(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
      case TWENTY_FOUR_HOURS:
      default:
        return new Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
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
      RecruitTimeLimitEnum recruitTimeLimit, int fee,
      TaskFinishLimitEnum taskFinishLimit, List<String> images) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "该账号不能在该端使用");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_NOT_EXIST,
          "请先做企业认证");
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

        // @TODO 这里要进行修改，需要用线程异步进行处理
        MessageDO message4Publisher = new MessageDO();
        message4Publisher.setTarget(userId);
        message4Publisher.setStatus(MessageStatus.UNREAD.name());
        message4Publisher.setTitle("维修招募信息发布");
        message4Publisher.setSubtitle(description);
        message4Publisher.setSource("REQUIREMENT");
        message4Publisher.setPublishTime(new Timestamp(System.currentTimeMillis()));
        message4Publisher
            .setContent("您发布了一条维修招募信息\n实验室: " + labDO.getName() + "\n任务描述：" + description + "\"");
        messageManager.insert(message4Publisher);

        AddressDO addressDO = addressManager.getById(labDO.getAddressId());
        if (addressDO != null) {
          List<MessageDO> message2mantainers = messageManager
              .getUserIdLinkUserDB(String.valueOf(addressDO.getCityAdcode()));
          for (MessageDO message2mantainer : message2mantainers) {
            logger.debug("发布成功，向用户发布信息，获取用户id为：" + message2mantainer.getTarget());
            message2mantainer.setStatus(MessageStatus.UNREAD.name());
            message2mantainer.setTitle("有实验室发布了维修招募信息");
            message2mantainer.setSubtitle(description);
            message2mantainer.setSource("REQUIREMENT");
            message2mantainer.setPublishTime(new Timestamp(System.currentTimeMillis()));
            message2mantainer.setContent(
                labDO.getName() + "发布了一条维修招募信息\n实验室: " + labDO.getName() + "\n任务描述：" + description
                    + "\"");
            messageManager.insert(message2mantainer);
          }
        }

      } catch (DataIntegrityViolationException e) {
        throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_CREATE_ERROR, "任务创建失败",
            e);
      }

      return requirementDO.getId();
    }
  }

  @Override
  public RequirementSearchResult getMyList(int appid, long userId, RequirementStatus status,
      RequirementType type, PageQueryEntity page) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "该账号不能在该端使用");
    }

    List<OrganizationDO> results = queryOrganization(userId, channel);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_NOT_EXIST,
          "请先做企业认证");
    } else {
      OrganizationDO organizationDO = results.get(0);

      RequirementDO factor = new RequirementDO();
      factor.setOrganizationId(organizationDO.getId());
      if (status != null) {
        factor.setStatus(status.name());
      }

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
  public List<RequirementEntity> getSuccessList() {

    RequirementDO factor = new RequirementDO();
    factor.setStatus(RequirementStatus.FINISHED.name());

    BaseQuery<RequirementDO> conditions = BaseQuery.getInstance(factor);
    conditions.addOrderBy("task_finish_time", 0);
    conditions.setPageSize(10);
    conditions.setCurrentPage(1);
    PageResult<RequirementDO> pageResult = requirementManager.query4Page(conditions);

    List<RequirementEntity> requirements = new ArrayList<RequirementEntity>();

    for (RequirementDO requirementDO : pageResult.getResult()) {
      requirements.add(buildRequirementEntity(requirementDO));
    }

    return requirements;
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
        throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_NOT_EXIST, "请先做企业认证");
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

          List<SkillCertificationInfo> skillCertificationInfos = userHttpService
              .getSkillCerts(motionUserInfo.userId);
          List<String> skills = new ArrayList<String>();
          for (SkillCertificationInfo skillCertificationInfo : skillCertificationInfos) {
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
          "该账号不能在该端使用");
    }

    try {
      UserIdentificationInfo userIdentificationInfo = userLoginHttpService
          .getIndentifationInfo(userId);
      if (userIdentificationInfo == null
          || VerifyStatus.valueOf(userIdentificationInfo.verifyStatus.name())
          != VerifyStatus.SUCCESS) {
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

    try {
      UserInfo userInfo = userHttpService.getUserInfo(appid, userId);

      MessageDO message4Publisher = new MessageDO();
      message4Publisher.setTarget(userId);
      message4Publisher.setStatus(MessageStatus.UNREAD.name());
      message4Publisher.setTitle("用户" + userInfo.nick + "应聘了您的任务");
      message4Publisher.setSubtitle(description);
      message4Publisher.setSource("REQUIREMENT");
      message4Publisher.setPublishTime(new Timestamp(System.currentTimeMillis()));
      message4Publisher
          .setContent("用户" + userInfo.nick + "应聘了您的招募 - " + requirementDO.getDescription());
      messageManager.insert(message4Publisher);
    } catch (ServiceException e) {
      logger.error(e.getMsg());
    }

    return motionDO.getId();
  }

  @Override
  public int withdraw(int appid, long userId, long requirementId) {
    RequirementDO requirementDO = requirementManager.getById(requirementId);
    if (requirementDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_NOT_EXIST, "需求不存在");
    } else if (requirementDO.getRecruitFinishTime().getTime() < System.currentTimeMillis() ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.CLOSE ||
        RequirementStatus.valueOf(requirementDO.getStatus()) == RequirementStatus.FINISHED) {
      throw new ServiceRuntimeException(RequirementErrorCode.MOTION_NOT_PERMISSION, "需求已关闭");
    } else if (RequirementStatus.valueOf(requirementDO.getStatus())
        != RequirementStatus.RECRUITING) {
      throw new ServiceRuntimeException(RequirementErrorCode.REQUIREMENT_STATUS_LOCK, "需求状态已经锁定");
    }

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_TYPE_ERROR,
          "该账号不能在该端使用");
    }

    OrganizationDO organizationDO = organizationManager.getById(requirementDO.getOrganizationId());
    if (organizationDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_NOT_EXIST, "请先做企业认证");
    } else if (organizationDO.getUserId() != userId) {
      throw new ServiceRuntimeException(RequirementErrorCode.USER_NOT_PERMITTED, "用户没有操作权限");
    }

    requirementDO.setStatus(RequirementStatus.CLOSE.name());
    return requirementManager.update(requirementDO);
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
          "该账号不能在该端使用");
    }

    OrganizationDO organizationDO = organizationManager.getById(requirementDO.getOrganizationId());
    if (organizationDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.ORGANIZATION_NOT_EXIST, "请先做企业认证");
    } else if (organizationDO.getUserId() != userId) {
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

    for (MotionDO motion : motionList) {
      if (motion.getId() == motionId) {
        motion.setStatus(MotionStatus.PROCESSING.name());

        MessageDO message4Applier = new MessageDO();
        message4Applier.setTarget(motion.getUserId());
        message4Applier.setStatus(MessageStatus.UNREAD.name());
        message4Applier.setTitle("您的应聘通过了");
        message4Applier.setSubtitle("您的应聘通过的甲方审核");
        message4Applier.setSource("REQUIREMENT");
        message4Applier.setPublishTime(new Timestamp(System.currentTimeMillis()));
        message4Applier
            .setContent("您应聘的了一条维修招募信息：" + requirementDO.getDescription() + "，通过了对方审核");
        messageManager.insert(message4Applier);

      } else {
        MessageDO message4Applier = new MessageDO();
        message4Applier.setTarget(motion.getUserId());
        message4Applier.setStatus(MessageStatus.UNREAD.name());
        message4Applier.setTitle("很遗憾，您的应聘没有被通过");
        message4Applier.setSubtitle("您的应聘没有通过甲方审核");
        message4Applier.setSource("REQUIREMENT");
        message4Applier.setPublishTime(new Timestamp(System.currentTimeMillis()));
        message4Applier
            .setContent("您应聘的了一条维修招募信息：" + requirementDO.getDescription() + "，没有通过对方审核");
        messageManager.insert(message4Applier);

        motion.setStatus(MotionStatus.FAIL.name());
      }
      motionManager.update(motion);
    }

    return true;
  }

  private MessageEntity buildMessageEntity(MessageDO messageDO) {
    MessageEntity messageEntity = new MessageEntity();

    messageEntity.content = messageDO.getContent();
    messageEntity.publishTime = messageDO.getPublishTime().getTime();
    if (messageDO.getReadTime() != null) {
      messageEntity.readTime = messageDO.getReadTime().getTime();
    }

    messageEntity.source = messageDO.getSource();
    messageEntity.status = MessageStatus.valueOf(messageDO.getStatus());
    messageEntity.subtitle = messageDO.getSubtitle();
    messageEntity.target = messageDO.getTarget();
    messageEntity.title = messageDO.getTitle();
    messageEntity.id = messageDO.getId();
    return messageEntity;
  }

  @Override
  public MessageSearchResult getMessageList(int appid, long userId, MessageStatus status,
      PageQueryEntity page) {

    MessageDO factor = new MessageDO();
    factor.setTarget(userId);
    if (status != null) {
      factor.setStatus(status.name());
    }

    BaseQuery<MessageDO> conditions = BaseQuery.getInstance(factor);
    conditions.setCurrentPage(page.pageNum);
    conditions.setPageSize(page.pageSize);
    conditions.addOrderBy("publish_time", 0);

    PageResult<MessageDO> pageResult = messageManager.query4Page(conditions);

    MessageSearchResult messageSearchResult = new MessageSearchResult();
    messageSearchResult.list = new ArrayList<MessageEntity>();

    for (MessageDO messageDO : pageResult.getResult()) {
      messageSearchResult.list.add(buildMessageEntity(messageDO));
    }

    PageEntity pageEntity = new PageEntity();
    pageEntity.cucrrentNum =
        pageResult.getCurrentPage() * pageResult.getPageSize() < pageResult.getTotalItem()
            ? pageResult.getTotalItem() : pageResult.getCurrentPage() * pageResult.getPageSize();
    pageEntity.pageSize = pageResult.getPageSize();
    pageEntity.totalNum = pageResult.getTotalItem();
    pageEntity.pageNum = pageResult.getCurrentPage();

    messageSearchResult.page = pageEntity;

    return messageSearchResult;
  }

  @Override
  public long getUnreadNum(int appid, long userId) {

    MessageDO factor = new MessageDO();
    factor.setTarget(userId);
    factor.setStatus(MessageStatus.UNREAD.name());

    BaseQuery<MessageDO> conditions = BaseQuery.getInstance(factor);

    return messageManager.count(conditions);
  }

  @Override
  public boolean readMessage(int appid, long userId, long messageId) {
    MessageDO messageDO = messageManager.getById(messageId);
    if (messageDO == null) {
      throw new ServiceRuntimeException(RequirementErrorCode.MESSAGE_NOT_EXIST);
    } else if (userId != messageDO.getTarget()) {
      throw new ServiceRuntimeException(RequirementErrorCode.USER_NOT_PERMITTED);
    } else {
      messageDO.setStatus(MessageStatus.READ.name());
      messageDO.setReadTime(new Timestamp(System.currentTimeMillis()));
      return messageManager.update(messageDO) > 0;
    }
  }
}
