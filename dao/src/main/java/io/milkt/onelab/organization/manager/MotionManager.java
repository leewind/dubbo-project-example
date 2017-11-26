package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.MotionDao;
import io.milkt.onelab.organization.domain.MotionDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("motionManager")
public class MotionManager extends BaseManager<MotionDO> {

  @Resource
  private MotionDao motionDao;

  @Override
  public BaseDao<MotionDO> getDao() {
    return motionDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/motion-sql-mapper.xml",
        MotionDao.class, MotionDO.class, "motion");
  }
}
