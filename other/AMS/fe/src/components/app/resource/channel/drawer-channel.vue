<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
			<el-form-item label="频道名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="媒体：">
				<span>{{formData.media.name}}</span>
			</el-form-item>
			<el-form-item label="父频道：" prop='parentName'>
				<el-select v-model="formData.parentName" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in parentName.options" :label="item.name" :value="item.channelId">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="描述：" prop="description">
				<el-input type="textarea" v-model="formData.description"></el-input>
			</el-form-item>
			<el-form-item label="页面URL：" :prop="formData.media.platform === 'APP' ? '' : 'url'">
				<el-input :disabled="formData.media.platform === 'APP' || drawerCtrlAction=='modify'" v-model="formData.url"></el-input>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
import Event from 'event';
import actions from 'actions';
import store from 'store';
import http from 'http';
import {
	objType,
	mixin,
	copyObj
} from 'utils/common';
export default {
	name: "drawer-media",
	data() {
		return {
			changeUrl: false,
			formData: {},
			parentName: {
				options: [{
					name: "无",
					channelId: 0
				}]
			},
			rules: {
				name: [{
					required: true,
					message: '请输入媒体名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				description: [{
					max: 200,
					message: '最大长度不超过200个字符',
					trigger: 'blur'
				}],
				url: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请填写页面URL'));
						} else if (!/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/.test(value)) {
							callback(new Error('URL格式不对！'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				parentName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择父频道'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}]
			},
			httpMethodMap: {
				new: 'post',
				modify: 'put'
			}
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config,
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		config: () => store.state.drawerCtrl
	},
	created() {
		this.formData = copyObj(this.drawerData);
		this.channelList(this.formData.publisherId);
		if (this.formData.parentName == '' && this.drawerCtrlAction == 'modify') {
			this.formData.parentName = '无';
		}
		console.log(this.formData.parentName);
	},
	methods: {
		channelList(id) {
			console.log(id);
			var vm = this;
			http.get("/api/allChannels", {
				params: {
					publisherId: id
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					var data = res.data.data;
					for (var i in data) {
						vm.parentName.options.push(data[i]);
					}
				}
			}).catch((Error) => {
				console.log(Error);
			});
		}
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			this.$refs.formData.validate((result) => {
				let params = {
					name: this.formData.name,
					publisherId: this.formData.publisherId,
					parentId: this.formData.parentName == '无' ? 0 : this.formData.parentName,
					description: this.formData.description,
					url: this.formData.url
				};
				console.log(params);
				if (result) {
					http({
						url: '/api/channel',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							channelId: this.formData.channelId
						}, params) : params
					})
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('media-save-result', {
								error: 0,
								res: res.data
							});
						} else {
							Event.$emit('media-save-result', {
								error: 1,
								res: null
							});
						}
					})
					.catch((e) => {
						Event.$emit('media-save-result', {
							error: 1,
							res: null
						});
					});
				} else {
					Event.$emit('media-save-result', {
						error: 1,
						res: null
					});
				}
			});
		});
	}
};
</script>