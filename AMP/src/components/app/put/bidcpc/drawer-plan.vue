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
								<el-date-picker :picker-options="pickerOptions" :editable="false" :disabled="formData.state && formData.state !== 4" :clearable="false" type="date" placeholder="开始时间" v-model="startTime" style="width: 100%;"></el-date-picker>
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
					<!--<el-radio-group v-model="formData.saleMode">-->
						<!-- <el-radio :label="1">CPM</el-radio> -->
						<!-- <el-radio :label="2">CPD</el-radio> -->
						<!--<el-radio :label="3">CPC（竞价）</el-radio>-->
					<!--</el-radio-group>-->
					CPC（竞价）
				</el-form-item>
				<el-form-item label="广告日预算：" style='position: relative;'>
					<el-row>
						<el-col :span="7">
							<el-radio-group style="vertical-align: middle;" v-model="formData.adLimited">
								<el-radio :label="0">不限</el-radio>
								<el-radio :label="1">自定义</el-radio>
							</el-radio-group>
						</el-col>
						<!--<el-col :span="14">-->
							<!--<el-input :disabled="formData.adLimited === 0" :placeholder="formData.adLimited === 0 ? '不限预算' : ''" v-model="formData.adLimited === 0 ? '' : formData.dailyAdBudget" style="width: 100%"></el-input>-->
						<!--</el-col>-->
						<el-col :span="14">
							<div :class="{'ele-input ele-input04':formData.adLimited === 0, 'ele-input ele-input04 ele-diy':formData.adLimited != 0,dailyBorderColor:!showHintDaily}">
								<input :disabled="formData.adLimited === 0" :placeholder="formData.adLimited === 0 ? '不限预算' : ''" v-model="formData.adLimited === 0 ? '' : formData.dailyAdBudget" style="width: 250px;"
									   id="dailyAdBudgetInput" @mouseout="dailyAdBudgetBlurCheck('dailyAdBudgetInput')" @input="dailyAdBudgetKeyUpCheck('dailyAdBudget', '广告日预算')" />
							</div>
						</el-col>
						<el-col :span="2">
							<span>元/天</span>
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
					<div style="position: absolute;left: 145px;top: 36px;" class="el-form-item__error" id="dailyAdBudgetError"></div>
					<el-row style='position: absolute;left: 0;top: 42px;' class="el-upload__tip" v-if="drawerCtrlAction == 'modify'">修改后广告日预算第二天生效</el-row>
				</el-form-item>
			</el-form>
			<br>
		</div>
	</div>
</template>
<script>
import store from 'store';
import {
	copyObj,
	objType,
	formatDate,
	mixin,
	formatTimeByUnit,
	limitLen
} from 'utils/common';
import moment from 'moment';
import Event from 'event';
import Http from 'http';

