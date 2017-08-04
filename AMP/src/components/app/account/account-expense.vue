<template>
	<div class="account-main">
		<div class="tabs">
			<ul>
				<router-link :to="{name: 'account-expense'}" class="actived" tag="li"><a href="#">消费记录</a></router-link>
				<router-link :to="{name: 'account-income'}" tag="li"><a href="#">充值记录</a></router-link>
			</ul>
		</div>
		<div class="main-record">
			<!-- 消费记录-->
			<div class="record-consume">
				<!-- 查询-->
				<div class="set-fn">
					<div class="fn-plan">
						<span class="plan-title">日期：</span>
						<el-date-picker v-model="formData.time" type="date" :editable=false style="width:190px;" placeholder="查询日期">
						</el-date-picker>
					</div>
					<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="getList">查询</a></div>
				</div>
				<!-- 列表-->
				<div class="cont-list scroll-x">
					<table class="table">
						<thead>
							<tr>
								<th :width="120"><span>日期</span></th>
								<th :width="120"><span>账户</span></th>
								<th :width="120"><span>支出(元)</span></th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="items in data">
								<td v-for="(item, i) in items">
									<span v-if="i == 0">
										<router-link :to="{name: 'account-expense-daily', params:{date:item}}">{{item}}</router-link>
									</span>
									<span v-else-if="i != 2">{{item}}</span>
									<span v-else>{{item.toFixed(2)}}</span>
								</td>
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
					:class="{'el-pagination-reset': true}"
					:total="page.totalCount">
				</el-pagination>
			</div>
		</div>
	</div>
</template>
<script type="text/javascript">
import http from "http";
export default {
	name: "app-account-expense",
	data(){
		return {
			formData: {
				time: '',
			},
			data: {},
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			}
		};
	},
	created(){
		this.getList();
	},
	methods: {
		getList() {
			http('api/account/expense', {
				method: 'get',
				params: {
					time: this.formData.time ? new Date(this.formData.time).valueOf() : 0,
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			})
			.then((res) => {
				if(res.data.code == 200) {
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
		}
	}
};
</script>
