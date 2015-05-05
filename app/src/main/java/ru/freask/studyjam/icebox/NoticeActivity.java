package ru.freask.studyjam.icebox;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.freask.studyjam.icebox.adapters.ProductAdapter;
import ru.freask.studyjam.icebox.recivers.AlarmNotification;

/**
 * Created by FreaskHOME on 04.05.2015.
 */
public class NoticeActivity extends BaseActivity implements View.OnClickListener, NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String TAG = "TAG";
    Context context;
    SharedPreferences sp;
    TimePicker time;
    CheckBox checkBox;
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        setContentView(R.layout.activity_notice);
        navigationDrawerSetUp();

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        FloatingActionButton searchButton = (FloatingActionButton) findViewById(R.id.notice_save_but);
        searchButton.setColorNormalResId(R.color.pink);
        searchButton.setColorPressedResId(R.color.pink_pressed);
        //button.setIcon(R.drawable.ic_fab_star);
        searchButton.setStrokeVisible(false);
        searchButton.setOnClickListener(this);

        time = (TimePicker) findViewById(R.id.timePicker);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        Boolean notifications = sp.getBoolean("notifications", false);
        checkBox.setChecked(notifications);
        if (notifications) {
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            time.setCurrentHour(sp.getInt("notifications.h", calendar.get(Calendar.HOUR_OF_DAY)));
            time.setCurrentMinute(sp.getInt("notifications.m", calendar.get(Calendar.MILLISECOND)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice_save_but:
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("notifications", checkBox.isChecked());
                if (checkBox.isChecked()) {
                    editor.putInt("notifications.h", time.getCurrentHour());
                    editor.putInt("notifications.m", time.getCurrentMinute());
                }
                editor.apply();
                setNotify(checkBox.isChecked());
                this.finish();
                break;
        }
    }

    private void setNotify(boolean set) {
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
// На случай, если мы ранее запускали активити, а потом поменяли время,
// откажемся от уведомления
        am.cancel(pendingIntent);
// Устанавливаем разовое напоминание
        if (set) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
            cal.set(Calendar.MINUTE, time.getCurrentMinute());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

            String notice_on_str = getResources().getString(R.string.notice_on);

            Toast.makeText(context, notice_on_str.replace("%%", time.getCurrentHour().toString() + ':' + time.getCurrentMinute().toString()), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.notice_off, Toast.LENGTH_LONG).show();
        }
    }
}
