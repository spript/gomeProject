<template>
    <div class="amp-content">
    	<m-nav :config="navMenu"></m-nav>
	    <div class="content-main">
	    	<div class="amp-account">
	    		<m-drawer></m-drawer>
 				<router-view></router-view>
 			</div>
	    </div>
    </div>
</template>
<script>
import Nav from '../../common/nav.vue';
import allDrawer from './mission/drawer.vue';
export default {
	name: 'app-material-main',
	data() {
		return {
			navMenu: [{
				msg: '用户任务',
				link: 'javascript:void(0)',
				icon: 'icon-mission-selected',
				checked: true,
				sub: [{
					msg: '任务管理',
					link: '/#/app/consumer/missionManage'
				}, {
					msg: '数据报表',
					link: '/#/app/consumer/missionReport'
				}]
			}]
		};
	},
	components: {
		'm-nav': Nav,
		'm-drawer': allDrawer
	}
};
</script>
