package com.nicjansma.tisktasks.views;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.models.TodoistColor;

/**
 * Color Selector Grid Adapter.
 *
 * Shows a grid of colors that the user can select.
 */
public final class ColorSelectorGridAdapter
    extends BaseAdapter
{
    //
    // Constants
    //
    /**
     * Color grid size.
     */
    private static final int COLOR_SIZE = 35;

    /**
     * Color grid padding.
     */
    private static final int COLOR_PADDING = 5;

    //
    // members
    //
    /**
     * Grid context.
     */
    private final Activity _activity;

    /**
     * Currently selected color.
     */
    private Integer _selectedColor = 0;

    /**
     * ColorSelectorGridAdapter constructor.
     *
     * @param activity Activity context
     * @param selectedColor Starting color
     */
    public ColorSelectorGridAdapter(final Activity activity, final int selectedColor)
    {
        _activity = activity;
        _selectedColor = selectedColor;
    }

    @Override
    public int getCount()
    {
        return TodoistColor.COLOR_COUNT;
    }

    @Override
    public Object getItem(final int position)
    {
        return null;
    }

    @Override
    public long getItemId(final int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent)
    {
        ImageButton imageButton;

        // if the view is new, inflate it
        if (convertView == null)
        {
            int sizePx = (int) (COLOR_SIZE * _activity.getResources().getDisplayMetrics().density);

            //
            // layout
            //
            imageButton = new ImageButton(_activity);

            imageButton.setLayoutParams(new GridView.LayoutParams(sizePx, sizePx));
        }
        else
        {
            imageButton = (ImageButton) convertView;
        }

        // set the background color
        int backgroundColor = Color.parseColor(TodoistColor.getColorStringFromIndex(position));
        imageButton.setBackgroundColor(backgroundColor);
        imageButton.setTag(position);

        // if selected, set the checkmark
        imageButton.setImageResource((_selectedColor == position) ? R.drawable.checkmark : 0);
        imageButton.setPadding(0, COLOR_PADDING, 0, 0);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v)
            {
                _selectedColor = position;
                notifyDataSetChanged();
            }
        });

        return imageButton;
    }

    /**
     * Gets the current color index.
     *
     * @return Current color index.
     */
    public Integer getColorIndex()
    {
        return _selectedColor;
    }

}
