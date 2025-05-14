# Contributing to pega-launchpad-functions

<!-- TOC -->
* [Contributing to pega-launchpad-functions](#contributing-to-pega-launchpad-functions)
  * [Code of Conduct](#code-of-conduct)
  * [I Have a Question](#i-have-a-question)
  * [I Want To Contribute](#i-want-to-contribute)
    * [Reporting Bugs](#reporting-bugs)
      * [Before Submitting a Bug Report](#before-submitting-a-bug-report)
      * [How Do I Submit a Good Bug Report?](#how-do-i-submit-a-good-bug-report)
    * [Your First Code Contribution](#your-first-code-contribution)
<!-- TOC -->

## Code of Conduct

This project and everyone participating in it is governed by the
[pega-launchpad-functions Code of Conduct](https://github.com/pegasystems/pega-launchpad-functions/master/CODE_OF_CONDUCT.md).

By participating, you are expected to uphold this code. Please report unacceptable behavior
to <tim.miranda@pega.com>.

## I Have a Question

> If you want to ask a question, we assume that you have read the available documentation.

Before you ask a question, it is best to search for existing [Issues](https://github.com/pegasystems/pega-launchpad-functions/issues) that might help you. In case you have found a suitable issue and still need clarification, you can write your question in this issue. It is also advisable to search the internet for answers first.

If you then still feel the need to ask a question and need clarification, we recommend the following:

- Open an [Issue](https://github.com/pegasystems/pega-launchpad-functions/issues/new).
- Provide as much context as you can about what you're running into.
- Provide project and platform versions (nodejs, npm, etc), depending on what seems relevant.

We will then take care of the issue as soon as possible.

## I Want To Contribute

> ### Legal Notice <!-- omit in toc -->

> When contributing to this project, you must agree that you have authored 100% of the content, that you have the necessary rights to the content and that the content you contribute may be provided under the project license.

### Reporting Bugs

<!-- omit in toc -->
#### Before Submitting a Bug Report

A good bug report shouldn't leave others needing to chase you up for more information. Therefore, we ask you to investigate carefully, collect information and describe the issue in detail in your report. Please complete the following steps in advance to help us fix any potential bug as fast as possible.

- Make sure that you are using the latest version.
- Determine if your bug is really a bug and not an error on your side e.g. using incompatible environment components/versions (Make sure that you have read the [documentation](). If you are looking for support, you might want to check [this section](#i-have-a-question)).
- To see if other users have experienced (and potentially already solved) the same issue you are having, check if there is not already a bug report existing for your bug or error in the [bug tracker](https://github.com/miratim/PegaLPSTToolsissues?q=label%3Abug).
- Also make sure to search the internet (including Stack Overflow) to see if users outside of the GitHub community have discussed the issue.
- Collect information about the bug:
  - Stack trace (Traceback)
  - OS, Platform and Version (Windows, Linux, macOS, x86, ARM)
  - Version of the interpreter, compiler, SDK, runtime environment, package manager, depending on what seems relevant.
  - Possibly your input and the output
  - Can you reliably reproduce the issue? And can you also reproduce it with older versions?

<!-- omit in toc -->
#### How Do I Submit a Good Bug Report?

> You must never report security related issues, vulnerabilities or bugs including sensitive information to the issue tracker, or elsewhere in public. Instead sensitive bugs must be sent by email to <tim.miranda@pega.com>.
<!-- You may add a PGP key to allow the messages to be sent encrypted as well. -->

We use GitHub issues to track bugs and errors. If you run into an issue with the project:

- Open an [Issue](https://github.com/miratim/PegaLPSTTools/issues/new). (Since we can't be sure at this point whether it is a bug or not, we ask you not to talk about a bug yet and not to label the issue.)
- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else can follow to recreate the issue on their own. This usually includes your code. For good bug reports you should isolate the problem and create a reduced test case.
- Provide the information you collected in the previous section.

Once it's filed:

- The project team will label the issue accordingly.
- A team member will try to reproduce the issue with your provided steps. If there are no reproduction steps or no obvious way to reproduce the issue, the team will ask you for those steps and mark the issue as `needs-repro`. Bugs with the `needs-repro` tag will not be addressed until they are reproduced.
- If the team is able to reproduce the issue, it will be marked `needs-fix`, as well as possibly other tags (such as `critical`), and the issue will be left to be [implemented by someone](#your-first-code-contribution).

<!-- You might want to create an issue template for bugs and errors that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->

### Your First Code Contribution

#### Pre-requisities:

1. Request through sail point the GitHub access called GitHub - Pegasystems - Organization User
2. Have your own personal account on github.com
3. Once you have sailpoint access, go to https://github.com/orgs/pegasystems/sso to associate your pega sso login with that personal account
4. Share your personal github username with Tim Miranda, who will add you to the Launchpad team on github.com
   
#### Adding a new example:

1. Create a new branch
2. Add a new module to the /examples folder
3. Make sure your module has a README.MD similar to the other modules. This is for people to learn how to install and use your example.
4. Create a build.gradle.kts in your module, similar to the other modules. This will pull dependent libraries into your binary, and will let gradlew build generate the JAR or ZIP for your module, and run unit tests
5. Try to include at least one test, even if it is just a single scenario smoke test to ensure the build works correctly.
6. Build the whole project from /pega-launchpad-functions with gradlew build. Use the JAR or ZIP generated in the /build directory of your module to import into a Function rule and test your example in Launchpad.
7. Commit your changes to your branch, push them to github, create a pull request.
