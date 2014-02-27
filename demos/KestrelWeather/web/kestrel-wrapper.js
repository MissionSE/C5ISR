var forever = require('forever-monitor');

var child = new (forever.Monitor)('./server.js', {
	max: 5,
	silent: true,
	sourceDir: './',
	logFile: __dirname + '/logs/kestrel.out',
	outFile: __dirname + '/logs/kestrel.out',
	errFile: __dirname + '/logs/kestrel.out'
});

child.on('exit', function () {
	console.log('kestrel server has exited after 5 restarts');
});

child.start();
