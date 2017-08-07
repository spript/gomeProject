<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
    	<div class="media-set">
    		<div class="accountMoney">
    			<ul class="clearfix">
    				<li>账户金额：<span>{{balance | currency('¥', 2)}}</span></li>
    				<li>昨日花费：<span>{{yesterdayCost | currency('¥', 2)}}</span></li>
    			</ul>
    		</div>
    		<div class="clearfix">
    			<div class="fn-media pt-20">
	    			<button class="btn btn-normal" @click.prevent='newMission()'>新建任务</button>
	    		</div>
    		</div>
    		<div class="clearfix">
    			<div class="fn-media">
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
									<th class="w120"><span>周期</span></th>
									<th class="w150"><span>奖励金额</span></th>
									<th class="w80"><span>状态</span></th>
									<th class="w200"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr class="body-row" v-if="so.list.length == 0">
									<td colspan="8" style="text-align: center; height: 30px;line-height: 30px;">无相关数据</td>
								</tr>
								<tr class="h50 body-row" v-for='(item,index) in so.list'>
									<td><span>{{item.missionId}}</span></td>
									<td><span><a href="javascript:;" @click="lookMission(item.missionId)">{{item.name}}</a></span></td>
									<td><span>{{item.typeUpVote==1?'点赞':''}}{{item.typeUpVote==1&&(item.typeShare==1||item.typeResearch==1||item.typeWatch==1)?'|':''}}{{item.typeShare==1?'分享':''}}{{item.typeShare==1&&(item.typeResearch==1||item.typeWatch==1)?'|':''}}{{item.typeResearch==1?'调研':''}}{{item.typeResearch&&item.typeWatch==1?'|':''}}{{item.typeWatch==1?'视频':''}}</span></td>
									<td><span>{{item.startTime | date('yyyy/MM/dd')}}-{{item.endTime | date('yyyy/MM/dd')}}</span></td>
									<td><span>{{item.cycleDays==0?Math.floor((item.endTime-item.startTime)/86400000)+1:item.cycleDays}}{{item.cycleMode==0?'(累计任务)':''}}</span></td>
									<td><span>国美币{{item.awardAmount | currency}}</span></td>
									<td><span>{{item.missionStatus==1?"有效":(item.missionStatus==2?"过期":(item.missionStatus==3?"未开始":"其它"))}}</span></td>
									<td>
										<span class="table-links">
											<b>
								                <a :class='item.missionStatus == 2 || (item.cycleMode==0 && item.missionStatus !=3)?"modify-disabled":""' href="" @click.prevent="modify(item.missionId,item.missionStatus,item.cycleMode,item.isTerminated,item.isLastCycle)">修改</a>
								                <a href="" @click.prevent="stop(item.missionId,item.cycleMode,item.missionStatus,item.isTerminated,item.isLastCycle)" :class="item.cycleMode==1 && item.missionStatus==1?'':'modify-disabled'">终止</a>
								                <a href="" @click.prevent="copy(item.missionId)">复制</a>
							                </b>
										</span>
									</td>
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
	    <el-dialog :title=title v-model="dialogVisible" size="tiny">
		    <span v-html=body_html></span>
		    <span slot="footer" class="dialog-footer">
		        <el-button @click="dialogVisible = false">取 消</el-button>
		        <el-button type="primary" @click="sureFn()">确 定</el-button>
		    </span>
	    </el-dialog>
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
	name: 'mission-manage',
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
					msg: '任务管理',
					link: '/#' + this.$route.path
				}]
			},
			id: '',
			today: new Date().getTime(),
			yesterdayCost: 0,
			balance: 0,
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
					}, {
						label: "未开始",
						value: "3"
					}],
					value: "0"
				},
				list: []
			},
			dialogVisible: false,
		    title : '提示',
		    body_html : '',
		    btn_state : '',
		    params: {}
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	},
	computed: {
		
	},
	mounted() {
    	initWindowResize('con-table-change', false, 0);
    	Event.$on('missionModify', (argv) => {
    		this.dialogVisible = true;
		    this.body_html = '<h3>确定修改该任务吗？</h3><p>(修改后将会在任务的下一个周期生效)</p>';
		    this.btn_state = 'modify';
		    this.params = argv;
    	});
    },
    updated() {
    	tableHandler('con-table-change', false, 0);
    },
    destroyed() {
    	offWindowEvent('con-table-change');
    },
	created() {
		var that = this;
		Event.$off("mission-save-success");
	    Event.$on("mission-save-success", function(){
	    	that.getList();
	    	that.getMissionAccount();
	    });
	    that.getMissionAccount();
	    that.getList();
	},
	methods: {
		sureFn() {
			if (this.btn_state == 'stop') {
				var vm = this;
				http.put('/api/mission/terminate', {
					missionId: vm.id
				})
				.then(function(res) {
					vm.$message({
						message: res.data.msg,
						type: 'success',
						duration: 3000
					});
					vm.getList();
				})
				.catch(function(error) {
					console.log(error);
				});
			} else if (this.btn_state == 'modify') {
				http({
					url: '/api/mission',
					method: 'put',
					data: this.params
				})
				.then((res) => {
					if (!res.data.iserror) {
						Event.$emit('mission-save-result', {
							error: 0,
							res: res.data,
							isRebate: this.formData.isRebate
						});
					} else {
						Event.$emit('mission-save-result', {
							error: 1,
							res: null
						});
					}
				})
				.catch((e) => {
					Event.$emit('mission-save-result', {
						error: 1,
						res: null
					});
				});
			}
			this.dialogVisible = false;
		},
		newMission() {
			actions.controlDrawer(this.$store, {
	    		show: true,
	    		action: 'new',
	    		type: 'mission',
	    		config: {
	    			name: '',
	    			totalCount: '',
	    			startTime: moment(Date.now()).startOf('day').valueOf(),
	    			endTime: moment(Date.now()).startOf('day').valueOf(),
	    			cycleMode: 0,
	    			cycleDays: '',
	    			typeUpVote: 0,
	    			typeShare: 0,
	    			typeResearch: 0,
	    			typeWatch: 0,
	    			typeUpVoteCount: '',
	    			typeShareCount: '',
	    			typeResearchCount: '',
	    			typeWatchCount: '',
	    			awardAmount: '',
	    			description: ''
	    		}
	    	});
		},
		getMissionAccount() {
			var vm = this;
			http.get('/api/mission/account')
			.then(function(res) {
				if (res.data.code == 200) {
					vm.balance = res.data.data.balance;
					vm.yesterdayCost = res.data.data.yesterdayCost;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		},
		getData(id,cb) {
			var that = this;
		    http.get("/api/mission",{
		        params:{missionId: id}
		    })
		    .then(function(res){
		    	if(res.data.code === 200){
		        	cb(res.data.data);
		        }
		    })
		    .catch(function(error){
		      	console.log(error);
		    });
		},
		getList() {
			var vm = this;
			http.get('/api/missions', {
				params: {
					keyword: vm.so.keyword,
					startTime: vm.formData.startTime ? vm.formData.startTime : '',
					endTime: vm.formData.endTime ? vm.formData.endTime : '',
					missionStatus: vm.so.status.value,
					page: vm.page.currentPage,
					number: vm.page.pageSize
				}
			})
			.then(function(res) {
				console.log(res);
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
		lookMission(id) {
			this.getData(id, (data) => {
		      	actions.controlDrawer(this.$store, {
		      		show: true,
		      		action: 'look',
		      		type: 'mission',
		      		config: data
		      	});
		    });
		},
		modify(id,status,cycleMode,isTerminated,isLastCycle) {
			if ((status != 2 && cycleMode != 0) || status == 3) {
				if (isTerminated == 0) {
					this.$message({
						message: '已经终止过了,不能进行修改！',
						type: 'warning',
						duration: 3000
					});
				} else if (isLastCycle == 0 && status != 3) {
					this.$message({
						message: '最后一个周期,不能进行修改！',
						type: 'warning',
						duration: 3000
					});
				} else {
					this.getData(id, (data) => {
				      	data.missionId = id;
				      	data.missionStatus = status;
				      	actions.controlDrawer(this.$store, {
				      		show: true,
				      		action: 'modify',
				      		type: 'mission',
				      		config: data
				      	});
				    });
				}
		    }
		},
		stop(id,mode,status,isTerminated,isLastCycle) {
			if (mode == 1 && status == 1) {
				if (isTerminated == 0) {
					this.$message({
						message: '已经终止过了,不能再次终止！',
						type: 'warning',
						duration: 3000
					});
				} else if (isLastCycle == 0) {
					this.$message({
						message: '最后一个周期,不能进行终止！',
						type: 'warning',
						duration: 3000
					});
				} else {
					this.dialogVisible = true;
				    this.body_html = '<h3>确定终止该任务吗？</h3><p>(终止后将会在任务的下一个周期生效)</p>';
				    this.btn_state = 'stop';
				    this.id = id;
				}
			}
		},
		copy(id) {
			this.getData(id, (data) => {
		      	actions.controlDrawer(this.$store, {
		      		show: true,
		      		action: 'copy',
		      		type: 'mission',
		      		config: data
		      	});
		    });
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
	.accountMoney{
		text-align: center;
		border-bottom: 1px solid #ececee;
		ul{
			display:inline-block;
			li {
				float: left;
				margin-right: 59px;
				color: #3598d2;
				font-size: 16px;
				line-height: 34px;
				height: 42px;
				&:last-child{
					margin-right: 0;
				}
			}
		}
	}
</style>