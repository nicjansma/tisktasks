package com.nicjansma.tisktasks.api;

import java.util.ArrayList;

import com.nicjansma.library.net.JsonResultArray;

/**
 * A JSON result that holds an array.
 *
 * @param <T> Type of objects in the array
 */
public final class TodoistApiResultArray<T>
    extends TodoistApiResult
{
    //
    // Privates
    //
    /**
     * JSON result array.
     */
    private final JsonResultArray<T> _array;

    /**
     * Constructor taking a JsonResultArray.
     *
     * @param json JSON result array
     */
    public TodoistApiResultArray(final JsonResultArray<T> json)
    {
        super(json);

        _array = json;
    }

    /**
     * Constructor taking an ArrayList.
     *
     * @param array Array
     */
    public TodoistApiResultArray(final ArrayList<T> array)
    {
        super(null);

        _array = new JsonResultArray<T>();
        _array.setArray(array);
    }

    /**
     * Gets the JSON result array.
     *
     * @return JSON result array
     */
    public JsonResultArray<T> getObject()
    {
        return _array;
    }

    /**
     * Gets the object array.
     *
     * @return Object array
     */
    public ArrayList<T> getArray()
    {
        return _array.getArray();
    }

    @Override
    public Boolean successfulInternal()
    {
        return _array != null;
    }

}
