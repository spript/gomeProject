<?php
/**
 * 用户任务
 */
class Controller_Huodong extends Controller_Render {

	protected $_layout = 'layouts/default';

	/**
	 * 首页
	 */
	public function action_yidongtuiguang() {

		$this->_layout->content = View::factory('huodong/yidongtuiguang');
	}
}
