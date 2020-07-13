package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

public class Fragment3 extends Fragment {
    private static final String TAG = "Fragment3";

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Map<String, String>Todo = new HashMap<>();
    Map<String, Integer> CntTodo = new HashMap<>();
    Map<String, String>Diary = new HashMap<>();
    String [] result = new String[100];
    int cntkey = 0;


    public void makeData() {
        Todo.put("2020,7,13","오늘 할 일 만들기,자료 있는 날짜 표시하게 하기");
        Todo.put("2020,7,15", "발표하기,밥 먹기");
        Diary.put("2020,7,13","오늘은 아침에 일찍 일어나 씻고 밥먹고 N1에 왔다");
        Diary.put("2020,7,11","내일은 일요일이다!!! 너무 신난다!!");
        Diary.put("2020,7,12","오늘은 아침에 일어나 순대국밥으로 해장을 하고 논문을 읽었다. 매우 뿌듯하다.");

        for(String key : Todo.keySet()){
            result[cntkey] = key;
            cntkey++;
        }
    }

    // LockScreen Button
    private static boolean isLocked = true;
    ImageButton lockButton;
    LinearLayout contentLayout;
    EditText pwEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_3);
    }


    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
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
                final TextView textView_diary = view.findViewById(R.id.diary);
                final TextView textView_todo = view.findViewById(R.id.to_do);

                final String shot_Day = Year + "." + Month + "." + Day;
                final String key = Year + "," + Month + "," + Day;


                if (Todo.containsKey(key)) {
                    String[] todo = Todo.get(key).split(",");
                    String printodo ="";
                    for (String elt : todo){
                        printodo = printodo+ "-  "+elt +"\n";
                    }
                    textView_todo.setText(printodo);
                }


                textView_day.setText(shot_Day);
                textView_todo_title.setText("오늘 할 일");
                textView_diary_title.setText("오늘의 일기");
                textView_diary.setText(Diary.get(key));


                materialCalendarView.clearSelection();

                ImageButton addDiaryButton = view.findViewById(R.id.Diary_button);
                addDiaryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        View diaryView = inflater.inflate(R.layout.add_diary, container, false);
                        alertDialog.setView(diaryView);
                        final EditText diaryText =  diaryView.findViewById(R.id.diary_text);
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String diary = diaryText.getText().toString();
                                Diary.put(key, diary);
                                textView_diary.setText(Diary.get(key));
                            }
                        });
                        alertDialog.show();
                    }
                });
                ImageButton addToDoButton = view.findViewById(R.id.To_Do_button);
                addToDoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        View todoView = inflater.inflate(R.layout.add_todo, container, false);
                        alertDialog.setView(todoView);
                        final EditText todoText =  todoView.findViewById(R.id.todo_text);
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String todo = todoText.getText().toString();
                                if (!Todo.containsKey(key)){
                                    result[cntkey] = key;
                                    Todo.put(key, todo);
                                    textView_todo.setText("-  "+todo);
                                }else{
                                    String addtodo = Todo.get(key);
                                    Todo.put(key, addtodo+","+todo);
                                    if (Todo.containsKey(key)) {
                                        String[] todolst = Todo.get(key).split(",");
                                        String printodo = "";
                                        for (String elt : todolst) {
                                            printodo = printodo + "-  " + elt + "\n";
                                        }
                                        textView_todo.setText(printodo);
                                    }
                                }
                            }
                        });
                        alertDialog.show();
                    }
                });

            }
        });


        // Views about Lock
        lockButton = view.findViewById(R.id.third_lock);
        contentLayout = view.findViewById(R.id.third_text);

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
                                contentLayout.setVisibility(View.VISIBLE);
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
                    contentLayout.setVisibility(View.GONE);
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
            materialCalendarView.addDecorator(new EventDecorator(Color.rgb(244, 201, 107), calendarDays, getActivity()));
        }
    }

    public Fragment3() {
    }

}
