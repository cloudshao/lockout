package com.greyweather.lockout;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses an RSS feed, given the URL of a feed.
 *
 * Clients should trigger the parse using createFeed, then retrieve
 * the results by calling getArticles.
 */
public class RssHandler extends DefaultHandler
{
    private boolean _inItem = false;
    private boolean _inTitle = false;
    private boolean _inLink = false;
    private boolean _inDate = false;

    private Article currRtcl;
    private ArrayList<Article> _rtcls = new ArrayList<Article>();

    /**
     * Should be called after createFeed
     * @return  all the articles found in this RSS feed
     */
    public ArrayList<Article> getArticles()
    {
        return _rtcls;
    }

    /**
     * Initializes and parses the feed
     * @post    the parsed articles can be accessed using getArticles
     */
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

    @Override
    public void startElement(String uri, String name, String qName,
                             Attributes attrs)
    {
        if (name.trim().equals("title"))
            _inTitle = true;
        else if (name.trim().equals("link"))
            _inLink = true;
        else if (name.trim().equals("pubDate"))
            _inDate = true;
        else if (name.trim().equals("item"))
        {
            _inItem = true;
            currRtcl = new Article();
        }
    }

    @Override
    public void endElement(String uri, String name, String qName)
        throws SAXException
    {
        if (name.trim().equals("title"))
            _inTitle = false;
        else if (name.trim().equals("link"))
            _inLink = false;
        else if (name.trim().equals("pubDate"))
            _inDate = false;
        else if (name.trim().equals("item"))
        {
            _inItem = false;
            _rtcls.add(currRtcl);
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
    {
        String chars = new String(ch, start, length);
        if (_inItem)
        {
            if (_inLink)
                currRtcl._url += chars;

            if (_inTitle)
                currRtcl._title += chars;

            if (_inDate)
                currRtcl._date += chars;
        }
    }
}
