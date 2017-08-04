<template>
	<div class="amp-content">
		<div v-if="config.isSucceed === false" class="open_account">
			<div class="account-step">
				<div class="part-guide" :class="'part-guide0' + (orderKey.indexOf(config.step) + 1)">
					<span class="guide-step guide-step01"><b class="step-text">选择身份</b><b class="step-number">1</b><b class="step-icon"><em class="icon icon-right"></em></b></span>
					<span class="guide-step guide-step02"><b class="step-text">填写公司信息</b><b class="step-number">2</b><b class="step-icon"><em class="icon icon-right"></em></b></span>
					<span class="guide-step guide-step03"><b class="step-text">指定联系人</b><b class="step-number">3</b><b class="step-icon"><em class="icon icon-right"></em></b></span>
					<span class="guide-step guide-step04"><b class="step-text">资料确认</b><b class="step-number">4</b><b class="step-icon"><em class="icon icon-right"></em></b></span><span class="guide-percent"></span>
				</div>
			</div>
			<div class="account_content">
				<transition name="component-fade" mode="out-in">
					<keep-alive>
						<component v-bind:is="config.step"></component>
					</keep-alive>
				</transition>
			</div>
		</div>
		<div v-if="config.isSucceed" class="open_account">
			<div class="account-icon"><em class="icon icon-confirm"></em>
				<h1>申请资料已成功提交!</h1>
				<h4>信息进入审核序列，请您耐心等待。结果将以邮件形式通知!</h4>
			</div>
		</div>
		<div class="footer">
			<p>Copyright &#x24B8; 2016 – 2017 All Rights Reserved. 国美互联网生态(分享)科技公司版权所有</p>
		</div>
	</div>
</template>
<script>
import store from 'store';
import RegisterIdentity from './register-identity.vue';
import RegisterCompany from './register-company.vue';
import RegisterContact from './register-contact.vue';
import RegisterConfirm from './register-confirm.vue';
export default {
	name: 'register',
	data() {
		return {
			orderKey: ['identity', 'company', 'contact','confirm']
		};
	},
	components: {
		'identity': RegisterIdentity,
		'company': RegisterCompany,
		'contact': RegisterContact,
		'confirm': RegisterConfirm
	},
	computed:{
	    config:() => store.state.anicerControl,
	    userInfo: () => store.state.userInfo
	},
	created() {
		console.log(this.userInfo);
		if (this.userInfo.isRegistered && this.userInfo.isApproved) {
			this.$router.push({name: 'index'});
		} else if (this.userInfo.isRegistered && !this.userInfo.isApproved) {
			this.$router.push({name: 'succeed'});
		}
	}
};
</script>
