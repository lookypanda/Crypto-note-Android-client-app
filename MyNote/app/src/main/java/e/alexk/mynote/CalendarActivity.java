package e.alexk.mynote;

import android.app.Activity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class CalendarActivity extends Activity {
    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setSelectionMode(SELECTION_MODE_MULTIPLE);
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        String date = "16.08.2016";
        //LocalDate localDate = LocalDate.(date, format1);
        //calendarView.setDateSelected(CalendarDay.from(2019, 6, 20), true);
        //calendarView.setDateSelected(CalendarDay.from(2019, 6, 24), true);
        int size = SessionConstants.Date.size();
        for (int temp = 0; temp < size; temp++) {
            Calendar Date = SessionConstants.Date.get(temp);
            calendarView.setDateSelected(CalendarDay.from(Date.get(YEAR), Date.get(MONTH)+1 , Date.get(DAY_OF_MONTH)), true);
        }



    }
}
