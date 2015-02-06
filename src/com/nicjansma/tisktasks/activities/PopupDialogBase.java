package com.nicjansma.tisktasks.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.models.TodoistObjectBase;

/**
 * A base class for popup dialogs that deal with a TodoistObjectBase object.
 *
 * @param <T> Type of TodoistObjectBase object.
 */
public abstract class PopupDialogBase<T extends TodoistObjectBase>
    extends Activity
{
    /**
     * Class tag (for debugging).
     */
    private static final String TAG = PopupDialogBase.class.getSimpleName();

    //
    // Constants
    //
    /**
     * Message: API Error.
     */
    protected static final int MESSAGE_API_ERROR = 0;

    /**
     * Maximum ID of messages.
     */
    protected static final int MESSAGE_MAX = 0;

    /**
     * Result: API error.
     */
    public static final int RESULT_API_ERROR = RESULT_FIRST_USER;

    /**
     * Activity bundle key for Object ID.
     */
    public static final String BUNDLE_ID_KEY = "id";

    //
    // UI
    //
    /**
     * Name edit text.
     */
    private EditText _nameEditText;

    /**
     * Save button.
     */
    private Button _saveButton;

    /**
     * Cancel button.
     */
    private Button _cancelButton;

    //
    // Components
    //
    /**
     * Updating thread.
     */
    private Thread _threadUpdating;

    /**
     * Progress dialog.
     */
    private ProgressDialog _progressDialog;

    /**
     * Message handler.
     */
    private Handler _handler;

    /**
     * Object being shown.
     */
    private T _obj;

    /**
     * Calling Activity context.
     */
    private Context _context;

    //
    // Extension methods
    //
    /**
     * Base class is requesting the ID of this content's view.
     *
     * @return Content view's ID
     */
    protected abstract int getContentViewId();

    /**
     * Base class is requesting the default text being edited.
     *
     * @return Default text being edited
     */
    protected abstract String getDefaultText();

    /**
     * Base class is requesting the text for the Save button (eg, Save or Add).
     *
     * @return Text for the save button.
     */
    protected abstract int getSaveButtonString();

    /**
     * Base class is requesting the text to display during the API call.
     *
     * @return Text to display during the API call
     */
    protected abstract int getUpdatingString();

    /**
     * Base class is requesting the string to track the screen view for analytics.
     *
     * @return Page view string
     */
    protected abstract String getStringScreenName();

    /**
     * Base class is requesting the specified object by ID.
     *
     * @param id Object ID to load
     *
     * @return Object to load
     */
    protected abstract T loadObj(long id);

    /**
     * Base class is asking if the object has changes to save.
     *
     * @return True if the object has changes to save.
     */
    protected abstract boolean hasChangesToSave();

    /**
     * Base class is asking for changes to be saved.
     *
     * @return True if changes were saved
     */
    protected abstract boolean makeChanges();

    /**
     * Implemented by subclasses.
     *
     * Called at the beginning of this class' onCreate.
     *
     * After this, loadObj() will be called to set the object.
     *
     * @param savedInstance Saved Bundle
     */
    protected abstract void onCreateInternalPre(final Bundle savedInstance);

    /**
     * Implemented by subclasses.
     *
     * Called at the end of this class' onCreate.
     *
     * In this, getObj() is guaranteed to be set.
     *
     * @param savedInstance Saved Bundle
     */
    protected abstract void onCreateInternalPost(final Bundle savedInstance);

    @Override
    public final void onCreate(final Bundle savedInstanceState)
    {
        long startTime = System.currentTimeMillis();

        super.onCreate(savedInstanceState);

        _context = this;

        setContentView(getContentViewId());

        // call children's oncreate that runs before view creation
        onCreateInternalPre(savedInstanceState);

        // get the intended object
        Bundle bundle = this.getIntent().getExtras();
        long id = bundle.getLong(BUNDLE_ID_KEY);
        _obj = loadObj(id);

        //
        // Inflation
        //
        _nameEditText = (EditText) findViewById(R.id.dialog_edit_value);
        _nameEditText.setText(getDefaultText());

        _saveButton = (Button) findViewById(R.id.dialog_save);
        _saveButton.setOnClickListener(_saveButtonListener);
        _saveButton.setText(getSaveButtonString());

        _cancelButton = (Button) findViewById(R.id.dialog_cancel);
        _cancelButton.setOnClickListener(_cancelButtonListener);

        setupHandler();

        // call children's oncreate that runs after view creation
        onCreateInternalPost(savedInstanceState);

        ServiceLocator.tracker().trackScreenStartup(getStringScreenName(), startTime);
    }

    /**
     * Sets up the message handler.
     */
    final void setupHandler()
    {
        if (_handler != null)
        {
            return;
        }

        _handler = new Handler() {
            @Override
            public void handleMessage(final Message message)
            {
                switch (message.what)
                {
                    case MESSAGE_API_ERROR:
                        showApiError();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected final void onPause()
    {
        super.onPause();

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
    };

    /**
     * Gets the held object.
     *
     * @return Held object
     */
    protected final T getObj()
    {
        return _obj;
    }

    /**
     * Cancel button on-click listener.
     */
    private final Button.OnClickListener _cancelButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    /**
     * Gets the text in the name field.
     *
     * @return Text in the name field
     */
    protected final String getNameField()
    {
        return _nameEditText.getText().toString();
    }

    /**
     * Returns the name if changed, null if not.
     *
     * @return New name if changed, null if not changed.
     */
    protected final String nameChanges()
    {
        final String newName = getNameField();

        if (!_obj.getText().equals(newName))
        {
            return newName;
        }
        else
        {
            return null;
        }
    }

    /**
     * Save button on-click listener.
     */
    private final Button.OnClickListener _saveButtonListener = new Button.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            // if we don't have any name or other changes, ignore and call this cancelled
            if (nameChanges() == null && !hasChangesToSave())
            {
                setResult(RESULT_CANCELED);
                finish();
                return;
            }

            // setup the progress dialog
            if (_progressDialog != null)
            {
                _progressDialog.cancel();
            }

            setProgressBarIndeterminateVisibility(true);

            _progressDialog = ProgressDialog.show(_context,
                                                  getString(R.string.please_wait),
                                                  getString(getUpdatingString()),
                                                  true);

            _progressDialog.setCancelable(true);

            // create a thread to make changes
            _threadUpdating = new Thread() {
                @Override
                public void run()
                {
                    boolean successful = makeChanges();

                    if (successful)
                    {
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }
                    else
                    {
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

                        _handler.sendEmptyMessage(MESSAGE_API_ERROR);
                    }
                };
            };

            _threadUpdating.start();
        }
    };

    /**
     * Show an API error toast.
     */
    private void showApiError()
    {
        Toast.makeText(_context, R.string.api_error, Toast.LENGTH_LONG)
            .show();
    }
}
