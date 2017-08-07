<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
			<el-form-item label="任务名称：" prop="name">
				<div v-show='!isTypeLook'><el-input style='width:342px;' :disabled='isMissionStatus' v-model="formData.name" @change='trim("name")'></el-input></div>
				<div v-show='isTypeLook'>{{formData.name}}</div>
			</el-form-item>
			<el-form-item label="领取奖励份数：" style='position: relative;'>
				<div v-show='!isTypeLook'><input ref='totalCount' @input.prevent="clearNum('totalCount');awardFocus($event,'totalCount');" v-model="formData.totalCount" class='w150 jugeInput'>&nbsp;&nbsp;份</div>
				<div v-show='isTypeLook'>{{formData.totalCount}}&nbsp;份</div>
				<div class="totalCount" v-show="showHint.totalCount">{{formData.totalCount>99999999?'最大值不能超过99999999':'请输入领取奖励份数'}}</div>
			</el-form-item>
			<el-form-item label="起止时间：" prop="startTime">
				<el-col class='w150' v-show='!isTypeLook'>
					<el-date-picker @change='selectedTime' :disabled='isMissionStatus' :picker-options="pickerOptions" :editable="false" :clearable="false" type="date" placeholder="开始时间" v-model="formData.startTime" style="width: 100%;"></el-date-picker>
				</el-col>
				<div class="fl" v-show='isTypeLook'>{{formData.startTime | date('yyyy-MM-dd')}}</div>
				<el-col :span="2" style="text-align: center;">-</el-col>
				<el-col class='w150' v-show='!isTypeLook'>
					<el-date-picker @change='selectedTime' :disabled='isMissionStatus' :picker-options="pickerOptions" :editable="false" :clearable="false" type="date" placeholder="结束时间" v-model="formData.endTime" style="width: 100%;"></el-date-picker>
				</el-col>
				<div class="fl" v-show='isTypeLook'>{{formData.endTime | date('yyyy-MM-dd')}}</div>
			</el-form-item>
			<el-form-item label='重复模式：' style='position: relative;'>
				<el-radio-group v-model="formData.cycleMode" style='padding-top: 10px;' v-show='!isTypeLook'>
					<el-radio :disabled='isMissionStatus' :label="0">累计模式</el-radio>
					<el-radio :disabled='isMissionStatus' :label="1" style='display: block;margin-left: 0;margin-top: 30px;'>周期模式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input ref='cycleDays' :class="{'mission-disabled':formData.cycleMode == 0 || isMissionStatus}" :disabled='formData.cycleMode == 0 || isMissionStatus' :style='{borderColor:showHint.cycleDays?"#ff4949":"#BFCBD9"}' @input.prevent="clearNum('cycleDays');awardFocus($event,'cycleDays');" v-model="formData.cycleDays" class='w100 jugeInput'>&nbsp;&nbsp;&nbsp;天</el-radio>
				</el-radio-group>
				<div v-show='isTypeLook&&formData.cycleMode==0'>累计模式</div>
				<div v-show='isTypeLook&&formData.cycleMode==1'>周期模式&nbsp;&nbsp;{{formData.cycleDays}}天</div>
				<div class="cycleDays" v-show="showHint.cycleDays">{{formData.cycleDays>99999999?'最大值不能超过99999999':(formData.cycleDays>timeDiff?'周期天数大于起止时间的天数请修改':'请输入周期')}}</div>
			</el-form-item>
			<el-form-item label='任务类型：'>
					<el-col v-show='!isTypeLook' :span="30" class='pb-22'>
				    	<el-checkbox :disabled='isMissionStatus' :checked='formData.typeUpVote == 1' @change='autoFocus($event,"typeUpVoteCount")' v-model='formData.typeUpVote' true-label="1" false-label="0">点赞</el-checkbox>&nbsp;&nbsp;&nbsp;&nbsp;数量：
				    	<input ref='typeUpVoteCount' :class="{'mission-disabled':formData.typeUpVote == 0 || isMissionStatus}" :disabled='formData.typeUpVote == 0 || isMissionStatus' @input.prevent="clearNum('typeUpVoteCount');awardFocus($event,'typeUpVoteCount');" v-model="formData.typeUpVoteCount" class='w100 jugeInput'>&nbsp;&nbsp;&nbsp;个
				    	<div class="missionTypeHint" v-show="showHint.typeUpVoteCount">{{formData.typeUpVoteCount>99999999?'最大值不能超过99999999':'请输入数量'}}</div>
				    </el-col>
				    <div v-show='isTypeLook&&formData.typeUpVote == 1'>点赞&nbsp;&nbsp;数量：{{formData.typeUpVoteCount}}个</div>
				    <el-col v-show='!isTypeLook' :span="30" class='pb-22'>
				    	<el-checkbox :disabled='isMissionStatus' :checked='formData.typeShare == 1' @change='autoFocus($event,"typeShareCount")' v-model='formData.typeShare' true-label="1" false-label="0">分享</el-checkbox>&nbsp;&nbsp;&nbsp;&nbsp;数量：
				    	<input ref='typeShareCount' :class="{'mission-disabled':formData.typeShare == 0 || isMissionStatus}" :disabled='formData.typeShare == 0 || isMissionStatus' @input.prevent="clearNum('typeShareCount');awardFocus($event,'typeShareCount');" v-model="formData.typeShareCount" class='w100 jugeInput'>&nbsp;&nbsp;&nbsp;次
				    	<div class="missionTypeHint" v-show="showHint.typeShareCount">{{formData.typeShareCount>99999999?'最大值不能超过99999999':'请输入数量'}}</div>
				    </el-col>
				    <div v-show='isTypeLook&&formData.typeShare == 1'>分享&nbsp;&nbsp;数量：{{formData.typeShareCount}}次</div>
				    <el-col v-show='!isTypeLook' :span="30" class='pb-22'>
				    	<el-checkbox :disabled='isMissionStatus' :checked='formData.typeResearch == 1' @change='autoFocus($event,"typeResearchCount")' v-model='formData.typeResearch' true-label="1" false-label="0">调研</el-checkbox>&nbsp;&nbsp;&nbsp;&nbsp;数量：
				    	<input ref='typeResearchCount' :class="{'mission-disabled':formData.typeResearch == 0 || isMissionStatus}" :disabled='formData.typeResearch == 0 || isMissionStatus' @input.prevent="clearNum('typeResearchCount');awardFocus($event,'typeResearchCount');" v-model="formData.typeResearchCount" class='w100 jugeInput'>&nbsp;&nbsp;&nbsp;份
				    	<div class="missionTypeHint" v-show="showHint.typeResearchCount">{{formData.typeResearchCount>99999999?'最大值不能超过99999999':'请输入数量'}}</div>
				    </el-col>
				    <div v-show='isTypeLook&&formData.typeResearch == 1'>调研&nbsp;&nbsp;数量：{{formData.typeResearchCount}}份</div>
				    <el-col v-show='!isTypeLook' :span="30" style='position: relative;'>
				    	<el-checkbox :disabled='isMissionStatus' :checked='formData.typeWatch == 1' @change='autoFocus($event,"typeWatchCount")' v-model='formData.typeWatch' true-label="1" false-label="0">视频</el-checkbox>&nbsp;&nbsp;&nbsp;&nbsp;数量：
				    	<input ref='typeWatchCount' :class="{'mission-disabled':formData.typeWatch == 0 || isMissionStatus}" :disabled='formData.typeWatch == 0 || isMissionStatus' @input.prevent="clearNum('typeWatchCount');awardFocus($event,'typeWatchCount');" v-model="formData.typeWatchCount" class='w100 jugeInput'>&nbsp;&nbsp;&nbsp;条
				    	<div class="missionTypeHint" v-show="showHint.typeWatchCount && formData.typeWatch == 1">{{formData.typeWatchCount>99999999?'最大值不能超过99999999':'请输入数量'}}</div>
				    </el-col>
				    <div v-show='isTypeLook&&formData.typeWatch == 1'>视频&nbsp;&nbsp;数量：{{formData.typeWatchCount}}条</div>
			</el-form-item>
			<el-form-item label='奖励金额：' style='position: relative;'>
				<div v-show='!isTypeLook'><input @blur="changeRound()" ref='awardAmount' @input.prevent="clearAwardAmount();awardFocus($event,'awardAmount');" v-model="formData.awardAmount" class='w150 jugeInput'>&nbsp;&nbsp;&nbsp;国美币</div>
				<div v-show='isTypeLook'>{{formData.awardAmount}}&nbsp;&nbsp;国美币</div>
				<div class="totalCount" v-show="showHint.awardAmount">{{formData.awardAmount>99999999?'最大值不能超过99999999':'请输入奖励金额'}}</div>
			</el-form-item>
			<el-form-item label='任务说明：' prop="description">
				<div v-show='!isTypeLook'><el-input style='width:342px;' type="textarea" v-model="formData.description" :rows="4" @change='trim("description")'></el-input></div>
				<div v-show='isTypeLook' class="missionDes">{{formData.description}}</div>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
