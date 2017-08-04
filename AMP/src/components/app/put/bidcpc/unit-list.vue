<template>
	<div>
		<!-- <dialog></dialog> -->
		<div class="amp-path">
			<span class="path-col">计划名称：<b>{{summary.name}}</b></span>
			<span class="path-col">有效期：<b>{{summary.startTime}}-{{summary.endTime}}</b></span>
			<span class="path-col">状态：<b>{{summary.state}}</b></span>
		</div>
		<div class="account-set">
			<div class="set-option">
				<!--<router-link to="/app/bidcpc/plan" class="btn btn-normal btn-back-up"><em class="icon icon-prev"></em>返回</router-link>-->
				<a href="" class="btn btn-normal" @click.prevent="newUnit">新建投放单元</a>
				<a href="" class="btn btn-normal" @click.prevent="start">启用</a>
				<a href="" class="btn btn-normal" @click.prevent="stop">暂停</a>
				<a href="" class="btn btn-normal" @click.prevent="del">删除</a>
				<span class="set-option-hint" v-show='showOpationHint'>*请选择至少一项后，再进行操作。</span>
			</div>
			<div class="set-fn">
				<div class="fn-plan"><span class="plan-title">单元名称：</span>
					<div class="ele-input">
						<input v-model="so.keyword" placeholder="请输入单元名称">
					</div>
				</div>
				<el-form :model="so" :rules="rules" ref="formlist">
					<div class="fn-plan">
						<el-form-item prop="startTime" style="display:inline-block;">
							<span class="plan-title">选择时间：</span>
							<el-date-picker v-model="so.startTime" type="date" :editable="false" style="width:125px;"
											placeholder="开始日期">
							</el-date-picker>
							<span class="plan-title plan-title-gray">-</span>
							<el-date-picker v-model="so.endTime" type="date" :editable="false" style="width:125px;"
											placeholder="结束日期">
							</el-date-picker>
						</el-form-item>
					</div>
				</el-form>
				<div class="fn-plan"><span class="plan-title">状态：</span>
					<el-select v-model="so.state.value" style="width:100px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.state.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>

				<div class="fn-search" @click.prevent="search"><a href="" class="btn btn-primary"
																  style="width:76px;padding:0">查询</a></div>
			</div>
		</div>
		<div class="amp-data" style="padding-bottom: 28px;">
			<div class="data-table" id="mainDataTable">
				<div class="main-table-wapper">
					<table class="table main-table">
						<thead>
						<tr :class="{actived: isActived, 'list-header': true}">
							<th class="w50"><span><em class="icon icon-select  select-all"
													  @click="checkall"></em></span></th>
							<th class="w100"><span><i class="data-id">单元ID</i></span></th>
							<th class="w220"><span>单元名称</span></th>
							<th class="w60"><span>状态</span></th>
							<th class="w120"><span>定向（地域|时间段）</span></th>
							<th class="w100"><span>搜索/推荐</span></th>
							<th class="w100"><span>出价</span></th>
							<th class="w80"><span>展现数</span></th>
							<th class="w80"><span>点击数</span></th>
							<th class="w80"><span>点击率(%)</span></th>
							<th class="w80"><span>花费</span></th>
							<th class="w80"><span>平均点击单价</span></th>
							<th class="w80"><span>千次展现成本</span></th>
							<th class="w220"><span>操作</span></th>
						</tr>
						</thead>
						<tbody>
						<tr class="body-row" v-if="list.length == 0">
							<td colspan="14" style="text-align: center; height: 100px;line-height: 100px;">无相关查询结果</td>
						</tr>
						<template v-for="item in list">
							<tr :class="{actived: item.isActived, 'body-row': true}">
								<td rowspan="2"><span><em @click="checkbox(item.flightId)"
														  :class="'icon icon-select select-' + item.flightId"
														  :data-id="item.flightId"></em></span></td>
								<td rowspan="2"><span><i class="data-id">{{item.flightId}}</i></span></td>
								<td rowspan="2">
                    <span>
                      <i class="ellipsis">
                        <router-link :to="{name: 'bidcpcIdea', params: {id: item.flightId}}" :title="item.name">{{item.name}}</router-link>
                      </i>
                    </span>
								</td>
								<td rowspan="2"><span>{{item.state==1?"暂停":"启用"}}</span></td>
								<td rowspan="2">
                    <span>
                      <i class="ellipsis">{{item.regionType==0?"全国":"区域"}}|{{item.timeType==0?"全日程":"自定义"}}</i>
                    </span>
								</td>
								<td><span>搜索</span></td>
								<td><span>{{item.dmpData[0].search.searchMinBid}}~{{item.dmpData[0].search.searchMaxBid}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.impressionNumber == null ? "--" : item.dmpData[0].search.impressionNumber}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.clickNumber == null ? "--" : item.dmpData[0].search.clickNumber}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.clkRate == null ? "--" : item.dmpData[0].search.clkRate}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.totalFee == null ? "--" : item.dmpData[0].search.totalFee}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.clickUnitPrice == null ? "--" : item.dmpData[0].search.clickUnitPrice}}</span>
								</td>
								<td><span>{{item.dmpData[0].search.costPerMills == null ? "--" : item.dmpData[0].search.costPerMills}}</span>
								</td>
								<td rowspan="2"><span>
                    <b class="operat-link">
                      <a href="" @click.prevent="modify(item.flightId)">修改</a><a href=""
																				 @click.prevent="copy(item.flightId)">复制单元</a><a
						href="" @click.prevent="newIdea(item.flightId, item.type)">新建创意</a>
                    </b>
                  </span></td>
							</tr>
							<tr>
								<td><span>推荐</span></td>
								<td><span>{{item.dmpData[0].recomment.adBid == null ? "--" : item.dmpData[0].recomment.adBid}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.impressionNumber == null ? "--" : item.dmpData[0].recomment.impressionNumber}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.clickNumber == null ? "--" : item.dmpData[0].recomment.clickNumber}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.clkRate == null ? "--" : item.dmpData[0].recomment.clkRate}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.totalFee == null ? "--" : item.dmpData[0].recomment.totalFee}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.clickUnitPrice == null ? "--" : item.dmpData[0].recomment.clickUnitPrice}}</span>
								</td>
								<td><span>{{item.dmpData[0].recomment.costPerMills == null ? "--" : item.dmpData[0].recomment.costPerMills}}</span>
								</td>
							</tr>
						</template>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<el-pagination v-show="Math.floor(page.totalCount/page.pageSize)>0"
					   @size-change="pageSizeChange"
					   @current-change="currentPageChange"
					   :current-page="page.currentPage"
					   :page-sizes="page.pageSizes"
					   :page-size="page.pageSize"
					   layout="total, sizes, prev, pager, next"
					   :class="{'el-pagination-reset': true}"
					   :total="page.totalCount">
		</el-pagination>
		<el-dialog :title="title" v-model="dialogVisible" size="tiny">
			<span v-html="body_html"></span>
			<span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" v-show='showCancel'>取 消</el-button>
        <el-button type="primary" @click="sureFn">确 定</el-button>
      </span>
		</el-dialog>
	</div>
