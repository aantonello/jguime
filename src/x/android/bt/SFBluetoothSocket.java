/**
 * \file
 * Defines the SFBluetoothSocket class.
 *
 * \author:  Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date:    Dezembro 16, 2013
 * \since:   jguime 2.4
 * \version: 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.bt;
/* #imports {{{ */
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothServerSocket;

import x.android.defs.ERROR;
import x.android.utils.debug;
import x.android.utils.arrays;
import x.android.io.CStreamReader;
import x.android.io.CStreamWriter;
import x.android.io.stream_t;
/* #imports }}} */
/**
 * \ingroup x_android_bt
 * Wrapper class for BluetoothSocket.
 *//* --------------------------------------------------------------------- */
public class SFBluetoothSocket implements Closeable
{
    /** \name Constructors */ //@{
    // SFBluetoothSocket(BluetoothSocket socket);/*{{{*/
    /**
     * Default constructor.
     * @param socket The raw socket implementation for this class to wrap.
     **/
    SFBluetoothSocket(BluetoothSocket socket)
    {
        m_socket = socket;
        m_server = null;
        m_connected = false;
    }/*}}}*/
    // SFBluetoothSocket(BluetoothServerSocket server);/*{{{*/
    /**
     * Builds a socket as a server.
     * @param server The server socket to be used.
     **/
    SFBluetoothSocket(BluetoothServerSocket server)
    {
        m_socket = null;
        m_server = server;
        m_connected = false;
    }/*}}}*/
    //@}

