package io.milkt.onelab.organization.service.http;

import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.api.OrganizationHttpExportService;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.enums.VerifyStatus;
import io.milkt.onelab.organization.exception.OrganizationErrorCode;
import io.milkt.onelab.organization.manager.OrganizationManager;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;

public class OrganizationHttpExportServiceImpl implements OrganizationHttpExportService {

  @Resource
  private OrganizationManager organizationManager;

  @Override
  public long save(int appid, long userId, String name, String attachmentCredentialUrl) {

    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    List<OrganizationDO> results = organizationManager.query(conditions);
    if (results.size() == 0) {
      OrganizationDO organizationDO = new OrganizationDO();
      organizationDO.setUserId(userId);
      organizationDO.setName(name);
      organizationDO.setAttachmentCredentialUrl(attachmentCredentialUrl);
      organizationDO.setVerifyStatus(VerifyStatus.INIT.name());

      organizationManager.insert(organizationDO);
      return organizationDO.getId();
    }else{
      OrganizationDO organizationDO = results.get(0);
      if (VerifyStatus.valueOf(organizationDO.getVerifyStatus()) == VerifyStatus.SUCCESS) {
        throw new ServiceRuntimeException(OrganizationErrorCode.ORGANIZATION_VERIFY_SUCCESS, "已经认证不能再修改");
      }else{
        organizationDO.setName(name);
        organizationDO.setAttachmentCredentialUrl(attachmentCredentialUrl);
        organizationDO.setVerifyStatus(VerifyStatus.INIT.name());

        organizationManager.update(organizationDO);
        return organizationDO.getId();
      }
    }
  }
}
