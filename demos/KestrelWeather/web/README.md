KestrelWeather Server
=====================

This directory contains the KestrelWeather server application, written in node.js javascript. It requires the following to be installed separately:

	node.js (and npm)
	mongodb

All of the following commands must be run from the root directory, where this file is located.

To install, run:

	npm install

To start the server, run:

	node server

To start the server with debug information, run:

	DEBUG=kestrel:* node server

See the [debug](https://www.npmjs.org/package/debug) module for more information on the DEBUG parameter.

For a production environment, a wrapper has been provided that utilizes the [forever](https://www.npmjs.org/package/forever) module to daemonize the process. To use this feature, run:

	node kestrel-wrapper.js

This should be executed from the relevant startup script. Note that the main server script is referenced with a relative path, and will it is therefore necessary to change directory to the `web` directory in which these scripts live.
