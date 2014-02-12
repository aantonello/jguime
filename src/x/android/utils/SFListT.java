/**
 * \file
 * Declares the SFListT class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 04, 2013
 * \since  jguime 2.4
 *
 * \copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;
/* #imports {{{ */
import java.util.*;
import java.lang.reflect.*;
/* #imports }}} */
/**
 * \ingroup x_android_utils
 * Generic class implementing a list of objects.
 * This class is, in some means, a implementation of Java List interface. The
 * goal in this class is to remove almost all exceptions that the original \c
 * ArrayList class does. This is, becouse, in some sence, those exceptions are
 * not required and have no real meaning. The concept is basic. When we check
 * for an existance of an object in the list we can just `get()` the object
 * and check if the result is \b null. There is no real reason for this method
 * to throw an exception.
 * @tparam T The type of the objects holded in the list.
 * @remarks This class is a copy of Java List class without inadequate
 * exceptions.
 *//* --------------------------------------------------------------------- */
public class SFListT<T> implements Collection<T>, Iterable<T>, Cloneable,
       RandomAccess
{
    /** \name Constructors */ //@{
    // public SFListT();/*{{{*/
    /**
     * Builds an empty list.
     **/
    public SFListT()
    {
        m_array = null;
        m_count = 0;
    }/*}}}*/
    // public SFListT(int capacity);/*{{{*/
    /**
     * Builds an instance of this class with an initial capacity.
     * @param capacity The inicial capacity for the list of objects.
     **/
    public SFListT(int capacity)
    {
        m_array = new Object[capacity];
        m_count = 0;
    }/*}}}*/
    // public SFListT(SFListT<T> list);/*{{{*/
    /**
     * Copy constructor.
     * @param list The list to be copied.
     * @remarks Althouth the list of objects are copied, the objects in it are
     * not. That is, the same reference object that is in the element 0 of
     * list \a list is in this object list. Whe the object at element 0 of \a
     * list is changed, the same object in this list will reproduce that
     * change as of it is the same object instance.
     **/
    public SFListT(SFListT<T> list)
    {
        m_array = new Object[list.size()];

        final Object[] items = list.m_array;
        final int      count = list.size();

        for (int i = 0; i < count; i++)
            m_array[i] = items[i];

        m_count = m_array.length;
    }/*}}}*/
    //@}

    /** \name Iterable Implementation */ //@{
    // public Iterator<T> iterator();/*{{{*/
    /**
     * Builds an Iterator for the elements in this list.
     * The same rules of the Java \c ArrayList class apply. The list can be
     * changed while this iterator are in use.
     * @returns An \c Iterator object to iterate with objects in this list.
     **/
    public Iterator<T> iterator()
    {
        return new __Iterator<T>(this);
    }/*}}}*/
    //@}

    /** \name Collection Implementation */ //@{
    // public int     size();/*{{{*/
    /**
     * Gets the size of the list.
     * The size of the list is the number of elements this list contains. This
     * can be less the real length of the list that is defined by the initial
     * capacity but can change as objects are added to the list, but not
     * removed.
     * @returns An integer with the number of elements in the list.
     **/
    public int size()
    {
        return m_count;
    }/*}}}*/
    // public boolean isEmpty();/*{{{*/
    /**
     * Checks wether this object is or not is empty.
     * @returns \b true if there is no elements in the list. Otherwise \b
     * false.
     **/
    public boolean isEmpty()
    {
        return (m_count == 0);
    }/*}}}*/
    // public boolean add(T element);/*{{{*/
    /**
     * Adds an element to this list.
     * @param element The element to be added.
     * @returns \b true if the function succeeds. If the \a element parameter
     * is a \b null reference the function returns \b false and no change is
     * made. \b null elements are not permited in this list.
     **/
    public boolean add(T element)
    {
        if (element == null) return false;

        if (!_internal_alloc(1))
            return false;

        m_array[m_count] = element;
        m_count++;
        return true;
    }/*}}}*/
    // public boolean addAll(Collection<? extends E> coll);/*{{{*/
    /**
     * Adds all elements in the specified collection to this list.
     * @param coll \c Collection of elements of type \b T or extending the
     * type \b T.
     * @returns Always \b true. Whe this function fails a not catcheable \e
     * OutOfMemoryError will be thrown. The internal list will be reallocated
     * to make room for the new element if needed.
     * @note The result will be \b true even when \a coll is empty or \b null.
     * This is a break in the \c Collection contract.
     **/
    public boolean addAll(Collection<? extends T> coll)
    {
        if ((coll == null) || coll.isEmpty()) return true;

        final int      limit = coll.size();
        final Object[] items = coll.toArray();
        _internal_alloc(limit);

        for (int i = 0; i < limit; i++)
            m_array[m_count++] = items[i];

        return true;
    }/*}}}*/
    // public boolean remove(Object element);/*{{{*/
    /**
     * Removes an element of this list.
     * The object presence will be compared using <i>fast check</i>.
     * @param element Element to be removed.
     * @returns \b true if the requested \e element is removed from the list.
     * Otherwise \b false.
     * @remarks Memory is not deallocated with this removal. Objects in
     * positions after the removed object will be repositioned.
     * @note As stated, <i>fast check</i> will compare elements by
     * reference.
     **/
    public boolean remove(Object element)
    {
        int index = indexOf(element);

        if (index < 0) return false;
        remove(index);
        return true;
    }/*}}}*/
    // public boolean removeAll(Collection<?> coll);/*{{{*/
    /**
     * Remove all elements from this list that are also elements in the
     * specified collection.
     * @param coll Collection of elements to be removed from this list. Can be
     * \b null, empty and can have elements that are not in this list.
     * @returns \b true if at least one element is removed from this list.
     * Othersize \b false.
     * @remarks By allowing the parameter \a coll to be \b null this method
     * doesn't throw \c NullPointerException exception. Also, the comparison
     * between elements are made by <i>fast check</i>, so no \e cast
     * operations are made and no \c ClassCastException will be thrown.
     **/
    public boolean removeAll(Collection<?> coll)
    {
        if ((coll == null) || coll.isEmpty())
            return false;

        boolean  result = false;
        final Object[] items = coll.toArray();
        final int      limit = items.length;
        int index = -1;

        for (int i = 0; i < limit; i++) {
            if ((index = indexOf(items[i])) >= 0) {
                remove(index);
                result = true;
            }
        }
        return result;
    }/*}}}*/
    // public boolean retainAll(Collection<?> coll);/*{{{*/
    /**
     * Retains only objects that are also contained in the specified
     * collection.
     * That is, this method will remove all elements that are not present in
     * the passed collection.
     * @param coll Collection with objects to be checked. Can be \b null or
     * empty. Comparisons are made in terms of <i>fast check</i>.
     * @returns \b true, changing or not the elements of this list. The result
     * will be \b false only when \a coll is a \b null reference or an empty
     * collection.
     * @remarks By doing a <i>fast check</i>, no cast operations are made so
     * no exceptions are thrown.
     * @note No memory is deallocated in this method.
     * @see contains()
     **/
    public boolean retainAll(Collection<?> coll)
    {
        if ((coll == null) || coll.isEmpty())
            return false;

        final Object[] items = coll.toArray();
        final int      count = items.length;
        final int      limit = (m_count - 1);
        Object any = null;
        int  index = -1;

        for (int i = limit; i >= 0; i--)
        {
            any = m_array[i];
            index = -1;
            for (int j = 0; j < count; j++)
            {
                if (any == items[j])
                {
                    index = j;
                    break;
                }
            }
            
            if (index < 0)
                remove(i);
        }
        return true;
    }/*}}}*/
    // public boolean contains(Object element);/*{{{*/
    /**
     * Checks if this list has a reference to specified element.
     * This function does a fast check. That is, the \a element instance is
     * checked by its reference and no \c equal() calls are made. So, this
     * method will returns \b true only if:
     * <pre>
     * list.get(some_index) == element;
     * </pre>
     * @param element Object to be checked.
     * @return \b true if the object \a element is present in this list.
     * Otherwise \b false.
     * @remarks To check the existance of an object through an \c equals()
     * method comparison use #has() function.
     **/
    public boolean contains(Object element)
    {
        return (indexOf(element) >= 0);
    }/*}}}*/
    // public boolean containsAll(Collection<?> coll);/*{{{*/
    /**
     * Checks if the especified collection is contained by this list.
     * The function will make a fast check in all elements contained by the
     * passed collection. This is a lengthy operation. Use with care.
     * @param coll The collection whoose elements must be checked.
     * @returns \b true when all of the elements in \a coll are present in
     * this list. \b false when at least one of the elements in \a coll are
     * not present in this list, or if \a coll is empty or \b null.
     * @remarks As stated, a fast check means that the elements in the two
     * collections are compared by reference.
     * \see contains()
     **/
    public boolean containsAll(Collection<?> coll)
    {
        if ((coll == null) || coll.isEmpty())
            return false;

        final Object[] items = coll.toArray();
        final int      limit = items.length;

        for (int i = 0; i < limit; i++) {
            if (!contains(items[i]))
                return false;
        }
        return true;
    }/*}}}*/
    // public void    clear();/*{{{*/
    /**
     * Clear the list of elements in this list.
     * The list will be empty after this method returns. No memory will be
     * deallocated thouth. The internal capacity will remain the same.
     **/
    public void clear()
    {
        final int limit = arrays.length(m_array);
        for (int i = 0; i < limit; i++)
            m_array[i] = null;

        m_count = 0;
    }/*}}}*/
    // public <T> T[] toArray(T[] array);/*{{{*/
    /**
     * Returns an array with elements from this list.
     * The returned array can be freely changed without changing the internal
     * array of this list.
     * @param array An array instance with the same class type of the elements
     * in this list. If this array has equal or greater number of elements
     * that are in the list, this same array is used to hold the elements in
     * the list. Further positions are set to \b null. When this argument has
     * length less than necessary, a new allocated array will be returned.
     * @return An array with elements from this list or \b null if the type of
     * the parameter \a array is not the same as the elements of this list.
     * @throw ClassCastException When the type of \a array is not the same as
     * the elements of this list.
     * @throw NullPointerException If \a array argument is \b null.
     **/
    public <T> T[] toArray(T[] array) throws ClassCastException
    {
        final int len = arrays.length(array);

        if (len < m_count)
        {
            try { array = (T[])Array.newInstance(array.getClass(), m_count); }
            catch (Exception ex) {
                debug.e(ex, "$n in SFListT::toArray(T[]): '$s'\n");
                return null;
            }
        }

        final int count = m_count;

        for (int i = 0; i < count; i++)
            array[i] = (T)m_array[i];

        for (int i = count; i < len; i++)
            array[i] = null;

        return array;
    }/*}}}*/
    // public Object[] toArray();/*{{{*/
    /**
     * Builds an array with the objects in this list.
     * The returned array can be changed freely without changing the list of
     * this class.
     * @returns An \c Object array with elements from this list. If this list
     * is empty an empty array is returned.
     **/
    public Object[] toArray()
    {
        Object[]  items = new Object[m_count];
        final int count = m_count;

        for (int i = 0; i < count; i++)
            items[i] = m_array[i];

        return items;
    }/*}}}*/
    //@}

    /** \name Implementation */ //@{
    // public int     capacity();/*{{{*/
    /**
     * Returns the current capacity of this list.
     * @return The current capacity of the list, in number of elements. This
     * can be greater than the number of elements in the list. To release the
     * unused memory one can use #trim().
     **/
    public int capacity()
    {
        return arrays.length(m_array);
    }/*}}}*/
    // public boolean has(Object element);/*{{{*/
    /**
     * Checks if this list has a reference to specified element.
     * This function does the comparison using the \c equals() Object method.
     * @param element Object to be checked.
     * @return \b true if the object \a element is present in this list.
     * \b false will be returned if the element is not present in the list or
     * if it is a \b null reference.
     **/
    public boolean has(Object element)
    {
        return (indexOfObject(element) >= 0);
    }/*}}}*/
    // public int     indexOf(Object element);/*{{{*/
    /**
     * Returns the index of the especified object.
     * @param element Element to gets its current position.
     * @returns The position of the element or \b -1 if the element doesn't
     * exists in this list.
     * @remarks This functions does a <i>fast check</i> in the passed \a
     * element. This meas that no \e cast operation is made and, by this, no
     * \c ClassCastException is thrown. Also, no \c NullPointerException is
     * thrown. When \a element is \b null the function returns \b -1
     * immediatly becouse \b null elements are not allowed in the list.
     *
     * To get the index of an element through its \c equals() method
     * comparison use the #indexOfObject() function.
     **/
    public int indexOf(Object element)
    {
        if (element == null) return -1;

        final int limit = m_count;

        for (int i = 0; i < limit; i++) {
            if (m_array[i] == element)
                return i;
        }
        return -1;
    }/*}}}*/
    // public int     indexOfObject(Object element);/*{{{*/
    /**
     * Returns the index of the especified object.
     * @param element Element to gets its current position.
     * @returns The position of the element or \b -1 if the element doesn't
     * exists in this list.
     * @remarks This method does the comparison using \c equals() method of
     * each object implementation. So the \a element parameter doesn't need to
     * be the same reference in the list.
     **/
    public int indexOfObject(Object element)
    {
        if (element == null) return -1;

        final int count = m_count;
        for (int i = 0; i < count; i++)
        {
            if (element.equals(m_array[i]))
                return i;
        }
        return -1;
    }/*}}}*/
    // public T       get(int index);/*{{{*/
    /**
     * Obtain the element at the specified index position.
     * @param index The position of the requested element. This argument can
     * be negative. In this case it will get elements from the end of the
     * list. So, -1 will retrieve the last element in the list and so on.
     * @returns The element reference or \b null when \a index is invalid.
     **/
    public T get(int index)
    {
        if (index < 0) index = (m_count - index);

        if ((index < 0) || (index >= m_count))
            return null;

        return (T)m_array[index];
    }/*}}}*/
    // public T       set(int index, T element);/*{{{*/
    /**
     * Replaces the element at the specified position.
     * @param index Position of the element to be replaced. This value can be
     * less than zero. In this case it will count positions starting at the
     * end of the list. -1 will replace the last element and so on.
     * @param element The object to replace the existing one.
     * @returns The element that was replaced means success. If \a index lies
     * out of bounds of the internal list, the result will be \b null and the
     * \a element will not be inserted. An exception to this rule is when \a
     * index is equal to the length of the list. In this case the \a element
     * will be appended but the result will still be \b null.
     **/
    public T set(int index, T element)
    {
        if (index < 0) index = (m_count - index);
        if ((index < 0) || (index > m_count))
            return null;

        T result = null;

        if (index == m_count)
            add(element);
        else
        {
            result = (T)m_array[index];
            m_array[index] = element;
        }
        return result;
    }/*}}}*/
    // public void    add(int index, T element);/*{{{*/
    /**
     * Inserts a new element in the specified position.
     * @param index Position where the new element must be inserted
     * @param element Object to be added to the list.
     * @remarks Elements in the specified position and after it will be
     * shifted. Memory is allocated if needed.
     * @throw IllegalArgumentException When \a index is less than 0 or
     * greather than the number of elements in the list. When \a index is
     * equal the number of elements in the list the \a element will be
     * appended at it.
     * @note Use #insert() method to do the same operation without worring
     * abount exceptions.
     **/
    public void add(int index, T element) throws IllegalArgumentException
    {
        if ((index < 0) || (index > m_count))
            throw new IllegalArgumentException("Invalid 'index' parameter!");

        _internal_alloc(1);
        m_array = arrays.add(m_array, index, element);
        m_count++;
    }/*}}}*/
    // public boolean insert(int index, T element);/*{{{*/
    /**
     * Inserts an element in the specified position.
     * By differing from #add(int,T) this method doesn't throw any exceptions.
     * @param index Position insert the new element. This value can be
     * less than zero. In this case it will count positions starting at the
     * end of the list. -1 will insert the element at the last position of
     * this list, shifint the last element one position after.
     * @param element Object to be inserted. Cannot be \b null since \b null
     * references are not allowed in this list.
     * @returns \b true whem the specified element is inserted in the list.
     * Otherwise \b false.
     **/
    public boolean insert(int index, T element)
    {
        if (index < 0) index = (m_count - index);
        if ((index < 0) || (index >= m_count) || (element == null))
            return false;

        _internal_alloc(1);
        m_array = arrays.add(m_array, index, element);
        m_count++;
        return true;
    }/*}}}*/
    // public T       remove(int index);/*{{{*/
    /**
     * Remove one element from the list.
     * @param index Position of the element to be removed. This value can be
     * less than zero. In this case it will count positions starting at the
     * end of the list. -1 will remove the last element and so on. Elements
     * after the specified position will be shifted to fill the gap.
     * @return The element removed means success. When \a index position lies
     * out of list bounds the return will be \b null.
     **/
    public T remove(int index)
    {
        if (index < 0) index = (m_count - index);
        if ((index < 0) || (index >= m_count))
            return null;

        T result = (T)m_array[index];
        final int lastIndex = (m_count - 1);

        if (index < lastIndex)
            arrays.move(m_array, index, m_array, (index + 1), (m_count - 1));

        m_array[lastIndex] = null;

//        for (int i = (index + 1); i <= count; i++)
//            m_array[i - 1] = m_array[i];

        m_count--;
        return result;
    }/*}}}*/
    // public void    ensureCapacity(int capacity);/*{{{*/
    /**
     * Ensure the list will have capacity for the specified number of
     * elements.
     * @param capacity The list capacity in number of elements. If this value
     * is less than the current list capacity the list will not change.
     **/
    public void ensureCapacity(int capacity)
    {
        if (capacity <= arrays.length(m_array))
            return;

        _internal_alloc(capacity - m_count);
    }/*}}}*/
    // public void    trim();/*{{{*/
    /**
     * Reduces this list capacity to the number of elements.
     * This function will free memory allocated and not used.
     **/
    public void trim()
    {
        m_array = arrays.realloc(m_array, m_count);
    }/*}}}*/
    //@}

    /** \name Object Overrides */ //@{
    // public Object clone();/*{{{*/
    /**
     * Clone this list.
     * @return The result is an \c SFListT instance with the same list of
     * objects and same capacity.
     * @remarks There is a copy constructor that does the same of this method.
     **/
    public Object clone()
    {
        return new SFListT<T>(this);
    }/*}}}*/
    //@}

    /** \name Internal Methods */ //@{
    // private boolean _internal_alloc(int count);/*{{{*/
    /**
     * Allocate space in the internal array.
     * @param count Number of elements to be added in the array.
     * @returns Always \b true. Whe this function fails a not catcheable \e
     * OutOfMemoryError will be thrown.
     **/
    private boolean _internal_alloc(int count)
    {
        if ((m_count + count) <= arrays.length(m_array))
            return true;

        m_array = arrays.realloc(m_array, (m_count + count));
        return true;
    }/*}}}*/
    //@}

    /** \name Internal Classes */ //@{
    // class __Iterator<T> implements Iterator<T>;/*{{{*/
    /**
     * The internal Iterator implementation.
     **/
    class __Iterator<T> implements Iterator<T>
    {
        /** \name Constructors */ //@{
        // public __Iterator(SFListT<T> list);/*{{{*/
        /**
         * Builds the __Iterator object.
         * @param list The list backing the elements.
         **/
        public __Iterator(SFListT<T> list)
        {
            m_list     = list;
            m_position = -1;
        }/*}}}*/
        //@}

        /** \name Iterator Implementation */ //@{
        // public boolean hasNext();/*{{{*/
        /**
         * Checks if there is more elements in the list.
         * @returns \b true if there is more elements to be iterated.
         * Otherwise \b false.
         **/
        public boolean hasNext()
        {
            return ((m_position + 1) < m_list.size());
        }/*}}}*/
        // public T       next();/*{{{*/
        /**
         * Returns the next element in the list.
         * The first call to this method returns the first element. The second
         * call will return the second element and so on.
         * @return The next element in the list.
         * @throw NoSuchElementException When the list has no more elements to
         * be iterated.
         **/
        public T next() throws NoSuchElementException
        {
            if (!hasNext())
                throw new NoSuchElementException("There are no more elements to iterate!");

            m_position++;
            return m_list.get(m_position);
        }/*}}}*/
        // public void    remove();/*{{{*/
        /**
         * Removes the current iterated element.
         * The current iterated element is the element returned by the last
         * call to #next() method.
         * @remarks This operation is not supported and will throw an
         * exception when used.
         * @throw UnsupportedOperationException This operation is not
         * supported.
         **/
        public void remove() throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException("SFListT::Iterator::remove() is not supported!");
        }/*}}}*/
        //@}

        /** \name Data Members */ //@{
        SFListT<T> m_list;          /**< The list backing this Iterator. */
        int    m_position;          /**< Current iterating position.     */
        //@}
    }/*}}}*/
    //@}

    /** \name Data members */ //@{
    protected Object[] m_array;             /**< Array of objects. */
    protected int      m_count;             /**< Number of objects in array. */
    //@}
}
// vim:syntax=java.doxygen
