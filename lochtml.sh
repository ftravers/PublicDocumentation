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

# Commit any changed files, necessary precursor to the archive command below
git commit -am"."

# Checkout files from GIT and put them into the webstage dir
git archive master | tar -x -C "$webStageDir"

cd $webStageDir
./markdownScript.sh
./multimarkdown.sh
ant generate-html



