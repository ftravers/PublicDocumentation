#!/bin/bash -x
for filename in `find . -name \*.mmd`; do
    NAME=`echo $filename | sed -e 's/\.mmd//g'`
	multimarkdown2XHTML.pl $NAME.mmd > $NAME.1.html    
	xsltproc ~/bin/mmd/XSLT/xhtml-toc-h2.xslt $NAME.1.html > $NAME.html
done

