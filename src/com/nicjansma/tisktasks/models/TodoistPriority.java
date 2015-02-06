package com.nicjansma.tisktasks.models;

import android.graphics.Color;

/**
 * Todoist item priority.
 */
public abstract class TodoistPriority
{
    //
    // Constants
    //
    /**
     * Highest priority.
     */
    public static final int HIGHEST = 4;

    /**
     * Lowest priority.
     */
    public static final int LOWEST = 1;

    /**
     * Default priority.
     */
    public static final int DEFAULT = LOWEST;

    /**
     * Priority 1.
     */
    public static final int PRIORITY_1 = 1;

    /**
     * Priority 2.
     */
    public static final int PRIORITY_2 = 2;

    /**
     * Priority 3.
     */
    public static final int PRIORITY_3 = 3;

    /**
     * Priority 4.
     */
    public static final int PRIORITY_4 = 4;

    /**
     * Priority 1 color (black).
     */
    public static final int PRIORITY_1_COLOR = Color.rgb(0, 0, 0);

    /**
     * Priority 2 color (green).
     */
    public static final int PRIORITY_2_COLOR = Color.rgb(0, 0x88, 0);

    /**
     * Priority 3 color (blue).
     */
    public static final int PRIORITY_3_COLOR = Color.rgb(0, 0x79, 0xb5);

    /**
     * Priority 4 color (red).
     */
    public static final int PRIORITY_4_COLOR = Color.rgb(0xff, 0, 0);

    /**
     * Gets the color of a priority.
     *
     * @param priority Task priority
     *
     * @return Color of the priority
     */
    public static int getColorFromPriority(final int priority)
    {
        switch (priority)
        {
            case PRIORITY_1:
                return PRIORITY_1_COLOR;

            case PRIORITY_2:
                return PRIORITY_2_COLOR;

            case PRIORITY_3:
                return PRIORITY_3_COLOR;

            case PRIORITY_4:
            default:
                return PRIORITY_4_COLOR;
        }
    }
}
