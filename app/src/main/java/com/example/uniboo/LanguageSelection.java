package com.example.uniboo;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageSelection {
    private Context context;
    public LanguageSelection(Context c){
        context = c;
    }

    public void updateResource(String code){
        Locale locale=new Locale(code);
        Locale.setDefault(locale);
        Resources resources= context.getResources();
        Configuration conf=resources.getConfiguration();
        conf.locale=locale;
        resources.updateConfiguration(conf,resources.getDisplayMetrics());
    }
}
