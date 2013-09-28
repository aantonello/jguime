/**
 * \file
 * Defines the _package class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   August 24, 2011
 *
 * \par Copyright
 * This file is provided in hope that it will be useful to someone. It is
 * offered in public domain. You may use, modify or distribute it freely.
 *
 * The code is provided "AS IS". There is no warranty at all, of any kind. You
 * may change it if you like. Or just use it as it is.
 */
package x.android.nms;

/**
 * \defgroup x_android_nms Notification Message System
 * This package has all the classes needed to implement the particular
 * notification message system (NMS).
 * The main particularity of this system is that it's not needed to create a
 * new \c Handler (android.os.Handler) object for every needed target. Classes
 * must only implement the \c INHandler interface and that's it.
 *
 * The main class of this system is \c sender. Sender is a kind of class that
 * has a single instance. That is, a singleton class, and does not need to be
 * instantiated in the application. The first call of one of it's methods will
 * create an instance and associate it with the main thread of an application.
 *
 * That is very important. Messages are only sent to the main thread of an
 * application. Parallel threads should never need them.
 * @{ *//* ---------------------------------------------------------------- */
///@} x_android_nms
// vim:syntax=java.doxygen
