package com.nicjansma.tisktasks.test;

import android.test.suitebuilder.annotation.Smoke;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.activities.AccountActivity;

public final class AccountActivityTest
    extends TiskTasksActivityTestBase<AccountActivity>
{
    /**
     * Constructor.
     */
    public AccountActivityTest()
    {
        super(TEST_PACKAGE, AccountActivity.class);
    }

    private EditText getEmailTextValueView()
    {
        return (EditText) getActivity().findViewById(com.nicjansma.tisktasks.R.id.login_email_value);
    }

    private TextView getAccountEmailView()
    {
        return (TextView) getActivity().findViewById(com.nicjansma.tisktasks.R.id.account_email);
    }

    private EditText getPasswordValueTextView()
    {
        return (EditText) getActivity().findViewById(com.nicjansma.tisktasks.R.id.login_password_value);
    }

    private Button getLoginButton()
    {
        return (Button) getActivity().findViewById(com.nicjansma.tisktasks.R.id.login_button);
    }

    private Button getLogoutButton()
    {
        return (Button) getActivity().findViewById(com.nicjansma.tisktasks.R.id.account_logout);
    }

    /**
     * Tests the Activity screen when no one is logged in.
     *
     * @throws Exception
     */
    @Smoke
    public void testUserNotLoggedIn() throws Exception
    {
        // no user logged in
        setNoUserLoggedIn();

        setupActivity();

        // sanity check
        assertAccountActivity();

        assertEquals(0, getEmailTextValueView().getText().length());
    }

    /**
     * Tests the Activity screen when someone is logged in.
     *
     * @throws Exception
     */
    @Smoke
    public void testUserLoggedIn() throws Exception
    {
        // no user logged in
        setDefaultLoggedInUser(true);

        setupActivity();

        // sanity check
        assertAccountActivity();

        assertEquals(TestConstants.DEFAULT_USER_EMAIL, getAccountEmailView().getText());
    }

    /**
     * Tests the Activity screen when someone is logged in, but the Keep Logged In was not checked.
     *
     * @throws Exception
     */
    @Smoke
    public void testUserLoggedInNoKeepLoggedIn() throws Exception
    {
        // no user logged in
        setDefaultLoggedInUser(false);

        //
        // HACK: Need to restart application for the "Keep Logged In" logic to run, but
        // we can't do this reliably from Robotium.  Instead, simply re-run the logic here.
        //
        if (ServiceLocator.userManager().isLoggedIn()
            && !ServiceLocator.prefs().keepLoggedIn())
        {
            ServiceLocator.userManager().logout();
        }

        setupActivity();

        // sanity check
        assertAccountActivity();

        assertEquals(TestConstants.DEFAULT_USER_EMAIL.length(), getEmailTextValueView().getText().length());
    }

    @Smoke
    public void testClickLogoutButton() throws Exception
    {
        // validate user logged in first
        testUserLoggedIn();

        // click logout
        getSolo().clickOnView(getLogoutButton());

        // sanity check
        getSolo().waitForView(com.nicjansma.tisktasks.R.id.login_button);
    }

    @Smoke
    public void testClickLoginButtonFailedNoPassword() throws Exception
    {
        // no user logged in
        setNoUserLoggedIn();

        setupActivity();

        // sanity check
        assertAccountActivity();

        // enter name and password
        getSolo().enterText(getEmailTextValueView(), TestConstants.DEFAULT_USER_EMAIL);
        getSolo().enterText(getPasswordValueTextView(), "");

        // log in
        getSolo().clickOnView(getLoginButton());

        // TODO assert popup dialog shown
    }

    private void playLogin(final String response, final int httpCode)
    {
        // no user logged in
        setNoUserLoggedIn();

        // setup activity
        setupActivity();

        // sanity check
        assertAccountActivity();

        // enter name and password
        getSolo().enterText(getEmailTextValueView(), TestConstants.DEFAULT_USER_EMAIL);
        getSolo().enterText(getPasswordValueTextView(), TestConstants.DEFAULT_USER_PASSWORD);

        // set mock response
        getFetcher().setResponse(response, httpCode);

        // click on login
        getSolo().clickOnView(getLoginButton());
    }

    @Smoke
    public void testClickLoginButtonSuccess() throws Exception
    {
        playLogin(getTestResources().getString(R.string.json_user_login), 200);

        // should be on main activity
        assertMainActivity();
    }

    @Smoke
    public void testClickLoginButtonFailedBadPassword() throws Exception
    {
        // TOOD
        playLogin(getTestResources().getString(R.string.api_login_error), 200);

        // TODO assert bad pwd dialog

        // should be on same activity
        assertAccountActivity();
    }

    @Smoke
    public void testClickLoginButtonFailedUnknownMessage() throws Exception
    {
        playLogin(getTestResources().getString(R.string.api_unknown), 200);

        // TODO assert bad pwd dialog

        // should be on same activity
        assertAccountActivity();
    }

    @Smoke
    public void testClickLoginButtonFailedApiError() throws Exception
    {
        // TOOD
        playLogin(getTestResources().getString(R.string.json_user_login), 200);

        // TODO assert API error dialog

        // should be on same activity
        assertAccountActivity();
   }

}