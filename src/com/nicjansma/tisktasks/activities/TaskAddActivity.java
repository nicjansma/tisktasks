package com.nicjansma.tisktasks.activities;

import android.content.Intent;
import android.os.Bundle;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistPriority;
import com.nicjansma.tisktasks.models.TodoistProject;

/**
 * Task Add activity.
 */
public final class TaskAddActivity
    extends TaskAddEditActivityBase<TodoistProject>
{
    //
    // Constants
    //
    /**
     * Activity intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.TASKADD";

    /**
     * Activity bundle key for Object ID.
     */
    public static final String BUNDLE_ID_NEAR_KEY = "near";

    /**
     * Activity bundle key for Object ID.
     */
    public static final String BUNDLE_ID_BEFORE = "before";

    /**
     * Task to add before or after.
     */
    private TodoistItem _nearTask;

    /**
     * Add task before the near task instead of before.
     */
    private boolean _addBefore;

    @Override
    public void onCreateInternalTaskAddEditPost(final Bundle savedInstanceState)
    {
        // decode the bundle
        Bundle bundle = this.getIntent().getExtras();

        long nearId = bundle.getLong(BUNDLE_ID_NEAR_KEY);

        if (nearId != 0)
        {
            _nearTask = getObj().getTaskManager().get(nearId);
        }

        _addBefore = bundle.getBoolean(BUNDLE_ID_BEFORE);
    }

    @Override
    protected void onCreateInternalTaskAddEditPre(final Bundle savedInstance)
    {
        // NOP
    }

    /**
     * Gets an Intent for this Activity.
     *
     * @param project Todoist project to start with
     * @param nearTask Task to add before or after
     * @param addBefore Add new task before the near task instead of after
     *
     * @return Intent to use
     */
    public static Intent getIntent(
            final TodoistProject project,
            final TodoistItem nearTask,
            final boolean addBefore)
    {
        // create an intent
        Intent intent = new Intent(INTENT);

        // store the specified minifig
        Bundle bundle = new Bundle();
        bundle.putLong(PopupDialogBase.BUNDLE_ID_KEY, project.getId());
        bundle.putLong(BUNDLE_ID_NEAR_KEY, nearTask != null ? nearTask.getId() : 0);
        bundle.putBoolean(BUNDLE_ID_BEFORE, addBefore);

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    public boolean makeChanges()
    {
        TodoistProject project = getObj();

        // determine which date
        String date = null;
        if (dateIsChecked())
        {
            date = getSelectedDate();
        }

        // add task
        TodoistApiResultObject<TodoistItem> result =
            ServiceLocator.todoistApi().addItem(project.getId(),
                                                getNameField(),
                                                date,
                                                TodoistPriority.DEFAULT);

        if (result.successful())
        {
            // addItem doesn't allow you to specify the indent.  Send it now
            if (_nearTask != null)
            {
                TodoistItem task = result.getObject();

                int indent = 0;
                if (_addBefore)
                {
                    indent = _nearTask.getIndent();
                }
                else
                {
                    indent = _nearTask.hasChildren() ? _nearTask.getIndent() + 1 : _nearTask.getIndent();
                }

                task.setIndent(indent);

                result = ServiceLocator.todoistApi().updateItem(task.getId(),
                                                                null,
                                                                null,
                                                                null,
                                                                task.getIndent(),
                                                                null,
                                                                null);
            }

            // add task to our DB
            project.getTaskManager().add(result.getObject(), _nearTask, _addBefore);

            // send updated order to Todoist
            project.getTaskManager().updateOrders();
            ServiceLocator.todoistApi().updateOrders(project.getId(), project.getTaskManager().getOrders());

            project.incrementCacheCount(1);

            ServiceLocator.projectManager().saveToCache();
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
        return R.string.adding_task;
    }

    @Override
    protected boolean hasChangesToSave()
    {
        return true;
    }

    @Override
    protected TodoistProject loadObj(final long id)
    {
        return ServiceLocator.projectManager().get(id);
    }

    @Override
    protected String getDefaultText()
    {
        return "";
    }

    @Override
    protected int getSaveButtonString()
    {
        return R.string.add;
    }

    @Override
    protected String getStringScreenName()
    {
        return "TaskAdd";
    }
}
