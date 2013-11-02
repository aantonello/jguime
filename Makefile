# =============================================================================
# @FILE Makefile
# @DESC Build the 'jar' file.
# =============================================================================

# -----------------------------------------------------------------------------
# DEFAULT EXECUTABLES
# -----------------------------------------------------------------------------
JC = javac
JL = jar
RC = jar

# -----------------------------------------------------------------------------
# SOURCE AND TARGET DIRECTORIES
# -----------------------------------------------------------------------------
SRCDIR = src/x/android
BINDIR = bin/apk
TMPDIR = bin/tmp
RESDIR = res

PLXLIB = $(WORKHOME)/libs/jguime-2.4
APKDIR = $(PLXLIB)/apk
RSCDIR = $(PLXLIB)/res

# -----------------------------------------------------------------------------
# BOOT CLASS PATH AND LIBRARIES PATH
# -----------------------------------------------------------------------------
SDKDIR = $(ANDROID_HOME)/platforms/android-11
BOOTCP = -bootclasspath $(SDKDIR)/android.jar

# -----------------------------------------------------------------------------
# DEFAULT COMMAND LINE OPTIONS
# -----------------------------------------------------------------------------
JCOPTS = -encoding UTF-8 -source 1.5 -target 1.5 -Xlint:deprecation -Xmaxerrs 5
CTAGS  = -R --extra=+q --fields=+iaS --file-scope=no --java-kinds=-eg --tag-relative=no

# -----------------------------------------------------------------------------
# DEFAULT TARGET, WITHOUT debug OR release BUILDS
# -----------------------------------------------------------------------------
TARGET = jguime
OUTDIR = $(BINDIR)/rel
OUTPUT = $(OUTDIR)/$(TARGET).jar

# -----------------------------------------------------------------------------
# COMPILER AND LINKER FLAGS
# -----------------------------------------------------------------------------
COMPILE = $(BOOTCP) $(JCOPTS) $(DBGOPT) -d $(TMPDIR) -cp $(TMPDIR)
LINK = cMf $(OUTPUT) -C $(TMPDIR) .
MAKERES = uf $(OUTPUT) -C $(RESDIR) .

# -----------------------------------------------------------------------------
# FILE LIST
# -----------------------------------------------------------------------------
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
			 $(SRCDIR)/utils/CStringTable.java

JGUIME_IO=$(SRCDIR)/io/CBinaryReader.java\
		  $(SRCDIR)/io/CBinaryWriter.java\
		  $(SRCDIR)/io/CStreamReader.java\
		  $(SRCDIR)/io/CStreamWriter.java\
		  $(SRCDIR)/io/socket_t.java\
		  $(SRCDIR)/io/stream_t.java

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
		  $(SRCDIR)/ui/CAndroidProgressDialog.java\
		  $(SRCDIR)/ui/CAndroidPasswordDialog.java\
		  $(SRCDIR)/ui/view_t.java

_FILE_LIST_=$(JGUIME_DEFS) $(JGUIME_UTILS) $(JGUIME_IO) $(JGUIME_XML)\
			$(JGUIME_NMS) $(JGUIME_UI)
# -----------------------------------------------------------------------------
# MAKEFILE TARGETS
# NOTE: Theres is no more differences between debug or release versions.
# -----------------------------------------------------------------------------
.PHONY: clean cleanall install tags docs

default : all

all : clean $(OUTPUT)

clean :
	rm -fR ./bin

cleanall :
	rm -fR ./bin
	rm -fR ./docs/help
	rm -f $(TARGET).tags
	rm -fR $(WWWROOT)/docs/plx/$(TARGET)

$(OUTDIR) :
	mkdir -p $@

$(TMPDIR) :
	mkdir -p $(TMPDIR)

$(OUTPUT) : $(TMPDIR) $(OUTDIR)
	$(JC) $(COMPILE) $(_FILE_LIST_)
	$(JL) $(LINK)

$(APKDIR) :
	mkdir -p $(APKDIR)

$(RSCDIR) :
	mkdir -p $(RSCDIR)

install: $(APKDIR) $(RSCDIR)
	cp ./$(OUTPUT) $(APKDIR)/
	cp ./$(TARGET).tags $(PLXLIB)/
	cp ./docs/help/$(TARGET).dxt $(PLXLIB)/
	cp -r ./$(RESDIR)/ $(RSCDIR)
	publish -doc plx/$(TARGET) -f

tags:
	ctags $(CTAGS) -f $(TARGET).tags $(PWD)/$(SRCDIR)/*

docs:
	doxygen doxyfile

