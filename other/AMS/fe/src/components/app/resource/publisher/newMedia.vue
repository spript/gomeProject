<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
			<el-form-item label="媒体名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="设备类型：">
				<el-radio-group v-model="formData.platform">
					<el-radio :label="1" :disabled ='drawerCtrlAction=="modify"'>APP</el-radio>
					<el-radio :label="2" :disabled ='drawerCtrlAction=="modify"'>WAP</el-radio>
					<el-radio :label="3" :disabled ='drawerCtrlAction=="modify"'>PC</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="媒体属性：">
				<el-radio-group v-model="formData.isInternal">
					<el-radio :label="1" :disabled ='drawerCtrlAction=="modify"'>站内</el-radio>
					<el-radio :label="0" :disabled ='drawerCtrlAction=="modify"'>站外</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="描述：" prop="description">
				<el-input type="textarea" v-model="formData.description"></el-input>
			</el-form-item>
			<el-form-item label="页面URL：" :prop="changeUrl ? 'url' : 'page'">
				<el-input :disabled="formData.platform === 1 || drawerCtrlAction=='modify'" v-model="formData.url"></el-input>
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
//		console.log(this.drawerData);
//		console.log(this.config);
	},
	methods: {
		
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			this.$refs.formData.validate((result) => {
				console.log(result);
				let params = {
					publisherId: this.formData.publisherId,
					name: this.formData.name,
					platform: this.formData.platform,
					isInternal: this.formData.isInternal,
					description: this.formData.description,
					url: this.formData.url
				};
				console.log(params);
				if (result) {
					http({
						url: '/api/publisher',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							publisherId: this.formData.publisherId
						}, params) : params
					})
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('media-save-result', {
								error: 0,
								res: res.data,
								isRebate: this.formData.isRebate
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
	},
	watch: {
		'formData.platform' : function() {
			if (this.formData.platform === 1) {
				this.changeUrl = false;
				this.formData.url = '';
			} else {
				this.changeUrl = true;
			}
		}
	}
};
</script>