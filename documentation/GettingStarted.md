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
5. Finally, right click on the imported projects, and under the `Checkstyle` sub-menu, select `Activate Checkstyle`.
