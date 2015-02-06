package com.nicjansma.tisktasks.models;

import java.util.Locale;

/**
 * Todoist Colors.
 */
public abstract class TodoistColor
{
    //
    // Constants
    //
    /**
     * Default color.
     */
    public static final String DEFAULT_COLOR = "#BDE876";

    /**
     * Default color index.
     */
    public static final int DEFAULT_COLOR_INDEX = 0;

    /**
     * Number of colors.
     */
    public static final int COLOR_COUNT = 12;

    /**
     * Color: green.
     */
    public static final int COLOR_GREEN = 0;

    /**
     * Color: red.
     */
    public static final int COLOR_RED = 1;

    /**
     * Color: orange.
     */
    public static final int COLOR_ORANGE = 2;

    /**
     * Color: yellow.
     */
    public static final int COLOR_YELLOW = 3;

    /**
     * Color: light blue.
     */
    public static final int COLOR_LIGHT_BLUE = 4;

    /**
     * Color: dark grey.
     */
    public static final int COLOR_DARK_GREY = 5;

    /**
     * Color: purple.
     */
    public static final int COLOR_PURPLE = 6;

    /**
     * Color: light grey.
     */
    public static final int COLOR_LIGHT_GREY = 7;

    /**
     * Color: salmon.
     */
    public static final int COLOR_SALMON = 8;

    /**
     * Color: dark orange.
     */
    public static final int COLOR_DARK_ORANGE = 9;

    /**
     * Color: teal.
     */
    public static final int COLOR_TEAL = 10;

    /**
     * Color: aqua.
     */
    public static final int COLOR_AQUA = 11;

    /**
     * Gets a color string from an index.
     *
     * @param colorIndex Color index
     *
     * @return Color string
     */
    public static String getColorStringFromIndex(final int colorIndex)
    {
        switch (colorIndex)
        {
            case COLOR_GREEN:
                return "#BDE876";
            case COLOR_RED:
                return "#FF8581";
            case COLOR_ORANGE:
                return "#FFC472";
            case COLOR_YELLOW:
                return "#FAED75";
            case COLOR_LIGHT_BLUE:
                return "#A8C9E5";
            case COLOR_DARK_GREY:
                return "#D2B8A4";
            case COLOR_PURPLE:
                return "#E3A8E5";
            case COLOR_LIGHT_GREY:
                return "#DDDDDD";
            case COLOR_SALMON:
                return "#FC896F";
            case COLOR_DARK_ORANGE:
                return "#FFCC00";
            case COLOR_TEAL:
                return "#74E8D4";
            case COLOR_AQUA:
                return "#3CD6FC";

            // unknown
            default:
                return DEFAULT_COLOR;
        }
    }

    /**
     * Gets a color index from a string.
     *
     * @param color Color string
     *
     * @return Color index
     */
    public static int getColorIndexFromString(final String color)
    {
        String colorUpper = color.toUpperCase(Locale.US);

        if (colorUpper.equals("#BDE876"))
        {
            return COLOR_GREEN;
        }
        else if (colorUpper.equals("#FF8581"))
        {
            return COLOR_RED;
        }
        else if (colorUpper.equals("#FFC472"))
        {
            return COLOR_ORANGE;
        }
        else if (colorUpper.equals("#FAED75"))
        {
            return COLOR_YELLOW;
        }
        else if (colorUpper.equals("#A8C9E5"))
        {
            return COLOR_LIGHT_BLUE;
        }
        else if (colorUpper.equals("#D2B8A4"))
        {
            return COLOR_DARK_GREY;
        }
        else if (colorUpper.equals("#999999"))
        {
            // both are used
            return COLOR_DARK_GREY;
        }
        else if (colorUpper.equals("#E3A8E5"))
        {
            return COLOR_PURPLE;
        }
        else if (colorUpper.equals("#DDDDDD"))
        {
            return COLOR_LIGHT_GREY;
        }
        else if (colorUpper.equals("#FC896F")
                 || colorUpper.equals("#FC603C"))
        {
            return COLOR_SALMON;
        }
        else if (colorUpper.equals("#FFCC00"))
        {
            return COLOR_DARK_ORANGE;
        }
        else if (colorUpper.equals("#74E8D4"))
        {
            return COLOR_TEAL;
        }
        else if (colorUpper.equals("#3CD6FC"))
        {
            return COLOR_AQUA;
        }
        else
        {
            // unknown
            return DEFAULT_COLOR_INDEX;
        }
    }
}
