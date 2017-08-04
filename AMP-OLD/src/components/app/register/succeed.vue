<template>
	<div>
		<div class="error-body">
			<div class="body-scaffold succeed-main"></div>
		</div>
		<div class="error-footer">
			<p class="error-title">申请资料已成功提交</p>
			<p class="error-detail">您的申请信息已经进入审核序列，后台审核员会在三个工作日内对您的申请进行审核，请您耐心等待，审核结果将以邮件形式通知您!</p>
		</div>
	</div>
</template>
<script>
import store from 'store';
export default {
	name: 'app-register-succeed',
	computed:{
	    userInfo: () => store.state.userInfo
	},
	created() {
		if (this.userInfo.isRegistered && this.userInfo.isApproved) {
			this.$router.push({name: 'index'});
		}
	}
};
</script>
<style scoped>
.body-scaffold {
    width: 350px;
    margin: 50px auto 0;
}
.error-detail {
    font-size: 14px;
    line-height: 30px;
    width: 560px;
    text-align: left;
    margin: 0 auto;
}
</style>