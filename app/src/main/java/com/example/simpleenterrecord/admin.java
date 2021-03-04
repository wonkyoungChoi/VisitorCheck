package com.example.simpleenterrecord;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class admin extends AppCompatActivity {
    ArrayList<Item> list = new ArrayList<>();
    Item item;
    ImageButton back, setting;
    Button calendar;
    RecyclerView recyclerView;
    Boolean check;
    String path;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);
        String temperature = sharedPreferences.getString("check", "");
        setContentView(R.layout.admin);

        if(temperature.equals("발열체크 X")) {
            Log.d("tem", temperature);
            check = false;

        } else {
            check = true;
        }

        back =  (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setting = (ImageButton) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), setting.class);
                startActivity(intent);
            }
        });

        calendar = (Button) findViewById(R.id.calendar);
        final Calendar c = Calendar.getInstance(); //현재 시간을 얻음
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String monthChange = monthChange(mMonth);
        String dayChange = dayChange(mDay);


        calendar.setText(mYear + "/" + mMonth + "/" + mDay);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PATH", path);
                showDate();
            }
        });
        String path = "/storage/self/primary/Documents/Visitor/" + mYear + "-" + monthChange + "-" + dayChange + " 방문객.txt";
        recycler(path);

    }

    private String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    void showDate() {
        final Calendar c = Calendar.getInstance(); //현재 시간을 얻음
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                calendar.setText(year + "/" + month + "/" + dayOfMonth);
                String monthChange = monthChange(month);
                String dayChange = dayChange(dayOfMonth);
                String path = "/storage/self/primary/Documents/Visitor/" + year + "-" + monthChange + "-" + dayChange + " 방문객.txt";
                recycler(path);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    String monthChange(int month) {
        String changeMonth = "";
        if(month<10) {
            changeMonth = "0" + month;
        } else {
            changeMonth = ""+month;
        }
        return changeMonth;
    }

    String dayChange(int day) {
        String changeDay = "";
        if(day<10) {
            changeDay = "0" + day;
        } else {
            changeDay = "" + day;
        }
        return changeDay;
    }

    void recycler(String path) {
        list.clear();
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(fis));
            String line = bReader.readLine();
            String phoneNum;
            String time;
            String city;
            String temperature = "";
            int i = 1;
            while (line != null) {
                Log.d("CHECK", check.toString());
                item = new Item();
                city = substringBetween(line, "", " ,");
                Log.d("CITY", city);
                phoneNum = substringBetween(line, " ,", " .");
                Log.d("PHONENUM", phoneNum);
                time = substringBetween(line, " .", "..");
                Log.d("TIME", time);
                String num = Integer.toString(i);
                if(check) {
                    if(substringBetween(line, "..", "..") == null) {
                        temperature = "X";
                    } else {
                        temperature = substringBetween(line, "..", "..");
                        Log.d("TEMPERATURE", temperature);
                    }
                } else {
                    temperature = "X";
                }
                item.setTemperature(temperature);
                item.setNum(num);
                item.setCity(city);
                item.setPhoneNum(phoneNum);
                item.setTime(time);
                list.add(item);
                line = bReader.readLine();
                i++;
            }
            bReader.close();
            for (Item v : list)
                Log.i("Array is ", String.valueOf(v));
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
    }

}

