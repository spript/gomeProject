var path = require('path');
var env = require('./env.config.js');

module.exports = {
	build: {
		env: env,
		index: path.resolve(__dirname, '../../src/main/resources/templates/production/index.html'),
		assetsRoot: path.resolve(__dirname, '../dist/pro'),
		assetsSubDirectory: '/static',
		assetsPublicPath: '/pro/',
		assetsPath: '/',
		productionSourceMap: false,
		productionGzip: false,
		productionGzipExtensions: ['js', 'css']
	},
	dev: {
		env: env,
		port: 5659,
		assetsSubDirectory: 'static',
		assetsPublicPath: '/',
		assetsPath: '/',
		proxyTable: {},
		cssSourceMap: false
	},
	bDev: {
		env: env,
		index: path.resolve(__dirname, '../../src/main/resources/templates/development/index.html'),
		assetsRoot: path.resolve(__dirname, '../dist/dev'),
		assetsSubDirectory: '/static',
		assetsPublicPath: '/dev/',
		assetsPath: '/',
		productionSourceMap: false,
		productionGzip: false,
		productionGzipExtensions: ['js', 'css']
	},
	pre: {
		env: env,
		index: path.resolve(__dirname, '../../src/main/resources/templates/preproduction/index.html'),
		assetsRoot: path.resolve(__dirname, '../dist/pre'),
		assetsSubDirectory: '/static',
		assetsPublicPath: '/pre/',
		assetsPath: '/',
		productionSourceMap: false,
		productionGzip: false,
		productionGzipExtensions: ['js', 'css']
	}
};

