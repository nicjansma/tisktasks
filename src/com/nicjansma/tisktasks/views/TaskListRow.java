package com.nicjansma.tisktasks.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.models.TodoistDueDate;
import com.nicjansma.tisktasks.models.TodoistItem;

/**
 * Task List row.
 */
@SuppressLint("ViewConstructor")
public final class TaskListRow
    extends TodoistListRowBase<TodoistItem>
{
    //
    // UI
    //
    /**
     * Name text view.
     */
    private final TextView _taskName;

    /**
     * Date text view.
     */
    private final TextView _taskDate;

    /**
     * Recurring image.
     */
    private final ImageView _taskRecurringImage;

    /**
     * Completed text box.
     */
    private final CheckBox _taskCompleteCheckBox;

    /**
     * Row constructor.
     *
     * @param context Activity context
     * @param inflater Cached Layout Inflater
     * @param resLayout Layout Resource ID
     * @param indent Whether to indent tasks
     */
    public TaskListRow(
        final Context context,
        final LayoutInflater inflater,
        final int resLayout,
        final boolean indent)
    {
        super(context, inflater, resLayout, indent);

        _taskCompleteCheckBox = (CheckBox) getView().findViewById(R.id.task_complete);
        _taskCompleteCheckBox.setOnClickListener(getCheckboxOnClickListener());

        _taskName = (TextView) getView().findViewById(R.id.task_name);
        _taskDate = (TextView) getView().findViewById(R.id.task_date);
        _taskRecurringImage = (ImageView) getView().findViewById(R.id.task_recurring_image);
    }

    @Override
    public void updateInternal(final TodoistItem task)
    {
        //
        // checkbox
        //
        if (task.showCheckBox())
        {
            _taskCompleteCheckBox.setVisibility(View.VISIBLE);
            _taskCompleteCheckBox.setChecked(task.isChecked());
        }
        else
        {
            _taskCompleteCheckBox.setVisibility(View.GONE);
        }

        //
        // name
        //
        _taskName.setText(task.getContentForDisplay());
        _taskName.setTextColor(task.getColor());

        //
        // recurring image
        //
        _taskRecurringImage.setVisibility(task.isRecurring() ? View.VISIBLE : View.GONE);

        //
        // date
        //
        TodoistDueDate dueDate = task.getFormattedDueDate();
        if (dueDate.getString().equals(""))
        {
            _taskDate.setVisibility(View.GONE);
        }
        else
        {
            _taskDate.setVisibility(View.VISIBLE);
            _taskDate.setText(dueDate.getString());

            // box color
            if (dueDate.getColor() != TodoistDueDate.COLOR_NONE)
            {
                _taskDate.setBackgroundColor(dueDate.getColor());
            }
            else
            {
                _taskDate.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public String getText()
    {
        return getObj().getContent();
    }
}
