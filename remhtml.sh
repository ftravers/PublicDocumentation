#!/bin/bash
cd $HOME/bin/website
time rsync -avP . ftravers@spicevan.com:/home/ftravers/mohe.spicevan.com 
#time rsync -avP . root@linux1.hk.oracle.com:/var/www/html/
time rsync -avP . ftravers@spicevan.com:/home/ftravers/spicevan.com/current/public
