package com.nicjansma.tisktasks;

import android.app.Activity;

import com.nicjansma.tisktasks.models.TodoistUser;

/**
 * User Manager interface.
 */
public interface IUserManager
{
    /**
     * Log in the specified user.
     *
     * @param user Todoist user
     * @param keepUserLoggedIn Keep the user logged in if the app closes
     */
    void login(TodoistUser user, boolean keepUserLoggedIn);

    /**
     * Log the current user out.
     */
    void logout();

    /**
     * Determines if there is a user logged in right now.
     *
     * @return True if there is a logged in user
     */
    boolean isLoggedIn();

    /**
     * Ensure the user is logged in.
     *
     * @param activity Current activity
     */
    void ensureLoggedIn(Activity activity);

    /**
     * Gets the current user, or null if no user is logged in.
     *
     * @return Current user, or null if no user is logged in
     */
    TodoistUser getCurrentUser();

    /**
     * Sets the current user.
     *
     * @param user Current user
     */
    void setCurrentUser(TodoistUser user);
}
