KestrelWeather Server
=====================

This directory contains the KestrelWeather server application, written in node.js javascript. All of the commands described below must be run from the root directory, where this file is located, unless otherwise stated.

Dependencies
------------

It requires the following to be installed separately:

	node.js (and npm)
	mongodb
	imagemagick

Installation
------------

To install, run:

	npm install

Running the Server
------------------

To start the server, run:

	node server

To start the server with debug information, run:

	DEBUG=kestrel:* node server

See the [debug](https://www.npmjs.org/package/debug) module for more information on the DEBUG parameter.

For a production environment, a wrapper has been provided that utilizes the [forever](https://www.npmjs.org/package/forever) module to daemonize the process. To use this feature, run:

	node kestrel-wrapper

This should be executed from the relevant startup script. Note that the main server script is referenced with a relative path, and will it is therefore necessary to change directory to the `web` directory in which these scripts live.

For more help, run:

	node server -h

Adding Test Data
----------------

A utility script has been provided to quickly and easily generate test data. This script will create any number of reports with random (but valid) data (and no media). To use, run:

	node util/generatetestdata

Each execution of this script will create 10 reports. To specify a certain number of reports to be created, run:

	node util/generatetestdata -r NUMBER

If you would like to generate specific data, run:

	node util/generatetestdata -c

When doing so, the script will automatically pull data from the `data.js` file in the `util/` directory, and create reports.

This script assumed you are not running it from the same machine on which the server is running. If you are, you should tell the machine to use localhost instead, by running:

	node util/generatetestdata -l

For more information, you can access help by running:

	node util/generatetestdata -h

Notes
-----

As of `3/14/2014`, the `mad` MSE server is running the latest Kestrel Weather server application, updated to commit a1874b7787c1f7d4eff74962968af688e1684d62.

`mad` currently does not have any init scripts that ensure the server will run on demand; therefore, should the machine ever restart, the Kestrel Weather server will not be started (and it is likely that the mongo daemon will also not be ready).

To start the server on `mad`, first ensure that the mongo daemon is ready by running:

	mongo

If the above lets you connect, move on to starting the Kestrel Weather server app, by manually invoking node on the wrapper as noted above. If the mongo daemon is not running, you will need to run:

	sudo mongod
