<template>
	<div>
		<div class="amp-form amp-form02">
			<el-popover ref="popover1" placement="right-start" width="390" trigger="hover">
				<img src="../../../../assets/img/bidcpc_example.jpg" height="480" width="370">
			</el-popover>
			<div class="form-column">
				<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
					<el-form-item label="投放单元名称：" prop="name">
						<el-input v-model="formData.name"></el-input>
					</el-form-item>
					<el-form-item label="投放类型：">
						<el-radio-group v-model="formData.type">
							<el-radio :label="1">商品推广</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="资源位类型：">
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
						<el-button @click="showAddKeyword = true">+添加关键词</el-button>
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
									<input type="text" value="" id="adBidInput" v-model="formData.adBid" @mouseout="adBidBlurCheck('adBidInput')" @keyup="adBidValidator('adBid', 'PC端推荐广告出价')">
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
									<input type="text" value="" id="wirelessAdBidRatioInput" v-model="formData.wirelessAdBidRatio" @mouseout="wirelessAdBidRatioBlurCheck('wirelessAdBidRatioInput')" @keyup="wirelessAdBidRatioValidator('wirelessAdBidRatio', '无线端广告出价系数')">
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
import {
	copyObj,
	objType,
	formatDate,
	mixin
} from 'utils/common';

import AREA_DATA from '../../../../help/CITY.js';

import Event from 'event';
import Http from 'http';

import addAd from './drawer-add-keyword.vue';
export default {
	name: 'app-put-bidcpc-new-unit',
	data() {
		return {
			formData: {},
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
					time: this.formData.time,
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
		deleteKeywords(index) {
			this.formData.keywords.splice(index, 1);
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
				document.getElementById(errorId).innerHTML = name + '最小为0.10';
				return false;
			} else if (value > 999.99) {
				document.getElementById(errorId).innerHTML = name + '最大不超过999.99';
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
