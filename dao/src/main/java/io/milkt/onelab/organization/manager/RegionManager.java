package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.RegionDao;
import io.milkt.onelab.organization.domain.RegionDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("regionManager")
public class RegionManager extends BaseManager<RegionDO> {

  @Resource
  private RegionDao regionDao;

  @Override
  public BaseDao<RegionDO> getDao() {
    return regionDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/region-sql-mapper.xml",
        RegionDao.class, RegionDO.class, "address");
  }
}
