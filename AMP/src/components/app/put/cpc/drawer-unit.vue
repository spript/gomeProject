<template>
	<div>
		<div class="amp-form amp-form02">
			<div class="form-column">
				<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
					<el-form-item label="投放单元名称：" prop="name">
						<el-input v-model="formData.name"></el-input>
					</el-form-item>
					<el-form-item label="设备类型：">
						<el-radio-group v-model="formData.platform">
							<el-radio :label="1">无线</el-radio>
							<el-radio :label="3">PC</el-radio>
						</el-radio-group>
					</el-form-item><!--
					<el-form-item label="投放类型：">
						<el-radio-group v-model="formData.type">
							<el-radio :label="1" :disabled="actionType === 'modify'">商品推广</el-radio>
							<el-radio :label="2" :disabled="actionType === 'modify'">活动推广</el-radio>
							<el-radio :label="3" :disabled="actionType === 'modify'">视频</el-radio>
						</el-radio-group>
					</el-form-item>-->
					<el-form-item label="投放广告位：">
						<span class="form-text">已选择<i class="group-tip">{{formData.advertisements.length}}</i>个广告位</span>
						<el-button @click="showAddAd = true" :disabled="!isShow">添加广告位</el-button>
						<div class="detail-row detail-row03" v-show="formData.advertisements.length">
							<table class="table">
								<tbody>
									<tr>
										<th><span>媒体名称</span></th>
										<th><span>广告位名称</span></th>
										<th width="90"><span>刊例价</span></th>
										<th width="120"><span>素材尺寸限制</span></th>
										<th width="120"><span>素材大小限制</span></th>
										<th v-show="isShow" width="58"><span>操作</span></th>
									</tr>
									<tr v-for="(item, index) in formData.advertisements">
										<td><span>{{item.publisherName}}</span></td>
										<td><span>{{item.advertisementName}}</span></td>
										<td><span>{{item.adBid/100}}</span></td>
										<td><span>{{item.width}} * {{item.height}}</span></td>
										<td><span>{{item.size}}</span></td>
										<td v-show="isShow"><span><em class="icon icon-delete" @click="delAd(index)"></em></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</el-form-item><!--
					<el-form-item label="返利金额：" v-show="isRebate" prop="rebateBid">
						<el-row>
							<el-col :span="8">
								<el-input placeholder="请输入返利金额" v-model.number="formData.rebateBid"></el-input>
							</el-col>
							<el-col :span="4">
								<span style="margin-left: 10px">元/次分享</span>
								<!-- <el-tooltip effect="light" placement="top-end">
									<div slot="content">
										<div class="dialog-white dialog-tip" style="font-size: 14px;">
											<p class="dialog-text"></p>
										</div>
									</div>
									<em class="icon icon-help"></em>
								</el-tooltip> --><!--
							</el-col>
						</el-row>
					</el-form-item>-->
					<el-form-item label="分享返利：" v-show="shareType" prop="rebateBid">
						<el-row>
							<el-col :span="1">
								<el-checkbox v-model="rebateBid"></el-checkbox>
							</el-col>
							<div v-if="rebateBid">
								<el-col :span="2">返利金额：</el-col>
								<el-col :span="6">
									<el-input placeholder="请输入返利金额" v-model="formData.rebateBid"></el-input style="">
								</el-col>
								<el-col :span="4">元/次分享</el-col>
							</div>
						</el-row>
					</el-form-item>
					<el-form-item label="视频浏览返利：" v-show="videoType" prop="videoRebate">
						<el-row>
							<el-col :span="1">
								<el-checkbox v-model="videoRebate"></el-checkbox>
							</el-col>
							<div v-if="videoRebate">
								<el-col :span="2">返利金额：</el-col>
								<el-col :span="6">
									<el-input placeholder="请输入返利金额" v-model="formData.videoRebate"></el-input style="">
								</el-col>
								<el-col :span="4">元/次浏览</el-col>
							</div>
						</el-row>
					</el-form-item>
					<el-form-item label="调研返利：" v-show="researchype" prop="researchRebate">
						<el-row>
							<el-col :span="1">
								<el-checkbox v-model="researchRebate"></el-checkbox>
							</el-col>
							<div v-if="researchRebate">
								<el-col :span="2">调研返利：</el-col>
								<el-col :span="6">
									<el-input placeholder="请输入返利金额" v-model="formData.researchRebate"></el-input style="">
								</el-col>
								<el-col :span="4">元/份</el-col>
							</div>
						</el-row>
						<el-row v-if="researchRebate">
							<el-col :span="1">&nbsp;</el-col>
							<el-col :span="2">调研问卷：</el-col>
							<el-col :span="4"><el-button @click="showAddSurvey = true">选择问卷</el-button></el-col>
							<el-col :span="4">有效问卷收集总数：</el-col>
							<el-col :span="6">
								<el-form-item prop="validQuestionnaireNum">
									<el-input placeholder="请输入有效问卷收集总数" v-model.number="formData.validQuestionnaireNum"></el-input>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-if="survey.length > 0">
							<el-col :span="1">&nbsp;</el-col>
							<el-col :span="16">
								<table class="table">
									<tbody>
										<tr>
											<th><span>问卷id</span></th>
											<th><span>问卷名称</span></th>
											<th width="200"><span>创建时间</span></th>
											<th v-show="isShow" width="58"><span>操作</span></th>
										</tr>
										<tr v-for="(item, index) in survey">
											<td><span>{{item.surveyId}}</span></td>
											<td><span>{{item.name}}</span></td>
											<td><span>{{item.createTime}}</span></td>
											<td v-show="isShow"><span><em class="icon icon-delete" @click="delSurvey(index)"></em></span></td>
										</tr>
									</tbody>
								</table>
							</el-col>
						</el-row>
					</el-form-item>
					<el-form-item label="地域定向：">
						<el-radio-group v-model="formData.regionType">
							<el-radio :label="0">不限</el-radio>
							<el-radio :label="1">自定义</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="" style="margin-top: -20px;" prop="region" v-show="formData.regionType">
						<el-checkbox v-model="regionCheck" @change="selectAllRegion">全选</el-checkbox>
						<div class="address-list">
							<div class="list-province">
								<ul>
									<li v-for="(item, index) in regionData" @click="applyInnerCity(index)">
										<el-checkbox v-model="item.checked" :indeterminate="item.city.filter((each) => {return each.checked}).length > 0 && item.city.filter((each) => {return each.checked}).length < item.city.length" @change="selectProvince(item, index)"></el-checkbox>
										<span>{{item.name}}</span>
										<span class="tips-span">{{item.city.filter((each) => {return each.checked}).length}}/{{item.city.length}}</span>
									</li>
								</ul>
							</div>
							<div class="list-city">
								<ul>
									<li v-for="(item, index) in regionChildrenData">
										<el-checkbox v-model="item.checked">{{item.name}}</el-checkbox>
									</li>
								</ul>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="时间段定向：">
						<el-radio-group v-model="formData.timeType">
							<el-radio :label="0">全时间段</el-radio>
							<el-radio :label="1">自定义时间段</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="" v-show="formData.timeType === 1" prop="time">
						<el-row>
							<el-col :span="2">
								<span>快速设定:</span>
							</el-col>
							<el-col :span="22">
								<el-radio-group v-model="showTimeSlot">
									<el-button @click="selectMultiDay(['1', '2', '3', '4', '5', '6', '7'])">整周展示</el-button>
									<el-button @click="selectMultiDay(['1', '2', '3', '4', '5'])">仅工作日展示</el-button>
									<el-button @click="selectMultiDay(['6', '7'])">仅休息日展示</el-button>
								</el-radio-group>
							</el-col>
						</el-row>
						<div class="time-space">
							<div class="space-cont">
								<div class="cont-tip"><i class="time-pointer green-module"> </i><span class="time-tip-text">投放时间段</span><i class="time-pointer"></i><span class="time-tip-text">暂停时间段</span></div>
								<div class="cont-header"><span class="span-row-1">日期</span><span class="span-row-2">复制到</span><span class="span-row-3">00:00-05:00</span><span class="span-row-3">06:00-11:00</span><span class="span-row-3">12:00-17:00</span><span class="span-row-3">18:00-23:00</span></div>
								<div class="cont-infor">
									<ul>
										<li class="actived" v-for="whichDay in ['1', '2', '3', '4', '5', '6', '7']">
											<div class="span-row-1">
												<el-checkbox v-model="timeAllDayCheck[whichDay]" @change="selectAllDay(whichDay)">星期{{numToCh(whichDay)}}</el-checkbox>
											</div>
											<div class="span-row-2"><a href="javascript:void(0)" @click="copyTime(whichDay, [...genContinuousNum(7)])" class="btn btn-gray-small">整周</a><a href="javascript:void(0)" @click="copyTime(whichDay, [...genContinuousNum(5)])" class="btn btn-gray-small">工作日</a><a href="javascript:void(0)" @click="copyTime(whichDay, [...genContinuousNum(5, 7)])" class="btn btn-gray-small">休息日</a></div>
											<div class="span-row-3"><i class="time-pointer" :class="{'gray-module': formData.time[whichDay].indexOf(item) === -1}" v-for="item in [0,1,2,3,4,5]" @click="selectOneDay(whichDay, item)">{{item}}</i></div>
											<div class="span-row-3"><i class="time-pointer" :class="{'gray-module': formData.time[whichDay].indexOf(item) === -1}" v-for="item in [6,7,8,9,10,11]" @click="selectOneDay(whichDay, item)">{{item}}</i></div>
											<div class="span-row-3"><i class="time-pointer" :class="{'gray-module': formData.time[whichDay].indexOf(item) === -1}" v-for="item in [12,13,14,15,16,17]" @click="selectOneDay(whichDay, item)">{{item}}</i></div>
											<div class="span-row-3"><i class="time-pointer" :class="{'gray-module': formData.time[whichDay].indexOf(item) === -1}" v-for="item in [18,19,20,21,22,23]" @click="selectOneDay(whichDay, item)">{{item}}</i></div>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</el-form-item>
				</el-form>
			</div>
		</div>
		<transition name="drawer">
			<m-add-ad v-if="showAddAd" :form-data="formData"></m-add-ad>
		</transition>
		<transition name="drawer">
			<m-add-survey v-if="showAddSurvey" :form-data="formData"></m-add-survey>
		</transition>
	</div>
