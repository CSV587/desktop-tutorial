package com.hy.iom.common.page;

import javax.swing.plaf.TableHeaderUI;
import java.util.List;

public class PageBody<T> {
    private List<TableHead> theader;
    private List<T> value;

    public List<TableHead> getTheader() {
        return theader;
    }

    public void setTheader(List<TableHead> theader) {
        this.theader = theader;
    }

    public List<T> getValue() {
        return value;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PageBody{" +
                "theader=" + theader +
                ", value=" + value +
                '}';
    }
}
