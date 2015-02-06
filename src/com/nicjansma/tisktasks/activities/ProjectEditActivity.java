package com.nicjansma.tisktasks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.models.TodoistObjectBase;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.views.ColorSelectorGridAdapter;

/**
 * Edit Project activity.
 */
public final class ProjectEditActivity
    extends PopupDialogBase<TodoistProject>
{
    //
    // Constants
    //
    /**
     * Activity's intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.PROJECTEDIT";

    /**
     * Color GridView.
     */
    private GridView _colorGrid;

    /**
     * Color Grid Adapter.
     */
    private ColorSelectorGridAdapter _colorGridAdapter;

    @Override
    protected void onCreateInternalPre(final Bundle savedInstance)
    {
        // NOP
    }

    @Override
    public void onCreateInternalPost(final Bundle savedInstanceState)
    {
        _colorGrid = (GridView) findViewById(R.id.project_color_grid);
        _colorGridAdapter = new ColorSelectorGridAdapter(this, getObj().getColorIndex());
        _colorGrid.setAdapter(_colorGridAdapter);
    }

    /**
     * Gets an Intent for this Activity.
     *
     * @param project Todoist project to start with
     * @param <T> Type of Todoist object
     *
     * @return Intent to use
     */
    public static <T extends TodoistObjectBase> Intent getIntent(final T project)
    {
        // create an intent
        Intent intent = new Intent(INTENT);

        // store the specified minifig
        Bundle bundle = new Bundle();
        bundle.putLong(PopupDialogBase.BUNDLE_ID_KEY, project.getId());

        intent.putExtras(bundle);

        return intent;
    }

    @Override
    public boolean hasChangesToSave()
    {
        return (colorIndexChanges() != null);
    }

    /**
     * Determines if there are any changes with the color index.
     *
     * @return True if there are any color changes
     */
    public Integer colorIndexChanges()
    {
        final Integer newColorIndex = _colorGridAdapter.getColorIndex();

        if (getObj().getColorIndex() != newColorIndex)
        {
            return newColorIndex;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean makeChanges()
    {
        // updat the project
        TodoistApiResultObject<TodoistProject> result =
            ServiceLocator.todoistApi().updateProject(getObj().getId(),
                                                      nameChanges(),
                                                      colorIndexChanges(),
                                                      null);

        // if successful, we need to update the project manager with the updated project too
        if (result.successful())
        {
            ServiceLocator.projectManager().update(result.getObject());
        }

        return result.successful();
    }

    @Override
    protected int getContentViewId()
    {
        return R.layout.project_edit;
    }

    @Override
    protected int getUpdatingString()
    {
        return R.string.updating_project;
    }

    @Override
    protected TodoistProject loadObj(final long id)
    {
        return ServiceLocator.projectManager().get(id);
    }

    @Override
    protected String getDefaultText()
    {
        return getObj().getName();
    }

    @Override
    protected int getSaveButtonString()
    {
        return R.string.save;
    }

    @Override
    protected String getStringScreenName()
    {
        return "ProjectEdit";
    }
}
