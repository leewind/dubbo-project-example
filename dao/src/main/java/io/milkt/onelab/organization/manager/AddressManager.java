package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.AddressDao;
import io.milkt.onelab.organization.domain.AddressDO;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("addressManager")
public class AddressManager extends BaseManager<AddressDO>{
  
  @Resource
  private AddressDao addressDao;

  @Override
  public BaseDao<AddressDO> getDao() {
    return addressDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/address-sql-mapper.xml",
        AddressDao.class, AddressDO.class, "address");
  }
}
