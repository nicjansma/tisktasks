package com.nicjansma.tisktasks.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.ITodoistApi;
import com.nicjansma.tisktasks.api.TodoistApiResultArray;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.api.TodoistApiResultSimple;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.ProjectManager;
import com.nicjansma.tisktasks.models.TodoistColor;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.models.TodoistProjects;
import com.nicjansma.tisktasks.views.ProjectListAdapter;
import com.nicjansma.tisktasks.views.ProjectListRow;

/**
 * Project List activity.
 */
public final class ProjectListActivity
    extends TodoistListActivityBase<TodoistProject>
{
    //
    // Constants
    //
    /**
     * Intent string.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.PROJECTLIST";

    /**
     * Type of activity started.
     */
    private static final int START_ACTIVITY_TASK_LIST = TodoistListActivityBase.START_ACTIVITY_MAX + 1;

    @Override
    protected void onResumeInternal()
    {
        // make sure to refresh the projects list
        notifyLoadingHandlerRefresh();
    }

    @Override
    protected void startClickActivity(final View view)
    {
        ProjectListRow row = (ProjectListRow) view;
        startClickActivity(row.getObj());
    }

    @Override
    protected void startClickActivity(final TodoistProject project)
    {
        startActivityForResult(TaskListActivity.getIntent(project), START_ACTIVITY_TASK_LIST);
    }

    @Override
    protected void createListAdapter()
    {
        setListAdapter(new ProjectListAdapter(getContext(), getObjectManager()));
    }

    @Override
    protected void loadFromTodoist()
    {
        showProgressDialog(R.string.loading_projects, new Thread() {
            @Override
            public void run()
            {
                TodoistApiResultArray<TodoistProject> result = ServiceLocator.todoistApi().getProjects();

                checkTodoistApiResult(result, "getProjects");

                if (result.successful())
                {
                    getProjectManager().importArray(result.getArray());
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void afterIndentObj(final TodoistProject project)
    {
        updateProject(project, null, null, project.getIndent());
    }

    @Override
    protected void addObj(final TodoistProject nearProject, final String newProjectName, final Boolean addBefore)
    {
        // cache interfaces
        final ITodoistApi todoist = ServiceLocator.todoistApi();

        showProgressDialog(R.string.adding_project, new Thread() {
            @Override
            public void run()
            {
                // get the values to add
                int colorIndex = (nearProject != null) ? nearProject.getColorIndex() : TodoistColor.DEFAULT_COLOR_INDEX;
                int indent = (nearProject != null) ? nearProject.getIndent() : 1;
                int order = (nearProject != null) ? nearProject.getItemOrder() : 0;

                // add project
                TodoistApiResultObject<TodoistProject> result =
                    todoist.addProject(newProjectName,
                                       colorIndex,
                                       indent,
                                       order);

                checkTodoistApiResult(result, "addProject");

                if (result.successful())
                {
                    // add project to our DB
                    getProjectManager().add(result.getObject(), nearProject, addBefore);

                    // send updated order to Todoist
                    getProjectManager().updateOrders();
                    todoist.updateProjectOrders(getProjectManager().getOrders());
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    /**
     * Update a project.
     *
     * @param project Project to update
     * @param newProjectName New project name
     * @param newColorIndex New color
     * @param newIndent New indent
     */
    private void updateProject(
            final TodoistProject project,
            final String newProjectName,
            final Integer newColorIndex,
            final Integer newIndent)
    {
        showProgressDialog(R.string.updating_project, new Thread() {
            @Override
            public void run()
            {
                // update project
                TodoistApiResultObject<TodoistProject> result =
                    ServiceLocator.todoistApi().updateProject(project.getId(),
                                                              newProjectName,
                                                              newColorIndex,
                                                              newIndent);

                checkTodoistApiResult(result, "updateProject");

                if (result.successful())
                {
                    getProjectManager().update(result.getObject());
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void deleteObj(final TodoistProject project)
    {
        showProgressDialog(R.string.deleting_project, new Thread() {
            @Override
            public void run()
            {
                // delete project
                TodoistApiResultSimple result = ServiceLocator.todoistApi().deleteProject(project.getId());

                checkTodoistApiResult(result, "result");

                if (result.successful())
                {
                    getProjectManager().delete(project);
                }

                notifyLoadingHandlerRefresh();
            };
        });
    }

    @Override
    protected void editDialog(final TodoistProject project)
    {
        startActivityForResult(ProjectEditActivity.getIntent(project), TodoistListActivityBase.START_ACTIVITY_EDIT);
    }

    @Override
    protected int getActivityLayout()
    {
        return R.layout.project_list;
    }

    @Override
    protected ITodoistBaseObjectManager<TodoistProject> getObjectManager()
    {
        return ServiceLocator.projectManager();
    }

    @Override
    protected int getStringNoItems()
    {
        return R.string.no_projects;
    }

    @Override
    protected int getStringLoadingItems()
    {
        return R.string.loading_projects;
    }

    @Override
    protected int getStringAddObj()
    {
        return R.string.add_project;
    }

    @Override
    protected int getStringDeleteObj()
    {
        return R.string.delete_project;
    }

    @Override
    protected int getStringDeleteObjConfirmation()
    {
        return R.string.delete_project_confirmation;
    }

    @Override
    protected int getMenu()
    {
        return R.menu.projectlist;
    }

    @Override
    protected void notifyListAdapterChanged()
    {
        // get the list adapter and notify it that contents have changed
        ProjectListAdapter listAdapter = (ProjectListAdapter) getListAdapter();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadInitial()
    {
        TodoistProjects projects = ServiceLocator.cache().getProjectsCache();
        if (projects == null)
        {
            loadFromTodoist();
        }
        else
        {
            getObjectManager().importArray(projects.getArray());
            refreshList();
        }
    }

    @Override
    protected int getStringAddObjMessage()
    {
        return R.string.add_project_message;
    }

    @Override
    protected void addDialog(final TodoistProject obj, final Boolean addAbove)
    {
        addDialogDefault(obj, addAbove);
    }

    @Override
    public void checkboxClick(final TodoistProject obj)
    {
        // no checkbox
        return;
    }

    @Override
    protected void loadAdditionalLayout()
    {
        // nothing
        return;
    }

    /**
     * Gets the Project Manager.
     *
     * @return Project Manager
     */
    private ProjectManager getProjectManager()
    {
        return (ProjectManager) getObjectManager();
    }

    @Override
    protected boolean showAddContextOptions()
    {
        return true;
    }

    @Override
    protected String getStringScreenName()
    {
        return "ProjectList";
    }

    @Override
    protected void onCreateInternalPre(final Bundle savedInstanceState)
    {
        // NOP
    }

    @Override
    protected boolean onOptionsItemSelectedInternal(final MenuItem item)
    {
        // No additional options
        return false;
    }
}
