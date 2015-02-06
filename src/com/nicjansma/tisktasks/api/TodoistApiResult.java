package com.nicjansma.tisktasks.api;

import com.nicjansma.library.net.HttpSimpleResponse;
import com.nicjansma.library.net.JsonResult;

/**
 * Simple wrapper class for JsonResult, for other TodoistApi classes to inherit from.
 */
public abstract class TodoistApiResult
{
    //
    // Privates
    //
    /**
     * JSON result.
     */
    private final JsonResult _json;

    //
    // Inheritance
    //
    /**
     * Inheriting class determines if it is successful.
     *
     * @return True if the inheriting class is successful.
     */
    public abstract Boolean successfulInternal();

    /**
     * Constructor.
     *
     * @param json JSON result
     */
    public TodoistApiResult(final JsonResult json)
    {
        _json = json;
    }

    /**
     * Gets the JSON result.
     *
     * @return JSON result
     */
    public final JsonResult getJson()
    {
        return _json;
    }

    /**
     * Determines if the JSON result was successful.
     *
     * @return True if the JSON result is successful
     */
    public final Boolean successful()
    {
        return _json != null && _json.successful() && successfulInternal();
    }

    /**
     * Gets the error string if the JSON result was not successful.
     *
     * @return The error string if the JSON result was not successful, the empty string otherwise
     */
    public final String getError()
    {
        if (!successful())
        {
            return _json.getJsonString();
        }

        // no error
        return "";
    }

    /**
     * Determines if the API result failed due to a connection failure.
     *
     * @return True if there was a connection failure
     */
    public final boolean hadConnectionFailure()
    {
        if (_json != null)
        {
            // grab the HTTP response object
            HttpSimpleResponse response = _json.getHttpResponse();
            if (response != null)
            {
                if (response.getHttpCode() == 0)
                {
                    return true;
                }
            }
        }

        return false;
    }
}
