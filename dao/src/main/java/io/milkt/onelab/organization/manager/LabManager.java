package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.LabDao;
import io.milkt.onelab.organization.domain.LabDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("labManager")
public class LabManager extends BaseManager<LabDO> {

  @Resource
  private LabDao labDao;

  @Override
  public BaseDao<LabDO> getDao() {
    return labDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/lab-sql-mapper.xml",
        LabDao.class, LabDO.class, "lab");
  }
}
