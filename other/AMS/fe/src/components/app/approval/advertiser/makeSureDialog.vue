<template>
	<div>
		<transition name="fade">
			<div style="z-index:1300;" class="master" v-if="showDialog"></div>
		</transition>
		<transition name="fade">
			<div class="dialog-pop" v-if="showDialog">
				<div class="pop-header">
					<span class="header-text">审核确认</span>
					<span class="header-close" @click="closeDialogHandler"><em class="icon icon-close"></em></span>
				</div>
				<div class="pop-body">
					<div class="body-infor">
						<h3 >是否确认该广告主通过资质审核？</h3>
					</div>
				</div>
				<div class="pop-footer">
					<span class="btn btn-primary" @click="advertiserPassAction">确认</span>
					<span class="btn btn-gray" @click="closeDialogHandler">取消</span>
				</div>
			</div>
		</transition>
	</div>
</template>
<script>
import Event from 'event';
import Http from 'utils/http.js';
export default {
	name: 'app-advertiser-dialog',
	data() {
		return {
			msg: ''
		};
	},
	props: ['showDialog', 'advertiserId', 'passType'],
	methods: {
		closeDialogHandler() {
			Event.$emit('close-dialog');
		},
		advertiserPassAction() {
			let vm = this;
			Http.put('/api/approval/advertiser/pass', {
				advertiserId: vm.advertiserId
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					Event.$emit('pass-advertiser');
				}
			}).catch((error) => {
				console.log(error);
			});
		}
	}

};
</script>
<style></style>
