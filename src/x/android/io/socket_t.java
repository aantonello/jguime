/**
 * \file
 * Defines the socket_t class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   October 04, 2010
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.io;

import x.android.defs.ERROR;
import x.android.utils.debug;
import x.android.utils.strings;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;

import java.io.IOException;
import java.nio.channels.IllegalBlockingModeException;

/**
 * \ingroup x_android_io
 * Implements the base socket communication.
 * The class uses the current socket implementation to provide its operations.
 * There is no exception in this implementation. All errors are returned in
 * means of error codes. See \c ERROR class.
 *//* --------------------------------------------------------------------- */
public class socket_t
{
    /** \name CONSTRUCTOR */ //@{
    /**
     * Default constructor.
     * Create an unconnected socket. This is the only constructor available.
     **/
    public socket_t() {
        m_socket = null;
    }
    //@}

    /** \name STREAMS */ //@{
    /**
     * Gets the stream for reading the socket.
     * \returns The InputStream or **null** if there is no current connection.
     **/
    public final InputStream getInputStream() {
        if (m_socket == null) return null;
        try { return m_socket.getInputStream(); }
        catch (Exception ex) {
            debug.w("Exception at socket_t::getInputStream(): '%s'\n", ex.getMessage());
        }
        return null;
    }

    /**
     * Gets the OutputStream to write the socket.
     * \returns The OutputStream implementation or **null** if there is no
     * current connection.
     **/
    public final OutputStream getOutputStream() {
        if (m_socket == null) return null;
        try { return m_socket.getOutputStream(); }
        catch (Exception ex) {
            debug.w("Exception at socket_t::getOutputStream(): '%s'\n", ex.getMessage());
        }
        return null;
    }
    //@}

    /** \name ATTRIBUTES */ //@{
    /**
     * Gets the address of the connected host.
     * \return A string with the textual representation of the address of the
     * connected host or \b null if this socket is not connected.
     **/
    public final String getPeerAddress() {
        if (m_socket == null) return null;
        InetAddress ia = m_socket.getInetAddress();
        if (ia == null) return null;
        return ia.getHostAddress();
    }

    /**
     * Gets the local address of this socket connection.
     * return A string with the local address or \b null if the socket is not
     * connected.
     **/
    public final String getLocalAddress() {
        if (m_socket == null) return null;
        InetAddress ia = m_socket.getLocalAddress();
        if (ia == null) return null;
        return ia.getHostAddress();
    }

    /**
     * Gets the port of the connected host.
     * \returns The port number of the current connection or \c ERROR#NOTFOUND
     * if there is no active connection.
     **/
    public final int    getPeerPort() {
        if (m_socket == null) return ERROR.NOTFOUND;
        int port = m_socket.getPort();
        return (port > 0 ? port : ERROR.NOTFOUND);
    }

    /**
     * Gets the local port number where this socket is bound to.
     * \returns The local port number or \c ERROR#NOTFOUND if the socket is
     * not bound to any port (not connected).
     **/
    public final int    getLocalPort() {
        if (m_socket == null) return ERROR.NOTFOUND;
        int port = m_socket.getLocalPort();
        return ((port > 0) ? port : ERROR.NOTFOUND);
    }

    /**
     * Queries the input stream for the current available data to read.
     * \returns An integer with the number of bytes available to read or an
     * error value which one of those:
     * \retval ERROR::NOCONN The connection was reset.
     * \retval ERROR::IO The connection was closed by the peer.
     * \note Error codes are values less than zero. Zero is a valid return for
     * this function meaning that there is no data to be read.
     **/
    public final int queryDataAvailable() {
        InputStream input = this.getInputStream();

        if (input == null) return ERROR.NOCONN;
        int count = 0;

        try { count = input.available(); }
        catch (Exception ex) {
            debug.w("Exception at socket_t::queryDataAvailable(): '%s'\n", ex.getMessage());
            count = ERROR.IO;
        }
        return count;
    }
    //@}

