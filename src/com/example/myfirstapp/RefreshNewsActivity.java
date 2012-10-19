package com.example.myfirstapp;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import com.example.myfirstapp.RssHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.net.Uri;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;

public class RefreshNewsActivity extends ListActivity
{
    private static String TAG = "RefreshNewsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        ListView view = getListView();
        if (view == null)
            Log.e(TAG, "ListView was null");

        // Show the view to the user
        setContentView(view);

        super.onCreate(savedInstanceState);

        RssHandler rh = new RssHandler();
        try 
        {
            rh.createFeed(this, new URL("http://news.google.com/news/feeds?q=nhl+lockout&hl=en&safe=off&prmd=imvnsu&bav=on.2,or.r_gc.r_pw.r_cp.r_qf.&bpcl=35277026&biw=1196&bih=697&um=1&ie=UTF-8&output=rss"));
        }
        catch (Exception e)
        {
            Log.v("Caught exception.", "Message: "+e.getMessage());
        }

        Log.v(TAG, "Created new RssHandler.");

        String content = "Default empty string";
        if (!rh.rtcls.isEmpty())
            content = rh.rtcls.get(0).title;

        Log.v(TAG, "Read content.");

        ArrayList<UrlButton> list = new ArrayList<UrlButton>();
        ArrayAdapter<UrlButton> adapter = new ArrayAdapter<UrlButton>(
            this, android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);

        for (Article a : rh.rtcls)
        {
            Log.v(TAG, "Adding a button.");

            UrlButton b = new UrlButton();
            b._article = a;

            Log.v(TAG, "Button title is " + a.title);
            list.add(b);
        }
        view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

/*
    class UrlAdapter extends ArrayAdapter
    {
        @Override
        public View getView()
        {
            TextView view = new TextView(this.getContext());
            textView.setTextSize(20);
            textView.setText(
*/

    class UrlButton //extends View //implements OnClickListener
    {
        public Article _article;
        
        //@Override
        public void onClick(View v)
        {
            Log.v("onClick", _article.url.toString());
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(_article.url.toString()));
            startActivity(browse);
        }

        public String toString()
        {
            return _article.title;
        }

    }

    /*private void openUrl(String url)
    {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browse);
    }
    */
}
