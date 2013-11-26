Developer's Guide
=================

This document defines coding conventions to be used, and other tasks that must be done, when contributing to this project.

Coding Style
------------

This project generally follows Google's own Java [style guide](http://source.android.com/source/code-style.html) for Android contributors. Highlights:

*   Fully qualify your imports.
*   Use an `m` prefix for non-public, non-static member variables.
*   Braces should appear at the end of the line, not on a newline.

Some notable deviations:

*   It is NOT necessary to prepend all files with copyright statement (at this time), nor is it necessary to add Javadoc comments to all functions. At a minimum, interfaces should be fully Javadoc'd.
*   It is not necessary to add a TODO comment when using the @SuppressWarning annotation.
*   It is not necessary to only use spaces for indentation. Tabs are recommended given the Eclipse requirement.

This project uses [Checkstyle](http://checkstyle.sourceforge.net) to enforce the above coding standards. See the [Getting Started guide](GettingStarted.md) for details on how to get it set up.

Documentation
-------------

Developers must properly document new libraries and examples when added. There are [templates](templates/) provided to help you get started. Generally, a `README.md` in the root directory of a library or example should describe the general purpose or use of the project, and all third-party libraries should be documented with the date the archived was saved, and where to find the original project.
