package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

import com.nicjansma.library.net.CacheableJsonObjectArrayBase;

/**
 * Todoist Projects.
 */
public final class TodoistProjects
    extends CacheableJsonObjectArrayBase<TodoistProject>
{
    /**
     * Constructor.
     */
    public TodoistProjects()
    {
        super();
    }

    /**
     * Constructor with an array of projects.
     *
     * @param projects Projects to import
     */
    public TodoistProjects(final ArrayList<TodoistProject> projects)
    {
        super(projects);
    }

    @Override
    protected Class<TodoistProject> getObjectClass()
    {
        return TodoistProject.class;
    }

}
