# Shell Pipes vs Java Streams
Demonstration of scripting power of Java compared to Shell.

Check out the how similar Bash Pipes and Java Streams can now get at:
http://www.diffnow.com/?report=ith6b

Update: now added self closing file writer collector which closes upon stream end similar to file writer in Bash closes upon pipe closure.

Unfortunately Java Streams have a large limitation. There have no the real equivallent of the Bash functions. The only similarity is offered by the Java Collectors, but they do not run in parallel because they are reduction operators. You can use the stream as argument of a function, but that will hurt the readability.
