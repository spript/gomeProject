<template>
  <div>
    <div class="account-set">
      <div class="set-option">
        <button class="btn btn-normal" @click.prevent="newPlan">新建投放计划</button>
        <button class="btn btn-normal" @click.prevent="start">启用</button>
        <button class="btn btn-normal" @click.prevent="stop">暂停</button>
        <button class="btn btn-normal" @click.prevent="del">删除</button>
        <span class="set-option-hint" v-show='showOpationHint'>*请选择至少一项后，再进行操作。</span>
      </div>
      <div class="set-fn">
        <div class="fn-plan"><span class="plan-title">计划名称：</span>
          <div class="ele-input">
            <input v-model="so.keyword" placeholder="请输入计划名称">
          </div>
        </div>
        <el-form :model="so" :rules="rules" ref="formlist">
          <div class="fn-plan">
            <el-form-item prop="startTime" style="display:inline-block;">
              <span class="plan-title">选择时间：</span>
              <el-date-picker v-model="so.startTime" type="date" :editable="false" style="width:125px;" placeholder="开始日期">
              </el-date-picker>
              <span class="plan-title plan-title-gray">-</span>
              <el-date-picker v-model="so.endTime" type="date" :editable="false" style="width:125px;" placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
          </div>
        </el-form>
        <div class="fn-plan fn-plan-new"><span class="plan-title">状态：</span>
          <el-select v-model="so.state.value" style="width:130px;display:inline-block;" placeholder="全部">
            <el-option v-for="item in so.state.options" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="fn-search" @click.prevent="search"><a href="" class="btn btn-primary">查询</a></div>
      </div>
    </div>
    <div class="amp-data">
      <div class="data-table" id="con-table-change">
        <div class="main-table-wapper">
          <table class="table main-table">
            <thead>
              <tr :class="{actived: isActived, 'list-header': true}">
              	<th class="w50"><span><em class="icon icon-select select-all" @click="checkall"></em></span></th>
                <th class="w100"><span><i class="data-id">计划ID</i></span></th>
                <th class="w140"><span>计划名称</span></th>
                <th class="w80"><span>状态</span></th>
                <th class="w220"><span>有效期</span></th>
                <th class="w100"><span>广告日预算</span></th>
                <th class="w80"><span>展现数</span></th>
                <th class="w80"><span>点击数</span></th>
                <th class="w80"><span>点击率(%)</span></th>
                <th class="w100"><span>花费</span></th>
                <th class="w100"><span>平均点击单价</span></th>
                <th class="w100"><span>千次展现成本</span></th>
                <th class="w220"><span>操作</span></th>
              </tr>
            </thead>
            <tbody>
            	<tr class="body-row" v-if="list.length == 0">
								<td colspan="13" style="text-align: center; height: 100px;line-height: 100px;">无相关查询结果</td>
							</tr>
              <template v-for="item in list">
                <tr :class="{actived: item.isActived, 'body-row': true}">
                	<td><span><em @click="checkbox(item.campaignId)" :class="'icon icon-select select-' + item.campaignId" :data-id="item.campaignId"></em></span></td>
                  <td><span><i class="data-id">{{item.campaignId}}</i></span></td>
                  <td><span><i class="ellipsis"><router-link :to="{name: 'bidcpcUnit', params: {id: item.campaignId}}" :title="item.name">{{item.name}}</router-link></i></span></td>
                  <td><span :class="{'plan-state-other': item.state !== 2 && item.state !== 3 && item.state !== 4, 'plan-state-expire': item.state === 3, 'span-col-4': true}">
                    {{item.state==1?"暂停":(item.state==2?"有效":(item.state==3?"过期":(item.state==4?"未开始":(item.state==5?"预算用完":(item.state==6?"不在投放时间段":"余额不足")))))}}
                  </span></td>
                  <td><span style="position:relative">
                    <i class="data-time" :class="{'time-more': item.schedule.length}" @mouseover="show(item.campaignId)" @mouseout="hide(item.campaignId)">{{item.startTime}}-{{item.isUnlimited==1?'不限':item.endTime}}</i>
                    <em class="icon" :class="{'icon-plus':item.schedule.length}"></em>
                    <div style="position:absolute" class="dialog dialog-white dialog-time" ref="dialog" v-show="item.timeMore">
                       <ul>
                         <li v-for="schedule in item.schedule"><a style="cursor:pointer;">{{schedule}}</a></li>
                       </ul>
                     </div>
                  </span></td>
                  <td><span>{{item.adLimited==0?"不限":item.dailyAdBudget/100}}</span></td>
                  <td><span>{{item.impression}}</span></td>
                  <td><span>{{item.click}}</span></td>
                  <td><span>{{item.ctr}}%</span></td>
                  <td><span>{{item.adAmount}}</span></td>
                  <td><span>{{item.clickUnitPrice}}</span></td>
                  <td><span>{{item.costPerMills}}</span></td>
                  <td><span>
                    <b class="operat-link">
                      <a href="" @click.prevent="modify(item.campaignId)">修改</a>
                      <a href="" @click.prevent="copy(item.campaignId)">复制计划</a>
                      <a href="" @click.prevent="newUnit(item.campaignId)">新建投放单元</a>
                    </b>
                  </span></td>
                </tr>
              </template>
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
      :total="page.totalCount"
      :class="{'el-pagination-reset': true}"
      >
    </el-pagination>
    <el-dialog :title=title v-model="dialogVisible" size="tiny">
      <span v-html="body_html"></span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" v-show='showCancel'>取 消</el-button>
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
import {formatDate,mixin} from 'utils/common';
import moment from 'moment';
import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';

