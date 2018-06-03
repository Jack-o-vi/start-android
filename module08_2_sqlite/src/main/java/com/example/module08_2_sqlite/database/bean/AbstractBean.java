package com.example.module08_2_sqlite.database.bean;

/**
 * Root of all entities which have identifier field.
 */

public abstract class AbstractBean {

    /** Entity`s id * */
    private long id;

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }
}
