package com.nicjansma.tisktasks.models;

import java.util.ArrayList;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nicjansma.library.net.CacheableJsonObjectBase;

/**
 * Todoist User.
 */
public final class TodoistUser
    extends CacheableJsonObjectBase
{
    //
    // Constants
    //
    /**
     * Timezone index: GMT string.
     */
    private static final int TIMEZONE_INDEX_GMT = 0;

    /**
     * Timezone index: Hours.
     */
    private static final int TIMEZONE_INDEX_HOURS = 1;

    /**
     * Timezone index: Minutes.
     */
    private static final int TIMEZONE_INDEX_MINUTES = 2;

    /**
     * Timezone index: Is DST.
     */
    private static final int TIMEZONE_INDEX_IS_DST = 3;

    //
    // Privates
    //
    /**
     * User ID.
     */
    private long _id;

    /**
     * User's API token.
     */
    private String _apiToken;

    /**
     * User's email address.
     */
    private String _email;

    /**
     * User's full name.
     */
    private String _fullName;

    /**
     * User's start page query.
     */
    private String _startPage;

    /**
     * User's time zone (string).
     */
    private String _timeZone;

    /**
     * User's time zone offset.
     *
     * Format: [GMT_STRING, HOURS, MINUTES, IS_DAYLIGHT_SAVINGS_TIME]
     * Example: ["+01:00", 1, 0, 0]
     */
    private String _timeZoneOffsetString;

    /**
     * User's time zone offset GMT string.
     *
     * Example: "+01:00"
     */
    private String _timeZoneOffsetGmtString;

    /**
     * User's time zone offset in hours.
     */
    private int _timeZoneOffsetHours;

    /**
     * User's time zone offset in minutes.
     */
    private int _timeZoneOffsetMinutes;

    /**
     * User's time zone offset is in daylight savings.
     */
    private boolean _timeZoneOffsetIsDaylightSavings;

    /**
     * User's time format.
     *
     * If 0 then show time as 13:00, else show time as 1pm.
     */
    private int _timeFormat;

    /**
     * User's date format.
     *
     * If 0 then show dates as DD-MM-YYYY, else show dates as MM-DD-YYYY.
     */
    private int _dateFormat;

    /**
     * User's sort order.
     *
     * If it's 0 then show Oldest dates first when viewing projects. Else Oldest dates last.
     *
     * TODO: Need to implement
     */
    private int _sortOrder;

    /**
     * User's Notifo account.
     */
    private String _notifo;

    /**
     * User's mobile phone number.
     */
    private String _mobileNumber;

    /**
     * User's mobile phone carrier.
     */
    private String _mobileHost;

    /**
     * User's premium account expiry date.
     */
    private String _premiumUntil;

    /**
     * Default time to set a reminder for.
     *
     * TODO: Implement.
     */
    private String _defaultReminder;

    @Override
    protected void initializeInternal(final JSONObject json)
    {
        _id = json.optLong("id");
        _apiToken = json.optString("api_token");
        _email = json.optString("email");
        _fullName = json.optString("full_name");
        _startPage = json.optString("start_page");
        _timeZone = json.optString("timezone");

        // also sets the other timeZoneOffset fields
        setTimeZoneString(json.optString("tz_offset"));

        _timeFormat = json.optInt("time_format", 0);
        _dateFormat = json.optInt("date_format", 0);
        _sortOrder = json.optInt("sort_order", 0);

        _notifo = json.optString("notifo");
        _mobileNumber = json.optString("mobile_number");
        _mobileHost = json.optString("mobile_host");

        _premiumUntil = json.optString("premium_until");
        _defaultReminder = json.optString("default_reminder");
    }

    @Override
    protected JSONObject toJsonInternal(final JSONObject json)
    {
        try
        {
            json.put("id", _id);
            json.put("api_token", _apiToken);
            json.put("email", _email);
            json.put("full_name", _fullName);
            json.put("start_page", _startPage);
            json.put("timezone", _timeZone);
            json.put("tz_offset", _timeZoneOffsetString);
            json.put("time_format", _timeFormat);
            json.put("date_format", _dateFormat);
            json.put("sort_order", _sortOrder);
            json.put("notifo", _notifo);
            json.put("mobile_number", _mobileNumber);
            json.put("mobile_host", _mobileHost);
            json.put("premium_until", _premiumUntil);
            json.put("default_reminder", _defaultReminder);
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Sets the time zone and all associated variables.
     *
     * @param timeZone Timezone to set to.
     */
    private void setTimeZoneString(final String timeZone)
    {
        _timeZoneOffsetString = timeZone;

        try
        {
            JSONArray tzArray = new JSONArray(timeZone);
            if (tzArray.length() == TIMEZONE_INDEX_IS_DST + 1)
            {
                _timeZoneOffsetGmtString = tzArray.getString(TIMEZONE_INDEX_GMT);
                _timeZoneOffsetHours = tzArray.getInt(TIMEZONE_INDEX_HOURS);
                _timeZoneOffsetMinutes = tzArray.getInt(TIMEZONE_INDEX_MINUTES);
                _timeZoneOffsetIsDaylightSavings = (tzArray.getInt(TIMEZONE_INDEX_IS_DST) == 1);
            }
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the user's ID.
     *
     * @return User ID
     */
    public long getId()
    {
        return _id;
    }

    /**
     * Gets the user's API token.
     *
     * @return API Token
     */
    public String getApiToken()
    {
        return _apiToken;
    }

    /**
     * Gets the user's email.
     *
     * @return Email
     */
    public String getEmail()
    {
        return _email;
    }

    /**
     * Gets the user's full name.
     *
     * @return Full name
     */
    public String getFullName()
    {
        return _fullName;
    }

    /**
     * Gets the user's start page string.
     *
     * Example: "od, tod, tom"
     *
     * @return Start page string
     */
    public String getStartPage()
    {
        return _startPage;
    }

    /**
     * Gets a list of the user's start page queries.
     *
     * If empty, "od, tod, tom" is returned.
     *
     * @return List of a user's start page queries.
     */
    public ArrayList<String> getStartPageQueries()
    {
        ArrayList<String> queries = new ArrayList<String>();

        String[] startPageSplit = _startPage.split(",");
        for (int i = 0; i < startPageSplit.length; i++)
        {
            queries.addAll(sanitizeQuery(startPageSplit[i]));
        }

        // if there aren't any queries (they have the info page, blank page, or a project set),
        // then give them "od, tod, tom"
        if (queries.size() == 0)
        {
            queries.add("od");
            queries.addAll(sanitizeQuery("tod"));
            queries.addAll(sanitizeQuery("tom"));
        }

        return queries;
    }

    /**
     * Sanitizes a start page query.
     *
     * @param query Single start page query term.
     *
     * @return Sanitized query
     */
    public static ArrayList<String> sanitizeQuery(final String query)
    {
        String trimmedQuery = query.trim();

        ArrayList<String> queries = new ArrayList<String>();

        // NOTE: Doesn't support _project queries, e.g. _project_882871
        // In this case, the website redirects to the project in question.
        // We decide to ignore this choice and show them an empty Today screen.

        // TODO: Could instead go to the Project List or direct project screen
        // as long as the back-stack to Project List works.

        if (trimmedQuery.startsWith("priority "))
        {
            // tasks of a priority
            queries.add(trimmedQuery.replace("priority ", "p"));
        }
        else if (trimmedQuery.equals("tod"))
        {
            // today's tasks
            DateTime today = new DateTime();
            queries.add(today.toString(ISODateTimeFormat.dateHourMinuteSecond()));
        }
        else if (trimmedQuery.equals("tom"))
        {
            // tomorrow's tasks
            DateTime tomorrow = new DateTime().plusDays(1);
            queries.add(tomorrow.toString(ISODateTimeFormat.dateHourMinuteSecond()));
        }
        else if (trimmedQuery.endsWith("days"))
        {
            // N days

            // add a query for each day from now until X days
            trimmedQuery = trimmedQuery.replace(" days", "");

            DateTime today = new DateTime();

            // get number of days
            int queryDays = Integer.parseInt(trimmedQuery);

            // add each day as a new query
            for (int i = 0; i < queryDays; i++)
            {
                DateTime day = today.plusDays(i);
                queries.add(day.toString(ISODateTimeFormat.dateHourMinuteSecond()));
            }
        }
        else if (trimmedQuery.equals("_info_page")
                 || trimmedQuery.equals("_blank")
                 || trimmedQuery.startsWith("_project"))
        {
            // user hasn't set a home-page.  leave blank
            // TODO: Work for _project
            queries.size(); // NOP
        }
        else
        {
            // pass-through.
            // Known: "od" Overdue
            //        "no date" No date
            queries.add(trimmedQuery);
        }

        return queries;
    }

    /**
     * Gets the user's time zone.
     *
     * @return Time zone.
     */
    public String getTimeZone()
    {
        return _timeZone;
    }

    /**
     * Gets the user's time zone offset string.
     *
     * Format: [GMT_STRING, HOURS, MINUTES, IS_DAYLIGHT_SAVINGS_TIME]
     * Example: ["+01:00", 1, 0, 0]
     *
     * @return Time zone offset string.
     */
    public String getTimeZoneOffsetString()
    {
        return _timeZoneOffsetString;
    }

    /**
     * Gets the user's time zone offset GMT string.
     *
     * Example: "+01:00"
     *
     * @return Time zone offset GMT string
     */
    public String getTimeZoneOffsetGmtString()
    {
        return _timeZoneOffsetGmtString;
    }

    /**
     * Gets the user's time zone offset in hours.
     *
     * @return Time zone offset in hours
     */
    public int getTimeZoneOffsetHours()
    {
        return _timeZoneOffsetHours;
    }

    /**
     * Gets the user's time zone offset in minutes.
     *
     * @return Time zone offset in minutes.
     */
    public int getTimeZoneOffsetMinutes()
    {
        return _timeZoneOffsetMinutes;
    }

    /**
     * Returns true if the user's time zone is in daylight savings.
     *
     * @return True if the user's time zone is in daylight savings.
     */
    public boolean isTimeZoneOffsetDaylightSavings()
    {
        return _timeZoneOffsetIsDaylightSavings;
    }

    /**
     * Gets the user's time zone format.
     *
     * If it's 0 then show time as 13:00, else show time as 1pm.
     *
     * @return Time zone format.
     */
    public int getTimeFormat()
    {
        return _timeFormat;
    }

    /**
     * Gets the user's date format.
     *
     * If it's 0 then show dates as DD-MM-YYYY, else show dates as MM-DD-YYYY.
     *
     * @return Date format.
     */
    public int getDateFormat()
    {
        return _dateFormat;
    }

    /**
     * Gets the user's sort order.
     *
     * If it's 0 then show Oldest dates first when viewing projects. Else Oldest dates last.
     *
     * @return Sort order
     */
    public int getSortOrder()
    {
        return _sortOrder;
    }

    /**
     * Gets the Notifo account.
     *
     * @return Notifo account
     */
    public String getNotifo()
    {
        return _notifo;
    }

    /**
     * Gets the user's mobile number.
     *
     * @return Mobile number
     */
    public String getMobileNumber()
    {
        return _mobileNumber;
    }

    /**
     * Gets the user's mobile host.
     *
     * @return Mobile host
     */
    public String getMobileHost()
    {
        return _mobileHost;
    }

    /**
     * Gets how long the user's account is premium until.
     *
     * @return How long the user's account is premium until
     */
    public String getPremiumUntil()
    {
        return _premiumUntil;
    }

    /**
     * Gets the user's default reminder length.
     *
     * @return Default reminder length
     */
    public String getDefaultReminder()
    {
        return _defaultReminder;
    }

    /**
     * Gets the formatted date for a user.
     *
     * @param year Year
     * @param month Month
     * @param day Day
     *
     * @return Date formatted to a user's preferences
     */
    public String getFormattedDate(final int year, final int month, final int day)
    {
        // If it's 0 then show dates as DD-MM-YYYY, else show dates as MM-DD-YYYY.
        if (_dateFormat == 0)
        {
            return String.format(Locale.US, "%d-%d-%d", day, month, year);
        }
        else
        {
            return String.format(Locale.US, "%d-%d-%d", month, day, year);
        }
    }
}
