package com.nicjansma.tisktasks.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.models.TodoistProject;

/**
 * A row in the Project List
 */
/**
 * @author Nic
 *
 */
@SuppressLint("ViewConstructor")
public final class ProjectListRow
    extends TodoistListRowBase<TodoistProject>
{
    //
    // UI
    //
    /**
     * Task count.
     */
    private final TextView _taskCount;

    /**
     * Project name.
     */
    private final TextView _projectName;

    /**
     * Project list constructor.
     *
     * @param context ListAdapter context
     * @param inflater Cached LayoutInflater
     * @param resLayout Resource layout ID
     */
    public ProjectListRow(final Context context, final LayoutInflater inflater, final int resLayout)
    {
        super(context, inflater, resLayout, true);

        _taskCount = (TextView) getView().findViewById(R.id.project_task_count);
        _projectName = (TextView) getView().findViewById(R.id.project_name);

        _taskCount.setOnClickListener(getOnClickListener());
        _projectName.setOnClickListener(getOnClickListener());

        _taskCount.setOnLongClickListener(getOnLongClickListener());
        _projectName.setOnLongClickListener(getOnLongClickListener());
    }

    @Override
    public void updateInternal(final TodoistProject project)
    {
        //
        // Task Count
        //
        _taskCount.setText(String.valueOf(project.getCacheCount()));
        _taskCount.setBackgroundColor(project.getColor());

        //
        // name
        //
        _projectName.setText(project.getName());
    }

    @Override
    public String getText()
    {
        return getObj().getName();
    }
}
