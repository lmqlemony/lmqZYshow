package com.fullall.lmq.lmqzyshow;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    private DbHelper helper;
    private TextView lmq;
    private Button sech, save;
    private EditText edit;
    private ListView listView;
    List<Map<String, String>> glite;
    private SimpleAdapter adapter;
    private String htmlContent = "源代码";
    final ContentValues cv = new ContentValues();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private AdapterView.OnItemClickListener viewclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String cmc = glite.get(position).get("mc");
            cv.put("mc", cmc);
            cv.put("rq", formatter.format(new Date(System.currentTimeMillis())));
            db.update("lmqzyshow", cv, "mc=?", new String[]{cmc});
            Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            listshow();//重查数据显示
        }
    };
    private View.OnClickListener btnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save:
                    cv.put("mc", edit.getText().toString());
                    cv.put("rq", formatter.format(new Date(System.currentTimeMillis())));
                    long s = db.insert("lmqzyshow", "", cv);
                    if (-1 != s) {
                        Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                    listshow();//重查数据显示
                    break;
                case R.id.sech:
                    for (int i = 0; i < glite.size(); i++) {
                        final String path, umc, urq;
                        umc = glite.get(i).get("mc");
                        path = "http://so.iqiyi.com/so/q_" + umc;
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    htmlContent = HtmlService.getHtml(path);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String str1;
                                String reg1 = "firstlink";//"\\d\\d\\d\\d-\\d\\d-\\d\\d";
                                Pattern pattern = Pattern.compile(reg1);
                                Matcher matcher = pattern.matcher(htmlContent);
                                Log.i("lmq",htmlContent);
                                if (matcher.find()) {
                                    str1 = matcher.group(0);
                                    cv.put("mc", umc);//htmlContent.substring(1, 10));
                                    cv.put("rq", str1);
                                    long s = db.update("lmqzyshow", cv, "mc=?", new String[]{umc});

                                }
                            }
                        }.start();
                    }
                    ;
                    listshow();//重查数据显示
                    break;
                case R.id.lmq:
                    listshow();//重查数据显示
                    break;
            }
        }
    };
    private AdapterView.OnItemLongClickListener longclick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String pid = glite.get(position).get("id");
            db.delete("lmqzyshow", "id=?", new String[]{pid});
            glite.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "删除了ID:" + pid, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // style选无标题主题

        lmq = (TextView) findViewById(R.id.lmq);
        sech = (Button) findViewById(R.id.sech);
        save = (Button) findViewById(R.id.save);
        edit = (EditText) findViewById(R.id.edit);
        listView = (ListView) findViewById(R.id.view);

        //从辅助类获得数据库对象
        helper = new DbHelper(this, "lmqzyshow.sqlite", null, 1);
        db = helper.getWritableDatabase();

        lmq.setOnClickListener(btnclick);
        sech.setOnClickListener(btnclick);
        save.setOnClickListener(btnclick);
        listView.setOnItemClickListener(viewclick);
        listView.setOnItemLongClickListener(longclick);
        listshow();
    }

    private void listshow() {
        Cursor cursor = db.rawQuery("select * from lmqzyshow order by rq,mc ", null);
        glite = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>(); //写入循环每个新建
            map.put("id", cursor.getString(cursor.getColumnIndex("id")));
            map.put("mc", cursor.getString(cursor.getColumnIndex("mc")));
            map.put("rq", cursor.getString(cursor.getColumnIndex("rq")));
            glite.add(map);
        }
        adapter = new SimpleAdapter(MainActivity.this, glite, R.layout.listsytle,
                new String[]{"id", "mc", "rq"}, new int[]{R.id.id, R.id.mc, R.id.rq});
        listView.setAdapter(adapter);
    }

}