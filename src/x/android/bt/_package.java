/**
 * \file
 * BT package documentation.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   Dezembro 16, 2013
 * \since  jguime 2.4
 *
 * \copyright 2013, Paralaxe Tecnologia. All right reserved.
 */
package x.android.bt;

/**
 * \defgroup x_android_bt Bluetooth 
 * Helper classes to assist in bluetooth communications.
 * As of in others packages the goal of this one is to minimize the number of
 * throwed exceptions. Exceptions are used only in places where they make
 * sence and, in most methods, are replaced by result codes or \b null return
 * values. Classes found in this package:
 * - \b SFBluetoothSocket: Input and output in one single class. This is a
 *      wrapper class that will be created when using \c sfBluetooth.listen()
 *      or \c sfBluetooth.openSocket().
 * - \b sfBluetooth: Static class to help getting the BluetoothAdapter
 *      instance and others operations. Used to start a server listening
 *      operation or to start a client socket.
 * .
 * @{ *//* ---------------------------------------------------------------- */
///@} x_android_bt Bluetooth
