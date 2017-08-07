<template>
    <div class="amp-content">
    	<m-nav :config="navMenu"></m-nav>
	    <div class="content-main">
	    	<div class="amp-account">
				<!-- <m-list></m-list> -->
 				<router-view></router-view>
 			</div>
	    </div>
    </div>
</template>
<script>
import Nav from '../../common/nav.vue';
// import List from './list.vue';
export default {
	name: 'app-material-main',
	data() {
		return {
			navMenu: [{
				msg: '素材审核',
				link: 'javascript:void(0)',
				icon: 'icon-material',
				privilege:'approval:material',
				checked: true,
				sub: [{
					msg: '审核素材',
					link: '/#/app/approval/material/not',
					privilege:'approval:material:all'
				},{
					msg: '未审核素材',
					link: '/#/app/approval/material/not',
					privilege:'approval:material:not'
				}, {
					msg: '已通过素材',
					link: '/#/app/approval/material/passed',
					privilege:'approval:material:passed'
				}, {
					msg: '未通过素材',
					link: '/#/app/approval/material/notpassed',
					privilege:'approval:material:notpassed'
				}]
			},
			{
				msg: '广告主审核',
				link: 'javascript:void(0)',
				icon: 'icon-account',
				privilege:'approval:advertiser',
				checked: false,
				sub: [{
					msg: '未审核广告主',
					link: '/#/app/approval/advertiser/not',
					privilege:'approval:advertiser:not'
				}, {
					msg: '已通过广告主',
					link: '/#/app/approval/advertiser/passed',
					privilege:'approval:advertiser:passed'
				}, {
					msg: '未通过广告主',
					link: '/#/app/approval/advertiser/notpassed',
					privilege:'approval:advertiser:notpassed'
				}]
			},
			{
				msg: '问卷审核',
				link: 'javascript:void(0)',
				icon: 'icon-survey',
				privilege:'approval:survey',
				checked: false,
				sub: [{
					msg: '未审核问卷	',
					link: '/#/app/approval/survey/not',
					privilege:'approval:survey:not'
				}, {
					msg: '已通过问卷',
					link: '/#/app/approval/survey/passed',
					privilege:'approval:survey:passed'
				}, {
					msg: '未通过问卷',
					link: '/#/app/approval/survey/reject',
					privilege:'approval:survey:notpassed'
				}]
			}]
		};
	},
	components: {
		'm-nav': Nav,
		// 'm-list': List
	}
};
</script>
