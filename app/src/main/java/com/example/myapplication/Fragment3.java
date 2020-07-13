package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Fragment3 extends Fragment {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Map<String, String[]>Todo = new HashMap<>();
    Map<String, String>Diary = new HashMap<>();
    String [] result = new String[100];

    public void makeData() {
        Todo.put("2020,7,13", new String[]{"오늘 할 일 만들기", "자료 있는 날짜 표시하게 하기"});
        Todo.put("2020,7,15", new String[]{"발표하기", "밥 먹기"});
        Diary.put("2020,7,13","오늘은 아침에 일찍 일어나 씻고 밥먹고 N1에 왔다");
        Diary.put("2020,7,11","내일은 일요일이다!!! 너무 신난다!!");
        Diary.put("2020,7,12","오늘은 아침에 일어나 순대국밥으로 해장을 하고 논문을 읽었다. 매우 뿌듯하다.");

        int i = 0;
        for(String key : Todo.keySet()){
            result[i] = key;
            i++;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_3);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_3, container, false);
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

        makeData();

        //String[] result = {"2020,7,10","2017,04,18","2017,05,18","2017,06,18"};


        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();
                TextView textView_day = view.findViewById(R.id.day);
                TextView textView_todo_title = view.findViewById(R.id.to_do_title);
                TextView textView_diary_title = view.findViewById(R.id.diary_title);
                TextView textView_diary = view.findViewById(R.id.diary);
                TextView textView_todo = view.findViewById(R.id.to_do);


                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day =  Year + "." + Month + "." + Day;
                String key = Year+","+Month+","+Day;
                String todo = new String();

                if (Todo.containsKey(key)) {
                    for (String values : Objects.requireNonNull(Todo.get(key))) {
                        todo = todo + "-  " + values + "\n";
                    }
                }

                textView_day.setText(shot_Day);
                textView_todo_title.setText("오늘 할 일");
                textView_diary_title.setText("오늘의 일기");
                textView_diary.setText(Diary.get(key));
                textView_todo.setText(todo);

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                //Toast.makeText(getActivity().getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }





    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
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
            for(int i = 0 ; i < Time_Result.length ; i ++) {
                if (Time_Result[i] != null) {

                    CalendarDay day = CalendarDay.from(calendar);
                    String[] time = Time_Result[i].split(",");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);

                    dates.add(day);
                    calendar.set(year, month - 1, dayy);
                }
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (Objects.requireNonNull(getActivity()).isFinishing()) {
                return;
            }
            materialCalendarView.addDecorator(new EventDecorator(Color.rgb(244,201,107), calendarDays, getActivity()));
        }
    }

    public Fragment3() {
    }

}
