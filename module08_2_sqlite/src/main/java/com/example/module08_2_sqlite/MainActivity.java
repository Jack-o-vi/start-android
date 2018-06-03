package com.example.module08_2_sqlite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.module08_2_sqlite.database.BeanDAO.BeanDAO;
import com.example.module08_2_sqlite.database.DBHelper;
import com.example.module08_2_sqlite.database.bean.Bean;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etName, etEmail, etID;

    DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        btnUpd =  findViewById(R.id.btnUpd);
        btnUpd.setOnClickListener(this);

        btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etID = (EditText) findViewById(R.id.etID);


        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        // получаем данные из полей ввода
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String id = etID.getText().toString();
        BeanDAO beanDAO = new BeanDAO(this);

        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                Bean bean = new Bean(name, email);
                bean = beanDAO.save(bean);
                Log.d(LOG_TAG, "row inserted, ID = " + bean.getId());
                break;
            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in mytable: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                List<Bean> beans = beanDAO.readAllBeans();
                onRefresh(beans);

                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = beanDAO.clear();
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
            case R.id.btnUpd:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                Log.d(LOG_TAG, "--- Update mytable: ---");
                int updCount = beanDAO.updateBean(new Bean(name,email), id);
                Log.d(LOG_TAG, "updated rows count = " + updCount);
                break;
            case R.id.btnDel:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                Log.d(LOG_TAG, "--- Delete from mytable: ---");
                // удаляем по id
                int delCount = beanDAO.delBean(id);
                Log.d(LOG_TAG, "deleted rows count = " + delCount);
                break;
        }
    }

    public void onRefresh(List<Bean> beans) {
        LinearLayout linLayout = findViewById(R.id.linLayout);

        LayoutInflater ltInflater = getLayoutInflater();
        linLayout.removeAllViews();
        int[] colors = {Color.parseColor("#559966CC"), Color.parseColor("#55336699")};
        if(beans == null) return;
        for (Bean b : beans) {
            View item = ltInflater.inflate(R.layout.item, linLayout, false);
            TextView tvName = item.findViewById(R.id.tvName);
            tvName.setText("Id" + b.getId());
            TextView tvPosition = item.findViewById(R.id.tvPosition);
            tvPosition.setText("Name: " + b.getName());
            TextView tvSalary = item.findViewById(R.id.tvSalary);
            tvSalary.setText("Email: " + b.getEmail());
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[(int) b.getId() % 2]);
            linLayout.addView(item);
        }
    }

}
