package io.milkt.onelab.organization.service.facade;

import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.entity.OrganizationEntity;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;

public class OrganizationFacade {

  public OrganizationEntity buildOrganizationEntity(OrganizationDO organizationDO) {
    OrganizationEntity organizationEntity = new OrganizationEntity();
    organizationEntity.attachmentCredentialUrl = organizationDO.getAttachmentCredentialUrl();
    organizationEntity.name = organizationDO.getName();
    organizationEntity.commonVerifyStatus = CommonVerifyStatus.valueOf(organizationDO.getVerifyStatus());
    return organizationEntity;
  }

}
