# CSCI 3601 Lab #2 - Building a web server in Java with Javalin

[![Server Build Status](../../workflows/Server%20Java/badge.svg)](actions)

Here you will explore serving up a simple website that you create,
using a server written with the [Javalin framework][javalin-io] server.
Javalin is a micro framework for
creating web applications in Java which you will be using to create
the server component (back-end) of your website.

The client component (front-end) of your website will use JavaScript
to allow you to accept and process user input. We will use JUnit to test
the server code and introduce continuous integration using [GitHub Actions][ghactions].

Your specific tasks for this lab can be found in the [LABTASKS.md][labtasks] file in this repository.

## Overview of the project

You'll be building parts of a simple to-do list using a
client-server architecture. The server will be able to handle
simple HTTP GET requests, where a client (or a user) can
visit a URL such as `http://localhost:4567/api/users` and the server
will respond with JSON-formatted text containing
all the users the server knows about, e.g.,

```json
[
  {
    "_id":"588935f57546a2daea44de7c",
    "name":"Connie Stewart",
    "age":25,
    "company":"OHMNET",
    "email":"conniestewart@ohmnet.com"
  },
  {
    "_id":"588935f5597715f06f3e8f6c",
    "name":"Lynn Ferguson",
    "age":25,
    "company":"NIQUENT",
    "email":"lynnferguson@niquent.com"
  },
  {
    "_id":"588935f51c55b55c75a84848",
    "name":"Roseann Roberson",
    "age":23,
    "company":"OHMNET",
    "email":"roseannroberson@ohmnet.com"
  },
  ...
]
```

The client will be a combination of HTML, CSS, and JavaScript
that runs in the browser and converts user actions (such as
clicking a button) into requests to the server. In a "real"
system you'd want to display the results nicely as part of
the application web interface (like the nicely formatted
list of e-mails in GMail).
To keep this lab simple, however, you'll just display the "raw" JSON
that the client receives from the server.

This lab has two components:

* Implement the desired server functionality
* Implement a simple web client that allows users to
access that server functionality through HTML forms

The details of both of these components are in [LABTASKS.md](./LABTASKS.md).

## Setup

### Cloning the project in GitKraken

1. First, Open [GitKraken][gitkraken]. If you already have a project open, close it or open a new tab.
2. Click "Clone a repo", select "GitHub.com" and find this repo in "Repository to Clone"
3. Select where to put it and clone it
4. You can then click "Open Repo" from the notification that appears to open the repo

### Open the project in VS Code

Launch Visual Studio Code, and then choose `File -> Open Folder…`. Navigate to your clone
of the repo and choose `Open`.

You may see a dialog that looks like this if you don't already have the recommended extensions:

