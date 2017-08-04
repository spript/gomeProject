<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<router-view></router-view>
	</div>
</template>
<script>
import Breadcrumb from '../../common/breadcrumb.vue';
export default {
	name: 'app-account-account-overview',
	data() {
		return {};
	},
	components: {
		'm-breadcrumb': Breadcrumb,
	},
	computed: {
		breadcrumbConfig(){
			return {
				msg: '账户明细',
				showBack: false
			};
		}
	}
};
</script>
