/**
 * \file
 * Defines the INotifyHandler interface.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   outubro 27, 2012
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.ui;

/**
 * Declares a interface that all object that wish to handle system
 * notification messages should implement.
 *//* --------------------------------------------------------------------- */
public interface INotifyHandler
{
    // public boolean onMessage(int msgID, int nParam, int iParam, Object extra);
    /**
     * Receives a message from the message system.
     * \param msgID Identifier of this message.
     * \param nParam First argument of this message. Message specific data.
     * \param lParam Second argument of this message. Message specific data.
     * \param extra Any extra data packed in an Object class.
     * \return Classes that handle messages usually return \b true, which
     * instructs the message system to stop propagation and release the
     * message object.
     **/
    public boolean onMessage(int msgID, int nParam, long lParam, Object extra);
}
// vim:syntax=java.doxygen
