<template>
	<div>
		<div class="account-tab">
			<router-link :to="{name: 'bidcpc-plan-campaign'}" tag="span">广告报表</router-link>
			<router-link :to="{name: 'bidcpc-effect-order'}" class="active" tag="span">效果报表</router-link>
		</div>
		<div class="account-set account-set-new">
			<div class="set-tab">
				<router-link :to="{name: 'bidcpc-effect-order'}" active-class="active" tag="span">订单效果</router-link>
				<router-link :to="{name: 'bidcpc-effect-summary'}" active-class="active" tag="span">订单汇总</router-link>
			</div>
			<div class="set-fn">
				<el-form :model="formData" :rules="rules" ref="formData">
					<div class="fn-plan">
						<el-form-item prop="startTime" style="display:inline-block;">
							<span class="plan-title">下单日期：</span>
							<el-date-picker v-model="formData.startTime" type="date" :editable=false style="width:130px;" placeholder="开始日期">
							</el-date-picker>
							<span class="plan-title plan-title-gray">-</span>
							<el-date-picker v-model="formData.endTime" type="date" :editable=false style="width:130px;" placeholder="结束日期">
							</el-date-picker>
						</el-form-item>
					</div>
				</el-form>
				<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="search">查询</a></div>
				<div style="float:right;font-size:16px;letter-spacing:3px;padding-top:8px"><a :href="exportExcel"><em class="icon icon-report"></em>导出</a></div>
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
	name: "app-report-bidcpc-effect-order",
	data() {
		return {
			formData: {
				startTime: '',
				endTime: '',
			},
			header: {},
			data: {},
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
	},
	computed:{
	  exportExcel:function() {
	     let url = apiConfig[process.env.NODE_ENV]+'/api/report/effect/order/export';
		 let startTime= this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0;
		 let endTime= this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0;
	     return url +'?productLine=3&startTime='+startTime+'&endTime='+endTime;
	  }
	},
	methods: {
		getList() {
			http('api/report/effect/order', {
				method: 'get',
				params: {
					startTime: this.formData.startTime ? new Date(this.formData.startTime).valueOf() : 0,
					endTime: this.formData.endTime ? new Date(this.formData.endTime).valueOf() : 0,
					page: this.page.currentPage,
					number: this.page.pageSize,
					productLine: "3"
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
	}
};
</script>
