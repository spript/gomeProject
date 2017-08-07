<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="showView"></div>
		</transition>
		<transition name="drawer">
			<div class="amp-drawer" v-if="showView">
				<div class="drawer-part">
					<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">查看问卷</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<div class="dialog-tab">
						<el-tabs v-model="activeName" type="card" @tab-click="handleClick">
							<el-tab-pane label="问卷详情" name="first"><m-detail :surveyId="surveyId"></m-detail></el-tab-pane>
							<el-tab-pane label="审核规范" name="second"></el-tab-pane>
						</el-tabs>
					</div>
				</div>
				<div class="drawer-btn">
					<span class="btn btn-primary" v-if="approveStatus !== 1" @click="passDialogHandler">通过</span>
					<span class="btn btn-gray" v-if="approveStatus === 0" @click="viewRefuseHandler">拒绝</span>
					<span class="btn btn-gray" @click="closeView">取消</span>
				</div>
			</div>
		</transition>
	</div>
</template>
<script>
import Event from 'event';
import Detail from './surveyPriview.vue';
export default {
	name: 'survey-view-dialog',
	data() {
		return {
			activeName: 'first'
		};
	},
	props: ['showView', 'surveyId', 'approveStatus'],
	components: {
		'm-detail': Detail
	},
	methods: {
		handleClick(tab, event) {
			console.log(tab, event);
		},
		closeView() {
			Event.$emit('close-view');
		},
		passDialogHandler() {
			Event.$emit('show-dialog');
		},
		viewRefuseHandler() {
			Event.$emit('show-refuse');
		}
    }
};
</script>
<style scoped>
.amp-drawer{
	padding-bottom: 80px;
}
.amp-drawer .drawer-btn{
	height: 80px;
}
.amp-drawer .drawer-btn:before{
	height: 80px;
}
.dialog-tab{
	padding: 0 20px;
}
.btn{
	padding: 0 15px;
	height: 30px;
	line-height: 30px;
	font-size: 14px;
}
</style>