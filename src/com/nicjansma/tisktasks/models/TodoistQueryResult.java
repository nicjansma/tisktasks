package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.nicjansma.library.net.CacheableJsonObjectBase;
import com.nicjansma.library.net.JsonUtils;

/**
 * Todoist query results.
 */
public final class TodoistQueryResult
    extends CacheableJsonObjectBase
{
    //
    // Privates
    //
    /**
     * Query type.
     */
    private TodoistQueryType _queryType;

    /**
     * Query type string.
     */
    private String _queryTypeString;

    /**
     * Query string.
     */
    private String _queryString;

    /**
     * Query task manager.
     */
    private ITaskManager _taskManager;

    /**
     * Constructor.
     */
    public TodoistQueryResult()
    {
        super();
    }

    @Override
    public void initializeInternal(final JSONObject json)
    {
        _queryTypeString = json.optString("type");
        _queryType = TodoistQueryType.get(_queryTypeString);
        _queryString = json.optString("query");

        _taskManager = new TaskManager();

        String jsonTaskString = json.optString("data");

        // convert to array, import into task manager
        if (jsonTaskString != null && jsonTaskString.length() != 0)
        {
            ArrayList<TodoistItem> tasks = JsonUtils.convertToArrayList(jsonTaskString, TodoistItem.class);
            if (tasks != null)
            {
                _taskManager.importArray(tasks);
            }
        }
    }

    @Override
    protected JSONObject toJsonInternal(final JSONObject json)
    {
        try
        {
            json.put("type", _queryTypeString);
            json.put("query", _queryString);
            json.put("data", JsonUtils.convertObjectArrayToJsonArray(_taskManager.getArray()));
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Gets the query string.
     *
     * @return Query string
     */
    public String getQueryString()
    {
        return _queryString;
    }

    /**
     * Gets the task manager.
     *
     * @return Task manager
     */
    public ITaskManager getTaskManager()
    {
        return _taskManager;
    }

    /**
     * Gets the query type.
     *
     * @return Query type
     */
    public TodoistQueryType getQueryType()
    {
        return _queryType;
    }
}
