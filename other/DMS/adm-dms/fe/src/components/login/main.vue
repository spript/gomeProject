<template>
	<div>
		<div class="login-header"><span class="header-login"><a href="/">gomeplus</a></span></div>
		<div class="login-body">
			<div class="body-scaffold">
				<div class="scaffold-main">
					<div class="main-wrap">
						<h2>欢迎登录</h2>
						<el-form :model="formData" :rules="rules" ref="formData">
							<el-form-item label="" prop="name">
								<el-input type="text" v-model="formData.name" placeholder="输入用户名"></el-input>
							</el-form-item>
							<el-form-item prop="password">
								<el-input type="password" v-model="formData.password" placeholder="输入密码"></el-input>
							</el-form-item>
							<el-form-item prop="captcha">
								<el-col :span="11">
									<el-input type="text" v-model="formData.captcha" placeholder="输入验证码"></el-input>
								</el-col>
							    <el-col class="line" :span="1">&nbsp;</el-col>
								<el-col :span="12" style="{font-size: 0;}">
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
							<a href="https://passport.gomeplus.com/regist" class="help-register">立即注册</a>
							<a href="https://passport.gomeplus.com/forgetpwd" class="help-password">忘记密码?&nbsp;|&nbsp;</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="footer">
			<p>Copyright &#x24B8; 2016 – 2017 All Rights Reserved. 国美互联网生态(分享)科技公司版权所有</p>
		</div>
	</div>
</template>
<script>
import Http from 'http';
import actions from 'actions';
import router from '../../route.js';
export default {
	name: 'login',
	data() {
		return {
			image: '',
			token: '',
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
	created() {
		this.getCaptcha();
	},
	methods: {
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
								token: vm.token,
							}
						})
						.then((res) => {
							alert('hh');
							if (res.data.code === 409) {
								vm.captchaError = res.data.msg;
								alert(res.data.msg);
								vm.$refs.formData.validate();
							} else if (res.data.code != 200) {
							    console.log('--login failure1--', res);
								vm.passwordError = '账号密码不正确';
								vm.$refs.formData.validate();
							} else if (res.data.iserror) {
								if (res.data.data.type === 'name') {
									vm.nameError = res.data.msg;
								} else if (res.data.data.type === 'password') {
									vm.passwordError = res.data.msg;
								}
								vm.$refs.formData.validate();
							} else {
								actions.setAccountInfo(vm.$store, res.data.data);
								if (!res.data.data.isRegistered) {
									router.push({
										name: 'register'
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
				})
				.then((res) => {
					if (res.data.code != 200) {
						console.log('get captcha error');
						return;
					} else if (res.data.iserror) {
						console.log('get captcha failure2');
						return;
					} else {
						this.token = res.data.data.token;
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
		}
	}
};
</script>
