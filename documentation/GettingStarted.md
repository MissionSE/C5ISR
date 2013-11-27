Getting Started
===============

This document describes the method by which new developers can get started and contribute to this project.

Setting Up Your Environment
---------------------------

To get started, you will need to prepare your environment for Android development. Google has provided a [guide](http://developer.android.com/training/index.html) that describes how to get the [Android SDK](http://developer.android.com/sdk/index.html). This project uses Eclipse and the ADT plugin, and so you can download the ADT Bundle as provided by Google.

It is recommended that you create a new workspace specifically for all of the projects that you may import when working in this repository.

Once you have your environment set up, you can now import any of the libraries and examples in this repository. Each library and example are individually a separate project, and may require that you import other ones that they are dependent on. See the [Troubleshooting guide](Troubleshooting.md) for help.

Checkstyle Static Analysis
--------------------------

This project employs [Checkstyle](http://checkstyle.sourceforge.net) as a means of [static code analysis](http://en.wikipedia.org/wiki/Static_program_analysis). Checkstyle failures prohibit successful builds, and therefore need to be resolved before any commits are made.

To use Checkstyle, open the Eclipse Marketplace, and install the [Checkstyle Plug-in](http://eclipse-cs.sourceforge.net/).

Once installed:

1. Navigate to `Preferences...` -> `Checkstyle`.
2. Click the 'New...' button.
3. In the dialog that appears, set `Type` to `External Configuration File`, `Name` to `Android Code Style`, and selected the [android_style_checks.xml](../staticanalysis/android_style_checks.xml) file in the repository.
4. Once complete, your imported file will appear in the `Global Check Configurations` list. Select the `Android Code Style` item, and `Set as Default`.
5. Finally, right click on the imported projects, and under the `Checkstyle` sub-menu, select `Activate Checkstyle`. (This is only necessary if the project doesn't already have it activited. You will need to activate it for new projects, which changes the `.project` file.)

Once you have set up Checkstyle, you will will probably get an error. The root directory of the project needs to be defined in order for the suppression filters (filters that keep Checkstyle from applying to specific files, such as `R.java`) to work. To resolve this:

1. Navigate to `Preferences...` -> `Checkstyle`.
2. Select the configuration we just created (`Android Code Style`, if you followed the previous steps).
3. Click the `Properties...` button.
4. In the dialog that appears, click the `Additional properties...` button.
5. In the dialog that appears, click the `Find unresolved properties` button, and Click `Yes` when prompted. We will need to define the `${repository.root.dir}` variable.
6. Select the repository.root.dir variable as displayed in the list, and click the `Edit...` button.
7. When prompted, enter the full path to the C5ISR repository root (i.e., /home/yourname/dev/C5ISR).
8. Save your changes.
