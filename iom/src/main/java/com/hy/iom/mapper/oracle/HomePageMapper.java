package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.RecordInfo;
import com.hy.iom.homepage.page.BarItem;
import com.hy.iom.homepage.page.HomePage;
import com.hy.iom.mapper.IomMapper;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.mapper.oracle
 */
public interface HomePageMapper extends IomMapper<RecordInfo> {
    HomePage querySummaryData(HomePage homePage);

    List<BarItem> queryAveDuration(HomePage homePage);

    HomePage queryConnect(HomePage homePage);
}