    /** \name OPERATIONS */ //@{
    /**
     * Opens a connection with a host in the specified address.
     * The operation blocks until the socket is connected or an error occurs.
     * The timeout period is 15 seconds.
     * \param address String with the IP address or URL for reaching the host.
     * \param port The port number to connect to.
     * \return Um error code. Zero means success. Any other value is an error
     * code. Possible values are:
     * \retval ERROR::ACCESS: If the permission to resolve the host was denied.
     * \retval ERROR::PARM: An argument is invalid.
     * \retval ERROR::IO: An I/O exception of any kind.
     * \retval ERROR::EXPIRED: If the connection was not established before the
     *         timeout period has expired.
     * \retval ERROR::FAILED: If the current channel is in non blocking mode.
     * \retval ERROR::UNREACH: This usually means that no network connection
     * could be made. On a cell phone, there is no WiFi or data connection
     * available.
     * \retval ERROR::HOST: The host computer couldn't be found. The server
     * can be down or turned off.
     * \retval ERROR::UNRESOLVED: Could not resolve the passed address.
     * \sa x.android.defs.ERROR
     **/
    public final int  open(String address, int port) {
        int result = 0;
        InetSocketAddress isa = null;

        try {
            isa = new InetSocketAddress(address, port);
        }
        catch (SecurityException se) {
            debug.e(se, "$n in socket_t::open('%s', %d) $s\n", address, port);
            return ERROR.ACCESS;
        }
        catch (IllegalArgumentException iax) {
            debug.e(iax, "$n in socket_t::open('%s', %d) $s\n", address, port);
            return ERROR.PARM;
        }

        m_socket = new Socket();

        try {
            m_socket.setPerformancePreferences(2, 1, 0);
            m_socket.setReceiveBufferSize(1*1024);      // 1K.
            m_socket.setSoLinger(false, 0);
            m_socket.setKeepAlive(true);
            m_socket.connect(isa, 15000);
            return ERROR.SUCCESS;
        }
        catch (IllegalArgumentException iae) {
            debug.e(iae, "$n in socket_t::open('%s', %d) $s\n", address, port);
            result = ERROR.PARM;
        }
        catch (SocketTimeoutException ste) {
            debug.e(ste, "$n in socket_t::open('%s', %d) $s\n", address, port);
            result = ERROR.EXPIRED;
        }
        catch (IllegalBlockingModeException bme) {
            debug.e(bme, "$n in socket_t::open('%s', %d) $s\n", address, port);
            result = ERROR.FAILED;
        }
        catch (IOException ioe) {
            debug.e(ioe, "$n in socket_t::open('%s', %d) $s\n", address, port);
            String errMsg = ioe.getMessage();
            if (strings.length(errMsg) == 0)
                result = ERROR.IO;
            else if (errMsg.indexOf("ENETUNREACH") >= 0)
                result = ERROR.UNREACH;
            else if (errMsg.indexOf("EHOSTUNREACH") >= 0)
                result = ERROR.HOST;
            else if (errMsg.indexOf("unresolved") >= 0)
                result = ERROR.UNRESOLVED;
            else
                result = ERROR.IO;
        }

        /* Only gets here on an error. */
        close();
        return result;
    }

    /**
     * Disconnects and closes the current connection, if any.
     * This operation will invalidate both members \c input and \c output. No
     * exception will be thrown by this function.
     **/
    public final synchronized void close() {
        if (m_socket != null)
        {
            try { m_socket.shutdownInput(); }
            catch (Exception ex) { /* We can ignore this. */ }

            try { m_socket.shutdownOutput(); }
            catch (Exception ex) { /* We can ignore this. */ }

            try { m_socket.close(); }
            catch (Exception ex) { /* We will ignore this either. */ }
        }
        m_socket = null;
    }

    /**
     * Reads bytes from the input stream.
     * The function queries the amount of available data before the reading
     * operation. So, less than the requested buffer size can be stored in the
     * array.
     * \param buffer Array to store the data read.
     * \param offset Index of the first element in \a buffer where the data
     *      should be stored.
     * \param count Total number of bytes to read.
     * \return The number of bytes actually read means success. Zero is a
     * valid value for this return. A integer less than zero is an error code,
     * with the possible values:
     * \retval ERROR::IO The connection was closed.
     * \retval ERROR::NOCONN The connection was reset.
     * \retval ERROR::EOF No more data to be read.
     **/
    public final int  read(byte[] buffer, int offset, int count) {
        InputStream input = this.getInputStream();

        if (input == null) return ERROR.NOCONN;
        int amount = 0;

        try { amount = input.available(); }
        catch (Exception ex) {
            debug.w("Exception at socket_t::read(InputStream::available()): '%s'\n", ex.getMessage());
            return ERROR.IO;
        }

        if (amount > count) amount = count;
        int result = 0;

        try { result = input.read(buffer, offset, amount); }
        catch (Exception ex) {
            debug.w("Exception at socket_t::read(InputStream::read()): '%s'\n", ex.getMessage());
            result = ERROR.IO;
        }
        
        return ((result == -1) ? ERROR.EOF : result);
    }

