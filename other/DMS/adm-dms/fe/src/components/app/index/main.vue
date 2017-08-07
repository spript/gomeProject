<template>
	<div class="amp-content">
		<div class="ams-index">
			<m-dashboard></m-dashboard>
			<div class='media-set'>
			<div class="clearfix">
				<!--<h2 style="font-size: 24px;text-align: center;">欢迎使用国美Plus Data Management System</h2>-->
			</div>
			<div class="action-content">
				<router-link class="btn btn-primary" :to="{path: getRoute('report:slot')}">广告位报表</router-link>
				<router-link class="btn btn-primary" :to="{path: getRoute('report:anicer')}">广告主报表</router-link>
				<router-link class="btn btn-primary" :to="{path: getRoute('report:income')}">收入报表</router-link>
				<router-link class="btn btn-primary" :to="{path: getRoute('report:record')}">充值消费记录</router-link>
			</div>
			<!--<div class="table-content" style="margin-top:40px;">
				<table class="table table-media">
					<thead>
						<tr class="media-tr">
							<th><span>事宜</span></th>
							<th><span>联络人</span></th>
							<th><span>邮箱</span></th>
							<th><span>电话</span></th>
						</tr>
					</thead>
					<tbody>
						<tr class="media-list-tr">
							<td><span>产品需求</span></td>
							<td><span>凌云</span></td>
							<td><span>lingyun@gomeplus.com</span></td>
							<td><span>18601247515</span></td>
						</tr>
						<tr class="media-list-tr">
							<td><span>技术反馈</span></td>
							<td><span>白珅</span></td>
							<td><span>baishen@gomeplus.com</span></td>
							<td><span>18612595283</span></td>
						</tr>
					</tbody>
				</table>
			</div>-->
			</div>
		</div>
	</div>
</template>
<script>
import Dashboard from './dashboard.vue';
import store from 'store';
export default {
	name: 'app-main',
	components: {
		'm-dashboard': Dashboard,
	},
	computed: {
		privileges() {
			return store.state.accountInfo.privileges;
		}
	},
	methods: {
		getRoute(permission) {
			let curPrivilege = this.privileges.find((privilege) => {
				return privilege.permission === permission;
			});
			if (curPrivilege == undefined) {
				return '/app/error403';
			} else if (curPrivilege.link === '') {
				let privilegeId = curPrivilege.privilegeId;
				let subPrivilege = this.getPrivilege(privilegeId);
				if (subPrivilege == undefined) {
					return '/app/error403';
				}
				return subPrivilege.link;
			}
			return curPrivilege.link;
		},
		getPrivilege(privilegeId) {
			let curPrivilege = this.privileges.find((privilege) => {
				return privilege.parentId === privilegeId;
			});
			if (curPrivilege.link !== '') {
				return curPrivilege;
			} else {
				curPrivilege = this.getPrivilege(curPrivilege.privilegeId);
			}
			return curPrivilege;
		}
	}
};
</script>
<style scoped lang="less">
.btn{
	margin-right: 30px;
	margin-bottom: 20px;
	cursor: pointer;
}

</style>
