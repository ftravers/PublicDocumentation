#!/bin/bash

for filename in `find . -name \*.mmd`; do
    NAME=`echo $filename | sed -e 's/\.md//g'`
	multimarkdown2XHTML.pl $NAME.mmd > $NAME.html    
done

