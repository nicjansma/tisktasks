package com.nicjansma.tisktasks.models;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Todoist project.
 */
public final class TodoistProject
    extends TodoistObjectBase
{
    //
    // Privates
    //
    /**
     * User ID.
     */
    private long _userId;

    /**
     * Project name.
     */
    private String _name;

    /**
     * Project color.
     */
    private String _color;

    /**
     * Number of cached (uncompleted) items.
     */
    private int _cacheCount;

    /**
     * Project task manager.
     */
    private ITaskManager _taskManager;

    /**
     * Constructor.
     */
    public TodoistProject()
    {
        super();
    }

    /**
     * Constructor.
     *
     * @param projectId Project ID
     * @param userId User ID
     * @param name Project name
     * @param color Project color
     * @param indent Project indent
     * @param cacheCount Number of un-completed tasks
     * @param collapsed Project is collapsed
     * @param itemOrder Project order
     */
    public TodoistProject(
        final long projectId,
        final long userId,
        final String name,
        final String color,
        final int indent,
        final int cacheCount,
        final Boolean collapsed,
        final int itemOrder)
    {
        super(projectId, collapsed, indent, itemOrder);

        _userId = userId;
        _name = name;
        _color = color;
        _cacheCount = cacheCount;
    }

    @Override
    protected void initializeInternalTodoist(final JSONObject json)
    {
        _userId = json.optLong("user_id");
        _name = json.optString("name", "Unknown");
        _color = json.optString("color", TodoistColor.DEFAULT_COLOR);
        _cacheCount = json.optInt("cache_count", 0);
    }

    @Override
    protected JSONObject toJsonInternalTodoist(final JSONObject json)
    {
        try
        {
            json.put("user_id", _userId);
            json.put("name", _name);
            json.put("color", _color);
            json.put("cache_count", _cacheCount);
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }

        return json;
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
     * Gets the name of the project.
     *
     * @return Name of the project
     */
    public String getName()
    {
        // strip leading *
        if (_name.startsWith("*"))
        {
            _name = _name.substring(1).trim();
        }

        return _name;
    }

    /**
     * Gets the color of the project.
     *
     * @return Color of the project
     */
    private String getFixedColorString()
    {
        if (_color.startsWith("#"))
        {
            return _color;
        }
        else
        {
            // might be a color index
            Integer colorInt = 0;
            try
            {
                colorInt = Integer.parseInt(_color);
            }
            catch (final NumberFormatException e)
            {
                colorInt = 0;
            }

            return TodoistColor.getColorStringFromIndex(colorInt);
        }
    }

    /**
     * Gets the color string of the project.
     *
     * May be a hex color (#fff) or a color index (0-15).
     *
     * @return Color string
     */
    public String getColorString()
    {
        return getFixedColorString();
    }

    /**
     * Gets the color index.
     *
     * @return Color index
     */
    public int getColorIndex()
    {
        if (_color.startsWith("#"))
        {
            return TodoistColor.getColorIndexFromString(_color);
        }
        else
        {
            // might be a color index
            Integer colorInt = 0;
            try
            {
                colorInt = Integer.parseInt(_color);
            }
            catch (final NumberFormatException e)
            {
                colorInt = 0;
            }

            return colorInt;
        }
    }

    /**
     * Gets the color of the project.
     *
     * @return Color of the project
     */
    public int getColor()
    {
        return Color.parseColor(getFixedColorString());
    }

    /**
     * Gets the number of un-completed tasks.
     *
     * @return Number of un-completed tasks
     */
    public int getCacheCount()
    {
        return _cacheCount;
    }

    /**
     * Increases the number of un-completed tasks.
     *
     * @param number Increase amount
     */
    public void incrementCacheCount(final int number)
    {
        _cacheCount += number;
    }

    /**
     * Decreases the number of un-completed tasks.
     *
     * @param number Decrease amount
     */
    public void decrementCacheCount(final int number)
    {
        _cacheCount -= number;
        if (_cacheCount < 0)
        {
            _cacheCount = 0;
        }
    }

    /**
     * Gets the Project's Task Manager.
     *
     * @return Task Manager
     */
    public ITaskManager getTaskManager()
    {
        if (_taskManager == null)
        {
            _taskManager = new TaskManager();
        }

        return _taskManager;
    }

    /**
     * Sets the Task Manager.
     *
     * @param taskManager Task Manager
     */
    public void setTaskManager(final ITaskManager taskManager)
    {
        _taskManager = taskManager;
    }

    @Override
    public String getText()
    {
        return _name;
    }
}
