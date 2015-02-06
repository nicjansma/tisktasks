package com.nicjansma.tisktasks.models;

import com.nicjansma.tisktasks.ServiceLocator;

/**
 * Project Manager
 *
 * Manages all known projects for the user.
 *
 * Handles caching of the project list for performance.
 *
 * All modifying operations on the TodoistObjectManagerBase class are overridden to saveToCache() as well.
 */
public final class ProjectManager
    extends TodoistObjectManagerBase<TodoistProject>
    implements IProjectManager
{
    /**
     * Saves the current project list in the object manager back to the cache.
     *
     * Should be used after any modifying operation on a TodoistProject reference.
     * Operations on the Project Manager are automatically saved to the cache,
     * but operations on the projects within the Manager are not visible to
     * the Manager and should be saved.
     */
    @Override
    public void saveToCache()
    {
        TodoistProjects projects = new TodoistProjects(getArray());
        ServiceLocator.cache().setProjectCache(projects);
    }

    @Override
    protected void postChangeHook()
    {
        saveToCache();
    }
}
