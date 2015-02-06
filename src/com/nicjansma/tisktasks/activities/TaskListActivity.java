package com.nicjansma.tisktasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultArray;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.api.TodoistApiResultSimple;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TaskManager;
import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.views.TaskListAdapter;
import com.nicjansma.tisktasks.views.TaskListRow;


/**
 * Task List activity.
 */
public final class TaskListActivity
    extends TodoistListActivityBase<TodoistItem>
{
    //
    // Constants
    //
    /**
     * Activity intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.TASKLIST";

    /**
     * Activity bundle key for Project ID.
     */
    private static final String BUNDLE_PROJECTID_KEY = "projectID";

    //
    // Privates
    //
    /**
     * Current project.
     */
    private TodoistProject _project;

    @Override
    public void onCreateInternalPre(final Bundle savedInstanceState)
    {
        // get the intended project
        Bundle bundle = this.getIntent().getExtras();
        long projectId = bundle.getLong(BUNDLE_PROJECTID_KEY);
        _project = ServiceLocator.projectManager().get(projectId);
    }

    @Override
    protected void startClickActivity(final View view)
    {
        TaskListRow row = (TaskListRow) view;
        startClickActivity(row.getObj());
    }

    @Override
    protected void startClickActivity(final TodoistItem obj)
    {
        // NOP
    }

    @Override
    protected void createListAdapter()
    {
        setListAdapter(new TaskListAdapter(getContext(), getObjectManager(), true));
    }

    @Override
    protected void loadFromTodoist()
    {
        showProgressDialog(R.string.loading_tasks, new Thread() {
            @Override
            public void run()
            {
                // get uncompleted items
                TodoistApiResultArray<TodoistItem> result = null;
                try
                {
                    result = ServiceLocator.todoistApi().getUncompletedItems(_project.getId());
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }

                checkTodoistApiResult(result, "getUncompletedItems");

                if (result != null && result.successful())
                {
                    getObjectManager().importArray(result.getArray());
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void afterIndentObj(final TodoistItem task)
    {
        updateTask(task, null, null, null, task.getIndent(), null, null);
    }

    @Override
    protected void addDialog(final TodoistItem nearTask, final Boolean addAbove)
    {
        TodoistItem nearTaskFinal = nearTask;

        // if not specified, take the last one
        if (nearTask == null)
        {
            nearTaskFinal = getObjectManager().getLast();
        }

        startActivityForResult(TaskAddActivity.getIntent(_project, nearTaskFinal, addAbove),
                               TodoistListActivityBase.START_ACTIVITY_ADD);
    }

    /**
     * Updates a task.
     *
     * @param task Task to update
     * @param content Content (task text)
     * @param dateString Date string
     * @param priority Task priority
     * @param indent Task indent (normal = 1)
     * @param itemOrder Item order
     * @param collapsed Task is collapsed
     */
    private void updateTask(
        final TodoistItem task,
        final String content,
        final String dateString,
        final Integer priority,
        final Integer indent,
        final Integer itemOrder,
        final Integer collapsed)
    {
        showProgressDialog(R.string.updating_task, new Thread() {
            @Override
            public void run()
            {
                // update the task
                TodoistApiResultObject<TodoistItem> result =
                    ServiceLocator.todoistApi().updateItem(
                       task.getId(),
                       content,
                       dateString,
                       priority,
                       indent,
                       itemOrder,
                       collapsed);

                checkTodoistApiResult(result, "updateItem");

                if (result.successful())
                {
                    getObjectManager().update(result.getObject());
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void deleteObj(final TodoistItem task)
    {
        showProgressDialog(R.string.deleting_task, new Thread() {
            @Override
            public void run()
            {
                // delete task
                TodoistApiResultSimple result = ServiceLocator.todoistApi().deleteItem(task.getId());

                checkTodoistApiResult(result, "deleteItem");

                if (result.successful())
                {
                    getObjectManager().delete(task);
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void editDialog(final TodoistItem task)
    {
        startActivityForResult(TaskEditActivity.getIntent(task), TodoistListActivityBase.START_ACTIVITY_EDIT);
    }

    /**
     * Gets an Intent for this Activity.
     *
     * @param project Todoist project to start with
     *
     * @return Intent to use
     */
    public static Intent getIntent(final TodoistProject project)
    {
        // create an intent
        Intent intent = new Intent(INTENT);

        // store the specified minifig
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_PROJECTID_KEY, project.getId());

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected int getActivityLayout()
    {
        return R.layout.task_list;
    }

    @Override
    protected ITodoistBaseObjectManager<TodoistItem> getObjectManager()
    {
        if (_project == null)
        {
            return null;
        }

        return _project.getTaskManager();
    }

    @Override
    protected int getStringAddObj()
    {
        return R.string.add_task;
    }

    @Override
    protected int getStringDeleteObj()
    {
        return R.string.delete_task;
    }

    @Override
    protected int getStringDeleteObjConfirmation()
    {
        return R.string.delete_task_confirmation;
    }

    @Override
    protected int getStringNoItems()
    {
        return R.string.no_tasks;
    }

    @Override
    protected int getStringLoadingItems()
    {
        return R.string.loading_tasks;
    }

    @Override
    protected int getMenu()
    {
        return R.menu.tasklist;
    }

    @Override
    protected void notifyListAdapterChanged()
    {
        // get the list adapter and notify it that contents have changed
        TaskListAdapter listAdapter = (TaskListAdapter) getListAdapter();

        if (listAdapter != null)
        {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void loadInitial()
    {
        if (_project != null)
        {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setTitle(_project.getName());
            }
        }

        loadFromTodoist();
    }

    @Override
    protected int getStringAddObjMessage()
    {
        return R.string.add_task_message;
    }

    @Override
    protected void addObj(final TodoistItem nearObj, final String newName, final Boolean addAbove)
    {
        // not used, using addDialog instead
    }

    @Override
    public void checkboxClick(final TodoistItem task)
    {
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        itemIds.add(task.getId());

        if (task.hasChildren())
        {
            itemIds.addAll(task.getAllChildrenIds(null));
        }

        if (!task.isChecked())
        {
            showProgressDialog(R.string.completing_task, new Thread() {
                @Override
                public void run()
                {
                    if (task.isRecurring())
                    {
                        // set to next due date
                        TodoistApiResultArray<TodoistItem> result =
                            ServiceLocator.todoistApi().updateRecurringDate(itemIds);

                        checkTodoistApiResult(result, "updateRecurringDate");

                        if (result.successful())
                        {
                            TaskManager taskManager = (TaskManager) getObjectManager();
                            taskManager.update(result.getArray());
                        }
                    }
                    else
                    {
                        boolean moveToHistory = !task.isChild();

                        // complete task
                        TodoistApiResultSimple result =
                            ServiceLocator.todoistApi().completeItems(itemIds, moveToHistory);

                        checkTodoistApiResult(result, "completeItems");

                        if (result.successful())
                        {
                            TaskManager taskManager = (TaskManager) getObjectManager();

                            if (moveToHistory)
                            {
                                taskManager.delete(task);
                            }
                            else
                            {
                                taskManager.completeItems(itemIds);
                            }

                            // either way, the count should be updated
                            _project.decrementCacheCount(itemIds.size());

                            ServiceLocator.projectManager().saveToCache();
                        }
                    }

                    notifyLoadingHandlerRefresh();
                };
            });
        }
        else
        {
            showProgressDialog(R.string.uncompleting_task, new Thread() {
                @Override
                public void run()
                {
                    // un-complete task
                    TodoistApiResultSimple result = ServiceLocator.todoistApi().uncompleteItems(itemIds);

                    checkTodoistApiResult(result, "uncompleteItems");

                    if (result.successful())
                    {
                        TaskManager taskManager = (TaskManager) getObjectManager();
                        taskManager.uncompleteItems(itemIds);

                        // need to re-send object orders as un-complete
                        // for some reason sends them to the end of the parent
                        ServiceLocator.todoistApi().updateOrders(_project.getId(), getObjectManager().getOrders());

                        _project.incrementCacheCount(itemIds.size());

                        ServiceLocator.projectManager().saveToCache();
                    }

                    notifyLoadingHandlerRefresh();
                };
            });
        }
    }

    @Override
    protected void loadAdditionalLayout()
    {
        // nothing
        return;
    }

    @Override
    protected boolean showAddContextOptions()
    {
        return true;
    }

    @Override
    protected void onResumeInternal()
    {
        // NOP
    }

    @Override
    protected String getStringScreenName()
    {
        return "TaskList";
    }

    @Override
    protected boolean onOptionsItemSelectedInternal(final MenuItem item)
    {
        // No additional options
        return false;
    }
}
