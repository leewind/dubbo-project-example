package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.RequirementDao;
import io.milkt.onelab.organization.domain.RequirementDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("requirementManager")
public class RequirementManager extends BaseManager<RequirementDO> {

  @Resource
  private RequirementDao requirementDao;

  @Override
  public BaseDao<RequirementDO> getDao() {
    return requirementDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/requirement-sql-mapper.xml",
        RequirementDao.class, RequirementDO.class, "requirement");
  }
}
