<?php
defined('SYSPATH') OR die('No direct access allowed.');

return array (
	'default' => array (
		'type' => 'PDO',
		'connection' => array(
			'dsn' => 'mysql:host=g1mysm01.ad.pre.gomeplus.com;port=3306;dbname=ams_launch;charset=utf8',
			'username' => 'ams_user',
			'password' => 'SCi9kH8A',
			'persistent' => FALSE,
		),
		'table_prefix' => 'ams_',
		'charset' => 'utf8',
		'caching' => FALSE,
	),
);