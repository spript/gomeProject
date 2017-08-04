<template>
	<div class="account-main account-main01">
		<div class="amp-path"><span class="path-col">{{title}}</span></div>
		<div class="main-wrap">
			<div class="record-consume">
				<div class="cont-list">
					<table class="table">
						<thead>
							<tr>
								<th :width="120"><span>计划名称</span></th>
								<th :width="120"><span>账户</span></th>
								<th :width="120"><span>支出(元)</span></th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="items in data">
								<td v-for="(item, i) in items">
									<span>{{item}}</span>
								</td>
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
			:class="{'el-pagination-reset': true}"
			:total="page.totalCount">
		</el-pagination>
	</div>
</template>
<script type="text/javascript">
import http from "http";
import {formatDate,mixin} from 'utils/common';

export default {
	name: "app-account-expense-daily",
	data(){
		return {
			data: {},
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			}
		};
	},
	created() {
		this.getList();
	},
	computed: {
		date() {
			return this.$route.params.date;
		},
		title() {
			// @todo check date
			var date = this.$route.params.date;
			return formatDate(date, "yyyy年MM月dd日") + "费用支出明细";
		}
	},
	methods: {
		getList(){
			http.get("/api/account/expense/daily", {
				params: {
					time: new Date(this.date).valueOf(),
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			})
			.then((res) => {
				if(res.data.code == 200){
					this.data = res.data.data.list.data;
					this.page.totalCount = res.data.data.totalCount;
				}
			})
			.catch(function(error){
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
		exportList(){

		}
	},
	watch: {
		date(){
			this.getList();
		}
	}
};

</script>
