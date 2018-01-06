package io.milkt.onelab.organization.service.task;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.domain.MotionDO;
import io.milkt.onelab.organization.domain.RequirementDO;
import io.milkt.onelab.organization.enums.MotionStatus;
import io.milkt.onelab.organization.enums.RequirementStatus;
import io.milkt.onelab.organization.manager.MotionManager;
import io.milkt.onelab.organization.manager.RequirementManager;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Resource;

/**
 * Created by leewind on 2017/12/9.
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.12.9
 */
public class AutoProcessOrderTask {

  private static final Logger logger = LoggerFactory.getLogger(AutoProcessOrderTask.class);

  @Resource
  private RequirementManager requirementManager;

  @Resource
  private MotionManager motionManager;

  public void expire() {
    logger.info("开始每小时的订单过期处理");

    RequirementDO factor = new RequirementDO();
    factor.setStatus(RequirementStatus.RECRUITING.name());

    BaseQuery<RequirementDO> conditions = BaseQuery.getInstance(factor);
    conditions.addLt("recruit_finish_time", new Timestamp(System.currentTimeMillis()));

    List<RequirementDO> requirementDOS = requirementManager.query(conditions);
    for (RequirementDO one : requirementDOS) {
      one.setStatus(RequirementStatus.CLOSE.name());

      MotionDO subfactor = new MotionDO();
      subfactor.setRequirementId(one.getId());

      BaseQuery<MotionDO> subcondition = BaseQuery.getInstance(subfactor);
      List<MotionDO> motions = motionManager.query(subcondition);

      for (MotionDO motion : motions) {
        motion.setStatus(MotionStatus.CLOSE.name());
        motionManager.update(motion);
      }

      requirementManager.update(one);
    }

    logger.info("结束每小时的订单过期处理");
  }

}
