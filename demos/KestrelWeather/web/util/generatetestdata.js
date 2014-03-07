var requestify = require('requestify');
var randomstring = require('randomstring');
var commander = require('commander');

commander
	.version('0.0.1')
	.option('-l, --local', 'Target localhost. This option should be used when running this script from the same machine on which the server is running.')
	.option('-r, --reports [num]', 'The number of reports to generate. Defaults to 10.', 10)
	.option('-c, --custom', 'Creates reports out of specific data, in the data.js file. Will override -r.')
	.parse(process.argv);

var target = '198.74.57.149';
if (commander.local) {
	target = 'localhost';
}

var numberOfReportsToGenerate = commander.reports;

if (commander.custom) {
	numberOfReportsToGenerate = 0;
}

var conditionCodes = [
	{ code: 200, text: 'thunderstorm with light rain' },
	{ code: 201, text: 'thunderstorm with rain' },
	{ code: 301, text: 'drizzle' },
	{ code: 312, text: 'heavy intensity drizzle rain' },
	{ code: 501, text: 'moderate rain' },
	{ code: 511, text: 'freezing rain' },
	{ code: 521, text: 'shower rain' },
	{ code: 601, text: 'snow' },
	{ code: 601, text: 'snow' },
	{ code: 611, text: 'sleet' },
	{ code: 701, text: 'mist' },
	{ code: 711, text: 'smoke' },
	{ code: 731, text: 'sand/dust whirls' },
	{ code: 741, text: 'fog' },
	{ code: 800, text: 'sky is clear' },
	{ code: 801, text: 'few clouds' },
	{ code: 802, text: 'scattered clouds' },
	{ code: 804, text: 'overcast clouds' }
];

var subjects=['I','You','Bob','John','Sue','Kate','The lizard people','We','Everyone','Mike','Roberto','Kyle','James'];
var verbs=['will search for','will get','will find','attained','found','will start interacting with','will accept','accepted','will eat'];
var objects=['Billy','an apple','a Triforce','the treasure','a sheet of paper'];
var endings=['.',', right?','.',', like I said.','.',', just like your momma!'];


for (var index = 0; index < numberOfReportsToGenerate; index++) {
	var conditionCodeIndex = (Math.random() * (conditionCodes.length - 1)).toFixed(0);
	requestify.post('http://' + target + ':3009/upload', {
		userid: randomstring.generate(8),
		latitude: (Math.random() * 180 - 90).toFixed(2),
		longitude: (Math.random() * 360 - 180).toFixed(2),

		createdat: Date.now(),
		updatedat: Date.now(),

		title: 'Springfield, ' + randomstring.generate(2).toUpperCase(),

		kestrel: {
			temperature: (Math.random() * 150 - 50).toFixed(2),
			humidity: (Math.random() * 100).toFixed(2),
			pressure: (Math.random() * 20 + 20).toFixed(2),
			pressuretrend: (Math.random()).toFixed(0),
			heatindex: (Math.random() * 150 - 35).toFixed(2),
			windspeed: (Math.random() * 40).toFixed(2),
			winddirection: (Math.random() * 360).toFixed(2),
			windchill: (Math.random() * 150 - 60).toFixed(2),
			dewpoint: (Math.random() * 50 + 50).toFixed(2)
		},
		weather: {
			conditioncode: conditionCodes[conditionCodeIndex].code,
			description: conditionCodes[conditionCodeIndex].text,
			name: 'Springfield',
			country: randomstring.generate(2).toUpperCase()
		},
		notes: [{
			title: 'Observation #1',
			content: subjects[Math.round(Math.random()*(subjects.length-1))] + ' ' +
					verbs[Math.round(Math.random()*(verbs.length-1))] + ' ' +
					objects[Math.round(Math.random()*(objects.length-1))] +
					endings[Math.round(Math.random()*(endings.length-1))],
			size: '23',
			createdat: Date.now()
		}, {
			title: 'Observation #2',
			content: subjects[Math.round(Math.random()*(subjects.length-1))] + ' ' +
					verbs[Math.round(Math.random()*(verbs.length-1))] + ' ' +
					objects[Math.round(Math.random()*(objects.length-1))] +
					endings[Math.round(Math.random()*(endings.length-1))],
			size: '55',
			createdat: Date.now()
		}, {
			title: 'Observation #3',
			content: subjects[Math.round(Math.random()*(subjects.length-1))] + ' ' +
					verbs[Math.round(Math.random()*(verbs.length-1))] + ' ' +
					objects[Math.round(Math.random()*(objects.length-1))] +
					endings[Math.round(Math.random()*(endings.length-1))],
			size: '9',
			createdat: Date.now()
		}]
	})
	.then(function(response) {
		console.log('create report with random data...');
	});
}

if (commander.custom) {
	var customData = require('./data.js');

	for (var index = 0; index < customData.length; index++) {
		requestify.post('http://' + target + ':3009/upload', customData[index])
		.then(function(response) {
			console.log('create report with custom data...');
		});
	}
}

