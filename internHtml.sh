#!/bin/bash
cd $HOME/bin/website
time rsync -avP . root@linux1.hk.oracle.com:/var/www/html 
