#!/bin/bash -xv
for filename in `find . -name \*.mmd`; do
    NAME=`echo $filename | sed -e 's/\.mmd//g'`
	multimarkdown2XHTML.pl $NAME.mmd > $NAME.a.html    
	#xsltproc ~/bin/mmd/XSLT/xhtml-toc-h2.xslt $NAME.a.html > $NAME.html
done