import Event from 'event';
import actions from 'actions';
import store from 'store';
import http from 'http';
import moment from 'moment';
import {
	objType,
	mixin,
	copyObj,
	limitLen
} from 'utils/common';
export default {
	name: "drawer-mission",
	data() {
		return {
			pickerOptions: {
	          	disabledDate(time) {
	            	return time.getTime() < Date.now() - 8.64e7;
	          	}
	        },
			formData: {},
			timeDiff: '',
			rules: {
				'name':[{validator: function(rule, value, callback) {
					if (!limitLen(value, 30)) {
						callback(new Error('最大长度不超过30个字符'));
					} else if (value == '') {
						callback(new Error('请输入任务名称'));
					} else {
						callback();
					}
				}, trigger: 'change' }],
				'description':[{validator: function(rule, value, callback) {
					if (!limitLen(value, 800)) {
						callback(new Error('最大长度不超过800个字符'));
					} else if (value == '') {
						callback(new Error('请输入任务说明'));
					} else {
						callback();
					}
				}, trigger: 'change' }],
				startTime: [{
					validator: (rule, value, callback) => {
						if (this.formData.startTime.valueOf() > this.formData.endTime.valueOf()) {
							callback(new Error('开始时间不得迟于结束时间'));
						} else if (value == '') {
							callback(new Error('请填写时间段'));
						} else {
							callback();
						}
					},
					trigger: 'change'
				}]
			},
			httpMethodMap: {
				new: 'post',
				modify: 'put',
				copy: 'post'
			},
			showHint: {
				totalCount: false,
				cycleDays: false,
				typeUpVoteCount: false,
				typeShareCount: false,
				typeResearchCount: false,
				typeWatchCount: false,
				awardAmount: false
			},
			index: ['totalCount','cycleDays','typeUpVoteCount','typeShareCount','typeResearchCount','typeWatchCount','awardAmount'],
			resultCount: [],
			firstEnter: false
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config,
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		config: () => store.state.drawerCtrl,
		isTypeLook() {
			return this.drawerCtrlAction == 'look';
		},
		isMissionStatus() {
			return this.formData.missionStatus == 1;
		}
	},
	created() {
		this.formData = copyObj(this.drawerData);
		if (this.drawerCtrlAction == 'copy') {
			this.formData.startTime = '';
			this.formData.endTime = '';
		}
		this.formData.typeUpVoteCount = this.formData.typeUpVoteCount == 0 ? '' : this.formData.typeUpVoteCount;
		this.formData.typeShareCount = this.formData.typeShareCount == 0 ? '' : this.formData.typeShareCount;
		this.formData.typeResearchCount = this.formData.typeResearchCount == 0 ? '' : this.formData.typeResearchCount;
		this.formData.typeWatchCount = this.formData.typeWatchCount == 0 ? '' : this.formData.typeWatchCount;
		this.formData.cycleDays = this.formData.cycleDays == 0 ? '' : this.formData.cycleDays;
		this.formData.awardAmount = this.formData.awardAmount ? this.formData.awardAmount / 100 : '';
		if (this.drawerCtrlAction == 'copy' || this.drawerCtrlAction == 'modify' || this.drawerCtrlAction == 'look') {
			this.changeRound();
		}
	},
	methods: {
		trim(type) {
			this.formData[type] = this.formData[type].replace(/(^\s*)|(\s*$)/g, "");
		},
		selectedTime() {
			this.timeDiff = moment(this.formData.endTime).diff(moment(this.formData.startTime), 'days') + 1;
			if (this.formData.cycleDays > this.timeDiff && this.formData.cycleMode == 1) {
				this.$refs.cycleDays.style.borderColor = '#ff4949';
				this.showHint.cycleDays = true;
			} else {
				this.$refs.cycleDays.style.borderColor = '#BFCBD9';
				this.showHint.cycleDays = false;
			}
		},
		autoFocus(event,type) {
			if (!event.currentTarget.checked) {
				this.showHint[type] = false;
				this.$refs[type].style.borderColor = '#BFCBD9';
				this.formData[type] = '';
			}
		},
		clearNum(type) {
			if (this.formData[type].length) {
				this.formData[type] = this.formData[type].replace(/^0/g, "");
				this.formData[type] = this.formData[type].replace(/[^0-9]/g,"");
		    }
		},
		clearAwardAmount() {
			if (this.formData.awardAmount.length) {
				this.formData.awardAmount = this.formData.awardAmount.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  
			    this.formData.awardAmount = this.formData.awardAmount.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的  
			    this.formData.awardAmount = this.formData.awardAmount.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
			}
		},
		changeRound() {
			if (this.formData.awardAmount != '') {
				var f_x = Math.round(this.formData.awardAmount * 100) / 100;
				var s_x = f_x.toString();
				var pos_decimal = s_x.indexOf('.');
				if (pos_decimal < 0) {
					pos_decimal = s_x.length;
					s_x += '.';
				}
				while (s_x.length <= pos_decimal + 2) {
					s_x += '0';
				}
				this.formData.awardAmount = s_x;
			}
		},
		changeBorderColor(event,color) {
			event.currentTarget.style.borderColor = color;
		},
		awardFocus(event,type) {
			this.timeDiff = moment(this.formData.endTime).diff(moment(this.formData.startTime), 'days') + 1;
			if (this.formData[type] == '') {
				this.changeBorderColor(event,'#ff4949');
				this.showHint[type] = true;
			} else if (this.formData[type] > 99999999) {
				this.changeBorderColor(event,'#ff4949');
				this.showHint[type] = true;
			} else if (type == 'cycleDays' && this.formData[type] > this.timeDiff) {
				this.changeBorderColor(event,'#ff4949');
				this.showHint[type] = true;
			} else {
				this.changeBorderColor(event,'#BFCBD9');
				this.showHint[type] = false;
			}
		},
		saveHint(type) {
			this.showHint[type] = true;
			this.$refs[type].style.borderColor = '#ff4949';
		},
		saveJudge() {
			this.resultCount = [];
			this.timeDiff = moment(this.formData.endTime).diff(moment(this.formData.startTime), 'days') + 1;
			for(var i = 0;i < this.index.length;i++) {
				if (this.index[i] == 'totalCount' || this.index[i] == 'awardAmount') {
					if (this.formData[this.index[i]] == '' || this.formData[this.index[i]] > 99999999) {
						this.saveHint(this.index[i]);
						this.resultCount.push(0);
					} else {
						this.resultCount.push(1);
					}
				} else if (this.index[i] == 'cycleDays') {
					if ((this.formData[this.index[i]] > 99999999 || this.formData[this.index[i]] == '' || this.formData[this.index[i]] > this.timeDiff) && this.formData.cycleMode == 1) {
						this.saveHint(this.index[i]);
						this.resultCount.push(0);
					} else {
						this.resultCount.push(1);
					}
				} else {
					var newStr = this.index[i].replace("Count", "");
					if ((this.formData[this.index[i]] > 99999999 || this.formData[this.index[i]] == '') && this.formData[newStr] == 1) {
						this.saveHint(this.index[i]);
						this.resultCount.push(0);
					} else {
						this.resultCount.push(1);
					}
				}
			}
			var open = true;
			for(var j = 0;j < this.resultCount.length;j++) {
				if (this.resultCount[j] == 0) {
					open = false;
				}
			}
			return open;
		},
		timeClearHours(date) {
			const cloneDate = new Date(date);
		    cloneDate.setHours(0, 0, 0);
		    return cloneDate.getTime();
		},
		setTimeHours(date) {
			const cloneDate = new Date(date);
			cloneDate.setHours(23, 59, 59);
			return cloneDate.getTime();
		}
	},
	mounted() {
		Event.$off('mission-save');
		Event.$on('mission-save', () => {
			this.$refs.formData.validate((result) => {
				let params = {
					name: this.formData.name,
					totalCount: this.formData.totalCount,
					startTime: this.timeClearHours(this.formData.startTime),
					endTime: this.setTimeHours(this.formData.endTime),
					cycleMode: this.formData.cycleMode,
					cycleDays: this.formData.cycleDays == ''? 0 : this.formData.cycleDays,
					typeUpVote: this.formData.typeUpVote,
					typeUpVoteCount: this.formData.typeUpVoteCount == ''? 0 : this.formData.typeUpVoteCount,
					typeShare: this.formData.typeShare,
					typeShareCount: this.formData.typeShareCount == ''? 0 : this.formData.typeShareCount,
					typeResearch: this.formData.typeResearch,
					typeResearchCount: this.formData.typeResearchCount == ''? 0 : this.formData.typeResearchCount,
					typeWatch: this.formData.typeWatch,
					typeWatchCount: this.formData.typeWatchCount == ''? 0 : this.formData.typeWatchCount,
					awardAmount: this.formData.awardAmount * 100,
					description: this.formData.description,
				};
				let modifyParams = mixin({missionId: this.formData.missionId}, params);
				this.saveJudge();
				if (this.formData.typeUpVote == 0 && this.formData.typeShare == 0 && this.formData.typeResearch == 0 && this.formData.typeWatch == 0) {
					this.$message({
						message: '至少选择一种任务类型',
						type: 'warning',
						duration: 3000
					});
				} else {
					if (result && this.saveJudge()) {
						if (this.drawerCtrlAction == 'modify' && this.formData.missionStatus == 1) {
							Event.$emit("missionModify",modifyParams);
						} else {
							http({
								url: '/api/mission',
								method: this.httpMethodMap[this.drawerCtrlAction],
								data: this.drawerCtrlAction === 'modify' ? mixin({missionId: this.formData.missionId}, params) : params
							})
							.then((res) => {
								if (!res.data.iserror) {
									Event.$emit('mission-save-result', {
										error: 0,
										res: res.data,
										isRebate: this.formData.isRebate
									});
								} else {
									Event.$emit('mission-save-result', {
										error: 1,
										res: null
									});
								}
							})
							.catch((e) => {
								Event.$emit('mission-save-result', {
									error: 1,
									res: null
								});
							});
						}
					} else {
						Event.$emit('mission-save-result', {
							error: 1,
							res: null
						});
					}
				}
			});
		});
	},
	watch: {
		'formData.cycleMode': function() {
			var vm = this;
			if (vm.formData.cycleMode != 1) {
				vm.showHint.cycleDays = false;
				vm.formData.cycleDays = '';
			}
		}
	}
};
</script>
<style lang="less">
	.totalCount{
		font-size: 12px;
		line-height: 16px;
		color: #ff4949;
		padding-top: 4px;
		position: absolute;
		left: 0;
		top: 35px;
	}
	.cycleDays{
		font-size: 12px;
		line-height: 16px;
		color: #ff4949;
		padding-top: 4px;
		position: absolute;
		left: 105px;
		top: 95px;
	}
	.pb-22{
		padding-bottom: 22px;
		position: relative;
	}
	.missionTypeHint{
		font-size: 12px;
		line-height: 16px;
		color: #ff4949;
		padding-top: 4px;
		position: absolute;
		left: 105px;
		top: 35px;
	}
	.missionDes{
		word-break: break-all;
	}
</style>