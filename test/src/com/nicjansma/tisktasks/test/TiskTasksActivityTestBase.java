package com.nicjansma.tisktasks.test;

import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.nicjansma.library.net.MockHttpFetcher;
import com.nicjansma.tisktasks.ServiceLocator;
import com.nicjansma.tisktasks.models.TodoistUser;
import com.robotium.solo.Solo;

public abstract class TiskTasksActivityTestBase<T extends Activity>
    extends ActivityInstrumentationTestCase2<T>
{
    //
    // constants
    //

    //
    // defaults
    //
    public static final int DEFAULT_WAIT = 2000;

    //
    // package
    //
    public static final String TEST_PACKAGE = "com.nicjansma.tisktasks";

    //
    // activities
    //
    public static final String ACTIVITY_MAIN = "MainActivity";
    public static final String ACTIVITY_ABOUT = "AboutActivity";
    public static final String ACTIVITY_ACCOUNT = "AccountActivity";

    //
    // locals
    //
    /**
     * Robotium
     */
    private com.robotium.solo.Solo _solo;

    /**
     * Mock HTTP fetcher.
     */
    private MockHttpFetcher _fetcher;

    /**
     * Test resources.
     */
    private Resources _testResources;

    /**
     * Constructor
     *
     * @param pkg Package
     * @param activityClass Activity class
     */
    public TiskTasksActivityTestBase(final String pkg, final Class<T> activityClass)
    {
        super(pkg, activityClass);
    }

    /**
     * Gets the test application's resources.
     *
     * @return Test application resources
     */
    protected final Resources getTestResources()
    {
        if (_testResources == null)
        {
            _testResources = getInstrumentation().getContext().getResources();
        }

        return _testResources;
    }

    @Override
    public final void setUp() throws Exception
    {
        resetState();
    }

    @Override
    public final void tearDown() throws Exception
    {
        resetState();

        try
        {
            _solo.finishOpenedActivities();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        getActivity().finish();
        super.tearDown();
    }

    /**
     * Sets up the activity prior to test
     */
    public final void setupActivity()
    {
        _solo = new Solo(getInstrumentation(), getActivity());

        // ensure no real HTTP requests get out
        _fetcher = new com.nicjansma.library.net.MockHttpFetcher();
        com.nicjansma.library.net.HttpUtils.setHttpFetcher(_fetcher);
    }

    /**
     * Resets the application to the beginning state
     */
    private void resetState()
    {
    }

    /**
     * Asserts the Main Activity is correct.
     *
     * @throws Exception
     */
    protected final void assertMainActivity() throws Exception
    {
        _solo.assertCurrentActivity(
            getActivity().getResources().getString(com.nicjansma.tisktasks.R.string.app_name),
            ACTIVITY_MAIN);
    }

    /**
     * Asserts the About Activity is correct.
     *
     * @throws Exception
     */
    protected final void assertAboutActivity() throws Exception
    {
        _solo.assertCurrentActivity(
            getActivity().getResources().getString(com.nicjansma.tisktasks.R.string.title_about),
            ACTIVITY_ABOUT);
    }

    /**
     * Asserts the Account Activity is correct.
     *
     * @throws Exception
     */
    protected final void assertAccountActivity()
    {
        _solo.assertCurrentActivity(
            getActivity().getResources().getString(com.nicjansma.tisktasks.R.string.title_account),
            ACTIVITY_ACCOUNT);
    }

    /**
     * @return Robotium Solo.
     */
    public final Solo getSolo()
    {
        return _solo;
    }

    protected final void setPrefs(final String email, final String apiToken, final Boolean keepLoggedIn)
    {
        ServiceLocator.setPrefs(new TestPreferences(email, apiToken, keepLoggedIn));
    }

    protected final void setNoUserLoggedIn()
    {
        setPrefs("", "", false);
        ServiceLocator.userManager().logout();
    }

    protected final void setDefaultLoggedInUser(final Boolean keepLoggedIn)
    {
        JSONObject jsonObject = null;
        try
        {
            String jsonString = getTestResources().getString(R.string.json_user_login);
            jsonObject = new JSONObject(jsonString);
        }
        catch (final JSONException e)
        {
            Log.e("Test", "Could not convert to JSON object.", e);
        }

        TodoistUser user = new TodoistUser();
        user.initialize(jsonObject);
        ServiceLocator.userManager().login(user, keepLoggedIn);
    }

    protected final MockHttpFetcher getFetcher()
    {
        return _fetcher;
    }
}
