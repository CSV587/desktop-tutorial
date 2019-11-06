package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.CustomerInfo;
import com.hy.iom.mapper.IomMapper;

import java.util.List;

public interface CustomerInfoMapper extends IomMapper<CustomerInfo> {

    List<CustomerInfo> getCustomerInfoByUUID(CustomerInfo customerInfo);

}
