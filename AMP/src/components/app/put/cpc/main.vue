<template>
    <div class="amp-content">
    	<m-nav :config="navMenu"></m-nav>
	    <div class="content-main">
	    	<div class="amp-account" :class="{'amp-unit': /unit/.test($route.path), 'amp-originality': /idea/.test($route.path)}">
				<router-view></router-view>
				<m-drawer></m-drawer>
			</div>
	    </div>
    </div>
</template>
<script>
import Drawer from './drawer.vue';
import Nav from '../../../common/nav.vue';
export default {
	name: 'app-plan-main',
	data() {
		return {
			navMenu: [{
				msg: '投放管理',
				link: '/#/app/put/plan',
				icon: 'icon-account',
				checked: true,
				sub: []
			}, {
				msg: '数据报表',
				link: 'javascript:void(0)',
				icon: 'icon-balance',
				checked: false,
				sub: [{
					msg: '广告报表',
					link: '/#/app/put/report/plan/campaign'
				}, {
					msg: '返利报表',
					link: '/#/app/put/report/rebate/campaign'
				}]
			}]
		};
	},
	components: {
		'm-nav': Nav,
		'm-drawer': Drawer
	},
	created(){
		this.navMenu.map((item) => {
			if(item.link !== 'javascript:void(0)'){
				item.checked = item.link.substring(2, item.link.length) == this.$route.path;
			}else{
				item.sub.map((i,index) => {
					if(i.link.substring(2,i.link.length) == this.$route.path){
						item.checked = true;
						return false;
					}
				})
			}
		})
	}
};
</script>
