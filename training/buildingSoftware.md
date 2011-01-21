# Advice on how to start a project

1. First create the project you want with
[Maven](/maven.html#createProject)

2. Make sure that you allow the project to be added to the [GIT
repository](/training/git.html#addingProjects)

2. Next setup your pom.xml so the project will be reasonable.  The
best advice is to look at an existing pom.xml to figure out what to do.

2.1 Setup [distribution management](/maven.html#distributionManagement).

3. Get [git](/training/git.html#initialize) setup for your project.

3. Code and [release](/maven.html#release) a version of your artifact.


# Advice on how to collaborate with someone

It's important to share code via a VCS, and GIT is arguably the best
one around at the moment.  Often you are a windows user (developer).
You need to install [cygwin](/secure.html#WindowsLinuxOSX) will get
you setup with cygwin, ssh, and git.

## Sharing your code

Once you have these, you'll need to have a repository setup for you,
ask the git admin to do this for you.  Lets say your code is contained
in a folder called: 

    myProject

Then ask the git repo admin to create a git repo by that name.

1. cd in cygwin bash shell to that folder and do the steps to
initialize a project specified [here](/training/git.html#initialize).

The pieces of information you need are: repo connect string and repo
name.

        git@linux1.hk.oracle.com:myProject.git
        [----connect string----] [-Repo Name-]

So the steps above have let you push your repo up into the server.

<a href="getCode"></a>
## Getting other peoples code

Sometimes you'll want to pull down a repo, normally this is done with
a command like:

    git clone git@linux1.hk.oracle.com:crmod-ws-wrapper.git

Here the connect string is: git@linux1.hk.oracle.com, and the repo
name is crmod-ws-wrapper.git.  A developer may ask you to download a
repo so you can look at it and see how he does something.
