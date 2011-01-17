#!/bin/bash

# Prepare location where html files will be exported to

bin="$HOME/bin"

if [ ! -d "$bin" ]; then
    mkdir $bin
fi

webStageDir="$bin/webStageDir"
if [ ! -d "$webStageDir" ]; then
    mkdir $webStageDir
fi

cd $webStageDir

# $DOCS_HOME defined in ~/.bashrc
cd $DOCS_HOME
git commit -a -m"."

git archive master | tar -x -C "$webStageDir"

cd $webStageDir
ant generate-html


