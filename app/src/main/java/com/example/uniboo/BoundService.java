package com.example.uniboo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BoundService extends Service {
    public BoundBinder binder = new BoundBinder();
    public BoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public String getOrderTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss  dd/mm/yyyy", Locale.US);
        return simpleDateFormat.format(new Date());
    }

    public class BoundBinder extends Binder {
        BoundService getBoundService(){
            return BoundService.this;
        }
    }
}