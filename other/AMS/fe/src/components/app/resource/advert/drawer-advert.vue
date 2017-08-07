<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="166px">
			<el-form-item label="广告位：">
				<span>{{formData.summary.advertisingName}}</span>
			</el-form-item>
			<el-form-item label="广告名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="计费模式：" prop='saleMode'>
				<el-select v-model="formData.saleMode" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in saleMode.options" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="广告形式：" prop='formatName'>
				<el-select v-model="formData.formatName" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in formatName.options" :label="item.name" :value="item.formatId">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="推广类型：" prop='generalizeType'>
				<el-select v-model="formData.generalizeType" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in generalizeType.options" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="模板：" prop='templateName'>
				<el-select v-model="formData.templateName" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in templateName.options" :label="item.name" :value="item.templateId">
					</el-option>
				</el-select>
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
	name: 'app-resource-advert-drawerAdvert',
	data() {
		return {
			formData: {},
			rules: {
				name: [{
					required: true,
					message: '请输入广告位名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				saleMode: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择计费模式'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				formatName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择广告形式'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				generalizeType: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择推广形式'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				templateName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择模板'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}]
			},
			saleMode: {
				options: [{
						label: "CPD",
						value: 5
					}, {
						label: "定价CPC",
						value: 1
					}, {
						label: "竞价CPC",
						value: 0
					}, {
						label: "定价CPM(合约)",
						value: 3
					}, {
						label: "定价CPM(非合约)",
						value: 4
					}, {
						label: "竞价CPM",
						value: 2
					}, {
						label: "CPS",
						value: 6
					}]
			},
			formatName: {
				options: []
			},
			generalizeType: {
				options: [{
					label: "商品广告",
					value: 0
				}, {
					label: "活动广告",
					value: 1
				}, {
					label: "店铺广告",
					value: 2
				}]
			},
			templateName: {
				options: []
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
		this.getTemplateList();
		this.formatList();
		if (this.drawerCtrlAction == 'modify') {
			this.formData.formatName = this.formData.formatId;
			this.formData.templateName = this.formData.templateId;
		}
	},
	methods: {
		getTemplateList() {
			var vm = this;
			http.get("/api/templates", {
				params: {
					status: 1
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					var data = res.data.data;
					for (var i in data.list) {
						vm.templateName.options.push(data.list[i]);
					}
				}
			}).catch((Error) => {
				console.log(Error);
			});
		},
		formatList() {
			var vm = this;
			http.get("/api/formats", {
				params: {
					status: 1
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					var data = res.data.data;
					for (var i in data.list) {
						vm.formatName.options.push(data.list[i]);
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
					"name": this.formData.name,
				    "slotId": this.formData.slotId,
				    "saleMode": this.formData.saleMode,
				    "formatId": this.formData.formatName,
				    "generalizeType": this.formData.generalizeType,
				    "templateId": this.formData.templateName
				};
				console.log(params);
				if (result) {
					http({
						url: '/api/advertisement',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							advertisementId: this.formData.advertisementId
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
