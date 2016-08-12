# Normally, this file should not be modified. But you are encouraged to read it
# and learn how to modify its behavior by adding to the variables defined here,
# or overriding theme altogether.
#
# Settings //vim: /=/,/^\s*$/-! sort -u
DUP 					 =  ../Programs/dup
LANG_TOOL      =  ../Programs/LanguageTool-3.2/languagetool-commandline.jar
LATEX_COMMAND  =  lualatex
LATEX_FLAGS    =  -file-line-error- -synctex=1 -halt-on-error
LATEX          = $(LATEX_COMMAND) $(LATEX_FLAGS)
LOCALTEXMF     =  ~/texmf   #  Comment or  change  if necessary.
MAIN           =  00
SOURCES        =  $(wildcard *.sty *.tex) $(LISTINGS) Makefile
TARGETS        =  $(MAIN).pdf
TMP 					 := $(shell mktemp -d)

# This is our first target. It would become your default if you include this
# file, without defining an targets before it. It is OK to use a target whose
# main body is defined later. 
.PHONY: help 
help:     
	@echo $(if $(need-help),,Type \'$(MAKE) help\' to get help)

need-help := $(filter help,$(MAKECMDGOALS))
define tip 
	$(if $(need-help),$(warning $1: $2)) 
endef

# Compound:
ifdef LOCALTEXMF
	export TEXINPUTS=:$(LOCALTEXMF)
	export BIBINPUTS=:$(LOCALTEXMF)
endif

.PHONY: status 
	status: $(call tip,status,file status, what's made, and what's not.)
	@ echo $(words $(SOURCES)) source files: 
	@ wc -l $(SOURCES) 
	@ echo $(G0) garbage files, sized $(G1) bytes:
	@ wc -l $(garbage)

# Target to be used for compiles when references did not change
.PHONY: view
view: $(call tip,view,open a PDF viewer on made output) $(MAIN).pdf 
	@ (okular --unique $(MAIN).pdf &> /dev/null > /dev/null &)
	
.PHONY: clean c 
c: $(call tip,c, shorthand of clean) clean

clean: $(call tip,clean,\
 move all auotmatically-generated-files (currently $(G0)) to /tmp)
	@ - [ -z '$(garbage)' ] || mv -f $(garbage) $(TMP)
	@ - [ $(G0) -gt 1 ] && echo $(G0) files moved to $(TMP) [$(G1) bytes] || true 
	@ - [ $(G0) -eq 1 ] && echo $(G0) file moved to $(TMP) [$(G1) bytes] || true 
	@ - [ $(G0) -eq 0 ] && echo already clean || true 

.PHONY: latex  
latex: $(call tip,latex,run latex to make $(MAIN).pdf) $(MAIN).pdf
	texfot $(LATEX) ./$(MAIN).tex


.PHONY: force f
f: $(call tip,f, shorthand of force) force

force: $(call tip,force, shorthand of 'latex') 
	texfot $(LATEX) ./$(MAIN).tex

$(MAIN).pdf: $(call tip,$(MAIN).pdf,our main latex target) $(SOURCES) 
	texfot $(LATEX) ./$(MAIN).tex

.PHONY: verbose v
v: $(call tip,v, shorthand of verose) verbose

verbose: $(call tip,verbose,run latex interactively; to fix compilation errors)  
	$(LATEX_COMMAND) ./$(MAIN).tex

.PHONY: all a
a: $(call tip,a, shorthand of all) all 

all: $(call tip,all,recreate all targets (currently $(TARGETS); useful in batch mode)) $(TARGETS)

.PHONY: silent
s: $(call tip,s, shorthand of silent) silent 

silent: $(call tip,silent,remake $(MAIN).pdf but silently) $(MAIN).pdf $(SOURCES) Makefile
	$(LATEX) -interaction=batchmode $(MAIN).tex

.PHONY: again
again: $(call tip,again,alias for 'silent' remake $(MAIN).pdf but silently) silent

.PHONY: write18
write18: $(call tip,write18,remake $(MAIN).pdf with -shell-escape) $(SOURCES) $(LISTINGS) 
	$(LATEX) -draftmode -shell-escape ./$(MAIN).tex

.PHONY: bibtex b
b: $(call tip,b, shorthand of bibtex) all 
$(MAIN).blg bibtex: $(call tip,bibtex, regenerate the bibliogrpahy)	
	$(LATEX) -interaction=batchmode -draftmode $(MAIN).tex
	bibtex $(MAIN) *.bib
	$(LATEX) -interaction=batchmode -draftmode ./$(MAIN).tex

.PHONY: all
all: write18 bibtex latex 

.PHONY: warnings
warnings: $(call tip,warnings,present all LaTeX warnings) 
	 $(LATEX) -interaction=batchmode -draftmoe -recorder 00.tex 2>&1 | grep --color=auto -E "Warning|Missing" 

.PHONY: lacheck l
lacheck: $(call tip,lachek,run lacheck on all sources) 
	lacheck *.tex
l: $(call tip,l, shorthand of lacheck) lacheck

.PHONY: aspell
aspell: $(call tip,aspell,run aspell on all sources) 
	for f in *.tex ; do if [ $$f != 00.tex ]; then aspell check $$f;fi; done

dup: $(call tip,dup,run dup on all sources) 
	@ for f in *.tex ; do if [ $$f != 00.tex ]; then echo dup $$f; $(DUP) $$f;fi; done

lt: $(call tip,lt,run language-tool on all sources) 
	for f in *.tex ; do if [ $$f != 00.tex ]; then echo lt $$f; detex $$f > $(TMP)/$$f | java -jar $(LANG_TOOL) $(TMP)/$$f ; fi; done

.PHONY: rebuild 
rebuild: $(call tip,rebuild,same as 'make clean'; try writing 'make rebuild all') clean 

.PHONY: anew 
anew: $(call tip,anew,same as 'make clean'; try writing 'make anew $(firstword $(TARGETS))') clean 

###############################################################################
# Garbage //vim: /=/+,/^\s*)\s*)\s*$/-1!column -t|sort -u|awk '{print "\t" $0}'
###############################################################################
garbage := $(sort $(wildcard \
	*~                                 \
	*.aux                              \
	*.auxlock                          \
	*.backup                           \
	*.bak                              \
	*.bbl                              \
	*.bcf                              \
	*.blg                              \
	*blx.bib                           \
	*.class                            \
	*[dD][eE][lL][mM][eE]*             \
	*.detexed                          \
	*.dpth                             \
	*.dvi                              \
	*.eho                              \
	*.fdb_                             \
	*.fdb_latexmk                      \
	*.fls                              \
	*.glo                              \
	*.gls                              \
	*gnuplottex-fig[0-9]*.*            \
	*.heo                              \
	*.ist                              \
	*.listing                          \
	*.loa                              \
	*.lof                              \
	*.log                              \
	*.lol                              \
	*.lolisting                        \
	*.lop                              \
	*.lot                              \
	*.maf                              \
	$(MAIN)-figure[0-9][0-9][0-9].pdf  \
	$(MAIN)-figure[0-9][0-9].pdf       \
	$(MAIN)-figure[0-9].pdf            \
	*.md5                              \
	*.mtc                              \
	*.mtc[0-9]                         \
	*.mtc[0-9][0-9]                    \
	*.nav                              \
	*.o                                \
	*.out                              \
	*.run.xml                          \
	*.snm                              \
	.*swo                              \
	.*swp                              \
	*.synctex.gz                       \
	*.synctex.gz\(busy\)               \
	$(TARGETS)                         \
	*.toc                              \
	*.vrb                              \
	*.vtc                              \
))
G0 := $(words $(garbage)) # 0th moment, i.e., garbage count
G1 := $(shell ls -ls $(garbage)|tee  $(TMP)/files.lst|awk "{t+=$$$$6}END{print t}")
