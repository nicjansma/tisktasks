package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.nicjansma.library.net.CacheableJsonObjectBase;

/**
 * Base Todoist object.
 */
public abstract class TodoistObjectBase
    extends CacheableJsonObjectBase
{
    //
    // Constants
    //
    /**
     * Minimum indent.
     */
    public static final int INDENT_MIN = 1;

    /**
     * Pixels per indent level.
     */
    public static final int INDENT_PIXELS = 15;

    //
    // Extension methods
    //
    /**
     * Initialize this object with a JSONObject.
     *
     * @param json JSON object
     */
    protected abstract void initializeInternalTodoist(JSONObject json);

    /**
     * Serializes this object to a JSONObject.
     *
     * @param json JSON object being built
     *
     * @return Updated JSONObject
     */
    protected abstract JSONObject toJsonInternalTodoist(final JSONObject json);

    /**
     * Gets the text of the object (eg, the name).
     *
     * @return Object's text
     */
    public abstract String getText();

    //
    // Privates
    //
    /**
     * Object ID.
     */
    private long _id;

    /**
     * Whether or not the object is collapsed.
     */
    private boolean _collapsed;

    /**
     * Object's indent.
     */
    private int _indent;

    /**
     * Object's order.
     */
    private int _itemOrder;

    /**
     * Whether or not the object is a parent to other objects.
     */
    private boolean _isParent;

    /**
     * Parent object.
     */
    private TodoistObjectBase _parent;

    /**
     * Array of children.
     */
    private ArrayList<TodoistObjectBase> _children = new ArrayList<TodoistObjectBase>();

    /**
     * Constructor.
     */
    public TodoistObjectBase()
    {
    }

    /**
     * Constructor.
     *
     * @param id Object ID
     * @param collapsed Object is collapsed
     * @param indent Object's indent
     * @param itemOrder Object's item order
     */
    public TodoistObjectBase(final long id, final boolean collapsed, final int indent, final int itemOrder)
    {
        _id = id;
        _collapsed = collapsed;

        setIndent(indent);

        _itemOrder = itemOrder;
    }

    @Override
    protected final void initializeInternal(final JSONObject json)
    {
        _id = json.optLong("id");
        _collapsed = json.optInt("collapsed", 0) == 1;

        // fix-up indent
        setIndent(json.optInt("indent"));

        _itemOrder = json.optInt("item_order");
        _isParent = false;
        _parent = null;

        initializeInternalTodoist(json);
    }

    @Override
    protected final JSONObject toJsonInternal(final JSONObject json)
    {
        try
        {
            json.put("id", _id);
            json.put("collapsed", _collapsed);
            json.put("indent", _indent);
            json.put("item_order", _itemOrder);
        }
        catch (final JSONException e)
        {
            e.printStackTrace();
        }

        return toJsonInternalTodoist(json);
    }

    /**
     * Gets the object's ID.
     *
     * @return Object ID
     */
    public final long getId()
    {
        return _id;
    }

    /**
     * Determines whether or not the object is collapsed.
     *
     * @return True if the object is collapsed
     */
    public final boolean isCollapsed()
    {
        return _collapsed;
    }

    /**
     * Sets whether the object is collapsed.
     *
     * @param collapsed Object's collapsed state
     */
    public final void setCollapsed(final Boolean collapsed)
    {
        _collapsed = collapsed;
    }

    /**
     * Gets the object's indent.
     *
     * @return Object's indent
     */
    public final int getIndent()
    {
        return _indent;
    }

    /**
     * Determines whether or not the object is indented.
     *
     * @return True if the object is indented
     */
    public final Boolean hasIndent()
    {
        return _indent > 1;
    }

    /**
     * Increases the object's indent.
     */
    public final void increaseIndent()
    {
        setIndent(_indent + 1);
    }

    /**
     * Decreases the object's indent.
     */
    public final void decreaseIndent()
    {
        setIndent(_indent - 1);
    }

    /**
     * Sets the object's indent.
     *
     * @param indent Object's indent
     */
    public final void setIndent(final int indent)
    {
        _indent = indent;

        if (_indent < INDENT_MIN)
        {
            _indent = INDENT_MIN;
        }
    }

    /**
     * Gets the object's indent in pixels.
     *
     * @return Object's indent in pixels
     */
    public final int getIndentInPixels()
    {
        int indent = (_indent - 1) * INDENT_PIXELS;

        return indent;
    }

    /**
     * Gets the items' order.
     *
     * @return Item's order
     */
    public final int getItemOrder()
    {
        return _itemOrder;
    }

    /**
     * Sets the item's order.
     *
     * @param order Item's order
     */
    public final void setItemOrder(final int order)
    {
        _itemOrder = order;
    }

    /**
     * Determines whether or not the object is a parent to other objects.
     *
     * @return True if the object is a parent to other objects
     */
    public final boolean isParent()
    {
        return _isParent;
    }

    /**
     * Sets whether or not this object is a parent to other objects.
     *
     * @param isParent True if this object is a parent to other objects
     */
    public final void setIsParent(final boolean isParent)
    {
        _isParent = isParent;
    }

    /**
     * Sets the object's parent.
     *
     * @param parent Parent object
     */
    public final void setParent(final TodoistObjectBase parent)
    {
        _parent = parent;
    }

    /**
     * Gets the object's parent object.
     *
     * @return Parent object
     */
    public final TodoistObjectBase getParent()
    {
        return _parent;
    }

    /**
     * Determines whether or not this object is a child of another object.
     *
     * @return True if this object is the child of another object
     */
    public final boolean isChild()
    {
        return _parent != null;
    }

    /**
     * Determines whether or not any parent is collapsed.
     *
     * This should determine the visibility of this object.
     *
     * @return True if any parent is collapsed
     */
    public final Boolean isAnyParentCollapsed()
    {
        if (_parent != null)
        {
            return _parent.isCollapsed() || _parent.isAnyParentCollapsed();
        }

        return false;
    }

    /**
     * Clears all children objects.
     */
    public final void clearChildren()
    {
        _children = new ArrayList<TodoistObjectBase>();
    }

    /**
     * Adds a child object.
     *
     * @param child Child object
     */
    public final void addChild(final TodoistObjectBase child)
    {
        _children.add(child);
    }

    /**
     * Determines whether or not this object has any children.
     *
     * @return True if this object has children
     */
    public final boolean hasChildren()
    {
        return _children.size() > 0;
    }

    /**
     * Gets all children, recursively.
     *
     * @param allChildren Current ArrayList of children
     *
     * @return Updated ArrayList of children
     */
    public final ArrayList<TodoistObjectBase> getAllChildren(ArrayList<TodoistObjectBase> allChildren)
    {
        if (allChildren == null)
        {
            allChildren = new ArrayList<TodoistObjectBase>();
        }

        for (int i = 0; i < _children.size(); i++)
        {
            allChildren.add(_children.get(i));
            allChildren = _children.get(i).getAllChildren(allChildren);
        }

        return allChildren;
    }

    /**
     * Gets all children IDs, recursively.
     *
     * @param allChildrenIds Current ArrayList of children IDs
     *
     * @return Updated ArrayList of children IDs
     */
    public final ArrayList<Long> getAllChildrenIds(ArrayList<Long> allChildrenIds)
    {
        if (allChildrenIds == null)
        {
            allChildrenIds = new ArrayList<Long>();
        }

        for (int i = 0; i < _children.size(); i++)
        {
            allChildrenIds.add(_children.get(i).getId());
            allChildrenIds = _children.get(i).getAllChildrenIds(allChildrenIds);
        }

        return allChildrenIds;
    }

}
