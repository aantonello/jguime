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
include make.inc

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
# COMPILER AND LINKER FLAGS
# -----------------------------------------------------------------------------
COMPILE = $(BOOTCP) $(JCOPTS) $(DBGOPT) -d $(TMPDIR) -cp $(TMPDIR)
LINK = cMf $(OUTPUT) -C $(TMPDIR) .
MAKERES = uf $(OUTPUT) -C $(RESDIR) .

# -----------------------------------------------------------------------------
# MAKEFILE TARGETS
# NOTE: Theres is no more differences between debug or release versions.
# -----------------------------------------------------------------------------
.PHONY: clean cleanall install tags docs

default : all

all : clean $(OUTPUT)

clean :
	rm -fR ./$(BINDIR)
	rm -fR ./$(TMPDIR)

cleanall : clean
	rm -fR ./$(DOCDIR)
	rm -f $(OUTTAG)

$(OUTDIR) :
	mkdir -p $@

$(TMPDIR) :
	mkdir -p $(TMPDIR)

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
	cp ./$(OUTPUT) $(APKDIR)/
	cp ./$(OUTTAG) $(PLXLIB)/
	cp ./$(OUTDXT) $(PLXLIB)/
	cp -r ./$(RESDIR)/ $(RSCDIR)/
	publish -doc plx/$(TARGET) -f

tags:
	ctags $(CTAGS) -f $(OUTTAG) $(PWD)/$(SRCDIR)/*

docs: $(DOCDIR)
	( cat doxyfile ; echo "GENERATE_TAGFILE=$(OUTDXT)" ) | doxygen -

