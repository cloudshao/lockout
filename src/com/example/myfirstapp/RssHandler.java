package com.example.myfirstapp;

import android.content.Context;
import android.util.Log;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.IOException;

public class RssHandler extends DefaultHandler
{
    private boolean _inItem = false;
    private boolean _inTitle = false;
    private boolean _inLink = false;

    private Article currRtcl;
    public ArrayList<Article> rtcls = new ArrayList<Article>();

    public void createFeed(Context ctx, URL url)
        throws SAXException, ParserConfigurationException, IOException,
               MalformedURLException
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(this);
        xr.parse(new InputSource(url.openStream()));
    }

    public void startElement(String uri, String name, String qName,
                             Attributes attrs)
    {
        if (name.trim().equals("title"))
            _inTitle = true;
        else if (name.trim().equals("link"))
            _inLink = true;
        else if (name.trim().equals("item"))
        {
            _inItem = true;
            currRtcl = new Article();
        }
    }

    public void endElement(String uri, String name, String qName)
        throws SAXException
    {
        if (name.trim().equals("title"))
            _inTitle = false;
        else if (name.trim().equals("link"))
            _inLink = false;
        else if (name.trim().equals("item"))
        {
            _inItem = false;
            rtcls.add(currRtcl);
        }
    }

    public void characters(char ch[], int start, int length)
    {
        String chars = new String(ch).substring(start, start + length);
        if (_inItem)
        {
            if (_inLink)
                try
                {
                    currRtcl.url = new URL(chars);
                }
                catch (MalformedURLException e)
                {
                    /*e.printStackTrace();
                    Log.v("Could not create new URL.",
                          "Message: "+e.getMessage());
                    */
                }
            if (_inTitle)
                currRtcl.title = chars;
        }
    }
}
