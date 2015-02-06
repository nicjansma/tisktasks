package com.nicjansma.tisktasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultArray;
import com.nicjansma.tisktasks.api.TodoistApiResultSimple;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TaskManager;
import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistQueryResult;
import com.nicjansma.tisktasks.models.TodoistQueryResults;
import com.nicjansma.tisktasks.views.TaskListAdapter;

/**
 * Main activity.
 */
public final class MainActivity
    extends TodoistListActivityBase<TodoistItem>
{
    //
    // Constants
    //
    /**
     * Activity intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.MAIN";

    //
    // UI
    //
    /**
     * Current query results.
     */
    private TodoistQueryResults _queryResults;

    @Override
    public void onCreateInternalPre(final Bundle savedInstanceState)
    {
        //
        // ensure the user is logged in
        //
        ServiceLocator.userManager().ensureLoggedIn(this);
    }

    @Override
    protected void loadAdditionalLayout()
    {
    }

    @Override
    protected void loadInitial()
    {
        // load initial query results
        _queryResults = ServiceLocator.cache().getStartPageQueries();
        if (_queryResults == null)
        {
            loadFromTodoist();
        }
        else
        {
            refreshList();
        }
    }

    @Override
    protected void onResumeInternal()
    {
        // NOP
    }

    @Override
    protected void addObj(final TodoistItem nearObj, final String newName, final Boolean addBefore)
    {
        // can't add items
    }

    @Override
    protected void createListAdapter()
    {
        setListAdapter(new TaskListAdapter(getContext(), getObjectManager(), false));
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

    @Override
    protected int getActivityLayout()
    {
        return R.layout.main;
    }

    @Override
    protected int getMenu()
    {
        return R.menu.main;
    }

    @Override
    protected ITodoistBaseObjectManager<TodoistItem> getObjectManager()
    {
        if (_queryResults == null)
        {
            return null;
        }

        return _queryResults.getTaskManager();
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
        return R.string.no_tasks_today;
    }

    @Override
    protected int getStringLoadingItems()
    {
        return R.string.loading_today;
    }

    @Override
    protected void loadFromTodoist()
    {
        showProgressDialog(R.string.loading_start_tasks, new Thread() {
            @Override
            public void run()
            {
                ArrayList<String> queries = ServiceLocator.userManager().getCurrentUser().getStartPageQueries();

                TodoistApiResultArray<TodoistQueryResult> result = null;
                try
                {
                    result = ServiceLocator.todoistApi().query(queries);
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }

                checkTodoistApiResult(result, "query");

                if (result != null && result.successful())
                {
                    // If we haven't seen results before, create a new TodoistQueryResults.
                    // Otherwise, don't kill the _queryResults object as he has the ITaskManager
                    // that gets passed to the ListAdapter.  Have him import results instead.
                    if (_queryResults == null)
                    {
                        _queryResults = new TodoistQueryResults(result.getArray());
                    }
                    else
                    {
                        _queryResults.importNewQueries(result.getArray());
                    }

                    // save these results to the cache
                    ServiceLocator.cache().setStartPageQueries(_queryResults);
                }

                notifyLoadingHandlerRefresh();
            }
        });
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
    protected void startClickActivity(final View view)
    {
        // clicking doesn't do anything
    }

    @Override
    protected void startClickActivity(final TodoistItem obj)
    {
        // clicking doesn't do anything
    }

    @Override
    protected int getStringAddObjMessage()
    {
        return R.string.add_task_message;
    }

    @Override
    protected void addDialog(final TodoistItem nearTask, final Boolean addAbove)
    {
        // can't add from here
    }

    @Override
    public void checkboxClick(final TodoistItem task)
    {
        final ArrayList<Long> itemIds = new ArrayList<Long>();
        itemIds.add(task.getId());

        // if we have children, check off all of them
        if (task.hasChildren())
        {
            itemIds.addAll(task.getAllChildrenIds(null));
        }

        // if it's not yet checked, check it
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
                            if (moveToHistory)
                            {
                                getObjectManager().delete(task);
                            }
                            else
                            {
                                TaskManager taskManager = (TaskManager) getObjectManager();
                                taskManager.completeItems(itemIds);
                            }
                        }
                    }

                    notifyLoadingHandlerRefresh();
                };
            });
        }
    }

    @Override
    protected boolean showAddContextOptions()
    {
        // don't show Add context menu options
        return false;
    }

    @Override
    protected void afterIndentObj(final TodoistItem obj)
    {
        // NOP
    }

    @Override
    protected String getStringScreenName()
    {
        return "Main";
    }

    @Override
    public boolean onOptionsItemSelectedInternal(final MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.projects:
                Intent intent = new Intent(ProjectListActivity.INTENT);
                startActivity(intent);
                break;

            default:
                break;
        }

        return true;
    }
}