/**
 * \file
 * Defines the cache_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Janeiro 12, 2013
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.nms;

import java.util.*;

/**
 * Implements a cache for notification messages.
 * The cache is used to store instances of \c msg_t classes that are in use or
 * available to be used. This improve speed against memory avoiding the
 * notification system the need to reallocating memory at every notification
 * sent.
 *
 * The cache is totally controlled by the \c issuer class and the cache_t
 * class cannot be accessed out side of this package.
 *//* --------------------------------------------------------------------- */
class cache_t extends Vector<msg_t>
{
    /** \name CONSTRUCTOR */ //@{
    /**
     * Default constructor.
     **/
    public cache_t() {
    }
    //@}

    /** \name PUBLIC INTERFACE */ //@{
    /**
     * Adds a \c msg_t instance in this list.
     * The object will be added at the end of the list.
     * \param m The object to be added.
     **/
    final void  push(msg_t m) {
        super.add(m);
    }

    /**
     * Get the first available \c msg_t instance from this list.
     * If there is no objects available the function will create a new one.
     * If, at least, one object is available it is removed from the list
     * before the function returns. The passed arguments are used to configure
     * the \c msg_t object returned.
     **/
    final msg_t get(INHandler h, int id, int np, Object o, long lp, long t) {
        msg_t msg = null;

        if (this.isEmpty())
            msg = new msg_t(h, id, np, o, lp, t);
        else
        {
            try { msg = this.remove(0); }
            catch (Exception e) { msg = new msg_t(); }

            msg.set(h, id, np, o, lp, t);
        }
        return msg;
    }

    /**
     * Finds and removes the specified object from this list and remove it.
     * \param msg Object to be removed.
     * \returns If the specified object is in this list the result will be it.
     * Otherwise the function returns \b null.
     **/
    final msg_t remove(msg_t msg) {
        return (super.remove(msg) ? msg : null);
    }

    /**
     * Finds and removes a \c msg_t object having the specified target.
     * \param h The target handler of the messages to be removed.
     * \return The removed \c msg_t object means success. \b null if no \c
     * msg_t object was found.
     **/
    final msg_t remove(INHandler h) {
        for (msg_t m : this) {
            if (m.target == h) {
                return this.remove(m);
            }
        }
        return null;
    }

    /**
     * Search and remove a \c msg_t object having the specified arguments.
     * \param h Target handler of the \c msg_t object.
     * \param id The message identifier of the \c msg_t object.
     * \return The removed \c msg_t object means success. \b null if no \c
     * msg_t object was found.
     **/
    final msg_t remove(INHandler h, int id) {
        for (msg_t m: this) {
            if ((m.target == h) && (m.msgID == id)) {
                return this.remove(m);
            }
        }
        return null;
    }

    /**
     * Search and remove a \c msg_t object having the specified arguments.
     * \param h Target handler of the \c msg_t object.
     * \param id The message identifier of the \c msg_t object.
     * \param np The long parameter of the \c msg_t object.
     * \return The removed \c msg_t object means success. \b null if no \c
     * msg_t object was found.
     **/
    final msg_t remove(INHandler h, int id, int np) {
        for (msg_t m: this) {
            if ((m.target == h) && (m.msgID == id) && (m.nParam == np)) {
                return this.remove(m);
            }
        }
        return null;
    }
    //@}
}
// vim:syntax=java.doxygen