    /** \name Attributes */ //@{
    // public final boolean isConnected();/*{{{*/
    /**
     * Checks whether this socket is already connected or not.
     * @returns \b true when the socket is already connected with the device.
     * Otherwise \b false.
     **/
    public final boolean isConnected()
    {
        return m_connected;
    }/*}}}*/
    // public final int queryDataAvailable();/*{{{*/
    /**
     * Queries how much data is available to read in the InputStream.
     * The function will retrieve the InputStream associated with this socket
     * and query how much data is ready to be read.
     * @return An integer equals to or greater than zero means the amount of
     * data ready to be read. A value less than zero indicates an error code.
     * Error codes can be returned when the socket is not connected yet or was
     * already closed.
     **/
    public final int queryDataAvailable()
    {
        InputStream is = this.getInputStream();

        if (is == null) return ERROR.NOCONN;
        int count = 0;

        try { count = is.available(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::queryDataAvailable(): '$s'\n");
            count = ERROR.READ;
        }
        return count;
    }/*}}}*/
    //@}

    /** \name Operations */ //@{
    // public final boolean connect();/*{{{*/
    /**
     * Starts a connection with the device that created this socket.
     * This is a blocking operation. It must be called from a thread that is
     * not the main thread. The function will return when the connection is
     * made or when an error occurs, like a timeout interval or calling
     * SFBluetoothSocket#close() from another thread.
     * @return \b true if the connection was made. Otherwise \b false.
     * @remarks Don't use this method on server sockets. Instead, use the 
     * SFBluetoothSocket#accept() to accept a connection from a client device.
     **/
    public final boolean connect()
    {
        if (m_socket == null) return false;

        try { m_socket.connect(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::connect(): '$s'\n");
            return false;
        }
        
        m_connected = true;
        return true;
    }/*}}}*/
    // public final boolean accept(int timeout);/*{{{*/
    /**
     * Waits a client connection.
     * This function will wait for a client connection when this socket is
     * retrived from \c sfBluetooth.listen(). When this socket was created
     * with \c sfBluetooth.openSocket() this function is a noop.
     *
     * As of #connect() this function blocks the current thread execution
     * until a connection is made or canceled. You can call #cancel() from
     * another thread, at any time, to cancel the wait.
     * @param timeout Timeout period to wait, in milliseconds. If \c
     * -1 is passed, this method waits forever.
     * @return \b true if a connection was made. \b false when the timeout
     * period ellapses without a connection or \c cancel() was called.
     **/
    public final boolean accept(int timeout)
    {
        if (m_server == null) return false;

        try {
            if (timeout < 0)
                m_socket = m_server.accept();
            else
                m_socket = m_server.accept(timeout);

            m_server.close();
        } catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::accept(timeout: %d): '$s'\n", timeout);
        }
        m_server = null;
        m_connected = (m_socket != null);
        return m_connected;
    }/*}}}*/
    // public final void    cancel();/*{{{*/
    /**
     * Cancel the listen state for a remote connection.
     * This function should be used in another thread to cancel a call to
     * #accept(). If there is no call to \c accept() or this socket was not
     * created with  sfBluetooth#listen(), this function does nothing.
     **/
    public final void cancel()
    {
        if (m_server != null)
        {
            try { m_server.close(); }
            catch (Exception ex) {
                debug.e(ex, "$n in SFBluetoothSocket::cancel(): '$s'\n");
            }
        }
        m_server = null;
    }/*}}}*/
    // public final InputStream getInputStream();/*{{{*/
    /**
     * Retrieves the InputStream associated with this socket.
     * This will retrieve an InputStream even if the connection was not
     * accomplished yet. When you try to use the stream, an exception will be
     * thrown if the connection is not ready.
     * @return The InputStream implementation associated with this socket.
     * Returns \b null if the socket was closed.
     **/
    public final InputStream getInputStream()
    {
        if (m_socket == null) return null;

        try { return m_socket.getInputStream(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::getInputStream(): '$s'\n");
        }
        return null;
    }/*}}}*/
    // public final OutputStream getOutputStream();/*{{{*/
    /**
     * Retrieves the OutputStream associated with this socket.
     * As of the #getInputStream() function, the output stream will be
     * returned even when the socket is not connected yet. Before the socket
     * becomes connected, trying to use this OutputStream will result in an
     * exception thrown.
     * @return The OutputStream implementation of this socket. \b null if
     * the socket was closed.
     **/
    public final OutputStream getOutputStream()
    {
        if (m_socket == null) return null;

        try { return m_socket.getOutputStream(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::getOutputStream(): '$s'\n");
        }
        return null;
    }/*}}}*/
    //@}

    /** \name Closeable Implementation */ //@{
    // public void close();/*{{{*/
    /**
     * Closes the current connection.
     * Also, this function can be used while #connect() is blocking a
     * thread. It is safe to call this method on an already closed socket.
     * No exceptions are thrown. This is the only way to abort a #connect()
     * operation.
     **/
    public void close()
    {
        if (m_socket == null) return;

        try { m_socket.close(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::close(): '$s'\n");
        }
        m_connected = false;
    }/*}}}*/
    //@}

    /** \name Reader/Writer Wrappers */ //@{
    // public final CStreamReader getStreamReader();/*{{{*/
    /**
     * Wrap the InputStream of this socket in a CStreamReader class.
     * @return A CStreamReader class wrapping the InputStream associated with
     * this socket or \b null if this socket was already closed.
     **/
    public final CStreamReader getStreamReader()
    {
        InputStream is = getInputStream();
        if (is == null) return null;

        return new CStreamReader(is);
    }/*}}}*/
    // public final CStreamWriter getStreamWriter();/*{{{*/
    /**
     * Wrap the OutputStream into a CStreamWriter class.
     * @return A CStreamWriter objeto wrapping the OutputStream associated
     * with this socket or \b null if the socket was already closed.
     **/
    public final CStreamWriter getStreamWriter()
    {
        OutputStream os = getOutputStream();
        if (os == null) return null;

        return new CStreamWriter(os);
    }/*}}}*/
    //@}

    /** \name Read/Write Operations */ //@{
    // public final int read(byte[] buffer, int offset, int count);/*{{{*/
    /**
     * Reads data into the buffer.
     * @param buffer Array of bytes where the function will put the read data.
     * @param offset Offset, from the first byte of the \a buffer array where
     * the function will start to write the read data.
     * @param count The amount of data to read. The function will compare this
     * value with the amount of data ready to be read. If this value is less
     * than zero or greater than the amount of available data, it will be
     * adjusted. Also, the \a count value will be compared with the space
     * available in the \a buffer array. The function will make the
     * adjustments if needed.
     * @return A value equals to or greater than zero means the number of byte
     * read and stored in the \a buffer array. A value less than zero
     * indicates an error code. As noted:
     * - \c ERROR.PARM: If \a buffer is null or zero length, offset is less
     *      than zero or greater than \a buffer length.
     * - \c ERROR.NOCONN: If this socket is already closed.
     * - \c ERROR.READ: When we could not read the stream for some reason.
     * - \c ERROR.EOF: When we detect the end of the stream.
     * .
     **/
    public final int read(byte[] buffer, int offset, int count)
    {
        final int space = arrays.length(buffer);

        if ((space == 0) || (offset < 0) || (offset >= space))
            return ERROR.PARM;

        InputStream input = this.getInputStream();

        if (input == null) return ERROR.NOCONN;
        int amount = 0;

        try { amount = input.available(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket:read(byte[]): '$s'\n");
            return ERROR.READ;
        }

        if (amount > count) amount = count;
        if (amount > (space - offset)) amount = space - offset;
        int result = 0;

        try { result = input.read(buffer, offset, amount); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::read(byte[]): '$s'\n");
            result = ERROR.READ;
        }
        return ((result == -1) ? ERROR.EOF : result);
    }/*}}}*/
    // public final int send(byte[] buffer, int offset, int count);/*{{{*/
    /**
     * Sends data to the connected peer.
     * @param buffer Byte array with data to send.
     * @param offset Offset from the start of \a buffer array where the
     * function should start sending data.
     * @param count The number of bytes to send starting from \a offset. This
     * method is permissive in the way that it will check this value against
     * the size of the \a buffer array starting from \a offset. The function
     * will make adjustments as needed. For this value, -1 is a valid counting
     * when all data starting from \a offset should be sent.
     * @return An integer equals to or greater than zero is the number of
     * bytes actually sent. A value less than zero indicates an error code. As
     * in the following list:
     * - \c ERROR.PARM: If \a buffer is null, offset is less than zero or
     *      greater than the length of \a buffer.
     * - \c ERROR.NOCONN: If the socket was already closed.
     * - \c ERROR.WRITE: When an I/O error occurs.
     * .
     **/
    public final int send(byte[] buffer, int offset, int count)
    {
        final int amount = arrays.length(buffer);

        if ((offset < 0) || (offset > amount))
            return ERROR.PARM;

        if (amount == 0) return 0;
        if (count > (amount - offset)) count = (amount - offset);

        OutputStream output = this.getOutputStream();

        if (output == null) return ERROR.NOCONN;
        int result = 0;

        try {
            output.write(buffer, offset, count);
            output.flush();
            result = count;
        } catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::send(byte[]): '$s'\n");
            result = ERROR.WRITE;
        }
        return result;
    }/*}}}*/
    // public final int read();/*{{{*/
    /**
     * Reads one byte from the InputStream.
     * The function blocks until at least one byte is available to be read.
     * @return The byte read, as an Integer type. If an error occurs the
     * return value will be less than zero indicating an error condition. \c
     * ERROR.EOF will be returned if the end of the stream is reached.
     **/
    public final int read()
    {
        InputStream is = this.getInputStream();
        if (is == null) return ERROR.NOCONN;

        int result = 0;
        try { result = is.read(); }
        catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::read(): '$s'\n");
            return ERROR.READ;
        }
        return ((result == -1) ? ERROR.EOF : result);
    }/*}}}*/
    // public final int send(int aByte);/*{{{*/
    /**
     * Sends a single byte to the connected peer.
     * @param aByte The byte value to send. Only the least significant byte of
     * this integer will be sent.
     * @return The number of bytes sent (one) or an error value if the
     * returned value is less than zero.
     **/
    public final int send(int aByte)
    {
        OutputStream os = this.getOutputStream();
        if (os == null) return ERROR.NOCONN;

        try {
            os.write(aByte);
            os.flush();
        } catch (Exception ex) {
            debug.e(ex, "$n in SFBluetoothSocket::send(): '$s'\n");
            return ERROR.WRITE;
        }
        return 1;
    }/*}}}*/
    // public final int read(stream_t stream, int count);/*{{{*/
    /**
     * Reads the InputStream writing the passed stream_t.
     * @param stream \c stream_t to write to.
     * @param count Number of bytes to read from the InputStream and write
     * into the passed \a stream. If this value is less than zero the function
     * will atempty to read all available bytes in the InputStream.
     * @return The number of bytes actually read from the InputStream of this
     * socket and written to the \a stream buffer. Zero is a valid value for
     * this operation when there is no data in the InputStream. If an error
     * occurr, the result will be less than zero (an error code).
     **/
    public final int read(stream_t stream, int count)
    {
        InputStream is = this.getInputStream();
        if (is == null) return ERROR.NOCONN;
        return stream.writeFromInputStream(is, count);
    }/*}}}*/
    // public final int send(stream_t stream, int count);/*{{{*/
    /**
     * Writes the OutputStream with data passed from an stream_t.
     * @param stream Data to be written.
     * @param count Number of bytes to write. If this value is less than zero
     * all available bytes in the \a stream will be written.
     * @return The number of bytes actually written. If the result value is
     * less than zero, it represents an error condition.
     **/
    public final int send(stream_t stream, int count)
    {
        OutputStream os = this.getOutputStream();
        if (os == null) return ERROR.NOCONN;
        if ((count = stream.readIntoOutputStream(os, count)) > 0)
        {
            try { os.flush(); }
            catch (Exception ex) {
                debug.e(ex, "$n in SFBluetoothSocket::send(stream_t): '$s'\n");
                return ERROR.WRITE;
            }
        }
        return count;
    }/*}}}*/
    //@}

    /** \name Data Members */ //@{
    protected BluetoothSocket m_socket;     /**< Raw bluetooth socket.  */
    private BluetoothServerSocket m_server; /**< Local temporary.       */
    private boolean  m_connected;           /**< Whether socket is connected. */
    //@}
}
// vim:syntax=java.doxygen
