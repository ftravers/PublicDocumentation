#!/bin/bash -x
for filename in `find . -name \*.mmd`; do
    NAME=`echo $filename | sed -e 's/\.mmd//g'`
	multimarkdown2XHTML.pl $NAME.mmd > $NAME.html    
done

