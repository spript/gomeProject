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
			<!--banner轮播-->
			<swiper-slide style="height:100%;">
				<!-- <div class="swiper-slide"> -->
					<div  class="login-scroll" id="login-scroll" style="height:100%;min-width: 1280px;">
						<div class="block" style="height:100%;">
							<el-carousel trigger="click" style="width:100%;height:100%;padding-bottom:30px;">
								<el-carousel-item v-for="(item, index) in bannerImg" style="height:100%;text-align: center">
									<template v-if="index === 0">
										<router-link :to="{name: 'second'}"><img :src="item" style="height:100%" ></router-link>
									</template>
									<template v-else><img :src="item" style="height:100%" ></template>
								</el-carousel-item>
							</el-carousel>
						</div>
					</div>
				<!-- </div> -->
			</swiper-slide>
			<!--通栏01-->
			<swiper-slide>
				<div class="login-banner">
					<div class="wrapper-half-red wrapper-half"></div>
					<div class="login-wrapper">
						<img src="../../assets/img/login/ping01.png" style="left:49%;">
					</div>
				</div>
			</swiper-slide>
			<!--通栏02-->
			<swiper-slide>
				<div class="login-banner">
					<div class="wrapper-half-green wrapper-half" ></div>
					<div class="login-wrapper">
						<img src="../../assets/img/login/ping02.png" style="left:50%;">
					</div>
				</div>
			</swiper-slide>
			<!--优势-->
			<swiper-slide>
				<div class="login-banner ">
					<div class="banner-wrapper" style="padding:5% 0;">
						<h3 class="banner-title">平台优势</h3>
						<ul class="banner-advantage">
							<li>
								<p><img src="../../assets/img/login/youshi-01.png"></p>
								<p class="banner-tip">分享返利</p>
								<p>分享即返利，裂变推</br>广全面提升传播广度</p>
							</li>
							<li>
								<p><img src="../../assets/img/login/youshi-02.png"></p>
								<p class="banner-tip">全链路整合</p>
								<p>媒体、电商、社交广告全覆</br>盖，短链模式提升转化效率</p>
							</li>
							<li>
								<p><img src="../../assets/img/login/youshi-03.png"></p>
								<p class="banner-tip">场景化体验</p>
								<p>社交原生广告无缝融</br>入用户内容消费环境</p>
							</li>
							<li>
								<p><img src="../../assets/img/login/youshi-04.png"></p>
								<p class="banner-tip">丰富资源</p>
								<p>多类效果广告模式，</br>轻松管理投放费用&nbsp;&nbsp;&nbsp;</p>
							</li>
						</ul>
						<h3 class="banner-title">服务模式</h3>
						<ul class="banner-service">
							<li>
								<div class="service-box01 service-box">
									<p class="banner-tip">返利广告</p>
									<p class="service-content">全新社交裂变推广模式</br>让广告创意飞速传播</p>
								</div>
							</li>
							<li>
								<div class="service-box02 service-box">
									<p class="banner-tip">效果广告</p>
									<p class="service-content">CPC计费，投放效率高</br>让营销活动效果更上层楼</p>
								</div>

							</li>
							<li>
								<div class="service-box03 service-box">
									<p class="banner-tip">合约广告</p>
									<p class="service-content">CPD/CPM计费，为您的</br>品牌产品急速提升曝光度</p>
								</div>

							</li>

						</ul>
					</div>
				</div>
			</swiper-slide>
			<!--流程-->
			<swiper-slide style="overflow-y:auto; overflow-x: hidden" id="theLastSlide">
				<div  class="login-banner ">
					<div class="banner-wrapper" style="padding:5% 0 0 0;">
						<h3 class="banner-title">入驻流程</h3>
						<ul class="banner-zhu">
							<li>
								<p><img src="../../assets/img/login/ruzhu-01.png"></p>
								<p class="zhu-des">使用国美账号登录</p>
							</li>
							<li>
								<i class="banner-arrow"></i>
							</li>
							<li>
								<p><img src="../../assets/img/login/ruzhu-02.png"></p>
								<p  class="zhu-des">选择身份</p>
							</li>
							<li>
								<i class="banner-arrow"></i>
							</li>
							<li>
								<p><img src="../../assets/img/login/ruzhu-03.png"></p>
								<p  class="zhu-des">上传信息等待审核</p>
							</li>
							<li>
								<i class="banner-arrow"></i>
							</li>
							<li>
								<p><img src="../../assets/img/login/ruzhu-04.png"></p>
								<p  class="zhu-des">成功入驻</p>
							</li>
						</ul>
						<h3 class="banner-title">投放流程</h3>
						<ul class="banner-tou">
							<li>
								<p><img src="../../assets/img/login/toufang-01.png"></p>
								<p  class="zhu-des">创建计划</p>
							</li>
							<li>
								<i class="banner-arrow"></i>
							</li>
							<li>
								<p><img src="../../assets/img/login/toufang-02.png"></p>
								<p  class="zhu-des">选择素材</p>
							</li>
							<li>
								<i class="banner-arrow"></i>
							</li>
							<li>
								<p><img src="../../assets/img/login/toufang-03.png"></p>
								<p  class="zhu-des">查看效果</p>
							</li>

						</ul>
						<p  class="banner-progress">
							<a href="" class="progress-btn"  @click.prevent="LoginVisible=true">立即入驻</a>
						</p>
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
		<el-dialog v-model="LoginVisible">
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
					<el-col :span="13">
						<el-input type="text" v-model="formData.captcha" placeholder="输入验证码"></el-input>
					</el-col>
					<el-col class="line" :span="1">&nbsp;</el-col>
					<el-col :span="10" style="{font-size: 0;}">
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
import banner01 from '../../assets/img/login/banner1.png';
import banner02 from '../../assets/img/login/banner2.png';
import { swiper, swiperSlide } from 'vue-awesome-swiper';
import moment from 'moment';

export default {
	name: 'login',
	data() {
		return {
			image: '',
			bannerImg:[banner01,banner02],
			LoginVisible:false,
			swiperOption: {
				notNextTick: true,
				simulateTouch:false,
				autoplay: 5000,
				direction : 'vertical',
				grabCursor : false,
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
				onSlideChangeEnd(swiper) {
					let last = document.getElementById('theLastSlide');
					if (swiper.activeIndex === 3) {
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
//		window.addEventListener('scroll', this.getScroll);
	},
	mounted() {
		// you can use current swiper instance object to do something(swiper methods)
		// 然后你就可以使用当前上下文内的swiper对象去做你想做的事了
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
			this.registUrl = $CONFIG['registUrl'];
			this.passwordUrl = $CONFIG['passwordUrl'];
			this.npopUrl = $CONFIG['npopUrl'];
		},
//		getScroll(){
//		    var Nav = document.getElementById('login-header');
//			this.scroll = document.documentElement.scrollTop || document.body.scrollTop;
//			if(this.scroll>0){
//				Nav.setAttribute('style', 'position: fixed;top: 0;width: 100%; z-index: 20;background-color: rgb(255, 255, 255, 0.9);-webkit-transition: top .5s; -moz-transition: top .5s; -o-transition: top .5s;transition: top .5s; -webkit-box-shadow: 0 2px 2px rgba(0,0,0,.1); -moz-box-shadow: 0 2px 2px rgba(0,0,0,.1);box-shadow: 0 2px 2px rgba(0,0,0,.1);');
//			}else{
//			    Nav.removeAttribute('style');
//			}
//		}

	}
};
</script>
<style>
/*.swiper-slide{
	padding-top: 70px;
}*/
</style>
