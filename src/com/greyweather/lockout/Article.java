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
    private static final DateFormat inputDateFormatter = 
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private static final DateFormat outputDateFormatter = 
        new SimpleDateFormat("EEEE MMMM d h:mm a");

    /**
     * Returns the formatted date
     * @param d    a Date object
     * @return     the local date/time formatted for display
     */
    public String getFormattedDate()
    {
        try {
            return outputDateFormatter.format(_getDate());
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
        Date dateObj = inputDateFormatter.parse(_date);
        return dateObj;
    }
}
