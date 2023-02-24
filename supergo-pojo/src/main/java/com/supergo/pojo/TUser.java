package com.supergo.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

//映射表名
@Table(name = "user")
public class TUser {
    //指定主键属性
    @Id
    //指定对应的列名
    @Column(name = "id")
    private Integer id;
    //指定对应的列名
    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "TUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
