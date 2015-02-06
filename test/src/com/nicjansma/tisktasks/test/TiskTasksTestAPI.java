package com.nicjansma.tisktasks.test;

import java.util.ArrayList;

import com.nicjansma.tisktasks.api.TodoistApiResultArray;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.api.TodoistApiResultSimple;
import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.models.TodoistQueryResult;
import com.nicjansma.tisktasks.models.TodoistUser;

public final class TiskTasksTestAPI
    implements com.nicjansma.tisktasks.api.ITodoistApi
{
    @Override
    public void setToken(final String token)
    {
    }

    @Override
    public TodoistApiResultArray<TodoistProject> getProjects()
    {
        ArrayList<TodoistProject> projects = new ArrayList<TodoistProject>();

        projects.add(new TodoistProject(0, 1, "Test 1", "white", 0, 10, false, 0));
        projects.add(new TodoistProject(1, 1, "Test 2", "white", 1, 20, false, 1));

        return new TodoistApiResultArray<TodoistProject>(projects);
    }

    @Override
    public TodoistApiResultObject<TodoistUser> login(final String email, final String password)
    {
        return null;
    }

    @Override
    public TodoistApiResultArray<String> getTimezones()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistUser> register(
        final String email,
        final String fullName,
        final String password,
        final String timeZone)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistUser> updateUser(
        final String email,
        final String fullName,
        final String password,
        final String timeZone)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistProject> getProject(final long projectId)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistProject> addProject(
        final String name,
        final int color,
        final int indent,
        final int order)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistProject> updateProject(
        final long projectId,
        final String name,
        final Integer color,
        final Integer indent)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple updateProjectOrders(final ArrayList<Long> orders)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple deleteProject(final long projectId)
    {
        return new TodoistApiResultSimple(false);
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getUncompletedItems(final long projectId)
    {
        return null;
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getCompletedItems(final long projectId)
    {
        return null;
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getItemsById(final ArrayList<Long> items)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistItem> addItem(
         final long projectId,
         final String content,
         final String dateString,
         final int priority)
    {
        return null;
    }

    @Override
    public TodoistApiResultObject<TodoistItem> updateItem(
        final long itemId,
        final String content,
        final String dateString,
        final Integer priority,
        final Integer indent,
        final Integer itemOrder,
        final Integer collapsed)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple updateOrders(final long projectId, final ArrayList<Long> orders)
    {
        return new TodoistApiResultSimple(false);
    }

    @Override
    public TodoistApiResultArray<TodoistItem> updateRecurringDate(final ArrayList<Long> items)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple deleteItems(final ArrayList<Long> items)
    {
        return new TodoistApiResultSimple(false);
    }

    @Override
    public TodoistApiResultSimple completeItems(final ArrayList<Long> items, final boolean inHistory)
    {
        return new TodoistApiResultSimple(false);
    }

    @Override
    public TodoistApiResultSimple uncompleteItems(final ArrayList<Long> items)
    {
        return new TodoistApiResultSimple(false);
    }

    public TodoistApiResultArray<TodoistQueryResult> query(final ArrayList<String> queries, final boolean asCount)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple deleteItem(final long itemId)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple completeItem(final long itemId, final boolean inHistory)
    {
        return null;
    }

    @Override
    public TodoistApiResultSimple uncompleteItem(final long itemId)
    {
        return null;
    }

    @Override
    public TodoistApiResultArray<TodoistQueryResult> query(final ArrayList<String> queries)
    {
        return null;
    }

    @Override
    public TodoistApiResultArray<Long> queryCount(final ArrayList<String> queries)
    {
        return null;
    }
}
