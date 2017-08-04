<template>
	<div>
		<router-view v-if="error == 0"></router-view>
		<m-loading></m-loading>
		<m-error v-if="error != 0"></m-error>
	</div>
</template>
<script>
import Loading from './components/common/loading.vue';
import ErrorView from './components/error/main.vue';
import store from 'store';
export default {
	name: 'app',
	components: {
		'm-loading': Loading,
		'm-error': ErrorView
	},
	computed:{
		error(){
			return store.state.errorCode;
		}
	}
};
</script>
<style lang="less">
@import './assets/css/public.less';
@import './assets/css/amp.less';
@import './assets/css/transition.less';
@import './assets/css/table.less';
</style>
