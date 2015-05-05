package ru.freask.studyjam.icebox.recivers;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.freask.studyjam.icebox.MainActivity;
import ru.freask.studyjam.icebox.R;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.ProductDao;
import ru.freask.studyjam.icebox.models.Product;

/**
 * Created by FreaskHOME on 04.05.2015.
 */
public class AlarmNotification extends BroadcastReceiver {
    private static OrmHelper ormHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);


        Log.v(MainActivity.TAG, "AlarmNotification OnReceive") ;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notification = new Notification(android.R.drawable.sym_def_app_icon, context.getResources().getString(R.string.notify_title), System.currentTimeMillis());
//Интент для активити, которую мы хотим запускать при нажатии на уведомление

        String notify_text = context.getResources().getString(R.string.notify_text) + " " + getNeededProductsString();
        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.notify_title))
                .setContentText(notify_text)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(notify_text))
                .build();

        Intent intentTL = new Intent(context, MainActivity.class);
        notification.setLatestEventInfo(context,
                context.getResources().getString(R.string.notify_title),
                context.getResources().getString(R.string.notify_text) + getNeededProductsString(),
                PendingIntent.getActivity(context, 0, intentTL,
                        PendingIntent.FLAG_CANCEL_CURRENT));
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);
// Установим следующее напоминание.
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //получение списка продуктов, которые надо прикупить
    private static String getNeededProductsString() {
        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            List<String> products = new ArrayList<>();
            for (Product product : productDao.getNeededProducts()) {
                int need_count = product.like_count - product.count;
                products.add(product.name + "("+need_count+")");
            }
            return MainActivity.implode(", ", products);
        } catch (SQLException e) {
            Log.e(MainActivity.TAG, e.getMessage());
            return "";
        }
    }
}