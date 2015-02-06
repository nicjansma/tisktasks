package com.nicjansma.tisktasks.api;

import com.nicjansma.library.net.JsonResult;


/**
 * Simple Todoist API result (eg. 'OK')
 */
public final class TodoistApiResultSimple
    extends TodoistApiResult
{
    //
    // Privates
    //
    /**
     * True if the API call was successful (eg. 'OK')
     */
    private final Boolean _successful;

    /**
     * Constructor.
     *
     * @param json JSON result
     */
    public TodoistApiResultSimple(final JsonResult json)
    {
        super(json);

        // set the successful bit
        _successful = getJson() != null
                      && getJson().getJsonString() != null
                      && getJson().getJsonString().equals("ok");
    }

    /**
     * Constructor taking a boolean for success.
     *
     * @param successful True if the API result was successful
     */
    public TodoistApiResultSimple(final Boolean successful)
    {
        super(null);

        _successful = successful;
    }

    @Override
    public Boolean successfulInternal()
    {
        return _successful;
    }
}
