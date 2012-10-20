package com.example.myfirstapp;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.SimpleAdapter;
import com.example.myfirstapp.RssHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import android.net.Uri;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.util.Log;
import java.util.HashMap;
import java.lang.Thread.UncaughtExceptionHandler;
import android.graphics.drawable.ColorDrawable;

public class RefreshNewsActivity extends ListActivity
{
    private static String TAG = "RefreshNewsActivity";
    private ArrayList<String> urls = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        ListView view = getListView();
        if (view == null)
            Log.e(TAG, "ListView was null");

        ColorDrawable black = new ColorDrawable(
            this.getResources().getColor(R.color.black));
        view.setDivider(black);
        view.setDividerHeight(6);

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

        final String LINE1 = "line1";
        final String LINE2 = "line2";
        String[] from = {LINE1, LINE2};
        //int[] to = {android.R.id.text1, android.R.id.text2};
        int[] to = {R.id.text1, R.id.text2};

        ArrayList<HashMap<String, Object>> list =
            new ArrayList<HashMap<String, Object>>();
        SimpleAdapter adapter = new SimpleAdapter(this, list,
            R.layout.zlist, from, to);
            //android.R.layout.simple_list_item_2, from, to);
        setListAdapter(adapter);

        view.setAdapter(adapter);

        urls.clear();

        for (Article a : rh.rtcls)
        {
            Log.v(TAG, "Adding a button.");
            Log.v(TAG, "Button title is " + a.title);

            UrlButton b = new UrlButton();
            b._article = a;

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(LINE1, a.title);
            map.put(LINE2, b);
            list.add(map);

            urls.add(a.url);

            adapter.notifyDataSetChanged();
        }

        view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                int position, long id)
            {
                String url = urls.get(position);
                Log.v("RefreshNewsActivity", "Opening "+url.toString());
                Intent browse = new Intent(Intent.ACTION_VIEW,
                                           Uri.parse(url));
                startActivity(browse);
            }
        });
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
            try {
                DateFormat formatter = new SimpleDateFormat("EEEE MMMM d h:mm a");
                return formatter.format(_article.getDate());
            } catch (ParseException e) {
                return "";
            }
        }
    }

    /*private void openUrl(String url)
    {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browse);
    }
    */
}
