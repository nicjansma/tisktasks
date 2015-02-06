package com.nicjansma.tisktasks.api;

import android.net.Uri;
import android.net.Uri.Builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import com.nicjansma.library.net.HttpUtils;
import com.nicjansma.library.net.JsonResult;
import com.nicjansma.library.net.JsonResultArray;
import com.nicjansma.library.net.JsonResultObject;
import com.nicjansma.library.net.JsonUtils;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.models.TodoistItem;
import com.nicjansma.tisktasks.models.TodoistProject;
import com.nicjansma.tisktasks.models.TodoistQueryResult;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * Todoist API.
 */
public final class TodoistApi
    implements ITodoistApi
{
    //
    // Constants
    //
    /**
     * Base URL.
     */
    public static final String BASE_URL = "http://todoist.com/API/";

    /**
     * Base secure URL.
     */
    public static final String BASE_SECURE_URL = "https://todoist.com/API/";

    //
    // Privates
    //
    /**
     * User's API token.
     */
    private String _apiToken;

    @Override
    public void setToken(final String token)
    {
        _apiToken = token;
    }

    /**
     * Gets the URL for an API call.
     *
     * @param methodName Method name
     * @param args Hash map of arguments to their values
     * @param useToken Use the API token
     * @param secureRequired Use a secure connection
     *
     * @return URL for the API call
     */
    private String apiUrl(
        final String methodName,
        final HashMap<String, String> args,
        final boolean useToken,
        final boolean secureRequired)
    {
        Builder apiUrl = new Uri.Builder();
        apiUrl.scheme(secureRequired ? "https" : "http");
        apiUrl.authority("todoist.com");
        apiUrl.path("API/");

        apiUrl.appendPath(methodName);

        if (useToken)
        {
            if (_apiToken == null || _apiToken.compareTo("") == 0)
            {
                _apiToken = ServiceLocator.prefs().apiToken();
            }

            if (_apiToken.compareTo("") != 0)
            {
                apiUrl.appendQueryParameter("token", _apiToken);
            }
        }

        if (args != null)
        {
            for (String key : args.keySet())
            {
                String value = args.get(key);

                apiUrl.appendQueryParameter(key, value);
            }
        }

        return apiUrl.build().toString();
    }

    /**
     * Tracks API calls for JsonResultObjects.
     *
     * @param <T> JsonResultObjectType
     * @param result Result
     * @param methodName Method name
     * @return Result
     */
    private static <T> JsonResultObject<T> track(final JsonResultObject<T> result, final String methodName)
    {
        checkApiResult(result, methodName);
        return result;
    }

    /**
     * Tracks API calls for JsonResults.
     *
     * @param result Result
     * @param methodName Method name
     * @return Result
     */
    private static JsonResult track(final JsonResult result, final String methodName)
    {
        checkApiResult(result, methodName);
        return result;
    }

    /**
     * Tracks API calls for JsonResultArrays.
     *
     * @param <T> JsonResultObjectType
     * @param result Result
     * @param methodName Method name
     * @return Result
     */
    private static <T> JsonResultArray<T> track(final JsonResultArray<T> result, final String methodName)
    {
        checkApiResult(result, methodName);
        return result;
    }

    /**
     * Checks an API result.
     *
     * @param result Result
     * @param methodName Method name
     */
    private static void checkApiResult(final JsonResult result, final String methodName)
    {
        // track API calls and errors
        ServiceLocator.tracker().trackEvent("API", "Call", methodName, 0);
        if (result == null || !result.successful())
        {
            // track HTTP response code if available
            int resultCode = 0;
            if (result != null
                && result.getHttpResponse() != null)
            {
                resultCode = result.getHttpResponse().getHttpCode();
            }

            ServiceLocator.tracker().trackEvent("API", "Error", methodName, resultCode);
        }
    }

    @Override
    public TodoistApiResultObject<TodoistUser> login(
        final String email,
        final String password)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("email", email);
        args.put("password", password);

        String url = apiUrl("login", args, false, true);

        return new TodoistApiResultObject<TodoistUser>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistUser.class), "login"));
    }

    @Override
    public TodoistApiResultArray<String> getTimezones()
    {
        String url = apiUrl("getTimezones", null, false, true);

        return new TodoistApiResultArray<String>(track(HttpUtils.getJsonAsArrayOfStrings(url), "getTimezones"));
    }

    @Override
    public TodoistApiResultObject<TodoistUser> register(
        final String email,
        final String fullName,
        final String password,
        final String timeZone)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("email", email);
        args.put("full_name", fullName);
        args.put("password", password);
        args.put("timezone", timeZone);

        String url = apiUrl("register", args, false, true);

        return new TodoistApiResultObject<TodoistUser>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistUser.class), "register"));
    }

    @Override
    public TodoistApiResultObject<TodoistUser> updateUser(
        final String email,
        final String fullName,
        final String password,
        final String timeZone)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("email", email);
        args.put("full_name", fullName);
        args.put("password", password);
        args.put("timezone", timeZone);

        String url = apiUrl("updateUser", args, false, true);

        return new TodoistApiResultObject<TodoistUser>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistUser.class), "updateUser"));
    }

    @Override
    public TodoistApiResultArray<TodoistProject> getProjects()
    {
        String url = apiUrl("getProjects", null, true, false);

        return new TodoistApiResultArray<TodoistProject>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistProject.class), "getProjects"));
    }

    @Override
    public TodoistApiResultObject<TodoistProject> getProject(final long projectId)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));

        String url = apiUrl("getProject", args, true, false);

        return new TodoistApiResultObject<TodoistProject>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistProject.class), "getProject"));
    }

    @Override
    public TodoistApiResultObject<TodoistProject> addProject(
        final String name,
        final int color,
        final int indent,
        final int order)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("name", name);
        args.put("color", String.valueOf(color));
        args.put("indent", String.valueOf(indent));
        args.put("order", String.valueOf(order));

        String url = apiUrl("addProject", args, true, false);

        return new TodoistApiResultObject<TodoistProject>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistProject.class), "addProject"));
    }

    @Override
    public TodoistApiResultObject<TodoistProject> updateProject(
        final long projectId,
        final String name,
        final Integer color,
        final Integer indent)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));

        if (name != null)
        {
            args.put("name", name);
        }

        if (color != null)
        {
            args.put("color", String.valueOf(color));
        }

        if (indent != null)
        {
            args.put("indent", String.valueOf(indent));
        }

        String url = apiUrl("updateProject", args, true, false);

        return new TodoistApiResultObject<TodoistProject>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistProject.class), "updateProject"));
    }

    @Override
    public TodoistApiResultSimple updateProjectOrders(final ArrayList<Long> orders)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(orders);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("item_id_list", jsonArray.toString());

        String url = apiUrl("updateProjectOrders", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "updateProjectOrders"));
    }

    @Override
    public TodoistApiResultSimple deleteProject(final long projectId)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));

        String url = apiUrl("deleteProject", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "deleteProject"));
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getUncompletedItems(final long projectId)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));

        String url = apiUrl("getUncompletedItems", args, true, false);

        return new TodoistApiResultArray<TodoistItem>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistItem.class), "getUncompletedItems"));
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getCompletedItems(final long projectId)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));

        String url = apiUrl("getCompletedItems", args, true, false);

        return new TodoistApiResultArray<TodoistItem>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistItem.class), "getCompletedItems"));
    }

    @Override
    public TodoistApiResultArray<TodoistItem> getItemsById(final ArrayList<Long> items)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(items);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("ids", jsonArray.toString());

        String url = apiUrl("getItemsById", args, true, false);

        return new TodoistApiResultArray<TodoistItem>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistItem.class), "getItemsById"));
    }

    @Override
    public TodoistApiResultObject<TodoistItem> addItem(
        final long projectId,
        final String content,
        final String dateString,
        final int priority)
    {
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));
        args.put("content", content);

        if (dateString != null)
        {
            args.put("date_string", dateString);
        }

        args.put("priority", String.valueOf(priority));

        String url = apiUrl("addItem", args, true, false);

        return new TodoistApiResultObject<TodoistItem>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistItem.class), "addItem"));
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
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("id", String.valueOf(itemId));

        if (content != null)
        {
            args.put("content", content);
        }

        if (dateString != null)
        {
            args.put("date_string", dateString);
        }

        if (priority != null)
        {
            args.put("priority", String.valueOf(priority));
        }

        if (indent != null)
        {
            args.put("indent", String.valueOf(indent));
        }

        if (itemOrder != null)
        {
            args.put("item_order", String.valueOf(itemOrder));
        }

        String url = apiUrl("updateItem", args, true, false);

        return new TodoistApiResultObject<TodoistItem>(
                track(HttpUtils.getJsonAsObjectOf(url, TodoistItem.class), "updateItem"));
    }

    @Override
    public TodoistApiResultSimple updateOrders(
        final long projectId,
        final ArrayList<Long> orders)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(orders);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("project_id", String.valueOf(projectId));
        args.put("item_id_list", jsonArray.toString());

        String url = apiUrl("updateOrders", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "updateOrders"));
    }

    @Override
    public TodoistApiResultArray<TodoistItem> updateRecurringDate(final ArrayList<Long> items)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(items);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("ids", jsonArray.toString());

        String url = apiUrl("updateRecurringDate", args, true, false);

        return new TodoistApiResultArray<TodoistItem>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistItem.class), "updateRecurringDate"));
    }

    @Override
    public TodoistApiResultSimple deleteItems(final ArrayList<Long> items)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(items);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("ids", jsonArray.toString());

        String url = apiUrl("deleteItems", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "deleteItems"));
    }

    @Override
    public TodoistApiResultSimple completeItems(final ArrayList<Long> items, final boolean inHistory)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(items);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("ids", jsonArray.toString());
        args.put("in_history", inHistory ? String.valueOf(1) : String.valueOf(0));

        String url = apiUrl("completeItems", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "completeItems"));
    }

    @Override
    public TodoistApiResultSimple uncompleteItems(final ArrayList<Long> items)
    {
        JSONArray jsonArray = JsonUtils.convertLongArrayToJsonArray(items);

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("ids", jsonArray.toString());

        String url = apiUrl("uncompleteItems", args, true, false);

        return new TodoistApiResultSimple(track(HttpUtils.getJson(url), "uncompleteItems"));
    }

    @Override
    public TodoistApiResultArray<TodoistQueryResult> query(final ArrayList<String> queries)
    {
        HashMap<String, String> args = new HashMap<String, String>();

        args.put("queries", JsonUtils.convertStringArrayToJsonArray(queries).toString());

        String url = apiUrl("query", args, true, false);

        return new TodoistApiResultArray<TodoistQueryResult>(
                track(HttpUtils.getJsonAsArrayOf(url, TodoistQueryResult.class), "query"));
    }

    @Override
    public TodoistApiResultArray<Long> queryCount(final ArrayList<String> queries)
    {
        TodoistApiResultArray<TodoistQueryResult> results = query(queries);

        ArrayList<Long> counts = new ArrayList<Long>();

        // count each result
        for (int i = 0; i < results.getArray().size(); i++)
        {
            Long queryCount = Long.valueOf(results.getArray().get(i).getTaskManager().getCount());
            counts.add(queryCount);
        }

        // convert to an API result
        TodoistApiResultArray<Long> resultCounts = new TodoistApiResultArray<Long>(counts);

        return resultCounts;
    }

    @Override
    public TodoistApiResultSimple deleteItem(final long itemId)
    {
        ArrayList<Long> items = new ArrayList<Long>();
        items.add(itemId);
        return deleteItems(items);
    }

    @Override
    public TodoistApiResultSimple completeItem(final long itemId, final boolean inHistory)
    {
        ArrayList<Long> items = new ArrayList<Long>();
        items.add(itemId);
        return completeItems(items, inHistory);
    }

    @Override
    public TodoistApiResultSimple uncompleteItem(final long itemId)
    {
        ArrayList<Long> items = new ArrayList<Long>();
        items.add(itemId);
        return uncompleteItems(items);
    }
}
