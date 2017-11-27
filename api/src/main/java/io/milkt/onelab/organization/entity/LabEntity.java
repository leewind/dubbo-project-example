package io.milkt.onelab.organization.entity;

import io.milkt.onelab.organization.enums.LabType;
import io.milkt.onelab.organization.enums.CommonVerifyStatus;
import java.io.Serializable;
import net.pocrd.annotation.Description;

/**
 * 插入地址信息
 *
 * @author leewind (leewind19841209@gmail.com)
 * @version v0.1 2017.11.23 17:00
 */
@Description("实验室信息")
public class LabEntity implements Serializable {

  private static final long serialVersionUID = -4876675784525611385L;

  @Description("实验室id")
  public long labId;

  @Description("所属组织id")
  public long organizationId;

  @Description("实验室名称")
  public String name;

  @Description("实验室类型")
  public LabType type;

  @Description("实验室地址信息")
  public AddressEntity address;

  @Description("联系电话")
  public String phone;

  @Description("评级")
  public int level;

  @Description("企业认证状态")
  public CommonVerifyStatus commonVerifyStatus;
}
