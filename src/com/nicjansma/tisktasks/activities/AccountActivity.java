package com.nicjansma.tisktasks.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.api.TodoistApiResultObject;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * Account activity.
 *
 * If there is a logged in user, show their info.
 *
 * Otherwise, show the login screen.
 */
public final class AccountActivity
    extends Activity
{
    /**
     * Class tag (for debugging).
     */
    private static final String TAG = AccountActivity.class.getSimpleName();

    //
    // Constants
    //
    /**
     * This activity's intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.ACCOUNT";

    /**
     * Dialog: Login failed: Incorrect Password.
     */
    private static final int DIALOG_LOGIN_FAILED_INCORRECT_PASSWORD = 0;

    /**
     * Dialog: Login failed: Connection failure.
     */
    private static final int DIALOG_LOGIN_FAILED_CONNECTION_FAILURE = 1;

    /**
     * Dialog: Login failed: No password.
     */
    private static final int DIALOG_LOGIN_FAILED_NO_PASSWORD = 2;

    /**
     * Result: Incorrect Password.
     */
    private static final int RESULT_INCORRECT_PASSWORD = RESULT_FIRST_USER;

    /**
     * Result: Connection failure.
     */
    private static final int RESULT_CONNECTION_FAILURE = RESULT_FIRST_USER + 1;

    //
    // UI
    //
    /**
     * Login button.
     */
    private Button _loginButton;

    /**
     * Email text box.
     */
    private EditText _loginEmail;

    /**
     * Password text box.
     */
    private EditText _loginPassword;

    /**
     * Keep Logged In checkbox.
     */
    private CheckBox _loginKeepLoggedIn;

    /**
     * Logout button.
     */
    private Button _accountLogoutButton;

    /**
     * Email text view.
     */
    private TextView _accountEmail;

    //
    // Components
    //
    /**
     * Login thread.
     */
    private Thread _threadLogin;

    /**
     * Login message handler.
     */
    private Handler _handlerLogin;

    /**
     * Progress dialog.
     */
    private ProgressDialog _progressDialog;

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupView();
        setupHandler();
    }

    /**
     * Sets up the view.
     *
     * If the user is not logged in, show the login screen.
     *
     * If the user is logged in, show who is logged in and allow them to log out.
     */
    private void setupView()
    {
        long startTime = System.currentTimeMillis();

        String userEmail = ServiceLocator.prefs().email();

        //
        // If there is no API token yet, we need to show the login screen
        //
        if (!ServiceLocator.userManager().isLoggedIn())
        {
            setContentView(R.layout.login);

            //
            // setup layout
            //
            _loginButton = (Button) findViewById(R.id.login_button);
            _loginEmail = (EditText) findViewById(R.id.login_email_value);
            _loginPassword = (EditText) findViewById(R.id.login_password_value);
            _loginKeepLoggedIn = (CheckBox) findViewById(R.id.login_keep_logged_in);

            if (userEmail.compareTo("") != 0)
            {
                _loginEmail.setText(userEmail);
            }

            //
            // onClick handlers
            //
            _loginButton.setOnClickListener(_loginButtonOnClickHandler);

            ServiceLocator.tracker().trackScreenStartup("Account/Login", startTime);
        }
        else
        {
            setContentView(R.layout.account);

            //
            // setup layout
            //
            _accountLogoutButton = (Button) findViewById(R.id.account_logout);
            _accountEmail = (TextView) findViewById(R.id.account_email);

            _accountEmail.setText(userEmail);

            //
            // onClick handlers
            //
            _accountLogoutButton.setOnClickListener(_logoutButtonOnClickHandler);

            ServiceLocator.tracker().trackScreenStartup("Account/View", startTime);
        }
    }

    /**
     * Login Button on-click handler.
     */
    private final Button.OnClickListener _loginButtonOnClickHandler = new Button.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            login();
        }
    };

    /**
     * Logout Button on-click handler.
     */
    private final Button.OnClickListener _logoutButtonOnClickHandler = new Button.OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            logout();
        }
    };

    /**
     * Sets up the login handler.
     */
    private void setupHandler()
    {
        if (_handlerLogin != null)
        {
            return;
        }

        _handlerLogin = new Handler() {
            @Override
            public void handleMessage(final Message message)
            {
                // dismiss the progress dialog
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

                switch (message.what)
                {
                    case RESULT_INCORRECT_PASSWORD:
                        showDialog(DIALOG_LOGIN_FAILED_INCORRECT_PASSWORD);
                        break;

                    case RESULT_CONNECTION_FAILURE:
                        showDialog(DIALOG_LOGIN_FAILED_CONNECTION_FAILURE);
                        break;

                    default:
                    case RESULT_OK:
                        // show login result
                        if (ServiceLocator.userManager().isLoggedIn())
                        {
                            Intent intent = new Intent(MainActivity.INTENT);
                            startActivity(intent);
                        }
                        else
                        {
                            showDialog(DIALOG_LOGIN_FAILED_INCORRECT_PASSWORD);
                        }
                        break;
                }
            }
        };
    }

    /**
     * Attempts to log the user in.
     */
    private void login()
    {
        if (_loginPassword.getText().length() == 0)
        {
            showDialog(DIALOG_LOGIN_FAILED_NO_PASSWORD);
            return;
        }

        // prepare progress dialog
        if (_progressDialog != null)
        {
            _progressDialog.cancel();
        }

        setProgressBarIndeterminateVisibility(true);

        _progressDialog = ProgressDialog.show(this,
                                              getString(R.string.please_wait),
                                              getString(R.string.logging_in),
                                              true);

        _progressDialog.setCancelable(true);

        // create login thread
        _threadLogin = new Thread() {
            @Override
            public void run()
            {
                String email = _loginEmail.getText().toString();
                String password = _loginPassword.getText().toString();
                boolean keepUserLoggedIn = _loginKeepLoggedIn.isChecked();

                int message = RESULT_OK;

                // try to log the user in
                TodoistApiResultObject<TodoistUser> userResult
                    = ServiceLocator.todoistApi().login(email, password);

                // log the user in if successful
                if (userResult.successful())
                {
                    TodoistUser user = userResult.getObject();
                    ServiceLocator.userManager().login(user, keepUserLoggedIn);
                }
                else
                {
                    if (userResult.hadConnectionFailure())
                    {
                        message = RESULT_CONNECTION_FAILURE;
                    }
                    else if (userResult.getError() != null
                            && userResult.getError().equals(R.string.api_login_error))
                    {
                        message = RESULT_INCORRECT_PASSWORD;
                    }
                    else
                    {
                        message = RESULT_INCORRECT_PASSWORD;
                    }

                    // TODO part of TodoistApi.apiUrl
                    // TODO note connection vs. password failure
                    ServiceLocator.tracker().trackEvent("API-Error", "login", "", 0);
                }

                _handlerLogin.sendEmptyMessage(message);
            };
        };

        _threadLogin.start();
    }

    /**
     * Log the user out.
     */
    private void logout()
    {
        ServiceLocator.userManager().logout();
        setupView();
    }

    @Override
    protected Dialog onCreateDialog(final int id)
    {
        Dialog dialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.login_failed)
            .setPositiveButton(android.R.string.ok, null);

        switch (id)
        {
            case DIALOG_LOGIN_FAILED_CONNECTION_FAILURE:
                builder.setMessage(R.string.unable_to_login_connection_failure);
                break;

            case DIALOG_LOGIN_FAILED_INCORRECT_PASSWORD:
                builder.setMessage(R.string.unable_to_login_incorrect_password);
                break;

            case DIALOG_LOGIN_FAILED_NO_PASSWORD:
                builder.setMessage(R.string.unable_to_login_no_password);
                break;

            default:
                dialog = null;
        }

        dialog = builder.create();

        return dialog;
    }
}
