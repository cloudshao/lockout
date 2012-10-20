package com.greyweather.lockout;

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
import com.greyweather.lockout.RssHandler;
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
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main activity for this app
 *
 * Gets the RSS feed and shows it in a ListView
 */
public class RefreshNewsActivity extends ListActivity
{
    private final static String TAG = "RefreshNewsActivity";
    private final static String LINE1 = "line1";
    private final static String LINE2 = "line2";
    private ArrayList<String> urls = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       _rebuildList(); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return true;
    }

    /**
     * Handler for options menu item press
     * @return true if the item was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_refresh)
        {
            _rebuildList();
            return true;
        }

        return false;
    }

    /**
     * Builds the view and fills it with newly parsed RSS data
     */
    private void _rebuildList()
    {
        // Grab the list view
        ListView view = getListView();
        if (view == null)
            Log.e(TAG, "ListView was null");

        // Set big dividers - programmatically, we're not formatting
        // this view using xml
        ColorDrawable black = new ColorDrawable(
            this.getResources().getColor(R.color.black));
        view.setDivider(black);
        view.setDividerHeight(6);

        // Show the view to the user
        setContentView(view);

        // Read in the RSS feed
        RssHandler rh = new RssHandler();
        try 
        {
            rh.createFeed(this, new URL(
                this.getResources().getString(R.string.feed_url)));
            Log.v(TAG, "Created new RssHandler.");
        }
        catch (Exception e)
        {
            Log.v(TAG, "Caught exception trying to " +
                       "parse rss: "+e.getMessage());
        }

        // Create a custom adapter that uses lists of 2-member
        // hashmaps. The first is the article button and second is date
        ArrayList<HashMap<String, Object>> list =
            new ArrayList<HashMap<String, Object>>();
        String[] from = {LINE1, LINE2};
        int[] to = {R.id.text1, R.id.text2};

        SimpleAdapter adapter = new SimpleAdapter(this, list,
            R.layout.list, from, to);

        // Bind the view to our new adapter
        setListAdapter(adapter);

        view.setAdapter(adapter);

        urls.clear();
        for (Article a : rh.getArticles())
        {
            // Create a new item
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(LINE1, a._title);
            map.put(LINE2, a.getFormattedDate());

            // Add the item to the list
            list.add(map);

            // Keep track of the url (in the same order)
            urls.add(a._url);

        }
        adapter.notifyDataSetChanged();

        // Set an object to handle item clicks for this view
        view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,
                int position, long id)
            {
                String url = urls.get(position);
                Log.v(TAG, "Opening "+url.toString());
                Intent browse =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browse);
            }
        });
    }
}

