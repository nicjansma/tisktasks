package com.nicjansma.tisktasks.test;

import com.nicjansma.tisktasks.IPreferences;

public final class TestPreferences
    implements IPreferences
{

    private String _email;
    private String _apiToken;
    private Boolean _keepLoggedIn;

    public TestPreferences(final String email, final String apiToken, final Boolean keepLoggedIn)
    {
        setEmail(email);
        setApiToken(apiToken);
        setKeepLoggedIn(keepLoggedIn);
    }

    @Override
    public String email()
    {
        return _email;
    }

    @Override
    public void setEmail(final String email)
    {
        _email = email;
    }

    @Override
    public String apiToken()
    {
        return _apiToken;
    }

    @Override
    public void setApiToken(final String apiKey)
    {
        _apiToken = apiKey;
    }

    @Override
    public boolean keepLoggedIn()
    {
        return _keepLoggedIn;
    }

    @Override
    public void setKeepLoggedIn(final boolean keepLoggedIn)
    {
        _keepLoggedIn = keepLoggedIn;
    }

    @Override
    public boolean analyticsEnabled()
    {
        return false;
    }

}
