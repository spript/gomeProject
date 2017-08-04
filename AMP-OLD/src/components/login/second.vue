<template>
	<div>
		<!--header-->
		<div class="login-header" id="login-header">
			<div class="header-logo">
				<a href="/"><img src="../../assets/img/login/logo.png"></a>
			</div>
			<div class="header-nav">
				<a class="nav-text" href="/">在线学习</a><i>|</i>
				<a class="nav-text" href="/">客服中心</a><i>|</i>
				<a class="nav-text" href="/">帮助中心</a>
				<a class="nav-login nav-btn"  @click="LoginVisible=true">登录</a>
				<a class="nav-reg nav-btn" :href="registUrl" target="_blank">注册</a>
			</div>
		</div>

        <swiper :options="swiperOption" ref="mySwiper">
  	        <!--banner01-->
			<swiper-slide>
				<div class="login-banner login-second ">
				  <div class="second-wrapper">
				  	 <p class="s-wapper-toptitle"><span>美媒</span>全民推广 引爆社交分享广告新模式</p>
				  	 <p class="s-wapper-topdes">美媒是国美Plus APP“逛逛”页面专注于“精选+品质”的路线，为用户呈现个性化及场景化的优质品牌内容，并以返利形式激活<br>
						 用户社交分享及互动的综合频道</p>
				  	 <p class="second-button"><a  @click="LoginVisible=true">马上体验全新广告模式</a></p>
				  </div>
				</div>
			</swiper-slide>
			  <!--banner02-->
			<swiper-slide>
				<div class="login-banner ">
				    <div class="second-wrapper">
					    <div class="s-wrapperImg">
					    <img src='../../assets/img/login-second/s-silder01.png'>
                        <p class="second-button button01"><a @click="LoginVisible=true">体验多重返利场景</a></p>
					    </div>

				    </div>
				</div>
			</swiper-slide>
			  <!--banner03-->
			<swiper-slide>
				<div class="login-banner ">
					<div class="second-wrapper">
					    <div class="s-wrapperImg">
					       <img src='../../assets/img/login-second/s-silder02.png'>
					       <p class="second-button button02"><a  @click="LoginVisible=true">3秒快速入驻</a></p>
					    </div>
				    </div>
				</div>
			</swiper-slide>
		 <!--banner04-->
			<swiper-slide>
				<div class="login-banner ">
					<div class="second-wrapper">
					    <div class="s-wrapperImg">
					       <img src='../../assets/img/login-second/s-silder03.png'>
					       <p class="second-button button03"><a  @click="LoginVisible=true">自助开通广告账户</a></p>
					    </div>
				    </div>
				</div>
			</swiper-slide>
			  <!--banner05-->
			<swiper-slide  id="theLastSlide" style="overflow-y:auto; overflow-x: hidden">
				<div class="login-banner ">
				    <div class="second-wrapper">
					    <div class="s-wrapperImg">
					       <img src='../../assets/img/login-second/s-silder04.png'>
					       <p class="second-button button04"><a  @click="LoginVisible=true">免费入驻</a></p>
					    </div>
				    </div>
				</div>
				<div class="login-banner-footer">
					<p>
						<a>友情链接</a>
						<a href="http://www.gome.com.cn/" target="_blank">国美在线</a>
						<a href="https://www.gomeplus.com" target="_blank">国美PLUS</a>
						<a href="https://www.gomefinance.com.cn" target="_blank">国美金融</a>
						<a href="http://cps.gome.com.cn" target="_blank" >国美联盟</a>
					</p>
					<p>Copyright &#x24B8; 2016 – 2017 All Rights Reserved. 国美互联网生态(分享)科技公司版权所有</p>
				</div>
			</swiper-slide>
        </swiper>
        <el-dialog v-model="LoginVisible" id="LoginForm">
			<div class="dialog-titleBox">
				<span class="dialog-title">登录</span>
			</div>
			<el-form :model="formData" :rules="rules" ref="formData">
				<el-form-item label="" prop="name">
					<el-input type="text" v-model="formData.name" placeholder="输入用户名" class="loginInputName"></el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input type="password" v-model="formData.password" placeholder="输入密码" class="loginInputPass"></el-input>
				</el-form-item>
				<el-form-item prop="captcha">
					<el-col :span="12">
						<el-input type="text" v-model="formData.captcha" placeholder="输入验证码"></el-input>
					</el-col>
					<el-col class="line" :span="1">&nbsp;</el-col>
					<el-col :span="11" style="{font-size: 0;}">
									<span class="valadate-img">
										<img v-bind:src="image">
									</span>
						<span class="valadate-text">
										<a href="javascript:void(0);" @click.prevent="getCaptcha()">换一换</a>
									</span>
					</el-col>
				</el-form-item>
			</el-form>
			<div class="wrap-btn">
				<button @click="login" class="btn btn-primary">登 录</button>
			</div>
			<div class="wrap-help">
				<a :href="registUrl" class="help-register">立即注册</a>
				<a :href="passwordUrl" class="help-password">忘记密码?&nbsp;|&nbsp;</a>
				<a :href="npopUrl" class="help-password">商家登录&nbsp;|&nbsp;</a>
			</div>
		</el-dialog>
    </div>
