package io.milkt.onelab.organization.service.facade;

import io.milkt.onelab.organization.domain.AddressDO;
import io.milkt.onelab.organization.entity.AddressEntity;
import io.milkt.onelab.organization.entity.AddressInsertEntity;
import io.milkt.onelab.organization.manager.AddressManager;
import javax.annotation.Resource;
import net.pocrd.entity.ServiceRuntimeException;
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

    addressManager.insertOrUpdate(addressDO);
    return addressDO.getId();
  }

  public AddressEntity buildAddressEntity(AddressDO addressDO) {
    AddressEntity addressEntity = new AddressEntity();

    addressEntity.provinceAdcode = addressDO.getProvinceAdcode();
    addressEntity.provinceName = addressDO.getProvinceName();
    addressEntity.cityAdcode = addressDO.getCityAdcode();
    addressEntity.cityName = addressDO.getCityName();
    addressEntity.districtAdcode = addressDO.getDistrictAdcode();
    addressEntity.districtName = addressDO.getDistrictName();
    addressEntity.detailAddress = addressDO.getDetailAddress();

    return addressEntity;
  }
}
