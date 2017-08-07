<template>
	<div>
		<div class="account-set">
			<div class="set-fn">
				<div class="fn-plan set-fn-new">
					<span class="plan-title">广告主名称：</span>
					<el-input v-model="search.advertiserName" placeholder="请输入广告位名称" style="width:140px;">
					<i slot="icon"
						class="el-input__icon el-icon-close"
						@click="handleClickIcon()"
						v-if="isAdvertiserName">
					</i>
					</el-input>
				</div>
				<div class="fn-plan">
					<span class="plan-title">送审时间：</span>
					<el-date-picker v-model="searchDateRange" type="daterange" align="right" placeholder="选择日期范围" :picker-options="datePickOptions" :editable="false" style="width:220px;"></el-date-picker>
				</div>
				<div class="fn-search"><span class="btn btn-primary" @click="searchHandle">查询</span></div>
			</div>
		</div>
		<div class="amp-data">
			<div class="data-table">
				<el-table :data="list" style="width: 100%" border>
					<el-table-column label="广告主名称" width="140" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.companyName">
								<i class="ellipsis">{{ scope.row.companyName }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column label="账号类型" width="140" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.companyNature">
								<i class="ellipsis">{{ scope.row.companyNature }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="送审时间" width="130" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="approveTime" label="操作时间" width="140" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="accountName" label="操作人" width="90" :resizable="false"></el-table-column>
					<el-table-column label="审核操作" width="120" :resizable="false">
						<template scope="scope">
							<i class="el-icon-search" style="padding:5px;cursor:pointer;" @click="handleView(scope.$index, scope.row)"></i>
							<i v-if="approveStatus !== 1" class="el-icon-check" style="padding:5px;cursor:pointer;" @click="handlePass(scope.$index, scope.row)"></i>
							<i v-if="approveStatus === 0" class="el-icon-close" style="padding:5px;cursor:pointer;margin-left:10px;" @click="handleRefuse(scope.$index, scope.row)"></i>
					      </template>
					</el-table-column>
				</el-table>
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
		 <m-view v-if="showChildComponents.showAdvertiserView" :showView="showChildComponents.showAdvertiserView" :advertiserId="viewAdvertiserId" :approveStatus="approveStatus"></m-view>
		 <m-dialog :showDialog="showChildComponents.showAdvertiserPass" :advertiserId="viewAdvertiserId"></m-dialog>
		 <m-refuse :showRefuse="showChildComponents.showAdvertiserRefuse" :advertiserId="viewAdvertiserId" ></m-refuse>
		</div>
</template>
<script>
import Vue from 'Vue';
import Http from 'utils/http';
import Event from 'event';
import { formatDate, debounce } from 'utils/common';
import advertiserView from './advertiserView.vue';
import passDialog from './makeSureDialog.vue';
import advertiserRefuse from './advertiserRefuse.vue';
export default {
	name:'app-advertiser-not-list',
	data() {
		return {
			showChildComponents: {
				showAdvertiserView: false,
				showAdvertiserPass: false,
				showAdvertiserRefuse: false
			},
			viewAdvertiserId:'',
			order: {
				order: '',
				orderBy: ''
			},
			search: {
				advertiserName: '',
				startTime: '',
				endTime: '',
				orderBy: '',
				order: ''
			},
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			},
			list: [],
			searchDateRange:'',
			datePickOptions: {
				disabledDate(time) {
					return time.getTime() > Date.now();
				}
			}
		};
	},
	mounted() {
		let vm = this;
		Event.$on('close-view', () => {
			vm.showChildComponents.showAdvertiserView = false;
			document.body.style.overflow = 'auto';
		});

		Event.$on('close-dialog', () => {
			vm.showChildComponents.showAdvertiserPass = false;
		});

		Event.$on('pass-advertiser', () => {
			vm.showChildComponents.showAdvertiserView = false;
			vm.showChildComponents.showAdvertiserPass = false;
			this.$message({
				message: '广告主审核通过成功！',
				type: 'success',
				duration: 3000
			});
			vm.getList();
		});

		Event.$on('close-refuse', () => {
			vm.showChildComponents.showAdvertiserRefuse = false;
		});
		Event.$on('show-dialog', () => {
			vm.showChildComponents.showAdvertiserPass = true;
		});
		Event.$on('show-refuse', () => {
			vm.showChildComponents.showAdvertiserRefuse = true;
		});

		Event.$on('refuse-advertiser', () => {
			vm.showChildComponents.showAdvertiserRefuse = false;
			vm.showChildComponents.showAdvertiserView = false;
			vm.getList();
		});
	},
	computed: {
		isAdvertiserName() {
			return this.search.advertiserName.length > 0;
		},
		approveStatus() {
			let route = this.$route;
			return route.name === 'advertiser-not' ? 0 : (route.name === 'advertiser-passed' ? 1 : -1);
		},
		orderType() {
			return this.order.order === 'ascending' ? 'asc' : (this.order.order === '' ? '' : 'desc');
		},
	},
	created() {
		if (this.approveStatus === 0 ) {
			this.search.order = 'asc';
			this.search.orderBy = 'createTime';
		} else {
			this.search.order = 'desc';
			this.search.orderBy = 'approveTime';
		}
		this.getList();
	},
	components: {
		'm-view': advertiserView,
		'm-dialog': passDialog,
		'm-refuse': advertiserRefuse
	},
	methods: {
		pageSizeChange(size) {
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page) {
			this.page.currentPage = page;
			this.getList();
		},
		handleClickIcon() {
			this.search.advertiserName = '';
		},
		searchHandle() {
			this.getList();
		},
		handleView(index, item) {
			this.viewAdvertiserId = item.advertiserId;
			this.showChildComponents.showAdvertiserView = true;
			if (this.showChildComponents.showAdvertiserView = true) {
				document.body.style.overflow = 'hidden';
			}
		},
		handlePass(index, item) {
			this.viewAdvertiserId = item.advertiserId;
			this.showChildComponents.showAdvertiserPass = true;
		},
		handleRefuse(index, item) {
			this.viewAdvertiserId = item.advertiserId;
			this.showChildComponents.showAdvertiserRefuse = true;
			document.body.style.overflow = 'hidden';
		},
		renderHeader(h, {column, $index}) {
			if (column.label === '送审时间') {
				column.order = this.order.orderBy === 'createTime' ? this.order.order : '';
			} else if (column.label === '操作时间') {
				column.order = this.order.orderBy === 'approveTime' ? this.order.order : '';
			}
			return h('div', {
				on: {
					click: () => { this.handleOrderClick($index, column.label); }
				}
			}, [
					column.label,
					h('span', {'class': 'caret-wrapper'}, [
								h('i', {'class': 'sort-caret ascending'}),
								h('i', {'class': 'sort-caret descending'})
					])
				]);
		},
		handleOrderClick(index, label) {
			if (label === '送审时间' && this.order.orderBy !== 'createTime'){
				this.order.orderBy = 'createTime';
				this.order.order = 'ascending';
			} else if(label === '操作时间' && this.order.orderBy !== 'approveTime') {
				this.order.orderBy = 'approveTime';
				this.order.order = 'ascending';
			} else {
				this.order.order = this.order.order === 'ascending' ? 'descending' : 'ascending';
			}
			this.search.order = this.orderType;
			this.search.orderBy = this.order.orderBy;
			this.getList();
		},
		timeClearHours(date) {
			const cloneDate = new Date(date);
		    cloneDate.setHours(0, 0, 0, 0);
		    return cloneDate.getTime();
		},
		setTimeHours(date) {
			const cloneDate = new Date(date);
			cloneDate.setHours(23, 59, 59, 999);
			return cloneDate.getTime();
		},
		getList() {
			let search = this.search, vm = this;
			Http.get('/api/approval/advertisers', {
				params: {
					advertiserName: search.advertiserName,
					startTime: search.startTime ? search.startTime : '',
					endTime: search.endTime ? search.endTime : '',
					page: vm.page.currentPage,
					number: vm.page.pageSize,
					approveStatus: vm.approveStatus,
					order: !!search.order ? search.order : 'asc',
					orderBy: !!search.orderBy ? search.orderBy : 'createTime'
				}
			}).then((res) => {
				vm.list = [];
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.list = res.data.data.list;
					vm.list.forEach((item, index) => {
						item.startTime = formatDate(item.startTime, 'yyyy.MM.dd');
						item.endTime = formatDate(item.endTime, 'yyyy.MM.dd');
						item.createTime = formatDate(item.createTime, 'yyyy年MM月dd日');
						item.approveTime = formatDate(item.approveTime, 'yyyy年MM月dd日');
						item.companyNature = item.companyNature == 1 ? '品牌商' : (item.companyNature == 2 ? '代理商' : '入驻商家');
					});
					vm.page.totalCount = res.data.data.totalCount;
				} else {
					vm.$message({
						message: res.data.msg,
						type: 'error'
					});
				}
			}).catch((error) => {
				console.log(error);
			});
		},
	},
	watch: {
		'searchDateRange': {
			handler: function(val) {
				if (!val[0] && !val[1]) {
					this.search.startTime = '';
					this.search.endTime = '';
				} else {
					let start = new Date(val[0]);
					let end = new Date(val[1]);
					this.search.startTime = this.timeClearHours(start);
					this.search.endTime = this.setTimeHours(end);
				}
			},
			deep: true
		},
		'approveStatus': function(val) {
			this.search = {
				advertiserName: '',
				startTime: '',
				endTime: '',
				orderBy: '',
				order: ''
			};
			this.searchDateRange = '';
			let route = this.$route;
			this.advertiserId = [];
			this.search.order = this.approveStatus === 0 ? 'asc' : 'desc';
			this.search.orderBy = this.approveStatus === 0 ? 'createTime' : 'approveTime';
			this.getList();
		}
	}

};
</script>
<style>
table{width: 100% !important;}
.btn{margin-bottom: 20px;}
.ellipsis{font-style: normal;}
.amp-data{margin-top: 0;}
.account-set{margin-top: 40px;}
.fn-plan{margin-bottom:20px;}
</style>
