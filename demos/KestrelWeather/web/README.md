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

See the [debug](https://github.com/visionmedia/debug) node.js module for more information on the DEBUG parameter.
