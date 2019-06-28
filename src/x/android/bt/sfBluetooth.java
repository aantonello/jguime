/**
 * \file
 * Defines the sfBluetooth class.
 *
 * \author:  Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date:    Dezembro 17, 2013
 * \since:   jguime 2.4
 * \version: 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.bt;

import java.util.*;

import android.os.Build;
import android.content.Context;
import android.bluetooth.*;

import x.android.defs.ENC;
import x.android.utils.debug;
import x.android.utils.strings;
import x.android.ui.CAndroidApp;

/**
 * \ingroup x_android_bt
 * Static class with helper methods for bluetooth management.
 *//* --------------------------------------------------------------------- */
public final class sfBluetooth
{
    /** \name Adapter Operations */ //@{
    /**
     * Retrives the single BluetoothAdapter instance of the system.
     * The function queries the system version to choose the right method for
     * getting the adapter.
     * @return The single BluetoothAdapter instance. If this function returns
     * \b null, there is no bluetooth adapter in this device.
     **/
    public static BluetoothAdapter getAdapter() {
        if (Build.VERSION.SDK_INT <= 17 /*Build.VERSION_CODES.JELLY_BEANS_MR1*/)
            return BluetoothAdapter.getDefaultAdapter();
        else
        {
            CAndroidApp theApp = CAndroidApp.currentApp();
            BluetoothManager bluetoothManager = (BluetoothManager)theApp.getSystemService(Context.BLUETOOTH_SERVICE);

            if (bluetoothManager == null)
                return null;

            return bluetoothManager.getAdapter();
        }
    }

    /**
     * Gets the friendly name of the local bluetooth adapter.
     * @param adapter The system adapter. Can be \b null.
     * @return A string with the system adapter friendly name. If the system
     * doesn't have a bluetooth adapter, the result will be an empty string.
     **/
    public static String getAdapterName(BluetoothAdapter adapter) {
        if (adapter == null) adapter = getAdapter();
        if (adapter == null)
            return strings.EMPTY;

        return adapter.getName();
    }

    /**
     * Starts listen to a remote connection using the specified service record
     * UUID.
     * This function will add the service record to be discoverable in SDP
     * lookup. Allowing remote devices to connect to the returned socket by
     * using #openSocket().
     * @param name The name of the service to be presented to the user.
     * @param uuid UUID for the service.
     * @return A almost ready \c SFBluetoothSocket to be used as server
     * socket. Call SFBluetoothSocket#accept() to wait a client connection. If
     * this function returns \b null, the system doesn't have a bluetooth
     * adapter or it is not enabled.
     **/
    public static SFBluetoothSocket listen(String name, UUID uuid) {
        BluetoothAdapter adapter = getAdapter();

        if (adapter == null) return null;

        BluetoothServerSocket server;

        try {
            server = adapter.listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (Exception ex) {
            debug.e(ex, "$n in sfBluetooth::listen(String, UUID): '$s'\n");
            return null;
        }

        return new SFBluetoothSocket(server);
    }

    /**
     * Starts listen to a remote connection using the specified service record
     * UUID.
     * This function will add the service record to be discoverable in SDP
     * lookup. Allowing remote devices to connect to the returned socket by
     * using #openSocket().
     * @param name The name of the service to be presented to the user.
     * @param uuid A string used as a namespace for the function to generate a
     * UUID from. This is \b not a UUID in textual format.
     * @return A almost ready \c SFBluetoothSocket to be used as server
     * socket. Call SFBluetoothSocket#accept() to wait a client connection. If
     * this function returns \b null, the system doesn't have a bluetooth
     * adapter or it is not enabled.
     **/
    public static SFBluetoothSocket listen(String name, String uuid) {
        byte[] namespace = strings.encode(uuid, ENC.UTF8);
        UUID   realUUID  = UUID.nameUUIDFromBytes(namespace);

        return listen(name, realUUID);
    }
    //@}

    /** \name Device Operations */ //@{
    /**
     * Opens a socket to an specific service in a device.
     * The function will create and return a \c SFBluetoothSocket for the
     * connection to be made. To be used, you must call
     * SFBluetoothSocket#connect().
     *
     * \c SFBluetoothSocket.connect() will search the service through SDP
     * lookup, establishing the connection when the specific channel is found.
     * @param device BluetoothDevice to connect to.
     * @param serviceUUID UUID of the service to connect to.
     * @return A \c SFBluetoothSocket almost ready to go. \b null if the
     * specified service was not found, the channel is already in use or one
     * of the arguments is invalid.
     **/
    public static SFBluetoothSocket openSocket(BluetoothDevice device, UUID serviceUUID) {
        if ((device == null) || (serviceUUID == null)) return null;

        BluetoothSocket socket;

        try { socket = device.createRfcommSocketToServiceRecord(serviceUUID); }
        catch (Exception ex) {
            debug.e(ex, "$n in sfBluetooth::openSocket(UUID): '$s'\n");
            return null;
        }
        return new SFBluetoothSocket(socket);
    }

    /**
     * Opens a socket to an specific service in a device.
     * The function will create and return a \c SFBluetoothSocket for the
     * connection to be made. To be used, you must call
     * SFBluetoothSocket#connect().
     *
     * \c SFBluetoothSocket.connect() will search the service through SDP
     * lookup, establishing the connection when the specific channel is found.
     * @param device BluetoothDevice to connect to.
     * @param stringUUID A string used as a namespace for the function to
     * generate a UUID from. This is \b not a UUID in textual format.
     * @return A \c SFBluetoothSocket almost ready to go. \b null if the
     * specified service was not found, the channel is already in use or one
     * of the arguments is invalid.
     **/
    public static SFBluetoothSocket openSocket(BluetoothDevice device, String stringUUID) {
        byte[] byteUUID = strings.encode(stringUUID, ENC.UTF8);
        UUID   realUUID = UUID.nameUUIDFromBytes(byteUUID);

        return openSocket(device, realUUID);
    }
    //@}
}
// vim:syntax=java.doxygen
