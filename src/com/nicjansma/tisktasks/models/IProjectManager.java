package com.nicjansma.tisktasks.models;

/**
 * Todoist Project Manager.
 */
public interface IProjectManager
    extends ITodoistBaseObjectManager<TodoistProject>
{
    /**
     * Saves the projects to the cache.
     */
    void saveToCache();
}
