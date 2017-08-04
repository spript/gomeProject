<template>
	<div>
		<div class="account-tab">
			<router-link :to="{name: 'bidcpc-plan-campaign'}" class="active" tag="span">广告报表</router-link>
			<router-link :to="{name: 'bidcpc-effect-order'}" tag="span">效果报表</router-link>
		</div>
		<div class="account-set account-set-new">
			<div class="set-tab">
				<router-link :to="{name: 'bidcpc-plan-campaign'}" active-class="active" tag="span">投放计划</router-link>
				<router-link :to="{name: 'bidcpc-plan-flight'}" active-class="active" tag="span">投放单元</router-link>
				<router-link :to="{name: 'bidcpc-plan-material'}" active-class="active" tag="span">创意</router-link>
				<router-link :to="{name: 'bidcpc-plan-keywords'}" active-class="active" tag="span">关键词</router-link>
			</div>

			<div class="set-fn">
				<el-form :model="formData" :rules="rules" ref="formData">
					<div class="fn-plan">
						<el-form-item prop="startTime" style="display:inline-block;margin-bottom: 0;">
							<span class="plan-title">起止日期：</span>
							<el-date-picker v-model="formData.startTime" type="date" :editable=false style="width:130px;" placeholder="开始日期">
							</el-date-picker>
							<span class="plan-title plan-title-gray">-</span>
							<el-date-picker v-model="formData.endTime" type="date" :editable=false style="width:130px;" placeholder="结束日期">
							</el-date-picker>
						</el-form-item>
					</div>
				</el-form>
				<div class="fn-plan">
					<div class="fn-plan fn-plan-new"><span class="plan-title">投放计划</span>
						<el-select v-model="selectedDspCampaignId" clearable style="width:130px;display:inline-block;" placeholder="全部投放计划">
							<el-option label="全部投放计划" value="0">全部投放计划</el-option>
							<el-option
								v-for="campaign in campaigns"
								:label="campaign.name"
								:value="campaign.dspCampaignId">
							</el-option>
						</el-select>
					</div>
					<div class="fn-plan fn-plan-new"><span class="plan-title">投放单元</span>
						<el-select v-model="selectedDspFlightId" clearable style="width:130px;display:inline-block;" placeholder="全部投放单元">
							<el-option label="全部投放单元" value="0">全部投放单元</el-option>
							<el-option
									   v-for="flight in flights"
									   :label="flight.name"
									   :value="flight.dspFlightId">
							</el-option>
						</el-select>
					</div>
				</div>
				<div class="fn-plan" style="margin-top:20px;width:100%;position:relative;">
					<div class="fn-plan fn-plan-new"><span class="plan-title">&nbsp;&nbsp;&nbsp;&nbsp;设备类型</span>
						<el-select v-model="deviceType" clearable style="width:130px;display:inline-block;" placeholder="全部">
							<el-option label="全部" value="0">全部</el-option>
							<el-option label="APP" value="1">APP</el-option>
							<el-option label="WAP" value="2">WAP</el-option>
							<el-option label="PC" value="3">PC</el-option>
						</el-select>
					</div>
					<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="search" style="width:130px">查询</a></div>
					<div style="float:right;font-size:16px;letter-spacing:3px;position:absolute;bottom:0;right:0;"><a :href="exportExcel" style="margin-bottom:0px;display:block;"><em class="icon icon-report"></em>导出</a></div>
				</div>
			</div>

			<div class="table-content">
				<table class="table">
					<thead>
						<tr>
							<th v-for="item in header" :width="120"><span>{{item}}</span></th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="items in data">
							<td v-for="item in items"><span>{{item}}</span></td>
						</tr>
					</tbody>
				</table>
			</div>
			<el-pagination v-show="Math.floor(page.totalCount/page.pageSize)>0"
				@size-change="pageSizeChange"
				@current-change="currentPageChange"
				:current-page="page.currentPage"
				:page-sizes="page.pageSizes"
				:page-size="page.pageSize"
				layout="total, sizes, prev, pager, next"
				:total="page.totalCount"
				:class="{'el-pagination-reset': true}"
				>
			</el-pagination>
		</div>
	</div>
</template>
<script>
import http from "../../../../utils/http";
import apiConfig from '../../../../config/api.config.js';
export default {
	name: "app-report-bidcpc-plan-keywords",
	data() {
		return {
			formData: {
				startTime: '',
				endTime: '',
			},
			header: {},
			data: {},
			campaigns: {},
			selectedDspCampaignId:'',
			flights: {},
			selectedDspFlightId: '',
			deviceType: '0',
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			},
			rules: {
				startTime: [{
					validator: (rule, value, callback) => {
						if (+this.formData.startTime > +this.formData.endTime) {
							callback(new Error("结束时间不能早于开始时间"));
						} else {
							callback();
						}
					},
					trigger: "change"
				}],
				endTime: [{
					validator: (rule, value, callback) => {
						if (+this.formData.startTime > +this.formData.endTime) {
							callback(new Error("结束时间不能早于开始时间"));
						} else {
							callback();
						}
					},
					trigger: "change"
				}]
			}
		};
	},
	created() {
		this.getList();
		this.getCampaignsAndFlightsList();
	},
	computed:{
	  exportExcel:function() {
	     let url = apiConfig[process.env.NODE_ENV]+'/api/report/anice/keywords/export';
		 let startTime= this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0;
		 let endTime= this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0;
		 let campaignId = this.selectedDspCampaignId;
		 let flightId = this.selectedDspFlightId;
		 let deviceType = this.deviceType;
	     return url +'?productLine=3&startTime='+startTime+'&endTime='+endTime+'&campaignId='+campaignId+'&flightId='+flightId+ '&deviceType=' + deviceType;
	  }
	},
	methods: {
		getCampaignsAndFlightsList(){
			http('api/campaigns/flights/all?productLine=3', {
				method: 'get',
			})
				.then((res) => {
				if (res.data.code == 200) {
				this.campaigns = res.data.data.list;
			}
		})
		.catch(function(error) {
				console.log(error);
			});
		},
		getList() {
			http('api/report/anice/keywords', {
				method: 'get',
				params: {
					startTime: this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0,
					endTime: this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0,
					page: this.page.currentPage,
					number: this.page.pageSize,
					campaignId: this.selectedDspCampaignId,
					flightId: this.selectedDspFlightId,
					productLine: "3",
					deviceType: this.deviceType

				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					this.header = res.data.data.list.header;
					this.data = res.data.data.list.data;
					this.page.totalCount = res.data.data.totalCount;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		},
		pageSizeChange(size) {
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page) {
			this.page.currentPage = page;
			this.getList();
		},
		search() {
			var that = this;
			this.$refs.formData.validate(function(valid){
				if (!valid) {
					return;
				}
				that.getList();
			});
		}
	},
	watch: {
		// 如果 selectedDspCampaignId 发生改变，这个函数就会运行
		selectedDspCampaignId: function(selectedDspCampaignId) {
			console.log(selectedDspCampaignId);
			if (selectedDspCampaignId == 0) {
				this.selectedDspFlightId = '0';
				this.flights = [];
				return;
			}
			this.flights = this.campaigns.filter(function(item) {
				return item.dspCampaignId == selectedDspCampaignId;
			})[0].flightList;
			if (this.flights == ""){
				this.selectedDspFlightId = '0';
				return;
			}
			this.selectedDspFlightId = '0';
		}
	},
};
</script>
