/**
 * \file
 * Defines the subscribers class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Junho 18, 2013
 *
 * \copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.nms;

import java.util.*;
import x.android.utils.*;

/**
 * Class that represents a list of bradcast messages and its subscribers.
 * As extending Hashtable class, each subscriber is bounded to a broadcast
 * message. Broadcast messages are named with strings and are always unique.
 * That is, there is only one entry in the hashtable for every broadcast
 * message.
 *
 * A message can have many subscribers. Subscribers must implement the \c
 * INHandler interface to receive the notification. Subscriptions can be made
 * using the #add() method. Subscriptions can be undone using the
 * #remove() method.
 *
 * When a message to a broadcast notification is sent, all classes subscribed
 * to that message will receive the notification immediatly. Whan a message to
 * a broadcast notification is posted, all classes subscribed to that
 * notification will be scheduled to receive the message. If one class
 * unsubscribe to that notification before the message is sent, it will be
 * canceled and no message will be sent to that object.
 *//* --------------------------------------------------------------------- */
class subscribers
{
    /** \name CONSTRUCTOR */ //@{
    /**
     * Default constructor.
     **/
    public subscribers() {
        m_table = new Hashtable<String, Vector<INHandler>>(1);
    }
    //@}

    /** \name OPERATIONS */ //@{
    /**
     * Add a subscription of the *client* to a *broadcast notification*.
     * If the notification doesn't exist in the list, a new entry will be
     * created for it. Also, if the client is already subscribed to that
     * notification, nothing will be done.
     * \param notificationName String identifying the broadcast
     * notification to which the handle will be subscribed to.
     * \param client Object implementing the `INHandler` interface subscribing
     * to the broadcast notification.
     * \return The function returns *this*. No success or failure is
     * importante in this operation.
     **/
    final synchronized subscribers add(String notificationName, INHandler client) {
        Vector<INHandler> clients = m_table.get(notificationName);

        if (clients == null)
        {
            clients = new Vector<INHandler>(1);
            try { m_table.put(notificationName, clients); }
            catch (Exception ex) {
                debug.w("subscribers::add('%s') failed with exception: '%s'\n",
                        notificationName, ex.getMessage());

                return this;
            }
        }
        else if (clients.contains(client)) {
            return this;
        }
        clients.add( client );

        return this;
    }

    /**
     * Remove a *client* from receiving a *notification*.
     * Removing clients from this list also prevents for they to received
     * scheduled messages waiting to be processed.
     * \param notificationName Notification name for what the client wants to
     * be unsubscribed.
     * \param client The object implementing `INHandler`.
     * \return The function returns *this*. There is no valuable error in this
     * operations.
     * \remarks When a broadcast notification become totaly empty (with no
     * clients) it will be removed from the list.
     **/
    final synchronized subscribers remove(String notificationName, INHandler client) {
        Vector<INHandler> clients = m_table.get(notificationName);

        if (clients == null) {    /* No client subscribed. */
            return this;
        }

        if (!clients.remove(client)) {   /* Object not found. */
            return this;
        }

        /* If the list contains no elements we can remove this notification
         * from the hashtable.
         */
        if (clients.isEmpty())
        {
            try { m_table.remove(notificationName); }
            catch (Exception ex) {
                debug.w("Unimportante exception in subscribers::remove('%s')\n", notificationName);
            }
        }
        return this;
    }

    /**
     * Gets all clients subscribed to a particular notification.
     * \param notificationName Name of the notification to get the list of
     * subscribers.
     * \return An `ArrayList` object containing the list of subscribers for
     * the notification. **Notice** that the result can be **null** and the
     * list can also be **empty**.
     **/
    final Vector<INHandler> getList(String notificationName) {
        return m_table.get(notificationName);
//        if (!debug.enabled)
//            return m_table.get(notificationName);
//        else
//        {
//            if (!m_table.containsKey(notificationName))
//                debug.w("subscribers::getList() table doens't has key '%s'\n", notificationName);
//            else
//            {
//                Vector<INHandler> list = m_table.get(notificationName);
//                if (list == null)
//                    debug.w("subscribers::getList() table '%s' is empty!\n", notificationName);
//
//                return list;
//            }
//            return null;
//        }
    }

    /**
     * Gets a snapshot of the clients subscribed a notification.
     * A snapshot is an array of objects contained in the specified list in
     * this particular moment. The list can change while the array is
     * processed with no harm.
     * \param notificationName Name of the broadcast notification to get the
     * list of subscribers.
     * \return An array of objects subscribed to the notification defined.
     * **Notice** that the result can be \b null if there is no client to that
     * notification.
     **/
    final INHandler[] get(String notificationName) {
        Vector<INHandler> clients = this.getList(notificationName);
        if (clients == null || clients.isEmpty())
            return null;

        int size = clients.size();
        INHandler[] handlers = new INHandler[ size ];

        try { handlers = clients.toArray(handlers); }
        catch (Exception ex) {
            debug.w("subscribers::get('%s'): '%s'\n",
                    notificationName, ex.getMessage());
            return null;
        }
        return handlers;
    }
    //@}

    /** \name DATA MEMBERS */ //@{
    /**
     * Holds the list of broadcast notifications and its subscribers.
     **/
    Hashtable<String, Vector<INHandler>> m_table;
    //@}
}
// vim:syntax=java.doxygen
