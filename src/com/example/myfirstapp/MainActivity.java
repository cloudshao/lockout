package com.example.myfirstapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import java.lang.Thread;
import java.lang.Thread.UncaughtExceptionHandler;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        final UncaughtExceptionHandler subclass =
            Thread.currentThread().getUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(
            new Thread.UncaughtExceptionHandler()
            {
                @Override
                public void uncaughtException(
                    Thread paramThread, Throwable paramThrowable)
                {
                    String st = Log.getStackTraceString(paramThrowable);
                    Log.v("Uncaught exception.", st);
                    subclass.uncaughtException(paramThread, paramThrowable);
                }
            }
        );

        System.out.println("println");
        Log.v("MainActivity", "Log.v");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void refresh(View view)
    {
        Intent intent = new Intent(this, RefreshNewsActivity.class);
        startActivity(intent);
    }
}
