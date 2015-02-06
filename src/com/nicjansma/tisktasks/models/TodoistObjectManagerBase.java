package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

/**
 * Base object manager.
 *
 * @param <T> Object type
 */
public abstract class TodoistObjectManagerBase<T extends TodoistObjectBase>
    implements ITodoistBaseObjectManager<T>
{
    //
    // Extension methods
    //
    /**
     * Called after a change is made (importArray, add, updateOrders, update, delete, appendArray).
     */
    protected abstract void postChangeHook();

    //
    // Privates
    //
    /**
     * The array of objects.
     */
    private ArrayList<T> _objects = new ArrayList<T>();

    @Override
    public final ArrayList<T> getArray()
    {
        return _objects;
    }

    @Override
    public final void clearArray()
    {
        _objects.clear();
    }

    @Override
    public final void importArray(final ArrayList<T> objects)
    {
        _objects = objects;
        updateHierarchy();

        postChangeHook();
    }

    @Override
    public final void appendArray(final ArrayList<T> objects)
    {
        _objects.addAll(objects);
        updateHierarchy();

        postChangeHook();
    }

    /**
     * Updates the hierarchy (parents, children, etc) after nodes in the tree change.
     */
    private void updateHierarchy()
    {
        // clear parents list
        for (int i = 0; i < _objects.size(); i++)
        {
            _objects.get(i).setParent(null);
        }

        // determine which have "children"
        for (int i = 0; i < _objects.size(); i++)
        {
            T obj = _objects.get(i);

            //
            // determine if this is a parent
            //
            if (i + 1 < _objects.size()
                && _objects.get(i + 1).getIndent() > obj.getIndent())
            {
                obj.setIsParent(true);

                // set all children
                for (int j = i + 1; j < _objects.size(); j++)
                {
                    if (_objects.get(j).getIndent() > obj.getIndent())
                    {
                        _objects.get(j).setParent(obj);
                        obj.addChild(_objects.get(j));
                    }
                    else
                    {
                        break;
                    }
                }
            }
            else
            {
                obj.setIsParent(false);
            }
        }
    }

    /**
     * Gets the indent from an object ID.
     *
     * @param id Object ID
     *
     * @return Index of the object, -1 if the object doesn't exist
     */
    private int getIndex(final long id)
    {
        for (int i = 0; i < _objects.size(); i++)
        {
            if (_objects.get(i).getId() == id)
            {
                return i;
            }
        }

        return -1;
    }

    @Override
    public final T get(final long id)
    {
        int i = getIndex(id);
        if (i != -1)
        {
            return _objects.get(i);
        }

        return null;
    }

    @Override
    public final void update(final T obj)
    {
        int i = getIndex(obj.getId());
        if (i != -1)
        {
            _objects.set(i, obj);
        }

        updateHierarchy();

        postChangeHook();
    }

    @Override
    public final void update(final ArrayList<T> objs)
    {
        for (int i = 0; i < objs.size(); i++)
        {
            update(objs.get(i));
        }

        postChangeHook();
    }

    @Override
    public final void delete(final T obj)
    {
        ArrayList<Long> childrenIds = obj.getAllChildrenIds(null);

        int idx = getIndex(obj.getId());
        if (idx != -1)
        {
            _objects.remove(idx);
        }

        // remove all sub-objects
        for (int i = 0; i < childrenIds.size(); i++)
        {
            idx = getIndex(childrenIds.get(i));
            if (idx != -1)
            {
                _objects.remove(idx);
            }
        }

        updateHierarchy();

        postChangeHook();
    }

    @Override
    public final void add(final T obj, final T nearObj, final Boolean before)
    {
        int i = _objects.size() - 1;

        if (nearObj != null)
        {
            i = getIndex(nearObj.getId());

            if (i == -1)
            {
                // reset to last one
                i = _objects.size() - 1;
            }

            if (!before)
            {
                i++;
            }
        }

        if (i < 0)
        {
            i = 0;
        }

        _objects.add(i, obj);

        // orders aren't updated here
        // call updateOrders and send results to API

        updateHierarchy();

        postChangeHook();
    }

    @Override
    public final void updateOrders()
    {
        for (int i = 0; i < _objects.size(); i++)
        {
            // orders are off-by-1
            _objects.get(i).setItemOrder(i + 1);
        }

        postChangeHook();
    }

    @Override
    public final ArrayList<Long> getOrders()
    {
        ArrayList<Long> orders = new ArrayList<Long>();

        for (int i = 0; i < _objects.size(); i++)
        {
            orders.add(_objects.get(i).getId());
        }

        return orders;
    }

    @Override
    public final int getCount()
    {
        return _objects.size();
    }

    @Override
    public final T getLast()
    {
        if (_objects.size() == 0)
        {
            return null;
        }

        return _objects.get(_objects.size() - 1);
    }
}
