package io.milkt.onelab.organization.api;

import io.milkt.onelab.organization.entity.RegionEntity;
import io.milkt.onelab.organization.exception.RegionErrorCode;
import java.util.List;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;

/**
 * 行政区域http接口接口信息
 *
 * @author leewind(leewind19841209@gamil.com)
 * @version v0.1 2017.8.29 08:23
 */
@ApiGroup(name = "region", minCode = 7000000, maxCode = 8000000, codeDefine = RegionErrorCode.class, owner = "leewind")
public interface RegionHttpExportService {

  @HttpApi(name = "region.getRegionListByParentAdcode", desc = "通过adcode获取下级行政区域列表", security = SecurityType.None, owner = "leewind")
  @DesignedErrorCode(RegionErrorCode._C_REGION_PARENT_NOT_EXIST)
  List<RegionEntity> getRegionListByParentAdcode(
      @ApiParameter(required = true, name = "parentAdcode", desc = "父行政区域的adcode - 例：310100") String parentAdcode
  );

  @HttpApi(name = "region.getRegionByAdcode", desc = "通过adcode获取行政区域信息", security = SecurityType.None, owner = "leewind")
  @DesignedErrorCode(RegionErrorCode._C_REGION_ADCODE_NOT_FOUND)
  RegionEntity getRegionByAdcode(
      @ApiParameter(required = true, name = "adcode", desc = "政区域的adcode - 例：310100") String adcode
  );
}