export default {
	name: 'app-put-bidcpc-new-plan',
	data() {
		return {
			showHintSelectTime: false,
			showHintDaily: true,
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
								campaignId: this.formData.campaignId,
								name: value,
								productLine: 3
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
//				dailyAdBudget: [{
//					validator: function(rule, value, callback) {
//						if (value === '') {
//							callback(new Error('请填写广告日预算'));
//						} else if (objType(value) !== 'Number') {
//							callback(new Error('只能为数字'));
//						} else if (value < 0) {
//							callback(new Error('不能为负数'));
//						} else if (value > 99999999) {
//							callback(new Error('最大值不超过99,999,999.00'));
//						} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length > 2 : false) {
//							callback(new Error('只能两位小数'));
//						} else {
//							callback();
//        				}
//					},
//					trigger: 'change'
//				}],
				dailyRebateBudget: [{
					validator: function(rule, value, callback) {
						if (value === '') {
							callback(new Error('请填写返利日预算'));
						} else if (objType(value) !== 'Number') {
							callback(new Error('只能为数字'));
						} else if (value < 0) {
							callback(new Error('不能为负数'));
						} else if (value > 99999999) {
							callback(new Error('最大值不超过99,999,999.00'));
						}  else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length > 2 : false) {
							callback(new Error('只能两位小数'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}],
				startTime: [{
					validator: (rule, value, callback) => {
						if (this.startTime.valueOf() > this.endTime.valueOf() && this.formData.isUnlimited === 0) {
							callback(new Error('开始时间不得晚于结束时间'));
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
				let cur = item;
				let result = formatTimeByUnit(cur[0], cur[1], {
					day: true
				});
				len += result.day;
			});
			return len;
		}
	},
	created() {
		this.formData = copyObj(this.drawerData);
		// 分转元
		this.formData.dailyRebateBudget = this.formData.dailyRebateBudget / 100;
		this.formData.dailyAdBudget = this.formData.dailyAdBudget / 100;
		this.startTime = moment(this.formData.startTime).startOf('day').valueOf() || moment(Date.now()).startOf('day').toDate();
		this.endTime = moment(this.formData.endTime).endOf('day').valueOf() || moment(Date.now()).endOf('day').toDate();
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
					productLine: 3
				};
				if (result & this.additionalCheck(this.formData.adLimited)) {
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
		intFormat(id) {
			var inputId = id + "Input";
			var value = document.getElementById(inputId).value;

			// 处理非数字 的字符
			value = value.replace(/[^0-9]+/, "");

			document.getElementById(inputId).value = value;
			return value;
		},
		limitedCondition(id, name, value) {
			var errorId = id + "Error";
			if (value === '') {
				document.getElementById(errorId).innerText = '请输入' + name;
				return false;
			} else if (value < 1) {
				document.getElementById(errorId).innerHTML = name + '最小不小于1';
				return false;
			} else if (value > 99999999) {
				document.getElementById(errorId).innerHTML = name + '最大不超过99999999';
				return false;
			} else {
				document.getElementById(errorId).innerHTML = '';
				return true;
			}
		},
		blurCheck(inputId) {
			var value = document.getElementById(inputId).value;

			// 处理一个或者多个0开头 的字符
			if (value != 0) {
				value = value.replace(/^0+/, "");
			}

			document.getElementById(inputId).value = value;

			return value;
		},
		dailyAdBudgetBlurCheck(inputId) {
			var value = this.blurCheck(inputId);
			this.formData.dailyAdBudget = value;
		},
		dailyAdBudgetKeyUpCheck(prefixId, name) {
			var value = this.intFormat(prefixId);
			this.formData.dailyAdBudget = value;
			this.showHintDaily = this.limitedCondition(prefixId, name, value);
			return this.limitedCondition(prefixId, name, value);
		},
		additionalCheck(type){
		    if (type != 0) {
				return this.dailyAdBudgetKeyUpCheck('dailyAdBudget', '广告日预算');
			}

			return true;
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
				this.showHintDaily = true;
				document.getElementById('dailyAdBudgetError').innerHTML = '';
			} else {
				this.showHintDaily = this.limitedCondition('dailyAdBudget', '广告日预算', this.formData.dailyAdBudget);
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
				this.formData.startTime = moment(this.startTime).startOf('day').valueOf();
			}
		},
		endTime: function() {
			if (this.endTime) {
				this.formData.endTime = moment(this.endTime).endOf('day').valueOf();
			}
		},
		'formData.schedule': function() {
			this.mergeTime();
		}
	}
};
</script>
<style scoped>
.error {
	color: #ff4949;
}
.ele-input:hover {
	border-color: #e1e3e5;
}
.ele-diy:hover {
	border-color: #20a0ff;
}
.hintSelectTime{
	position: absolute;
	left: 0;
	top: 10px;
	color: #ff4949;
	font-size: 13px;
}
.dailyBorderColor{
	border-color: #ff4949;
}
.dailyBorderColor:hover{
	border-color: #ff4949;
}
</style>
