package com.nicjansma.tisktasks.views;

import android.app.Activity;
import android.view.LayoutInflater;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TodoistItem;

/**
 * Task List adapter.
 */
public final class TaskListAdapter
    extends TodoistListAdapterBase<TodoistItem, TaskListRow>
{
    //
    // Privates
    //
    /**
     * Whether or not to indent tasks.
     */
    private final boolean _indent;

    /**
     * Constructor.
     *
     * @param context Activity context
     * @param todoistManager Manager to use
     * @param indent Indent tasks
     */
    public TaskListAdapter(
        final Activity context,
        final ITodoistBaseObjectManager<TodoistItem> todoistManager,
        final boolean indent)
    {
        super(context, todoistManager);
        _indent = indent;
    }

    @Override
    protected TaskListRow newRowInstance(final Activity context, final LayoutInflater inflater)
    {
        return new TaskListRow(context, inflater, R.layout.task_list_row, _indent);
    }
}
