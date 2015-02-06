package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

import com.nicjansma.library.net.CacheableJsonObjectArrayBase;

/**
 * Todoist query results.
 */
public final class TodoistQueryResults extends CacheableJsonObjectArrayBase<TodoistQueryResult>
{
    /**
     * Task manager of the query.
     */
    private ITaskManager _taskManager;

    /**
     * Default constructor.
     */
    public TodoistQueryResults()
    {
        super();
    }

    /**
     * Constructor with an array of results.
     *
     * @param results Query results
     */
    public TodoistQueryResults(final ArrayList<TodoistQueryResult> results)
    {
        super(results);
    }

    /**
     * Appends queries to the results.
     */
    private void appendQueries()
    {
        for (int i = 0; i < getArray().size(); i++)
        {
            _taskManager.appendArray(getArray().get(i).getTaskManager().getArray());
        }
    }

    /**
     * Gets the query's task manager.
     *
     * @return Task manager.
     */
    public ITaskManager getTaskManager()
    {
        if (_taskManager == null)
        {
            _taskManager = new TaskManager();
            appendQueries();
        }

        return _taskManager;
    }

    @Override
    protected Class<TodoistQueryResult> getObjectClass()
    {
        return TodoistQueryResult.class;
    }

    /**
     * Imports new queries.
     *
     * @param array New results to import.
     */
    public void importNewQueries(final ArrayList<TodoistQueryResult> array)
    {
        setArray(array);
        _taskManager.clearArray();
        appendQueries();
    }
}
