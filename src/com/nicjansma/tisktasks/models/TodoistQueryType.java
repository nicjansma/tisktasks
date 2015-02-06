package com.nicjansma.tisktasks.models;

import android.util.Log;
import android.util.SparseArray;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Todoist query types
 *
 * @author Nic Jansma
 */

/**
 * Todoist query type.
 */
public enum TodoistQueryType
{
    /**
     * Date query.
     */
    date(0),

    /**
     * Overdue.
     */
    overdue(1),

    /**
     * Priority.
     */
    priority(2);

    // TODO more types

    /**
     * Class tag (for debugging).
     */
    private static final String TAG = TodoistQueryType.class.getSimpleName();

    //
    // statics
    //
    /**
     * Code (0, 1) to enum lookup.
     */
    private static SparseArray<TodoistQueryType> _lookupCode = new SparseArray<TodoistQueryType>();

    /**
     * String (date, overdue, etc) to enum lookup.
     */
    private static Map<String, TodoistQueryType> _lookupString = new HashMap<String, TodoistQueryType>();

    //
    // members
    //
    /**
     * Code.
     */
    private int _code;

    /**
     * TodoistQueryType constructor.
     *
     * @param code From code
     */
    private TodoistQueryType(final int code)
    {
        _code = code;
    }

    /**
     * @return Enum code
     */
    public int getCode()
    {
        return _code;
    }

    /**
     * Get the TodoistQueryType from a code.
     *
     * @param code Code (0, 1)
     * @return TodoistQueryType
     */
    public static TodoistQueryType get(final int code)
    {
        return _lookupCode.get(code);
    }

    /**
     * Get the TodoistQueryType from a string.
     *
     * @param code String (US, EUR)
     * @return TodoistQueryType
     */
    public static TodoistQueryType get(final String code)
    {
        if (!_lookupString.containsKey(code))
        {
            Log.e(TAG, "Unknown TodoistQueryType: " + code);
        }

        return _lookupString.get(code);
    }

    //
    // static constructor
    //
    static
    {
        for (TodoistQueryType s : EnumSet.allOf(TodoistQueryType.class))
        {
            _lookupCode.put(s.getCode(), s);
            _lookupString.put(s.toString(), s);
        }
    }
};
