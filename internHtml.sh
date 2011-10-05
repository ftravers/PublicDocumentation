#!/bin/bash
cd $HOME/bin/websiteWorkDoco
time rsync -avP . root@linux1.hk.oracle.com:/var/www/html/workDoco 
