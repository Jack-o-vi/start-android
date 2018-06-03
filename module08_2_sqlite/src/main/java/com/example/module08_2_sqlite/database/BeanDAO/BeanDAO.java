package com.example.module08_2_sqlite.database.BeanDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.module08_2_sqlite.database.DBManager;
import com.example.module08_2_sqlite.database.bean.Bean;

import java.util.ArrayList;
import java.util.List;

public class BeanDAO {

    private DBManager dbManager;
    private Context context;

    public BeanDAO(Context context) {
        this.context = context;
    }

    public Bean save(Bean bean) {
        dbManager = DBManager.getInstance();
        SQLiteDatabase db = dbManager.getSQLDatabase(context);

        if (bean != null) {
            // создаем объект для данных
            ContentValues cv = new ContentValues();

            cv.put("name", bean.getName());
            cv.put("email", bean.getEmail());

            // вставляем запись и получаем ее ID
            long rowID = db.insert("mytable", null, cv);
            bean.setId(rowID);
        }

        dbManager.closeDatabase();
        return bean;
    }

    public List<Bean> readAllBeans() {
        dbManager = DBManager.getInstance();
        SQLiteDatabase db = dbManager.getSQLDatabase(context);

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable",
                null,
                null,
                null,
                null,
                null,
                null);
        List<Bean> beans = new ArrayList<>(50);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int emailColIndex = c.getColumnIndex("email");

            do {
                beans.add(new Bean(c.getString(nameColIndex), c.getString(emailColIndex), c.getInt(idColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        dbManager.closeDatabase();
        return beans;
    }

    public int updateBean(Bean bean, String id) {
        dbManager = DBManager.getInstance();
        SQLiteDatabase db = dbManager.getSQLDatabase(context);
        // создаем объект для данных
        ContentValues cv = new ContentValues();
// подготовим значения для обновления
        cv.put("name", bean.getName());
        cv.put("email", bean.getEmail());

        // обновляем по id
        int updCount = db.update("mytable", cv, "id = ?", new String[]{id});
        return updCount;
    }

    public int delBean(String id){
        dbManager = DBManager.getInstance();
        SQLiteDatabase db = dbManager.getSQLDatabase(context);
        int delCount = db.delete("mytable", "id = " + id, null);
        return  delCount;
    }

    public int clear() {
        dbManager = DBManager.getInstance();
        SQLiteDatabase db = dbManager.getSQLDatabase(context);
        int clearCount = db.delete("mytable", null, null);
        dbManager.closeDatabase();
        return clearCount;
    }
}
