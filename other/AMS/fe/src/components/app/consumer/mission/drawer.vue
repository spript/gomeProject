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
					<m-mission v-if="config.show && config.type === 'mission'"></m-mission>
				</div>
				<div class="drawer-btn" v-show='config.action!="look"'>
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
import Event from 'event';
import newMission from './newMission.vue';
export default {
	name: 'newMedia',
	data() {
		return {
			typeMap: {
				mission: '任务'
			},
			actionMap: {
				new: '新建',
				modify: '修改',
				copy: '复制',
				look: '查看'
			}
		};
	},
	components: {
		'm-mission': newMission,
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
			Event.$off('mission-save-result');
			Event.$once('mission-save-result', (argv) => {
				if (argv.error) {
					return;
				}
				Event.$emit('mission-save-success');
				if (cb && objType(cb) === 'Function') {
					cb(argv);
				} else {
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
			Event.$emit('mission-save');
		}
	}
};
</script>