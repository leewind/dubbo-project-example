package io.milkt.onelab.organization.dao;

import com.sfebiz.common.dao.BaseDao;
import io.milkt.onelab.organization.domain.MessageDO;
import java.util.List;

/**
 * Created by leewind on 2017/12/4.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.4
 */
public interface MessageDao extends BaseDao<MessageDO> {

  List<MessageDO> getUserIdLinkUserDB(String adcode);

}
