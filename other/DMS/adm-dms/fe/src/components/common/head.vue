<template>
	<div class="amp-header">
		<div class="header-logo"><router-link to="/app">gomeplus</router-link></div>
		<div class="header-nav">
			<ul>
				<li><router-link to="/app/index"></router-link></li>
				<li><router-link to="/app/report/anicer-anice-summary"></router-link></li>
			</ul>
		</div>
		<div @click="controlLogoutStatus()" class="header-login">
			<a href="javascript:void(0)" title="" class="login-after">
				<span class="after-face">
					<img :src="accountInfo.avatar !== '' ? accountInfo.avatar : avatar" width="30" height="30" alt="">
				</span>
				<span class="after-username">{{accountInfo.name}}</span>
				<em class="icon icon-arrow"></em>
			</a>
			<div v-show="logoutStatus" class="dialog dialog-black arrow arrow-black">
				<ul>
					<li @click="logout()"><a href="javascript:void(0)" title="">退出</a></li>
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
			logoutStatus: false,
			avatar:require('../../assets/img/user-face.png')
		};
	},
	computed: {
		accountInfo: () => store.state.accountInfo
	},
	mounted() {
		document.body.addEventListener('click', (e) => {
			let event = e || window.event;
			let ele = event.target || event.srcElement;
			while (ele) {
				if (ele.classList && [...ele.classList].indexOf('header-login') !== -1) {
					return;
				}
				ele = ele.parentNode;
			}
			this.logoutStatus = false;
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
						actions.setAccountInfo(this.$store, {});
						window.location.href = '/';
					}
				});
		}
	}
};
</script>
