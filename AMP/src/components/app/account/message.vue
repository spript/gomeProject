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
		return {
			breadcrumbConfig: {
				msg: '站内信',
				showBack: false
			}
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	}
};
</script>
