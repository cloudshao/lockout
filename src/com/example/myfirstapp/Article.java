package com.example.myfirstapp;

import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

public class Article
{
    public String title = "";
    public String url = "";
    public String body = "";
    public String date = "";

    public Date getDate() throws ParseException
    {
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date dateObj = formatter.parse(date);

        return dateObj;
    }
}
