<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
			<el-form-item label="模板名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="广告位尺寸：">
				<el-col :span='15'>
					<el-form-item prop="width">
						<span>宽&nbsp;&nbsp;</span>
						<el-input v-model="formData.width" style='width:200px;'></el-input>
						<span>&nbsp;&nbsp;px</span>
					</el-form-item>
				</el-col>
				<el-col :span='15' class='mrt-20'>
					<el-form-item prop="height">
						<span>高&nbsp;&nbsp;</span>
						<el-input v-model="formData.height" style='width:200px;'></el-input>
						<span>&nbsp;&nbsp;px</span>
					</el-form-item>
				</el-col>
			</el-form-item>
			<el-form-item label="素材个数：" prop="materialNumber">
				<el-input v-model.number="formData.materialNumber"></el-input>
			</el-form-item>
			<el-form-item label="代码类型">
				<el-radio-group v-model="formData.type">
					<el-radio :label="1">JS</el-radio>
					<el-radio :label="2">HTML</el-radio>
					<el-radio :label="3">JSON</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="模板代码：" prop="content">
				<el-input type="textarea" v-model="formData.content" :rows="15"></el-input>
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
	name: 'app-resource-template',
	data() {
		return {
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
				height: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						} else if(value === '') {
							callback(new Error('请输入高度'));
						} else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				width: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						} else if(value === '') {
							callback(new Error('请输入宽度'));
						}  else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				materialNumber: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请输入素材的个数'));
						} else if (objType(value) !== 'Number') {
							callback(new Error('只能为数字'));
						} else if (value < 0) {
							callback(new Error('不能为负数'));
						} else {
							callback();
						}
					},
					trigger: 'blur'
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
		console.log(this.formData);
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			this.$refs.formData.validate((result) => {
				let params = {
					"name": this.formData.name,
				    "width": this.formData.width,
				    "height": this.formData.height,
				    "materialNumber": this.formData.materialNumber,
				    "type": this.formData.type,
				    "content": this.formData.content
				};
				console.log(params);
				if (result) {
					http({
						url: '/api/template',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							templateId: this.formData.templateId
						}, params) : params
					})
					.then((res) => {
						console.log(res);
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