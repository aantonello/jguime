# =============================================================================
# @FILE Makefile
# @DESC Build the 'jar' file.
# =============================================================================

# -----------------------------------------------------------------------------
# SOURCE AND TARGET DIRECTORIES
# -----------------------------------------------------------------------------
include make.inc

# -----------------------------------------------------------------------------
# DEFAULT EXECUTABLES
# -----------------------------------------------------------------------------
ifeq "$(HOMEDRIVE)" "C:"
JC = $(JAVA_HOME)/bin/javac.exe
JL = $(JAVA_HOME)/bin/jar.exe
RC = $(JAVA_HOME)/bin/jar.exe
CP = rsync
ANDROID := $(shell cygpath -m $(ANDROID_HOME))
else
JC = javac
JL = jar
RC = jar
CP = rsync
ANDROID := $(ANDROID_HOME)
endif

# -----------------------------------------------------------------------------
# BOOT CLASS PATH AND LIBRARIES PATH
# -----------------------------------------------------------------------------
SDKDIR = $(ANDROID)/platforms/android-19
BOOTCP = -bootclasspath $(SDKDIR)/android.jar

# -----------------------------------------------------------------------------
# DEFAULT COMMAND LINE OPTIONS
# -----------------------------------------------------------------------------
JCOPTS = -encoding UTF-8 -source 1.5 -target 1.5 -Xlint:deprecation -Xmaxerrs 5
CTAGS  = -R --extra=+q --fields=+iaS --file-scope=no --java-kinds=-eg --tag-relative=no

# -----------------------------------------------------------------------------
# CP COMMAND LINE OPTIONS
# -----------------------------------------------------------------------------
CPOPTS = -vcruptOmC --no-o --no-g --delete --delete-excluded --exclude='.*.sw?'

# -----------------------------------------------------------------------------
# COMPILER AND LINKER FLAGS
# -----------------------------------------------------------------------------
COMPILE = $(BOOTCP) $(JCOPTS) $(DBGOPT) -d $(TMPDIR) -cp $(TMPDIR)
LINK = cMf $(OUTPUT) -C $(TMPDIR) .
MAKERES = uf $(OUTPUT) -C $(RESDIR) .

# -----------------------------------------------------------------------------
# MAKEFILE TARGETS
# NOTE: Theres is no more differences between debug or release versions.
# -----------------------------------------------------------------------------
.PHONY: clean cleanall cleandocs install tags docs

default : all

all : clean $(OUTPUT)

clean :
	rm -fR ./$(BINDIR)
	rm -fR ./$(TMPDIR)

cleandocs :
	rm -fR ./$(DOCDIR)

cleanall : clean cleandocs
	rm -f $(OUTTAG)

$(OUTDIR) :
	mkdir -p $@

$(TMPDIR) :
	mkdir -p $@

$(DOCDIR) :
	mkdir -p $@

$(APKDIR) :
	mkdir -p $@

$(RSCDIR) :
	mkdir -p $@

$(OUTPUT) : $(TMPDIR) $(OUTDIR)
	$(JC) $(COMPILE) $(_FILE_LIST_)
	$(JL) $(LINK)

install: $(APKDIR) $(RSCDIR)
	$(CP) $(CPOPTS) $(OUTPUT) $(APKDIR)
	$(CP) $(CPOPTS) $(OUTDXT) $(PLXLIB)
	$(CP) $(CPOPTS) $(RESDIR)/ $(RSCDIR)
	-publish -doc plx/$(TARGET) -f -q

#	-cp ./$(OUTPUT) $(APKDIR)/
# 	-cp ./$(OUTDXT) $(PLXLIB)/
# 	-cp -r ./$(RESDIR)/* $(RSCDIR)/

tags:
	ctags $(CTAGS) -f $(OUTTAG) $(PWD)/$(SRCDIR)/*

docs: $(DOCDIR)
	( cat doxyfile ; echo "$(PROJECT_NUMBER)" ; echo "$(GENERATE_TAGFILE)" ) | doxygen -

