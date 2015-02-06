package com.nicjansma.tisktasks.api;

import java.util.ArrayList;

import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.models.TodoistQueryResult;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * Todoist API interface.
 */
public interface ITodoistApi
{
    //
    // API reference: http://todoist.com/API/help
    //

    /**
     * Sets the user's API token to use.
     *
     * @param token User's API token
     */
    void setToken(String token);

    //
    // User
    //

    /**
     * Attempt to log in the user.
     *
     * Uses HTTPs.
     *
     * @param email User's email
     * @param password User's password
     *
     * @return A TodoistUser if the login was successful
     */
    TodoistApiResultObject<TodoistUser> login(String email, String password);

    /**
     * Gets a list of available timezones.
     *
     * @return List of timezones.
     */
    TodoistApiResultArray<String> getTimezones();

    /**
     * Register a new user.
     *
     * Uses HTTPs.
     *
     * @param email User's email
     * @param fullName User's full name
     * @param password User's password
     * @param timeZone User's timezone
     *
     * @return A TodoistUser if the registration was successful
     */
    TodoistApiResultObject<TodoistUser> register(String email, String fullName, String password, String timeZone);

    /**
     * Updates the user.
     *
     * Uses HTTPS.
     *
     * @param email User's email
     * @param fullName User's full name
     * @param password User's password
     * @param timeZone User's timezone
     *
     * @return The updated TodoistUser if the registration was successful
     */
    TodoistApiResultObject<TodoistUser> updateUser(String email, String fullName, String password, String timeZone);

    //
    // Projects
    //
    /**
     * Gets the user's projects.
     *
     * @return A list of the user's projects.
     */
    TodoistApiResultArray<TodoistProject> getProjects();

    /**
     * Gets the specified project.
     *
     * @param projectId Project ID
     *
     * @return The specified project
     */
    TodoistApiResultObject<TodoistProject> getProject(long projectId);

    /**
     * Creates a new project.
     *
     * @param name Project name
     * @param color Project color
     * @param indent Project indentation level (level 1 is default)
     * @param order Order of the new project
     *
     * @return TodoistProject if the project was created successfully
     */
    TodoistApiResultObject<TodoistProject> addProject(String name, int color, int indent, int order);

    /**
     * Updates a project.
     *
     * @param projectId Project ID
     * @param name Project name
     * @param color Project color
     * @param indent Project indentation level
     *
     * @return TodoistProject if the project was updated successfully
     */
    TodoistApiResultObject<TodoistProject> updateProject(long projectId, String name, Integer color, Integer indent);

    /**
     * Updates project orders.
     *
     * @param orders A list of project IDs in their order
     *
     * @return OK on success
     */
    TodoistApiResultSimple updateProjectOrders(ArrayList<Long> orders);

    /**
     * Deletes a project.
     *
     * @param projectId Project ID
     *
     * @return OK on success
     */
    TodoistApiResultSimple deleteProject(long projectId);

    //
    // Labels (TODO)
    //

    //
    // Items (tasks)
    //
    /**
     * Gets uncompleted items for a project.
     *
     * @param projectId Project ID
     *
     * @return An array of uncompleted items
     */
    TodoistApiResultArray<TodoistItem> getUncompletedItems(long projectId);

    /**
     * Gets completed items for a project.
     *
     * @param projectId Project ID
     *
     * @return An array of completed items
     */
    TodoistApiResultArray<TodoistItem> getCompletedItems(long projectId);

    /**
     * Gets the specified items.
     *
     * @param items A list of item IDs
     *
     * @return A list of items
     */
    TodoistApiResultArray<TodoistItem> getItemsById(ArrayList<Long> items);

    /**
     * Creates the specified item.
     *
     * @param projectId Project ID
     * @param content Content (item text)
     * @param dateString Due date string
     * @param priority Priority (a number between 1 and 4, 4 for very urgent and 1 for natural).
     *
     * @return TodoistItem if item was created successfully
     */
    TodoistApiResultObject<TodoistItem> addItem(long projectId, String content, String dateString, int priority);

    /**
     * Updates an item.
     *
     * @param itemId Item ID
     * @param content Content (item text)
     * @param dateString Due date string
     * @param priority Priority (a number between 1 and 4, 4 for very urgent and 1 for natural).
     * @param indent Item indent
     * @param itemOrder Item order
     * @param collapsed Item is collapsed
     *
     * @return TodoistItem if the item was updated successfully
     */
    TodoistApiResultObject<TodoistItem> updateItem(long itemId,
                                                   String content,
                                                   String dateString,
                                                   Integer priority,
                                                   Integer indent,
                                                   Integer itemOrder,
                                                   Integer collapsed);

    /**
     * Updates item orders for a project.
     *
     * @param projectId Project ID
     * @param orders List of Item IDs in order
     *
     * @return OK if successful
     */
    TodoistApiResultSimple updateOrders(long projectId, ArrayList<Long> orders);

    /**
     * Updates items to the next recurring date.
     *
     * @param items Items to update
     *
     * @return An array of TodoistItems that were updated
     */
    TodoistApiResultArray<TodoistItem> updateRecurringDate(ArrayList<Long> items);

    /**
     * Deletes an item.
     *
     * (non in the API, helper function)
     *
     * @param itemId Item to delete
     *
     * @return OK if successful
     */
    TodoistApiResultSimple deleteItem(long itemId);

    /**
     * Deletes the specified items.
     *
     * @param items List of item IDs to remove
     *
     * @return OK if successful
     */
    TodoistApiResultSimple deleteItems(ArrayList<Long> items);

    /**
     * Completes an item.
     *
     * (non in the API, helper function)
     *
     * @param itemId Item to complete
     * @param inHistory Puts the items in history as opposed to being checked off
     *
     * @return OK if successful
     */
    TodoistApiResultSimple completeItem(long itemId, boolean inHistory);

    /**
     * Completes the specified items.
     *
     * @param items List of item IDs to complete
     * @param inHistory Puts the items in history as opposed to being checked off
     *
     * @return OK if successful
     */
    TodoistApiResultSimple completeItems(ArrayList<Long> items, boolean inHistory);

    /**
     * Un-Completes an item.
     *
     * (non in the API, helper function)
     *
     * @param itemId Item to un-complete
     *
     * @return OK if successful
     */
    TodoistApiResultSimple uncompleteItem(long itemId);

    /**
     * Un-Completes the specified items.
     *
     * @param items List of item IDs to un-complete
     *
     * @return OK if successful
     */
    TodoistApiResultSimple uncompleteItems(ArrayList<Long> items);

    /**
     * Executes a search query.
     *
     * (asCount removed, moved to its own function)
     *
     * @param queries List of queries to execute.
     *
     * @return Array of query results
     */
    TodoistApiResultArray<TodoistQueryResult> query(ArrayList<String> queries);

    /**
     * Executes a search query, and gets the number of results for each.
     *
     * (abstracted from query)
     *
     * @param queries List of queries to execute.
     *
     * @return Number of results for each query
     */
    TodoistApiResultArray<Long> queryCount(ArrayList<String> queries);

    //
    // Notes (TODO)
    //
}
