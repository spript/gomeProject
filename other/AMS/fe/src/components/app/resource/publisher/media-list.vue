<template>
    <div>
    	<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
    	<div class="media-set">
    		<!--<div class="clearfix mrb-30">
	    		<div class="fn-media">
	    			<button class="btn btn-normal">导出</button>
	    		</div>
    		</div>-->
    		<div class="clearfix">
    			<div class="fn-media pt-20">
	    			<div class="fn-media">
		    			<button class="btn btn-normal" @click.prevent='showMedia'>新增媒体</button>
		    		</div>
	    			<div class="fn-media"><span class="plan-title">媒体名称：</span>
						<div class="ele-input">
							<input v-model='so.keyword' placeholder="请输入媒体名称" maxlength="60">
						</div>
					</div>
					<div class="fn-media"><span class="plan-title">设备类型：</span>
						<el-select v-model="so.deviceState.value" style="width:100px;display:inline-block;" placeholder="全部">
							<el-option v-for="item in so.deviceState.options" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
	    			<div class="fn-media"><span class="plan-title">媒体属性：</span>
						<el-select v-model="so.mediaState.value" style="width:100px;display:inline-block;" placeholder="全部">
							<el-option v-for="item in so.mediaState.options" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</div>
				</div>
				<div class="fn-media mrt-20 pb-10">
					<div class="fn-media"><span class="plan-title">状态：</span>
						<el-select v-model="so.status.value" style="width:100px;display:inline-block;" placeholder="全部">
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
									<th class="w100"><span>媒体ID</span></th>
									<th class="w200"><span>媒体名称</span></th>
									<th class="w80"><span>设备类型</span></th>
									<th class="w220"><span>页面URL</span></th>
									<th class="w80"><span>媒体属性</span></th>
									<th class="w80"><span>状态</span></th>
									<th class="w260"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr v-for='item in so.list' class="h50 body-row">
									<td><span>{{item.publisherId}}</span></td>
									<td><span><router-link :to="{name: 'channel', params: {id: item.publisherId}}" :title="item.name">{{item.name}}</router-link></span></td>
									<td><span>{{item.platform==1?"APP":(item.platform==2?"WAP":(item.platform==3?"PC":"其它"))}}</span></td>
									<td><span>{{item.platform==1?"-":item.url}}</span></td>
									<td><span>{{item.isInternal==0?"站外":"站内"}}</span></td>
									<td><span>{{item.status==1?"上线":"下线"}}</span></td>
									<td>
										<span class="table-links">
											<b>
												<a href="" @click.prevent="modify(item.publisherId,item.status)":class="item.status==1?'modify-disabled':''" :title="item.status==1?'只有下线的媒体才能被修改':''">修改</a>
								                <a href="" @click.prevent="changeStatus(item.publisherId,item.status)">{{item.status==1?"下线":"上线"}}</a>
								                <a href="" @click.prevent="newChannel(item.publisherId,item.name,item.platform,item.status)" :class="item.status==0?'modify-disabled':''" :title="item.status==0?'只有上线的媒体才能新增频道':''">新增频道</a>
								                <a href="" @click.prevent="del(item.publisherId,item.status)">删除</a>
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
    	<!--页数-->
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
    	<!--弹出框-->
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
import CONST from '../../../../help/CONST.js';
import Api from "../../../../config/api.config";
import http from "../../../../utils/http";
import Event from 'event';
import Breadcrumb from '../../../common/breadcrumb.vue';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
export default {
	name: 'app-resource-publisher-list',
	data() {
		return {
			breadcrumbConfig: {
				data: [{
					msg: '资源位',
					link: '/#/app/resource/'
				}, {
					msg: '媒体',
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
				keyword: "",
				startTime: new Date(),
				endTime: new Date(),
				list: [],
				showCM: false,
				deviceState: {
					options: [{
						label: "全部",
						value: "0"
					}, {
						label: "APP",
						value: "1"
					}, {
						label: "WAP",
						value: "2"
					}, {
						label: "PC",
						value: "3"
					}],
					value: "0"
				},
				mediaState: {
					options: [{
						label: "全部",
						value: "-1"
					}, {
						label: "站内",
						value: "1"
					}, {
						label: "站外",
						value: "0"
					}],
					value: "-1"
				},
				status: {
					options: [{
						label: "全部",
						value: "-2"
					}, {
						label: "上线",
						value: "1"
					}, {
						label: "下线",
						value: "0"
					}],
					value: "-2"
				}
			},
			summary: {
		        name: "",
		        platform: ""
			},
			dialogVisible: false,
		    title : '提示',
		    body_html : '',
		    btn_state : '',
		    id: '',
		    status: ''
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	},
	computed: {
		
	},
	created() {
		var that = this;
		this.getList();
		Event.$off("media-save-success");
	    Event.$on("media-save-success", function(){
	      that.getList();
	    });
	},
	mounted() {
    	initWindowResize('con-table-change', true, 0);
    },
    updated() {
    	tableHandler('con-table-change', true, 0);
    },
    destroyed() {
    	offWindowEvent('con-table-change');
    },
	methods: {
		getList() {
			var so = this.so;
			var vm = this;
			http.get("/api/publishers", {
				params: {
					keyword: so.keyword,
					platform: so.deviceState.value,
					isInternal: so.mediaState.value,
					status: so.status.value,
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			}).then((res) => {
				console.log(res);
				if (res.status === 200 && res.data.code === 200) {
					vm.so.list = res.data.data.list;
					vm.page.totalCount = res.data.data.totalCount;
				}
			}).catch((Error) => {
				console.log(Error);
			});
		},
		getData(id,cb) {
			var that = this;
		    http.get("/api/publisher",{
		        params:{publisherId: id}
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
		pageSizeChange(size){
	        this.page.pageSize = size;
	        this.getList();
	    },
	    currentPageChange(page){
	        this.page.currentPage = page;
	        this.getList();
	    },
		sureFn() {
			if (this.btn_state == 'down') {
		        var that = this, list = this.so.list;
		        http.get("/api/publisher/offline", {
		        	params: {publisherId :that.id}
		        })
		        .then (function(res) {
		        	if (res.data.code == 200) {
		        		var result = res.data.data.result;
		        		if (result != 0) {
		        			that.dialogVisible = true;
						    that.body_html = '<h3>无法下线该媒体</h3><p>(该媒体下仍有处于上线状态的频道)</p>';
						    that.btn_state = 'noCan';
		        		} else {
		        			http.put("/api/publisher/status", {
					           publisherId: that.id,
					           status: that.status == 1 ? 0 : 1
					        })
					        .then (function(res) {
						        if (res.data.code == 200) {
						            that.getList();
						            that.$message({
										message: res.data.msg,
										type: 'success',
										duration: 3000
									});
						        }
					        })
						    .catch(function(error) {
						            alert("fail");
						        });
		        		}
		        	}
		        })
		        .catch(function(res) {
		        	console.log(res);
		        });
	        }else if (this.btn_state == 'up') {
		        var that = this, list = this.so.list;
		        http.put("/api/publisher/status", {
			           publisherId: that.id,
			           status: that.status == 1 ? 0 : 1
			        })
			        .then (function(res) {
				        if (res.data.code == 200) {
				            that.getList();
				            that.$message({
								message: res.data.msg,
								type: 'success',
								duration: 3000
							});
				        }
			        })
				    .catch(function(error) {
				        alert("fail");
				    });
	        } else if (this.btn_state == 'delete') {
		        var that = this, list = this.so.list;
		        http.get("/api/publisher/status", {
		        	params: {publisherId :that.id}
		        })
		        .then (function(res) {
		        	if (res.data.code == 200) {
		        		var status = res.data.data.status;
		        		if (status == 1) {
		        			that.dialogVisible = true;
						    that.body_html = '<h3>无法删除该媒体</h3><p>(该媒体仍处于上线状态)</p>';
						    that.btn_state = 'noCan';
		        		} else {
		        			http.delete("/api/publisher", {
					            data: {publisherId: that.id}
					        })
					        .then (function(res) {
						        if (res.data.code == 200) {
						            that.getList();
						            that.$message({
										message: res.data.msg,
										type: 'success',
										duration: 3000
									});
						        }
					        })
						    .catch(function(error) {
						        alert("fail");
						    });
		        		}
		        	}
		        })
		        .catch(function(res) {
		        	console.log(res);
		        });
	        } else if (this.btn_state == 'noCan') {
	        	this.dialogVisible = false;
	        }
			
		    this.dialogVisible = false;
		},
		showMedia() {
			actions.controlDrawer(this.$store, {
	    		show: true,
	    		action: 'new',
	    		type: 'media',
	    		config: CONST.DRAWERMEDIA
	    	});
		},
		modify(id,status) {
			if (status != 1) {
				this.getData(id, (data) => {
			      	data.publisherId = id;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'modify',
			      		type: 'media',
			      		config: data
			      	});
			    });
		    }
		},
		changeStatus(id,status) {
			var vm = this;
			if (status == 1) {
        		vm.dialogVisible = true;
			    vm.body_html = '<h3>您确定要下线该媒体吗？</h3><p>(请确保该媒体下无处于上线状态的频道)</p>';
			    vm.btn_state = 'down';
			    vm.id = id;
			    vm.status = status;
		    } else {
		    	vm.dialogVisible = true;
			    vm.body_html = '<h3>您确定要上线该媒体吗？</h3>';
			    vm.btn_state = 'up';
			    vm.id = id;
			    vm.status = status;
		    }
		},
		del(id,status) {
			this.dialogVisible = true;
		    this.body_html = '<h3>您确定要删除该媒体吗？</h3>';
		    this.btn_state = 'delete';
		    this.id = id;
		},
		newChannel(id,name,deviceType,status) {
			if (status == 1) {
				this.summary.name = name;
				this.summary.platform = deviceType == 1 ? "APP" : (deviceType == 2 ? "WAP" : (deviceType == 3 ? "PC" : "其它"));
				actions.controlDrawer(this.$store, {
		    		show: true,
		    		action: 'new',
		    		type: 'channel',
		    		config: {
		    			name: '',
		    			publisherId: id,
						media: this.summary,
						parentName: '',
						description: '',
						url: ''
		    		}
		    	});
	    	}
		}
		
	}
};
</script>