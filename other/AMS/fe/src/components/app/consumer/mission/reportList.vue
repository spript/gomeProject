<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
    	<div class="media-set">
    		<div class="clearfix" style="position: relative;">
    			<div class="fn-media pt-10">
		    		<div class="fn-media"><span class="plan-title">任务名称：</span>
						<div class="ele-input">
							<input v-model='so.keyword' placeholder="请输入任务名称" maxlength="60">
						</div>
					</div>
					<div class="fn-media">
						<span class="plan-title">起止时间：</span>
	                	<el-date-picker v-model="searchDateRange" type="daterange" align="right" placeholder="选择日期范围" :picker-options="datePickOptions" :editable="false" style="width:240px;"></el-date-picker>
	                </div>
					<div class="fn-media"><span class="plan-title">状态：</span>
						<el-select v-model="so.status.value" style="width:130px;display:inline-block;" placeholder="全部">
							<el-option v-for="item in so.status.options" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
					<div class="fn-media" @click='getList()'>
		    			<button class="btn media-btn-primary ">查询</button>
		    		</div>
		    		<a :href="getExportUrl" class="icon icon-report missionReport"><em style="color: #494949;font-size: 15px;margin-left: 5px;">导出</em></a>
				</div>
    		</div>
    		<!--list-->
    		<div class="amp-data">
      			<div class="data-table" id="con-table-change">
					<div class="main-table-wapper">
						<table class="table main-table">
							<thead>
								<tr class="h45 list-header">
									<th class="w100"><span>任务ID</span></th>
									<th class="w260"><span>任务名称</span></th>
									<th class="w150"><span>任务类型</span></th>
									<th class="w200"><span>起止时间</span></th>
									<th class="w80"><span>状态</span></th>
									<th class="w100"><span>完成任务人次</span></th>
									<th class="w100"><span>完成任务人数</span></th>
									<th class="w120"><span>已领取奖励人次</span></th>
									<th class="w120"><span>已领取奖励人数</span></th>
									<th class="w150"><span>消耗金额</span></th>
								</tr>
							</thead>
							<tbody>
								<tr class="body-row" v-if="so.list.length == 0">
									<td colspan="10" style="text-align: center; height: 30px;line-height: 30px;">无相关数据</td>
								</tr>
								<tr class="h50 body-row" v-for='(item,index1) in so.list'>
									<td><span>{{item.missionId}}</span></td>
									<td><span>{{item.name}}</span></td>
									<td><span>{{item.typeUpVote==1?'点赞':''}}{{item.typeUpVote==1&&(item.typeShare==1||item.typeResearch==1||item.typeWatch==1)?'|':''}}{{item.typeShare==1?'分享':''}}{{item.typeShare==1&&(item.typeResearch==1||item.typeWatch==1)?'|':''}}{{item.typeResearch==1?'调研':''}}{{item.typeResearch&&item.typeWatch==1?'|':''}}{{item.typeWatch==1?'视频':''}}</span></td>
									<td><span>{{item.startTime | date('yyyy/MM/dd')}}-{{item.endTime | date('yyyy/MM/dd')}}</span></td>
									<td><span>{{item.status==1?"有效":(item.status==2?"过期":"其他")}}</span></td>
									<td><span>{{item.timesOfTaskFinished}}</span></td>
									<td><span>{{item.peopleOfTaskFinished}}</span></td>
									<td><span>{{item.timesOfAwardReceived}}</span></td>
									<td><span>{{item.peopleOfAwardReceived}}</span></td>
									<td><span>国美币{{item.totalCost | currency}}</span></td>
								</tr>
							</tbody>
						</table>
					</div>
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
import Vue from "vue";
import actions from 'actions';
import http from "../../../../utils/http";
import Event from 'event';
import moment from 'moment/moment.js';
import Breadcrumb from '../../../common/breadcrumb.vue';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
export default {
	name: 'mission-report',
	data() {
		return {
			searchDateRange: '',
			datePickOptions: {
				shortcuts: [{
					text: '今天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						picker.$emit('pick', [start, end]);
					}
				}, {
					text: '最近3天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 2);
						picker.$emit('pick', [start, end]);
					}
				}, {
					text: '最近7天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
						picker.$emit('pick', [start, end]);
					}
				}, {
					text: '最近15天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 14);
						picker.$emit('pick', [start, end]);
					}
				}, {
					text: '全部',
					onClick(picker) {
						picker.$emit('pick', [null,null]);
					}
				}]
			},
			formData: {
				startTime: '',
				endTime: '',
			},
			breadcrumbConfig: {
				data: [{
					msg: '用户任务',
					link: '/#/app/consumer/missionManage'
				}, {
					msg: '数据报表',
					link: '/#' + this.$route.path
				}]
			},
			page: {
		    	totalCount: 0,
		        currentPage: 1,
		        pageSizes: [20, 30, 50],
		        pageSize: 20
		    },
			so: {
				keyword: '',
				status: {
					options: [{
						label: "全部",
						value: "0"
					}, {
						label: "有效",
						value: "1"
					}, {
						label: "过期",
						value: "2"
					}],
					value: "0"
				},
				list: []
			}
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	},
	created() {
		this.getList();
	},
	mounted() {
		initWindowResize('con-table-change', false, 3);
    },
    updated() {
    	if (this.so.list.length == 0) {
    		tableHandler('con-table-change', false, 0);
    	} else {
    		tableHandler('con-table-change', false, 3);
    	}
    },
    destroyed() {
    	offWindowEvent('con-table-change');
    },
	computed: {
		getExportUrl() {
			var startTime = moment(this.formData.startTime).valueOf() ? moment(this.formData.startTime).valueOf() : '';
			var endTime = moment(this.formData.endTime).valueOf() ? moment(this.formData.endTime).valueOf() : '';
			return $CONFIG['domain'] + '/api/report/mission/export?keyword='+this.so.keyword+'&startTime='+startTime+'&endTime='+endTime+'&status='+this.so.status.value+'&page='+this.page.currentPage+'&number='+this.page.pageSize;
		}
	},
	methods: {
		getList() {
			var vm = this;
			http.get('/api/report/mission', {
				params: {
					keyword: vm.so.keyword,
					startTime: vm.formData.startTime ? vm.formData.startTime : '',
					endTime: vm.formData.endTime ? vm.formData.endTime : '',
					status: vm.so.status.value,
					page: vm.page.currentPage,
					number: vm.page.pageSize
				}
			})
			.then(function(res) {
				if (res.data.code == 200) {
					vm.so.list = res.data.data.list;
					vm.page.totalCount = res.data.data.totalCount;
				}
			})
			.catch(function(error) {
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
	    timeClearHours(date) {
			const cloneDate = new Date(date);
		    cloneDate.setHours(0, 0, 0);
		    return cloneDate.getTime();
		},
		setTimeHours(date) {
			const cloneDate = new Date(date);
			cloneDate.setHours(23, 59, 59);
			return cloneDate.getTime();
		}
	},
	watch: {
		'searchDateRange': {
			handler: function(val) {
				if (!val[0] && !val[1]) {
					this.formData.startTime = '';
					this.formData.endTime = '';
				} else {
					let start = new Date(val[0]);
					let end = new Date(val[1]);
					this.formData.startTime = this.timeClearHours(start);
					this.formData.endTime = this.setTimeHours(end);
				}
			},
			deep: true
		}
	}
};
</script>

<style lang="less">
	.missionReport{
		position: absolute;
		right: 0;
		bottom: 5px;
	}
</style>