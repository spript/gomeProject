<template>
	<div>
		<div class="account-set account-set-new">
			<div class="set-tab">
				<router-link :to="{name: 'rebate-campaign'}" active-class="active" tag="span">投放计划</router-link>
				<router-link :to="{name: 'rebate-flight'}" active-class="active" tag="span">投放单元</router-link>
				<router-link :to="{name: 'rebate-material'}" active-class="active" tag="span">创意</router-link>
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
				<div class="fn-plan fn-plan-new"><span class="plan-title">投放计划：</span>
					<el-select v-model="selectedDspCampaignId" clearable style="width:130px;display:inline-block;" placeholder="全部投放计划">
						<el-option label="全部投放计划" value="0">全部投放计划</el-option>
						<el-option
							v-for="campaign in campaigns"
							:label="campaign.name"
							:value="campaign.dspCampaignId">
						</el-option>
					</el-select>
				</div>
				<div class="fn-plan fn-plan-new"><span class="plan-title">投放单元：</span>
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
				<div class="fn-plan fn-plan-new"><span class="plan-title">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;创意：</span>
					<el-select v-model="selectedDspMaterialId" clearable style="width:130px;display:inline-block;" placeholder="全部创意">
						<el-option label="全部创意" value="0">全部创意</el-option>
						<el-option
								   v-for="material in materials"
								   :label="material.name"
								   :value="material.dspMaterialId">
						</el-option>
					</el-select>
					<span class="plan-title" style = "margin-left:20px">返利类型：</span>
					<el-select v-model="formData.rebateType" placeholder="请选择" style="display:inline-block;width:130px;">
							<el-option label="全部" :value="0"></el-option>
							<el-option label="分享返" :value="1"></el-option>
							<el-option label="视频浏览返" :value="2"></el-option>
							<el-option label="调查问卷返" :value="3"></el-option>
					</el-select>
				</div>
				<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="search" style="width:130px">查询</a></div>
				<div style="float:right;font-size:16px;letter-spacing:3px;position:absolute;bottom:0;right:0;"><a :href="exportExcel" style="margin-bottom:0px;display:block;"><em class="icon icon-report"></em>导出</a></div>
				</div>
			</div>
		</div>
		<div class="amp-data">
			<div class="data-table" id="mainDataTable">
				<div class="main-table-wapper">
					<table class="table main-table">
						<thead>
							<tr class="list-header">
								<th v-for="item in header" :width="120"><span>{{item}}</span></th>
							</tr>
						</thead>
						<tbody>
							<tr class="body-row" v-for="items in data">
								<td v-for="item in items"><span>{{item}}</span></td>
							</tr>
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
			:total="page.totalCount"
			:class="{'el-pagination-reset': true}"
			>
		</el-pagination>
	</div>
</template>
<script>
import http from "../../../../utils/http";
import apiConfig from '../../../../config/api.config.js';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
export default {
	name: "app-report-rebate-material",
	data() {
		return {
			formData: {
				startTime: '',
				endTime: '',
				rebateType:0
			},
			header: {},
			data: {},
			campaigns: {},
			selectedDspCampaignId:'',
			flights: {},
			selectedDspFlightId: '',
			materials: {},
			selectedDspMaterialId:'',
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
		this.getCampaignsAndFlightsAndMaterialsList();
	},
	mounted() {
	    initWindowResize('mainDataTable', true, 4);
	},
    updated() {
    	if (this.header.length) {
    		tableHandler('mainDataTable', true, 4);
    	}
    },
    destroyed() {
        offWindowEvent('mainDataTable');
    },
	computed:{
	  exportExcel:function() {
	     let url = apiConfig[process.env.NODE_ENV]+'/api/report/rebate/material/export';
		 let startTime= this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0;
		 let endTime= this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0;
		 let campaignId = this.selectedDspCampaignId;
		 let flightId= this.selectedDspFlightId;
		 let materialId= this.selectedDspMaterialId;
		 let rebateType = this.formData.rebateType;
	     return url +'?startTime='+startTime+'&endTime='+endTime+'&campaignId='+campaignId+'&flightId='+flightId+'&materialId='+materialId+'&rebateType='+rebateType;
	  }
	},
	methods: {
		getCampaignsAndFlightsAndMaterialsList(){
			http('api/campaigns/flights/materials/all', {
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
			http('api/report/rebate/material', {
				method: 'get',
				params: {
					startTime: this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0,
					endTime: this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0,
					page: this.page.currentPage,
					number: this.page.pageSize,
					campaignId: this.selectedDspCampaignId,
					flightId: this.selectedDspFlightId,
					materialId: this.selectedDspMaterialId,
					rebateType:this.formData.rebateType
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
				this.flights = [];
				this.selectedDspFlightId = '0';
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
			console.log(this.flights);
		},
		// 如果 selectedDspFlightId 发生改变，这个函数就会运行
		selectedDspFlightId: function(selectedDspFlightId) {
			console.log(selectedDspFlightId);
			if (selectedDspFlightId == 0) {
				this.materials = [];
				this.selectedDspMaterialId = '0';
				return;
			}
			this.materials = this.flights.filter(function(item) {
				return item.dspFlightId == selectedDspFlightId;
			})[0].materialList;
			if (this.materials == ""){
				this.selectedDspMaterialId = '0';
				return;
			}
			this.selectedDspMaterialId = '0';
		}
	},
};
</script>
<style scoped>
.account-set-new{
	margin-top: 30px;
}
</style>
