package com.example.module08_2_sqlite.database.bean;

public class Bean extends AbstractBean {

    private String name;
    private String email;

    public Bean(String name, String email, long id) {
        this.name = name;
        this.email = email;
        setId(id);
    }

    public Bean(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
