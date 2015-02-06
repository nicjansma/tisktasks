package com.nicjansma.tisktasks.views;

import android.app.Activity;
import android.view.LayoutInflater;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TodoistProject;

/**
 * Project List adapter.
 */
public final class ProjectListAdapter
    extends TodoistListAdapterBase<TodoistProject, ProjectListRow>
{
    /**
     * Constructor.
     *
     * @param context Application context
     * @param todoistManager Todoist Manager
     */
    public ProjectListAdapter(final Activity context, final ITodoistBaseObjectManager<TodoistProject> todoistManager)
    {
        super(context, todoistManager);
    }

    @Override
    protected ProjectListRow newRowInstance(final Activity context, final LayoutInflater inflater)
    {
        return new ProjectListRow(context, inflater, R.layout.project_list_row);
    }
}