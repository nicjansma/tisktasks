package com.nicjansma.tisktasks.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nicjansma.tisktasks.models.ITodoistBaseObjectManager;
import com.nicjansma.tisktasks.models.TodoistObjectBase;

/**
 * List adapter base class.
 *
 * @param <T> Todoist object type
 * @param <S> TodoistListRow object type
 */
public abstract class TodoistListAdapterBase<T extends TodoistObjectBase, S extends TodoistListRowBase<T>>
    extends BaseAdapter
{
    //
    // Components
    //
    /**
     * Object manager.
     */
    private ITodoistBaseObjectManager<T> _objectManager;

    /**
     * Activity context.
     */
    private Activity _context;

    /**
     * Cached layout inflater.
     */
    private LayoutInflater _inflater;

    //
    // Extension methods
    //
    /**
     * Creates a new row instance.
     *
     * @param context Activity context
     * @param inflater Cached layout inflater
     *
     * @return New row
     */
    protected abstract S newRowInstance(final Activity context, final LayoutInflater inflater);

    /**
     * Constructor.
     *
     * @param context Activity context
     * @param todoistManager Object manager
     */
    public TodoistListAdapterBase(final Activity context, final ITodoistBaseObjectManager<T> todoistManager)
    {
        _context = context;
        _objectManager = todoistManager;

        _inflater = LayoutInflater.from(context);
    }

    @Override
    public final int getCount()
    {
        if (_objectManager == null)
        {
            return 0;
        }

        return _objectManager.getCount();
    }

    @Override
    public final Object getItem(final int position)
    {
        if (_objectManager == null)
        {
            return null;
        }

        return _objectManager.getArray().get(position);
    }

    @Override
    public final long getItemId(final int position)
    {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final View getView(final int position, final View convertView, final ViewGroup parent)
    {
        TodoistListRowBase<T> row = null;
        if (convertView == null)
        {
            row = newRowInstance(_context, _inflater);
        }
        else
        {
            row = (TodoistListRowBase<T>) convertView;
        }

        row.update(_objectManager.getArray().get(position));

        return row;
    }
}
