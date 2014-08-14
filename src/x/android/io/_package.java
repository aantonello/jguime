/**
 * \file
 * Defines the _package class.
 *
 * \author Alessandro Antonello <aantonello@paralaxe.com.br>
 * \date   novembro 01, 2013
 * \since  jguime 2.4
 * \version 1.0
 *
 * \copyright
 * Paralaxe Tecnologia, 2013. All rights reserved.
 */
package x.android.io;

/**
 * \defgroup x_android_io I/O Package
 * This package has classes to assist reading and write streams.
 * The use of the standard Java interfaces require to much verbose code and \c
 * try \c cath blocks to write. This set of classes never, ever throws an
 * exception and let you decide what to do when a value isn't available.
 *
 * Classes found in this package:
 * - \b CBinaryReader: Implements DataInput and let you read any value from a
 *   byte array in memory. Has support to read numbers in \e Little-Endian
 *   notation and to read string from several encodings.
 * - \b CBinaryWriter: Implements DataOutput and let you write any value to a
 *   byte array in memory. Has support to write numbers in \e Little-Endian
 *   notation and to write strings in several encodings.
 * - \b CStreamReader: Implements DataInput and let you read any InputStream.
 *   The goal of this class is to catch all exceptions returning alterantive
 *   values. Also has the same support of CBinaryReader to read number in \e
 *   Little-Endian notation and strings from several encodings.
 * - \b CStreamWriter: Implements DataOutput and let you write any
 *   OutputStream. The goal of this class is to catch all exceptions returning
 *   alterantive values. Also has the same support of CBinaryWriter to write
 *   number in \e Little-Endian notation and strings of several encodings.
 * - \b stream_t: This is a two way, input and output stream class.
 *   Implementing at same time DataInput and DataOutput can server as an
 *   efficient memory buffer. Has the same capabilities of CBinaryReader and
 *   CBinaryWriter for reading and write numbers in \e Little-Endian notation
 *   and support for several string encodings, zero terminated or not. It also
 *   help you with exceptions. It doesn't throw any exception. Instead this
 *   class holds an error code the you can check when needed.
 * - \b socket_t: This class is an implementation of three Java classes:
 *   Socket, InputStream and OutputStream. This means that you can read and
 *   write to a socket, working in sending or retrieving streams of data with
 *   this single class.
 * .
 * @{ *//* ---------------------------------------------------------------- */
///@} x_android_io
// vim:syntax=java.doxygen
