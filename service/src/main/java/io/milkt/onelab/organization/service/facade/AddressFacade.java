package io.milkt.onelab.organization.service.facade;

import io.milkt.onelab.organization.domain.AddressDO;
import io.milkt.onelab.organization.entity.AddressInsertEntity;
import io.milkt.onelab.organization.manager.AddressManager;
import javax.annotation.Resource;
import org.springframework.dao.DataIntegrityViolationException;

public class AddressFacade {

  @Resource
  private AddressManager addressManager;

  public long save(AddressInsertEntity addressInsertEntity, long addressId) throws DataIntegrityViolationException {

    AddressDO addressDO = new AddressDO();

    if (addressId > 0) {
      addressDO = addressManager.getById(addressId);
    }

    addressDO.setProvinceAdcode(addressInsertEntity.provinceAdcode);
    addressDO.setProvinceName(addressInsertEntity.provinceName);
    addressDO.setCityAdcode(addressInsertEntity.cityAdcode);
    addressDO.setCityName(addressInsertEntity.cityName);
    addressDO.setDistrictAdcode(addressInsertEntity.districtAdcode);
    addressDO.setDistrictName(addressInsertEntity.districtName);
    addressDO.setDetailAddress(addressInsertEntity.detailAddress);

    // @todo 需要引入amap服务对地址求坐标和geohash

    addressManager.insert(addressDO);
    return addressDO.getId();
  }
}
