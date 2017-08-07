<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="amp-path pb-10">
	        <span class="path-col">媒体名称：<b>{{summary.mediaName}}</b></span>
	        <span class="path-col">一级频道 | 二级频道：<b>{{summary.levelone+' | '+summary.leveltwo}}</b></span>
	        <span class="path-col">广告位名称：<b>{{summary.advertisingName}}</b></span>
	        <span class="path-col">状态：<b>{{summary.advertisingState==1?"售卖":(summary.advertisingState==2?"非售卖":(summary.advertisingState==0?"下线":"其它"))}}</b></span>
	    </div>
	    <div class="media-set">
	    	<div class="clearfix mrb-20">
	    		<div class="fn-media">
	    			<router-link :to="{name: 'advertisingList', params: {id: channelId?channelId:1}}" class="btn btn-normal btn-back-up"><em class="icon icon-prev"></em>返回</router-link>
	    		</div>
	    		<div class="fn-media">
	    			<button class="btn btn-normal" @click.prevent='showAdvert' :class="summary.advertisingState==0?'modify-disabled':''" :title="summary.advertisingState==0?'只有上线的广告位才能新增广告':''">新增广告</button>
	    		</div>
	    		<!--<div class="fn-media">
	    			<button class="btn btn-normal">导出</button>
	    		</div>-->
	    	</div>
	    	<div class="clearfix">
	    		<div class="fn-media"><span class="plan-title">广告名称：</span>
					<div class="ele-input">
						<input v-model='so.keyword' placeholder="请输入广告位名称" maxlength="60">
					</div>
				</div>
				<div class="fn-media fn-plan-new"><span class="plan-title">计费模式：</span>
					<el-select v-model="so.paidState.value" style="width:160px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in so.paidState.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
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
									<th class="w100"><span>广告ID</span></th>
									<th class="w200"><span>广告名称</span></th>
									<th class="w150"><span>计费模式</span></th>
									<th class="w150"><span>广告形式</span></th>
									<th class="w150"><span>模板</span></th>
									<th class="w80"><span>状态</span></th>
									<th class="w260"><span>操作</span></th>
								</tr>
							</thead>
							<tbody>
								<tr v-for='item in so.list' class="h50 body-row">
									<td><span>{{item.advertisementId}}</span></td>
									<td><span>{{item.name}}</span></td>
									<td><span>{{item.saleMode==5?"CPD":(item.saleMode==1?"定价CPC":(item.saleMode==0?"竞价CPC":(item.saleMode==4?"定价CPM(非合约)":(item.saleMode==2?"竞价CPM":(item.saleMode==3?"定价CPM(合约)":(item.saleMode==6?"CPS":"其他"))))))}}</span></td>
									<td><span>{{item.formatName}}</span></td>
									<td><span>{{item.templateName}}</span></td>
									<td><span>{{item.status==1?"上线":"下线"}}</span><p v-show="item.effectiveTime != -28800000 && Math.floor((item.effectiveTime-nowDate)/86400000)>-1" class="changeStatusTxt">({{Math.floor((item.effectiveTime-nowDate)/86400000)==0?Math.floor((item.effectiveTime-nowDate)/3600000):Math.floor((item.effectiveTime-nowDate)/86400000)}}{{Math.floor((item.effectiveTime-nowDate)/86400000)==0?'小时'+Math.ceil(Math.floor((item.effectiveTime-nowDate)/1000)%86400%3600/60)+'分后':'天后'}}变为下线状态)</p></td>
									<td>
										<span class="table-links">
											<b>
												<a href="" @click.prevent="modify(item.advertisementId,item.status)" :class="item.status==1?'modify-disabled':''" :title="item.status==1?'只有下线的频道才能被修改':''">修改</a>
								                <a href="" @click.prevent="changeStatus(item.advertisementId,item.status)" :class="summary.advertisingState==0 && item.status==0?'modify-disabled':''" :title="summary.advertisingState==0 && item.status==0?'只有上线的广告位才能上线':''">{{item.status==1?"下线":"上线"}}</a>
								                <a :class="item.saleMode==0 || item.saleMode==2?'modify-disabled':''" href="" @click.prevent="price(item.advertisementId,item.saleMode)">价格</a>
								                <a href="" @click.prevent="del(item.advertisementId,item.status)">删除</a>
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
	    
	    <m-price :showPrice='showChildComponents.showPrice' :advertisementId='advertisementId' :saleMode='saleMode'></m-price>
	    <m-cpcPrice :showCpcPrice='showChildComponents.showCpcPrice' :advertisementId='advertisementId'></m-cpcPrice>
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
import Price from './drawer-price.vue';
import cpcPrice from './drawer-cpcPrice.vue';
import {formatDate,mixin} from 'utils/common';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
export default {
	name: 'resource-advert',
	data() {
		return {
			nowDate: new Date().getTime(),
			page: {
			    	totalCount: 0,
			        currentPage: 1,
			        pageSizes: [20, 30, 50],
			        pageSize: 20
			    },
			channelId: '',
			publisherId: '',
			advertisementId: '',
			saleMode: '',
			summary: {
		        mediaName: "",
		        advertisingState: "",
		        levelone: '',
		        leveltwo: '',
		        advertisingName: ''
			},
			so: {
				keyword: "",
				list: [],
				paidState: {
					options: [{
						label: "全部",
						value: "-1"
					}, {
						label: "CPD",
						value: "5"
					}, {
						label: "定价CPC",
						value: "1"
					}, {
						label: "竞价CPC",
						value: "0"
					}, {
						label: "定价CPM(合约)",
						value: "3"
					}, {
						label: "定价CPM(非合约)",
						value: "4"
					}, {
						label: "竞价CPM",
						value: "2"
					}, {
						label: "CPS",
						value: "6"
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
			dialogVisible: false,
		    title : '提示',
		    body_html : '',
		    btn_state : '',
		    id: '',
			status: '',
			effectTime: '',
			compare: '',
			showChildComponents: {
				showPrice: false,
				showCpcPrice: false
			}
		};
	},
	computed: {
		breadcrumbConfig() {
			let breadcrumbConfig = {
				data: [{
					msg: '资源位',
					link: '/#/app/resource/'
				}, {
					msg: '媒体',
					link: '/#/app/resource/publisher'
				}, {
					msg: '频道列表',
					link: '/#/app/resource/channel/'+this.publisherId
				}, {
					msg: '广告位列表',
					link: '/#/app/resource/advertisingList/'+this.channelId
				}, {
					msg: '广告',
					link: '/#' + this.$route.path
				}]
			};
			return breadcrumbConfig;
		}
	},
	components: {
		'm-breadcrumb': Breadcrumb,
		'm-price': Price,
		'm-cpcPrice': cpcPrice
	},
	created() {
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
		changeLength() {
			console.log(1);
		},
		price(id,saleMode) {
			if (saleMode != 0 && saleMode != 2 && saleMode != 1) {
				this.showChildComponents.showPrice = true;
    			this.advertisementId = id;
    			this.saleMode = saleMode;
			} else if (saleMode == 1) {
				this.showChildComponents.showCpcPrice = true;
    			this.advertisementId = id;
			}
		},
		showAdvert() {
			if (this.summary.advertisingState != 0) {
				actions.controlDrawer(this.$store, {
		    		show: true,
		    		action: 'new',
		    		type: 'advert',
		    		config: {
		    			name: '',
						saleMode: '',
						formatName: '',
						generalizeType: '',
						templateName: '',
						summary: this.summary,
						slotId: this.$route.params.id
		    		}
		    	});
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
	        http.get("/api/advertisements", {
	            params:{
		            "slotId": vm.$route.params.id,
		            "keyword": so.keyword,
					"saleMode": so.paidState.value,
					"status": so.status.value,
					"page": this.page.currentPage,
					"number": this.page.pageSize
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
		getSummary() {
	    	var summary = this.summary;
	    	var vm = this;
	    	//获取单个广告位的详情
	    	http.get("/api/slot", {
	            params: {
	                slotId: this.$route.params.id
	            }
	        })
	        .then(function(res){
	      	    if (res.data.code == 200) {
	      	    	var data = res.data.data;
	      	    	summary.advertisingState = data.status;
	      	    	summary.advertisingName = data.name;
	      	    	vm.publisherId = data.publisherId;
	      	    	vm.channelId = data.channelId;
	      	    	localStorage.setItem("publisherId", data.publisherId);
	      	    	localStorage.setItem("channelId", data.channelId);
	      	    	//获取单个频道详情
			        http.get("/api/channel", {
			            params: {
			                channelId: vm.channelId
			            }
			        })
			        .then(function(res){
			      	    if (res.data.code == 200) {
			      	    	var data = res.data.data;
			      	    	summary.levelone = data.parentName ? data.parentName : data.name;
			      	    	summary.leveltwo = data.parentName ? data.name : '--';
			      	    }
			        })
			        .catch(function(error){
			            console.log(error);
			        });
			        //获取单个媒体信息
			        http.get("/api/publisher", {
			            params: {
			                publisherId: vm.publisherId
			            }
			        })
			        .then(function(res){
			      	    if (res.data.code == 200) {
			      	    	var data = res.data.data;
			      	    	summary.mediaName = data.name;
			      	    }
			        })
			        .catch(function(error){
			            console.log(error);
			        });
	      	    }
	        })
	        .catch(function(error){
	            console.log(error);
	        });
	    },
	    getData(id,cb) {
	   	    var that = this;
	        http.get("/api/advertisement",{
	            params:{advertisementId: id}
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
	    sureFn() {
	    	var that = this;
	    	if (this.btn_state == 'delete') {
		        http.get("/api/advertisement/status", {
		        	params: {advertisementId :that.id}
		        })
		        .then (function(res) {
		        	if (res.data.code == 200) {
		        		var status = res.data.data.status;
		        		if (status == 1) {
		        			that.dialogVisible = true;
						    that.body_html = '<h3>处于上线状态的广告无法删除！</h3>';
						    that.btn_state = 'noCan';
		        		} else {
		        			http.delete("/api/advertisement", {
					            data: {advertisementId: that.id}
					        })
					        .then (function(res) {
					        	console.log(res);
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
		    } else if (that.btn_state == "change") {
	   	    	if (that.compare == 1 && that.status == 0) {
	   	    		http.get("/api/advertisement/checkOfflineCondition", {
   	    				params:{
					        advertisementId: that.id,
	   	    				status: that.status,
	   	    				effectiveTime: formatDate(that.effectTime, "yyyy-MM-dd")
				        }
	   	    		})
	   	    		.then (function(res) {
	   	    			if (res.data.code == 200 && res.data.data.result) {
				            http.put("/api/advertisement/status", {
					            advertisementId: that.id,
			    				status: that.status,
			    				effectiveTime: formatDate(that.effectTime, "yyyy-MM-dd")
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
				        } else {
				        	that.dialogVisible = true;
						    that.body_html = '<h3>该广告无法下线</h3><p>预期销售时间与广告销售排期冲突</p>';
						    that.btn_state = 'noCan';
				        }
	   	    		})
	   	    		.catch (function(error) {
	   	    			alert("fail");
	   	    		});
	   	    	} else {
	   	    		http.put("/api/advertisement/status", {
				            "advertisementId": that.id,
		    				"status": that.status
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
	   	    	}
	   	    } else if (this.btn_state == 'noCan') {
	        	this.dialogVisible = false;
	        }
	    	this.dialogVisible = false;
	    },
	    modify(id,status) {
	   	  	if (status != 1) {
	   	  		this.getData(id, (data) => {
	   	  			data.summary = this.summary;
	   	  			console.log(data);
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'modify',
			      		type: 'advert',
			      		config: data
			      	});
			    });
	   	  	}
	    },
	    del(id,status) {
	   		this.dialogVisible = true;
		    this.body_html = '<h3>您确定要删除该广告吗？</h3>';
		    this.btn_state = 'delete';
		    this.id = id;
	    },
	    changeStatus(id,status) {
	    	if (this.summary.advertisingState != 0) {
				this.getData(id, (data) => {
					data.summary = this.summary;
			      	actions.controlDrawer(this.$store, {
			      		show: true,
			      		action: 'change',
			      		type: 'advertChange',
			      		config: data
			      	});
		        });
	        }
	    }
	},
	mounted() {
		var that = this;
		Event.$on('changeStatus', (argv) => {
			if　(argv.compare == 1 && argv.status == 0) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告下线吗？</h3>';
			} else if (argv.compare == 0 && argv.status == 1) {
				this.dialogVisible = true;
				this.body_html = '<h3>您确定要将该广告上线吗？</h3>';
			}
			this.btn_state = 'change';
			this.id = argv.advertisementId;
			this.status = argv.status;
			this.effectTime = argv.effectTime;
			this.compare = argv.compare;
		});
		Event.$on('close-price', function() {
			that.showChildComponents.showPrice = false;
		});
		Event.$on('close-Cpc-price', function() {
			that.showChildComponents.showCpcPrice = false;
		});
		initWindowResize('con-table-change', true, 0);
	}
};
</script>
