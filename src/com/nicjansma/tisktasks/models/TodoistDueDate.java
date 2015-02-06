package com.nicjansma.tisktasks.models;

import android.graphics.Color;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.nicjansma.tisktasks.ServiceLocator;

/**
 * Todoist Due Date constants.
 */
public final class TodoistDueDate
{
    //
    // Constants
    //
    /**
     * Overdue color.
     */
    public static final int COLOR_OVERDUE = Color.rgb(247, 203, 203);

    /**
     * Soon color.
     */
    public static final int COLOR_SOON = Color.rgb(255, 255, 204);

    /**
     * Tomorrow color.
     */
    public static final int COLOR_TOMORROW = Color.rgb(217, 232, 254);

    /**
     * Today color.
     */
    public static final int COLOR_TODAY = Color.rgb(184, 247, 182);

    /**
     * No color.
     */
    public static final int COLOR_NONE = 0;

    /**
     * Days when a task is overdue for date-diff.
     */
    public static final int DAYS_OVERDUE = -2;

    /**
     * Yesterday for date-diff.
     */
    public static final int DAYS_YESTERDAY = -1;

    /**
     * Today for date-diff.
     */
    public static final int DAYS_TODAY = 0;

    /**
     * Tomorrow for date-diff.
     */
    public static final int DAYS_TOMORROW = 1;

    /**
     * Maximum "soon" days for date-diff.
     */
    public static final int DAYS_SOON_MAX = 7;

    //
    // Privates
    //
    /**
     * Color string.
     */
    private String _colorString;

    /**
     * Color.
     */
    private int _color;

    /**
     * Date-Time.
     */
    private DateTime _dateTime;

    /**
     * TodoistDueDate constructor.
     *
     * @param dateTime Date time
     * @param hasTimeSpecified Date has the time specified
     */
    public TodoistDueDate(final DateTime dateTime, final boolean hasTimeSpecified)
    {
        _dateTime = dateTime;
        _colorString = "";
        _color = COLOR_NONE;

        if (_dateTime == null)
        {
            return;
        }

        DateTime now = new DateTime().plusHours(ServiceLocator.userManager().getCurrentUser().getTimeZoneOffsetHours());

        //
        // date
        //
        Days daysDiff = Days.daysBetween(now.toDateMidnight(), _dateTime.toDateMidnight());

        if (daysDiff.getDays() <= DAYS_OVERDUE)
        {
            _colorString = _dateTime.toString("MMM d");
            _color = TodoistDueDate.COLOR_OVERDUE;
        }
        else if (daysDiff.getDays() == DAYS_OVERDUE)
        {
            _colorString = "Yes";
            _color = TodoistDueDate.COLOR_OVERDUE;
        }
        else if (daysDiff.getDays() == DAYS_TODAY)
        {
            _colorString = "Tod";
            _color = TodoistDueDate.COLOR_TODAY;
        }
        else if (daysDiff.getDays() == DAYS_TOMORROW)
        {
            _colorString = "Tom";
            _color = TodoistDueDate.COLOR_TOMORROW;
        }
        else if (daysDiff.getDays() < DAYS_SOON_MAX)
        {
            _colorString = _dateTime.toString("EEE");
            _color = TodoistDueDate.COLOR_SOON;
        }
        else if (now.year().get() != _dateTime.year().get())
        {
            _colorString = _dateTime.toString("MMM d yyyy");
            _color = TodoistDueDate.COLOR_NONE;
        }
        else
        {
            _colorString = _dateTime.toString("MMM d");
            _color = TodoistDueDate.COLOR_NONE;
        }

        //
        // hour
        //
        if (hasTimeSpecified)
        {
            _colorString += " @ " + _dateTime.toString("Ha");
        }
    }

    /**
     * Sets the color string.
     *
     * @param colorString Color string
     */
    public void setString(final String colorString)
    {
        _colorString = colorString;
    }

    /**
     * Sets the color.
     *
     * @param color Color
     */
    public void setColor(final int color)
    {
        _color = color;
    }

    /**
     * Gets the color string.
     *
     * @return Color string
     */
    public String getString()
    {
        return _colorString;
    }

    /**
     * Gets the color.
     *
     * @return Color
     */
    public int getColor()
    {
        return _color;
    }
}
