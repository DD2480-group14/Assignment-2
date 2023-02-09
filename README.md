# Continuous Integration

**Warning: This is a university project of no use.**

An implementation of a CI server as described in [the specification](https://canvas.kth.se/courses/37918/assignments/235346).


## Installing Prerequisites

To build this program you need to have [JDK 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) (or later) and [Maven 3.6](https://maven.apache.org/download.cgi) (or later).

### Linux

If you are using system with `apt` package manager (e.g. Ubuntu, Debian), you can install JDK and Maven using:

```
apt install default-jdk maven
```

If you are using `pacman` (e.g. Arch), you can install the prerequisites by:

```
pacman -S jdk-openjdk maven
```

### Windows

On Windows you can download [JDK](https://www.oracle.com/java/technologies/downloads/#java8-windows) and [Maven](https://dlcdn.apache.org/maven/maven-3/3.8.7/binaries/apache-maven-3.8.7-bin.zip) installers from the official websites. Once they are installed, you will need to add corresponding `\bin` folders to your PATH: more detailed instruction is [available](https://maven.apache.org/install.html#windows-tips) on Maven's site.


TEST

## Building

The used Maven configuration can be found in `pom.xml`. JUnit is used for testing. To create an executable in `target/assignment-1.0-SNAPSHOT.jar` you can run:

```
mvn clean verify
```

This command also runs all the tests and checks configured for the code.


## Running

In order to run the server, you will need to specify two environment variables: CI_SECRET and CI_TOKEN. CI_SECRET is secret value from GitHub webhook settings, CI_TOKEN is your access token with right to set up statuses for commits.


## Code Style

The code style is enforced by the build system. The file describing the formatting rules is `format.xml`, it is in the Eclipse file-format. The code style is based on [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with indentation by 4 spaces instead of 2. You can use Maven to automatically apply the code style by running:

```
mvn spotless:apply
```


## Contributing

You can choose any open task on the project's GitHub Issues [page](https://github.com/DD2480-group14/Assignment-1/issues) and create a branch named `issues/#` where # is the number of the issue you want to work on. After committing to the branch to the point where you feel like the work is done, you can create a pull request. All pull requests need to pass all existing project tests, as well as comply to the project's code style. Additionally, before being merged a pull request needs to be reviewed by someone else.



### Aleksey Veresov

### Jonas Hulthén

### Christoffer Lundholm

### Tianyu Deng


## TEAM

We estimate our way of working to be at "Collaborating" level.

### Seeded

We have defined the composition and division of labor of the team. We selected remote cooperation as the way of working. 

### Formed 

We discussed the work flow and assigned the responsibilities of each person via Slack meeting, which made everyone understand the whole team and their own roles. By using github issues, we can all understand what need to accomplish and keep track of tasks.

### Collaborating 

We have completed our homework together, which proves that we are cohesive. We communicate with each other through slip and GitHub comment, honestly express our work progress and difficulties, and solve problems through communication to jointly accomplish team mission. This certainly shows that we understand and trust each other.

### Performing 

We think we didn't reach this level because we didn't meet some requirements: We didn't keep recognizing wasted work and the potential for wasted work and use some tools such as Java CI to help us to identify some coding problems.