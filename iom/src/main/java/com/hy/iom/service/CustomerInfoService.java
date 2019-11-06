package com.hy.iom.service;

import com.hy.iom.entities.CustomerInfo;
import com.hy.iom.mapper.oracle.CustomerInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInfoService {
    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    public List<CustomerInfo> selectByUuid(CustomerInfo record) {
        return customerInfoMapper.getCustomerInfoByUUID(record);
    }
}
