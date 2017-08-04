<template>
	<div>
		<div class="amp-form amp-form02">
			<el-popover ref="popover1" placement="right-start" width="390" trigger="hover">
				<h2 class="bidcpc-example-title">广告可能出现的位置：</h2>
				<ul class="bidcpc-example-content">
					<li v-for='(item, index) in showExample'>
						<p>{{item.title}}</p>
						<img :src="item.lSrc"/>
						<a :href="item.bSrc" target="_blank">查看大图</a>
					</li>
				</ul>
			</el-popover>
			<div class="form-column">
				<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
					<el-form-item label="投放单元名称：" prop="name">
						<el-input v-model="formData.name"></el-input>
					</el-form-item>
					<el-form-item label="投放类型：">
						<!--<el-radio-group v-model="formData.type">
							<el-radio :label="1">商品推广</el-radio>
						</el-radio-group>-->
						商品推广
					</el-form-item>
					<el-form-item label="资源位类型：" prop='advertisementGroups'>
						<el-checkbox-group v-model="formData.advertisementGroups">
							<el-checkbox :label="1">搜索位</el-checkbox>
							<el-checkbox :label="2">推荐位<el-button  type="text" v-popover:popover1>(示例)</el-button></el-checkbox>
					  	</el-checkbox-group>
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
										<!--<span class="tips-span">{{item.city.filter((each) => {return each.checked}).length}}/{{item.city.length}}</span>-->
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
						<div v-if="formData.region.length>0">
							<span class="selectSpan">已选：</span>
							<div>
								<el-tag
									:key="tag"
									v-for="(tag,index) in regionData.filter((item) => {return item.checked || item.city.filter((each) => {return each.checked}).length > 0 && item.city.filter((each) => {return each.checked}).length < item.city.length})"
									:closable="true"
									:close-transition="false"
									@close="handleClose(tag,index)"
								>
									{{tag.name}}
								</el-tag>
							</div>
						</div>
					</el-form-item>
					<el-form-item label="时间段定向：">
						<el-radio-group v-model="formData.timeType">
							<el-radio :label="0">全时间段</el-radio>
							<el-radio :label="1">自定义时间段</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="" v-show="formData.timeType === 1" prop="time" label-width="32px">
						<el-row class="time-space">
							<div class="space-cont">
								<div class="cont-tip">
									<span style="float: left;color:#a5a6a7; margin-left: 10px;">点击或拖拽可选择时间段</span>
									<i class="time-pointer green-module"></i><span class="time-tip-text">已选择</span>
									<i class="time-pointer"></i><span class="time-tip-text">未选择</span>
								</div>
							</div>
						</el-row>
						<table class="time-table" ondragstart="return false">
							<thead>
							<tr>
								<th>小时</th>
								<th :colspan="6">00:00-06:00</th>
								<th :colspan="6">06:00-12:00</th>
								<th :colspan="6">12:00-18:00</th>
								<th :colspan="6">18:00-24:00</th>
							</tr>
							<tr>
								<th>天</th>
								<th v-for="i in 24">{{i}}</th>
							</tr>
							</thead>
							<tbody @mouseleave="timeTip.isShowTip = false">
							<tr v-for="(whichweek,week) in formData.time">
								<td class="timeCheck"  @mouseenter="timeTip.isShowTip = false">
									<el-checkbox v-model="timeAllDayCheck[week]"
												 @change="selectAllDay(week)">星期{{numToCh(week)}}
									</el-checkbox>
								</td>
								<td v-for="(whichtime,t) in whichweek.time"
									@mousedown="timeSelStart(whichtime,t,week)"
									@mouseenter="timeSelMove(t,week)"
									@mouseup="timeSelEnd"
									:class="{'select-module': whichtime.sel,'move-module':whichtime.mov}">
								</td>
							</tr>
							<div class="timeTableTip" :style='{"top":timeTip.top,"left":timeTip.left}' v-show="timeTip.isShowTip" v-html="timeTip.tipMsg"></div>
							</tbody>
						</table>
						<el-row style="margin-top: 20px;">
							<el-col :span="2">
								<span>快速选择:</span>
							</el-col>
							<el-col :span="22" class="timeselBtnGroup">
								<el-radio-group v-model="showTimeSlot">
									<el-button class="btn"  @click="selectMultiDay(['0','1', '2', '3', '4', '5', '6'])">整周投放</el-button>
									<el-button class="btn"  @click="selectMultiDay(['0','1', '2', '3', '4'])">工作日投放</el-button>
									<el-button class="btn"  @click="selectMultiDay(['5', '6'])">周末投放</el-button>
								</el-radio-group>
							</el-col>
						</el-row>
						<el-row v-if="times.filter((item) => {return item.length>0}).length > 0">
							<el-col :span="2">
								<span>已选时间段:</span>
							</el-col>
							<el-col :span="22">
								<div  class="selectTime">
									<el-button @click="clearSelect">清空选择</el-button>
									<div v-for="(items,index) in times" v-if="items.length > 0">
										<span class="selectTimeTitle">星期{{numToCh(index-1)}}</span>
										<div class="selectTimeCont">
												<span v-for="(item , i) in items">
												   {{getTimeZone(item)}}
											    </span>
										</div>
									</div>
								</div>
							</el-col>
						</el-row>
					</el-form-item>

					<!-- <el-form-item label="人群信息定向：">
						<el-radio-group v-model="formData.ageType">
							<el-radio :label="0">不限年龄</el-radio>
							<el-radio :label="1">自定义</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="" v-show="formData.ageType === 1">
						<el-checkbox-group v-model="formData.age">
							<el-checkbox label="0-18岁"></el-checkbox>
							<el-checkbox label="19-26岁"></el-checkbox>
							<el-checkbox label="27岁以上"></el-checkbox>
					  </el-checkbox-group>
					</el-form-item>

					<el-form-item label="">
						<el-radio-group v-model="formData.genderType">
							<el-radio :label="0">不限性别</el-radio>
							<el-radio :label="1">自定义</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="" v-show="formData.genderType === 1">
						<el-checkbox-group v-model="formData.gender">
							<el-checkbox label="男"></el-checkbox>
							<el-checkbox label="女"></el-checkbox>
						</el-checkbox-group>
					</el-form-item> -->

					<el-form-item label="关键词设置：" v-show="formData.advertisementGroups.find((n) => n == 1)" >
						<el-button style='position: relative;' @click="showAddKeyword = true">+添加关键词<div v-show='showHintKeywords' class="hintKeywords">*请添加关键词</div></el-button>
						<div class="detail-row" v-show="formData.keywords.length">
							<table class="table" style="width:400px;margin-top:20px;">
								<tbody>
								<tr>
									<th><span>已购买</span></th>
									<th><span>PC端出价</span></th>
									<th><span>操作</span></th>
								</tr>
								</tbody>
							</table>
						</div>
						<div class="detail-row" v-show="formData.keywords.length" style="width: 420px;height: 150px;overflow: auto;margin-top: -1px;">
							<table class="table" style="width:400px;">
								<tbody>
									<tr v-for="(item, index) in formData.keywords">
										<td><span>{{item.name}}</span></td>
										<td><span>{{item.pcBid}}</span></td>
										<td><span><em class="icon icon-delete" @click="deleteKeywords(index)"></em></span></td>
									</tr>
								</tbody>
							</table>
						</div>
					</el-form-item>
					<el-form-item label="PC端推荐广告出价：" label-width="160px" v-show="formData.advertisementGroups.find((n) => n == 2)" >
						<el-row style="margin-bottom: -10px;">
							<el-col :span="3">
								<div class="ele-input ele-input04">
									<input type="text" value="" id="adBidInput" v-model="formData.adBid" @mouseout="adBidBlurCheck('adBidInput')" @input="adBidValidator('adBid', 'PC端推荐广告出价')">
								</div>
							</el-col>
							<el-col :span="4">
								<span style="margin-left: 10px">元/点击</span>
							</el-col>
						</el-row>
						<div class="el-form-item__error" id="adBidError"></div>
					</el-form-item>

					<el-form-item label="无线端广告出价系数：" label-width="160px">
						<el-row style="margin-bottom: -10px;">
							<el-col :span="3">
								<div class="ele-input ele-input04">
									<input type="text" value="" id="wirelessAdBidRatioInput" v-model="formData.wirelessAdBidRatio" @mouseout="wirelessAdBidRatioBlurCheck('wirelessAdBidRatioInput')" @input="wirelessAdBidRatioValidator('wirelessAdBidRatio', '无线端广告出价系数')">
								</div>
							</el-col>
							<el-col :span="4">
								<span style="margin-left: 10px"></span>
								<el-popover
									ref="popover1"
									placement="right-end"
									width="250"
									trigger="hover"
									content="无线端出价=PC端出价*无线端出价系数">
								</el-popover>
								<em class="icon icon-help" v-popover:popover1></em>
							</el-col>
						</el-row>
						<div class="el-form-item__error" id="wirelessAdBidRatioError"></div>
					</el-form-item>
				</el-form>
			</div>
		</div>
		<transition name="drawer">
			<m-add-ad v-if="showAddKeyword" :kwds="formData.keywords"></m-add-ad>
		</transition>
	</div>
