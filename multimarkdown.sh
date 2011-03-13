#!/bin/bash -xv
for filename in `find . -name \*.mmd`; do
    NAME=`echo $filename | sed -e 's/\.mmd//g'`
	multimarkdown2XHTML.pl $NAME.mmd 
	xsltproc ~/bin/mmd/XSLT/xhtml-toc-h2.xslt $NAME.html -nonet -novalid > $NAME.toc.html
	mv $NAME.toc.html $NAME.html
done

