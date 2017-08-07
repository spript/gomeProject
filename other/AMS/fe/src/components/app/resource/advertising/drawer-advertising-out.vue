<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="166px">
			<el-form-item label="广告位名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="媒体：" prop='mediaName'>
				<el-select v-show='drawerCtrlAction!="modify"' v-model="formData.mediaName" @change='mediaChange' style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in mediaName.options" :label="item.name" :value="item.publisherId">
					</el-option>
				</el-select>
				<span v-show='drawerCtrlAction=="modify"'>{{formData.publisherName}}</span>
			</el-form-item>
			<el-form-item label="一级频道：" prop='channelName'>
				<el-select v-show='drawerCtrlAction!="modify"' v-model="formData.channelName" @change='channelChange' style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in channelName.options" :label="item.name" :value="item.channelId">
					</el-option>
				</el-select>
				<span v-show='drawerCtrlAction=="modify"'>{{formData.parentChannelName?formData.parentChannelName:formData.channelName}}</span>
			</el-form-item>
			<el-form-item label="二级频道：" :prop='jugeTwoChannel?"page":"twoChannelName"'>
				<el-select v-show='drawerCtrlAction!="modify"' v-model="formData.twoChannelName" style="width:152px;display:inline-block;" placeholder="请选择">
					<el-option v-for="item in twoChannelName.options" :label="item.name" :value="item.channelId">
					</el-option>
				</el-select>
				<span v-show='drawerCtrlAction=="modify"'>{{formData.parentChannelName?formData.channelName:'--'}}</span>
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
			<el-form-item label="轮播数：" prop="rotators">
				<el-input v-model.number="formData.rotators"></el-input>
			</el-form-item>
			<el-form-item label="外采流量：">
				<el-radio-group v-model="formData.outSourceFlow">
					<el-radio :label="0">否</el-radio>
					<el-radio :label="1">是</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="单次请求返回素材数量：" prop="materialNumber">
				<el-input v-model.number="formData.materialNumber" placeholder="1-20"></el-input>
			</el-form-item>
			<el-form-item label="素材多级审核：">
				<el-radio-group v-model="formData.approveFlowId" @change="approvePhase">
					<el-radio :label="1">否</el-radio>
					<el-radio :label="2">是</el-radio>
				</el-radio-group>
				<br>
				<el-select v-model="approvePhaseName" style="width:170px;display:inline-block;" placeholder="请选择" :disabled="!isApprovePhaseOpened">
					<el-option v-for="item in approveOptions" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="描述：" prop="description">
				<el-input type="textarea" v-model="formData.description"></el-input>
			</el-form-item>
			<!--<el-form-item label="所属部门：" v-show='drawerCtrlAction != "modify"'>
				<div class="advertising-tree clearfix">
					<div class="fl">
						<span>待选部门</span>
						<el-tree :data="department" :props="defaultProps" @node-click="selectDepartment" class="w200"></el-tree>
					</div>
					<div class="fl mrl-30">
						<span>已选部门</span>
						<div class="advertising-selected">
							{{formData.department}}
						</div>
					</div>
				</div>
			</el-form-item>-->
			<!--{{jugeTwoChannel?"page":"twoChannelName"}}-->
			<el-form-item label="状态" v-show='drawerCtrlAction != "modify"'>
				<el-radio-group v-model="formData.state">
					<el-radio :label="0">下线</el-radio>
					<el-radio :label="1" :disabled="formData.controlRadio">售卖</el-radio>
					<el-radio :label="2" :disabled="formData.controlRadio">非售卖</el-radio>
				</el-radio-group>
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
	name: 'app-resource-advertising-drawerAdvertising',
	data() {
		return {
			formData: {},
			isApprovePhaseOpened:false,
			approveOptions: [{
				value: 1,
				label: '平台-广告二级审核'
			}],
			approvePhaseName:'',
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
				height: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						}else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				width: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						}else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				rotators: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请输入轮播的次数'));
						} else if (objType(value) !== 'Number') {
							callback(new Error('只能为数字'));
						} else if (value < 0) {
							callback(new Error('不能为负数'));
						} else if (value > 10) {
							callback(new Error('轮播数不能大于10'));
						}  else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				materialNumber: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请输入单次返回素材的数量'));
						} else if (objType(value) !== 'Number') {
							callback(new Error('只能为数字'));
						} else if (value < 1 || value > 20) {
							callback(new Error('只能输入1-20的数字'));
						} else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				description: [{
					max: 200,
					message: '最大长度不超过200个字符',
					trigger: 'blur'
				}],
				mediaName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择媒体'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				channelName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择一级频道'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				twoChannelName: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请选择二级频道'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}]
			},
			department: [{
	          label: '一级部门A',
	          children: [{
	            label: '二级部门A',
	          }, {
	          	label: '二级部门B'
	          }, {
	          	label: '二级部门C'
	          }]
	        }, {
	          label: '一级部门B',
	          children: [{
	            label: '二级部门D',
	          }, {
	            label: '二级部门E',
	          }]
	        }],
	        defaultProps: {
	          children: 'children',
	          label: 'label'
	        },
	        httpMethodMap: {
				new: 'post',
				modify: 'put'
			},
			mediaName: {
				options: []
			},
			channelName: {
				options: []
			},
			twoChannelName: {
				options: []
			},
			jugeTwoChannel: true
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config,
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		config: () => store.state.drawerCtrl,
	},
	created() {
		this.formData = copyObj(this.drawerData);
		this.isApprovePhaseOpened = this.formData.approveFlowId === 2;
		this.mediaList();
		if (this.isApprovePhaseOpened) {
				this.approvePhaseName = 1;
		}
	},
	methods: {
		approvePhase:function() {
			this.isApprovePhaseOpened = !this.isApprovePhaseOpened;
			if (this.isApprovePhaseOpened) {
				this.approvePhaseName = 1;
			}
		},
		selectDepartment(data) {
			this.formData.department = data.label;
		},
		mediaChange(value) {
			var vm = this;
			vm.formData.channelName = '';
			vm.formData.twoChannelName = '';
			vm.channelName = {
				options: []
			};
			vm.twoChannelName = {
				options: []
			};
			http.get("/api/allChannels", {
				params: {
					publisherId: value
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					var data = res.data.data;
					for (var i in data) {
						vm.channelName.options.push(data[i]);
					}
					console.log(vm.formData.twoChannelName);
				}
			}).catch((Error) => {
				console.log(Error);
			});
		},
		channelChange(value) {
			if (value != '') {
				var vm = this;
				vm.formData.twoChannelName = '';
				vm.twoChannelName.options = [];
				http.get("/api/allChannels", {
					params: {
						channelId: value
					}
				})
				.then((res) => {
					if (res.data.code == 200) {
						var data = res.data.data;
						for (var i in data) {
							vm.twoChannelName.options.push(data[i]);
						}
					}
				}).catch((Error) => {
					console.log(Error);
				});
			}
		},
		mediaList() {
			var vm = this;
			http.get("/api/publishers", {
				params: {
					status: 1
				}
			})
			.then((res) => {
				console.log(res);
				if (res.status === 200 && res.data.code === 200) {
					var data = res.data.data;
					for (var i in data.list) {
						vm.mediaName.options.push(data.list[i]);
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
				if (this.drawerCtrlAction == 'modify') {
					this.formData.mediaName = this.formData.publisherId;
					this.formData.twoChannelName = this.formData.channelId;
				}
				let params = {
					"name": this.formData.name,
				    "publisherId": this.formData.mediaName,
				    "channelId": this.formData.twoChannelName ? this.formData.twoChannelName : this.formData.channelName,
				    "width": this.formData.width,
				    "height": this.formData.height,
				    "rotators": this.formData.rotators,
				    "outSourceFlow": this.formData.outSourceFlow,
				    "materialNumber": this.formData.materialNumber,
				    "approveFlowId": this.formData.approveFlowId,
				    "description": this.formData.description
				};
				console.log(params);
				if (result) {
					http({
						url: '/api/slot',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							slotId: this.formData.slotId
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
	},
	watch: {
		'twoChannelName.options' : function() {
			if (this.twoChannelName.options.length != 0) {
				this.jugeTwoChannel = false;
			} else {
				this.jugeTwoChannel = true;
			}
		}
	}
};
</script>
