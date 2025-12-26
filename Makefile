SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

JAVA=java
JAVAC=javac
JFLEX=ext/jflex-1.6.1.jar
CUP=ext/java-cup-11a.jar
MARS=ext/Mars4_5.jar
BUILDDIR=build
SOURCEDIR=.
TESTDIR=tests
TARGET=kyr0

GENERATED = $(SOURCEDIR)/kyr/parser/Parser.java $(SOURCEDIR)/kyr/parser/Symbols.java $(SOURCEDIR)/kyr/parser/Lexer.java
SOURCES   = $(GENERATED) $(shell find $(SOURCEDIR) -name '*.java' -not -path '*/parser/*')
BYTECODE  = $(patsubst $(SOURCEDIR)/%.java, $(BUILDDIR)/%.class, $(SOURCES))
TESTS     = $(patsubst $(TESTDIR)/$(TARGET)/%.kyr, $(BUILDDIR)/tests/%.kyr, $(wildcard $(TESTDIR)/$(TARGET)/*.kyr))

.PHONY: run clean

all: dir $(GENERATED) $(BYTECODE) $(BUILDDIR)/$(TARGET).jar

$(BUILDDIR)/$(TARGET).jar: $(BYTECODE)
	@printf "Manifest-Version: 1.0\nMain-Class: kyr.Kyr\nCreated-By: University of Lorraine"> $(BUILDDIR)/manifest.txt
	mkdir -p $(BUILDDIR)/tmp_extract
	unzip -o -qq $(CUP) -d $(BUILDDIR)/tmp_extract
	jar -cvfm $(BUILDDIR)/$(TARGET).jar $(BUILDDIR)/manifest.txt -C $(BUILDDIR)/tmp_extract . -C $(BUILDDIR) .

$(BUILDDIR)/%.class: $(SOURCEDIR)/%.java
	@echo $< $@
	@$(JAVAC) -d build -classpath .:$(CUP):$(SOURCEDIR) $<

$(SOURCEDIR)/kyr/parser/Parser.java $(SOURCEDIR)/kyr/parser/Symbols.java: $(SOURCEDIR)/kyr/parser/kyr.cup
	@$(JAVA) -jar $(CUP) -symbols Symbols -parser Parser $(SOURCEDIR)/kyr/parser/kyr.cup
	@mv Symbols.java $(SOURCEDIR)/kyr/parser
	@mv Parser.java $(SOURCEDIR)/kyr/parser/

$(SOURCEDIR)/kyr/parser/Lexer.java: $(SOURCEDIR)/kyr/parser/kyr.jflex
	@$(JAVA) -jar $(JFLEX) $(SOURCEDIR)/kyr/parser/kyr.jflex

dir:
	@mkdir -p $(BUILDDIR)

clean:
	@rm -rf build
	@rm -f $(GENERATED)

tests: $(BUILDDIR)/tests $(BUILDDIR)/$(TARGET).jar
	@for KYR in $(TESTS) ; do \
		echo $$KYR ; \
		$(JAVA) -jar $(BUILDDIR)/$(TARGET).jar $$KYR ; \
		MIPS=$$(echo $$KYR|sed 's/\.kyr/.mips/') ; \
		REF=$$(echo $$KYR|sed 's/\.kyr/.out/') ; \
		if [ -f $$MIPS ]; then \
			$(JAVA) -jar $(MARS) $$MIPS | sed -e '/MARS 4.5/d' -e '/^$$/d' | diff $$REF - ; \
		fi \
	done

$(BUILDDIR)/tests:
	@mkdir -p $(BUILDDIR)/tests
	@cp -r $(TESTDIR)/$(TARGET)/* $(BUILDDIR)/tests/