</template>
<script>
	import Vue from "vue";
	import actions from 'actions';
	import Event from 'event';
	import CONST from '../../../../help/CONST.js';
	import Api from "../../../../config/api.config";
	import http from "../../../../utils/http";
	import {
		copyObj
	} from 'utils/common';
	// import Dialog from "../../../common/dialog";
	import {formatDate, mixin, isEmptyObj} from 'utils/common';
	import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';

	export default {
		name: 'app-put-bidcpc-unit-list',
		data() {
			return {
				showOpationHint: false,
				summary: {
					name: "",
					startTime: 0,
					endTime: 0,
					state: "2"
				},
				so: {
					keyword: "",
					startTime: new Date(),
					endTime: new Date(),
					state: {
						options: [{
							label: "全部",
							value: "0"
						}, {
							label: "暂停",
							value: "1"
						}, {
							label: "启动",
							value: "2"
						}],
						value: "0"
					},
				},
				list: [],
				page: {
					totalCount: 0,
					currentPage: 1,
					pageSizes: [20, 30, 50],
					pageSize: 30
				},
				rules: {
					startTime: [{
						validator: (rule, value, callback) => {
							if (+this.so.endTime != 0 && +this.so.startTime > +this.so.endTime) {
								callback(new Error("结束时间不能早于开始时间"));
							} else {
								callback();
							}
						},
						trigger: "change"
					}]
				},
				dialogVisible: false,
				title: '提示',
				body_html: '',
				btn_state: '',
				showCancel: true,
				campaignId: ''
			};
		},
		components: {
			// 'dialog': Dialog
		},
		computed: {
			flightIds(){
				var flightIds = [], list = this.list;
				for (var item of list) {
					if (item.isActived) {
						flightIds.push(item.flightId);
					}
				}
				return flightIds;
			},
			isActived(){
				var isActived = true, list = this.list;
				for (var item of list) {
					if (item.isActived) {
						continue;
					}
					isActived = false;
					break;
				}
				return isActived;
			},
			stopStates(){
				var states = this.selectedUnit, num;
				if (states.length == 1) {
					if (states[0] == 1) {
						num = 1;//暂停
					} else {
						num = 2;//启用
					}
				} else {
					for (let i = 0; i < states.length; i++) {
						if (states[i] != 1) {
							num = 3;//全是启用
						} else {
							num = 4;//混合
							break;
						}
					}
				}
				return num;
			},
			startDelStates(){
				var states = this.selectedUnit, num;
				if (states.length == 1) {
					if (states[0] == 1) {
						num = 1;//暂停
					} else {
						num = 2;//启用
					}
				} else {
					for (let i = 0; i < states.length; i++) {
						if (states[i] == 1) {
							num = 3;//全是暂停
						} else {
							num = 4;//混合
							break;
						}
					}
				}
				return num;
			},
			selectedUnit(){
				var states = [], list = this.list;
				for (var item of list) {
					if (item.isActived) {
						states.push(item.state);
					}
				}
				return states;
			}
		},
		created(){
			var that = this;
			Event.$off("put-save-success");
			Event.$on("put-save-success", function () {
				that.getList();
			});
			localStorage.setItem("campaignId", this.$route.params.id);
			this.campaignId = this.$route.params.id;
			this.getSummary();
			this.getList();
		},
		mounted() {
			initWindowResize('mainDataTable', true, 4);
		},
		updated() {
			tableHandler('mainDataTable', true, 4);
		},
		destroyed() {
			offWindowEvent('mainDataTable');
		},
		methods: {
			checkbox(flightId){
				var list = this.list;
				for (var item of list) {
					if (item.flightId == flightId) {
						item.isActived = !item.isActived;
					}
				}
			},
			checkall(){
				var list = this.list, isActived = this.isActived;
				for (var item of list) {
					item.isActived = !isActived;
				}
			},
			getSummary(){
				var summary = this.summary;
				http.get("/api/campaign/brief", {
					params: {
//						campaignId: this.$route.params.id
						campaignId: this.campaignId
					}
				})
					.then(function (res) {
						if (res.data.code == 200) {
							var data = res.data.data;
							summary.name = data.name;
							summary.startTime = formatDate(data.startTime, "yyyy/MM/dd");
							summary.endTime = data.isUnlimited ? "不限" : formatDate(data.endTime, "yyyy/MM/dd");
							summary.state = data.state == 0 ? "全部" : (data.state == 1 ? "暂停" : (data.state == 2 ? "有效" : (data.state == 3 ? "过期" : "未开始")));
						}
					})
					.catch(function (error) {
						console.log(error);
					});
			},
			search(){
				var that = this;
				this.$refs.formlist.validate(function (valid) {
					if (!valid) {
						return;
					}
					that.getList();
				});
			},
			getList(){
				var so = this.so, that = this;
				http.get("/api/flights", {
					params: {
//						campaignId: this.$route.params.id,
						campaignId: this.campaignId,
						keyword: so.keyword,
						startTime: so.startTime ? +so.startTime : 0,
						endTime: so.endTime ? +so.endTime : 0,
						state: so.state.value,
						page: this.page.currentPage,
						number: this.page.pageSize,
						productLine: 3
					}
				})
					.then(function (res) {
						if (res.data.code == 200) {
							that.list = res.data.data.list;
							that.page.totalCount = res.data.data.totalCount;
							for (var item of that.list) {
								Vue.set(item, "isActived", false);
								Vue.set(item, "timeMore", false);
								item.startTime = formatDate(item.startTime, "yyyy/MM/dd");
								item.endTime = formatDate(item.endTime, "yyyy/MM/dd");
								if (Array.isArray(item.schedule) && item.schedule.length) {
									var arr = [];
									for (var schedule of item.schedule) {
										var temp = [];
										for (var date of schedule) {
											temp.push(formatDate(+date, "yyyy/MM/dd"));
										}
										arr.push(temp.join("-"));
									}
									item.schedule = arr;
								}
							}
						}
					})
					.catch(function (error) {
						alert("list fail");
					});
			},
			sureFn(){
				if(this.btn_state == 'start'){
					var that = this, list = this.list;
					if(!this.flightIds.length){
						return;
					}
					http.put("/api/flights/status", {
						flightIds: this.flightIds,
						status: 1
					})
						.then(function(res) {
							if(res.data.code == 200){
								that.getList();
							}
						})
						.catch(function(error) {
							alert("fail");
						});
				}else if(this.btn_state == 'stop'){
					var that = this, list = this.list;
					if(!this.flightIds.length){
						return;
					}
					http.put("/api/flights/status", {
						flightIds: this.flightIds,
						status: 0
					})
						.then(function(res) {
							if(res.data.code == 200){
								that.getList();
							}
						})
						.catch(function(error) {
							alert("stop fail");
						});
				}else if(this.btn_state == 'delete'){
					var that = this, list = this.list;
					if(!this.flightIds.length){
						return;
					}
					http.delete("/api/flights", {
						data:{
							flightIds: this.flightIds
						}
					})
						.then(function(res) {
							if(res.data.code == 200){
								for(var id of that.flightIds){
									for(var i = 0, len = list.length; i < len; i++){
										if(id == list[i].flightId){
											list.splice(i, 1);
											break;
										}
									}
								}
								that.getList();
							}
						})
						.catch(function(error) {
							alert("fail");
						});
				} else if (this.btn_state == 'cancel') {
					this.dialogVisible=false;
				}
				this.dialogVisible=false;
			},
			toastChangeHint(condition, type) {
				var hintStr = '';
				if (condition == 1) {
					hintStr = type == 'start' ? `<h3>确定启动该投放单元吗？</h3>` : (type == 'stop' ? `<h3>仅“启动”状态的投放单元才可以暂停。</h3>` : (type == 'delete' ? `<h3>确定删除该投放单元吗？</h3>` : ''));
					this.btn_state = type == 'start' ? 'start' : (type == 'stop' ? 'cancel' : (type == 'delete' ? 'delete' : ''));
					this.showCancel = type == 'start' ? true : (type == 'stop' ? false : (type == 'delete' ? true : ''));
				} else if (condition == 2) {
					hintStr = type == 'start' ? `<h3>仅“暂停”状态的投放单元才可以启动。</h3>` : (type == 'stop' ? `<h3>确定暂停该投放单元吗？</h3>` : (type == 'delete' ? `<h3>仅“暂停”状态的投放单元才可以删除。</h3>` : ''));
					this.btn_state = type == 'start' ? 'cancel' : (type == 'stop' ? 'stop' : (type == 'delete' ? 'cancel' : ''));
					this.showCancel = type == 'start' ? false : (type == 'stop' ? true : (type == 'delete' ? false : ''));
				} else if (condition == 3) {
					hintStr = type == 'start' ? `<h3>已选择${this.flightIds.length}个投放单元，确定全部启动吗？</h3>` : (type == 'stop' ? `<h3>已选择${this.flightIds.length}个投放单元，确定全部暂停吗？</h3>` : (type == 'delete' ? `<h3>已选择${this.flightIds.length}个投放单元，确定全部删除吗？</h3>` : ''));
					this.btn_state = type == 'start' ? 'start' : (type == 'stop' ? 'stop' : (type == 'delete' ? 'delete' : ''));
					this.showCancel = type == 'start' ? true : (type == 'stop' ? true : (type == 'delete' ? true : ''));
				} else if (condition == 4) {
					hintStr = type == 'start' ? `<h3>已选择${this.flightIds.length}个投放单元，其中仅“暂停”状态的单元才可以启动。请重新选择。</h3>` : (type == 'stop' ? `<h3>已选择${this.flightIds.length}个投放单元，其中仅“启动”状态的单元才可以暂停。请重新选择。</h3>` : (type == 'delete' ? `<h3>已选择${this.flightIds.length}个投放单元，其中仅“暂停”状态的单元才可以删除。请重新选择。</h3>` : ''));
					this.btn_state = type == 'start' ? 'cancel' : (type == 'stop' ? 'cancel' : (type == 'delete' ? 'cancel' : ''));
					this.showCancel = type == 'start' ? false : (type == 'stop' ? false : (type == 'delete' ? false : ''));
				}
				this.dialogVisible = true;
				this.body_html = hintStr;
			},
			start(){
				var that = this, list = this.list;
				if (!this.flightIds.length) {
					this.showOpationHint = true;
				} else {
					this.toastChangeHint(this.startDelStates, 'start');
				}
			},
			stop(){
				var that = this, list = this.list;
				if (!this.flightIds.length) {
					this.showOpationHint = true;
				} else {
					this.toastChangeHint(this.stopStates, 'stop');
				}
			},
			del(){
				var that = this, list = this.list;
				if (!this.flightIds.length) {
					this.showOpationHint = true;
				} else {
					this.toastChangeHint(this.startDelStates, 'delete');
				}
			},
			newUnit() {
				if (this.page.totalCount == 20) {
					this.showCancel = false;
					this.dialogVisible = true;
					this.body_html = '<h3>一个投放计划下最多可创建20个投放单元</h3>';
					this.btn_state = 'cancel';
				} else {
					var id = this.$route.params.id, that = this;
					http.get("/api/campaign", {
						params: {
//							campaignId: id
							campaignId: that.campaignId
						}
					})
						.then(function (res) {
							if (res.data.code == 200) {
								actions.controlDrawer(that.$store, {
									show: true,
									action: 'new',
									type: 'unit',
									config: mixin({
//										campaignId: id
										campaignId: that.campaignId
									}, CONST.DRAWERUNIT),
									isRebate: res.data.data.isRebate
								});
							}
						})
						.catch(function (error) {
							console.log(error);
						});
				}
			},
			newIdea(id, flightType) {
				var that = this;
				http.get('/api/materials', {
					params: {
						flightId: id
					}
				})
					.then((res) => {
						if (res.data.code == 200) {
							if (res.data.data.totalCount == 50) {
								this.showCancel = false;
								this.dialogVisible = true;
								this.body_html = '<h3>一个投放单元下最多可创建50个创意</h3>';
								this.btn_state = 'cancel';
							} else {
								http.get("/api/campaign", {
									params: {
										// campaignId: this.$route.params.id
										campaignId: that.campaignId
									}
								})
									.then(function (res) {
										if (res.data.code == 200) {
											if (flightType == 1) {
												//商品推广
												actions.controlDrawer(that.$store, {
													show: true,
													action: 'new',
													type: 'idea',
													config: mixin({
														flightId: id
													}, CONST.DRAWERIDEA),
													isRebate: res.data.data.isRebate
												});
											} else if (flightType == 2) {
												//活动推广
												actions.controlDrawer(that.$store, {
													show: true,
													action: 'new',
													type: 'idea_url',
													config: mixin({
														flightId: id
													}, CONST.DRAWERIDEA),
													isRebate: res.data.data.isRebate
												});
											}
										}
									})
									.catch(function (error) {
										console.log(error);
									});
							}
						}
					})
					.catch((error) => {
						console.log(error);
					})
			},
			modify(campaignId){
				this.getData(campaignId, "modify");
			},
			copy(campaignId){
				if (this.page.totalCount == 20) {
					this.showCancel = false;
					this.dialogVisible = true;
					this.body_html = '<h3>一个投放计划下最多可创建20个投放单元</h3>';
					this.btn_state = 'cancel';
				} else {
					this.getData(campaignId, "copy");
				}
			},
			getData(id, type){
				var that = this;
				http.get("/api/flight", {
					params: {flightId: id}
				})
					.then(function (res) {
						if (res.data.code == 200) {
							let data = res.data.data;
							let times = [];
							let days = [];
							for (let i = 0; i < 24; i++) {
								days.push({
									sel: false,
									mov: false,
								})
							}
							for (let i = 0; i < 7; i++) {
								times.push({
									on: false,
									weeks: i,
									time: copyObj(days)
								})
							}
							let time = res.data.data.time;
							for (let i = 0; i < times.length; i++) {
								times[i].time.forEach((item, index) => {
									item.sel = time[i + 1].indexOf(index) > -1;
								})
							}
							data.time = times;
							var dataPackage = {
								show: true,
								action: type,
								type: 'unit',
								config: data
							};
							actions.controlDrawer(that.$store, dataPackage);
						}
					})
					.catch(function (error) {
						console.log(error);
					});
			},
			pageSizeChange(size){
				this.page.pageSize = size;
				this.getList();
			},
			currentPageChange(page){
				this.page.currentPage = page;
				this.getList();
			},
			show(flightId){
				var list = this.list;
				for (var item of list) {
					if (item.flightId == flightId && item.schedule.length) {
						item.timeMore = true;
					}
				}
			},
			hide(flightId){
				var list = this.list;
				for (var item of list) {
					if (item.flightId == flightId) {
						item.timeMore = false;
					}
				}
			}
		},
		watch: {
			'flightIds': function () {
				if (this.flightIds.length) {
					this.showOpationHint = false;
				}
			}
		}
	};
