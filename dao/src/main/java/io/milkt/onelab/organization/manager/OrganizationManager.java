package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.OrganizationDao;
import io.milkt.onelab.organization.domain.OrganizationDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("organizationManager")
public class OrganizationManager extends BaseManager<OrganizationDO> {

  @Resource
  private OrganizationDao organizationDao;

  @Override
  public BaseDao<OrganizationDO> getDao() {
    return organizationDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/organization-sql-mapper.xml",
        OrganizationDao.class, OrganizationDO.class, "organization");
  }
}
