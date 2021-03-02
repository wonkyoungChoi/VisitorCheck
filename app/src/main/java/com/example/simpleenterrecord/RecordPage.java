package com.example.simpleenterrecord;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class RecordPage extends AppCompatActivity {
    EditText middle_number, end_number;
    TextView name, message;
    Button city, district, finish, check;
    ImageButton setting;
    CheckBox agree_check;
    Boolean temCheck;
    String path;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int MULTIPLE_PERMISSION = 10235;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences= getSharedPreferences("test", MODE_PRIVATE);    // test 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
        String password = sharedPreferences.getString("password", "");
        String nameSet = sharedPreferences.getString("name","");
        String messageSet = sharedPreferences.getString("message","");
        String citySet = sharedPreferences.getString("city", "");
        String districtSet = sharedPreferences.getString("district", "");
        String temperature = sharedPreferences.getString("check", "");

        Toast.makeText(this, "오늘도 즐거운 하루 되세요~!.", Toast.LENGTH_SHORT).show();
        if(temperature.equals("발열체크 X")) {
            temCheck = false;
            setContentView(R.layout.record_page);
        } else {
            temCheck = true;
            setContentView(R.layout.record_page_tem);
        }

        if (!hasPermissions(this, PERMISSIONS_STORAGE)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, MULTIPLE_PERMISSION);
        }


        city = (Button) findViewById(R.id.city);
        district = (Button) findViewById(R.id.district);
        check = (Button) findViewById(R.id.check);
        middle_number = (EditText) findViewById(R.id.middle_number);
        end_number = (EditText) findViewById(R.id.end_number);
        name = (TextView) findViewById(R.id.name);
        message = (TextView) findViewById(R.id.message);
        agree_check = (CheckBox) findViewById(R.id.agree_check);
        finish = (Button) findViewById(R.id.finish);
        setting = (ImageButton) findViewById(R.id.setting);

        city.setText(citySet);
        district.setText(districtSet);
        name.setText(nameSet);    // TextView에 SharedPreferences에 저장되어있던 값 찍기.
        message.setText(messageSet);

        middle_number.setShowSoftInputOnFocus(false);
        end_number.setShowSoftInputOnFocus(false);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(RecordPage.this);
                dlg.setTitle("관리자 모드로 들어가기 위한 비밀번호 입력");
                final EditText et = new EditText(RecordPage.this);
                dlg.setView(et);
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(et.getText().toString().equals(password)) {
                            Intent in = new Intent(getApplicationContext(), admin.class);
                            startActivity(in);
                        } else {
                            Toast.makeText(getApplicationContext(), "비밀번호를 정확히 입력하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlg.show();
            }
        });
        

        long delete = System.currentTimeMillis() - 28L*24*60*60*1000;
        Date deleteDate = new Date(delete);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String delete1 = sdf2.format(deleteDate);
        File deletefile = new File("/storage/self/primary/Documents/Visitor/" + delete1 + " 방문객.txt");
        File deletefile_tem = new File("/storage/self/primary/Documents/Visitor/" + delete1 + " 방문객(온도).txt");
        deletefile.delete();
        deletefile_tem.delete();

        finish.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                File nFile = new File("/storage/self/primary/Documents/", "Visitor");
                if (!nFile.exists()) {
                    nFile.mkdir();
                }
                if(!agree_check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "개인정보 수집/이용/제공에 동의해주세요.", Toast.LENGTH_SHORT).show();
                } else if (middle_number.length() < 3 || end_number.length() < 4)  {
                        Toast.makeText(getApplicationContext(), "전화번호를 정확히 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if(middle_number != null && end_number != null && agree_check.isChecked()) {
                    String area = city.getText().toString() + " " + district.getText().toString() + " ,";
                    String number = "010 " + middle_number.getText().toString() + " " + end_number.getText().toString() + " .";
                    long now = System.currentTimeMillis(); // 현재시간 받아오기
                    Date date = new Date(now); // Date 객체 생성
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = sdf.format(date) + "..";
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    String nowDate = sdf1.format(date);
                    if(!temCheck) {
                        path = "/storage/self/primary/Documents/Visitor/" + nowDate + " 방문객.txt";
                    } else {
                        path = "/storage/self/primary/Documents/Visitor/" + nowDate + " 방문객(온도).txt";
                    }
                    Log.d("GET", Environment.DIRECTORY_DOCUMENTS);
                    // 파일 생성
                    File savefile = new File(path);
                    if(!temCheck) {

                        try {
                            FileOutputStream fos = new FileOutputStream(savefile, true);
                            fos.write(area.getBytes());
                            fos.write(number.getBytes());
                            fos.write((nowTime + "\n").getBytes());
                            fos.close();
                            city.setText(citySet);
                            district.setText(districtSet);
                            middle_number.setText("");
                            end_number.setText("");
                            agree_check.setChecked(false);
                            Toast.makeText(getApplicationContext(), "작성 완료했습니다.", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.d("ERROR", String.valueOf(e));
                        }
                    } else {
                        try {
                            String temperature = check.getText().toString() + "..\n";
                            FileOutputStream fos = new FileOutputStream(savefile, true);
                            fos.write(area.getBytes());
                            fos.write(number.getBytes());
                            fos.write(nowTime.getBytes());
                            fos.write(temperature.getBytes());
                            fos.close();
                            city.setText(citySet);
                            district.setText(districtSet);
                            middle_number.setText("");
                            end_number.setText("");
                            check.setText("37.1 이하");
                            agree_check.setChecked(false);
                            Toast.makeText(getApplicationContext(), "작성 완료했습니다.", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.d("ERROR", String.valueOf(e));
                        }
                    }
                    }
                }
        });

        middle_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                middle_number.setText(null);
            }
        });

        end_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_number.setText(null);
            }
        });

        middle_number.setInputType(EditorInfo.TYPE_NULL);
        end_number.setInputType(EditorInfo.TYPE_NULL);// setCursorVisible(false);

        if(temCheck) {
            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(RecordPage.this);
                    dlg.setTitle("체온 선택"); //제목
                    final String[] temList = new String[]{"37.1 이하", "37.1", "37.2", "37.3", "37.4", "37.5", "37.6", "37.7", "37.8", "37.9", "38.0",
                            "38.1", "38.2", "38.3", "38.4", "38.5", "38.6", "38.7", "38.8", "38.9", "39.0",
                            "39.1", "39.2", "39.3", "39.4", "39.5", "39.6", "39.7", "39.8", "39.9", "40.0 이상",};

                    dlg.setItems(temList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            check.setText(temList[which]);
                            Toast.makeText(getApplicationContext(), temList[which] + " 선택", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg.show();
                }
            });
        }

        city.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(RecordPage.this);
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

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        if(middle_number.length()<4) {
            if(view.getId() == R.id.num1) {
                middle_number.setText(middle_number.getText().toString() + 1);
            } else if(view.getId() == R.id.num2) {
                middle_number.setText(middle_number.getText().toString() + 2);
            } else if(view.getId() == R.id.num3) {
                middle_number.setText(middle_number.getText().toString() + 3);
            } else if(view.getId() == R.id.num4) {
                middle_number.setText(middle_number.getText().toString() + 4);
            }else if(view.getId() == R.id.num5) {
                middle_number.setText(middle_number.getText().toString() + 5);
            }else if(view.getId() == R.id.num6) {
                middle_number.setText(middle_number.getText().toString() + 6);
            }else if(view.getId() == R.id.num7) {
                middle_number.setText(middle_number.getText().toString() + 7);
            }else if(view.getId() == R.id.num8) {
                middle_number.setText(middle_number.getText().toString() + 8);
            }else if(view.getId() == R.id.num9) {
                middle_number.setText(middle_number.getText().toString() + 9);
            }else if(view.getId() == R.id.num0) {
                middle_number.setText(middle_number.getText().toString() + 0);
            }else if(view.getId() == R.id.back) {
                String word = middle_number.getText().toString();
                int input = word.length();
                if (input > 0){
                    middle_number.setText(word.substring(0, input-1));
                }
            }else if(view.getId() == R.id.clear) {
                middle_number.setText("");
            }
        } else if(middle_number.length()==4 && end_number.length() == 0) {
            if(view.getId() == R.id.num1) {
                end_number.setText(end_number.getText().toString() + 1);
            } else if(view.getId() == R.id.num2) {
                end_number.setText(end_number.getText().toString() + 2);
            } else if(view.getId() == R.id.num3) {
                end_number.setText(end_number.getText().toString() + 3);
            } else if(view.getId() == R.id.num4) {
                end_number.setText(end_number.getText().toString() + 4);
            }else if(view.getId() == R.id.num5) {
                end_number.setText(end_number.getText().toString() + 5);
            }else if(view.getId() == R.id.num6) {
                end_number.setText(end_number.getText().toString() + 6);
            }else if(view.getId() == R.id.num7) {
                end_number.setText(end_number.getText().toString() + 7);
            }else if(view.getId() == R.id.num8) {
                end_number.setText(end_number.getText().toString() + 8);
            }else if(view.getId() == R.id.num9) {
                end_number.setText(end_number.getText().toString() + 9);
            }else if(view.getId() == R.id.num0) {
                end_number.setText(end_number.getText().toString() + 0);
            }
            if(view.getId() == R.id.back) {
                String word = middle_number.getText().toString();
                int input = word.length();
                if (input > 0){
                    middle_number.setText(word.substring(0, input-1));
                }
            }else if(view.getId() == R.id.clear) {
                middle_number.setText("");
            }
        } else {
            if(view.getId() == R.id.num1) {
                end_number.setText(end_number.getText().toString() + 1);
            } else if(view.getId() == R.id.num2) {
                end_number.setText(end_number.getText().toString() + 2);
            } else if(view.getId() == R.id.num3) {
                end_number.setText(end_number.getText().toString() + 3);
            } else if(view.getId() == R.id.num4) {
                end_number.setText(end_number.getText().toString() + 4);
            }else if(view.getId() == R.id.num5) {
                end_number.setText(end_number.getText().toString() + 5);
            }else if(view.getId() == R.id.num6) {
                end_number.setText(end_number.getText().toString() + 6);
            }else if(view.getId() == R.id.num7) {
                end_number.setText(end_number.getText().toString() + 7);
            }else if(view.getId() == R.id.num8) {
                end_number.setText(end_number.getText().toString() + 8);
            }else if(view.getId() == R.id.num9) {
                end_number.setText(end_number.getText().toString() + 9);
            }else if(view.getId() == R.id.num0) {
                end_number.setText(end_number.getText().toString() + 0);
            }else if(view.getId() == R.id.back) {
                String word = end_number.getText().toString();
                int input = word.length();
                if (input > 0){
                    end_number.setText(word.substring(0, input-1));
                }
            }else if(view.getId() == R.id.clear) {
                end_number.setText("");
            }
        }
    }

    void showDialog(String[] districtChoose) {
        AlertDialog.Builder dlg2 = new AlertDialog.Builder(RecordPage.this);
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

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //권한 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*..권한이 있는경우 실행할 코드....*/
                } else {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");
                    // 권한설정 클릭시 이벤트 발생
                    alertDialog.setPositiveButton("권한설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    //취소
                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                return;
        }
    }



}
