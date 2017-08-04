<template>
	<div class="amp-form">
		<div class="form-column">
			<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
				<el-form-item label="投放计划名称：" prop="name">
					<el-input v-model="formData.name"></el-input>
				</el-form-item>
				<el-form-item label="起止时间：">
					<el-radio-group v-model="formData.isContinuous">
						<el-radio :label="1">连续投放</el-radio>
						<el-radio :label="0">不连续投放</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="" v-if="formData.isContinuous" label-width="140px">
					<el-radio-group v-model="formData.isUnlimited">
						<el-radio :label="0">设定结束时间</el-radio>
						<el-radio :label="1">不限结束时间</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="" v-if="formData.isContinuous" prop="startTime" label-width="140px">
					<el-col :span="11">
						<el-row>
							<el-col :span="7">
								<span>开始时间:</span>
							</el-col>
							<el-col :span="17">
								<el-date-picker :picker-options="pickerOptions" :editable="false" :clearable="false" type="date" placeholder="开始时间" v-model="startTime" style="width: 100%;"></el-date-picker>
							</el-col>
						</el-row>
					</el-col>
					<el-col :span="2" style="text-align: center;">-</el-col>
					<el-col :span="11" v-if="!formData.isUnlimited">
						<el-row>
							<el-col :span="7">
								<span>结束时间:</span>
							</el-col>
							<el-col :span="17">
								<el-date-picker :picker-options="pickerOptions" :editable="false" :clearable="false" type="date" placeholder="结束时间" v-model="endTime" style="width: 100%;"></el-date-picker>
							</el-col>
						</el-row>
					</el-col>
					<el-col :span="11" v-else>
						<span>本计划将长期有效</span>
					</el-col>
				</el-form-item>
				<el-form-item label="" v-if="formData.isContinuous === 0" style="margin-top: -15px;">
					<div class="time-setting">
						<div class="setting-header">
							<span class="header-day">共 {{daysLen}} 天</span>
							<span class="header-del" @click.prevent="formData.schedule = []"><em class="icon icon-delete"></em>全部删除</span>
							<span class="header-calendar" style="position: relative;">
								<em class="icon icon-calendar"></em>
								<el-date-picker :picker-options="pickerOptions" type="daterange" placeholder="请选择时间" size="mini" :editable="false" v-model="noContinuousDate" style="width: 14px;height: 14px;position: absolute;left: 0;top: 13px;opacity: 0;"></el-date-picker>
							</span>
						</div>
						<div class="setting-list" style="position: relative;">
							<ul>
								<li v-for="(item, index) in scheduleList">
									<span>{{item}}</span>
									<i @click="delSchedule(index)" style="cursor: pointer;font-size: 12px;margin-left: 80px;" class="el-icon-close"></i>
								</li>
							</ul>
							<div class="hintSelectTime" v-show='showHintSelectTime'>请选择投放时间段</div>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="计费方式：">
					<!--<el-radio-group v-model="formData.saleMode">
						&lt;!&ndash; <el-radio :label="1">CPM</el-radio> &ndash;&gt;
						<el-radio :label="3">CPC</el-radio>
					</el-radio-group>-->
					CPC（定价）
				</el-form-item>
				<el-form-item label="广告日预算：" prop="dailyAdBudget" class="daliyRebate">
					<el-row>
						<el-col :span="7">
							<el-radio-group style="vertical-align: middle;" v-model="formData.adLimited">
								<el-radio :label="0">不限</el-radio>
								<el-radio :label="1">自定义</el-radio>
							</el-radio-group>
						</el-col>
						<el-col :span="14" prop="adLimited">
							<el-input :disabled="formData.adLimited === 0" v-filterNum :placeholder="formData.adLimited === 0 ? '不限预算' : ''" v-model.number="formData.adLimited === 0 ? '' : formData.dailyAdBudget" style="widht: 100%"></el-input>
						</el-col>
						<el-col :span="2">
							<span style="margin-left: 6px;">元/天</span>
						</el-col>
						<el-col :span="1">
							<!-- <el-tooltip effect="light" placement="top-end">
								<div slot="content">
									<div class="dialog-white dialog-tip" style="font-size: 14px;">
										<p class="dialog-text"></p>
									</div>
								</div>
								<em class="icon icon-help"></em>
							</el-tooltip> -->
						</el-col>
					</el-row>
				</el-form-item>
				<el-row class="el-upload__tip" v-if="drawerCtrlAction == 'modify'">修改后广告日预算第二天生效</el-row>
				<el-form-item label="返利活动：">
					<el-switch on-text="" off-text="" v-model="isRebate" @change="customIsRebate"></el-switch>
				</el-form-item>
				<el-form-item label="返利日预算：" prop="dailyRebateBudget" v-show="isRebate" class="daliyRebate">
					<el-row>
						<el-col :span="7">
							<el-radio-group style="vertical-align: middle;" v-model="formData.rebateLimited">
								<el-radio :label="0">不限</el-radio>
								<el-radio :label="1">自定义</el-radio>
							</el-radio-group>
						</el-col>
						<el-col :span="14">
							<el-input :disabled="formData.rebateLimited === 0"  v-filterNumDaily :placeholder="formData.rebateLimited === 0 ? '不限预算' : ''" v-model.number="formData.rebateLimited === 0 ? '' : formData.dailyRebateBudget" style="widht: 100%"></el-input>
						</el-col>
						<el-col :span="2">
							<span style="margin-left: 6px;">元/天</span>
						</el-col>
						<el-col :span="1">
							<!-- <el-tooltip effect="light" placement="top-end">
								<div slot="content">
									<div class="dialog-white dialog-tip" style="font-size: 14px;">
										<p class="dialog-text"></p>
									</div>
								</div>
								<em class="icon icon-help"></em>
							</el-tooltip> -->
						</el-col>

					</el-row>
				</el-form-item>
				<el-row class="el-upload__tip" v-if="drawerCtrlAction == 'modify'">修改后返利日预算第二天生效</el-row>
			</el-form>
		</div>
	</div>