    /**
     * Writes data in the output stream.
     * The data is automatically flushed in the stream. If the automatic flush
     * is not needed use the \c CStreamWriter#put() method.
     * \param data The data to be written.
     * \param offset Index of the first element in the \a data array to be
     *      sent.
     * \param count The total number of bytes to send, starting from \a
     *      offset.
     * \return The real amount of data sent or an error code with the folloing
     * values:
     * \retval ERROR::NOCONN The connection was reset.
     * \retval ERROR::IO The connection was closed by the peer.
     **/
    public final int  send(byte[] data, int offset, int count) {
        OutputStream output = this.getOutputStream();

        if (output == null) return ERROR.NOCONN;
        int result = 0;

        try {
            output.write(data, offset, count);
            output.flush();
            result = count;
        }
        catch (Exception ex) {
            debug.w("Exception at socket_t::send(OutputStream::write()): '%s'\n", ex.getMessage());
            result = ERROR.IO;
        }
        return result;
    }

    /**
     * Reads the input stream and writes the data into the *stream* object.
     * \param stream Object to store the data. The current write position will
     * be respected.
     * \param count Number of bytes to read from the input stream and written
     * to the *stream*. If this parameter is less than 0, all available data
     * in the input stream will be read.
     * \return On success the function returns the number of bytes read and
     * stored in the `stream_t` object. Zero is a valid value for this result.
     * On failure an error code will be returned. That can be:
     * \retval ERROR::READ The connection was closed.
     * \retval ERROR::NOCONN The connection was reset.
     * \retval ERROR::EOF No more data to be read.
     **/
    public final int read(stream_t stream, int count) {
        InputStream is = this.getInputStream();
        if (is == null) return ERROR.NOCONN;
        return stream.writeFromInputStream(is, count);
    }

    /**
     * Reads data from the *stream_t* object sending it to this socket output
     * stream.
     * \param stream Object `stream_t` with data to send. The data will be
     * read starting from the current read position.
     * \param count Number of bytes to send. If this value is less than zero
     * all available data in *stream* will be read and sent.
     * \return The result is the number of bytes actually sent. Zero is a
     * valid value for this result. If the function fails the result is an
     * error code that can be:
     * \retval ERROR::NOCONN The connection was reset.
     * \retval ERROR::IO The connection was closed by the peer.
     **/
    public final int send(stream_t stream, int count) {
        OutputStream os = this.getOutputStream();
        if (os == null) return ERROR.NOCONN;
        if ((count = stream.readIntoOutputStream(os, count)) > 0)
        {
            try { os.flush(); }
            catch (Exception ex) {
                debug.e(ex, "A ridiculous Exception in a stupid OuputStream::flush() method: $n, $s\n");
            }
        }
        return count;
    }
    //@}

    /** \name OVERRIDABLES */ //@{
    /**
     * Reads bytes from the input stream.
     * The function queries the amount of available data before the reading
     * operation. So less than the requested buffer size can be stored in the
     * array.
     * \param buffer Array to store the data read.
     * \return The number of bytes actually read means success. Zero is a
     * valid value for this return. A integer less than zero is an error code.
     **/
    public int read(byte[] buffer) {
        return read(buffer, 0, buffer.length);
    }

    /**
     * Writes data in the output stream.
     * The data is automatically flushed in the stream. If the automatic flush
     * is not needed use the \c CStreamWriter#put() method.
     * \param data The data to be written.
     * \return The real amount of data sent or an error code.
     **/
    public int send(byte[] data) {
        return send(data, 0, data.length);
    }
    //@}

    /** \name DATA MEMBERS */ //@{
    protected volatile Socket m_socket;         /**< Socket implementation. */
    //@}
}
// vim:syntax=java.doxygen
