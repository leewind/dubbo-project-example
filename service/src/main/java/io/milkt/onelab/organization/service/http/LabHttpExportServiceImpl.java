package io.milkt.onelab.organization.service.http;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.api.LabHttpExportService;
import io.milkt.onelab.organization.domain.LabDO;
import io.milkt.onelab.organization.domain.OrganizationDO;
import io.milkt.onelab.organization.entity.AddressInsertEntity;
import io.milkt.onelab.organization.enums.AppType;
import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.exception.LabErrorCode;
import io.milkt.onelab.organization.manager.LabManager;
import io.milkt.onelab.organization.manager.OrganizationManager;
import io.milkt.onelab.organization.service.facade.AddressFacade;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class LabHttpExportServiceImpl implements LabHttpExportService {

  @Resource
  private OrganizationManager organizationManager;

  @Resource
  private LabManager labManager;

  @Autowired
  private AddressFacade addressFacade;

  private static final Logger logger = LoggerFactory.getLogger(LabHttpExportServiceImpl.class);

  @Override
  public long save(int appid, long userId, long labId, String name, LabType type,
      AddressInsertEntity address,
      String phone) {

    AppType channel = AppType.getEnumByCode(appid);
    if (channel == null || channel == AppType.MAINTAINER) {
      throw new ServiceRuntimeException(LabErrorCode.ORGANIZATION_TYPE_ERROR, "apptype不在接受的范围内");
    }

    OrganizationDO factor = new OrganizationDO();
    factor.setUserId(userId);
    factor.setChannel(channel.name());

    BaseQuery<OrganizationDO> conditions = BaseQuery.getInstance(factor);

    List<OrganizationDO> results = organizationManager.query(conditions);
    if (results.size() == 0) {
      throw new ServiceRuntimeException(LabErrorCode.ORGANIZAION_NOT_EXIST, "organization不存在");
    } else {
      OrganizationDO organizationDO = results.get(0);

      LabDO labDO = new LabDO();
      if (labId > 0) {
        labDO = labManager.getById(labId);
      }

      try {
        long addressId = addressFacade.save(address, labDO.getAddressId() != null ? labDO.getAddressId() : 0);
        labDO.setAddressId(addressId);
      } catch (DataIntegrityViolationException e) {
        throw new ServiceRuntimeException(LabErrorCode.ADDRESS_CREATE_ERROR, "address创建失败");
      }

      labDO.setName(name);
      labDO.setPhone(phone);
      labDO.setType(type.name());
      labDO.setOrganizationId(organizationDO.getId());

      try {
        labManager.insertOrUpdate(labDO);
      } catch (DataIntegrityViolationException e) {
        throw new ServiceRuntimeException(LabErrorCode.LAB_CREATE_ERROR, "address创建失败");
      }

      return labDO.getId();
    }
  }
}
