<?php
/**
 * 用户任务
 */
class Controller_Mission extends Controller_Render {

	protected $_layout = 'layouts/main';

	/**
	 * 首页
	 */
	public function action_index() {

		$this->_layout->content = View::factory('mission/index');
	}
}
