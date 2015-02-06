package com.nicjansma.tisktasks.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicjansma.tisktasks.AppOptionsMenu;
import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResult;
import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TodoistObjectBase;

/**
 * A ListActivity for Todoist tasks and projects.
 *
 * @param <T> Type of object being listed
 */
public abstract class TodoistListActivityBase<T extends TodoistObjectBase>
    extends ActionBarActivity
{
    //
    // Constants
    //
    /**
     * Class tag (for debugging).
     */
    private static final String TAG = TodoistListActivityBase.class.getSimpleName();

    /**
     * Message: Refresh list.
     */
    protected static final int MESSAGE_REFRESH_LIST = 0;

    /**
     * Message: API error.
     */
    protected static final int MESSAGE_API_ERROR = 1;

    /**
     * Max message number.
     */
    protected static final int MESSAGE_MAX = 1;

    /**
     * Dialog: Delete.
     */
    private static final int DIALOG_DELETE = 0;

    /**
     * Context menu: Edit.
     */
    private static final int CONTEXTMENU_EDIT = 0;

    /**
     * Context menu: Delete.
     */
    private static final int CONTEXTMENU_DELETE = 1;

    /**
     * Context menu: Add Above.
     */
    private static final int CONTEXTMENU_ADD_ABOVE = 2;

    /**
     * Context menu: Add Below.
     */
    private static final int CONTEXTMENU_ADD_BELOW = 3;

    /**
     * Context menu: Increase Indent.
     */
    private static final int CONTEXTMENU_INDENT_INCREASE = 4;

    /**
     * Context menu: Decrease Indent.
     */
    private static final int CONTEXTMENU_INDENT_DECREASE = 5;

    /**
     * Start Activity: Edit.
     */
    protected static final int START_ACTIVITY_EDIT = 0;

    /**
     * Start Activity: Add.
     */
    protected static final int START_ACTIVITY_ADD = 1;

    /**
     * Start Activity maximum.
     */
    protected static final int START_ACTIVITY_MAX = 1;

    /**
     * Alert padding.
     */
    protected static final int ALERT_PADDING = 10;

    //
    // Components
    //
    /**
     * Loading thread.
     */
    private Thread _threadLoading;

    /**
     * Loading handler.
     */
    private Handler _handlerLoading;

    /**
     * Progress dialog.
     */
    private ProgressDialog _progressDialog;

    //
    // UI
    //
    /**
     * Status text view.
     */
    private TextView _statusTextView;

    /**
     * Main list view.
     */
    private ListView _listView;

    /**
     * Add button.
     */
    private Button _addButton;

    /**
     * Refresh button.
     */
    private Button _refreshButton;

    /**
     * Main object.
     */
    private T _obj;

    /**
     * Activity context.
     */
    private Activity _context;

    //
    // Extension methods
    //
    /**
     * Called at the completion of onResume in the base class.
     */
    protected abstract void onResumeInternal();

    /**
     * Called at the beginning of onCreate in the base class.
     *
     * @param savedInstanceState Saved instance bundle
     */
    protected abstract void onCreateInternalPre(final Bundle savedInstanceState);

    /**
     * Called after an object has been indented.
     *
     * @param obj Object indented
     */
    protected abstract void afterIndentObj(final T obj);

    /**
     * Starts a list view click activity.
     *
     * @param view View clicked on
     */
    protected abstract void startClickActivity(View view);

    /**
     * Starts a list view click activity.
     *
     * @param obj Object clicked on
     */
    protected abstract void startClickActivity(T obj);

    /**
     * Base class is requesting the activity layout resource ID.
     *
     * @return Activity layout resource ID.
     */
    protected abstract int getActivityLayout();

    /**
     * Base class is requesting the object manager of the activity.
     *
     * @return Object manager.
     */
    protected abstract ITodoistBaseObjectManager<T> getObjectManager();

    /**
     * Base class is requesting that the List Adapter be created.
     */
    protected abstract void createListAdapter();

    /**
     * Base class is requesting that list is loaded from Todoist.
     */
    protected abstract void loadFromTodoist();

    /**
     * Base class calls in the onCreate method after all of the base class' layout has been inflated.
     */
    protected abstract void loadAdditionalLayout();

    /**
     * Base class calls in the onCreate method after loadAdditionalLayout() to load initial data.
     */
    protected abstract void loadInitial();

    /**
     * Base class queries to determine if the Add context menu should be shown.
     *
     * @return True if the the Add context menu should be shown
     */
    protected abstract boolean showAddContextOptions();

    /**
     * Base class calls to add a new object.
     *
     * @param nearObj Near this object.
     * @param newName New object name.
     * @param addAbove Add above the nearObj as opposed to below it.
     */
    protected abstract void addObj(final T nearObj, final String newName, final Boolean addAbove);

    /**
     * Base class calls to delete an object.
     *
     * @param obj Object to remove
     */
    protected abstract void deleteObj(final T obj);

    /**
     * Base class calls to get the menu resource ID.
     *
     * @return Menu resource ID
     */
    protected abstract int getMenu();

    /**
     * Base class calls when a menu item is selected.
     *
     * @param item Menu item
     * @return True if item was handled
     */
    protected abstract boolean onOptionsItemSelectedInternal(final MenuItem item);

    /**
     * Base class calls to show an Edit dialog.
     *
     * @param obj Object to edit
     */
    protected abstract void editDialog(final T obj);

    /**
     * Base class calls to show an Add dialog.
     *
     * @param nearObj Object to edit
     * @param addAbove Add new object above instead of below nearObj
     */
    protected abstract void addDialog(final T nearObj, final Boolean addAbove);

    /**
     * Base class is requesting the string to display when there are no items.
     *
     * @return No Items string resource ID
     */
    protected abstract int getStringNoItems();

    /**
     * Base class is requesting the string to display when loading.
     *
     * @return Loading Items string resource ID
     */
    protected abstract int getStringLoadingItems();

    /**
     * Base class is requesting the string to display for the title of the dialog when Adding an object.
     *
     * @return Title of Add dialog
     */
    protected abstract int getStringAddObj();

    /**
     * Base class is requesting the string to display as a prompt for Adding an object.
     *
     * @return Prompt in the Add dialog
     */
    protected abstract int getStringAddObjMessage();

    /**
     * Base class is requesting the string to display for the title of the dialog when Deleting an object.
     *
     * @return Title of Delete dialog
     */
    protected abstract int getStringDeleteObj();

    /**
     * Base class is requesting the string to display when confirming Deleting an object.
     *
     * @return Confirmation to display when Deleting an object
     */
    protected abstract int getStringDeleteObjConfirmation();

    /**
     * Base class is requesting the string to track the screen view for analytics.
     *
     * @return Page view string
     */
    protected abstract String getStringScreenName();

    /**
     * Notifies the List Adapter that data has changed.
     */
    protected abstract void notifyListAdapterChanged();

    /**
     * Called when an item's checkbox is clicked.
     *
     * @param obj Object whose checkbox was clicked
     */
    public abstract void checkboxClick(T obj);

    @Override
    public final void onCreate(final Bundle savedInstanceState)
    {
        long startTime = System.currentTimeMillis();

        onCreateInternalPre(savedInstanceState);

        super.onCreate(savedInstanceState);

        //
        // If there is no API token yet, we need to show the login screen
        //
        if (!ServiceLocator.userManager().isLoggedIn())
        {
            finish();
            return;
        }

        setContentView(getActivityLayout());

        _context = this;

        _statusTextView = (TextView) findViewById(R.id.list_status);
        _statusTextView.setVisibility(View.VISIBLE);
        _statusTextView.setText(getStringLoadingItems());

        _listView = getListView();
        _listView.setTextFilterEnabled(false);
        _listView.setItemsCanFocus(false);
        _listView.setVisibility(View.GONE);
        registerForContextMenu(_listView);

        _addButton = (Button) findViewById(R.id.add_item);
        if (_addButton != null)
        {
            _addButton.setOnClickListener(_addButtonOnClickListener);
        }

        _refreshButton = (Button) findViewById(R.id.refresh);
        if (_refreshButton != null)
        {
            _refreshButton.setOnClickListener(_refreshButtonOnClickListener);
        }

        setupHandler();

        loadAdditionalLayout();

        loadInitial();

        ServiceLocator.tracker().trackScreenStartup(getStringScreenName(), startTime);
    }

    /**
     * Gets the ListView associated with the activity.
     *
     * @return ListView
     */
    protected final ListView getListView()
    {
        if (_listView == null)
        {
            _listView = (ListView) findViewById(android.R.id.list);
            _listView.setOnItemClickListener(_onItemClickListener);
        }

        return _listView;
    }

    /**
     * Sets the ListAdapter associated with the ListView.
     *
     * @param adapter List adapter
     */
    protected final void setListAdapter(final ListAdapter adapter)
    {
        getListView().setAdapter(adapter);
    }

    /**
     * Gets the ListAdapter associated with the ListView.
     *
     * @return ListAdapter
     */
    protected final ListAdapter getListAdapter()
    {
        ListAdapter adapter = getListView().getAdapter();

        if (adapter instanceof HeaderViewListAdapter)
        {
            return ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        else
        {
            return adapter;
        }
    }


    /**
     * Add button on-click listener.
     */
    private final Button.OnClickListener _addButtonOnClickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            addDialog(null, false);
        }
    };

    /**
     * Refresh button on-click listener.
     */
    private final Button.OnClickListener _refreshButtonOnClickListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            loadFromTodoist();
        }
    };

    /**
     * List item click listener.
     */
    private final ListView.OnItemClickListener _onItemClickListener = new ListView.OnItemClickListener()
    {
        @Override
        public void onItemClick(final AdapterView<?> av, final View v, final int position, final long id)
        {
            startClickActivity(v);
        }
    };

    /**
     * Sets the status text.
     *
     * @param stringId Status resource ID
     */
    protected final void setStatusText(final int stringId)
    {
        _statusTextView.setText(stringId);
    }

    /**
     * Sets up the message handler.
     */
    private void setupHandler()
    {
        if (_handlerLoading != null)
        {
            return;
        }

        _handlerLoading = new Handler() {
            @Override
            public void handleMessage(final Message message)
            {
                switch (message.what)
                {
                    case MESSAGE_REFRESH_LIST:
                        try
                        {
                            if (_progressDialog != null)
                            {
                                _progressDialog.dismiss();
                                _progressDialog = null;
                            }
                        }
                        catch (final Exception e)
                        {
                            // might have occurred if the activity is being canceled, ignore this error.
                            Log.w(TAG, e);
                        }

                        setProgressBarIndeterminateVisibility(false);

                        refreshList();
                        break;

                    case MESSAGE_API_ERROR:
                        showApiError();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    /**
     * Loads objects from the manager.
     */
    private void loadFromManager()
    {
        if (getObjectManager() == null)
        {
            return;
        }

        if (getListAdapter() == null)
        {
            createListAdapter();
        }

        if (getObjectManager().getArray().size() == 0)
        {
            _statusTextView.setVisibility(View.VISIBLE);

            _statusTextView.setText(getStringNoItems());

            _listView.setVisibility(View.GONE);
        }
        else
        {
            _statusTextView.setVisibility(View.GONE);
            _listView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Shows a progress dialog.
     *
     * @param titleResId Progress dialog text
     * @param startThread Thread to run
     */
    protected final void showProgressDialog(final int titleResId, final Thread startThread)
    {
        if (_progressDialog != null)
        {
            _progressDialog.cancel();
        }

        setProgressBarIndeterminateVisibility(true);

        _progressDialog = ProgressDialog.show(this,
                                              getString(R.string.please_wait),
                                              getString(titleResId),
                                              true);

        _progressDialog.setCancelable(true);

        _threadLoading = startThread;
        _threadLoading.start();
    }

    /**
     * Notifies the handler to refresh the list.
     */
    protected final void notifyLoadingHandlerRefresh()
    {
        if (_handlerLoading != null)
        {
            _handlerLoading.sendEmptyMessage(MESSAGE_REFRESH_LIST);
        }
    }

    /**
     * Indents and object.
     *
     * @param obj Object to indent
     * @param indentAmount Amount to indent (-1 to de-indent)
     */
    protected final void indentObj(final T obj, final int indentAmount)
    {
        if (indentAmount == 1)
        {
            obj.increaseIndent();
        }
        else
        {
            obj.decreaseIndent();
        }

        afterIndentObj(obj);
    }

    @Override
    protected final void onResume()
    {
        super.onResume();

        //
        // Nothing to show if they're not logged in
        //
        if (!ServiceLocator.userManager().isLoggedIn())
        {
            finish();
        }

        onResumeInternal();
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item)
    {
        if (!AppOptionsMenu.onOptionsItemSelected(this, item))
        {
            switch (item.getItemId())
            {
                case R.id.refresh:
                    loadFromTodoist();
                    break;

                case R.id.add_item:
                    addDialog(null, false);
                    break;

                default:
                    break;
            }
        }

        return onOptionsItemSelectedInternal(item);
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getMenu(), menu);

        return AppOptionsMenu.onCreateOptionsMenu(this, menu);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean onContextItemSelected(final MenuItem item)
    {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();

        T obj = (T) getListAdapter().getItem(menuInfo.position);

        if (obj == null)
        {
            return false;
        }

        switch (item.getItemId())
        {
            case CONTEXTMENU_EDIT:
                editDialog(obj);
                return true;

            case CONTEXTMENU_DELETE:
                confirmDeleteDialog(obj);
                return true;

            case CONTEXTMENU_ADD_ABOVE:
                addDialog(obj, true);
                return true;

            case CONTEXTMENU_ADD_BELOW:
                addDialog(obj, false);
                return true;

            case CONTEXTMENU_INDENT_INCREASE:
                indentObj(obj, 1);
                return true;

            case CONTEXTMENU_INDENT_DECREASE:
                indentObj(obj, -1);
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    protected final void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // behavior is the same for both START_ACTIVITY_EDIT
        // and START_ACTIVITY_ADD
        if (resultCode == RESULT_OK)
        {
            refreshList();
        }
        else if (resultCode == PopupDialogBase.RESULT_API_ERROR)
        {
            showApiError();
        }
    }

    /**
     * Default Add dialog.
     *
     * @param nearObj Object to add below
     * @param addAbove Add above the object as opposed to below
     */
    protected final void addDialogDefault(final T nearObj, final Boolean addAbove)
    {
        _obj = nearObj;

        // if not specified, take the last one
        if (_obj == null)
        {
            _obj = getObjectManager().getLast();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getStringAddObj());
        builder.setMessage(getStringAddObjMessage());

        // create an EditText to get the user's input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton)
            {
                String newName = input.getText().toString();

                addObj(_obj, newName, addAbove);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int whichButton)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setView(input, ALERT_PADDING, ALERT_PADDING, ALERT_PADDING, ALERT_PADDING);
        alert.show();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo)
    {
        AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
        T obj = (T) getListAdapter().getItem(adapterMenuInfo.position);

        menu.add(0, CONTEXTMENU_EDIT, CONTEXTMENU_EDIT, R.string.edit);
        menu.add(0, CONTEXTMENU_DELETE, CONTEXTMENU_DELETE, R.string.delete);

        // only add Add items if allowed
        if (showAddContextOptions())
        {
            menu.add(0, CONTEXTMENU_ADD_ABOVE, CONTEXTMENU_ADD_ABOVE, R.string.add_above);
            menu.add(0, CONTEXTMENU_ADD_BELOW, CONTEXTMENU_ADD_BELOW, R.string.add_below);
        }

        menu.add(0, CONTEXTMENU_INDENT_INCREASE, CONTEXTMENU_INDENT_INCREASE, R.string.increase_indent);

        // only show decrese indent if already indented
        if (obj.hasIndent())
        {
            menu.add(0, CONTEXTMENU_INDENT_DECREASE, CONTEXTMENU_INDENT_DECREASE, R.string.decrease_indent);
        }
    }

    /**
     * Show the Delete confirmation dialog.
     *
     * @param obj Object to delete if confirmed
     */
    private void confirmDeleteDialog(final T obj)
    {
        _obj = obj;
        showDialog(DIALOG_DELETE);
    }

    //
    // Note: API level 8 has Bundle as a second argument
    //
    @Override
    protected final Dialog onCreateDialog(final int id)
    {
        Dialog dialog;

        switch (id)
        {
            case DIALOG_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // need to use a placeholder message or dialog won't be shown
                builder.setTitle(getStringDeleteObj())
                       .setMessage(R.string.placeholder)
                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                           @Override
                        public void onClick(final DialogInterface dialog, final int id)
                           {
                               deleteObj(_obj);
                           }
                       })
                       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                           @Override
                        public void onClick(final DialogInterface dialog, final int id)
                           {
                               dialog.dismiss();
                           }
                       });

                dialog = builder.create();
                break;

            default:
                dialog = null;
        }

        return dialog;
    }

    //
    // Note: API level 8 has Bundle as a third argument
    //
    @Override
    protected final void onPrepareDialog(final int id, final Dialog dialog)
    {
        switch (id)
        {
            case DIALOG_DELETE:
                AlertDialog alertDialog = (AlertDialog) dialog;

                String confirmation = getString(getStringDeleteObjConfirmation());
                confirmation = String.format(confirmation, _obj.getText());

                alertDialog.setMessage(confirmation);
                break;

            default:
                break;
        }
    }

    /**
     * Refreshes the list from the manager.
     */
    protected final void refreshList()
    {
        loadFromManager();
        notifyListAdapterChanged();
    }

    /**
     * Expands or collapses an item.
     *
     * @param obj Item to expand
     */
    public final void expandClick(final T obj)
    {
        if (obj.isParent())
        {
            if (obj.isCollapsed())
            {
                obj.setCollapsed(false);
            }
            else
            {
                obj.setCollapsed(true);
            }

            refreshList();
        }
    }

    /**
     * Called when a row is clicked on.
     *
     * @param obj Row clicked on
     */
    public final void rowClick(final T obj)
    {
        startClickActivity(obj);
    }

    /**
     * Shows a Toast when an API error occurs.
     */
    public final void showApiError()
    {
        Toast.makeText(_context, R.string.api_error, Toast.LENGTH_LONG)
             .show();
    }

    /**
     * Checks the result of an API call.
     *
     * @param result Todoist result
     * @param methodName Todoist method name
     */
    public final void checkTodoistApiResult(final TodoistApiResult result, final String methodName)
    {
        if (result == null || !result.successful())
        {
            _handlerLoading.sendEmptyMessage(MESSAGE_API_ERROR);
        }
    }

    /**
     * Gets the Activity context.
     *
     * @return Activity context
     */
    protected final Activity getContext()
    {
        return _context;
    }
}
