package com.madcamp.parklee.tab3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.madcamp.parklee.R;
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
    public static DiaryDBHelper diaryDBHelper;
    public static ToDoDBHelper toDoDBHelper;
    public static SQLiteDatabase db;
    // LockScreen Button
    private static boolean isLocked = true;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView materialCalendarView;
    Map<String, String> Todo = new HashMap<>();
    Map<String, Integer> CntTodo = new HashMap<>();
    Map<String, String> Diary = new HashMap<>();
    String[] result = new String[100];
    static int cntkey = 0;
    ImageButton lockButton;
    LinearLayout contentLayout;
    EditText pwEditText;

    private SoundPool soundPool;
    int soundPlay;
    int soundPlay_fail;

    // 달력
    ApiSimulator showspecialday;

    public Fragment3() {
    }

    public static boolean isExistDiary(String date) {
        db = diaryDBHelper.getReadableDatabase();
        String sql = "SELECT * FROM diary WHERE date='" + date + "';";
        Cursor cursor = db.rawQuery(sql, null);
        boolean res = false;
        while (cursor.moveToNext()) {
            res = true;
            Log.d(TAG, "isExistDiary: 다이어리:" + date + ", " + cursor.getString(2));
        }
        cursor.close();
        db.close();
        return res;
    }

    public static boolean isExistTodo(String date) {
        db = toDoDBHelper.getReadableDatabase();
        String sql = "SELECT * FROM todo WHERE date='" + date + "';";
        Cursor cursor = db.rawQuery(sql, null);
        boolean res = false;
        while (cursor.moveToNext()) {
            res = true;
            Log.d(TAG, "isExistTodo: 다이어리:" + date + ", " + cursor.getString(2));
        }
        cursor.close();
        db.close();
        return res;
    }

    public static void InsertDiary(String date, String content) {
        if (isExistDiary(date)) {
            Log.d(TAG, "InsertDiary: 이미 존재함: " + date);
            UpdateDiary(date, content);
        } else {
            db = diaryDBHelper.getWritableDatabase();
            Log.d(TAG, "InsertDiary: 존재하지 않음: " + date);
            String sql = "insert into diary('date', 'content') values(?,?);";
            SQLiteStatement st = db.compileStatement(sql);
            st.bindString(1, date); //date가 들어갈 장소. 만약 존재하면 update라고 해야겠네.
            st.bindString(2, content); // content
            st.execute();
            db.close();
        }
    }

    public static void InsertTodo(String date, String content) {
        if (isExistTodo(date)) {
            Log.d(TAG, "InsertTodo: 이미 존재함: " + date);
            UpdateTodo(date, content);
        } else {
            db = toDoDBHelper.getWritableDatabase();
            Log.d(TAG, "InsertTodo: 존재하지 않음: " + date);
            String sql = "insert into todo('date', 'content') values(?,?);";
            SQLiteStatement st = db.compileStatement(sql);
            st.bindString(1, date); //date가 들어갈 장소. 만약 존재하면 update라고 해야겠네.
            st.bindString(2, content); // content
            st.execute();
        }
        db.close();
    }

    public static void UpdateDiary(String date, String content) {
        if (isExistDiary(date)) {
            Log.d(TAG, "UpdateDiary: 다이어리 존재함");
            db = diaryDBHelper.getWritableDatabase();
            String sql = "UPDATE diary SET content=? WHERE date=?";
            SQLiteStatement st = db.compileStatement(sql);
            st.bindString(1, content);
            st.bindString(2, date);
            st.execute();
            db.close();
        } else {
            Log.d(TAG, "UpdateDiary: 다이어리 존재하지 않음");
        }
    }

    public static void UpdateTodo(String date, String content) {
        if (isExistTodo(date)) {
            Log.d(TAG, "UpdateTodo: 투두 존재함");
            db = toDoDBHelper.getWritableDatabase();
            String sql = "UPDATE todo SET content=? WHERE date=?";
            SQLiteStatement st = db.compileStatement(sql);
            st.bindString(1, content);
            st.bindString(2, date);
            st.execute();
            db.close();
        } else {
            Log.d(TAG, "UpdateTodo: 다이어리 존재하지 않음");
        }
    }

    public static Map<String, String> showDiary() {
        db = diaryDBHelper.getReadableDatabase();

        Map<String, String> res = new HashMap<>();

        String sql = "select * from diary";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(1); // date
            String content = cursor.getString(2); // content
//            Log.d(TAG, "showDiary: Show Diary. [Data]: date: " + date + ", content: " + content);
            res.put(date, content);
        }

        cursor.close();
        db.close();
        return res;
    }

    public static Map<String, String> showTodo() {
        db = toDoDBHelper.getReadableDatabase();

        Map<String, String> res = new HashMap<>();

        String sql = "select * from todo";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(1); // date
            String content = cursor.getString(2); // content
//            Log.d(TAG, "showTodo: Show Todo. [Data]: date: " + date + ", content: " + content);
            res.put(date, content);
        }

        cursor.close();
        db.close();
        return res;
    }

    public void makeData() {
        InsertTodo("2020,7,13", "오늘 할 일 만들기,자료 있는 날짜 표시하게 하기");
        InsertTodo("2020,7,15", "발표하기,밥 먹기");

        InsertDiary("2020,7,13", "오늘은 아침에 일찍 일어나 씻고 밥먹고 N1에 왔다");
        InsertDiary("2020,7,11", "내일은 일요일이다!!! 너무 신난다!!");
        InsertDiary("2020,7,12", "오늘은 아침에 일어나 순대국밥으로 해장을 하고 논문을 읽었다. 매우 뿌듯하다.");
        InsertDiary("2020,7,10", "내일은 토요일이다! 실습실 가는 날이다.");
        InsertDiary("2020,7,14", "7 곱하기 2 = 14이다. 7월 14일이다.");


        Diary = showDiary();
        Todo = showTodo();
        cntkey = 0;
        for (String key : Todo.keySet()) {
            result[cntkey] = key;
            cntkey++;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, ">>> onCreate: DiaryDBHelper 생성");
        diaryDBHelper = new DiaryDBHelper(getContext());
        toDoDBHelper = new ToDoDBHelper(getContext());
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPlay = soundPool.load(getContext(), R.raw.unlock, 1);
        soundPlay_fail = soundPool.load(getContext(), R.raw.unlock_fail, 1);

        db = diaryDBHelper.getReadableDatabase();
        diaryDBHelper.onUpgrade(db, 1, 1); // test용
        db.close();

        db = toDoDBHelper.getReadableDatabase();
        toDoDBHelper.onUpgrade(db, 1, 1); // test용
        db.close();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
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


        showspecialday = new ApiSimulator(result);
        ArrayList<CalendarDay> reulst = showspecialday.getResult();

        materialCalendarView.addDecorators(new EventDecorator(Color.rgb(142, 77, 234), reulst));

        showspecialday.executeOnExecutor(Executors.newSingleThreadExecutor());


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
                    String printodo = "";
                    for (String elt : todo) {
                        printodo = printodo + "-  " + elt + "\n";
                    }
                    textView_todo.setText(printodo);
                } else {
                    textView_todo.setText("");
                }

                textView_day.setText(shot_Day);
                textView_todo_title.setText("오늘 할 일");
                textView_diary_title.setText("오늘의 일기");
                textView_diary.setText(Diary.get(key));

                materialCalendarView.clearSelection();

                ImageButton addDiaryButton = view.findViewById(R.id.Diary_button);
                addDiaryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        View diaryView = inflater.inflate(R.layout.add_diary, container, false);
                        alertDialog.setView(diaryView);
                        final EditText diaryText = diaryView.findViewById(R.id.diary_text);
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String diary = diaryText.getText().toString();
                                InsertDiary(key, diary);
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
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        View todoView = inflater.inflate(R.layout.add_todo, container, false);
                        alertDialog.setView(todoView);
                        final EditText todoText = todoView.findViewById(R.id.todo_text);
                        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String todo = todoText.getText().toString();
                                if (!Todo.containsKey(key)) {
                                    materialCalendarView.addDecorators(new EventDecorator(Color.rgb(142, 77, 234), showspecialday.addResult(key)));
                                }

                                InsertTodo(key, todo);
                                Todo.put(key, todo);
                                String[] todolist = Todo.get(key).split(",");
                                String printodo = "";
                                for (String elt : todolist) {
                                    printodo = printodo + "-  " + elt + "\n";
                                }
                                textView_todo.setText(printodo);
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
                    View dialogView = inflater.inflate(R.layout.password, container, false);
                    alertDialog.setView(dialogView);
                    pwEditText = dialogView.findViewById(R.id.check_pass);
                    alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String pwd = checkPassword();
                            if (pwEditText.getText().toString().equals(pwd)) {
                                Toast.makeText(getContext(), "UnLock!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onCreateView: 비밀번호 맞음");
                                isLocked = false;
                                soundPool.play(soundPlay, 1f, 1f, 0, 0, 1f);
                                lockButton.setSelected(true);
                                contentLayout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "비번틀림!", Toast.LENGTH_SHORT).show();
                                soundPool.play(soundPlay_fail, 1f, 1f, 0, 0, 1f);
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
        String password = "1111";
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
                if (Time_Result[i] != null) {
                    String[] time = Time_Result[i].split(",");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);
                    CalendarDay day = CalendarDay.from(calendar);
                    calendar.set(year, month - 1, dayy);
                    dates.add(day);
                }

            }
            return dates;
        }

        public ArrayList<CalendarDay> getResult() {
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < Time_Result.length; i++) {
                if (Time_Result[i] != null) {
                    String[] time = Time_Result[i].split(",");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);
                    calendar.set(year, month - 1, dayy);
                    CalendarDay day = CalendarDay.from(calendar);
                    dates.add(day);
                }
            }
            return dates;
        }

        public List<CalendarDay> addResult(String date) {
            Time_Result[cntkey] = date;
            cntkey++;

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < Time_Result.length; i++) {
                if (Time_Result[i] != null) {
                    String[] time = Time_Result[i].split(",");
                    int year = Integer.parseInt(time[0]);
                    int month = Integer.parseInt(time[1]);
                    int dayy = Integer.parseInt(time[2]);
                    CalendarDay day = CalendarDay.from(calendar);
                    calendar.set(year, month - 1, dayy);
                    dates.add(day);
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
//            materialCalendarView.addDecorator(new EventDecorator(Color.rgb(142, 77, 234), calendarDays));
        }
    }

}
