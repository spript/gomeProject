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
import allDrawer from './publisher/drawer.vue';
export default {
	name: 'app-material-main',
	data() {
		return {
			navMenu: [{
				msg: '媒体',
				link: '/#/app/resource/publisher',
				icon: 'icon-media',
				checked: true,
				sub:[]
			}, {
				msg: '广告位',
				link: '/#/app/resource/advertising',
				icon: 'icon-mediaAd',
				checked: false,
				sub: []
			}, {
				msg: '广告形式',
				link: '/#/app/resource/advertForm',
				icon: 'icon-mediaAdForm',
				checked: false,
				sub: []
			}, {
				msg: '模板',
				link: '/#/app/resource/template',
				icon: 'icon-mediaTemplate',
				checked: false,
				sub: []
			}]
		};
	},
	components: {
		'm-nav': Nav,
		'm-drawer': allDrawer
	}
};
</script>