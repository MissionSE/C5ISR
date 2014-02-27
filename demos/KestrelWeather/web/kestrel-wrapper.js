var forever = require('forever-monitor');

var child = new (forever.Monitor)('server.js', {
	max: 5,
	silent: true,
	sourceDir: './',
	logFile: './logs/kestrel.out',
	outFile: './logs/kestrel.out',
	errFile: './logs/kestrel.out'
});

child.on('exit', function () {
	console.log('kestrel server has exited after 5 restarts');
});

child.start();
