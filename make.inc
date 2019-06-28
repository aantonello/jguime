# ============================================================================
# General Include Make file.
# vim:ft=make
# ============================================================================

# Common directories
SRCDIR = src/x/android
BINDIR = bin/apk
TMPDIR = bin/tmp
DOCDIR = docs/help
OUTDIR = $(BINDIR)/rel
RESDIR = res
TARGET = jguime
APPVER = 2.5
BUILD  = 1
OUTPUT = $(OUTDIR)/$(TARGET).jar
OUTTAG = $(TARGET).tags
OUTDXT = $(DOCDIR)/$(TARGET).dxt

PLXHOME = /Volumes/HD750/plx

PLXLIB = $(PLXHOME)/libs/$(TARGET)-$(APPVER)
APKDIR = $(PLXLIB)/apk
RSCDIR = $(PLXLIB)/res

# Variables to documentation
GENERATE_TAGFILE=GENERATE_TAGFILE=$(OUTDXT)
PROJECT_NUMBER=PROJECT_NUMBER=$(APPVER).$(BUILD)

# ----------------------------------------------------------------------------
# FILE LIST
# ----------------------------------------------------------------------------
JGUIME_DEFS=$(SRCDIR)/defs/ERROR.java\
			$(SRCDIR)/defs/ENC.java\
			$(SRCDIR)/defs/ALIGN.java\
			$(SRCDIR)/defs/SIZE.java\
			$(SRCDIR)/defs/RES.java

JGUIME_UTILS=$(SRCDIR)/utils/debug.java\
			 $(SRCDIR)/utils/strings.java\
			 $(SRCDIR)/utils/arrays.java\
			 $(SRCDIR)/utils/numbers.java\
			 $(SRCDIR)/utils/thread_t.java\
			 $(SRCDIR)/utils/time_t.java\
			 $(SRCDIR)/utils/res.java\
			 $(SRCDIR)/utils/SFAsset.java\
			 $(SRCDIR)/utils/SFListT.java\
			 $(SRCDIR)/utils/CStringTable.java

JGUIME_IO=$(SRCDIR)/io/CBinaryReader.java\
		  $(SRCDIR)/io/CBinaryWriter.java\
		  $(SRCDIR)/io/CStreamReader.java\
		  $(SRCDIR)/io/CStreamWriter.java\
		  $(SRCDIR)/io/socket_t.java\
		  $(SRCDIR)/io/stream_t.java

JGUIME_BT=$(SRCDIR)/bt/SFBluetoothSocket.java\
		  $(SRCDIR)/bt/sfBluetooth.java

JGUIME_XML=$(SRCDIR)/xml/CXmlTok.java\
		   $(SRCDIR)/xml/CXmlAttr.java\
		   $(SRCDIR)/xml/CXmlNode.java\
		   $(SRCDIR)/xml/CXmlFile.java

JGUIME_NMS=$(SRCDIR)/nms/INHandler.java\
		   $(SRCDIR)/nms/msg_t.java\
		   $(SRCDIR)/nms/cache_t.java\
		   $(SRCDIR)/nms/subscribers.java\
		   $(SRCDIR)/nms/issuer.java

JGUIME_UI=$(SRCDIR)/ui/ALERT.java\
		  $(SRCDIR)/ui/IMSG.java\
		  $(SRCDIR)/ui/IAndroidView.java\
		  $(SRCDIR)/ui/IAndroidListDelegate.java\
		  $(SRCDIR)/ui/IAndroidListAdapter.java\
		  $(SRCDIR)/ui/AbstractView.java\
		  $(SRCDIR)/ui/CAndroidAdapter.java\
		  $(SRCDIR)/ui/CRect.java\
		  $(SRCDIR)/ui/CAndroidApp.java\
		  $(SRCDIR)/ui/CAndroidView.java\
		  $(SRCDIR)/ui/CAndroidListView.java\
		  $(SRCDIR)/ui/CAndroidGridView.java\
		  $(SRCDIR)/ui/CAndroidPagerView.java\
		  $(SRCDIR)/ui/CAndroidSliderView.java\
		  $(SRCDIR)/ui/CAndroidProgressDialog.java\
		  $(SRCDIR)/ui/CAndroidPasswordDialog.java\
		  $(SRCDIR)/ui/view_t.java\
		  $(SRCDIR)/ui/SFTableDelegate.java\
		  $(SRCDIR)/ui/SFTableAdapter.java\
		  $(SRCDIR)/ui/SFTableView.java\
		  $(SRCDIR)/ui/SFGridView.java\
		  $(SRCDIR)/ui/sfView.java

_FILE_LIST_=$(JGUIME_DEFS) $(JGUIME_UTILS) $(JGUIME_IO) $(JGUIME_BT)\
			$(JGUIME_XML) $(JGUIME_NMS) $(JGUIME_UI)

