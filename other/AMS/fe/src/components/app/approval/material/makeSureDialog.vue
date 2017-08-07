<template>
	<div>
		<transition name="fade">
			<div style="z-index:1300;" class="master" v-if="showDialog"></div>
		</transition>
		<transition name="fade">
			<div class="dialog-pop" v-if="showDialog">
				<div class="pop-header">
					<span class="header-text">素材审核</span>
					<span class="header-close" @click="closeDialogHandler"><em class="icon icon-close"></em></span>
				</div>
				<div class="pop-body">
					<div class="body-infor">
						<h3 v-if="passType === 'single'">确定要通过该素材吗？</h3>
						<h3 v-else>是否批量通过已选中的素材？</h3>
					</div>
				</div>
				<div class="pop-footer">
					<span class="btn btn-primary" @click="materialPassAction">确认</span>
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
	name: 'app-material-dialog',
	data() {
		return {
			msg: ''
		};
	},
	props: ['showDialog', 'materialIds', 'passType'],
	methods: {
		closeDialogHandler() {
			Event.$emit('close-dialog');
		},
		materialPassAction() {
			let vm = this;
			Http.put('/api/approval/materials/pass', {
				materialIds: vm.materialIds
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					Event.$emit('pass-material');
				}
			}).catch((error) => {
				console.log(error);
			});
		}
	}

};
</script>
<style></style>