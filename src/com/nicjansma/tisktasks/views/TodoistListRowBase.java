package com.nicjansma.tisktasks.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nicjansma.tisktasks.R;
import com.nicjansma.tisktasks.activities.TodoistListActivityBase;
import com.nicjansma.tisktasks.models.TodoistObjectBase;

/**
 * List row base class.
 *
 * @param <T> Todoist object type
 */
public abstract class TodoistListRowBase<T extends TodoistObjectBase>
    extends LinearLayout
{
    //
    // Extension methods
    //
    /**
     * Gets the text for a row.
     *
     * @return Text for a row
     */
    public abstract String getText();

    /**
     * Called by the base class to update its view with a new object.
     *
     * @param obj New object
     */
    public abstract void updateInternal(T obj);

    //
    // UI
    //
    /**
     * Row image.
     */
    private final ImageView _expandImage;

    /**
     * Contained Todoist object.
     */
    private T _obj;

    /**
     * Current view.
     */
    private final View _view;

    /**
     * ListActivity base.
     */
    private final TodoistListActivityBase<T> _baseListActivity;

    /**
     * Whether or not to indent objects.
     */
    private final boolean _indent;

    /**
     * Constructor.
     *
     * @param context Activity context
     * @param inflater Cached inflater
     * @param resLayout Resource layout ID
     * @param indent Whether to not to indent objects.
     */
    @SuppressWarnings("unchecked")
    public TodoistListRowBase(
        final Context context,
        final LayoutInflater inflater,
        final int resLayout,
        final boolean indent)
    {
        super(context);

        _baseListActivity = (TodoistListActivityBase<T>) context;

        _view = inflater.inflate(resLayout, null);

        final View listLine = _view.findViewById(R.id.list_line);
        listLine.setOnClickListener(_onClickListenerRow);

        _expandImage = (ImageView) _view.findViewById(R.id.expand_image);
        _expandImage.setOnClickListener(_onClickListenerExpand);

        _indent = indent;

        addView(_view);
    }

    /**
     * Listener for a long-click on a row.
     */
    private final OnLongClickListener _onLongClickListenerRow = new OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v)
        {
            showContextMenuForChild(v);

            return true;
        }
    };

    /**
     * Gets the on long-click listener.
     *
     * @return On long-click listener
     */
    protected final OnLongClickListener getOnLongClickListener()
    {
        return _onLongClickListenerRow;
    }

    /**
     * Listener for a click on a row.
     */
    private final OnClickListener _onClickListenerRow = new OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            _baseListActivity.rowClick(_obj);
        }
    };

    /**
     * Gets the on-click listener.
     *
     * @return On-click listener
     */
    protected final OnClickListener getOnClickListener()
    {
        return _onClickListenerRow;
    }

    /**
     * Listener for the expand/collapse button for a row.
     */
    private final OnClickListener _onClickListenerExpand = new OnClickListener() {
        @Override
        public void onClick(final View v)
        {
            _baseListActivity.expandClick(_obj);
        }
    };

    /**
     * Listener for the checkbox for a row.
     */
    private final CompoundButton.OnClickListener _onClickListenerCheckbox = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(final View view)
        {
            _baseListActivity.checkboxClick(_obj);
        }
    };

    /**
     * Gets the checkbox on-click listener.
     *
     * @return Checkbox on-click listener
     */
    protected final OnClickListener getCheckboxOnClickListener()
    {
        return _onClickListenerCheckbox;
    }

    /**
     * Updates a row with the new object.
     *
     * @param obj New object to show on the row
     */
    public final void update(final T obj)
    {
        _obj = obj;

        // margin
        LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layout.setMargins(_indent ? _obj.getIndentInPixels(_baseListActivity.getResources().getDisplayMetrics().density) : 0, 0, 0, 0);

        if (_obj.isAnyParentCollapsed())
        {
            layout.height = 0;
            layout.width = 0;
            _view.setLayoutParams(layout);
        }

        updateExpandImage();

        updateViewLayout(_view, layout);

        updateInternal(obj);
    }

    /**
     * Updates the expanded/collapsed image for a row.
     */
    private void updateExpandImage()
    {
        //
        // image
        //
        if (_indent && _obj.isParent())
        {
            int resIdImage;

            if (_obj.isCollapsed())
            {
                resIdImage = R.drawable.arrow_up;
            }
            else
            {
                resIdImage = R.drawable.arrow_down;
            }

            _expandImage.setVisibility(View.VISIBLE);
            _expandImage.setImageResource(resIdImage);
        }
        else
        {
            _expandImage.setVisibility(View.GONE);
        }
    }

    /**
     * Gets the object within the row.
     *
     * @return Object
     */
    public final T getObj()
    {
        return _obj;
    }

    /**
     * Gets the row's view.
     *
     * @return Row's view
     */
    public final View getView()
    {
        return _view;
    }
}