</template>
<script>
	import Http from 'http';
	import actions from 'actions';
	import router from '../../route.js';
	import apiConfig from '../../config/api.config.js';
	import { swiper, swiperSlide } from 'vue-awesome-swiper';
	import moment from 'moment';

	export default {
	name: 'login',
	data() {
		return {
			image: '',
			LoginVisible:false,
			swiperOption: {
				notNextTick: true,
				simulateTouch:false,
				autoplay: 5000,
				direction : 'vertical',
				grabCursor : true,
				setWrapperSize :true,
				autoHeight: true,
				mousewheelControl : true,
				observeParents:true,
				mousewheelReleaseOnEdges: true,
				// if you need use plugins in the swiper, you can config in here like this
				// 如果自行设计了插件，那么插件的一些配置相关参数，也应该出现在这个对象中，如下debugger
				debugger: true,
				// swiper callbacks
				// swiper的各种回调函数也可以出现在这个对象中，和swiper官方一样
				onTransitionStart(swiper){
					console.log(swiper);
				},
				onSlideChangeEnd(swiper) {
					if (swiper.activeIndex === 3) {
						let last = document.getElementById('theLastSlide');
						last.scrollTop = 0;
					}
				}
			},
			scroll:'',
			registUrl: '',
			passwordUrl: '',
			npopUrl: '',
			formData: {
				name: '',
				password: '',
				captcha: '',
			},
			nameError: '',
			passwordError: '',
			captchaError: '',
			rules: {
				name: [{
					validator: (rule, value, callback) => {
						if (value === '') {
							callback(new Error('用户名不能为空'));
						} else if (this.nameError !== '') {
							callback(new Error(this.nameError));
						} else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				password: [{
					validator: (rule, value, callback) => {
						if (value === '') {
							callback(new Error('密码不能为空'));
						} else if (this.passwordError !== '') {
							callback(new Error(this.passwordError));
						} else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				captcha: [{
					validator: (rule, value, callback) => {
						if (value === '') {
							callback(new Error('验证码不能为空'));
						} else if (this.captchaError !== '') {
							callback(new Error(this.captchaError));
						} else {
							callback();
						}
					},
					trigger: 'blur'
				}],
			}
		};
	},
	components: {
		swiper,
		swiperSlide
	},
	computed: {
		swiper() {
			return this.$refs.mySwiper.swiper;
		}
	},
	created() {
		this.getCaptcha();
		this.setUrl();
		window.addEventListener('keyup', this.enterLogin);
		window.addEventListener('scroll', this.getScroll);
	},
	mounted() {
		this.swiper.slideTo(0, 1000, false);
		let vm = this;
		let last = document.getElementById('theLastSlide');
		if (document.addEventListener) {
			last.addEventListener('scroll', function(e) {
				let ele = e.target;
				if (ele.scrollTop > 0) {
					vm.swiper.disableMousewheelControl();
				} else {
					vm.swiper.enableMousewheelControl();
				}
			});
		} else {
			last.attachEvent('onscroll', function() {
				if (last.scrollTop > 0) {
					vm.swiper.disableMousewheelControl();
				} else {
					vm.swiper.enableMousewheelControl();
				}
			});
		}
	},
	methods: {
		enterLogin(event) {
			if (event.code === 'Enter' || event.keyCode === 13) {
				this.login();
			}
		},
		login() {
			let vm = this;
			vm.nameError = '';
			vm.passwordError = '';
			vm.captchaError = '';
			vm.$refs.formData.validate(function(valid){
				if(valid){
					Http('api/login', {
							method: 'post',
							data: {
								name: vm.formData.name,
								password: vm.formData.password,
								captcha: vm.formData.captcha,
							}
						})
						.then((res) => {
							if (res.data.code === 409) {
								vm.captchaError = res.data.msg;
								vm.$refs.formData.validate();
								vm.getCaptcha();
							} else if (res.data.code != 200) {
								console.log('--login failure1--', res);
								vm.passwordError = '账号密码不正确';
								vm.$refs.formData.validate();
								vm.getCaptcha();
							} else if (res.data.iserror) {
								if (res.data.data.type === 'name') {
									vm.nameError = res.data.msg;
								} else if (res.data.data.type === 'password') {
									vm.passwordError = res.data.msg;
								}
								vm.$refs.formData.validate();
								vm.getCaptcha();
							} else {
								actions.setUserInfo(vm.$store, res.data.data);
								Http.defaults.headers.common['Authorization'] = store.state.userInfo.token;
								if (!res.data.data.isRegistered) {
									router.push({
										name: 'register'
									});
								} else if (res.data.data.isApproved === 0) {
									router.push({
										name: 'succeed'
									});
								} else if (res.data.data.isApproved === -1) {
									router.push({
										name: 'refused'
									});
								}  else if (res.data.data.isTest === 1 && moment().valueOf() > res.data.data.testEndTime) {
									router.push({
										name: 'testEnd'
									});
								} else {
									router.push({
										name: 'index'
									});
								}
							}
						}, (reject) => {
							console.log('--login failure2--', reject);
							vm.passwordError = '账号密码不正确';
							vm.getCaptcha();
							return;
						})
						.catch((err) => {
							console.log('--login failure3--', err);
							vm.passwordError = '账号密码不正确';
							return;
						});
				}
			});
		},
		getCaptcha() {
			Http('api/captcha', {
					method: 'get',
					params: {
						t: new Date().getTime(),
					}
				})
				.then((res) => {
					if (res.data.code != 200) {
						console.log('get captcha error');
						return;
					} else if (res.data.iserror) {
						console.log('get captcha failure2');
						return;
					} else {
						this.image = res.data.data.image;
					}
				}, (reject) => {
					console.log('--get captcha failure2--', reject);
					this.captchaError = '账号密码不正确';
						return;
				})
				.catch((err) => {
					console.log('--get captcha failure3--', err);
					this.captchaError = '账号密码不正确';
						return;
				});
		},
		setUrl() {

			if (apiConfig[process.env.NODE_ENV] == apiConfig.production) {
				this.registUrl = apiConfig.pro_regist;
				this.passwordUrl = apiConfig.pro_password;
				this.npopUrl = apiConfig.pro_npop;
			} else {
				this.registUrl = apiConfig.pre_regist;
				this.passwordUrl = apiConfig.pre_password;
				this.npopUrl = apiConfig.pre_npop;
			}

		},
		getScroll(){
		    var Nav = document.getElementById('login-header');
			this.scroll = document.documentElement.scrollTop || document.body.scrollTop;
			if(this.scroll>0){
				Nav.setAttribute('style', 'position: fixed;top: 0;width: 100%; z-index: 20;background-color: rgb(255, 255, 255, 0.9);-webkit-transition: top .5s; -moz-transition: top .5s; -o-transition: top .5s;transition: top .5s; -webkit-box-shadow: 0 2px 2px rgba(0,0,0,.1); -moz-box-shadow: 0 2px 2px rgba(0,0,0,.1);box-shadow: 0 2px 2px rgba(0,0,0,.1);');
			}else{
			    Nav.removeAttribute('style');
			}
		}

	}
};


</script>
<style scoped>
	.login-banner{overflow: hidden;}
</style>
