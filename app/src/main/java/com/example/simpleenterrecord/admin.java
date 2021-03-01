package com.example.simpleenterrecord;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
    ImageButton back;
    Button calendar;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        back =  (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar = (Button) findViewById(R.id.calendar);
        final Calendar c = Calendar.getInstance(); //현재 시간을 얻음
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        calendar.setText(mYear + "/" + mMonth + "/" + mDay);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });
        String month = monthChange(mMonth);
        String day= dayChange(mDay);
        String path = "/storage/self/primary/Documents/Visitor/"+mYear+"-"+ month  + "-"+ day + " 방문객.txt";
        Log.d("MONTH", String.valueOf(month));
        Log.d("PATH", path);

        recycler(path);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear+1;
                calendar.setText(year + "/" + month + "/" + dayOfMonth);
                String monthChange = monthChange(month);
                String dayChange = dayChange(dayOfMonth);
                String path = "/storage/self/primary/Documents/Visitor/" + year+"-" + monthChange+"-" + dayChange + " 방문객.txt";
                recycler(path);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    String monthChange(int month) {
        String changeMonth = "";
        if(month<10) {
            changeMonth = "0" + month;
        }
        return changeMonth;
    }

    String dayChange(int day) {
        String changeDay = "";
        if(day<10) {
            changeDay = "0" + day;
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
            int i = 1;
            while (line != null) {
                item = new Item();
                city = substringBetween(line, "", " ,");
                Log.d("CITY", city);
                phoneNum = substringBetween(line, " ,", " .");
                Log.d("PHONENUM", phoneNum);
                time = substringBetween(line, " .", ".");
                Log.d("TIME", time);
                String num = Integer.toString(i);
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