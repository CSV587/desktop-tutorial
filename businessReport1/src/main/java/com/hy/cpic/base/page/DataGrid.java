package com.hy.cpic.base.page;

import java.util.List;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 17:43 2018/8/24
 * @ Description ：datagrid返回报文类
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class DataGrid {

    private DataGrid(){}

    public static DataGrid createDateGridRespose(List list){
        DataGrid dataGrid = new DataGrid();
        dataGrid.total = list.size();
        dataGrid.setRows(list);
        return dataGrid;
    }

    private Integer total;

    private List rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
