package com.eflexsoft.larrysnews.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.SimpleFormatter;

public class Utils {

    private static ColorDrawable colorDrawable[] = {

            new ColorDrawable(Color.parseColor("#000080")),
            new ColorDrawable(Color.parseColor("#800000")),
            new ColorDrawable(Color.parseColor("#32CD32")),
            new ColorDrawable(Color.parseColor("#228B22")),
            new ColorDrawable(Color.parseColor("#ffffff")),
            new ColorDrawable(Color.parseColor("#9400D3")),
            new ColorDrawable(Color.parseColor("#000000")),
            new ColorDrawable(Color.parseColor("#FF8C00"))


    };

    public static ColorDrawable getRandomColor() {
        int r = new Random().nextInt(7);

        return colorDrawable[r];
    }

    public static String getCountry() {

        Locale locale = Locale.getDefault();

        return locale.getCountry();

    }

    public static String getLanguage() {

        Locale locale = Locale.getDefault();

        return locale.getLanguage();

    }

    public static String getTime(String timeEntered) {

        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));

        String theTime = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

        try {
            Date date = simpleDateFormat.parse(timeEntered);
            theTime = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "• "+theTime;

    }

}