![Dialog suggesting installation of recommended extensions](https://user-images.githubusercontent.com/1300395/72710961-bf767500-3b2d-11ea-8ea4-fbbd39c78da5.png)

Don't worry if you don't get the dialog, it is probably because you already have them all installed.

Like in previous labs, click "Install All" to automatically install them.

### Install JSONView browser extension

You'll also want the JSONView extension for either Firefox or Chrome installed.
This will make JSON in the browser look pretty and actually be readable.

* Firefox: [JSONView][jsonview-firefox]
* Chrome: [JSONView][jsonview-chrome]

## Running Your project

We use the [Gradle][gradle] build tool to build and run our web application.
Gradle is a powerful system for defining, managing, and running tasks
that allows us to easily build and test our full web application.

We will be using Gradle from the terminal. You can do this either with the terminal app or the terminal built into VS Code.

From the project directory, you will need to change into the `server` directory with

```bash
cd server
```

From the server directory you can use Gradle to run the server:

```bash
./gradlew run
```

Your server should now be running on port 4567, the port we've configured Javalin to
run on.
Visit it at [http://localhost:4567][local] in your web browser. The
server will continue to run indefinitely until you stop it

To run the JUnit tests:

```bash
./gradlew test
```

You can also run your tests right in VS Code by clicking the "Run Test" link above them.

## Testing Your Project

There's very little meaningful logic in the client component of this
project so we're not going to worry about testing the client here.
We'll begin testing the client when we introduce Angular in subsequent
labs.

The server-side portion of this project will be tested using JUnit.
Server-side tests are located in the `src/test/java` directory.

To run your server-side tests, you have a few options:

You can run `./gradlew test` from the `server` directory in the terminal to run all tests and output info about the run to an HTML file.

Additionally, you can go to the Test pane on the left side of VS Code and run all or individual tests from there.
The test pane looks like this:

![VS Code test pane](https://i.vgy.me/TAfWcG.png)

You can also run your tests right in the code editor by clicking the "Run Test" link above them.

![VS Code inline testing](https://i.vgy.me/SxWfWh.png)

This last two options are particularly nice because it gives you better
integrated feedback in VS Code and the ability to just run single
tests or files of tests when you're trying to debug a complex problem.

## Checking your code coverage

We have [the JaCoCo (Java Code Coverage) plugin set up in Gradle](https://docs.gradle.org/current/userguide/jacoco_plugin.html)
so you can see how well your tests cover (i.e., exercise) your code. The command

```bash
./gradlew test jacocoTestReport
```

will run the tests followed by the test coverage report generator. The report is in fact
a small "website" composed of a collection of HTML files. To see the report open the file

```bash
server/build/jacocoHtml/index.html
```

If you generate and look at that report at the start of the lab, you'll see that you start
with 100% coverage of all the `user` files. You'd like to keep it that way, so check your
code coverage after major stories are finished and look for areas that you're not yet testing.

:bangbang: The server file itself currently has 0% coverage, although we think we could
improve that by using [Javalin's support for functional/integration tests](https://javalin.io/tutorials/testing). You are not obliged to provide any coverage for that. You should make
sure your tests cover things like your `ToDoController` and the like, though.

## Continuous Integration with GitHub Actions

[GitHub Actions][ghactions] is a Continuous Integration tool that performs
builds of your project every time you push to GitHub.
This is very helpful, as it makes keeping track of your testing
over the lifetime of a project very easy. Having a build/test
history makes it easy to find where, or when, your project broke,
which makes it a lot easier to figure out _why_ it broke and get
it fixed and building successfully again.

GitHub Actions is built into GitHub and free to use on open source projects (like ours).

We've done the hard part of setting up the config files for GitHub Actions so it will work automatically. You can find the tests in the "Actions" tab of the GitHub repo.

## Go do the lab!

Now that you're all set up, you should be ready to head over to [LABTASKS.md](./LABTASKS.md), where
most of the actual work of the lab is described.

## Resources

### Running in the command line

We include a Gradle wrapper which lets you run gradle tasks from the command line. So, you can run tasks like:

```bash
./gradlew test
```

### Handling requests in Javalin

* [Getting started in Javalin](https://javalin.io/documentation#getting-started)
* [Javalin tutorials](https://javalin.io/tutorials/)
* [Testing Javalin](https://javalin.io/tutorials/testing)
* [Mockito testing in Javalin](https://javalin.io/tutorials/mockito-testing)
* [Best practices for REST interface design][rest-best-practices]
* [HTTP Status Codes][status-codes]

[javalin-io]: https://javalin.io
[gradle]: https://gradle.org/
[intellij-idea]: https://www.jetbrains.com/idea/
[jasmine]: https://jasmine.github.io/
[jasmine-introduction]: http://jasmine.github.io/2.0/introduction.html
[jsonview-chrome]: https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc?hl=en
[jsonview-firefox]: https://addons.mozilla.org/en-us/firefox/addon/jsonview/
[karma]: https://karma-runner.github.io/1.0/index.html
[labtasks]: LABTASKS.md
[local]: http://localhost:4567/
[rest-best-practices]: https://medium.com/@mwaysolutions/10-best-practices-for-better-restful-api-cbe81b06f291

[status-codes]: https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
[ghactions]: https://github.com/features/actions
[gitkraken]: https://www.gitkraken.com/git-client