</script>
<style scope scoped>
	.table {
		width: 100%;
		border-collapse: collapse;
		border: 1px solid #f0f3f6;
	}

	.table tr th {
		background: #f0f3f6;
		color: #596069;
		font-weight: normal;
	}

	.table tr:hover {
		background: none !important;
	}

	.table span.span-col-1 .icon {
		cursor: pointer;
		margin-left: -27px;
		margin-right: 10px;
		color: #b3babf;
	}

	.table tr th, .table tr td {
		padding: 0;
		margin: 0;
		border-collapse: collapse;
		border: 1px solid #e1e3e6;
		color: #23272c;
		vertical-align: middle;
	}

	.table tr th span, .table tr td span {
		display: block;
		text-align: center;
		line-height: 20px;
		padding: 6px 0px;
		margin: 0px 4px;
		font-size: 14px;
	}

	.table span .operat-link a {
		display: inline-block;
		white-space: nowrap;
		padding-right: 18px;
	}

	.table .icon-select {
		color: #ccc;
		cursor: pointer;
		font-size: 16px;
	}

	.table .list-header.actived .icon-select, .table .body-row.actived .icon-select {
		color: #d30312;
	}

	.data-id {
		font-style: normal;
	}

	.el-dialog__body h3 {
		font-weight: normal;
		line-height: 30px;
		font-size: 16px;
		color: #23272c;
	}

	.el-dialog__body p {
		padding-top: 15px;
		font-size: 14px;
		line-height: 24px;
		color: #89919c;
	}
</style>
