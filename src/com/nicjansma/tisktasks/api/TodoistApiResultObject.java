package com.nicjansma.tisktasks.api;

import com.nicjansma.library.net.JsonResultObject;


/**
 * JSON result containing a single object.
 *
 * @param <T> Object type
 */
public final class TodoistApiResultObject<T>
    extends TodoistApiResult
{
    //
    // Privates
    //
    /**
     * Result object.
     */
    private final T _obj;

    /**
     * Constructor.
     *
     * @param json JSON result object
     */
    public TodoistApiResultObject(final JsonResultObject<T> json)
    {
        super(json);

        _obj = json.getObject();
    }

    @Override
    public Boolean successfulInternal()
    {
        return _obj != null;
    }

    /**
     * Gets the result object.
     *
     * @return Result object
     */
    public T getObject()
    {
        return _obj;
    }
}