</template>
<script>
import Vue from 'vue';
import store from 'store';
import lListSrc from '../../../../assets/img/bidcpc_example_list.png';
import lGoodsSrc from '../../../../assets/img/bidcpc_example_goodsDetail.png';
import lOrderSrc from '../../../../assets/img/bidcpc_example_orderDetail.png';
import {
	copyObj,
	objType,
	formatDate,
	mixin,
	limitLen
} from 'utils/common';

import AREA_DATA from '../../../../help/CITY.js';

import Event from 'event';
import Http from 'http';

import addAd from './drawer-add-keyword.vue';
export default {
	name: 'app-put-bidcpc-new-unit',
	data() {
		return {
			showExample: [
				{'title': '三级品类列表页', 'bSrc': 'https://gfs6.gomein.net.cn/T1ScAvBXhg1RCvBVdK.jpg', 'lSrc': lListSrc},
				{'title': '商品详情页', 'bSrc': 'https://gfs5.gomein.net.cn/T1YmKvBvYb1RCvBVdK.jpg', 'lSrc': lGoodsSrc},
				{'title': '订单详情页', 'bSrc': 'https://gfs5.gomein.net.cn/T18mVvBbEg1RCvBVdK.jpg', 'lSrc': lOrderSrc}
			],
			timeTip:{
				tipMsg:'',
				isShowTip:false,
				top:0,
				left:0
			},
			times:[],
			time:{
				'0': [],
				'1': [],
				'2': [],
				'3': [],
				'4': [],
				'5': [],
				'6': [],
			},
			flag:false,
			pointer:{
				startX:0,
				startY:0
			},
			showHintKeywords: false,
			showHintResource: false,
			formData: {},
			rules: {
				'name':[{validator: (rule, value, callback) => {
					if (!limitLen(value, 60)) {
						callback(new Error('字数不超过30个汉字'));
					} else if (value.trim() == '') {
						callback(new Error('请输入投放单元名称'));
					} else {
						Http.get('/api/flight/exist', {
							params: {
								name: value,
								campaignId: this.formData.campaignId,
								flightId: this.formData.flightId
							}
						})
						.then((res) => {
							if (res.data.code == 200) {
								if (res.data.data.exist == 1) {
									callback(new Error('投放单元名称已存在'));
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
				region: [{
					validator: (rule, value, callback) => {
						if (this.formData.region.length === 0 && this.formData.regionType) {
							callback(new Error('请选择投放地域'));
						} else {
							callback();
						}
					}
				}],
				time: [{
					validator: (rule, value, callback) => {
						let len = 0;
						this.times.forEach((item) => {
							len += item.length;
						});
						console.log(len);
						if (len === 0 && this.formData.timeType === 1) {
							callback(new Error('请选择投放时间段'));
						} else {
							callback();
						}
					}
				}],
				advertisementGroups: [{
					validator: (rule, value, callback) => {
						if (value.length == 0) {
							callback(new Error('请选择资源位类型'));
						} else {
							callback();
						}
					}
				}]
			},
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
			showAddKeyword: false,
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
		actionType: () => store.state.drawerCtrl.action
	},
	components: {
		'm-add-ad': addAd
	},
	created() {
		this.formData = copyObj(this.drawerData);

		this.formData.time = this.actionType=='modify' ? this.formData.time : this.initTimes();
		// 分转元
		this.formData.rebateBid = this.formData.rebateBid / 100;
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
		// 隐藏添加关键词
		Event.$off('drawer-hide-add-keyword');
		Event.$on('drawer-hide-add-keyword', () => {
			this.showAddKeyword  = false;
		});
		// 保存关键词
		Event.$off('drawer-save-add-keyword');
		Event.$on('drawer-save-add-keyword', (result) => {
			this.formData.keywords = result;
			this.showAddKeyword = false;
		});
		// 保存
		Event.$off('put-save');
		Event.$on('put-save', () => {
			this.$refs.formData.validate((result) => {
				let params = {
					name: this.formData.name,
					type: this.formData.type,
					advertisementGroups : this.formData.advertisementGroups,
					timeType: this.formData.timeType,
					time: this.time,
					regionType: this.formData.regionType,
					region: this.formData.regionType ? this.formData.region : [],
					// ageType: this.formData.ageType,
					// age: this.formData.age,
					// genderType: this.formData.genderType,
					// gender: this.formData.gender,
					adBid: this.formData.advertisementGroups.find((n) => n == 2) ? this.formData.adBid : '0',
					wirelessAdBidRatio: this.formData.wirelessAdBidRatio,
					keywords: this.formData.advertisementGroups.find((n) => n == 1) ? this.formData.keywords : [],
					productLine: 3
				};
				if (!this.formData.keywords.length && this.formData.advertisementGroups.find((n) => n == 1)) {
					this.showHintKeywords = true;
					return false;
				} else {
					this.showHintKeywords = false;
				}
				if (result & this.additionalCheck(this.formData.advertisementGroups)) {
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
				}
			});
		});
	},
	methods: {
		//滑动结束
		timeSelEnd() {
			this.flag = false;
			this.timeTip.isShowTip = false;
			this.formData.time.forEach((week) => {
				week.time.forEach((t) => {
					if (t.mov && t.sel) {
						t.sel = false;
						t.mov = false;
					} else if (t.mov && !t.sel) {
						t.sel = true;
						t.mov = false;
					}
				})
			})
		},
		//滑动过程
		timeSelMove(x,y){
			let self = this;
			self.timeTip.isShowTip = true;
			self.timeTip.left = x<18 ? (x+4)*36 + 'px' : (x-2)*36 +'px';
			self.timeTip.top = (y+3)*37 + 'px';
			if (self.flag) {
				let startX = self.pointer.startX; //0
				let startY = self.pointer.startY; //0
				self.timeTip.tipMsg = '星期'+ this.numToCh(startY) + ' 到 星期' + this.numToCh(y) + '<br>'+ this.changgeNum(startX) +'-'+this.changgeNum(x+1);
				self.formData.time.forEach(function(week, curY) {
					week.time.forEach(function(time, curX) {
						time.mov = (curX >= startX &&
							curX <= x &&
							curY >= startY &&
							curY <= y) ||
							(curX <= startX &&
							curX >= x &&
							curY <= startY &&
							curY >= y) ||
							(curX >= startX &&
							curX <= x &&
							curY <= startY &&
							curY >= y) ||
							(curX <= startX &&
							curX >= x &&
							curY >= startY &&
							curY <= y);
					})
				});
			}else{
				self.timeTip.tipMsg = '星期'+ this.numToCh(y) +' '+ this.changgeNum(x) +' '+this.changgeNum(x+1);
			}
		},
		//滑动开始
		timeSelStart(whichtime,x,y) {
			this.flag = true;
			whichtime.mov = true;
			this.pointer.startX = x;
			this.pointer.startY = y;
		},
		//选择某一周filter
		selectAllDay(day) {
			if (!this.timeAllDayCheck[day]) {
				this.formData.time[day].time.map((item) => {
					item.sel = false
				});
			} else {
				this.formData.time[day].time.map((item) => {
					item.sel = true
				});
			}
		},
		//初始化时间表
		initTimes() {
			let times = [];
			let days = [];
			for (let i=0; i<24; i++) {
				days.push({
					sel:false,
					mov:false,
				})
			}
			for(let i=0; i<7; i++){
				times.push({
					on:false,
					weeks:i,
					time:copyObj(days)
				})
			}
			return times
		},
		//已选时间段，合并时间
		mergeTime(time) {
			let times = [[],[],[],[],[],[],[]];
			Object.keys(time).forEach((t) => {
				time[t].sort(function(a, b) {
					return a - b;
				});
				let arr = [];
				let tempArr = [];
				let isContinuous = false;
				for (let i = 0; i < time[t].length; i++) {
					let item1 = time[t][i];
					let item2 = i == time[t].length ? time[t][i] : time[t][i + 1];
					if (item1 + 1 == item2) {
						if (!isContinuous) {
							tempArr.push(item1);
						}
						isContinuous = true;
						continue;
					} else {
						isContinuous = false;
						tempArr.push(item1);
					}
					if (!isContinuous) {
						arr.push(tempArr);
						tempArr = [];
					}
					times[t] = copyObj(arr);
				}
			})
			this.times = copyObj(times);
		},
		getTimeZone(num) {
			if(num.length == 1) {
				return this.changgeNum(num[0]) +'-'+ this.changgeNum(num[0]+1)
			}else{
				return this.changgeNum(num[0]) +'-'+ this.changgeNum(num[1]+1)
			}
		},
		changgeNum(n){
			n = n < 9 ? '0' + n + ':00' : n + ':00'
			return n;
		},
		clearSelect() {
			this.formData.time = this.initTimes();
			this.times = [];
		},
		deleteKeywords(index) {
			this.formData.keywords.splice(index, 1);
		},
		handleClose(tag,index){
		    let i = this.regionData.indexOf(tag);
			tag.checked = false;
		    this.regionData[i].city.forEach((item) => {
		        item.checked = tag.checked;
			})
			this.formData.region.splice(index,1)
		},
		selectProvince(item, index) {
			item.checked = !item.checked;
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
		selectMultiDay(days) {
			this.formData.time = this.initTimes();
			this.isMultiDay = {
				allweek : days.length == 7,
				workday : days.length == 5,
				happyday: days.length == 2
			}
			days.forEach((item) => {
				this.formData.time[item].time.forEach((time) => {
					time.sel = true;
				});
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
				0: '一',
				1: '二',
				2: '三',
				3: '四',
				4: '五',
				5: '六',
				6: '日',
				7: '八',
				8: '九'
			};
			return map[num];
		},
		wirelessAdBidRatioValidator(id, name) {
			var value = this.floatFormat(id);
			this.formData.wirelessAdBidRatio = value;
			return this.limitedCondition(id, name, value);
		},
		adBidValidator(id, name) {
			var value = this.floatFormat(id);
			this.formData.adBid = value;
			return this.limitedCondition(id, name, value);
		},
		additionalCheck(type) {
			if (type.toString().indexOf("2") != -1) {
				return this.adBidValidator('adBid', 'PC端推荐广告出价') & this.wirelessAdBidRatioValidator('wirelessAdBidRatio', '无线端广告出价系数');
			} else {
				return this.wirelessAdBidRatioValidator('wirelessAdBidRatio', '无线端广告出价系数');
			}
		},
		floatFormat(id) {
			var inputId = id + "Input";
			var value = document.getElementById(inputId).value;

			// 处理非数字 非小数点 的字符
			value = value.replace(/[^0-9\.]+/, "");
			// 处理以小数点开头的字符
			value = value.replace(/^\.+/, "");

			// 处理不小数点开头但出现多次小数点的字符
			var regObject = value.match(/\.+/g);
			var count = 0;
			if (regObject != null && regObject.length > 0) {
				for (var i = 0; i < regObject.length; i++) {
					count += regObject[i].length;
				}
			}

			if (value.indexOf(".") > 0) {
				if (count > 1) {
					value = value.substring(0, value.indexOf(".") + 1) + value.substring(value.indexOf(".")).replace(/\./g, "");
				}

				value = value.substring(0, value.indexOf(".") + 3);
			}

			document.getElementById(inputId).value = value;
			return value;
		},
		limitedCondition(id, name, value) {
			var errorId = id + "Error";

			if (value === '') {
				document.getElementById(errorId).innerText = '请输入' + name;
				return false;
			} else if (value < 0.10) {
				document.getElementById(errorId).innerHTML = name.slice(-2) + '最小为0.10，可精确到小数点后两位';
				return false;
			} else if (value > 999.99) {
				document.getElementById(errorId).innerHTML = name.slice(-2) + '最大不超过999.99，可精确到小数点后两位';
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
			if (value.indexOf(".") == 0) {
				value = "0" + value;
			}
			// 处理以小数点结尾的字符
			value = value.replace(/\.$/, "");

			document.getElementById(inputId).value = value;

			return value;
		},
		wirelessAdBidRatioBlurCheck(id) {
			var value = this.blurCheck(id);
			this.formData.wirelessAdBidRatio = value;
		},
		adBidBlurCheck(id) {
			var value = this.blurCheck(id);
			this.formData.adBid = value;
		}
	},
	watch: {
		'formData.time': {
			handler: function() {
				this.time = {};
				this.formData.time.forEach((item,index) => {
					let len = item.time.filter((t) => { return t.sel == true}).length;
					if( len == 24) {
						this.timeAllDayCheck[index] = true;
					}else {
						this.timeAllDayCheck[index] = false;
					}
					this.time[index+1] = [];
					item.time.forEach((time,t) => {
						if(time.sel) {
							this.time[index+1].push(t);
						}
					})
				});
				this.mergeTime(this.time);
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
<style lang="less" scoped>
	.selectTime{
		background-color: #f8f9fa;
		padding: 20px 0px;
		div{
			width:86%;
		}
		button{
			float: right;
			margin: 3px 20px 0 0;
		}
		span{
			margin: 0 10px;
		}
		.selectTimeTitle{
			float: left;
		}
		.selectTimeCont{
			display: inline-block;
			width:90%;
		}
	}
	.timeselBtnGroup{
		button{
			margin-right: 20px;
			background: none;
			border: 1px solid #e6e6e6;
		}
		.timeselBtn,.timeselBtn::focus{
			background: linear-gradient(to bottom, #d30312 0%, #d30312 100%);
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d30312', endColorstr='#d30312', GradientType=0);
			border-color: #d30312;
			color:#fff;
		&:hover{
			 background: linear-gradient(to bottom, #d30312 0%, #d30312 100%);
			 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d30312', endColorstr='#d30312', GradientType=0);
			 border-color: #d30312;
			 color:#fff;
		 }
		}
	}
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
.bidcpc-example-title{
	font-size: 14px;
	padding-top: 10px;
}
.bidcpc-example-content{
	p{
		font-size: 12px;
		height: 38px;
		line-height: 38px;
		margin-top: 7px;
	}
	a{
		font-size: 14px;
		margin-left: 20px;
	}
}
.selectSpan{
	float: left;
	width: 50px;
	&+div{
      	width: 560px;
	  	float: left;
	  	span{
		  	margin-right: 5px;
		  	background-color: #dde0e3;
		 	color:#23272c;
			.el-icon-close:hover{
				background-color: #d30312;
				color:#fff
			}
		 	&:hover{
				background-color: #d30312;
	        	color:#fff
		  	}
	  	}
	}
}
.hintKeywords{
	position: absolute;
	left:4px;
	top: 38px;
	color: #ff4949;
}
</style>
