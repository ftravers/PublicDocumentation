Recently I was working on trying to automate the sending of email
messages from my linux box.  I wanted to have attachments AND the
email body be HTML formatted.

I'm pretty happy with the result as it sticks to these principles:

* uses common linux programs that are easily available:

```
base64 - for encoding binary attachments
msmtp - for sending email over smtp (using ssl)
uuidgen - to create a unique string for mime boundary
python - (optional) to build up my mail message
```

and thats it!  So basically I hand constructed the email so it looked
something like the following:

```
To:fenton.travers@gmail.com
From:fenton.travers@oracle.com
Subject: Test
MIME-Version: 1.0
Content-Type: multipart/mixed; boundary=asdfghjkl
--asdfghjkl
Content-type: text/html; charset=utf-8
<html><body>
<b>This is a test mail.</b><br/>
this is line two
</body></html>
--asdfghjkl
Content-Type: application/octet-stream; name="screen.jpg"
Content-Transfer-Encoding: base64
Content-Disposition: inline; filename="screen.jpg"
/9j/4AAQSkZJRgABAQEAAQABAAD/2wBDAAUDBAQEAwUEBAQFBQUGBwwIBwcHBw8LCwkMEQ8SEhEP
ERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/2wBDAQUFBQcGBw4ICA4eFBEUHh4eHh4e
...
--asdfghjkl---
```

I base64 encoded my attachment, called: `screen.jpg` with:

```bash
$ base64 screen.jpg
```

Then I sent the file with:

```bash
$ msmtp "fenton.travers@gmail.com" < mail.txt
```

I have a `~/.msmtprc` file that looks like:

```
# Accounts will inherit settings from this section
defaults
auth             on

# A freemail service
account    acmecorp
host       smtp.acmecorp.com
auth       on
tls		on
tls_certcheck off
tls_starttls off	
port	   465
from       fenton.travers@acmecorp.com
user       fenton.travers@acmecorp.com
password   PASSWORD

# Set a default account
account default : acmecorp
```

You'd want to change the above to work with your smtp provider.  There
are examples for gmail on the web.

I constructed a python script to help build up mail message itself.

```python
#!/usr/bin/python

from optparse import OptionParser
import os, sys 

parser = OptionParser()

parser.add_option ("-t", "--to", action="store", type="string", dest="to", help="to email address")
parser.add_option ("-f", "--from", action="store", type="string", dest="fr", help="from email address")
parser.add_option ("-s", "--subject", action="store", type="string", dest="subject", help="email subject")
parser.add_option ("-m", "--html", action="store",  type="string", dest="html_file", help="html message body")
parser.add_option ("-e", "--encoded", action="store",  type="string", dest="encoded_file", help="base 64 encoded binary file to attach")
parser.add_option ("-o", "--outfile", action="store",  type="string", dest="out_file", help="file to store constructed mail message in")

(ops, args) = parser.parse_args()

boundary = os.popen('uuidgen').read().strip()

def buildMessage(ops,boundary):
    msg =  "To: " + ops.to + "\n"
    msg += "From: " + ops.fr + "\n"
    msg += "Subject: " + ops.subject + "\n"
    msg += "MIME-Version: 1.0\n"
    msg += "Content-Type: multipart/mixed; boundary=" + boundary + "\n\n"
    msg += "--" + boundary + "\n" 
    msg += htmlMessage(ops, boundary) + "\n\n"
    msg += "--" + boundary + "\n"
    msg += binary(ops, boundary) + "\n"
    msg += "--" + boundary + "--\n" 
    return msg
    
def htmlMessage(ops, boundary):
    body = open(ops.html_file).read()
    htmlMessage = "Content-type: text/html; charset=utf-8\n\n" + body
    return htmlMessage

def binary(ops, boundary):
    ct = 'Content-Type: application/octet-stream; name="' + ops.encoded_file + '"\n'
    en = 'Content-Transfer-Encoding: base64\n'
    cd = 'Content-Disposition: inline; filename="' + ops.encoded_file + '"\n\n'
    encoded = open(ops.encoded_file).read().strip()
    return ct + en + cd + encoded

msg = buildMessage(ops, boundary)
f = open(ops.out_file, 'w')
f.write (msg)
```

Then I created a shell script to call this and do the rest of the
coordinating steps.  I used this script to attach a git bundle to an
email and also to show what had changed in the repository using git's
color-words option to `git show`.

```bash
#!/bin/bash

binary="cur.bundle.unencoded"
git bundle create $binary HEAD

encoded="cur.bundle"
base64 $binary > $encoded

html="all_file.html"
(git show -C --color-words HEAD | simpleAnsi2Html.sh) > $html

from="fenton.travers@oracle.com"
repo=${PWD##*/}
subject="$repo: repo has been modified."
out="out_file.mime"

emails=("fenton.travers@gmail.com" "fenton.travers@acmecorp.com")
for to in "${emails[@]}"
do
    echo "Emailing: $to" 
    construct_mail_message.py -t $to -f $from -s "$subject" -m $html -e $encoded -o $out
    msmtp -a oracle $to < $out &
done

rm -f $binary $encoded $html $out
```
