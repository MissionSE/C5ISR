var randomstring = require('random-string');

module.exports = [
	{
		userid: 'test-' + randomstring({ length: 8, numeric: false }),
		latitude: 39.9742894,
		longitude: -74.9766567,

		createdat: Date.now(),
		updatedat: Date.now(),

		title: 'Marlton, US',

		kestrel: {
			temperature: 59.055283,
			humidity: 60.123155,
			pressure: 30.825304,
			pressuretrend: '0',
			heatindex: 60.658104,
			windspeed: 3.752452,
			winddirection: 258.855253,
			windchill: -3.075103,
			dewpoint: 49.231145
		},
		weather: {
			conditioncode: 200,
			description: 'thunderstorm with light rain',
			name: 'Marlton',
			country: 'US'
		},
		notes: [{
			title: 'Observation #1',
			content: 'Major storm front coming from the north, wind is picking up.',
			size: '9',
			createdat: Date.now()
		}, {
			title: 'Observation #2',
			content: 'Dark clouds forming, potential heavy rain fall coming.',
			size: '8',
			createdat: Date.now()
		}, {
			title: 'Observation #3',
			content: 'Wind is increasing in speed. Potential precursor to a tornado.',
			size: '4',
			createdat: Date.now()
		}]
	}
];
