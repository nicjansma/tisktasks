package com.nicjansma.tisktasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.models.ITaskManager;
import com.nicjansma.tisktasks.models.TodoistItem;

/**
 * Task Edit activity.
 */
public final class TaskEditActivity
    extends TaskAddEditActivityBase<TodoistItem>
{
    //
    // Constants
    //
    /**
     * Class tag (for debugging).
     */
    private static final String TAG = TaskEditActivity.class.getSimpleName();

    /**
     * Activity intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.TASKEDIT";

    /**
     * Activity bundle key for Project ID.
     */
    private static final String BUNDLE_PROJECTID_KEY = "projectID";

    //
    // Privates
    //
    /**
     * Current Project ID.
     */
    private long _projectId = 0;

    /**
     * Project Task Manager.
     */
    private ITaskManager _taskManager;

    @Override
    public void onCreateInternalTaskAddEditPre(final Bundle savedInstanceState)
    {
        // decode the bundle first
        Bundle bundle = this.getIntent().getExtras();
        _projectId = bundle.getLong(BUNDLE_PROJECTID_KEY);
    }

    @Override
    public void onCreateInternalTaskAddEditPost(final Bundle savedInstanceState)
    {
        // object is now available
        if (getObj().hasDateSpecified())
        {
            getDateEditText().setText(getObj().getDueDateUserString());

            // month is base 0
            getDatePicker().updateDate(getObj().getDueDate().year().get(),
                                       getObj().getDueDate().monthOfYear().get() - 1,
                                       getObj().getDueDate().dayOfMonth().get());

            // check the date box
            getSetDateCheckBox().setChecked(true);

            if (getObj().getDueDateUserString().length() > 0)
            {
                useDateText();
            }
            else
            {
                useDatePicker();
            }
        }
    }

    /**
     * Gets an Intent for this Activity.
     *
     * @param task Todoist task to start with
     *
     * @return Intent to use
     */
    public static Intent getIntent(final TodoistItem task)
    {
        // create an intent
        Intent intent = new Intent(INTENT);

        // store the specified minifig
        Bundle bundle = new Bundle();
        bundle.putLong(PopupDialogBase.BUNDLE_ID_KEY, task.getId());
        bundle.putLong(BUNDLE_PROJECTID_KEY, task.getProjectId());

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    public boolean makeChanges()
    {
        // determine which date
        String date = null;
        if (dateIsChecked())
        {
            date = getSelectedDate();
        }
        else
        {
            if (getObj().hasDateSpecified())
            {
                date = "";
            }
        }

        // add task
        TodoistApiResultObject<TodoistItem> result =
            ServiceLocator.todoistApi().updateItem(getObj().getId(),
                                                getNameField(),
                                                date,
                                                null,
                                                null,
                                                null,
                                                null);

        if (result.successful())
        {
            // update task in our DB
            _taskManager.update(result.getObject());
        }

        return result.successful();
    }

    @Override
    protected int getContentViewId()
    {
        return R.layout.task_add_edit;
    }

    @Override
    protected int getUpdatingString()
    {
        return R.string.updating_task;
    }

    @Override
    protected boolean hasChangesToSave()
    {
        if (!dateIsChecked())
        {
            // if the date isn't checked, return true if there used to be a date specified
            return getObj().hasDateSpecified();
        }
        else
        {
            // if the date is checked, return true if the date wasn't specified, or it has changed
            return !getObj().hasDateSpecified()
                 || !getSelectedDate().equals(getObj().getDueDateUserString());
        }
    }

    @Override
    protected TodoistItem loadObj(final long id)
    {
        // get project manager
        if (ServiceLocator.projectManager().get(_projectId) != null)
        {
            _taskManager = ServiceLocator.projectManager().get(_projectId).getTaskManager();
        }
        else
        {
            // project manager might not exist yet if we're editing from the start page.
            // try to load the task from the start page queries.
            _taskManager = ServiceLocator.cache().getStartPageQueries().getTaskManager();
        }

        return _taskManager.get(id);
    }

    @Override
    protected String getDefaultText()
    {
        if (getObj() == null)
        {
            Log.e(TAG, "getObj() is NULL");
            return "";
        }

        return getObj().getContent();
    }

    @Override
    protected int getSaveButtonString()
    {
        return R.string.save;
    }

    @Override
    protected String getStringScreenName()
    {
        return "TaskEdit";
    }

}
