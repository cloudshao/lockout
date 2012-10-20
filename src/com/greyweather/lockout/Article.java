package com.greyweather.lockout;

import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * An RSS item
 */
public class Article
{
    public String _title = "";
    public String _url = "";
    public String _date = "";

    /**
     * Returns the formatted date
     * @param d    a Date object
     * @return     the local date/time formatted for display
     */
    public String getFormattedDate()
    {
        try {
            DateFormat formatter =
                new SimpleDateFormat("EEEE MMMM d h:mm a");
            return formatter.format(_getDate());
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * Returns the date
     * @return  the date as a Date object
     */
    private Date _getDate() throws ParseException
    {
        DateFormat formatter =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date dateObj = formatter.parse(_date);

        return dateObj;
    }
}
