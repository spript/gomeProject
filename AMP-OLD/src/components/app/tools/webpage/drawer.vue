<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="config.show" @click="closeDrawer()"></div>
		</transition>
		<transition name="drawer">
			<div class="amp-drawer" v-if="config.show">
				<div class="drawer-part">
					<h2 class="part-header"><span class="header-text">{{actionMap[config.action] + typeMap[config.type]}}</span><span class="header-fn"><em @click="closeDrawer()" class="icon icon-close"></em></span></h2>
					<div class="part-guide" :class="'part-guide0' + (orderKey.indexOf(config.type) + 1)" >
						<span class="guide-step guide-step01">
							<b class="step-text">选择模板</b>
							<b class="step-number">1</b>
							<b class="step-icon">
								<em class="icon icon-right"></em>
							</b>
						</span>
						<span class="guide-step guide-step02">
							<b class="step-text">编辑页面</b>
							<b class="step-number">2</b>
							<b class="step-icon">
								<em class="icon icon-right"></em>
							</b>
						</span>
					</div>
					<!-- <transition-group name="drawer-inner"> -->
						<m-newpage key="1" v-if="config.show && config.type === 'template'"></m-newpage>
						<m-editpage key="2" v-if="config.show && config.type === 'edit'"></m-editpage>
					<!-- </transition-group> -->
				</div>
				<div class="drawer-btn">
					<button class="btn btn-primary" v-show="config.type === 'template'" @click="next()">下一步</button>
					<button class="btn btn-gray" v-show="config.action === 'new' && config.type === 'edit'" @click="getDraft()">上一步</button>
					<button class="btn btn-primary" v-show="config.type === 'edit'" @click="saveDraft()">保存草稿</button>
					<button class="btn btn-primary" v-show="config.type === 'edit'" @click="publish()">发布</button>
					<button class="btn btn-primary" v-show="config.type === 'edit'" @click="preview()">预览</button>
					<button @click="closeDrawer()" class="btn btn-gray">取消</button>
				</div>
			</div>
		</transition>
		<m-preview v-if="showPreview" :pageId="pageId" :showPreview="showPreview"></m-preview>
	</div>
</template>
<script>
/*
 * 抽屉调用方法：调用 actions 的 controlDrawer 方法
 * 传入一个对象
 * 具体可看 vuex/state/drawer.js 列出的 key
 */
import store from 'store';
import actions from 'actions';
import Event from 'event';
import Http from 'http';
import {
	objType,
	mixin,
	copyObj
} from 'utils/common';

import CONST from '../../../../help/CONST.js';

import DrawerTemplate from './drawer-template.vue';
import DrawerEdit from './drawer-edit.vue';
import pagePreview from './pagePreview.vue';
export default {
	name: 'app-page-new',
	props: ['pageId'],
	data() {
		return {
			typeMap: {
				template: '选择模板',
				edit: '编辑页面'
			},
			orderKey: ['template', 'edit'],
			orderMsg: ['选择模板', '编辑页面'],
			actionMap: {
				new: '新建',
				modify: '修改'
			},
			showPreview: false,
			con : CONST.DRAWERPAGE
		};
	},
	computed: {
		config: () => store.state.drawerCtrl
	},
	components: {
		'm-newpage': DrawerTemplate,
		'm-editpage': DrawerEdit,
		'm-preview': pagePreview
	},
	mounted() {
		let vm = this;
		Event.$on('close-preview', () => {
			vm.showPreview = false;
		});
	},
	methods: {
		closeDrawer() {
			actions.controlDrawer(this.$store, {
				show: false,
				config:{}
			});
			Event.$emit('get-list');
		},
		saveDraft() {
			// 将要保存
			Event.$emit('page-save',{pId:this.pageId});
			Event.$off('page-save-result');
			Event.$once('page-save-result', (argv) => {
				if (argv.error) {
					return;
				}
				actions.controlDrawer(store, {
					show: false
				});
				this.$message({
					message: '保存成功',
					type: 'success',
					duration: 3000
				});
				Event.$emit('get-list');
				Event.$emit('clear-pageId');
			});
		},
		publish() {
			Event.$emit('page-save' ,{pId:this.pageId,publish:'publish'});
			Event.$off('page-save-result');
			Event.$once('page-save-result', (argv) => {
				if (argv.error) {
					return;
				}
				this.pageId = argv.res.data.pageId;
				Event.$emit('page-publish', argv);
				Event.$off('page-publish-result');
				Event.$once('page-publish-result', (argv) => {
					if (argv.error) {
						this.$message({
							message: argv.res.msg,
							type: 'error',
							customClass: "message-mod"
						});
						return;
					}
					actions.controlDrawer(store, {
						show: false
					});
					this.$message({
						message: '发布成功',
						type: 'success',
						duration: 3000
					});
					Event.$emit('get-list');
					Event.$emit('clear-pageId');
				});
			});
		},
		preview() {

			Event.$emit('page-save',{pId:this.pageId});
			Event.$off('page-save-result');
			Event.$once('page-save-result', (argv) => {
				if (argv.error) {
					return;
				}
				if(argv.res.pageTemplateId == 3 && argv.res.videoId !== '0'){
					Http.get("/api/video/detail",{
						hideLoading: true,
						params: {
							videoId:argv.res.videoId,
						}
					})
						.then((res) => {
							if (res.data.code === 200 && res.data.data.convertStatus == -1) { //失败
								this.$message({
									message: '视频转码失败，请重新上传!',
									type: 'error'
								});
								setTimeout(()=>{
									this.pageId = argv.res.data.pageId;
									this.showPreview = true;
								},3000)
							}else{
								this.pageId = argv.res.data.pageId;
								this.showPreview = true;
							}
						})
						.catch(function (error) {

						});
				}else{
					this.pageId = argv.res.data.pageId;
					this.showPreview = true;
				}
			});
		},
		next() {
			Event.$off('template-save-result');
			Event.$once('template-save-result', (data) => {
				let index = this.orderKey.indexOf(this.config.type);
				actions.controlDrawer(this.$store, {
					show: true,
					type: this.orderKey[index + 1],
					action: this.config.action,
					config: mixin(data,CONST.DRAWERPAGE)
				});
			});
			Event.$emit('template-save');
		},
		getDraft() {
			this.con = CONST.DRAWERPAGE;
			let index = this.orderKey.indexOf(this.config.type);
			actions.controlDrawer(this.$store, {
				show: true,
				type: this.orderKey[index - 1],
				action: this.config.action,
				config: {}
			});
		}
	}
};
</script>
<style scoped>
.amp-drawer .drawer-part .part-guide{width: 225px;}
.amp-drawer .drawer-part .part-guide .guide-step02 {
	left: 100%;
}
</style>