export default {
  name: 'app-put-bidcpc-plan-list',
  data() {
    return {
    	rules: {
        startTime: [{
          validator: (rule, value, callback) => {
            if(+this.so.endTime != 0 && +this.so.startTime > +this.so.endTime){
              callback(new Error("开始时间不得晚于结束时间"));
            }else{
              callback();
            }
          },
          trigger: "change"
        }]
      },
    	showOpationHint: false,
      so: {
        keyword: "",
        startTime: moment().startOf('day').valueOf(),
        endTime: moment().endOf('day').valueOf(),
        state: {
          options: [{
            label: "全部",
            value: "0"
          },{
            label: "暂停",
            value: "1"
          },{
            label: "有效",
            value: "2"
          },{
            label: "过期",
            value: "3"
          },{
            label: "未开始",
            value: "4"
          }],
          value: "0"
        }
      },
      list: [],
      page: {
        totalCount: 0,
        currentPage: 1,
        pageSizes: [20, 30, 50],
        pageSize: 20
      },
      dialogVisible: false,
      title : '提示',
      body_html : '',
      btn_state : '',
      showCancel: true,
    };
  },
  components: {

  },
  computed: {
  	dailyBudget() {
  		var dailyBudgetNum = 0,list = this.list;
      for(var item of list){
        if(item.isActived){
          dailyBudgetNum += item.dailyAdBudget / 100;
        }
      }
      return dailyBudgetNum;
  	},
    campaignIds(){
      var campaignIds = [],list = this.list;
      for(var item of list){
        if(item.isActived){
          campaignIds.push(item.campaignId);
        }
      }
      return campaignIds;
    },
    startStates(){
    	var states = this.selectedPlan,num;
      if (states.length == 1) {
      	if (states[0] == 1) {
      		num = 1;//不是暂停
      	} else {
      		num = 2;
      	}
      } else {
      	for(let i = 0; i < states.length; i++) {
	      	if (states[i] == 1) {
	      		num = 3;
	      	} else {
	      		num = 4;
	      		break;
	      	}
	      }
      }
      return num;
    },
    stopStates(){
    	var states = this.selectedPlan,num;
      if (states.length == 1) {
      	if (states[0] == 2 || states[0] == 4) {
      		num = 1;
      	} else {
      		num = 2;
      	}
      } else {
      	for(var i = 0; i < states.length; i++) {
	      	if (states[i] != 2 && states[i] != 4) {
	      		num = 4;
	      		break;
	      	} else {
	      		num = 3;
	      	}
	      }
      }
      return num;
    },
    delStates(){
    	var states = this.selectedPlan,num;
      if (states.length == 1) {
      	if (states[0] == 1 || states[0] == 3 || states[0] == 4) {
      		num = 1;
      	} else {
      		num = 2;
      	}
      } else {
      	for(let i = 0; i < states.length; i++) {
	      	if (states[i] == 1 || states[i] == 3 || states[i] == 4) {
	      		num = 3;
	      	} else {
	      		num = 4;
	      		break;
	      	}
	      }
      }
      return num;
    },
    isActived(){
      var isActived = true, list = this.list;
      for(var item of list){
        if(item.isActived){
          continue;
        }
        isActived = false;
        break;
      }
      return isActived;
    },
    selectedPlan() {
    	var states = [],list = this.list;
      for(var item of list){
        if(item.isActived){
          states.push(item.state);
        }
      }
      return states;
    }
  },
  created(){
    var that = this;
    this.getList();
    Event.$off("put-save-success");
    Event.$on("put-save-success", function(){
      that.getList();
    });
  },
  mounted() {
    initWindowResize('con-table-change', true, 4);
  },
  updated() {
    tableHandler('con-table-change', true, 4);
  },
  destroyed() {
    offWindowEvent('con-table-change');
  },
  methods: {
    checkbox(campaignId){
      var list = this.list;
      for(var item of list){
        if(item.campaignId == campaignId){
          item.isActived = !item.isActived;
        }
      }
    },
    checkall(){
      var list = this.list, isActived = this.isActived;
      for(var item of list){
        item.isActived = !isActived;
      }
    },

    getList(){
      var so = this.so, that = this;
      http.get("/api/campaigns", {
        params:{
          keyword: so.keyword,
          startTime: +so.startTime,
          endTime: +so.endTime,
          state: so.state.value,
          page: this.page.currentPage,
          number: this.page.pageSize,
          productLine: 3
        }
      })
      .then(function(res) {
        if(res.data.code == 200){
          that.list = res.data.data.list;
          that.page.totalCount = res.data.data.totalCount;
          for(var item of that.list){
            Vue.set(item, "isActived", false);
            Vue.set(item, "timeMore", false);
            item.startTime = formatDate(item.startTime, "yyyy/MM/dd");
            item.endTime = formatDate(item.endTime, "yyyy/MM/dd");
            if(Array.isArray(item.schedule) && item.schedule.length){
              var arr = [];
              for(var schedule of item.schedule){
                var temp = [];
                for(var date of schedule){
                  temp.push(formatDate(+date, "yyyy/MM/dd"));
                }
                arr.push(temp.join("-"));
              }
              item.schedule = arr;
            }else {
                item.schedule = [];
			}
          }
        }
      })
      .catch(function(error) {
        alert("list fail");
      });
    },
    search(){
      var that = this;
      this.$refs.formlist.validate(function(valid){
        if(!valid){
          return;
        }
        that.getList();
      });
    },
    sureFn(){
      if(this.btn_state == 'start'){
        var that = this, list = this.list;
        if(!this.campaignIds.length){
          return;
        }
        http.put("/api/campaigns/status", {
          campaignIds: this.campaignIds,
          status: 1
        })
        .then(function(res) {
          if(res.data.code == 200){
            for(var id of that.campaignIds){
              for(var item of list){
                if(id == item.campaignId){
                  item.state = 2;
                  break;
                }
              }
            }
            that.getList();
          }
        })
        .catch(function(error) {
          alert("fail");
        });
      }else if(this.btn_state == 'stop'){
        var that = this, list = this.list;
        if(!this.campaignIds.length){
          return;
        }
        http.put("/api/campaigns/status", {
          campaignIds: this.campaignIds,
          status: 0
        })
        .then(function(res) {
          if(res.data.code == 200){
            for(var id of that.campaignIds){
              for(var item of list){
                if(id == item.campaignId){
                  item.state = 1;
                  break;
                }
              }
            }
            that.getList();
          }
        })
        .catch(function(error) {
          alert("stop fail");
        });
      }else if(this.btn_state == 'delete'){
        var that = this, list = this.list;
        if(!this.campaignIds.length){
          return;
        }
        http.delete("/api/campaigns", {
          data:{
            campaignIds: this.campaignIds
          }
        })
        .then(function(res) {
          if(res.data.code == 200){
            for(var id of that.campaignIds){
              for(var item of list){
                if(id == item.campaignId){
                  Vue.delete(list, item);
                  break;
                }
              }
            }
            that.getList();
          }
        })
        .catch(function(error) {
          alert("fail");
        });
      } else if (this.btn_state == 'cancel') {
      	this.dialogVisible=false;
      }
      this.dialogVisible=false;
    },
    toastChangeHint(condition, statusStr, type) {
    	let singelCan = `<h3>确定${type}该投放计划吗？</h3>`
    	let singelNoCan = `<h3>仅${statusStr}状态的投放计划才可以${type}。</h3>`
    	let multipleCan = `<h3>已选择${this.campaignIds.length}个投放计划，确定全部${type}吗？</h3>`
    	let multipleNoCan = `<h3>已选择${this.campaignIds.length}个投放计划，其中仅${statusStr}状态的计划才可以${type}。请重新选择。</h3>`
    	let surnType = type == '启动' ? 'start' : (type == '暂停' ? 'stop' : (type == '删除' ? 'delete' : ''));
    	if (condition == 1) {
    		this.body_html = singelCan;
    		this.showCancel = true;
    		this.btn_state = surnType
    	} else if (condition == 2) {
    		this.body_html = singelNoCan;
    		this.showCancel = false;
    		this.btn_state = 'cancel';
    	} else if (condition == 3) {
    		this.body_html = multipleCan;
    		this.showCancel = true;
    		this.btn_state = surnType;
    	} else if (condition == 4) {
    		this.body_html = multipleNoCan;
    		this.showCancel = false;
    		this.btn_state = 'cancel';
    	}
    	this.dialogVisible = true;
    },
    start(){
      var that = this, list = this.list;
      if(!this.campaignIds.length){
        this.showOpationHint = true;
      } else {
      	http.get('/api/account')
      	.then((res)=> {
      		if (res.data.code == 200) {
      			if (res.data.data.adBalance <= this.dailyBudget) {
      					this.body_html = `<h3>广告账户余额不足，请您充值后，再启动计划。</h3>`;
					      this.btn_state = 'cancel';
					      this.showCancel = false;
					      this.dialogVisible = true;
      			} else {
      				this.toastChangeHint(this.startStates, '“暂停”', '启动');
      			}
      		}
      	})
      	.catch((error) => {
      		console.log(error);
      	})
      }
    },
    stop(){
      var that = this, list = this.list;
      if(!this.campaignIds.length){
        this.showOpationHint = true;
      } else {
      	this.toastChangeHint(this.stopStates, '“有效”或“未开始”', '暂停');
      }
    },
    del(){
      var that = this, list = this.list;
      if(!this.campaignIds.length){
        this.showOpationHint = true;
      } else {
      	this.toastChangeHint(this.delStates, '暂停、未开始和过期', '删除');
      }
    },
    modify(id){
      this.getData(id, (data) => {
      	actions.controlDrawer(this.$store, {
      		show: true,
      		action: 'modify',
      		type: 'plan',
      		config: data
      	});
      });
    },
    copy(id){
      this.getData(id, (data) => {
      	actions.controlDrawer(this.$store, {
      		show: true,
      		action: 'copy',
      		type: 'plan',
      		config: data
      	});
      });
    },
    newUnit(id) {
    	http.get('/api/flights', {
    		params: {
    			campaignId: id,
    			productLine: 3
    		}
    	})
    	.then((res) => {
    		if (res.data.code == 200) {
    			if (res.data.data.totalCount == 20) {
    				this.showCancel = false;
		    		this.dialogVisible = true;
		      	this.body_html = '<h3>一个投放计划下最多可创建20个投放单元</h3>';
		      	this.btn_state = 'cancel';
    			} else {
    				this.getData(id, (data) => {
			    		actions.controlDrawer(this.$store, {
				    		show: true,
				    		action: 'new',
				    		type: 'unit',
				    		config: mixin({
				    			campaignId: id
				    		}, CONST.DRAWERUNIT),
				    		isRebate: data.isRebate
				    	});
			    	});
    			}
    		}
    	})
    	.catch((error) => {
    		console.log(error);
    	})
    },
    getData(id, cb){
      var that = this;
      http.get("/api/campaign",{
        params:{campaignId: id}
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
    show(campaignId){
		var list = this.list;
		var tabList = this.$refs.dialog;
		var changeDom = document.getElementById('con-table-change');
		var rage = this.getTop(changeDom) + changeDom.offsetHeight - this.getTop(event.currentTarget);
		var target = this.getMask(event.currentTarget);
		for(var item of list){
			if(item.campaignId == campaignId && item.schedule.length){
				item.timeMore = true;
			}
		}

		if(parseInt(rage) <= 180){
			target.className = 'dialog dialog-white dialog-time arrow-b-r';
			target.style.top = -(150) + 'px';
		} else {
			target.className = 'dialog dialog-white dialog-time arrow';
			target.style.top = 50 + 'px';
		}
    },
    hide(campaignId){
      var list = this.list;
      for(var item of list){
        if(item.campaignId == campaignId){
          item.timeMore = false;
        }
      }
    },
    newPlan() {
    	actions.controlDrawer(this.$store, {
    		show: true,
    		action: 'new',
    		type: 'plan',
    		config: CONST.DRAWERPLAN
    	});
    },
	getTop(ele) {
		var T = ele.offsetTop;
		while(ele = ele.offsetParent)
		{
			T += ele.offsetTop;
		}
		return T;
	},
	getMask(ele) {
		for(var item of ele.parentNode.childNodes) {
			for(var goal of this.$refs.dialog)
			if(item == goal){
				return item;
			}
		}
	}
  },
  watch: {
  	'campaignIds' : function() {
			if (this.campaignIds.length) {
				this.showOpationHint = false;
			}
		}
  }
};
</script>
<style scoped>
.plan-state-other{
  color: #d30312;
}
.plan-state-expire{color: #999999;}
</style>