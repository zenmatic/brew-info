# spit out a random number of homebrew package descriptions

(just for fun)

## build

```
sbt compile
sbt package
```

## use

`scala target/scala-2.13/brewinfo_2.13-1.0.jar update` adds
description to `brew update` output.  Takes stdin.

`scala target/scala-2.13/brewinfo_2.13-1.0.jar random` spits
out 10 random formulas with descriptions.

## cron

add to cron for daily email:
```
PATH=/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin
30 1 * * * brew update | scala target/scala-2.13/brewinfo_2.13-1.0.jar update
0  2 * * * scala target/scala-2.13/brewinfo_2.13-1.0.jar random
```
