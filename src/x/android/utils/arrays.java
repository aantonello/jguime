/**
 * \file
 * Defines the arrays class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   September 30, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.utils;

import java.util.Random;
import x.android.defs.ERROR;

/**
 * \ingroup x_android_utils
 * Helper class with static array functions and facilities.
 *//* --------------------------------------------------------------------- */
public final class arrays
{
    /** \name LENGTH PROPERTY */ //@{
    // public static int length(byte[] array);/*{{{*/
    /**
     * Safely gets the length of an array.
     * \param array The array thats the length should be retrieved.
     * \returns The length of the array. If \a array is \b null the return will
     * be zero.
     **/
    public static int length(byte[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(char[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(char[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(short[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(short[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(int[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(int[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(long[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(long[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(String[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(String[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    // public static int length(Object[] array);/*{{{*/
    /**
     * \copydoc arrays#length(byte[])
     **/
    public static int length(Object[] array)
    {
        return ((array == null) ? 0 : array.length);
    }/*}}}*/
    //@}

    /** \name THE set() METHOD */ //@{
    // public static void set(byte[] array, byte val);/*{{{*/
    /**
     * Fills the array with some value.
     * \param array The array to be filled. The entire array length will be
     * filled with the passed value.
     * \param val The value to fill the array.
     **/
    public static void set(byte[] array, byte val) {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(char[] array, char val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(char[] array, char val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(short[] array, short val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(short[] array, short val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(int[] array, int val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(int[] array, int val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(long[] array, long val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(long[] array, long val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(String[] array, String val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(String[] array, String val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(Object[] array, Object val);/*{{{*/
    /** \copydoc arrays#set(byte[],byte) */
    public static void set(Object[] array, Object val)
    {
        arrays.set(array, val, 0, arrays.length(array));
    }/*}}}*/
    // public static void set(byte[] array, int val, int first, int count);/*{{{*/
    /**
     * Fills an area of the array with some value.
     * \param array The array to be filled.
     * \param val The value to fill the array.
     * \param first The index of the element in the array where the
     * process should start.
     * \param count The number of elements, starting at \a first, that must be
     * filled.
     **/
    public static void set(byte[] array, int val, int first, int count)
    {
        int last = first + count;
        for (int i = first; i < last; i++) {
            array[i] = (byte)(val & 0x000000FF);
        }
    }/*}}}*/
    // public static void set(char[] array, char val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(char[] array, char val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = val;
        }
    }/*}}}*/
    // public static void set(short[] array, int val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(short[] array, int val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = (short)(val & 0x0000FFFF);
        }
    }/*}}}*/
    // public static void set(int[] array, int val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(int[] array, int val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = val;
        }
    }/*}}}*/
    // public static void set(long[] array, long val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(long[] array, long val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = val;
        }
    }/*}}}*/
    // public static void set(String[] array, String val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(String[] array, String val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = val;
        }
    }/*}}}*/
    // public static void set(Object[] array, Object val, int first, int count);/*{{{*/
    /** \copydoc arrays#set(byte[],int,int,int) */
    public static void set(Object[] array, Object val, int first, int count)
    {
        int limit = first + count;
        for (int i = first; i < limit; i++) {
            array[i] = val;
        }
    }/*}}}*/
    //@}

    /** \name THE copy() METHOD */ //@{
    // public static void copy(byte[] dst, int dstInd, byte[] src, int srcInd, int count);/*{{{*/
    /**
     * Copy elements from one array to another.
     * \param dst The destination array.
     * \param dstInd The element, in the destination array, where the copy
     *      should start.
     * \param src The source array.
     * \param srcInd The element, int the source array, where the copy should
     *      start.
     * \param count The number of elements to copy from \a src to \a dst.
     * \remarks \a srcInd and \a dstInd must be valid. No check is done in
     * these arguments. \a count can be less than zero if the entire \a src
     * array should be copied to \a dst. In cases where the number of elements
     * to copy are zero, no objects are touched by this function.
     * \note Arguments are in different order of \c System.arraycopy()
     * standard function.
     **/
    public static void copy(byte[] dst, int dstInd, byte[] src, int srcInd, int count)
    {
        if (count < 0) { count = (arrays.length(src) - srcInd); }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(char[] dst, int dstInd, char[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int);
     **/
    public static void copy(char[] dst, int dstInd, char[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(short[] dst, int dstInd, short[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int);
     **/
    public static void copy(short[] dst, int dstInd, short[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(int[] dst, int dstInd, int[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int);
     **/
    public static void copy(int[] dst, int dstInd, int[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(long[] dst, int dstInd, long[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int);
     **/
    public static void copy(long[] dst, int dstInd, long[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(String[] dst, int dstInd, String[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int);
     **/
    public static void copy(String[] dst, int dstInd, String[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    // public static void copy(Object[] dst, int dstInd, Object[] src, int srcInd, int count);/*{{{*/
    /**
     * \copydoc copy(byte[],int,byte[],int,int)
     **/
    public static void copy(Object[] dst, int dstInd, Object[] src, int srcInd, int count)
    {
        if (count < 0) { count = arrays.length(src) - srcInd; }
        for (int i = 0; i < count; i++) {
            dst[i+dstInd] = src[i+srcInd];
        }
    }/*}}}*/
    //@}

    /** \name THE move() METHOD */ //@{
    // public static void move(byte[] dst, int dstIdx, byte[] src, int srcIdx, int count);/*{{{*/
    /**
     * Move data from one array to another.
     * Notice the different order of parameters compared with \c
     * System.arraycopy() function. \a dst and \a src can be the same array. \a
     * dstIdx and \a srcIdx can overlap.
     * \param dst The destination array.
     * \param dstIdx The element, in the destination array, where the copy
     * should start.
     * \param src The source array.
     * \param srcIdx The element, int the source array, where the copy should
     * start.
     * \param count The number of elements to copy from \a src to \a dst.
     * \remarks \a srcIdx and \a dstIdx must be valid. No check is done in
     * these arguments. \a count can be less than zero if the entire \a src
     * array should be copied to \a dst. If \a src and \a dst are different
     * arrays the \c copy() method will be used due of performance reasons.
     **/
    public static void move(byte[] dst, int dstIdx, byte[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(char[] dst, int dstIdx, char[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(char[] dst, int dstIdx, char[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(short[] dst, int dstIdx, short[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(short[] dst, int dstIdx, short[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(int[] dst, int dstIdx, int[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(int[] dst, int dstIdx, int[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(long[] dst, int dstIdx, long[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(long[] dst, int dstIdx, long[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(String[] dst, int dstIdx, String[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(String[] dst, int dstIdx, String[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    // public static void move(Object[] dst, int dstIdx, Object[] src, int srcIdx, int count);/*{{{*/
    /**
     * \copydoc move(byte[],int,byte[],int,int)
     **/
    public static void move(Object[] dst, int dstIdx, Object[] src, int srcIdx, int count)
    {
        if ((dst != src) || (dstIdx < srcIdx)) {
            copy(dst, dstIdx, src, srcIdx, count);
            return;
        }

        if (count < 0) {
            count = (src.length - srcIdx);
        }

        while (--count >= 0) {
            dst[dstIdx + count] = src[srcIdx + count];
        }
    }/*}}}*/
    //@}

    /** \name THE realloc() METHOD */ //@{
    // public static byte[]   realloc(byte[] array, int size);/*{{{*/
    /**
     * Reallocate an array.
     * The function creates a new array of size \a size, copies the data from
     * the current \a array to the new created array and returns it.
     * \param array The array with current values to fill the new array.
     * \param size The length of the new array.
     * \return If succeeds the function returns the newly created array. If
     * fails the function returns \a array.
     **/
    public static byte[] realloc(byte[] array, int size)
    {
        int    count  = arrays.length(array);
        byte[] memory = new byte[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static char[]   realloc(char[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static char[] realloc(char[] array, int size)
    {
        int    count  = arrays.length(array);
        char[] memory = new char[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static short[]  realloc(short[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static short[] realloc(short[] array, int size)
    {
        int     count  = arrays.length(array);
        short[] memory = new short[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static int[]    realloc(int[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static int[] realloc(int[] array, int size)
    {
        int   count  = arrays.length(array);
        int[] memory = new int[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static long[]   realloc(long[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static long[] realloc(long[] array, int size)
    {
        int    count  = arrays.length(array);
        long[] memory = new long[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static String[] realloc(String[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static String[] realloc(String[] array, int size)
    {
        int    count  = arrays.length(array);
        String[] memory = new String[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    // public static Object[] realloc(Object[] array, int size);/*{{{*/
    /**
     * \copydoc realloc(byte[],int)
     **/
    public static Object[] realloc(Object[] array, int size)
    {
        int    count  = arrays.length(array);
        Object[] memory = new Object[size];
        if (memory == null) return array;
        if (array == null) return memory;
        arrays.copy(memory, 0, array, 0, ((size < count) ? size : count));
        return memory;
    }/*}}}*/
    //@}

    /** \name THE add() METHOD */ //@{
    // public static byte[]   add(byte[] array, int pos, int val);/*{{{*/
    /**
     * Adds an element to the array.
     * The function increases the length of the array to insert the new value.
     * \param array The original array where the new value must be added. If
     *      this argument is \c null a new array will be created.
     * \param pos The position where the new value should be added. If this
     *      value is less than zero or greater than the original length of the
     *      \a array the element will be added at the end.
     * \param val The element to be added.
     * \returns If succeeds the return value is the new array with the element
     * added. The original array can be destroied after the function returns.
     **/
    public static byte[]   add(byte[] array, int pos, int val) {
        if (array == null) {
            array = new byte[1];
            array[0] = (byte)(val & 0x000000FF);
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = (byte)(val & 0x000000FF);
        }
        return array;
    }/*}}}*/
    // public static char[]   add(char[] array, int pos, char val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static char[] add(char[] array, int pos, char val) {
        if (array == null) {
            array = new char[1];
            array[0] = val;
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = val;
        }
        return array;
    }/*}}}*/
    // public static short[]  add(short[] array, int pos, int val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static short[] add(short[] array, int pos, int val) {
        if (array == null) {
            array = new short[1];
            array[0] = (short)(val & 0x0000FFFF);
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = (short)(val & 0x0000FFFF);
        }
        return array;
    }/*}}}*/
    // public static int[]    add(int[] array, int pos, int val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static int[] add(int[] array, int pos, int val) {
        if (array == null) {
            array = new int[1];
            array[0] = val;
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = val;
        }
        return array;
    }/*}}}*/
    // public static long[]   add(long[] array, int pos, long val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static long[] add(long[] array, int pos, long val) {
        if (array == null) {
            array = new long[1];
            array[0] = val;
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = val;
        }
        return array;
    }/*}}}*/
    // public static String[] add(String[] array, int pos, String val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static String[] add(String[] array, int pos, String val) {
        if (array == null) {
            array = new String[1];
            array[0] = val;
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = val;
        }
        return array;
    }/*}}}*/
    // public static Object[] add(Object[] array, int pos, Object val);/*{{{*/
    /**
     * \copydoc add(byte[],int,int)
     **/
    public static Object[] add(Object[] array, int pos, Object val) {
        if (array == null) {
            array = new Object[1];
            array[0] = val;
        } else {
            int count = array.length;
            array = realloc(array, count + 1);
            if ((pos >= 0) && (pos < count)) {
                move(array, (pos+1), array, pos, (count - pos));
            } else if (pos < 0) {
                pos = count;
            }
            array[pos] = val;
        }
        return array;
    }/*}}}*/
    //@}

    /** \name THE rem() METHOD */ //@{
    // public static byte[]   rem(byte[] array, int item);/*{{{*/
    /**
     * Removes an element from the array.
     * \param array The array. Cannot be \b null.
     * \param item The index of the item to be removed.
     * \returns A new array whithout the item.
     * \remarks The array will be reallocated with its size decreased by one.
     **/
    public static byte[] rem(byte[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    // public static char[]   rem(char[] array, int item);/*{{{*/
    /**
     * \copydoc rem(byte[],int)
     **/
    public static char[] rem(char[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    // public static short[]  rem(short[] array, int item);/*{{{*/
    /**
     * \copydoc rem(byte[],int)
     **/
    public static short[] rem(short[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    // public static int[]    rem(int[] array, int item);/*{{{*/
    /**
     * \copydoc rem(byte[],int)
     **/
    public static int[] rem(int[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    // public static long[]   rem(long[] array, int item);/*{{{*/
    /**
     * \copydoc rem(byte[],int)
     **/
    public static long[] rem(long[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    // public static String[] rem(String[] array, int item);/*{{{*/
    /**
     * \copydoc rem(byte[],int)
     **/
    public static String[] rem(String[] array, int item) {
        int count = array.length - 1;
        if (item < count) {
            move(array, item, array, (item+1), (count - item));
        }
        return realloc(array, count);
    }/*}}}*/
    //@}

    /** \name THE append() METHOD */ //@{
    // public static byte[]   append(byte[]   arr1, byte[]   arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static byte[] append(byte[] arr1, byte[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static char[]   append(char[]   arr1, char[]   arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static char[] append(char[] arr1, char[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static short[]  append(short[]  arr1, short[]  arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static short[] append(short[] arr1, short[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static int[]    append(int[]    arr1, int[]    arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static int[] append(int[] arr1, int[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static long[]   append(long[]   arr1, long[]   arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static long[] append(long[] arr1, long[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static String[] append(String[] arr1, String[] arr2);/*{{{*/
    /**
     * \copydoc append(Object[],Object[])
     **/
    public static String[] append(String[] arr1, String[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    // public static Object[] append(Object[] arr1, Object[] arr2);/*{{{*/
    /**
     * Append one array to another.
     * \param arr1 The main array. The second array will be appended to this
     *      one.
     * \param arr2 The second array, to be appended in the first one.
     * \return An array resulting of the operation.
     * \remarks If \a arr1 is \b null, \a arr2 will be returned. If \a arr2 is
     * \b null, \a arr1 will be returned without any changes.
     **/
    public static Object[] append(Object[] arr1, Object[] arr2) {
        if (arr1 == null) return arr2;
        if (arr2 == null) return arr1;

        int size = arrays.length(arr1);
        arr1     = arrays.realloc(arr1, size + arrays.length(arr2));
        arrays.copy(arr1, size, arr2, 0, arr2.length);
        return arr1;
    }/*}}}*/
    //@}

    /** \name THE slice() METHOD */ //@{
    // public static byte[]   slice(byte[] array, int first, int count);/*{{{*/
    /**
     * Build an array of a sliced part of anoter array.
     * \param array The original array to be slided. This array will not be
     * changed in this function.
     * \param first Index of the first element to be sliced. If less than zero
     * or greater than the number of elements in the \a array the return will
     * be \b null.
     * \param count Number of elements to be copied from the original \a
     * array. If less than zero or greater than the number of elements in the
     * array only the remaining elements starting from \a first will be
     * copied.
     * \return A new array built from the elements sliced from the original \a
     * array or \b null, if an argument was invalid.
     **/
    public static byte[]   slice(byte[] array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        byte[] result = new byte[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    // public static char[]   slice(char[] array, int first, int count);/*{{{*/
    /**
     * \copydoc slice(byte[],int,int)
     **/
    public static char[]   slice(char[] array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        char[] result = new char[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    // public static short[]  slice(short[] array, int first, int count);/*{{{*/
    /**
     * \copydoc slice(byte[],int,int)
     **/
    public static short[]  slice(short[] array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        short[] result = new short[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    // public static int[]    slice(int[]  array, int first, int count);/*{{{*/
    /**
     * \copydoc slice(byte[],int,int)
     **/
    public static int[]    slice(int[]  array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        int[] result = new int[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    // public static long[]   slice(long[] array, int first, int count);/*{{{*/
    /**
     * \copydoc slice(byte[],int,int)
     **/
    public static long[]   slice(long[] array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        long[] result = new long[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    // public static String[] slice(String[] array, int first, int count);/*{{{*/
    /**
     * \copydoc slice(byte[],int,int)
     **/
    public static String[] slice(String[] array, int first, int count) {
        if ((array == null) || (first < 0) || (first > arrays.length(array)))
            return null;

        if ((count < 0) || ((first + count) > arrays.length(array)))
            count = arrays.length(array) - first;

        String[] result = new String[count];
        arrays.copy(result, 0, array, first, count);

        return result;
    }/*}}}*/
    //@}

    /** \name NUMBER TO BYTE ARRAY CONVERSION */ //@{
    // public static byte[] longAsLittleEnd(long val);/*{{{*/
    /**
     * Converts a long value to an array in little-endian format.
     * \param val The value to be converted.
     * \returns A byte array with the value in little-endian format.
     **/
    public static byte[] longAsLittleEnd(long val) {
        byte[] array = new byte[8];
        return longAsLittleEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] intAsLittleEnd(int val);/*{{{*/
    /**
     * Converts an int value to an array in little-endian format.
     * \param val The value to be converted.
     * \returns A byte array with the value in little-endian format.
     **/
    public static byte[] intAsLittleEnd(int val) {
        byte[] array = new byte[4];
        return intAsLittleEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] shortAsLittleEnd(short val);/*{{{*/
    /**
     * Converts a short value to an array in little-endian format.
     * \param val The value to be converted.
     * \returns A byte array with the value in little-endian format.
     **/
    public static byte[] shortAsLittleEnd(short val) {
        byte[] array = new byte[2];
        return shortAsLittleEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] charAsLittleEnd(char val);/*{{{*/
    /**
     * Converts a char value to an array in little-endian format.
     * \param val The value to be converted.
     * \returns A byte array with the value in little-endian format.
     **/
    public static byte[] charAsLittleEnd(char val) {
        return shortAsLittleEnd((short)val);
    }/*}}}*/
    // public static byte[] longAsBigEnd(long val);/*{{{*/
    /**
     * Convert a long value into an array in big-endian format.
     * \param val The value to be converted.
     * \returns An array with the value \a val in big-endian format. This
     * array will have 8 bytes long.
     **/
    public static byte[] longAsBigEnd(long val) {
        byte[] array = new byte[8];
        return longAsBigEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] intAsBigEnd(int val);/*{{{*/
    /**
     * Convert an int value into an array in big-endian format.
     * \param val The value to be converted.
     * \returns An array with the value \a val in big-endian format. This
     * array will have 4 bytes long.
     **/
    public static byte[] intAsBigEnd(int val) {
        byte[] array = new byte[4];
        return intAsBigEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] shortAsBigEnd(short val);/*{{{*/
    /**
     * Convert a short value into an array in big-endian format.
     * \param val The value to be converted.
     * \returns An array with the value \a val in big-endian format. This
     * array will have 2 bytes long.
     **/
    public static byte[] shortAsBigEnd(short val) {
        byte[] array = new byte[2];
        return shortAsBigEnd(array, 0, val);
    }/*}}}*/
    // public static byte[] charAsBigEnd(char val);/*{{{*/
    /**
     * Convert a char value into an array in big-endian format.
     * \param val The value to be converted.
     * \returns An array with the value \a val in big-endian format. This
     * array will have 2 bytes long.
     **/
    public static byte[] charAsBigEnd(char val) {
        byte[] array = new byte[2];
        return charAsBigEnd(array, 0, val);
    }/*}}}*/
    //@}

    /** \name NUMBER TO ARRAY COPY CONVERSION */ //@{
    // public static byte[] longAsLittleEnd(byte[] a, int i, long v);/*{{{*/
    /**
     * Convert a long value into an array in little-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] longAsLittleEnd(byte[] a, int i, long v) {
        a[i+7] = (byte)(0xFF & (v >> 56));
        a[i+6] = (byte)(0xFF & (v >> 48));
        a[i+5] = (byte)(0xFF & (v >> 40));
        a[i+4] = (byte)(0xFF & (v >> 32));
        a[i+3] = (byte)(0xFF & (v >> 24));
        a[i+2] = (byte)(0xFF & (v >> 16));
        a[i+1] = (byte)(0xFF & (v >>  8));
        a[i+0] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] intAsLittleEnd(byte[] a, int i, int v);/*{{{*/
    /**
     * Convert an int value into an array in little-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] intAsLittleEnd(byte[] a, int i, int v) {
        a[i+3] = (byte)(0xFF & (v >> 24));
        a[i+2] = (byte)(0xFF & (v >> 16));
        a[i+1] = (byte)(0xFF & (v >> 8));
        a[i+0] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] shortAsLittleEnd(byte[] a, int i, short v);/*{{{*/
    /**
     * Convert a short value into an array in little-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] shortAsLittleEnd(byte[] a, int i, short v) {
        a[i+1] = (byte)(0xFF & (v >> 8));
        a[i+0] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] charAsLittleEnd(byte[] a, int i, char v);/*{{{*/
    /**
     * Convert a char value into an array in little-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] charAsLittleEnd(byte[] a, int i, char v) {
        a[i+1] = (byte)(0xFF & (v >> 8));
        a[i+0] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] longAsBigEnd(byte[] a, int i, long v);/*{{{*/
    /**
     * Convert a long value into an array in big-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] longAsBigEnd(byte[] a, int i, long v) {
        a[i+0] = (byte)(0xFF & (v >> 56));
        a[i+1] = (byte)(0xFF & (v >> 48));
        a[i+2] = (byte)(0xFF & (v >> 40));
        a[i+3] = (byte)(0xFF & (v >> 32));
        a[i+4] = (byte)(0xFF & (v >> 24));
        a[i+5] = (byte)(0xFF & (v >> 16));
        a[i+6] = (byte)(0xFF & (v >>  8));
        a[i+7] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] intAsBigEnd(byte[] a, int i, int v);/*{{{*/
    /**
     * Convert an int value into an array in big-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] intAsBigEnd(byte[] a, int i, int v) {
        a[i+0] = (byte)(0xFF & (v >> 24));
        a[i+1] = (byte)(0xFF & (v >> 16));
        a[i+2] = (byte)(0xFF & (v >> 8));
        a[i+3] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] shortAsBigEnd(byte[] a, int i, short v);/*{{{*/
    /**
     * Convert a short value into an array in big-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] shortAsBigEnd(byte[] a, int i, short v) {
        a[i+0] = (byte)(0xFF & (v >> 8));
        a[i+1] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    // public static byte[] charAsBigEnd(byte[] a, int i, char v);/*{{{*/
    /**
     * Convert a char value into an array in big-endian format, copying the
     * result in the passed array at the point especified.
     * \param a The array where the converted value should be copied.
     * \param i The starting point, int the array \a a where the copy should
     * start.
     * \param v The value to be converted and copied.
     * \returns \a a.
     **/
    public static byte[] charAsBigEnd(byte[] a, int i, char v) {
        a[i+0] = (byte)(0xFF & (v >> 8));
        a[i+1] = (byte)(0xFF & v);
        return a;
    }/*}}}*/
    //@}

    /** \name ARRAY TO NUMBER CONVERSION */ //@{
    // public static long  littleEndToLong (byte[] array, int start);/*{{{*/
    /**
     * Convert byte array in a integral type.
     * The conversion is done in the \b little-endian format. Java uses the \b
     * big-endian format as default.
     * \param array The \b byte array to be converted.
     * \param start Index of the first element of the \a array array where the
     * conversion should start.
     * \return The integral type with the result of the conversion.
     * \remarks This function assumes that the sequency of bytes in array is in
     * the \b little-endian format (most significant bytes first).
     **/
    public static long littleEndToLong(byte[] array, int start) {
        return (((long)(array[start+7] & 0xFF) << 56) |
                ((long)(array[start+6] & 0xFF) << 48) |
                ((long)(array[start+5] & 0xFF) << 40) |
                ((long)(array[start+4] & 0xFF) << 32) |
                ((long)(array[start+3] & 0xFF) << 24) |
                ((long)(array[start+2] & 0xFF) << 16) |
                ((long)(array[start+1] & 0xFF) <<  8) |
                ((long)(array[start+0] & 0xFF))
               );
    }/*}}}*/
    // public static int   littleEndToInt  (byte[] array, int start);/*{{{*/
    /**
     * \copydoc littleEndToLong()
     **/
    public static int littleEndToInt(byte[] array, int start) {
        return (((array[start+3] & 0xFF) << 24) |
                ((array[start+2] & 0xFF) << 16) |
                ((array[start+1] & 0xFF) <<  8) |
                (array[start+0] & 0xFF)
               );
    }/*}}}*/
    // public static short littleEndToShort(byte[] array, int start);/*{{{*/
    /**
     * \copydoc littleEndToLong()
     **/
    public static short littleEndToShort(byte[] array, int start) {
        return (short)((array[start+1] << 8) | (array[start] & 0xFF));
    }/*}}}*/
    // public static char  littleEndToChar (byte[] array, int start);/*{{{*/
    /**
     * \copydoc littleEndToLong()
     **/
    public static char littleEndToChar(byte[] array, int start) {
        return (char)((array[start+1] << 8) | (array[start] & 0xFF));
    }/*}}}*/
    // public static long  bigEndToLong    (byte[] array, int start);/*{{{*/
    /**
     * Converts a byte array into long from big-endian format.
     * \param array The byte array to convert from.
     * \param start The starting point, int the array \a a, where the
     * conversion should start.
     * \returns The number after the conversion.
     **/
    public static long bigEndToLong(byte[] array, int start) {
        return (((long)(array[start+0] & 0xFF) << 56) |
                ((long)(array[start+1] & 0xFF) << 48) |
                ((long)(array[start+2] & 0xFF) << 40) |
                ((long)(array[start+3] & 0xFF) << 32) |
                ((long)(array[start+4] & 0xFF) << 24) |
                ((long)(array[start+5] & 0xFF) << 16) |
                ((long)(array[start+6] & 0xFF) <<  8) |
                ((long)(array[start+7] & 0xFF))
               );
    }/*}}}*/
    // public static int   bigEndToInt     (byte[] array, int start);/*{{{*/
    /**
     * Converts a byte array into int from big-endian format.
     * \param array The byte array to convert from.
     * \param start The starting point, int the array \a a, where the
     * conversion should start.
     * \returns The number after the conversion.
     **/
    public static int bigEndToInt(byte[] array, int start) {
        return (((array[start+0] & 0xFF) << 24) |
                ((array[start+1] & 0xFF) << 16) |
                ((array[start+2] & 0xFF) <<  8) |
                (array[start+3] & 0xFF)
               );
    }/*}}}*/
    // public static short bigEndToShort   (byte[] array, int start);/*{{{*/
    /**
     * Converts a byte array into short from big-endian format.
     * \param array The byte array to convert from.
     * \param start The starting point, int the array \a a, where the
     * conversion should start.
     * \returns The number after the conversion.
     **/
    public static short bigEndToShort(byte[] array, int start) {
        return (short)((array[start+0] << 8) | (array[start+1] & 0xFF));
    }/*}}}*/
    // public static char  bigEndToChar    (byte[] array, int start);/*{{{*/
    /**
     * Converts a byte array into char from big-endian format.
     * \param array The byte array to convert from.
     * \param start The starting point, int the array \a a, where the
     * conversion should start.
     * \returns The number after the conversion.
     **/
    public static char bigEndToChar(byte[] array, int start) {
        return (char)bigEndToShort(array, start);
    }/*}}}*/
    //@}

    /** \name BINARY REPRESENTATION */ //@{
    // public static String toString(byte[] array, String separator, int frequency);/*{{{*/
    /**
     * Covert each byte of the array in a two characters string
     * representation.
     * @param array Byte array with values to convert.
     * @param separator A string to separate groups. Can be \b null.
     * @param frequency The frequency to separate groups. See the example.
     * When \a separtor is \b null, \a frequency is ignored.
     * @returns A string with the converted value.
     * @remarks The conversion is done byte by byte, building its
     * representation in a string notation. For example:
     * <pre>
     * byte[] array = { 0x01, 0xA0, 0x2F, 0xFF };
     * arrays.toString(array, "-", 2);
     * arrays.toString(array, " ", 4);
     * </pre>
     * Then it will be converted in this string:
     * <pre>
     * "01-A0-2F-FF"
     * "01A0 2FFF"
     * </pre>
     * Where each byte is represented by two characters.
     **/
    public static String toString(byte[] array, String separator, int frequency) {
        int    limit = arrays.length(array);
        int    extra = (separator == null ? 0 : separator.length() * limit);
        StringBuilder sb = new StringBuilder(limit*2 + extra);

        if (separator != null)
        {
            for (int i = 0; i < limit; i++) {
                if ((i>0) && (((i+1) % frequency) == 0))
                    sb.append(separator);
                sb.append(String.format("%02X", array[i]));
            }
        }
        else
        {
            for (int i = 0; i < limit; i++)
                sb.append(String.format("%02X", array[i]));
        }
        return sb.toString();
    }/*}}}*/
    // public static String toString(byte[] array);/*{{{*/
    /**
     * Converts a byte array into its string representation.
     * This version is similar to the default \c toString(byte[],String,int),
     * as of passed the \a separator parameter as \b null.
     * @param array Byte array with values to convert.
     * @returns A string representation of the array passed in.
     **/
    public static String toString(byte[] array) {
        return toString(array, null, 0);
    }/*}}}*/
    // public static byte[] fromString(String binary);/*{{{*/
    /**
     * Does the oposite of the function \c toString(byte[]).
     * Converts a byte array representation in a string into a real byte
     * array. This function supports separators, as of the \c toString()
     * version can return string using separators. Any character is accepted
     * as a separator along it is not a valid hexadecimal character.
     * @param binary The byte array representation in a string.
     * @return The byte array extracted from the string.
     **/
    public static byte[] fromString(String binary) {
        char[] nibbles;
        int i, x, limit = strings.length(binary);

        if (limit == 0) return new byte[0];

        /* First we remove any non hexadecimal character, forming an array of
         * remaining values.
         */
        nibbles = new char[limit];
        for (i = 0, x = 0; i < limit; i++) {
            if (strings.isHexChar(binary.charAt(i)))
                nibbles[x++] = (char)Character.digit(binary.charAt(i), 16);
        }

        /* Now we converte each two characters into a byte value. */
        int value;
        byte[] result = new byte[x/2];      /* x has the final number of nibbles found. */

        limit = result.length * 2;      /* In the case when x is odd. */
        i = x = 0;
        while (i < limit) {
            value = nibbles[i++] * 16;
            value += nibbles[i++];
            result[x++] = (byte)(value & 0x000000FF);
        }
        return result;
    }/*}}}*/
    // public static String utf8String(byte[] array, int start);/*{{{*/
    /**
     * Convert the array into a modified UTF-8 Java string.
     * \param array Byte array with data to be parsed.
     * \param start The index of the first byte in the array.
     * \remarks The convertion takes the first two bytes as the number of
     * bytes to read. The following sequence must be a valid UTF-8 java
     * encoded string.
     * \return A string with the value parsed or \b null if an error is
     * encontered.
     **/
    public static String utf8String(byte[] array, int start) {
        int limit = arrays.length(array) - start;
        if (limit <= 2) return null;            /* Impossible conversion. */

        int count = 0x0000FFFF & arrays.bigEndToShort(array, start);
        if (count > limit) return null;         /* Invalid length. */

        int i = start, c = 0;
        byte temp;
        char[] data;

        limit = arrays.length(array);

        /* We alloc memory previously to gain performance. */
        data = new char[count];

        while (i < limit)
        {
            /* First bit is 0 or 1: single byte char. */
            if ((array[i] & 0x80) == 0x00)
                data[c] = (char)(array[i] & 0xFF);

            /* First three bits are 110: double byte character. */
            else if ((array[i] & 0xC0) == 0xC0) {
                data[c] = (char)(((array[i] & 0x1F) << 6) | (array[i+1] & 0x3F));
                i++;
            }

            /* First four bits are 1110: three byte character. */
            else if ((array[i] & 0xE0) == 0xE0) {
                data[c] = (char)(((array[i] & 0x0F) << 12) | ((array[i+1] & 0x3F) << 6) | (array[i+2] & 0x3F));
                i+=2;
            }

            /* Otherwise this is a bad encoded string. */
            else
                return null;

            i++;
            c++;
        }

        try { return new String(data, 0, c); }
        catch (Exception ex) { return null; }
    }/*}}}*/
    //@}

    /** \name OTHER HELPER FUNCTIONS */ //@{
    // public static void randomize(byte[] array);/*{{{*/
    /**
     * Fills an array with random values.
     * \param array The byte array to be filled. The entire array will be
     * filled with random values.
     **/
    public static void randomize(byte[] array) {
        Random rnd = new Random(System.currentTimeMillis());
        int    len = array.length;
        int  count = (len / 8);
        int   rest = (len - (count * 8));
        int  index = 0;

        /* Fill in the array with random 64 bits values. */
        for (index = 0; index < count; index+=8) {
            longAsLittleEnd(array, index, rnd.nextLong());
        }

        /* Fill the rest of the array. */
        if (rest >= 4) {
            intAsLittleEnd(array, index, rnd.nextInt());
            index += 4;
            rest  -= 4;
        }

        if (rest >= 2) {
            shortAsLittleEnd(array, index, (short)(rnd.nextInt() & 0x0000FFFF));
            index += 2;
            rest  -= 2;
        }

        if (rest >= 1) {
            array[index] = (byte)(rnd.nextInt() & 0x000000FF);
        }
    }/*}}}*/
    // public static String join(Object[] array, String separator);/*{{{*/
    /**
     * Join elements of an array separated by the given string.
     * \param array The array with objects to join. These objects are
     * converted to String using \c String.valueOf() method.
     * \param separator The separator string to use in the join. If \b null or
     * zero length no separator will be used.
     * \return A string with the elements of the array \a array joined. An
     * empty string will be returned if the array is \b null or zero length.
     **/
    public static String join(Object[] array, String separator) {
        if (arrays.length(array) == 0) return strings.EMPTY;
        int limit = array.length;
        StringBuilder sb = new StringBuilder(256);

        /* We have two for's() to speed up iteration. */
        if (strings.length(separator) > 0)
        {
            for (int i = 0; i < limit; i++) {
                sb.append(String.valueOf(array[i])).append(separator);
            }
            sb.setLength(sb.length() - separator.length());
        }
        else
        {
            for (int i = 0; i < limit; i++) {
                sb.append(String.valueOf(array[i]));
            }
        }
        return sb.toString();
    }/*}}}*/
    //@}
}
// vim:syntax=java.doxygen
