package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Fragment3 extends Fragment {
    private static final String TAG = "Fragment3";

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;

    // LockScreen Button
    private static boolean isLocked = true;
    ImageButton lockButton;
    TextView textView;
    EditText pwEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_3);
    }

    @SuppressLint("ClickableViewAccessibility") // App Accessibility는 나중에 고려
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        materialCalendarView = view.findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2020,7,10", "2017,04,18", "2017,05,18", "2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "," + Month + "," + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                Toast.makeText(getActivity().getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();
            }
        });


        // Views about Lock
        lockButton = view.findViewById(R.id.third_lock);
        textView = view.findViewById(R.id.third_text);

        isLocked = true;
        lockButton.setSelected(false);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocked) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//                    alertDialog.setMessage("비밀번호를 입력하세요");
                    View dialogView = inflater.inflate(R.layout.password, container, false);
                    alertDialog.setView(dialogView);
                    pwEditText = (EditText) dialogView.findViewById(R.id.check_pass);
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pwd = checkPassword();
                            if(pwEditText.getText().toString().equals(pwd)){
                                Toast.makeText(getContext(), "UnLock!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreateView: 비밀번호 맞음");
                                isLocked = false;
                                lockButton.setSelected(true);
                                textView.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getContext(), "비번틀림!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreateView: 비밀번호 틀림");
                            }
                        }
                    });
                    alertDialog.show();
                } else {
                    Toast.makeText(getContext(), "Locked!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onCreateView: isLocked False");
                    isLocked = true;
                    lockButton.setSelected(false);
                    textView.setVisibility(View.GONE);
                }
            }


        });

        return view;
    }

    private String checkPassword() {
        String password="1111";
        return password;
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year, month - 1, dayy);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (Objects.requireNonNull(getActivity()).isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.rgb(244, 201, 107), calendarDays, getActivity()));
        }
    }

    public Fragment3() {
    }

}
