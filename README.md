JGUIME
======

JGUIME is an acronym from Java Graphic User Interface for Micro Edition.

Back on 2007, JGUIME was a library to build a user interface on cell phones
supported by J2ME. Today it is a library to interface and support Android and
Java.

It carries some of my personal tastes about development and my feelings about
Exception handling in Java (It's too much).

Today all code especially built to J2ME was removed. Only the interface with
Java and Android remains.

Directory Structure
-------------------

+ **docs**: Some notes taken. This is where the documentation generated with
[Doxygen](http://www.doxygen.org) is written. Why Doxygen? Because it is much
smarter than JavaDoc.
+ **res**: Resources. It is not packed inside the `.jar` file. Instead it is
published as is to be inserted in the `assets` directory of an Android
project. Android lacks the implementation supporting resources inside `jar` files.
+ **src**: All source code is inside of this directory.
  + **x/android**: Root directory.
    + **defs**: Constant declarations.
    + **utils**: A bunch of useful classes.
    + **io**: Classes to read and write streams.
    + **bt**: Bluetooth support.
    + **xml**: Simplistic XML reading and writing.
    + **nms**: A distributed message system. Was built to be used in J2ME.
        Still useful in Android development. Largely used in my Apps.
    + **ui**: Some classes and interfaces to circumvent some Android oddities.

Files in the root folder
------------------------

The root of the library directory has these files:

+ `make.inc`: A common makefile include file having some variables
    definitions. Why a makefile you will ask. Well, essentially I was writing
    my own makefiles too long before starting develop in Java. Also, a
    `build.xml` used with [Ant](http://ant.apache.org) is just a makefile with
    an XML makeup. The `build.xml` file is not supposed to be written by hand.
    You should use Eclipse or something to do this job. I don't. I prefer to
    organize my projects with a similar structure and don't be dependent of
    some IDE to do the job. So, whenever is possible, I use makefiles written
    by hand and taking a fine control of what will be built and what will not.

    Well, to build this library in another machine some variables will need to
    be defined or changed. This is the file where variables are set. So, this
    is the place.
+ `Makefile`: The main makefile script. Includes `make.inc` and define the
    targets. To build the library you type:

        make

    To build the documentation using Doxygen you type:

        make docs

    To publish the library to a particular location you type:

        make install

+ `make.jse`: Was used to build the library in a Windows platform. It is not
    maintained any more and, if you need, change it according to your needs.
    Today I use the same `Makefile` in Mac, Linux or Windows (under MSys).
+ `.gitignore`: You should know what this file is.
+ `onload.vim`: This is [Vim](http://www.vim.org) script that configures this
    project in my environment.
+ `doxyfile`: The Doxygen documentation configuration.
+ `README.md`: This file.
+ `ChangeLog`: Used to keep the Git log up to date.
+ `jguime.vip`: My project file. Works only in Vim with Project plugin. If you
    are interested see my [GitHub](https://github.com/aantonello/project_vim)
    account.

Building the Library
--------------------

In case you are curious and want a quick way to build the library here are
some considerations.

The library uses android-19 API package. You will need to install the Android
SDK and download the API 19. If you already has the Android SDK with a
previous downloaded API version the minimal requirement for this library is
API 11.

The "ANDROID_HOME" environment variable must be defined pointing to the root
directory of the downloaded SDK. For example, if you installed the SDK in
"C:\Program Files\Android\SDK" the "ANDROID_HOME" variable should point to
that directory.

You'll also need Java SDK (really?!). If you are under Windows you will need
to set the "JAVA_HOME" environment variable pointing to the root of JDK
installation. On Unices this is usually not needed since the `javac` compiler
is installed in a `bin` directory found in "PATH" environment variable.

Under Windows you can try the `make.jse` script. It works with "nmake" tool
developed by Microsoft. But, as I said, it is out of date and is not
maintained any more. You should using [Cygwin](https://www.cygwin.com) or
[Msys](http://www.mingw.org) instead. The `Makefile` script works under
Cygwin pretty well.

Building the documentation
--------------------------

You will need [Doxygen](http://www.doxygen.org). You can install the Windows
binaries if you like. Then use it together with `nmake` and `make.jse` file or
with MSys and `Makefile` script. If you installed Cygwin you can download the
doxygen version for it. Works just fine. Type:

    make docs

Or

    nmake -f make.jse docs

The documentation will be written in the `docs/help/html` directory. A Doxygen
tags file will also be written in the `docs/help` directory named
`jguime.dxt`. The `Makefile` script will take care of creating those
directories if they don't exists yet.

Resources
---------

This library provides a series of resource files to be added in the `assets`
directory of the Android application. Mostly the resources are string tables
with text in Brazilian Portuguese and US English. To get access to those
files, besides copying the files in your `assets` directory, an instance of
the [CAndroidApp](@ref x.android.ui.CAndroidApp) class must be loaded.
`CAndroidApp` extends android.app.Application and provides some facilities to
the running application. If you don't intend to create your own Application
subclass (that should extend CAndroidApp) you can direct the Android OS to
instantiate the class by setting its name in the application element of the
AndroidManifest file, as in the following example:

    <application
      android:name="x.android.ui.CAndroidApp"
      android:icon="..."
      >
      ...
    </application>

That should be enough to load an instance of the CAndroidApp class and make it
available to the entire application. Without that, the
[SFAsset](@ref x.android.utils.SFAsset) class will no work properly and will
throw exceptions in some cases.

