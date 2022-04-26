# Git utilisation

## Start new task

**Go to master branch :**
```shell
git checkout master
```

**Pull to be up to date :**
```shell
git pull
```

**Create your new branch link to your task :**
```shell
git checkout -b (front/back/image)/task-name
```
You can start !

## Who am i in my work ?
```shell
git status
```
You gonna view every files need to be / be add to your next commit.

## Push your work

**When you are in your working branch, do status to show files changed/added/deleted :**
```shell
git status
```

**You can add files with this command :**
```shell
git add filename
```

**Now you need to commit your added changes :**
```shell
git commit -m "[front/back/image] your changes message"
```

**And let's push it to your branch :**
```shell
git push -u origin (front/back/image)/task-name
```

## Make your branch up to date (To do every new work session)

**Switch to master branch :**
```shell
git checkout master
```

**Pulling changes :**
```shell
git pull
```

**Switch to your task branch :**
```shell
git checkout (front/back/image)/task-name
```

**Rebase from master :**
```shell
git rebase master
```
You'r up to work !

## Oups ! This is not my working branch !

**Stash your changes to pop it on your own branch :**
```shell
git stash
```

**Now you need to go to your real work branch :**
```shell
git checkout (front/back/image)/task-name
```

**And you can pop your stash to the good branch :**
```shell
git stash pop
```

## How to back to previous commit ?

**Show commits to select where you need to back :**
```shell
git log
```

**Reset to previous commit :**
```shell
git reset --soft commit_id
```