</template>
<script>
import Vue from 'vue';
import store from 'store';
import {
	copyObj,
	objType,
	formatDate,
	mixin,
	formatTimeByUnit,
	limitLen
} from 'utils/common';

import Event from 'event';
import Http from 'http';

var _THIS = '';
Vue.directive('filterNum', {
	bind: function(el) {
		const Input = el.getElementsByTagName('input')[0];
		Input.addEventListener('input',function(e){
			let val = Input.value.replace(/[^0-9]+/, "");
			Input.value = val;
			_THIS.formData.dailyAdBudget = Number(val);
		});
	}
});

Vue.directive('filterNumDaily', {
	bind: function(el) {
		const Input = el.getElementsByTagName('input')[0];
		Input.addEventListener('input',function(e){
			let val = Input.value.replace(/[^0-9]+/, "");
			Input.value = val;
			_THIS.formData.dailyRebateBudget = Number(val);
		});
	}
});

export default {
	name: 'app-put-new-plan',
	data() {
		return {
			showHintSelectTime: false,
			formData: {},
			pickerOptions: {
	          	disabledDate(time) {
	            	return time.getTime() < Date.now() - 8.64e7;
	          	}
	        },
			rules: {
				'name':[{validator: (rule, value, callback) => {
					if (!limitLen(value, 60)) {
						callback(new Error('字数不超过30个汉字'));
					} else if (value.trim() == '') {
						callback(new Error('请输入投放计划名称'));
					} else {
						Http.get('/api/campaign/exist', {
							params: {
								name: value,
								productLine: 2,
								campaignId: this.formData.campaignId
							}
						})
						.then((res) => {
							if (res.data.code == 200) {
								if (res.data.data.exist == 1) {
									callback(new Error('投放计划名称已存在'));
								} else {
									callback();
								}
							}
						})
						.catch((error) => {
							console.log(error);
						})
					}
				}, trigger: 'blur' }],
				dailyAdBudget: [{
					validator: (rule, value, callback) =>  {
					    if(this.formData.adLimited == 1) {
							if (value === '') {
								callback(new Error('请输入广告日预算'));
							}  else if (value < 0) {
								callback(new Error('不能为负数'));
							} else if (value > 99999999) {
								callback(new Error('广告日预算最大值不超过99999999.00'));
							} else if (value < 0.01) {
								callback(new Error('广告日预算最小不小于0.01'));
							} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length > 2 : false) {
								callback(new Error('只能两位小数'));
							} else {
								callback();
							}
						}else{
							callback();
						}
					},
					trigger: 'change'
				}],
				dailyRebateBudget: [{
					validator: (rule, value, callback) =>  {
						if(this.formData.rebateLimited == 1 && this.formData.isRebate == 1) {
							console.log(value);
							if (value === '') {
								callback(new Error('请输入返利日预算'));
							}  else if (value < 0) {
								callback(new Error('不能为负数'));
							} else if (value > 99999999) {
								callback(new Error('返利日预算最大值不超过99999999.00'));
							} else if (value < 0.01) {
								callback(new Error('返利日预算最小不小于0.01'));
							} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length > 2 : false) {
								callback(new Error('只能两位小数'));
							} else {
								callback();
							}
						}else {
							callback();
						}
					},
					trigger: 'change'
				}],
				startTime: [{
					validator: (rule, value, callback) => {
						if (this.startTime.valueOf() > this.endTime.valueOf()) {
							callback(new Error('开始时间不得迟于结束时间'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}]
			},
			noContinuousDate: [null, null],
			startTime: '',
			endTime: '',
			httpMethodMap: {
				new: 'post',
				copy: 'post',
				modify: 'put'
			}
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config, // from store
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		isRebate: function() {
			return this.formData.isRebate === 1;
		},
		scheduleList: function() {
			if (this.formData.schedule[0] === null)  {
				return [];
			}
			let result = [];
			this.formData.schedule.forEach((item) => {
				let one = Number(item[0]);
				let two = Number(item[1]);
				if (one === two) {
					result.push(formatDate(one, 'yyyy-MM-dd'));
				} else {
					result.push(`${formatDate(one, 'yyyy-MM-dd')} 至 ${formatDate(two, 'yyyy-MM-dd')}`);
				}
			});
			return result;
		},
		daysLen: function() {
			let len = 0;
			this.formData.schedule.forEach((item) => {
//				let cur = item.split('-');
				let cur = item;
				let result = formatTimeByUnit(cur[0], cur[1], {
					day: true
				});
				len += result.day;
			});
			return len;
		},
	},
	created() {
		this.formData = copyObj(this.drawerData);
		// 分转元
		this.formData.dailyRebateBudget = this.formData.dailyRebateBudget / 100;
		this.formData.dailyAdBudget = this.formData.dailyAdBudget / 100;
		this.startTime = this.formData.startTime || Date.now();
		this.endTime = this.formData.endTime || Date.now();
		_THIS = this;
	},
	mounted() {
		Event.$off('put-save');
		Event.$on('put-save', () => {
			this.$refs.formData.validate((result) => {
				let schedule = null;
				if (!this.formData.isContinuous) {
					schedule = this.formData.schedule.map((item) => {
						return [item[0].toString(), item[1].toString()];
					});
					if (schedule.length === 0) {
						this.showHintSelectTime = true;
						return false;
					}
				} else {
					schedule = [];
				}

				let params = {
					name: this.formData.name,
					isContinuous: this.formData.isContinuous,
					isUnlimited: this.formData.isUnlimited,
					startTime: this.formData.startTime,
					endTime: this.formData.endTime,
					schedule: schedule,
					saleMode: this.formData.saleMode,
					adLimited: this.formData.adLimited,
					dailyAdBudget: this.formData.adLimited ? this.formData.dailyAdBudget * 100 : 0,
					isRebate: this.formData.isRebate,
					rebateLimited: this.formData.isRebate ? this.formData.rebateLimited : 0,
					dailyRebateBudget: this.formData.rebateLimited ? this.formData.dailyRebateBudget * 100 : 0,
					productLine: 2
				};
				if (result) {
					Http({
						url: 'api/campaign',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							campaignId: this.formData.campaignId
						}, params) : params
					})
						.then((res) => {
							if (!res.data.iserror) {
								Event.$emit('put-save-result', {
									error: 0,
									res: res.data,
									isRebate: this.formData.isRebate
								});
							} else {
								Event.$emit('put-save-result', {
									error: 1,
									res: null
								});
							}
						})
						.catch((e) => {
							Event.$emit('put-save-result', {
								error: 1,
								res: null
							});
						});
				} else {
					Event.$emit('put-save-result', {
						error: 1,
						res: null
					});
				}
			});
		});
	},
	methods: {
		customIsRebate(value) {
			// value -> true/false transform to 1/0
			this.formData.isRebate = Number(value);
		},
		delSchedule(index) {
			this.formData.schedule.splice(index, 1);
		},
		mergeTime() {
			var start = null;
			var end = null;
			for(let i = 0; i < this.formData.schedule.length; i++) {
				for (let j = i + 1; j < this.formData.schedule.length; j++) {
					if (this.formData.schedule[j][0] <= this.formData.schedule[i][1] && this.formData.schedule[j][1] >= this.formData.schedule[i][0]) {
						if (this.formData.schedule[j][0] < this.formData.schedule[i][0]) {
							start = this.formData.schedule[j][0];
						} else {
							start = this.formData.schedule[i][0];
						}
						if (this.formData.schedule[j][1] < this.formData.schedule[i][1]) {
							end = this.formData.schedule[i][1];
						} else {
							end = this.formData.schedule[j][1];
						}
						this.formData.schedule[i] = [start, end];
						this.formData.schedule.splice(j, 1);
					}
				}
			}
		}
	},
	watch: {
		'formData.adLimited': function() {
			if (!this.formData.adLimited) {
				this.formData.dailyAdBudget = 0;
			}
		},
		'formData.rebateLimited': function() {
			if (!this.formData.rebateLimited) {
				this.formData.dailyRebateBudget = 0;
			}
		},
		/**
		'formData.isContinuous': function(val, oldVal) {
			this.formData.schedule = [];
		},
		*/
		noContinuousDate: function() {
			// 重置
			if (this.noContinuousDate[0] !== null) {
			    this.formData.schedule.push([this.noContinuousDate[0].valueOf(),this.noContinuousDate[1].valueOf()]);
				this.noContinuousDate[0] = this.noContinuousDate[1] = null;
			}
			this.showHintSelectTime = false;
		},
		startTime: function() {
			if (this.startTime) {
				this.formData.startTime = new Date(this.startTime).valueOf();
			}
		},
		endTime: function() {
			if (this.endTime) {
				this.formData.endTime = new Date(this.endTime).valueOf();
			}
		},
		'formData.schedule': function() {
			this.mergeTime();
		}
	}
};
</script>
<style scoped>

	.el-upload__tip{
		line-height: 0.8;
		margin: -8px 0 22px 140px;
	}
.error {
	color: #ff5151;
}
.hintSelectTime{
	position: absolute;
	left: 0;
	top: 10px;
	color: #ff4949;
	font-size: 13px;
}
</style>
