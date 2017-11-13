package io.milkt.onelab.organization.service.http;

import com.sfebiz.common.dao.domain.BaseQuery;
import io.milkt.onelab.organization.api.RegionHttpExportService;
import io.milkt.onelab.organization.domain.RegionDO;
import io.milkt.onelab.organization.entity.RegionEntity;
import io.milkt.onelab.organization.enums.RegionLevelEnum;
import io.milkt.onelab.organization.exception.RegionErrorCode;
import io.milkt.onelab.organization.manager.RegionManager;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;

public class RegionHttpExportServiceImpl implements RegionHttpExportService {

  @Resource
  private RegionManager regionManager;

  @Override
  public List<RegionEntity> getRegionListByParentAdcode(String parentAdcode) {
    RegionDO factor = new RegionDO();
    factor.setParent(parentAdcode);

    BaseQuery<RegionDO> query = BaseQuery.getInstance(factor);
    List<RegionDO> regionDOList= regionManager.query(query);
    if (regionDOList == null || regionDOList.size() == 0) {
      throw new ServiceRuntimeException(RegionErrorCode.REGION_PARENT_NOT_EXIST, "parentAdcode对应的行政区域不存在");
    } else {
      ArrayList<RegionEntity> regions = new ArrayList<RegionEntity>();
      for (RegionDO regionDO : regionDOList) {
        RegionEntity entity = buildRegionEntity(regionDO);
        regions.add(entity);
      }

      return regions;
    }
  }

  private RegionEntity buildRegionEntity(RegionDO regionDO) {
    RegionEntity entity = new RegionEntity();
    entity.adcode = regionDO.getAdcode();
    entity.citycode = regionDO.getCitycode();
    entity.latitude = regionDO.getLatitude();
    entity.longitude = regionDO.getLongitude();
    entity.level = RegionLevelEnum.valueOf(regionDO.getLevel());
    entity.name = regionDO.getName();
    entity.parentAdcode = regionDO.getParent();
    entity.pinyin = regionDO.getPinyin();

    return entity;
  }

  @Override
  public RegionEntity getRegionByAdcode(String adcode) {
    RegionDO factor = new RegionDO();
    factor.setAdcode(adcode);

    BaseQuery<RegionDO> query = BaseQuery.getInstance(factor);
    List<RegionDO> regions= regionManager.query(query);

    if (regions.size() > 0 && regions.get(0) != null){
      return buildRegionEntity(regions.get(0));
    }else {
      throw new ServiceRuntimeException(RegionErrorCode.REGION_ADCODE_NOT_FOUND, "adcode不存在");
    }
  }
}
