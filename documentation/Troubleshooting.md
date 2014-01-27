Troubleshooting
===============

This document contains common problems and their solutions.

Android Studio
--------------

**TODO: Fill out this section.**

---

Eclipse
-------

### My imported project is missing references.

Many of the projects reference other projects. They are placed where they are so that when each developer imports a project, they are all still relative to each other in the same way. To fix this issue, simply import the missing dependency. To determine which dependencies are missing, right click on the broken project, and select `Properties`. In the `Android` sub-menu, you can see which projects are referenced. Missing projects will have a red 'X', and need only be imported.

### My project can't find a third-party library, and I can't import it.

Third-party libraries are archived in .zip format for ease-of-use. To import them, you will have to unzip them first. You can simply use the unzip command: `unzip file.zip`.
