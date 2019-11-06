package com.hy.iom.common.page;

public class TableBody {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TableBody{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
