package com.nicjansma.tisktasks.test;

import android.test.suitebuilder.annotation.Smoke;

import com.nicjansma.tisktasks.activities.AboutActivity;

/**
 * About Activity test cases.
 */
public final class AboutActivityTest extends TiskTasksActivityTestBase<AboutActivity>
{
    /**
     * Constructor.
     */
    public AboutActivityTest()
    {
        super(TEST_PACKAGE, AboutActivity.class);
    }

    /**
     * Tests the about screen.
     *
     * @throws Exception
     */
    @Smoke
    public void testAbout() throws Exception
    {
        setupActivity();

        // sanity check
        assertAboutActivity();
    }

}
