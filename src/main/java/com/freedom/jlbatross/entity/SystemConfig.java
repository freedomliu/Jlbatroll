package com.freedom.jlbatross.entity;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 17:43
 * @Description:
 */
public class SystemConfig {
    private int id;

    private String mavenHome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mavenHome;
    }

    public void setName(String mavenHome) {
        this.mavenHome = mavenHome;
    }
}
