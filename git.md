# Adding a new Repository

## For the impatient...

### Configure security/permissions

Edit config file: 

```
gitosis-admin/gitosis.conf
```

Push up your change.

    $ git commit -am"."; git push

###  Prep the Project

In the folder you want setup as a git repo, do:

<code><pre>
git init
git remote add origin <span class="sample">git@linux1.hk.oracle.com:testProject.git</span></pre></code>

You must add at least one file...git doesn't do empty directories! :)

    git add .; git commit -m"Initial Commit"
    git push origin master:refs/heads/master

# Misc

## git log

Show stats for which files changed, for last `-3` revs.  Format so a
bit more legible.

```
$ git log --stat -3 --pretty=format:"%h%x09%an%x09%ad%x09%s"
```

# git bundle

if you want to keep two repos in sync but cannot use ssh or some other
protocol to do so directly, you can use git bundle to help out.

Create your bundle:

```
$ git bundle create my.bundle HEAD
```

This creates a bundle called `my.bundle`, now email this to the other
person who applies it to their repo with:

```
$ git pull my.bundle
```

# Go back in time

First find the hash of the commit you want to go to with:

    git log --oneline

Then just do:

    git checkout [hash]

# Detached Head issues

## Changes on a detached head

If you have changes on a detached head do the following:

* Create a new branch with:

    git checkout -b [branchName]

* Switch to master

    git checkout master

* Merge changes from branch

    git merge [branchName]

* Delete old branch

    git branch -d [branchName]

## Commits on a detached head

If you accidently committed some changes on a detached head and say
switched to master do the following:

* Switch back to the detached head, (say it had a commit hash = e0a5a84):

    git checkout e0a5a84

* Then do the steps from above.

# Finding when a problem was introduced

Sometimes a problem gets introduced into the code and you want to find
out when it got introduced.  You can use the `git bisect` command to
find the offending commit.

    git bisect start
    git bisect good 61d20a3
    git bisect bad master
    
assuming the last known good commit was `61d20a3`.  You will then be
put on a commit halfway between `master` and `61d20a3`.  Then run
whatever it is that you'll do to find the problem, a grep, a compile,
etc...  

If the problem still persists, type:

    git bisect bad

So git will give you a commit 1/2 way between `61d20a3` and the
current commit.  Otherwise `git bisect good` will move you to a commit
in the opposite direction.

## Stop Bisecting

    git bisect reset HEAD

## In more detail...

### Edit config file

Edit config file: `gitosis-admin/gitosis.conf`

The following is the template for understanding the format of this
file:

    [group groupName]
    writable = repoName
    members = user1 user2

The fields above that need to be filled in with your specific
information are:

-   `groupName` : This is the name of your project group
-   `repoName` : This is the name of the folder that holds your
    repository/project
-   `user1`, `user2` : This is the name of the user who will belong
    to this group. This must correspond to the filename located in
    `gitosis-admin/keydir/user1.pub`. Note don’t include the `.pub`
    extension.

Example:

    $ cat ~/projects/gitosis-admin/gitosis.conf
    [gitosis]
    [group gitosis-admin]
    writable = gitosis-admin
    members = ftravers@ftravers-laptop
    [group testgroup1]
    members = ftravers@ftravers-laptop @anothergroup
    writable = testrepo1 testrepo2 
    readonly = testrepo3
    ## You can use groups just to avoid listing users multiple times. Note
    ## no writable= or readonly= lines.
    [group anothergroup]
    members = alice bill

where

-   `writable` : defines who has commit privileges
-   `readonly` : defines who has read only privileges
-   `testgroup1` : is the name of the group
-   `testrepo1` : is the name of the new repository
-   `members` : is a space delimeted list of users included in this
    group

The `members` list should be the filenames from
`~/projects/gitosis-admin/keydir` folder without the `.pub`
extension.

### Create a new project and push to server

Now create the project...

    $ cd ~/projects; 

You can create a new empty directory, or copy an existing
directory/files into this folder

-   Without Maven

    $ rm -rf testrepo1; mkdir testrepo1

-   - OR – with Maven

    $ mvn archetype:create -DgroupId=com.mycompany.app -DartifactId=testrepo1

