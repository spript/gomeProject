<template>
	<div>
		<div class="account-set">
			<div v-if="approveStatus === 0 || approveStatus === -1" class="set-fn">
				<div class="fn-search">
					<span class="btn btn-normal" @click='listHandle'>批量通过</span>
				</div>
			</div>
			<div class="set-fn">
				<div class="fn-plan set-fn-new">
					<span class="plan-title">问卷ID/问卷名称：</span>
					<el-input v-model="search.keyword" placeholder="请输入问卷ID或名称" style="width:150px;">	
					</el-input>
				</div>
				<div class="fn-plan set-fn-new">
					<span class="plan-title">广告主名称：</span>
					<el-input v-model="search.advertiserName" placeholder="" style="width:140px;">
					<i slot="icon"
						class="el-input__icon el-icon-close"
						@click="handleClickIcon()"
						v-if="isAdvertiserName">
					</i>
					</el-input>
				</div>
				<div class="fn-plan">
					<span class="plan-title">送审时间：</span>
					<el-date-picker v-model="searchDateRange" type="daterange" align="right" placeholder="选择日期范围" :picker-options="datePickOptions" :editable="false" style="widows: 20px;0px;"></el-date-picker>
				</div>
				<div class="fn-search"><span class="btn btn-primary" @click="searchHandle">查询</span></div>
			</div>
		</div>
		<div class="amp-data">
			<div class="data-table">
				<el-table :data="list" style="width: 100%" border @selection-change="handleSelectionChange" @sort-change="handleSortChange">
					<el-table-column v-if="approveStatus === 0 || approveStatus === -1" type="selection" width="48" :resizable="false"></el-table-column>
					<el-table-column label="问卷ID" width="70" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.surveyId">
								<i class="ellipsis">{{ scope.row.surveyId }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column label="问卷名称" width="180" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.name">
								<i class="ellipsis">{{ scope.row.name }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column label="广告主名称/账号类型" width="140" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.companyName + '/' + scope.row.companyNature">
								<i class="ellipsis">{{ scope.row.companyName }}/{{ scope.row.companyNature }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="送审时间" width="140" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="approveTime" label="操作时间" width="140" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="approveAccountName" label="操作人" width="90" :resizable="false"></el-table-column>
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
		<m-view v-if="showChildComponents.showSurveyView" :showView="showChildComponents.showSurveyView" :surveyId="viewSurveyId" :approveStatus="approveStatus"></m-view>
		 <m-dialog :showDialog="showChildComponents.showSurveyPass" :passType="passType" :surveyIds="surveyIds"></m-dialog>
		 <m-refuse :showRefuse="showChildComponents.showSurveyRefuse" :surveyId="viewSurveyId" ></m-refuse>
	</div>
</template>
<script>
import Vue from 'Vue';
import Http from 'utils/http';
import Event from 'event';
import { formatDate, debounce } from 'utils/common';
import surveyView from './surveyView.vue';
import passDialog from './surveySureDialog.vue';
import surveyRefuse from './surveyRefuse.vue';
export default {
	name:'app-survey-not-list',
	data() {
		return {
			showChildComponents: {
				showSurveyView: false,
				showSurveyPass: false,
				showSurveyRefuse: false
			},
			order: {
				order: '',
				orderBy: ''
			},
			search: {
				keyword:'',
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
			passType: 'single',
			viewSurveyId:'',
			surveyIds: [],
		    mutilSurveys: [],
			list: [],
			searchDateRange:'',
			datePickOptions: {
				shortcuts: [{
					text: '今天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						picker.$emit('pick', [start, end]);
						console.log('click today');
					}
				}, {
					text: '最近3天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 2);
						picker.$emit('pick', [start, end]);
						console.log('click three days');
					}
				}, {
					text: '最近7天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
						picker.$emit('pick', [start, end]);
						console.log('clcik seven days');
					}
				}, {
					text: '最近15天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 14);
						picker.$emit('pick', [start, end]);
						console.log('clcik fifteen days');
					}
				}, {
					text: '全部',
					onClick(picker) {
						picker.$emit('pick', [null,null]);
						console.log('all');
					}
				}],
				disabledDate(time) {
					return time.getTime() > Date.now();
				}
			}
		};
	},
	mounted() {
		let vm = this;
		Event.$on('close-view', () => {
			vm.showChildComponents.showSurveyView = false;
			document.body.style.overflow = 'auto';
		});
		Event.$on('close-dialog', () => {
			vm.showChildComponents.showSurveyPass = false;
			if (vm.passType === 'single') {
				vm.surveyIds = [];
			}
		});
		Event.$on('close-refuse', () => {
			vm.showChildComponents.showSurveyRefuse = false;
			vm.surveyId = '';
			document.body.style.overflow = 'auto';
		});
		Event.$on('show-dialog', () => {
			vm.showChildComponents.showSurveyPass = true;
		});
		Event.$on('show-refuse', () => {
			vm.showChildComponents.showSurveyRefuse = true;
		});
		Event.$on('refuse-survey', () => {
			vm.showChildComponents.showSurveyRefuse = false;
			vm.showChildComponents.showSurveyView = false;
			document.body.style.overflow = 'auto';
			vm.getList();
		});
		Event.$on('pass-survey', () => {
			vm.showChildComponents.showSurveyView = false;
			vm.showChildComponents.showSurveyPass = false;
			document.body.style.overflow = 'auto';
			vm.getList();
		});
	},
	computed: {
		isAdvertiserName() {
			return this.search.advertiserName.length > 0;
		},
		approveStatus() {
			let route = this.$route;
			return route.name === 'survey-not' ? 0 : (route.name === 'survey-passed' ? 1 : -1);
		},
		orderType() {
			return this.order.order === 'ascending' ? 'asc' : (this.order.order === '' ? '' : 'desc');
		},
	},
	created() {
		if (this.approveStatus === 0 ) {
			this.search.order = 'asc';
			this.search.orderBy = 'updateTime';
		} else {
			this.search.order = 'desc';
			this.search.orderBy = 'approveTime';
		}
		this.getList();
	},
	components: {
		'm-view': surveyView,
		'm-dialog': passDialog,
		'm-refuse': surveyRefuse
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
			this.viewSurveyId = item.surveyId;
			this.surveyIds = [this.viewSurveyId];
			this.passType = 'single';
			this.showChildComponents.showSurveyView = true;
			if (this.showChildComponents.showSurveyView = true) {
				document.body.style.overflow = 'hidden';
			}
		},
		handleSelectionChange(val) {
			this.mutilSurveys = [];
			val.forEach((item,index) => {
				this.mutilSurveys.push(item.surveyId);
			});
		},
		handleSortChange(sort) {
			this.list = [];
			this.getList();
		},
		handlePass(index, item) {
			this.viewSurveyId = item.surveyId;
			this.surveyIds = [this.viewSurveyId];
			this.passType = 'single';
			this.showChildComponents.showSurveyPass = true;
		},
		handleRefuse(index, item) {
			this.viewSurveyId = item.surveyId;
			this.showChildComponents.showSurveyRefuse = true;
			document.body.style.overflow = 'hidden';
		},
		listHandle() {
			this.surveyIds = this.mutilSurveys;
			if (this.surveyIds.length > 0) {
				this.passType = 'mutil';
				this.showChildComponents.showSurveyPass = true;
			} else {
				this.$message({
					message: '您没有选中任何问卷！',
					type: 'warning'
				});
			}
		},
		renderHeader(h, {column, $index}) {
			if (column.label === '送审时间') {
				column.order = this.order.orderBy === 'updateTime' ? this.order.order : '';
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
			if (label === '送审时间' && this.order.orderBy !== 'updateTime'){
				this.order.orderBy = 'updateTime';
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
			Http.get('/api/surveys', {
				params: {
					keyword: search.keyword,
					advertiserName: search.advertiserName,
					startTime: search.startTime ? search.startTime : '',
					endTime: search.endTime ? search.endTime : '',
					page: vm.page.currentPage,
					number: vm.page.pageSize,
					approveStatus: vm.approveStatus,
					order: !!search.order ? search.order : 'asc',
					orderBy: !!search.orderBy ? search.orderBy : 'updateTime'
				}
			}).then((res) => {
				vm.list = [];
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.list = res.data.data.list;
					vm.list.forEach((item, index) => {
						item.startTime = formatDate(item.startTime, 'yyyy.MM.dd');
						item.endTime = formatDate(item.endTime, 'yyyy.MM.dd');
						item.updateTime = formatDate(item.updateTime, 'yyyy年MM月dd日');
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
				keyword: '',
				advertiserName: '',
				startTime: '',
				endTime: '',
				orderBy: '',
				order: ''
			};
			this.searchDateRange = '';
			let route = this.$route;
			this.surveyId = [];
			this.mutilSurveys = [];
			this.search.order = this.approveStatus === 0 ? 'asc' : 'desc';
			this.search.orderBy = this.approveStatus === 0 ? 'updateTime' : 'approveTime';
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
.amp-account .account-set {
    padding: 10px 10px;
    background: #fff;
}
</style>
