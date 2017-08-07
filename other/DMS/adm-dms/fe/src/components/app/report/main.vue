<template>
	<div class="amp-content">
		<m-nav :config="navMenu"></m-nav>
		<div class="content-main">
			<div class="amp-account">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>
<script>
import Nav from '../../common/nav.vue';
import { hasPrivilege, isRouterChecked } from 'utils/common';
export default {
	name: 'app-report-main',
	data() {
		return {
			navMenu: [{
				msg: '广告位报表',
				link: '/#/app/report/slot-slot',
				checked: isRouterChecked(this.$route, 'app/report/slot-slot'),
				permission: 'report:slot',
				sub: []
			}, {
				msg: '广告主报表',
				link: 'javascript:void(0)',
				checked: isRouterChecked(this.$route, 'app/report/anicer'),
				permission: 'report:anicer',
				sub: [{
					msg: '广告报表',
					link: hasPrivilege('report:anicer:anice:summary') ? '/#/app/report/anicer-anice-summary' : '/#/app/report/anicer-anice-detail',
					permission: 'report:anicer:anice'
				}, {
					msg: '返利报表',
					link: hasPrivilege('report:anicer:rebate:summary') ? '/#/app/report/anicer-rebate-summary' : '/#/app/report/anicer-rebate-detail',
					permission: 'report:anicer:rebate'
				}]
			}, {
				msg: '收入报表',
				link: 'javascript:void(0)',
				checked: isRouterChecked(this.$route, 'app/report/income'),
				permission: 'report:income',
				sub: [{
					msg: '广告收入',
					link: '/#/app/report/income-anice-income',
					permission: 'report:income:anice'
				}, {
					msg: '返利收入',
					link: '/#/app/report/income-rebate-income',
					permission: 'report:income:rebate'
				}]
			}, {
				msg: '充值消费记录',
				link: 'javascript:void(0)',
				checked: isRouterChecked(this.$route, 'app/report/record'),
				permission: 'report:record',
				sub: [{
					msg: '广告主充值',
					link: hasPrivilege('report:record:recharge:summary') ? '/#/app/report/record-recharge-summary' : '/#/app/report/record-recharge-detail',
					permission: 'report:record:recharge'
				}, {
					msg: '广告主消费',
					link: hasPrivilege('report:record:cosume:summary') ? '/#/app/report/record-cosume-summary' : '/#/app/report/record-cosume-detail',
					permission: 'report:record:cosume'
				}]
			}]
		};
	},
	components: {
		'm-nav': Nav,
	}
};
</script>