-   Add it to git server

    $ cd testrepo1; git init
    $ git remote add spicevan ft_git3@spicevan.com:testrepo1.git
    $ git remote add origin git@linux1.hk.oracle.com:testrepo1.git

Do some work, git add and commit files like: `test.txt`

If you copy some files into here that you want to then manage,
after the copy do the following:

    $ cd testrepo1
    $ git add .
    $ git commit -m"Initial Commit"
    $ git push origin master:refs/heads/master

# References

[Git Reference](http://git.or.cz/course/svn.html)

[Visual Git](http://marklodato.github.com/visual-git-guide/index-en.html)

# Install gitosis on Dreamhost

Reference:
[Dreamhost Gitosis Setup](http://wiki.dreamhost.com/Gitosis)

    $ ssh-keygen -t rsa
    $ ssh-copy-id ft_git3@spicevan.com
    $ rsync -avP --stats .ssh/id_rsa.pub ft_git3@spicevan.com:~

Check your version of python with: `python --version`

-   for JeOS systems see:
    -   [Installing GIT](/applications.html#git)
    -   [Installing Python 2.6](/applications.html#python26)


for 2.5\*

    [remote]$ mkdir -p $HOME/lib/python2.5/site-packages
    [remote]$ export PYTHONPATH=$HOME/lib/python2.5/site-packages
    [remote]$ mkdir ~/src; cd ~/src; wget http://pypi.python.org/packages/2.5/s/setuptools/setuptools-0.6c11-py2.5.egg
    [remote]$ sh setuptools-0.6c11-py2.5.egg --prefix=$HOME

if you need a proxy for wget use something like:

    export http_proxy=http://your.proxy.server:port && wget -c http://whatever

for 2.6\*

    [remote]$ mkdir -p $HOME/lib/python2.6/site-packages
    [remote]$ export PYTHONPATH=$HOME/lib/python2.6/site-packages
    [remote]$ mkdir ~/src; cd ~/src; wget http://pypi.python.org/packages/2.6/s/setuptools/setuptools-0.6c11-py2.6.egg
    [remote]$ sh setuptools-0.6c11-py2.6.egg --prefix=$HOME

The rest is the same for either version.

    [remote]$ git clone git://eagain.net/gitosis.git

If the `git clone git://eagain.net/gitosis.git` line doesn’t work
for you  
like it didn’t for me once, you can download the tar.gz off the web
and   
untar it in your root folder so `cd; ls` shows the `gitosis`
directory.

Maybe get it here:
[http://felixembeddedonandroid.googlecode.com/files/gitosis.tar.gz](http://felixembeddedonandroid.googlecode.com/files/gitosis.tar.gz)

    [remote]$ cd; tar xvfz gitosis-somelongHashCode.tar.gz

The rest should go without a hitch

    [remote]$ cd gitosis/
    [remote]$ export PATH=$HOME/bin:$PATH
    [remote]$ python setup.py install --prefix=$HOME; cd
    [remote]$ echo "export PYTHONPATH=$HOME/lib/python2.5/site-packages/" >> .bashrc
    [remote]$ echo "export PYTHONPATH=$HOME/lib/python2.5/site-packages/" >> .bash_profile
    [remote]$ echo "export PATH=$HOME/bin:$PATH" >> .bashrc
    [remote]$ echo "export PATH=$HOME/bin:$PATH" >> .bash_profile
    [remote]$ . ~/.bash_profile
    [remote]$ gitosis-init < id_rsa.pub; rm -f id_rsa.pub
    [remote]$ chmod 750 $HOME/repositories/gitosis-admin.git/hooks/post-update
    $ mkdir ~/projects; cd ~/projects
    $ git clone ft_git3@spicevan.com:gitosis-admin.git


# When others want to use repository

## Get users public key

Whoever wants to work on the repo must create a public/private key
pair and send you the public key.

[testuser@local]$ ssh-keygen -t rsa  
[testuser@local]$ cp .ssh/id\_rsa.pub \~

Send the administrator of the repository your `~/.ssh/id_rsa.pub`
file.

As the administrator add the file to the `keydir` folder:

    $ cp /home/testuser/id_rsa.pub ~/projects/gitosis-admin/keydir/testuser.pub
    $ cd ~/projects/gitosis-admin/keydir
    $ git add .
    $ cd ..

Now we have their public key we can add them to projects, edit the
`~/projects/gitosis-admin/gitosis.conf` file.

    $ cat ~/projects/gitosis-admin/gitosis.conf
    [gitosis]
    [group gitosis-admin]
    writable = gitosis-admin
    members = ftravers@ftravers-laptop
    [group testgroup1]
    writable = testrepo1
    members = ftravers@ftravers-laptop testuser
    $

Finally push up your changes to the conf file and the added public
key to the server:

    $ git commit -a -m"My Message."
    $ git push

## Clone Repository

Now the other user `testuser` can checkout this repository

    [testuser@local]$ mkdir ~/projects; cd ~/projects
    [testuser@local]$ git clone ft_git3@spicevan.com:testrepo1.git; cd ~/projects/testrepo1
    # make some changes to the file @test.txt@ in the repo and commit and push them back to the server.
    [testuser@local]$ git commit -a -m"My comment.";
    [testuser@local]$ git push

## Pull Updates from Remote Repository

    git pull origin master

# Test merging

In the above section a second user `testuser` modified a file:
`test.txt` that the user `ftravers` also has. So as `ftravers` we
modify our copy of `test.txt` and show below how to merge this with
the different version that exists up on the server.

## Verify the updates

As the original `ftravers` user, verify the updates the `testuser`
made.

    $ cd ~/projects/testrepo1/

Make some mods to `test.txt` file. This means what’s on the server
will conflict with what you have locally. So try to pull down the
changes that are on the server.

    $ git pull origin master

Do any merging required if auto-merge fails and push up your
changes.

    $ cd ~/projects/testrepo1/; git push

## Verify the changes as testuser

Now you can log back in as test user and pull down the changes.

    [testuser@local]$ cd ~/projects/testrepo1; git pull; cat test.txt

# Misc Tasks

## Branching

Create a new branch and begin working on it.

    git checkout -b <new branch name>

As you go developing the master branch may have moved along, so it is
useful to pull in the changes that have occured on master while you have
been editing on your branch.  While on your branch do:

     git rebase master

That will pull the changes that have occured on master while you've
been editing on your branch, into your branch.

## Pushing a branch to remote repository

    git push origin newfeature

Where `origin` is your remote name and `newfeature` is the name of
the branch you want to push up.

## Pull a remote branch

Check the remote branches

    git remote show origin

update your local repo so it is aware of new branches on ‘origin’.
    
    git fetch

create a local tracking branch:

    git checkout -b local-branch-name origin/remote-branch-name

where the first experimental is the name of your local branch that
maps to the branch name on the remote (origin) named (experimental) as
well.

## Delete a remote tracking branch

    git branch -d -r origin/<remote branch name>

origin/master is a remote tracking branch for
the master branch in the origin repo

### Deleting remote branch

Deleting is also a pretty simple task (despite it feeling a bit
kludgy):

    git push origin :newfeature

That will delete the newfeature branch on the origin remote, but
you’ll still need to delete the branch locally with git branch -d
newfeature.

## Compare Branches

Often you'll be working on a branch, then switch back to the
master...and would like to know what is the difference between the two
branches.  The following shows files and how changed.

    git diff --name-status <branch1>..<branch2>
    git diff --name-status master..npe

# Branching, Merging best practices

Normally when you've created a branch you want to regularly do:

    git rebase master

This gets all the changes that have occurred on master while you are
on your branch and puts them *under* your commits.

When you are ready to merge back to master, you want to squash all
your commits into a single commit.

First find out which commits have occurred since you were on master
with either of the following:

    git log master..HEAD
    git log master..HEAD --oneline

Here is an example:

    $ git log master..HEAD --oneline
    0da7acd .
    574f56c .
    $

So I can see there have been two commits, and I want to squish these
into one.  These are listed most recent at top.

Next run an interactive rebase with:

    git rebase -i HEAD~2

Since I only have two commits, I use HEAD~2, if you have more commits,
change the number after HEAD~.  Now your editor will pop up allowing
you to start your rebase.  Now we will squish the newer commits into
the oldest commit.  During rebase the commits are listed in reverse
order to git log, with newest at the bottom.  My editor has the
following two lines at the top:

    pick 574f56c . <--- older commit
    pick 0da7acd . <--- newer commit

I change this to:

    pick 574f56c .
    squash 0da7acd .

This will merge the two commits into a single new commit.  Then just
save and exit out of your editor to go to the next step.  Another
editor window will popup where you can adjust the commit message for
this new single commit.

Now we are in a good position to create a proper patch file which we
can give to the code base owner, so they can easily see what you've
changed in the code.

# How to create and apply a patch with Git

## Create a patch file

You are on branch that you want to compare against master.

<pre><div class="sample_code">git format-patch <span class="parameter">master</span> --stdout &gt; <span class="parameter">file.patch</span></div></pre>

Take a look at what changes are in the patch

    git apply --stat file.patch

## Compare working directory against latest commit (HEAD)

    git diff HEAD --

## diff

Then you might wonder what is the difference between a file that
exists in two different  
branches.

    git diff <branchA> <branchB> -- <file>
    git diff <commit> <commit> -- <file>

compare file: pom.xml with current and two commits ago:

    git diff HEAD HEAD^^ -- pom.xml
    git diff HEAD HEAD~2 -- pom.xml

tags: diff two files

## Bring down remote changes

Say the branch we are interested in bring remote changes down into is
‘master’. The first thing we do is ensure locally we are on the master
branch. If you are not on the master branch commit or stash your local
changes on the branch you are on and switch to the master branch.

    git checkout master

Now pull down the remote changes into your remote-tracking branch.
You can see your local branches and your remote-tracking branches by
doing: `git branch` and `git branch -r` respectively. When I do a `git
branch -r` I see that I have a branch called `origin/master`.

    $ git branch -r
      origin/master

So to safetly update my remote-tracking branch origin/master I simply
do:

    git fetch origin

Now I can compare the differences between my version of master and the
remote version. I simply do:

    $ git diff --name-status master origin/master

To see a list of files that have been changed. Then I can get the  
full diff file by dropping the `--name-status` parameter:

    $ git diff master origin/master > ~/tmp/diff.diff

I output the results of that command to a file that I can later
open  
in emacs to get a better view of what the changes actually are.

If you are happy with the differences then you can merge them:

    $ git merge origin/master

## (segue) Resolving merge problems for a single file

Say we have a file that can’t be automatically merged. The
branches  
are ‘bugFix’ and ‘master’, and the file it is complaining about
is:  
‘src/main/java/com/acme/My.java’. First commit all changes on
branch  
‘bugFix’, then switch to ‘master’ and try to merge. My.java fails  
auto merge. Get the diff for this file with:

    git diff bugFix master src/main/java/com/acme/My.java > ~/tmp/My.java.diff

Now get the My.java on the bugFix branch and apply this patch to it
in  
emacs.

    mv src/main/java/com/acme/My.java src/main/java/com/acme/My.java.master
    git checkout bugFix src/main/java/com/acme/My.java

Now open both files in emacs and run command:
`A-x ediff-patch-buffer`  
and pick the correct buffers for the patch and the file to patch.

Finally you can use ‘n’, ‘p’ for next and previous patch chunks.
You  
can type ‘?’ in the mini-buffer to get a list of commands to help
you.

## (segue – over)

The following workflow has us list all of our branch (-a includes
the remote branches)

    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git branch -a
      activity
    * master
      remotes/origin/master
    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git fetch origin

Check which files have changed, and what has changed in the files

    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git diff --name-status master..remotes/origin/master
    M       src/main/java/com/oracle/ngsp/crmod/ServiceRequest.java
    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git diff master..remotes/origin/master src/main/java/com/oracle/ngsp/crmod/ServiceRequest.java
    ...a lot of diff info...

Finally we can merge the branch into our master branch

    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git branch -a
      activity
    * master
      remotes/origin/master
    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git merge remotes/origin/master
    Updating 2872496..3f88c4a
    Fast-forward
     .../java/com/oracle/ngsp/crmod/ServiceRequest.java |   47 ++++++++++----------
     1 files changed, 24 insertions(+), 23 deletions(-)
    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ 

Finally, we might just want to make sure if anything got added that
we push that back up  
to the remote too.

    ftravers@ftravers-laptop:~/projects2/crmod-ws-wrapper$ git push
    Everything up-to-date

## Manual Merge

Sometimes the auto-merge will report an inability to auto-merge for
you:

    CONFLICT (content): Merge conflict in src/main/java/com/oracle/ngsp/crmod/Activity.java
    Automatic merge failed; fix conflicts and then commit the result.

Now type:

    git mergetool


## Unstaging a file

To unstage the file: `src/site/apt/build.apt~`

    git reset HEAD src/site/apt/build.apt~

## Git Ignore file

Create a file called `.gitignore` in the project root and put lines
like the following in it.

    # a comment - this is ignored
    *.a       # no .a files
    !lib.a    # but do track lib.a, even though you're ignoring .a files above
    /TODO     # only ignore the root TODO file, not subdir/TODO
    build/    # ignore all files in the build/ directory
    doc/*.txt # ignore doc/notes.txt, but not doc/server/arch.txt
    target/
    *.diff
    *~


## Committing

Before you commit, you’ll want to checkout what’s going on. The
command  
to see what has been modified, added, etc.. is:

    git status

To get a feel for which files have been changed and by how much
try:

    git diff --stat

which gives you a sense of which files have been changed and by how
much. To  
see what has changed in a given file do:

    git diff <filename>    # Command Format
    git diff src/test/java/com/oracle/ngsp/crmod/ServiceRequestTest.java     # Command Example

Now you can add the file into the staging area with:

    git add src/test/java/com/oracle/ngsp/crmod/ServiceRequestTest.java

Then you can commit it with:

    git commit -m"<commit_Message>"   # Command Format
    git commit -m"Removed testGetActivities from ServiceRequestTest"     # Command Example

## See changes to a file

    git log -p <path to file>

# Using Git on Windows

* Install `msysgit`

Download a file that looks like: `Git-1.7.9-preview20120201.exe` from
[msysgit downloads](http://code.google.com/p/msysgit/downloads/list)
page.  The actual version number/date may be incremented from whats
above when you go to download this.

In the selection "how would you like to use Git from the command
line?", the default is "use Git bash only", which is ok

Now you can open Git Bash and interact with repositories.  Keep in
mind that if you want to interact by writing to repositories in GIT
you'll need to follow the next few steps.  But with what you've done
so far you can clone publicly readable repositories.  They are the
ones that start with `http` not `<username>@<host>:<repo>`

To get write access do the following:

* Generate your public/private key pair.  Open GIT-Bash and type:

    ssh-keygen

Just hit enter for all the prompts, don't use a passphrase.

The public key is the file:

    ~/.ssh/id_rsa.pub

Email the git administrator your public key.

# Change your routing

If you are plugged on a company network where SSH is not tolerated
but on the other hand have access to a wifi network where it is,   
there is a way to have GIT working without plugging/unplugging your
network cable

-   connect to the wifi network and make sure you can access
    internet

You can now figure out two informations from the traceroute
command:

    bvanders-laptop:~ bvanders$ traceroute spicevan.com
    traceroute to spicevan.com (173.236.138.100), 64 hops max, 40 byte packets
     1  xxx.xxxx.xxx (192.168.0.1)  163.935 ms  167.994 ms  153.271 ms

*Note: on windows, the command would be tracert spicevan.com but the result would be similar*

*192.168.0.1* is your gateway  
*173.236.138.100* is spicevan.com’s ip address

-   connect your ethernet cable and type the following command to
    add a static route:

`sudo route add 173.236.138.100/32 192.168.0.1`

*Note: on windows, the command is `route ADD 173.236.138.100 MASK 255.255.255.0 192.168.0.1`*

You should now be able to test it with the command `git status`

# Tags

List the tags with 

    git tag -l 

and then checkout a specific tag: 

    git checkout <tag_name>

# Recipes

## Cherry pick files from other branch

What is the best way to merge selective files from one development
branch to another while leaving behind everything else?

    #You are in the branch you want to merge to
    git checkout <branch_you_want_to_merge_from> <file_paths...>

## Restore deleted file

Q: I deleted a file, committed that delete. How do I find the
commit where that file was deleted and how do I restore just that
file?

A: Find the last commit that affected the given path. As the file
isn’t in the HEAD commit, this commit must have deleted it.

    git rev-list -n 1 HEAD -- <file_path>

Then checkout the version at the commit before.

    git checkout <deleting_commit>^ -- <file_path>

## I committed something in the past for one file and I want to undo that commit.

open gitk in the project and navigate the history to find the
commit you want to undo. Make sure you have ‘patch’ selected in
bottom right window. You can right click on the filename and choose
‘Highlight this only’. Look in the box: ‘SHA1 ID:’ to get a sense
of the SHA1 ID for this commit. Then do a: `git log` in the
terminal so you can select (copy/paste) this SHA1. Make sure you
have the version number (SHA1) of the commit before the commit that
has the change. Once you have the SHA1 you want to checkout this
version like so:

git checkout a5ab6dd2505a5bcbdeafcb393cafd6a404ef051d —
src/main/java/com/oracle/git/sesReader/SesReader.java

## revert (reset) a single file

This one is hard to find out there so here it is. If you have an
uncommitted change (its only in your working copy) that you wish to
revert (in SVN terms) to the copy in your latest commit, do the
following:

    git checkout filename

## Move remote repo

Say I want to start using a new remote repository. Say also that this
remote repository doesn’t have the code there yet.

-   Change Origin

The first thing to do is change where origin points to

    $ git remote -v
    origin  ft_git3@spicevan.com:elisp.git (fetch)
    origin  ft_git3@spicevan.com:elisp.git (push)
    $ git remote set-url origin git@linux1.hk.oracle.com:elisp.git
    $ git push origin master:refs/heads/master

If the repo already exists on the server (say for your colleagues  
after you’ve moved it there), they just have to do the @git
remote  
set-url...@ step, followed by:

    git pull origin master

## Setting up a public repository

See: [applications.html#Public_Read_Access]

## Normal Development Workflow

The following example demonstrates how a normal coding workflow should
be executed.  A simple file will be used to demonstrate how these
things work.  The file is called `testBranch.mmd` and only has the
following line in it to start with.  It is on the master branch.

    Starting file in _master_ branch.

Commit this file on master like so:

    git commit -am'Starting commit on _master_'

## Branch

The first step is to make a branch, we'll call it `branch1`.

    git checkout -b branch1

Now lets add an additional line:

    Starting file in _master_ branch. 
    First commit on branch1.

and commit our change on `branch1`.

    git commit -am'First commit on branch1'

Lets modify the second line and add another line

    Starting file in _master_ branch. 
    First commit on branch1, modified.
    2nd commit on branch1.

and do another commit:

    git commit -am'2nd commit on branch1'

Now lets switch back to master:

    git checkout master

Insert a line at the top of the file:

    Came back to master and change original file.
    Starting file in _master_ branch. 

and commit the change:

    git commit -am'back on master'

## Normal Development Workflow Tutorial

Lets imagine that you've asked a fellow coder to fix or enhance some
code that you wrote.  You would like to understand the changes that
they are making.  In an ideal world they would submit a patch file to
you that demonstrates the fix/enhancement that they made, in an easy
to understand format.

Lets say you are now the coder doing the fix/enhancement, how can you
ensure that your work is presented in a nice patch file for the
'owner' of the code?  The following tutorial aims to teach you the
concepts and steps you would take to create the patch file.

First we will be using GIT to manage the code and help us create the
patch file.  This tutorial will teach you the following concepts:

* branching
* rebasing/squashing
* creating a patch file

In short, branching is the process of creating something like a copy
of the code base where your changes are isolated from the master/main
branch of code.  This way if you make a disaster of things, its
relatively harmless to the code base.

Rebasing/Squashing, in the following context, means taking several
commits and combining them into a single commit.

Finally, creating a patch file, means creating a file that shows the
difference between the original code and your new code, only
highlighting the parts of the code you changed.

After you have completed the following tutorial, you can use the next
section called recipe to help you remember the commands and steps that
you should use in your coding workflow.  

## Tutorial

A simple file will be used to demonstrate how branching, squashing and
patch file creation work. We'll look at a single file, with just a
couple lines in it, to keep the tutorial material as brief and
instructive as possible.  However, the exact same process applies
equally well for any number of files and changes. The file is called
`testBranch.mmd` and to start with only has the following line in it.

    Starting file in _master_ branch.

Commit this file on the master/main branch like so:

    $ git commit -am'Starting commit on _master_'
    [master 62e2f60] Starting commit on _master_
     1 files changed, 1 insertions(+), 2 deletions(-)

### Branch

The first step is to make a branch, we'll call it `branch1`.

    $ git checkout -b branch1
    Switched to a new branch 'branch1'

Now lets add an additional line:

    Starting file in _master_ branch. 
    First commit on branch1.

and commit our change on `branch1`.

    $ git commit -am'First commit on branch1'
    [branch1 36a404f] First commit on branch1
     1 files changed, 1 insertions(+), 0 deletions(-)

Lets modify the second line and add another line.  This is significant
because we are not only *adding* a line but we are also *modifying* an
existing line.  Be sure to look at what that line looks like in the
patch file, so you can see how modifying a line gets represented in a
patch file.

    Starting file in _master_ branch. 
    First commit on branch1, modified.
    2nd commit on branch1.

and do another commit:

    $ git commit -am'2nd commit on branch1'
    [branch1 5dae0ab] 2nd commit on branch1
     1 files changed, 2 insertions(+), 1 deletions(-)

Lets see what the commit hashes are for reference, I've edited the
output to just the essentials:

    $ git log master..HEAD
    commit 5dae0ab43d8c4b87eb62f0b3253ac58477b046dc
        2nd commit on branch1
    commit 36a404f968a74302cdca5ec8cc81aa078a237ade
        First commit on branch1

### Patch File

Lets look at what a patch file looks like now:

    $ git format-patch master --stdout > ~/Desktop/file.patch
    1      Subject: [PATCH 1/2] First commit on branch1
    2      testBranch.mmd |    1 +
    3      1 files changed, 1 insertions(+), 0 deletions(-)
    4      ---
    5      Starting file in _master_ branch.
    6     +First commit on branch1.
    7      
    8     Subject: [PATCH 2/2] 2nd commit on branch1
    9      testBranch.mmd |    3 ++-
    10    1 files changed, 2 insertions(+), 1 deletions(-)
    11    ---
    12    Starting file in _master_ branch.
    13   -First commit on branch1.
    14   +First commit on branch1, modified.
    15   +2nd commit on branch1.

As you can see, a line that is modified is represented as a line that
is removed (original) and then added (new line).  The only two
concepts in a patch file are added and removed lines. + and -.  There
are no *modified* lines.

However, the really important thing to note here is that as the owner
of the code, if I have to read this patch file, there is a nasty
problem with it.  (I've number the lines of the patch file so they can
be referred to.)  The whole first patch file is redundant!  Lines 1 ->
7 are completely useless.  The reason being is that line 6 tells me
than a line has been added.  But then lines 13 and 14 tell me this
line has been modified.  All I really should have is line 14, just
telling me that a line has been added, but instead I have also lines
6, and 13 which don't help me in the slightest!  And actually the
whole first patch file is useless, so 50% of the information in this
patchfile is crap, and just makes my job of a reviewer harder.

So how can we fix this situation?  This is where squashing commits
comes in handy using the `rebase` command.

## Squash/Rebase

A patch file should represent a single commit, not several commits, as
you can have situations like the above where changes made in earlier
commits actually get superceded in subsequent commits.  We need to
squash all the commits into a single commit.  So lets do that.

    git rebase -i HEAD~2

This will pop open your editor with the following in it:

    pick 36a404f First commit on branch1
    pick 5dae0ab 2nd commit on branch1

Change this to:

    pick 36a404f First commit on branch1
    s 5dae0ab 2nd commit on branch1

Notice we've simply changed the word `pick` to the letter `s` meaning
squash this commit.

Save and exit your editor, this will pop open another editor with the
following contents (edited slightly), allowing you to adjust your
commit comments:

    # This is a combination of 2 commits.
    # The first commit's message is:
    First commit on branch1
    # This is the 2nd commit message:
    2nd commit on branch1

You can simply just leave this as is, and exit your editor to finish
the process.  You'll see the following output:

    [detached HEAD db947ce] First commit on branch1
     1 files changed, 2 insertions(+), 0 deletions(-)
    Successfully rebased and updated refs/heads/branch1.

Now lets look at the log history (slightly edited):

    $ git log master..HEAD
    commit db947ce2b427a241034057faf07f7fed8f3e5f3c
        First commit on branch1
        2nd commit on branch1

Compare this to the last time we looked at the log history, now we
only have one single commit (albeit with a two line comment in it),
but the important part is, is that this is just one commit *not* two!

Lets see what the patch file looks like now too:

    1  Subject: [PATCH] First commit on branch1
    2  2nd commit on branch1
    3   testBranch.mmd |    2 ++
    4   1 files changed, 2 insertions(+), 0 deletions(-)
    5   ---
    6   Starting file in _master_ branch.
    7  +First commit on branch1, modified.
    8  +2nd commit on branch1.

This is 8 lines long instead of 14, and this is just for a trivial
edit, it just gets worse the more commits you have!  As you can see on
line 7, we now have just the information that is necessary, and that
is that a line was added.  Not the whole nasty previous patch file
with two commits, with the first commit not giving *any* meaningful
information.

Now sometimes you are working on your code for a long time, and the
owner of the code has made their own changes to the master branch in
the mean time.  I'll demonstrate how we can deal with this situation
now too.  Lets simulate this situation with master moving forward
while we are still working on our branch, so lets do just that now.
Switch back to master branch.

    $ git checkout master
    Switched to branch 'master'

Insert a line at the top of the file:

    Came back to master and change original file.
    Starting file in _master_ branch. 

Notice on master we don't see the changes that were made on `branch1`
we simply see the file how is was at the time of the branching.

Now lets commit the change:

    $ git commit -am'moving master forward 1'

then switch back to `branch1`, and lets look at what a patch file
looks like.

    $ git format-patch master --stdout > ~/Desktop/file.patch

    Subject: [PATCH] First commit on branch1
    2nd commit on branch1
     testBranch.mmd |    2 ++
     1 files changed, 2 insertions(+), 0 deletions(-)
    diff --git a/testBranch.mmd b/testBranch.mmd
    @@ -1 +1,3 @@
     Starting file in _master_ branch.
    +First commit on branch1, modified.
    +2nd commit on branch1.

Interesting, the patch file doesn't show us the new stuff on master.
So this is really just a patch of whats on `branch1` compared to where
it branched from `master`, NOT where `master` is at the moment.  So at
some point you need to bring your branch back to master, but now our
patch file is only going to help with where we branched from, *not*
where master is *now*!!!  What we want to always do is have our branch
be able to patch where `master` is now, not where it was.  So what we
need to do is `rebase` our branch over where master is now, and we can
do that with the following command, while on our branch:

    $ git rebase master
    First, rewinding head to replay your work on top of it...
    Applying: First commit on branch1
    Using index info to reconstruct a base tree...
    Falling back to patching base and 3-way merge...
    Auto-merging testBranch.mmd

So lets see what a patch file looks like now:

    @@ -1,2 +1,4 @@
     Came back to master and change original file.
     Starting file in _master_ branch.
    +First commit on branch1, modified.
    +2nd commit on branch1.
    -- 

As we can see, now we have that first line from the latest, moved
forward `master`.  Now when we supply the patch file to the *owner* of
the master branch, they'll be able to trivially merge the changes
onto the latest master.

# Cookbook Recipies

## Reduce Repo Size

Sometimes you checked in and committed a file that down the road you
really didn't want or need in your repo, and your repo has gotten big
and unmanageable.

### Find the big commits

## Branching

    git checkout -b branch1

where `branch1` is the name of the new branch, choose a name
appropriate for your fix/enhancement

## Counting your commits

    git log master..HEAD

This will tell you the number of commits you have done.  If you have
more than one, then you need to do some squashing.

## Squashing

while on your branch (note the ~2 should be change to the number of
commits you have, if you have 3 commits, type ~3)

    git rebase -i HEAD~2

Change: 

    pick 3ee3e6b .
    pick f875a3c .
    pick 5eeec57 .

to:

    pick 3ee3e6b .
    s f875a3c .
    s 5eeec57 .

## Creating a Patch File

    git format-patch master --stdout > ~/Desktop/file.patch

You can obviously change the name of the patch file from `file.patch`
to something more descriptive if you want.

## Rebase on latest master

    git rebase master
