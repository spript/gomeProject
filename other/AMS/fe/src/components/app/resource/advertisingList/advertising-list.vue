<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="amp-path pb-10">
	        <span class="path-col">媒体名称：<b>{{summary.name}}</b></span>
	        <span class="path-col">设备类型：<b>{{summary.platform}}</b></span>
	        <span class="path-col">一级频道 | 二级频道：<b>{{summary.levelone+' | '+summary.leveltwo}}</b></span>
	        <span class="path-col">状态：<b>{{summary.state == 1 ? '上线' : '下线'}}</b></span>
	    </div>
	    <div class="media-set">
	    	<div class="clearfix mrb-20">
	    		<div class="fn-media">
	    			<router-link :to="{name: 'channel', params: {id: publisherId}}" class="btn btn-normal btn-back-up"><em class="icon icon-prev"></em>返回</router-link>
	    		</div>
	    		<div class="fn-media">
	    			<button class="btn btn-normal" @click.prevent='showAdertising' :class="summary.state==0?'modify-disabled':''" :title="summary.state==0?'只有上线的频道才能新增广告位':''">新增广告位</button>
	    		</div>
	    		<!--<div class="fn-media">
	    			<button class="btn btn-normal">导出</button>
	    		</div>-->
	    	</div>
	    	<div class="clearfix">
	    		<div class="fn-media"><span class="plan-title">广告位名称：</span>
					<div class="ele-input">
						<input v-model='so.keyword' placeholder="请输入广告位名称" maxlength="60">
					</div>
				</div>
				<div class="fn-media fn-plan-new"><span class="plan-title">状态：</span>
					<el-select v-model="so.state.value" style="width:100px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.state.options" :label="item.label" :value="item.value">
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
									<th class="w100"><span>广告位ID</span></th>
									<th class="w200"><span>广告位名称</span></th>
									<th class="w100"><span>广告位尺寸</span></th>
									<th class="w130"><span>状态</span></th>
									<th class="w300"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr v-for='item in so.list' class="h50 body-row">
									<td><span>{{item.slotId}}</span></td>
									<td><span><router-link :to="{name: 'advert', params: {id: item.slotId}}" :title="item.name">{{item.name}}</router-link></span></td>
									<td><span>{{item.width+'*'+item.height}}</span></td>
									<td><span>{{item.status==1?"售卖":(item.status==2?"非售卖":(item.status==0?"下线":"其它"))}}</span><p v-show="Math.floor((item.effectiveTime-nowDate)/86400000)>-1" class="changeStatusTxt">({{Math.floor((item.effectiveTime-nowDate)/86400000)==0?Math.floor((item.effectiveTime-nowDate)/3600000):Math.floor((item.effectiveTime-nowDate)/86400000)}}{{Math.floor((item.effectiveTime-nowDate)/86400000)==0?'小时'+Math.ceil(Math.floor((item.effectiveTime-nowDate)/1000)%86400%3600/60)+'分后':'天后'}}变为{{item.effectiveStatus==1?"售卖":(item.effectiveStatus==2?"非售卖":(item.effectiveStatus==0?"下线":"其它"))}}状态)</p></td>
									<td>
										<span class="table-links">
											<b>
												<a href="" @click.prevent="modify(item.slotId,item.status)" :class="item.status==1 || item.status==2?'modify-disabled':''" :title="item.status==1 || item.status==2?'只有下线的频道才能被修改':''">修改</a>
								                <a href="" @click.prevent="newAdvert(item.slotId,item.name,item.status)" :class="item.status==0?'modify-disabled':''" :title="item.status==0?'只有上线的广告位才能新增广告':''">新增广告</a>
								                <a href="" @click.prevent="del(item.slotId,item.status)">删除</a>
								                <a href="" @click.prevent="changeStatus(item.slotId)" :class="summary.state==0?'modify-disabled':''" :title="summary.state==0?'只有上线的频道才能切换':''">状态切换</a>
								                <a href="" @click.prevent="stock(item.slotId)">库存</a>
							                </b>
										</span>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			
			<m-stock :showStock='showChildComponents.showStock' :slotId='slotId'></m-stock>
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
import Stock from '../advert/drawer-stock.vue';
import {formatDate,mixin} from 'utils/common';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
export default {
	name: 'resource-advertisingList',
	data() {
		return {
			page: {
			    	totalCount: 0,
			        currentPage: 1,
			        pageSizes: [20, 30, 50],
			        pageSize: 20
			    },
			showChildComponents: {
				showStock: false
			},
			nowDate: new Date().getTime(),
			publisherId: localStorage.getItem("publisherId"),
			id: '',
			slotId: '',
			status: '',
			compare: '',
			effectTime: '',
			breadcrumbConfig: {
				data: [{
					msg: '资源位',
					link: '/#/app/resource/'
				}, {
					msg: '媒体',
					link: '/#/app/resource/publisher'
				}, {
					msg: '频道列表',
					link: '/#/app/resource/channel/'+localStorage.getItem("publisherId")
				}, {
					msg: '广告位列表',
					link: '/#' + this.$route.path
				}]
			},
			summary: {
		        state: '',
		        levelone: '',
		        leveltwo: '',
		        name: '',
		        platform: ''
			},
			so: {
				keyword: "",
				list: [],
				showCM: false,
				state: {
					options: [{
						label: "全部",
						value: "-2"
					}, {
						label: "售卖",
						value: "1"
					}, {
						label: "非售卖",
						value: "2"
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
		    btn_state : ''
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb,
		'm-stock': Stock
	},
	created() {
		localStorage.setItem("channelId", this.$route.params.id);
    	this.getSummary();
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
		stock(id) {
    		this.showChildComponents.showStock = true;
    		this.slotId = id;
	    },
		showAdertising() {
			if (this.summary.state == 1) { 
				actions.controlDrawer(this.$store, {
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
						approveFlowId: 1,
						department: '',
						state: 0,
						controlRadio: true,
						summary: this.summary,
						publisherId: this.publisherId,
						channelId: this.$route.params.id,
						description: ''
		    		}
		    	});

		    	console.log(this.$store);
	    	}
		},
		pageSizeChange(size){
	        this.page.pageSize = size;
	        this.getList();
	    },
	    currentPageChange(page){
	        this.page.currentPage = page;
	        this.getList();
	    },
		getList() {
	    	var so = this.so, vm = this;
	        http.get("/api/slots", {
	          params:{
		            channelId: vm.$route.params.id,
			        keyword: so.keyword,
	    			status: so.state.value,
		            page: this.page.currentPage,
					number: this.page.pageSize
	            }
	        })
	        .then(function(res) {
	      		if (res.data.code == 200) {
	      			var data =res.data.data;
	      			so.list = data.list;
	      			vm.page.totalCount = res.data.data.totalCount;
	      		}
	        })
	        .catch(function(error) {
	          	alert("list fail");
	        });
	    },
	    getSummary() {
	    	var summary = this.summary;
	        http.get("/api/channel", {
	            params: {
	                channelId: this.$route.params.id
	            }
	        })
	        .then(function(res){
	      	    if (res.data.code == 200) {
	      	    	var data = res.data.data;
	      	    	summary.state = data.status;
	      	    	summary.levelone = data.parentName ? data.parentName : data.name;
	      	    	summary.leveltwo = data.parentName ? data.name : '--';
	      	    	console.log(summary);
	      	    }
	        })
	        .catch(function(error){
	            console.log(error);
	        });
	        http.get("/api/publisher", {
	            params: {
	                publisherId: this.publisherId
	            }
	        })
	        .then(function(res){
	      	    if (res.data.code == 200) {
	      	    	var data = res.data.data;
	      	    	summary.name = data.name;
	      	    	summary.platform  = data.platform == 1 ? "APP" : (data.platform == 2 ? "WAP" : (data.platform == 3 ? "PC" : "其它"));
	      	    }
	        })
	        .catch(function(error){
	            console.log(error);
	        });
	    },
	    commonStatus() {
	    	var that = this;
	    	http.put("/api/slot/status", {
		            "slotId": that.id,
    				"status": that.status,
    				"effectiveTime": formatDate(that.effectTime, "yyyy-MM-dd")
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
			    .catch (function(error) {
			        alert("fail");
			    });
		    actions.controlDrawer(that.$store, {
				show: false
			});
	    },
	    sureFn() {
	    	var that = this;
	   	    if (that.btn_state == "change") {
	   	    	if (that.compare == 1 && that.status == 2) {
	   	    		http.get("/api/slot/onsale", {
   	    				params:{
					        slotId: that.id,
	   	    				status: that.status,
	   	    				effectiveTime: formatDate(that.effectTime, "yyyy-MM-dd")
				        }
	   	    		})
	   	    		.then (function(res) {
	   	    			if (res.data.code == 200 && res.data.data.result) {
				            that.commonStatus();
				        } else {
				        	that.dialogVisible = true;
						    that.body_html = '<h3>无法按生效时间将该广告位设为非售卖类型</h3><p>该广告位状态改变生效时间与该广告位下的广告的售卖排期冲突</p>';
						    that.btn_state = 'noCan';
				        }
	   	    		})
	   	    		.catch (function(error) {
	   	    			alert("fail");
	   	    		});
	   	    	} else if (that.compare == 1 && that.status == 0) {
	   	    		http.get("/api/slot/onsale", {
   	    				params:{
					        slotId: that.id,
	   	    				status: that.status,
	   	    				effectiveTime: formatDate(that.effectTime, "yyyy-MM-dd")
				        }
	   	    		})
	   	    		.then (function(res) {
	   	    			if (res.data.code == 200 && res.data.data.result) {
				            that.commonStatus();
				        } else {
				        	that.dialogVisible = true;
						    that.body_html = '<h3>无法按生效时间将该广告位下线</h3><p>该广告位状态改变生效时间与该广告位下的广告的售卖排期冲突</p>';
						    that.btn_state = 'noCan';
				        }
	   	    		})
	   	    		.catch (function(error) {
	   	    			alert("fail");
	   	    		});
	   	    	} else {
	   	    		that.commonStatus();
	   	    	}
	   	    } else if (this.btn_state == 'delete') {
		        var that = this, list = this.so.list;
		        http.get("/api/slot/status", {
		        	params: {slotId :that.id}
		        })
		        .then (function(res) {
		        	if (res.data.code == 200) {
		        		var status = res.data.data.status;
		        		if (status == 1) {
		        			that.dialogVisible = true;
						    that.body_html = '<h3>无法删除该广告位</h3><p>(该广告位仍处于上线状态)</p>';
						    that.btn_state = 'noCan';
		        		} else {
		        			http.delete("/api/slot", {
					            data: {slotId: that.id}
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
	    modify(id,status) {
	   		if (status != 1 && status != 2) {
				this.getData(id, (data) => {
					data.summary = this.summary;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'modify',
			      		type: 'advertisingList',
			      		config: data
			      	});
			    });
		    }
	    },
	    newAdvert(id,name,status) {
	    	if (status != 0) {
		    	this.summary.advertisingName = name;
		   	    actions.controlDrawer(this.$store, {
		    		show: true,
		    		action: 'new',
		    		type: 'advert',
		    		config: {
		    			name: '',
						saleMode: '',
						formatName: '',
						promotion: '',
						templateName: '',
						summary: this.summary,
						slotId: id
		    		}
		    	});
	    	}
	    },
	    del(id,status) {
	   	    this.dialogVisible = true;
		    this.body_html = '<h3>您确定要删除该广告位吗？</h3>';
		    this.btn_state = 'delete';
		    this.id = id;
	    },
	    getData(id, cb){
	        var that = this;
	        http.get("/api/slot",{
	            params:{slotId: id}
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
	    changeStatus(id) {
	    	if (this.summary.state == 1) {
				this.getData(id, (data) => {
					data.summary = this.summary;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'change',
			      		type: 'advertisingChange',
			      		config: data
			      	});
		        });
	        }
	    }
	},
	mounted() {
		var that = this;
		Event.$on('changeStatus', (argv) => {
			if (argv.compare == 1 && argv.status == 2){
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位设为非售卖类型么？</h3><p>(将广告位设为非售卖类型将会导致该广告位下的所有广告无法售卖！)</p>';
			} else if (argv.compare == 1 && argv.status == 0) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位下线么？</h3><p>(将广告位设下线将会导致该广告位下的所有广告无法展示！)</p>';
			} else if (argv.compare == 2 && argv.status == 1) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位设为售卖类型么？</h3>';
			} else if (argv.compare == 2 && argv.status == 0) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位下线么？</h3><p>(将广告位设下线将会导致该广告位下的所有广告无法展示！)</p>';
			} else if (argv.compare == 0 && argv.status == 1) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位设为售卖类型么？</h3>';
				
			} else if (argv.compare == 0 && argv.status == 2) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告位设为非售卖类型么？</h3>';
			}
			this.btn_state = 'change';
			this.id = argv.slotId;
			this.status = argv.status;
			this.compare = argv.compare;
			this.effectTime = argv.effectTime;
		});
		Event.$on('close-stock', function() {
			that.showChildComponents.showStock = false;
		});
		initWindowResize('con-table-change', true, 0);
	}
};
</script>
