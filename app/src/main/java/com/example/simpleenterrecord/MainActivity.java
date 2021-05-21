package com.example.simpleenterrecord;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button city = (Button) findViewById(R.id.city);
        final Button district = (Button) findViewById(R.id.district);
        final Button temperature = (Button) findViewById(R.id.temperature);
        final Button start = (Button) findViewById(R.id.start);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText message = (EditText) findViewById(R.id.message);

        sharedPreferences = getSharedPreferences("test", MODE_PRIVATE); // test 이름의 기본모드 설정
        if(!sharedPreferences.getString("password", "").equals("")) {
            finish();
            Intent intent = new Intent(getApplicationContext(), RecordPage.class);
            startActivity(intent);
        } else {
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor = sharedPreferences.edit(); //sharedPreferences를 제어할 editor를 선언
                    password.getText().toString();
                    name.getText().toString();
                    if(!district.getText().toString().equals("시•군•구 선택") && !city.getText().toString().equals("시•도 선택") && !temperature.getText().toString().equals("발열체크 여부")) {
                        editor.putString("password", password.getText().toString());
                        editor.putString("name", name.getText().toString());
                        editor.putString("message", message.getText().toString());
                        editor.putString("district", district.getText().toString());
                        editor.putString("city", city.getText().toString()); // key,value 형식으로 저장
                        editor.putString("check", temperature.getText().toString());
                        editor.apply();    //최종 커밋. 커밋을 해야 저장이 된다.
                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), RecordPage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "모든값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("발열체크 여부 선택");
                final String[] check = new String[]{"발열체크 O", "발열체크 X"};

                dlg.setItems(check, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temperature.setText(check[which]);
                        Toast.makeText(getApplicationContext(), check[which] + " 선택", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });

        city.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("시•도 선택"); //제목
                final String[] cityList = new String[]{"강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시", "대전광역시", "부산광역시", "서울특별시", "세종특별자치시", "울산광역시"};

                dlg.setItems(cityList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        city.setText(cityList[which]);
                        Toast.makeText(getApplicationContext(), cityList[which] + " 선택", Toast.LENGTH_SHORT).show();
                        district.setText("시•군•구 선택");
                    }
                });
                dlg.show();
            }
        });


        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String choose = city.getText().toString();
                Log.d("choose", city.getText().toString());


                final String[] gangwondoList = new String[]{"강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군", "원주시",
                        "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군", "홍천군", "화천군", "횡성군"};

                final String[] gyounggidoList = new String[]{"가평군", "고양시", "고양시 덕양구", "고양시 일산동구",
                        "고양시 일산서구", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시",
                        "부천시", "성남시", "성남시 분당구", "성남시 수정구", "성남시 중원구", "수원시", "수원시 권선구", "수원시 영통구",
                        "수원시 장안구", "수원시 팔달구", "시흥시", "안산시", "안산시 단원구", "안산시 상록구", "안성시", "안양시", "안양시 동안구",
                        "안안시 만안구", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시", "용인시 기흥구", "용인시 수지구", "용인시 처인구",
                        "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시"};

                final String[] gyoungnamList = new String[]{"거제시", "거창군", "고성군", "김해시", "남해군", "밀양시", "사천시", "산청군", 
                        "양산시", "의령군", "진주시", "창녕군", "창원시", "창원시 마산합포구", "창원시 마산회원구", "창원시 성산구", "창원시 의창구",
                        "창원시 진해구", "통영시", "하동군", "함안군", "함양군", "합천군"};

                final String[] gyoungbukList = new String[]{"경산시", "경주시", "고령군", "구미시", "군위군", "김천시", "문경시", "봉화군", "상주시",
                        "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시", "예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군",
                        "포항시", "포항시 남구", "포항시 북구"};

                final String[] gwangjuList = new String[]{"광산구", "남구", "동구", "북구", "서구"};

                final String[] daeguList = new String[]{"남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"};

                final String[] daejeonList = new String[]{"대덕구", "동구", "서구", "유성구", "중구"};

                final String[] busanList = new String[]{"강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구", "북구",
                        "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구"};

                final String[] seoulList = new String[]{"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구",
                        "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};

                final String[] sejongList = new String[]{"세종특별자치시"};

                final String[] ulsanList = new String[]{"남구", "동구", "북구", "울주군", "중구"};

                final String[] incheonList = new String[]{"강화군", "계양구", "남동구", "동구", "미추홀구", "부평구", "서구", "연수구", "옹진군", "중구"};

                final String[] jeonnamList = new String[]{"강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군",
                        "순천시", "신안군", "여수시", "영광군", "영암군", "완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군"};

                final String[] jeonbukList = new String[]{"고창군", "군산시", "김제시", "남원시", "무주군", "부안군", "순창군", "완주군", "익산시", "임실군",
                        "장수군", "전주시", "전주시 덕진구", "전주시 완산구", "정읍시", "진안군"};

                final String[] jejuList = new String[]{"서귀포시", "제주시"};

                final String[] chungnamList = new String[]{"계룡시", "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시", "서천군", "아산시",
                        "예산군", "천안시", "천안시 동남구", "천안시 서북구", "청양군", "태안군", "홍성군"};

                final String[] chungbukList = new String[]{"괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "제천시", "증평군", "진천군", "청주시",
                        "청주시 상당구", "청주시 서원구", "청주시 청원구", "청주시 흥덕구", "충주시"};

                switch (choose) {
                    case "강원도":
                        showDialog(gangwondoList);
                        break;
                    case "경기도":
                        showDialog(gyounggidoList);
                        break;
                    case "경상남도":
                        showDialog(gyoungnamList);
                        break;
                    case "경상북도":
                        showDialog(gyoungbukList);
                        break;
                    case "광주광역시":
                        showDialog(gwangjuList);
                        break;
                    case "대구광역시":
                        showDialog(daeguList);
                        break;
                    case "대전광역시":
                        showDialog(daejeonList);
                        break;
                    case "부산광역시":
                        showDialog(busanList);
                        break;
                    case "서울특별시":
                        showDialog(seoulList);
                        break;
                    case "세종특별자치시":
                        showDialog(sejongList);
                        break;
                    case "울산광역시":
                        showDialog(ulsanList);
                        break;
                    case "인천광역시":
                        showDialog(incheonList);
                        break;
                    case "전라남도":
                        showDialog(jeonnamList);
                        break;
                    case "전라북도":
                        showDialog(jeonbukList);
                        break;
                    case "제주특별자치도":
                        showDialog(jejuList);
                        break;
                    case "충청남도":
                        showDialog(chungnamList);
                        break;
                    case "충청북도":
                        showDialog(chungbukList);
                        break;

                }

            }
        });

    }

    void showDialog(String[] districtChoose) {
        AlertDialog.Builder dlg2 = new AlertDialog.Builder(MainActivity.this);
        dlg2.setTitle("시•군•구 선택");
        Button district = (Button) findViewById(R.id.district);
        dlg2.setItems(districtChoose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                district.setText(districtChoose[which]);
                Toast.makeText(getApplicationContext(), districtChoose[which] + " 선택", Toast.LENGTH_SHORT).show();
            }
        });
        dlg2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlg2.show();
    }
}

