package com.nicjansma.tisktasks;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nicjansma.tisktasks.activities.AboutActivity;
import com.nicjansma.tisktasks.activities.AccountActivity;
import com.nicjansma.tisktasks.activities.AppPreferencesActivity;

/**
 * Options menu to be used by all TiskTasks' screens.
 */
public abstract class AppOptionsMenu
{
    /**
     * Private constructor.
     */
    private AppOptionsMenu()
    {
    }

    /**
     * Called when an options item is selected.
     *
     * @param activity Android activity
     * @param item Selected item
     *
     * @return True if the option was handled
     */
    public static boolean onOptionsItemSelected(final Activity activity, final MenuItem item)
    {
        Intent intent = null;

        switch (item.getItemId())
        {
            case R.id.preferences:
                intent = new Intent(AppPreferencesActivity.INTENT);
                activity.startActivity(intent);
                return true;

            case R.id.account:
                intent = new Intent(AccountActivity.INTENT);
                activity.startActivity(intent);
                return true;

            case R.id.about:
                intent = new Intent(AboutActivity.INTENT);
                activity.startActivity(intent);
                return true;

            default:
                return false;
        }
    }

    /**
     * Called when the options menu is created.
     *
     * @param activity Android activity
     * @param menu Menu to inflate
     *
     * @return True if creation was successful
     */
    public static boolean onCreateOptionsMenu(final Activity activity, final Menu menu)
    {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }
}
