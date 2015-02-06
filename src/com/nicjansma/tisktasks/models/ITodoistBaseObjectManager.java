package com.nicjansma.tisktasks.models;

import java.util.ArrayList;

/**
 * Object manager interface.
 *
 * @param <T> Type of object within the manager
 */
public interface ITodoistBaseObjectManager<T extends TodoistObjectBase>
{
    /**
     * Gets the number of objects.
     *
     * @return Number of objects.
     */
    int getCount();

    /**
     * Gets the array of objects.
     *
     * @return Array of objects.
     */
    ArrayList<T> getArray();

    /**
     * Gets the specified object.
     *
     * @param id Object ID
     *
     * @return Object specified by the ID
     */
    T get(long id);

    /**
     * Imports the items, clearing any existing objects first.
     *
     * @param items Array of objects to import
     */
    void importArray(ArrayList<T> items);

    /**
     * Appends the items to the current list.
     *
     * @param items Items to append.
     */
    void appendArray(ArrayList<T> items);

    /**
     * Updates the specified object.
     *
     * @param obj Object to update
     */
    void update(T obj);

    /**
     * Updates all of the specified objects.
     *
     * @param objs Objects to update.
     */
    void update(ArrayList<T> objs);

    /**
     * Deletes the specified object.
     *
     * @param obj Object to remove
     */
    void delete(T obj);

    /**
     * Adds the specified object close to another object.
     *
     * If nearObj is null, the new object is added to the end of the list.
     *
     * @param obj Object to add
     * @param nearObj After this specified object, or null for the end of the list
     * @param before Instead of adding the object after the nearObj, add the new object before it.
     */
    void add(T obj, T nearObj, Boolean before);

    /**
     * Fixes up the orders of objects in the list.
     */
    void updateOrders();

    /**
     * Gets the IDs of objects in their specified order.
     *
     * @return Array of object IDs
     */
    ArrayList<Long> getOrders();

    /**
     * Gets the last object.
     *
     * @return Last object, or null if there are no objects
     */
    T getLast();

    /**
     * Clears all objects in the array.
     */
    void clearArray();

}
