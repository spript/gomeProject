<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="media-set">
			<div class="clearfix pt-20">
				<div class="fn-media" id="a">
	    			<button class="btn btn-normal" @click.prevent='newForm'>新增广告形式</button>
	    		</div>
	    		<div class="fn-media"><span class="plan-title">广告形式ID/名称：</span>
					<div class="ele-input">
						<input class="w200" v-model='so.keyword' placeholder="请输入广告形式ID/名称" maxlength="60">
					</div>
				</div>
				<div class="fn-media fn-plan-new"><span class="plan-title">状态：</span>
					<el-select v-model="so.status.value" style="width:100px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.status.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="fn-media" @click='getList()'>
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
									<th class="w100"><span>广告形式ID</span></th>
									<th class="w200"><span>广告形式名称</span></th>
									<th class="w100"><span>状态</span></th>
									<th class="w200"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr v-for='item in so.list' class="h50 body-row">
									<td><span>{{item.formatId}}</span></td>
									<td><span>{{item.name}}</span></td>
									<td><span>{{item.status==1?"上线":"下线"}}</span><p v-show="item.offlineTime != -28800000 && Math.floor((item.offlineTime-nowDate)/86400000)>-1" class="changeStatusTxt">({{Math.floor((item.offlineTime-nowDate)/86400000)==0?Math.floor((item.offlineTime-nowDate)/3600000):Math.floor((item.offlineTime-nowDate)/86400000)}}{{Math.floor((item.offlineTime-nowDate)/86400000)==0?'小时'+Math.ceil(Math.floor((item.offlineTime-nowDate)/1000)%86400%3600/60)+'分后':'天后'}}变为下线状态)</p></td>
									<td>
										<span class="table-links">
											<b>
												<a href="" @click.prevent="modify(item.formatId,item.status)":class="item.status==1?'modify-disabled':''" :title="item.status==1?'只有下线的媒体才能被修改':''">修改</a>
								                <a href="" @click.prevent="changeStatus(item.formatId,item.status)">{{item.status==1?"下线":"上线"}}</a>
								                <a href="" @click.prevent="del(item.formatId,item.status)">删除</a>
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
	name: 'app-resource-advertForm',
	data() {
		return {
			nowDate: new Date().getTime(),
			breadcrumbConfig: {
				data: [{
					msg: '资源位',
					link: '/#/app/resource/'
				}, {
					msg: '广告形式',
					link: '/#' + this.$route.path
				}]
			},
			so: {
				keyword: "",
				list: [],
				showCM: false,
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
		    id: '',
		    status: '',
		    offline_time: '',
		    page: {
			    	totalCount: 0,
			        currentPage: 1,
			        pageSizes: [20, 30, 50],
			        pageSize: 20
			    }
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	},
	created() {
		this.getList();
		var that = this;
		Event.$off("media-save-success");
	    Event.$on("media-save-success", function(){
	      that.getList();
	    });
	},
    updated() {
    	tableHandler('con-table-change', true, 0);
    },
    destroyed() {
    	offWindowEvent('con-table-change');
    },
	methods: {
		pageSizeChange(size){
	        this.page.pageSize = size;
	        this.getList();
	    },
	    currentPageChange(page){
	        this.page.currentPage = page;
	        this.getList();
	    },
		getList() {
			var so = this.so;
			var vm = this;
			http.get("/api/formats", {       
				params: {
					keyword: this.so.keyword,
					status: this.so.status.value,
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			})
			.then((res) => {
				console.log(res);
				if (res.data.code == 200) {
					var data = res.data.data;
					so.list = data.list;
					vm.page.totalCount = res.data.data.totalCount;
				}
			}).catch((Error) => {
				console.log(Error);
			});
	    },
		newForm() {
			actions.controlDrawer(this.$store, {
	    		show: true,
	    		action: 'new',
	    		type: 'format',
	    		config: {
	    			name: '',
					adElement:{
						image: [],
						video: [],
						text: []
					}
	    		}
	    	});
		},
		sureFn() {
			var that = this;
			if (this.btn_state == 'delete') {
		        http.get("/api/format/status", {
		        	params: {formatId :that.id}
		        })
		        .then (function(res) {
		        	if (res.data.code == 200) {
		        		var status = res.data.data.status;
		        		if (status == 1) {
		        			that.dialogVisible = true;
						    that.body_html = '<h3>无法删除</h3><p>(只有下线的广告形式才能被删除)</p>';
						    that.btn_state = 'noCan';
		        		} else {
		        			http.delete("/api/format", {
					            data: {formatId: that.id}
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
		        http.put("/api/format/status", {
			           formatId: that.id,
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
	        } else if (this.btn_state == 'noCan') {
	        	this.dialogVisible = false;
	        } else if (this.btn_state == 'change') {
	        	http.get("/api/format/checkOfflineConditon", {
   	    				params:{
					        formatId: that.id
				        }
	   	    		})
	   	    		.then (function(res) {
	   	    			if (res.data.code == 200 && res.data.data.result) {
				            http.put("/api/format/status", {
					           formatId: that.id,
					           status: that.status,
					           offline_time: that.offline_time
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
						    actions.controlDrawer(that.$store, {
								show: false
							});
				        } else {
				        	that.dialogVisible = true;
						    that.body_html = '<h3>该广告形式无法下线</h3><p>请先解除与该广告形式绑定的广告的关联关系</p>';
						    that.btn_state = 'noCan';
				        }
	   	    		})
	   	    		.catch (function(error) {
	   	    			alert("fail");
	   	    		});
	        }
	        this.dialogVisible = false;
		},
		getData(id,cb) {
			var that = this;
		    http.get("/api/format",{
		        params:{formatId: id}
		    })
		    .then(function(res){
		    	if (res.data.code == 200) {
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
					if (!data.adElement.image) {
						data.adElement.image = [];
					} 
					if (!data.adElement.video) {
						data.adElement.video = [];
					} 
					if (!data.adElement.text) {
						data.adElement.text = [];
					}
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'modify',
			      		type: 'format',
			      		config: data
			      	});
			    });
		    }
		},
		del(id,status) {
			this.dialogVisible = true;
		    this.body_html = '<h3>您确定要删除该广告形式吗？</h3>';
		    this.btn_state = 'delete';
		    this.id = id;
		},
		changeStatus(id,status) {
			if (status == 1) {
				this.getData(id, (data) => {
					data.summary = this.summary;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'change',
			      		type: 'advertFormChange',
			      		config: data
			      	});
		        });
	        } else {
	      		this.dialogVisible = true;
			    this.body_html = '<h3>您确定要上线该广告形式吗？</h3>';
			    this.btn_state = 'up';
			    this.id = id;
	        }
	    }
	},
	mounted() {
		Event.$on('changeStatus', (argv) => {
			if　(argv.status == 2) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告下线吗？</h3>';
			}
			this.btn_state = 'change';
			this.id = argv.formatId;
			this.status = argv.status;
			this.offline_time = argv.offline_time;
		});
		initWindowResize('con-table-change', true, 0);
	}
};
</script>