</template>
<script>
import Vue from 'vue';
import store from 'store';
import {
	copyObj,
	objType,
	formatDate,
	mixin
} from 'utils/common';

import AREA_DATA from '../../../../help/CITY.js';

import Event from 'event';
import Http from 'http';

import addAd from './drawer-add-ad.vue';
import addSurvey from './drawer-add-survey.vue';
import moment from 'moment';
export default {
	name: 'app-put-new-unit',
	data() {
		let vm = this;
		return {
			formData: {},
			isShow: true,
			rules: {
				name: [{
					required: true,
					message: '请输入投放单元名称',
					trigger: 'blur'
				}, {
					max: 30,
					message: '最大长度不超过30个汉字',
					trigger: 'blur'
				}],
				rebateBid: [{
					validator: (rule, value, callback) => {
						if(this.isRebate ==1 && this.rebateBid == true){
							var reg = new RegExp("^\\d+(\\.\\d+)?$");
							if (value === '') {
								callback(new Error('请输入返利出价'));
							} else if (!reg.test(value)) {
								callback(new Error('只能为数字'));
							} else if (this.isRebate === 1 && value < 0.1) {
								callback(new Error('返利出价最低为0.1元/次'));
							} else if (this.isRebate === 1 && value > 100) {
								callback(new Error('返利出价最高不超过100元/次'));
							} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length >1 : 0) {
								callback(new Error('只能一位小数'));
							}
						}
						callback();
					},
					trigger: 'change'
				}],
				videoRebate: [{
					validator: (rule, value, callback) => {
						if(this.isRebate ==1 && this.videoRebate == true ){
							var reg = new RegExp("^\\d+(\\.\\d+)?$");
							if (value === '') {
								callback(new Error('请输入返利出价'));
							} else if (!reg.test(value)) {
								callback(new Error('只能为数字'));
							} else if (this.isRebate === 1 && value < 0.1) {
								callback(new Error('返利出价最低为0.1元/次'));
							} else if (this.isRebate === 1 && value > 100) {
								callback(new Error('返利出价最高不超过100元/次'));
							} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length >1 : 0) {
								callback(new Error('只能一位小数'));
							}
						}
						callback();
					},
					trigger: 'change'
				}],
				researchRebate: [{
					validator: (rule, value, callback) => {
						if(this.isRebate== 1 && this.researchRebate == true){
							var reg = new RegExp("^\\d+(\\.\\d+)?$");
							if (value === '') {
								callback(new Error('请输入返利出价'));
							} else if (!reg.test(value)) {
								callback(new Error('只能为数字'));
							} else if (this.isRebate === 1 && value < 0.3) {
								callback(new Error('返利出价最低为0.3元/次'));
							} else if (this.isRebate === 1 && value > 100) {
								callback(new Error('返利出价最高不超过100元/次'));
							} else if (/\.(.*)/.test(value.toString()) ? value.toString().match(/\.(.*)/)[1].length >1 : 0) {
								callback(new Error('只能一位小数'));
							}
						}
						callback();
					},
					trigger: 'change'
				}],
				validQuestionnaireNum: [{
					validator: (rule, value, callback) => {
						if (this.isRebate == 1 && this.researchRebate == true) {
							if (value === 0) {
								callback(new Error('请输入大于0的数'));
							} else {
								callback();
							}
						} else {
							callback();
						}
					}
				}],
				surveryList: [{
					validator: (rule, value, callback) => {
						if (this.isRebate == 1 && this.researchRebate) {
							if (value.list.length === 0) {
								callback(new Error('请选择问卷'));
							} else {
								callback();
							}
						} else {
							callback();
						}
					}
				}],
				region: [{
					validator: (rule, value, callback) => {
						if (this.formData.region.length === 0 && this.formData.regionType) {
							callback(new Error('投放地域不能为空'));
						} else {
							callback();
						}
					}
				}],
				time: [{
					validator: (rule, value, callback) => {
						let len = 0;
						Object.keys(this.formData.time).forEach((item) => {
							len += this.formData.time[item].length;
						});
						if (len === 0 && this.formData.timeType === 1) {
							callback(new Error('投放时间段不能为空'));
						} else {
							callback();
						}
					}
				}]
			},
			rebateBid: false,
			videoRebate: false,
			researchRebate: false,
			regionCheck: false,
			regionData: AREA_DATA,
			regionChildrenData: [],
			showTimeSlot: 0,
			timeAllDayCheck: {
				'1': false,
				'2': false,
				'3': false,
				'4': false,
				'5': false,
				'6': false,
				'7': false
			},
			survey: [],
			showAddAd: false,
			showAddSurvey: false,
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
		isRebate: () => store.state.drawerCtrl.isRebate, // 是否返利，从 state 中来 如果被复写掉了为 false 达到知晓上一级是否返利的目的
		actionType: () => store.state.drawerCtrl.action,
		shareType() {
			if(this.isRebate == 0 || this.isRebate == undefined){
				return false;
			}else{
				if(this.formData.advertisements.length !=0){
					return true;
				}
			}
		},
		researchype() {
			if(this.isRebate == 0 || this.isRebate == undefined){
				return false;
			}else{
				if(this.formData.advertisements.length !=0){
					if(this.formData.advertisements[0].generalizeType == 2 && this.formData.advertisements[0].webpageTemplateId == 1){//有腔调
						return false;
					}else if(this.formData.advertisements[0].generalizeType == 1){//商品
						return false;
					}else{
						return true;
					}
				}
			}
		},
		videoType() {
			if(this.isRebate == 0 || this.isRebate == undefined){
				return false;
			}else{
				if(this.formData.advertisements.length != 0){
					if(this.formData.advertisements[0].generalizeType == 9){
						return true;
					}else{
						return false;
					}
				}
			}
		},
	},
	components: {
		'm-add-ad': addAd,
		'm-add-survey': addSurvey
	},
	created() {
		this.formData = copyObj(this.drawerData);

		if(this.actionType=='modify'){
			this.getMaterialCount();
			this.rebateBid = this.formData.rebateBid != 0;
			this.videoRebate = this.formData.videoRebate != 0;
			this.researchRebate = this.formData.researchRebate != 0;
			if (this.researchRebate) {
				this.getSurveyWithId();
				this.formData.validQuestionnaireNum = this.formData.validQuestionnaireNum == 0 ? '' : this.formData.validQuestionnaireNum;
			}
		}

		this.formData.time = this.formData.time ? this.formData.time : { // 时间定向 默认参数
			'1': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'2': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'3': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'4': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'5': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'6': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23],
			'7': [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]
		};
		// 分转元
		this.formData.rebateBid = this.formData.rebateBid / 100;
		this.formData.videoRebate = this.formData.videoRebate / 100;
		this.formData.researchRebate = this.formData.researchRebate / 100;
	},
	mounted() {
		this.regionData.forEach((item) => {
			Vue.set(item, 'checked', false);
			item.city.forEach((each) => {
				if (this.formData.region.indexOf(each.zip) !== -1) {
					Vue.set(each, 'checked', true);
				} else {
					Vue.set(each, 'checked', false);
				}
			});
		});
		// 隐藏添加广告位
		Event.$off('drawer-hide-add-ad');
		Event.$on('drawer-hide-add-ad', () => {
			this.showAddAd = false;
		});
		// 保存广告位
		Event.$off('drawer-save-add-ad');
		Event.$on('drawer-save-add-ad', (result) => {
			this.formData.advertisements = result;
			this.showAddAd = false;
		});
		// 隐藏添加问卷
		Event.$off('drawer-hide-add-survey');
		Event.$on('drawer-hide-add-survey', (result) => {
			if (result) {
				this.survey = result;
				this.formData.surveyId = result[0].surveyId;
			}
			this.showAddSurvey = false;
		});
		// 保存
		Event.$off('put-save');
		Event.$on('put-save', () => {
			if(this.isRebate == 1){
				if(this.rebateBid == false && this.videoRebate == false && this.researchRebate == false){
					this.$message({
						message: '请至少勾选一种返利',
						type: 'error'
					});
					return false;
				} else if (this.researchRebate && this.survey.length === 0) {
					this.$message({
						message: '请选择问卷',
						type: 'error'
					});
					return false;
				}
			}
			this.$refs.formData.validate((result) => {
				let params = {
					name: this.formData.name,
					platform: this.formData.platform,
					type: this.formData.type,
					timeType: this.formData.timeType,
					time: this.formData.time,
					regionType: this.formData.regionType,
					region: this.formData.regionType ? this.formData.region : [],
					rebateBid: this.isRebate && this.rebateBid ? this.formData.rebateBid * 100: 0,
					videoRebate: this.isRebate && this.videoRebate ? this.formData.videoRebate * 100: 0,
					researchRebate: this.isRebate && this.researchRebate ? this.formData.researchRebate * 100: 0,
					advertisements: this.formData.advertisements,
					productLine: 2,
					surveyId: this.formData.surveyId,
					validQuestionnaireNum: this.formData.validQuestionnaireNum == '' ? 0 : this.formData.validQuestionnaireNum
				};
				if (result) {
					Http({
						url: 'api/flight',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: this.drawerCtrlAction === 'modify' ? mixin({
							flightId: this.formData.flightId
						}, params) : mixin({
							campaignId: this.formData.campaignId
						}, params)
					})
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('put-save-result', {
								error: 0,
								res: res.data,
								isRebate: this.isRebate,
								type: this.formData.type
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
				};
			});
		});
	},
	methods: {
		delSurvey(index) {
			this.survey.splice(index, 1);
			this.formData.surveyId = '';
		},
		delAd(index) {
			this.formData.advertisements.splice(index, 1);
		},
		getSurveyWithId() {
			Http.get('/api/survey?surveyId=' + this.formData.surveyId).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror == 0) {
					this.survey.push({
						surveyId: res.data.data.surveyId,
						name: res.data.data.name,
						createTime: moment(res.data.data.createTime).format('YYYY年MM月DD日')
					});
				}
			}).catch((err) => {
				this.$message({
					message: err,
					type: 'error'
				});
			})
		},
		selectProvince(item, index) {
			this.regionData[index].city.forEach((each) => {
				if (this.formData.region.indexOf(each) === -1) {
					each.checked = item.checked;
				}
			});
		},
		applyInnerCity(index) {
			this.regionChildrenData = [...this.regionData[index].city];
		},
		selectAllRegion() {
			this.regionData.forEach((item) => {
				item.city.forEach((each) => {
					each.checked = this.regionCheck;
				});
			});
		},
		selectOneDay(day, hour) {
			this.formData.time[day].reverse();
			let index = this.formData.time[day].indexOf(hour);
			index !== -1 ? this.formData.time[day].splice(index, 1) : this.formData.time[day].push(hour);
		},
		selectAllDay(day) {
			if (!this.timeAllDayCheck[day]) {
				this.formData.time[day] = [];
			} else {
				this.formData.time[day] = [...this.genContinuousNum(23)];
			}
		},
		selectMultiDay(days) {
			this.formData.time = {
				'1': [],
				'2': [],
				'3': [],
				'4': [],
				'5': [],
				'6': [],
				'7': []
			};
			days.forEach((item) => {
				this.formData.time[item] = [...this.genContinuousNum(23)];
			});
		},
		copyTime(from, array) {
			array.forEach((item) => {
				this.formData.time[JSON.stringify(item)] = [...this.formData.time[from]];
			});
		},
		genContinuousNum(from, to) {
			let realFrom = 0;
			let realTo = 0;
			if (to) {
				realTo = to;
				realFrom = from;
			} else {
				realTo = from;
			}
			let result = [];
			while (realTo >= realFrom) {
				result.push(realTo);
				realTo--;
			}
			return result.reverse();
		},
		numToCh(num) {
			let map = {
				0: '零',
				1: '一',
				2: '二',
				3: '三',
				4: '四',
				5: '五',
				6: '六',
				7: '日',
				8: '八',
				9: '九'
			};
			return map[num];
		},
		getMaterialCount(){
		  let tem = this;
			Http.get("/api/material/total?flightId=" +this.formData.flightId, {
			})
			.then(function(res) {
				if(res.data.code == 200){
					 if(res.data.data.materialTotal != 0){
							tem.isShow = false;
					 }
				}
			})
			.catch(function(error) {
				alert("list fail");
			});
		}
	},
	watch: {
		'formData.time': {
			handler: function() {
				Object.keys(this.formData.time).forEach((item) => {
					if (this.formData.time[item].length === 24) {
						this.timeAllDayCheck[item] = true;
					} else {
						this.timeAllDayCheck[item] = false;
					}
				});
			},
			deep: true
		},
		'regionData': {
			handler: function() {
				this.formData.region = [];
				let allLen = 0;
				let allCheckLen = 0;
				this.regionData.forEach((item) => {
					let cur = 0;
					item.city.forEach((each) => {
						allLen++;
						let index = this.formData.region.indexOf(each);
						if (each.checked && index === -1) {
							this.formData.region.push(each.zip);
							cur++;
						}
					});
					if (cur === item.city.length) {
						item.checked = true;
					} else {
						item.checked = false;
					}
					allCheckLen = cur + allCheckLen;
				});
				if (allCheckLen === allLen) {
					this.regionCheck = true;
				}
			},
			deep: true
		}
	}
};
</script>
<style>
.tips-span {
	position: absolute;
	background: #14a3ff;
	color: #fff;
	border-radius: 5px;
	font-size: 12px;
	line-height: 12px;
	padding: 2px 5px;
	right: 25px;
	top: 5px;
}
</style>
