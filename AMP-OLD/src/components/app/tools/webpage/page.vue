<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<m-list></m-list>
	</div>
</template>
<script>
import actions from 'actions';
import Event from 'event';
import Breadcrumb from '../../../common/breadcrumb.vue';
import PageList from './list.vue';
export default {
	name: 'app-account-account-overview',
	data() {
		return {
			breadcrumbConfig: {
				data: [{
					msg: '工具',
					link: '/#/app/page/'
				}, {
					msg: '自建页面',
					link:'/#/app/page/list'
				}]
			}
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb,
		'm-list': PageList
	},
	methods: {
		showNew(obj) {
			actions.controlDrawer(this.$store, obj);
		}
	}
};
</script>
