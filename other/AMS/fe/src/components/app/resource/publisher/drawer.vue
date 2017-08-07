<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="config.show" @click="closeView"></div>
		</transition>
		<transition name="drawer">
			<div class="amp-drawer" v-if="config.show">
				<div class="drawer-part">
					<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">{{actionMap[config.action] + typeMap[config.type]}}</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<m-media v-if="config.show && config.type === 'media'"></m-media>
					<m-channel v-if="config.show && config.type === 'channel'"></m-channel>
					<m-advertising v-if="config.show && config.type === 'advertisingList'"></m-advertising>
					<m-advert v-if="config.show && config.type === 'advert'"></m-advert>
					<m-advertisingChange v-if="config.show && config.type === 'advertisingChange'"></m-advertisingChange>
					<m-advertFormChange v-if="config.show && config.type === 'advertFormChange'"></m-advertFormChange>
					<m-advertChange v-if="config.show && config.type === 'advertChange'"></m-advertChange>
					<m-template v-if="config.show && config.type === 'template'"></m-template>
					<m-format v-if="config.show && config.type === 'format'"></m-format>
					<m-advertisingOut v-if="config.show && config.type === 'advertisingOut'"></m-advertisingOut>
				</div>
				<div class="drawer-btn">
					<button style="height: 40px;padding: 0px 44px;" class="btn media-btn-primary" @click="save()">保存</button>
					<button @click="closeView()" class="btn btn-gray">取消</button>
				</div>
			</div>
		</transition>
	</div>
</template>

<script>
import store from 'store';
import http from 'http';
import actions from 'actions';
import newMedia from './newMedia.vue';
import newChannel from '../channel/drawer-channel.vue';
import newAdvertising from '../advertisingList/drawer-advertising.vue';
import newAdvert from '../advert/drawer-advert.vue';
import newAdvertisingChange from '../advertisingList/drawer-changeStatus.vue';
import newAdvertFormChange from '../advertForm/drawer-changeStatus.vue';
import newAdvertChange from '../advert/drawer-changeStatus.vue';
import newTemplate from '../template/drawer-template.vue';
import newFormat from '../advertForm/drawer-form.vue';
import newAdvertisingOut from '../advertising/drawer-advertising-out.vue';
import Event from 'event';
export default {
	name: 'newMedia',
	data() {
		return {
			typeMap: {
				media: '媒体',
				channel: '频道',
				advertisingList: '广告位',
				advertisingOut: '广告位',
				advert: '广告',
				advertisingChange: '切换',
				advertChange: '切换',
				advertFormChange: '切换',
				template: '模板',
				format: '广告形式'
			},
			actionMap: {
				new: '新增',
				modify: '修改',
				change: '状态'
			}
		};
	},
	components: {
		'm-media': newMedia,
		'm-channel': newChannel,
		'm-advertising': newAdvertising,
		'm-advert': newAdvert,
		'm-advertisingChange': newAdvertisingChange,
		'm-advertFormChange': newAdvertFormChange,
		'm-advertChange': newAdvertChange,
		'm-template': newTemplate,
		'm-format': newFormat,
		'm-advertisingOut': newAdvertisingOut
	},
	computed: {
		config: () => store.state.drawerCtrl
	},
	methods: {
		closeView() {
			actions.controlDrawer(this.$store, {
				show: false
			});
		},
		save(cb) {
			Event.$off('media-save-result');
			Event.$once('media-save-result', (argv) => {
				console.log('return--------------------');
				if (argv.error) {
					return;
				}
				Event.$emit('media-save-success');
				if (cb && objType(cb) === 'Function') {
					cb(argv);
				} else {
					console.log('success***********************');
					actions.controlDrawer(store, {
						show: false
					});
				}
				this.$message({
					message: '保存成功',
					type: 'success',
					duration: 3000
				});
			});
			Event.$emit('media-save');
		}
	}
};
</script>