package com.nicjansma.tisktasks;

import android.app.Activity;
import android.content.Intent;

import com.nicjansma.tisktasks.activities.AccountActivity;
import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * User manager.
 */
public final class UserManager
    implements IUserManager
{
    /**
     * Current Todoist user.
     */
    private TodoistUser _user = null;

    @Override
    public void login(final TodoistUser user, final boolean keepUserLoggedIn)
    {
        // update preferences
        ServiceLocator.prefs().setEmail(user.getEmail());
        ServiceLocator.prefs().setApiToken(user.getApiToken());
        ServiceLocator.prefs().setKeepLoggedIn(keepUserLoggedIn);

        // update API token
        ServiceLocator.todoistApi().setToken(user.getApiToken());

        setCurrentUser(user);
    }

    @Override
    public void logout()
    {
        // clear user's API token
        ServiceLocator.prefs().setApiToken("");

        // clear the API token
        ServiceLocator.todoistApi().setToken("");

        // clear the current user
        setCurrentUser(null);

        // clear the cache
        ServiceLocator.cache().clearAll();
    }

    @Override
    public boolean isLoggedIn()
    {
        return (ServiceLocator.prefs().apiToken().compareTo("") != 0
                && getCurrentUser() != null);
    }

    @Override
    public void ensureLoggedIn(final Activity activity)
    {
        //
        // If there is no API token yet, we need to show the login screen
        //
        if (!isLoggedIn())
        {
            Intent intent = new Intent(AccountActivity.INTENT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            activity.startActivity(intent);
        }
    }

    @Override
    public TodoistUser getCurrentUser()
    {
        // load from the object cache
        if (_user == null)
        {
            _user = ServiceLocator.cache().getCurrentUser();
        }

        return _user;
    }

    @Override
    public void setCurrentUser(final TodoistUser user)
    {
        _user = user;

        if (_user != null)
        {
            ServiceLocator.cache().setCurrentUser(user);
        }
    }
}
