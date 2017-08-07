<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="amp-path pb-10">
	        <span class="path-col">媒体名称：<b>{{summary.name}}</b></span>
	        <span class="path-col">设备类型：<b>{{summary.platform}}</b></span>
	        <span class="path-col">状态：<b>{{summary.state == 1 ? '上线' : '下线'}}</b></span>
	    </div>
	    <div class="media-set">
	    	<div class="clearfix mrb-20">
	    		<div class="fn-media">
	    			<router-link to="/app/resource/publisher" class="btn btn-normal btn-back-up"><em class="icon icon-prev"></em>返回</router-link>
	    		</div>
	    		<div class="fn-media">
	    			<button class="btn btn-normal" @click.prevent='showChannel' :class="summary.state==0?'modify-disabled':''" :title="summary.state==0?'只有上线的媒体才能新增频道':''">新增频道</button>
	    		</div>
	    		<!--<div class="fn-media">
	    			<button class="btn btn-normal">导出</button>
	    		</div>-->
    		</div>
    		<div class="clearfix pb-10">
    			<div class="fn-media"><span class="plan-title">频道名称：</span>
					<div class="ele-input">
						<input v-model='so.keyword' placeholder="请输入频道名称" maxlength="60">
					</div>
				</div>
				<div class="fn-media fn-plan-new"><span class="plan-title">父频道：</span>
					<el-select v-model="so.parentName.channelId" style="width:152px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.parentName.options" :label="item.name" :value="item.channelId">
						</el-option>
					</el-select>
				</div>
				<div class="fn-media fn-plan-new"><span class="plan-title">状态：</span>
					<el-select v-model="so.status.value" style="width:100px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.status.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="fn-media" @click='selectChannel'>
	    			<button class="btn media-btn-primary">查询</button>
	    		</div>
    		</div>
    		
    	    <!--list-->
		    <div class="amp-data">
      			<div class="data-table" id="con-table-change">
					<div class="main-table-wapper">
						<table class="table main-table">
							<thead>
								<tr class="h45 list-header">
									<th class="w100"><span>频道ID</span></th>
									<th class="w200"><span>频道名称</span></th>
									<th class="w150"><span>父频道</span></th>
									<th class="w200"><span>页面URL</span></th>
									<th class="w100"><span>状态</span></th>
									<th class="w300"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr v-for='item in so.list' class="h50 body-row">
									<td><span>{{item.channelId}}</span></td>
									<td><span><router-link :to="{name: 'advertisingList', params: {id: item.channelId}}" :title="item.name">{{item.name}}</router-link></span></td>
									<td><span>{{item.parentName}}</span></td>
									<td><span>{{summary.platform == 'APP'?'-':item.url}}</span></td>
									<td><span>{{item.status==1?"上线":"下线"}}</span></td>
									<td>
										<span class="table-links">
											<b>
												<a href="" @click.prevent="modify(item.channelId,item.status)" :class="item.status==1?'modify-disabled':''" :title="item.status==1?'只有下线的频道才能被修改':''">修改</a>
								                <a href="" @click.prevent="changeStatus(item.channelId,item.status)" :class="summary.state==0 && item.status==0?'modify-disabled':''" :title="summary.state==0 && item.status==0?'只有上线媒体才能上线':''">{{item.status==1?"下线":"上线"}}</a>
								                <a href="" @click.prevent="newAdvertising(item.channelId,item.status)" :class="item.status==0?'modify-disabled':''" :title="item.status==0?'只有上线的频道才能新增广告位':''">新增广告位</a>
								                <a href="" @click.prevent="del(item.channelId,item.status)">删除</a>
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
	    <el-dialog :title=title v-model="dialogVisible" size="tiny">
		    <span v-html=body_html></span>
		    <span slot="footer" class="dialog-footer">
		        <el-button @click="dialogVisible = false">取 消</el-button>
		        <el-button type="primary" @click="sureFn">确 定</el-button>
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
	name: 'resource-channel-channelList',
	data() {
		return {
			breadcrumbConfig: {
				data: [{
					msg: '资源位',
					link: '/#/app/resource/'
				}, {
					msg: '媒体',
					link: '/#/app/resource/publisher'
				}, {
					msg: '频道列表',
					link: '/#' + this.$route.path
				}]
			},
			page: {
			    	totalCount: 0,
			        currentPage: 1,
			        pageSizes: [20, 30, 50],
			        pageSize: 20
			    },
			summary: {
				name: "",
		        platform: "",
		        state: 1,
		        levelone: '',
		        leveltwo: ''
			},
			so: {
				keyword: "",
				startTime: new Date(),
				endTime: new Date(),
				list: [],
				showCM: false,
				parentName: {
					options: [{
						name: "全部",
						channelId: -1
					}, {
						name: "无",
						channelId: 0
					}],
					channelId: -1
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
			dialogVisible: false,
		    title : '提示',
		    body_html : '',
		    btn_state : '',
		    channelId : '',
		    status: ''
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
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
	created() {
		localStorage.setItem("publisherId", this.$route.params.id);
    	this.getSummary();
    	this.getList();
    	this.channelList(this.$route.params.id);
    	var that = this;
		Event.$off("media-save-success");
	    Event.$on("media-save-success", function(){
	        that.getList();
	        that.channelList(that.$route.params.id);
	    });
	},
	methods: {
		channelList(id) {
			var vm = this;
			vm.so.parentName = {
				options: [{
					name: "全部",
					channelId: -1
				}, {
					name: "无",
					channelId: 0
				}],
				channelId: -1
			};
			http.get("/api/allChannels", {
				params: {
					publisherId: id
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					var data = res.data.data;
					for (var i in data) {
						vm.so.parentName.options.push(data[i]);
					}
				}
				console.log(vm.so.parentName);
			}).catch((Error) => {
				console.log(Error);
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
		getSummary() {
			var summary = this.summary;
	        http.get("/api/publisher", {
	            params: {
	                publisherId: this.$route.params.id,
	            }
	        })
	        .then(function(res){
	      	    if (res.data.code == 200) {
	      	    	var data = res.data.data;
	      	    	summary.name = data.name;
	      	    	summary.platform  = data.platform == 1 ? "APP" : (data.platform == 2 ? "WAP" : (data.platform == 3 ? "PC" : "其它"));
	      	    	summary.state = data.status;
	      	    	console.log(res);
	      	    }
	        })
	        .catch(function(error){
	            console.log(error);
	        });
	    },
	    getList() {
	    	var so = this.so;
	    	var value = this.so.parentName.channelId;
	    	var vm = this;
	    	console.log(value);
	        http.get("/api/channels", {
	          params:{
		            publisherId: this.$route.params.id,
		            keyword: this.so.keyword,
		            parentId: value,
		            status: this.so.status.value,
		            page: this.page.currentPage,
					number: this.page.pageSize
	            }
	        })
	        .then(function(res) {
	      		if (res.data.code == 200) {
	      			var data = res.data.data;
	      			so.list = data.list;
	      			vm.page.totalCount = res.data.data.totalCount;
	      		}
	        })
	        .catch(function(error) {
	          	alert("list fail");
	        });
	    },
	    sureFn() {
	    	if (this.btn_state == 'downUp') {
	        var that = this, list = this.so.list;
	        http.put("/api/channel/status", {
	            channelId: this.channelId,
	            status: this.status == 1 ? 0 : 1
	        })
	        .then (function(res) {
		        if (res.data.code == 200) {
		           that.getList();
		           that.$message({
						message: res.data.msg,
						type: 'success',
						duration: 3000
					});
					that.channelList(that.$route.params.id);
		        }
	        })
		    .catch(function(error) {
		          alert("fail");
		        });
	        } else if (this.btn_state == 'delete') {
        	    var that = this, list = this.list;
		        if (this.status == 1) {
		            this.dialogVisible = false;
		            return;
		        }
		        http.delete("/api/channel", {
		        	data:{
		        		channelId: this.channelId
		        	}
		        })
		        .then(function(res) {
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
	        } else if (this.btn_state == 'noCan') {
	        	this.dialogVisible = false;
	        }
	    	
	    	this.dialogVisible = false;
	    },
	    showChannel() {
	    	if (this.summary.state == 1) {
				actions.controlDrawer(this.$store, {
		    		show: true,
		    		action: 'new',
		    		type: 'channel',
		    		config: {
		    			name: '',
		    			publisherId: this.$route.params.id,
						media: this.summary,
						parentName: "",
						description: '',
						url: ''
		    		}
		    	});
	    	}
		},
		selectChannel() {
			this.getList();
		},
		getData(id,cb) {
			var that = this;
		    http.get("/api/channel",{
		        params:{channelId: id}
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
		modify(id,status) {
			if (status != 1) {
				this.getData(id, (data) => {
			      	data.publisherId = id;
			      	data.media = this.summary;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'modify',
			      		type: 'channel',
			      		config: data
			      	});
			    });
		    }
		},
		changeStatus(id,status) {
			var vm = this;
			if (status == 1) {
				http.get("/api/channel/children",{
			        params: {channelId: id}
			    })
			    .then(function(res){
			    	if (res.data.code == 200) {
			    		var data = res.data.data;
			    		if (data.result == 0) {
			    			http.get("/api/channel/offline",{
			    				params: {channelId: id}
			    			})
			    			.then(function(res) {
			    				if (res.data.code == 200) {
			    					var data = res.data.data;
			    					if (data.result == 0) {
			    						vm.dialogVisible = true;
									    vm.body_html = '<h3>您确定要下线该频道么？</h3><p>(请确保该频道下无非下线状态的广告位)</p>';
									    vm.btn_state = 'downUp';
									    vm.channelId = id;
			    					} else {
			    						vm.dialogVisible = true;
									    vm.body_html = '<h3>无法下线该频道</h3><p>(该频道下仍有非下线状态的广告位)</p>';
									    vm.btn_state = 'noCan';
									    vm.channelId = id;
			    					}
			    				}
			    			});
			    		} else {
			    			http.get("/api/channel/offline",{
			    				params: {channelId: id}
			    			})
			    			.then(function(res) {
			    				if (res.data.code == 200) {
			    					var data = res.data.data;
			    					if (data.result == 0) {
			    						vm.dialogVisible = true;
									    vm.body_html = '<h3>您确定要下线该频道及二级频道么？</h3><p>(请确保该频道下无非下线状态的广告位)</p>';
									    vm.btn_state = 'downUp';
									    vm.channelId = id;
			    					} else {
			    						vm.dialogVisible = true;
									    vm.body_html = '<h3>无法下线该频道</h3><p>(该频道或二级频道下仍有非下线状态的广告位)</p>';
									    vm.btn_state = 'noCan';
									    vm.channelId = id;
			    					}
			    				}
			    			});
			    		}
			    	}
			    })
			    .catch(function(error){
			      	console.log(error);
			    });
		    } else {
		    	if (this.summary.state == 1) {
		    		http.get("/api/channel/online", {
		    			params: {
		    				channelId: id
		    			}
		    		})
		    		.then(function(res) {
		    			if (res.data.code == 200) {
		    				var data = res.data.data;
		    				if (data.result == 0) {
		    					vm.dialogVisible = true;
							    vm.body_html = '<h3>您确定要上线该频道么？</h3>';
							    vm.btn_state = 'downUp';
							    vm.channelId = id;
		    				} else {
		    					vm.dialogVisible = true;
							    vm.body_html = '<h3>该频道无法上线，请先上线其父频道！</h3>';
							    vm.btn_state = 'noCan';
		    				}
		    			}
		    		})
		    		.catch(function(error) {
		    			console.log(error);
		    		});
			    }
		    }
		    vm.status = status;
		},
		del(id,status) {
			var vm = this;
			http.get("/api/channel/children",{
		        params:{channelId: id}
		    })
		    .then(function(res){
		    	if (res.data.code) {
		    		var data = res.data.data;
		    		if (data.result == 0) {
						var strPre = status == 1 ? "无法删除该频道" : "您确定要删除该频道么？";
						var strNext = status == 1 ? "(该频道未下线)" : "(请确保该频道已下线)";
					    vm.dialogVisible = true;
					    vm.body_html = `<h3>${strPre}</h3><p>${strNext}</p>`;
					    vm.btn_state = 'delete';
					    vm.channelId = id;
					    vm.status = status;
		    		} else {
					    vm.dialogVisible = true;
					    vm.body_html = '<h3>无法删除该频道！</h3><p>请确保该频道下的子频道是否存在！</p>';
					    vm.btn_state = 'noCan';
		    		}
		    	}
		    })
		    .catch(function(error){
		      	console.log(error);
		    });
		},
		newAdvertising(id,status) {
			var vm = this;
			var summary = this.summary;
			if (status == 1) {
				http.get("/api/channel", {
		            params: {
		                channelId: id
		            }
		        })
		        .then(function(res){
		      	    if (res.data.code == 200) {
		      	    	var data = res.data.data;
		      	    	summary.levelone = data.parentName ? data.parentName : data.name;
		      	    	summary.leveltwo = data.parentName ? data.name : '--';
		      	    	
		      	    	actions.controlDrawer(vm.$store, {
				    		show: true,
				    		action: 'new',
				    		type: 'advertisingList',
				    		config: {
				    			name: '',
								width: '',
								height: '',
								rotators: '',
								outSourceFlow: 0,
								materialNumber: '',
								department: '',
								approveFlowId: 1,
								state: 0,
								controlRadio: true,
								summary: vm.summary,
								publisherId: vm.$route.params.id,
								channelId: id,
								description: ''
				    		}
				    	});
		      	    }
		        })
		        .catch(function(error){
		            console.log(error);
		        });
	    	}
		}
	}
};
</script>