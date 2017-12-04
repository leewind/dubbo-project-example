package io.milkt.onelab.organization.manager;

import com.sfebiz.common.dao.BaseDao;
import com.sfebiz.common.dao.helper.DaoHelper;
import com.sfebiz.common.dao.manager.BaseManager;
import io.milkt.onelab.organization.dao.MessageDao;
import io.milkt.onelab.organization.domain.MessageDO;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
@Component("messageManager")
public class MessageManager extends BaseManager<MessageDO> {

  @Resource
  private MessageDao messageDao;

  @Override
  public BaseDao<MessageDO> getDao() {
    return messageDao;
  }

  public static void main(String[] args) {
    DaoHelper.genXMLWithFeature("/Users/leewind/Projects/milkt/milkt-1lab-service-organization/dao/src/main/resources/sqlmap/message-sql-mapper.xml",
        MessageDao.class, MessageDO.class, "message");
  }

  public List<MessageDO> getUserIdLinkUserDB(String cityAdcode){
    return messageDao.getUserIdLinkUserDB(cityAdcode);
  }
}
