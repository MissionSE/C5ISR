var requestify = require('requestify');
var randomstring = require('random-string');
var commander = require('commander');
var async = require('async');
var sleep = require('sleep');
var request = require('request');

commander
	.version('0.0.1')
	.option('-l, --local', 'Target localhost. This option should be used when running this script from the same machine on which the server is running.')
	.option('-r, --reports [num]', 'The number of reports to generate. Defaults to 10.', 10)
	.option('-c, --custom', 'Creates reports out of specific data, in the data.js file. Will override -r.')
	.option('-m, --mad', 'Target mad. This option should be used when you want to generate data on the internal MSE \'mad\' server. If set, this will override -l.')
	.parse(process.argv);

var target = '198.74.57.149';
if (commander.local) {
	target = 'localhost';
}
if (commander.mad) {
	target = '192.168.195.170';
}

var numberOfReportsToGenerate = commander.reports;

if (commander.custom) {
	numberOfReportsToGenerate = 0;
}

var subjects=['I','You','Bob','John','Sue','Kate','The lizard people','We','Everyone','Mike','Roberto','Kyle','James'];
var verbs=['will search for','will get','will find','attained','found','will start interacting with','will accept','accepted','will eat'];
var objects=['Billy','an apple','a Triforce','the treasure','a sheet of paper'];
var endings=['.',', right?','.',', like I said.','.',', just like your momma!'];

var index = 0;
async.whilst(
	function() {
		return (index < numberOfReportsToGenerate);
	},
	function(next) {
		var validLatLong = false;
		var generatedLat, generatedLong;
		var generatedAddress;
		async.doWhilst(
			function(next) {
				generatedLat = (Math.random() * 111 - 54).toFixed(2); //-54 to 67
				generatedLong = (Math.random() * 360 - 180).toFixed(2);

				var reverseGeocodingRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
					generatedLat + "," + generatedLong +
					"&sensor=false&result_type=political&key=AIzaSyAx7ziJyqi_s5X0Pr-iNTJFquAnQCdcr1I";

				requestify.get(reverseGeocodingRequest)
					.then(function(response) {
						var data = response.getBody();
						if (data.status != "ZERO_RESULTS") {
							generatedAddress = data.results[0].formatted_address;
							console.log('found: ' + generatedAddress);
							validLatLong = true;
						}
						next();
					});
			},
			function() {
				return !validLatLong;
			},
			function(err) {
				if (err) {
					console.log('got err while finding location: ' + err);
				}

				// Calculate city and country
				var calculatedCity, calculatedCountry;
				var commaSeparatedAddress = generatedAddress.split(",");

				if (commaSeparatedAddress.length < 2) {
					calculatedCity = "Unknown";
					calculatedCountry = generatedAddress;
				} else {
					calculatedCountry = commaSeparatedAddress.pop();
					calculatedCity = commaSeparatedAddress.toString();
				}

				// Calculate created and updated times
				var calculatedCreatedAtTime, calculatedUpdatedAtTime;
				calculatedCreatedAtTime = Date.now() - ((1000 * 60) * Math.random() * (60 * 24  * 7));
				calculatedUpdatedAtTime = calculatedCreatedAtTime;

				// Get valid weather data
				var openWeatherRequest = 'http://api.openweathermap.org/data/2.5/weather?lat=' + generatedLat +
					'&lon=' + generatedLong;

				console.log('requesting data from open weather...');
				requestify.get(openWeatherRequest)
					.then(function(response) {
						var data = response.getBody();

						var calculatedConditionCode = data.weather[0].id;
						var calculatedWeatherDescription = data.weather[0].description;
						var calculatedTemp = data.main.temp - 272.15;
						var calculatedHumidity = data.main.humidity;
						var calculatedPressure = data.main.pressure * 0.1;

						var calculatedWindSpeed = data.wind.speed;
						var calculatedWindDir = data.wind.deg;

						var windSpeedInKpH = calculatedWindSpeed * 1000 / 3600;
						var calculatedWindChill = 13.12 + (0.6215 * calculatedTemp) -
							(11.37 * Math.pow(windSpeedInKpH, 0.16)) +
							(0.3965 * calculatedTemp * Math.pow(windSpeedInKpH, 0.16));

						console.log('uploading data to the target...');
						requestify.post('http://' + target + ':3009/upload', {
							userid: randomstring({ length: 8, numeric: false }),
							latitude: generatedLat,
							longitude: generatedLong,

							createdat: calculatedCreatedAtTime,
							updatedat: calculatedUpdatedAtTime,

							title: generatedAddress,

							kestrel: {
								temperature: calculatedTemp,
								humidity: calculatedHumidity,
								pressure: calculatedPressure,
								pressuretrend: (Math.random()).toFixed(0),
								heatindex: calculatedTemp + (Math.random() * 10),
								windspeed: calculatedWindSpeed,
								winddirection: calculatedWindDir,
								windchill: calculatedWindChill,
								dewpoint: (Math.random() * 50 + 50).toFixed(2)
							},
							weather: {
								conditioncode: calculatedConditionCode,
								description: calculatedWeatherDescription,
								name: calculatedCity,
								country: calculatedCountry
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
							console.log('created report ' + (index + 1));
							console.log('response: ' + response.getBody());
							sleep.sleep(2);
							index++;
							next();
						});
					});
			}
		);

	},
	function(err) {
		console.log('finished!');
	}
);

if (commander.custom) {
	var customData = require('./data.js');

	for (var index = 0; index < customData.length; index++) {
		requestify.post('http://' + target + ':3009/upload', customData[index])
		.then(function(response) {
			console.log('created report with custom data...');
		});
	}
}
