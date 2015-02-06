package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

/**
 * Task Manager.
 */
public final class TaskManager
    extends TodoistObjectManagerBase<TodoistItem>
    implements ITaskManager
{
    /**
     * Complete items.
     *
     * @param itemIds Items to complete
     */
    public void completeItems(final ArrayList<Long> itemIds)
    {
        for (int i = 0; i < itemIds.size(); i++)
        {
            TodoistItem item = get(itemIds.get(i));
            item.setCheckedState(true);
        }
    }

    /**
     * Un-Complete items.
     *
     * @param itemIds Items to un-complete
     */
    public void uncompleteItems(final ArrayList<Long> itemIds)
    {
        for (int i = 0; i < itemIds.size(); i++)
        {
            TodoistItem item = get(itemIds.get(i));
            item.setCheckedState(false);
        }
    }

    @Override
    protected void postChangeHook()
    {
        // NOP
    }
}
