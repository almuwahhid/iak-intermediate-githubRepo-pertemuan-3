
package iak.almuwahhid.com.iak1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import iak.almuwahhid.com.iak1.Helper.DatabaseHandler;
import iak.almuwahhid.com.iak1.Utilities.NotifUtil;

/**
 * Created by gueone on 5/12/2017.
 */

public class InsertService extends Service {
    DatabaseHandler db_handler;
    String a, b, c, d, e, f;
    NotifUtil ntf;


    @Override
    public void onCreate() {
        super.onCreate();
        db_handler = new DatabaseHandler(this);
        ntf = new NotifUtil();
        Toast.makeText(this, "Service telah dibuat", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                a = intent.getStringExtra("a");
                b = intent.getStringExtra("b");
                c = intent.getStringExtra("c");
                d = intent.getStringExtra("d");
                e = intent.getStringExtra("e");
                f = intent.getStringExtra("f");
                if(db_handler.addData(a,b,c,d,e,f)){
                    ntf.remindInsert(getBaseContext());
                    stopSelf();
                }
            }
        }, 5000);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("good", "onDestroy: Berhasil");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
