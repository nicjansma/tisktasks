package com.nicjansma.tisktasks.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.ServiceLocator;

/**
 * About activity.
 *
 * @author Nic Jansma
 */
public final class AboutActivity
    extends Activity
{
    //
    // Constants
    //
    /**
     * Android Intent.
     */
    public static final String INTENT = "com.nicjansma.tisktasks.action.ABOUT";

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        long startTime = System.currentTimeMillis();

        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.about);

        TextView versionTextView = (TextView) findViewById(R.id.about_app_version);

        Context context = getApplicationContext();
        try
        {
            // set version info
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionTextView.setText(packageInfo.versionName);
        }
        catch (final NameNotFoundException e)
        {
            Log.v(AboutActivity.class.getSimpleName(), e.toString());
        }

        ServiceLocator.tracker().trackScreenStartup("About", startTime);
    }
}