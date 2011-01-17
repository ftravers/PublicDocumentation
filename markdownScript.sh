#!/bin/bash

for filename in `find . -name \*.md`; do
    NAME=`echo $filename | sed -e 's/\.md//g'`
    pandoc -o $NAME.html -i $NAME.md
done
