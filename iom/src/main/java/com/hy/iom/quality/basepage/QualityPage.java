package com.hy.iom.quality.basepage;

import com.hy.iom.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-21
 * user: lxg
 * package_name: com.hy.iom.quality.basepage
 */
@Setter
@Getter
public class QualityPage extends BasePage {
    /**
     * .
     * id
     */
    private String id;
    /**
     * .
     * 维度名称
     */
    private String dimensionName;

    /**
     * .
     * 高级预警
     */
    private double advancedWarning;

    /**
     * .
     * 中级预警
     */
    private double middleWarning;

    /**
     * .
     * 低级预警
     */
    private double lowWarning;

    /**
     * .
     * 项目名称
     */
    private String projectName;

    /**
     * .
     * 创建时间
     */
    private Date fst;

    /**
     * .
     * 修改时间
     */
    private Date lmt;

    /**
     * .
     * 创建人
     */
    private String fsu;

    /**
     * .
     * 修改人
     */
    private String lmu;
}
