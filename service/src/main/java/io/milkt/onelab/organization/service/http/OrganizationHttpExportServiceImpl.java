package io.milkt.onelab.organization.service.http;

import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.api.OrganizationHttpExportService;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.entity.OrganizationEntity;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;
import io.milkt.onelab.organization.exception.OrganizationErrorCode;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.service.facade.OrganizationFacade;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;

public class OrganizationHttpExportServiceImpl implements OrganizationHttpExportService {

  @Autowired
  private OrganizationFacade organizationFacade;

  @Resource
  private OrganizationManager organizationManager;

  @Override
  public long save(int appid, long userId, String name, String attachmentCredentialUrl) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null) {
      throw new ServiceRuntimeException(OrganizationErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    List<OrganizationDO> results = organizationManager.query(conditions);
    if (results.size() == 0) {
      OrganizationDO organizationDO = new OrganizationDO();
      organizationDO.setUserId(userId);
      organizationDO.setName(name);
      organizationDO.setAttachmentCredentialUrl(attachmentCredentialUrl);
      organizationDO.setVerifyStatus(CommonVerifyStatus.APPLY.name());
      organizationDO.setChannel(AppType.getEnumByCode(appid).name());

      organizationManager.insert(organizationDO);
      return organizationDO.getId();
    }else{
      OrganizationDO organizationDO = results.get(0);
      if (CommonVerifyStatus.valueOf(organizationDO.getVerifyStatus()) == CommonVerifyStatus.SUCCESS) {
        throw new ServiceRuntimeException(OrganizationErrorCode.ORGANIZATION_VERIFY_SUCCESS, "已经认证不能再修改");
      }else{
        organizationDO.setName(name);
        organizationDO.setAttachmentCredentialUrl(attachmentCredentialUrl);
        organizationDO.setVerifyStatus(CommonVerifyStatus.APPLY.name());
        organizationDO.setChannel(AppType.getEnumByCode(appid).name());

        organizationManager.update(organizationDO);
        return organizationDO.getId();
      }
    }
  }

  @Override
  public OrganizationEntity get(int appid, long userId) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null) {
      throw new ServiceRuntimeException(OrganizationErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);
    factor.setChannel(channel.name());

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    List<OrganizationDO> results = organizationManager.query(conditions);

    if (results.size() == 0) {
      throw new ServiceRuntimeException(OrganizationErrorCode.ORGANIZAION_NOT_EXIST, "organization不存在");
    }else{
      OrganizationDO organizationDO = results.get(0);
      return organizationFacade.buildOrganizationEntity(organizationDO);
    }
  }
}
