package io.milkt.onelab.organization.entity;

import java.io.Serializable;
import java.util.List;
import net.pocrd.annotation.Description;

@Description("需求搜索结果")
public class RequirementSearchResult implements Serializable {

  private static final long serialVersionUID = 8774384550741229465L;

  @Description("需求列表")
  public List<RequirementEntity> list;

  @Description("页码")
  public PageEntity page;
}
