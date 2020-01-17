package com.freedom.jlbatross.entity;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 17:43
 * @Description:
 */
public class SystemConfig {
    private Integer id;

    private String mavenHome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return mavenHome;
    }

    public void setName(String mavenHome) {
        this.mavenHome = mavenHome;
    }
}
