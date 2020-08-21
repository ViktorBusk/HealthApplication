package com.viktor.health;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//TODO: Add animations, add time- and date-picker, add number-picker for values, save data to FireBase

public class MainActivity2 extends AppCompatActivity {

    /*
    First enter a date picker
    https://www.google.com/search?q=android+calendar+view&client=ubuntu&hs=kGD&channel=fs&sxsrf=ALeKk01JXvWD0TQ9LxEt5y6eyLJuR91jaA:1597868099113&source=lnms&tbm=isch&sa=X&ved=2ahUKEwio3cfDiqjrAhXLwosKHSksB70Q_AUoAXoECA0QAw&biw=1920&bih=969#imgrc=afinnmnJh34h0M
    You can press a date and it will load all saved data from that date from FireBase
    you are then going to manually enter the data in text into a Time picker etc and save if to FireBase
    The chart will be updated after that
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}