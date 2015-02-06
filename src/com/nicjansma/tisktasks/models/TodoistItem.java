package com.nicjansma.tisktasks.models;

import android.graphics.Color;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.nicjansma.tisktasks.ServiceLocator;

/**
 * Todoist item (task).
 * @author Nic
 */
public final class TodoistItem
    extends TodoistObjectBase
{
    /**
     * Completed color (grey).
     */
    public static final int COMPLETE_COLOR = Color.rgb(0xaa, 0xaa, 0xaa);

    /**
     * Default color (black).
     */
    public static final int DEFAULT_COLOR = Color.rgb(0, 0, 0);

    //
    // Privates
    //
    /**
     * User ID.
     */
    private long _userId;

    /**
     * Project ID.
     */
    private long _projectId;

    /**
     * Content (task text).
     */
    private String _content;

    /**
     * Content for display (content with formatting stripped).
     */
    private String _contentForDisplay;

    /**
     * Due date time.
     */
    private DateTime _dueDateTime;

    /**
     * Due date string.
     */
    private String _dueDateString;

    /**
     * Due date user string.
     */
    private String _dueDateUserString;

    /**
     * Task is in history.
     */
    private boolean _inHistory;

    /**
     * Task priority.
     */
    private int _priority;

    /**
     * Task is checked.
     */
    private boolean _checked;

    /**
     * TodoistItem constructor.
     */
    public TodoistItem()
    {
        super();
    }

    /**
     * TodoistItem constructor.
     *
     * @param itemId Item ID
     * @param userId User ID
     * @param projectId Project ID
     * @param content Content text
     * @param dueDateString Due date string
     * @param inHistory Task is in history
     * @param collapsed Task is collapsed
     * @param indent Task indent
     * @param itemOrder Task order
     */
    public TodoistItem(
        final long itemId,
        final long userId,
        final long projectId,
        final String content,
        final String dueDateString,
        final boolean inHistory,
        final boolean collapsed,
        final int indent,
        final int itemOrder)
    {
        super(itemId, collapsed, indent, itemOrder);

        _userId = userId;
        _projectId = projectId;
        _content = content;
        _contentForDisplay = null;
        setDueDate(dueDateString);
        _inHistory = inHistory;
    }

    @Override
    protected void initializeInternalTodoist(final JSONObject json)
    {
        _userId = json.optLong("user_id");
        _projectId = json.optLong("project_id");
        _content = json.optString("content");
        _contentForDisplay = null;
        _inHistory = json.optInt("in_history", 0) == 1;
        _priority = json.optInt("priority");
        _checked = json.optInt("checked", 0) == 1;
        _dueDateUserString = json.optString("date_string");

        if (json.has("due_date") && !json.isNull("due_date"))
        {
            setDueDate(json.optString("due_date"));
        }
    }

    @Override
    protected JSONObject toJsonInternalTodoist(final JSONObject json)
    {
        try
        {
            json.put("user_id", _userId);
            json.put("project_id", _projectId);
            json.put("content", _content);
            json.put("in_history", _inHistory);
            json.put("priority", _priority);
            json.put("checked", _checked);
            json.put("date_string", _dueDateUserString);
            json.putOpt("due_date", _dueDateString);
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Sets the due date.
     *
     * @param dueDateString Due date string
     */
    private void setDueDate(final String dueDateString)
    {
        if (dueDateString != null && dueDateString.length() > 0)
        {
            Date parsedDate = new Date(Date.parse(dueDateString));
            _dueDateTime = new DateTime(parsedDate);

            if (hasTimeSpecified())
            {
                // the due date is set to 11:59:59 on the date of, unless the time is set as well.
                // In that case, we need to apply the TZ offset
                int offsetHours = ServiceLocator.userManager().getCurrentUser().getTimeZoneOffsetHours();
                _dueDateTime = _dueDateTime.plusHours(offsetHours);
            }

            _dueDateString = dueDateString;
        }
    }

    /**
     * Gets the User ID.
     *
     * @return User ID
     */
    public long getUserId()
    {
        return _userId;
    }

    /**
     * Gets the Project ID.
     *
     * @return Project ID
     */
    public long getProjectId()
    {
        return _projectId;
    }

    /**
     * Gets the due date.
     *
     * @return Due date
     */
    public DateTime getDueDate()
    {
        return _dueDateTime;
    }

    /**
     * Gets the due date string.
     *
     * @return Due date string
     */
    public String getDueDateString()
    {
        return _dueDateString;
    }

    /**
     * Gets the due date user string.
     *
     * @return Due date user string
     */
    public String getDueDateUserString()
    {
        return _dueDateUserString;
    }

    /**
     * Gets a formatted date.
     *
     * @return The formatted date
     */
    public TodoistDueDate getFormattedDueDate()
    {
        return new TodoistDueDate(_dueDateTime, hasTimeSpecified());
    }

    /**
     * Determines if the user has specified a time for the due date.
     *
     * @return True if there is a time specified in the date
     */
    public boolean hasTimeSpecified()
    {
        return _dueDateUserString.contains("@") || _dueDateUserString.contains(" at ");
    }

    /**
     * Determines if the task is recurring.
     *
     * @return True if the task is recurring
     */
    public boolean isRecurring()
    {
        return _dueDateUserString.startsWith("ev");
    }

    /**
     * Determines if the task is in the history.
     *
     * @return True if the task is in the history
     */
    public boolean isInHistory()
    {
        return _inHistory;
    }

    /**
     * Gets the task's priority.
     *
     * @return Task priority
     */
    public int getPriority()
    {
        return _priority;
    }

    /**
     * Gets the color of the task.
     *
     * @return Task color
     */
    public int getColor()
    {
        // checked overwrites everything else
        if (_checked)
        {
            return COMPLETE_COLOR;
        }

        // otherwise return based on priority
        return TodoistPriority.getColorFromPriority(_priority);
    }

    /**
     * Gets the content (text) of a task.
     *
     * @return Task content
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Gets the simplified name of a task.
     *
     * For example, if the task starts with a asterisk (*), it is removed.
     *
     * @return Simplified name of a task
     */
    public String getContentForDisplay()
    {
        if (_contentForDisplay != null)
        {
            return _contentForDisplay;
        }

        _contentForDisplay = _content.trim();

        // strip leading *
        if (_contentForDisplay.startsWith("*"))
        {
            _contentForDisplay = _contentForDisplay.substring(1).trim();
        }

        // look for and strip formatting
        if (_contentForDisplay.contains("%("))
        {
            //
            // Formatting:
            //  %(b)bold% %(bold)bold%
            //  %(i)italics%
            //  %(hl)highlight%
            //  %(ul)underline%
            //  %(bi)italics bold% %(ib)italics bold%
            //  %(u)underline%
            //  %nothing%
            //  %()nothing%
            //

            // use a regular expression to remove formatting tokens
            Pattern pattern = Pattern.compile("%\\([a-zA-Z]+\\)([^%]+)%");
            Matcher matcher = pattern.matcher(_contentForDisplay);
            _contentForDisplay = matcher.replaceAll("$1");
        }

        _contentForDisplay = _contentForDisplay.trim();

        return _contentForDisplay;
    }

    /**
     * Determines if the task is checked.
     *
     * @return True if the task is checked
     */
    public boolean isChecked()
    {
        return _checked;
    }

    /**
     * Sets the checked state.
     *
     * @param isChecked True if the task is checked
     */
    public void setCheckedState(final boolean isChecked)
    {
        _checked = isChecked;
    }

    @Override
    public String getText()
    {
        return _content;
    }

    /**
     * Determines if the task should show a check box.
     *
     * @return True if the task should show a check box
     */
    public boolean showCheckBox()
    {
        return !_content.startsWith("*");
    }

    /**
     * Determines if the task has a date specified.
     *
     * @return True if the task has a date specified
     */
    public boolean hasDateSpecified()
    {
        return _dueDateString != null && !_dueDateString.equals("");
    }

}
