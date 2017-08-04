<template>
    <div class="amp-header">
        <div class="header-logo"><router-link to="/app">gomeplus</router-link></div>
        <div class="header-nav">
            <ul v-if="!error">
            	<li></li>
                <li v-if="userInfo.isRegistered && userInfo.isApproved > 0" :class="{'actived': $route.path === '/app/index', 'head-nav-item': true}"><router-link to="/app/index">首页</router-link></li>
                <li v-if="userInfo.isRegistered && userInfo.isApproved  > 0" :class="{'actived': /^\/app\/account\/|^\/app\/account$/.test($route.path), 'head-nav-item': true}"><router-link to="/app/account">我的账户</router-link></li>
                <li v-if="userInfo.isRegistered && userInfo.isApproved  > 0" :class="{'actived': /^\/app\/put\/|^\/app\/put|^\/app\/bidcpc|^\/app\/bidcpc$/.test($route.path), 'head-nav-item': true}">
                	<a href="" @click.prevent="showSubMenu = !showSubMenu">产品线</a>
                	<ul class="head-menu" v-show="showSubMenu">
                		<li :class="{'is-active': /^\/app\/put\/|^\/app\/put$/.test($route.path), 'head-menu-item': true}" @click="showSubMenu = !showSubMenu" v-if="userInfo.isTest !== 1"><router-link to="/app/put" tag="span">定价cpc</router-link></li>
                		<li :class="{'is-active': /^\/app\/bidcpc\/|^\/app\/bidcpc$/.test($route.path), 'head-menu-item': true}" @click="showSubMenu = !showSubMenu"><router-link to="/app/bidcpc" tag="span">竞价cpc</router-link></li>
                	</ul>
                </li>
                <!-- <li v-if="userInfo.isRegistered && userInfo.isApproved  > 0" :class="{'actived': /^\/app\/report\/|^\/app\/report$/.test($route.path), 'head-nav-item': true}"><router-link to="/app/report">数据报表</router-link></li> -->
				<!--<li :class="{'actived': /^\/app\/register\/|^\/app\/register/.test($route.path)}"><router-link to="/app/register">商家入驻</router-link></li>-->
				<li v-if="userInfo.isRegistered && userInfo.isApproved  > 0" :class="{'actived': /^\/app\/tools\/|^\/app\/tools$/.test($route.path), 'head-nav-item': true}"><router-link to="/app/tools">工具</router-link></li>
            </ul>
        </div>
        <div @click="controlLogoutStatus()" class="header-login">
        	<a href="javascript:void(0)" title="" class="login-after">
        		<span class="after-face">
        			<img :src="userInfo.avatar !== '' ? userInfo.avatar : avatar" width="30" height="30" alt="">
        		</span>
        		<span class="after-username">{{userInfo.name}}</span>
        		<em class="icon icon-arrow"></em>
        	</a>
            <div v-show="logoutStatus" class="dialog dialog-black arrow arrow-black">
                <ul>
                    <li @click="logout()"><a href="javascript:void(0)" title="" style="text-align: center;">退出</a></li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
import Http from 'http';
import store from 'store';
import actions from 'actions';
import router from '../../route.js';
export default {
	name: 'head',
	data() {
		return {
			showSubMenu: false,
			logoutStatus: false,
			avatar:require('../../assets/img/user-face.png')
		};
	},
	computed: {
		userInfo: () => store.state.userInfo,
		error() {
			return /error/g.test(this.$route.name);
		}
	},
	mounted() {
		document.body.addEventListener('click', (e) => {
			let event = e || window.event;
			let ele = event.target || event.srcElement;
			while (ele) {
				if (ele.classList && ([...ele.classList].indexOf('header-login') !== -1 || [...ele.classList].indexOf('head-nav-item') !== -1)) {
					return;
				}
				ele = ele.parentNode;
			}
			this.logoutStatus = false;
            this.showSubMenu = false;
		});
	},
	methods: {
		controlLogoutStatus() {
			this.logoutStatus = !this.logoutStatus;
		},
		logout() {
			// 登出
			Http.post('api/logout')
				.then((res) => {
					if (!res.data.iserror) {
						// 跳转到登陆页
						actions.setUserInfo(this.$store, {});
						router.push({
							name: 'login'
						});
					}
				});
		}
	}
};
</script>
<style scoped>
.head-nav-item{
	position: relative;
}
.head-menu{
	position: absolute;
    top: 65px;
    left: 0;
    border: 1px solid #d1dbe5;
    padding: 5px 0;
    margin-left: 0 !important;
    background-color: #fff;
    z-index: 100;
    min-width: 100%;
    box-shadow: 0 2px 4px 0 rgba(0,0,0,.12), 0 0 6px 0 rgba(0,0,0,.04);
}
.head-menu-item {
	clear: both;
    background-color: #fff;
    float: none;
    height: 36px;
    line-height: 36px;
    padding: 0 10px;
    cursor: pointer;
    position: relative;
    box-sizing: border-box;
    border-bottom: 5px solid transparent;
}
.head-menu-item.is-active {
    color: #20a0ff;
}
.head-menu-item:hover {
    background-color: #d1dbe5;
}
</style>
