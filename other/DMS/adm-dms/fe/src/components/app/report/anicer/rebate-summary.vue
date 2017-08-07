<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="account-set account-set-new mrt-0 pt-20 mb-25">
			<div class="dms-path pb-10">
				查询信息
			</div>
			<div class="set-fn pt-20">
				<el-form :model="formData" :rules="rules" ref="formData">
					<div class="fn-plan">
						<span class="plan-title">起止日期：</span>
						<el-form-item prop="dateRange" style="display:inline-block;margin-bottom:0;">
							<el-date-picker v-model="formData.dateRange" type="daterange" :picker-options="datePickerOptions" :editable="false" placeholder="日期范围" style="width:220px;">
							</el-date-picker>
						</el-form-item>
						<el-select v-model="formData.optionType" placeholder="请选择" style="display:inline-block;width:120px;margin-left:20px;">
							<el-option label="广告主pin" :value="0"></el-option>
							<el-option label="广告主名称" :value="1"></el-option>
						</el-select>
						<el-form-item prop="optionTypeText" style="display:inline-block;margin-bottom:0;">
							<el-input v-model="formData.optionTypeText" placeholder="请输入内容" style="display:inline-block;width:160px;"></el-input>
						</el-form-item>
						<span class="plan-title" style="margin-left:20px;">广告主类型：</span>
						<el-select v-model="formData.advertiserType" placeholder="请选择" style="display:inline-block;width:120px;">
							<el-option label="全部" :value="0"></el-option>
							<el-option label="品牌商" :value="1"></el-option>
							<el-option label="代理商" :value="2"></el-option>
							<el-option label="商家" :value="3"></el-option>
							<el-option label="运营" :value="4"></el-option>
						</el-select>
					</div>
				</el-form>
				<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="search">查询</a></div>
			</div>
		</div>
		<div class="account-set account-set-new mrt-0">
			<div class="set-tab">
				<router-link :to="{name: 'rebate-summary'}" active-class="active" tag="span" v-privilege="'report:anicer:rebate:summary'">汇总</router-link>
				<router-link :to="{name: 'rebate-detial'}" active-class="active" tag="span" v-privilege="'report:anicer:rebate:detail'">明细</router-link>
				<el-popover ref="popover2" placement="bottom" width="260" trigger="click">
					<template v-if="list.length > 0">
						<p style="margin-top:10px;font-size:16px;text-align:center;">可选择导出页数</p>
						<p style="margin:10px 0;font-size:16px;">起始页号</p>
						<el-select v-model="exportData.startPage">
							<el-option v-for="item in exprotStartRageRange" :label="item" :value="item"></el-option>
						</el-select>
						<p style="margin:10px 0;font-size:16px;">结束页号</p>
						<el-select v-model="exportData.endPage">
							<el-option v-for="item in exportEndPageRange" :label="item" :value="item"></el-option>
						</el-select>
						<div style="margin:8px 0;"></div>
						<a :href="getUrl + getUrlStringWithParam(exportCurrentParams).slice(1)" class="btn btn-light-blue">导出当前页</a><a :href="getUrl + getUrlStringWithParam(exportRangeParams).slice(1)" class="btn btn-blue" type="primary">导出页码范围</a>
					</template>
					<template v-else>
						<p style="margin:10px 0;font-size:16px;">没有可导出数据</p>
					</template>
			 	</el-popover>
				<div style="text-align:right;">
					<!--<a v-popover:popover1 href="javascript:;" class='icon icon-manifest dms-a-self'><em class="dms-a-com">自定义指标</em></a>-->
					<a v-popover:popover2 href="javascript:;"  class="icon icon-report dms-a-report" v-privilege="'report:anicer:rebate:summary:export'"><em class="dms-a-com">导出</em></a>
				</div>
			</div>
			<div class="table-content">
				<table class="table">
					<thead>
						<tr>
							<th v-for="(item, index) in checkedHeaders" v-show="item.checked"><span>{{item.msg}}</span></th>
						</tr>
					</thead>
					<tbody>
						<tr v-for="item in list">
							<td v-show="checkedHeaders[0].checked" class="mw120"><span>{{item.advertiserId}}</span></td>
							<td v-show="checkedHeaders[1].checked" class="mw120"><span>{{item.advertiserName}}</span></td>
							<td v-show="checkedHeaders[2].checked" class="mw120"><span>{{item.advertiserType}}</span></td>
							<td v-show="checkedHeaders[3].checked" class="mw120"><span>{{item.landPv}}</span></td>
							<td v-show="checkedHeaders[4].checked" class="mw120"><span>{{item.landUv}}</span></td>
							<td v-show="checkedHeaders[5].checked" class="mw120"><span>{{item.sharedPv}}</span></td>
							<td v-show="checkedHeaders[6].checked" class="mw120"><span>{{item.sharedUv}}</span></td>
							<td v-show="checkedHeaders[7].checked" class="mw120"><span>{{item.chargeShareNumber}}</span></td>
							<td v-show="checkedHeaders[8].checked" class="mw120"><span>{{item.avgClickCost}}</span></td>
							<td v-show="checkedHeaders[9].checked" class="mw120"><span>{{item.totalFee}}</span></td>
						</tr>
					</tbody>
				</table>
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
	</div>
</template>
<script>
import http from "utils/http";
import apiConfig from "../../../../config/api.config.js";
import Breadcrumb from '../../../common/breadcrumb.vue';
import moment from "moment";
const ADREPORTHEADERS = [{
		title: '效果指标',
		fields: [
			{msg: '着陆页PV', checked: true},
			{msg: '着陆页UV', checked: true},
			{msg: '分享页PV', checked: true},
			{msg: '分享页UV', checked: true}
		]
	}, {
		title: '订单指标',
		fields: [
			{msg: '计费分享次数', checked: true},
			{msg: '平均分享单价（元）', checked: true},
			{msg: '总费用（元）', checked: true}
		]
	}
];
export default {
	name: "app-report-anicer-rebate-summary",
	data() {
		return {
			dateRange: [],
			formData: {
				dateRange: [moment().subtract(1, 'months').subtract(1, 'days').startOf('day').valueOf(),moment().subtract(1, 'days').startOf('day').valueOf()],
				startTime: '',
				endTime: '',
				optionType: 0,
				optionTypeText: '',
				advertiserType: 0
			},
			exportData: {
				startPage: 1,
				endPage:1
			},
			breadcrumbConfig: {
				data: [{
					msg: '报表',
					link: '/#/app/report/anicer-anice-summary'
				}, {
					msg: '广告主报表',
					link: '/#/app/report/anicer-anice-summary'
				}, {
					msg: '返利报表',
					link: '/#/app/report/anicer-rebate-summary'
				}]
			},
			adReportHeaders: ADREPORTHEADERS,
			defaultHeaders: [
				{msg: '广告主pin', checked: true},
				{msg: '广告主名称', checked: true},
				{msg: '广告主类型', checked: true}
			],
			header: {},
			list: {},
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			},
			rules: {
				dateRange: [{
					validator: (rule, value, callback) => {
						if(value && value[0]) {
							let start = moment(value[0]);
							let end = moment(value[1]);
							let sub = end.valueOf() - start.valueOf();
							if (sub > 60 * 24 * 60 * 60 * 1000) {
								callback(new Error("起止日期间隔最长60天"));
							} else {
								callback();
							}
						} else {
							callback(new Error("时间不能为空"));
						}
					},
					trigger: "change"
				}],
				optionTypeText: [{
					validator: (rule, value, callback) => {
						if(value && this.formData.optionType == 0) {
							let regex = /^\d+($|(\,\d+){0,9}$)/;
							if (!regex.test(value)) {
								callback(new Error("最多10个数字id以英文逗号分割,数字结尾"));
							} else {
								callback();
							}
						} else {
							callback();
						}
						
					},
					trigger: "change"
				}]
			},
			datePickerOptions: {
				disabledDate(time) {
					let tody = moment();
					let lastYear = moment().year(tody.year() - 1);
					return time.getTime() >= tody.startOf("day").valueOf() || time.getTime() < lastYear.startOf("day").valueOf();
				}
			}
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb
	},
	computed: {
		checkedHeaders() {
			let headers = [...this.defaultHeaders];
			this.adReportHeaders.forEach((group, index) => {
				headers.push(...group.fields);
			});
			return headers;
		},
		headerStr() {
			let str = [];
			this.checkedHeaders.forEach((item) => {
				if (item.checked) {
					str.push(item.msg);
				}
			});
			return str.join(',');
		},
		exportEndPageRange() {
			let range = [];
			let tem = this.page.totalCount % this.page.pageSize == 0 ? this.page.totalCount / this.page.pageSize : this.page.totalCount / this.page.pageSize + 1 ;
			let value = 0;
			if(tem < this.exportData.startPage) {
				value = 0;
			} else if(tem - this.exportData.startPage < 100) {
				value = tem;
			} else {
				value = this.exportData.startPage + 100;
			}
			for(let i = this.exportData.startPage; i <= value; i++) {
				range.push(i);
			}
			return range;
		},
		exprotStartRageRange() {
			let range = [];
			let tem = this.page.totalCount % this.page.pageSize == 0 ? this.page.totalCount / this.page.pageSize : this.page.totalCount / this.page.pageSize + 1 ;
			for(let i=1 ; i< tem; i++) {
				range.push(i);
			}
			return range;
		},
		exportCurrentParams() {
			return {
				startTime: this.formData.startTime,
				endTime: this.formData.endTime,
				optionType: this.formData.optionType,
				optionTypeText: this.formData.optionTypeText,
				advertiserType: this.formData.advertiserType,
				page: this.page.currentPage,
				pageEnd: this.page.currentPage,
				number: this.page.pageSize,
				name: this.headerStr
			};
		},
		exportRangeParams() {
			return {
				startTime: this.formData.startTime,
				endTime: this.formData.endTime,
				optionType: this.formData.optionType,
				optionTypeText: this.formData.optionTypeText,
				advertiserType: this.formData.advertiserType,
				page: this.exportData.startPage,
				pageEnd: this.exportData.endPage,
				number: this.page.pageSize,
				name: this.headerStr
			};
		},
		getUrl() {
			return apiConfig[process.env.NODE_ENV] + '/api/rebate/summary?';
		}
	},
	created() {
		this.formData.startTime = moment().subtract(1, 'months').subtract(1, 'days').startOf('day').valueOf();
		this.formData.endTime = moment().subtract(1, 'days').endOf('day').valueOf();
		this.getList();
	},
	methods: {
		getUrlStringWithParam(param, key, encode) {
		    if(param==null) return '';
		    var paramStr = '';
		    var t = typeof (param);
		    if (t == 'string' || t == 'number' || t == 'boolean') {
		        paramStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);
		    } else {
		        for (var i in param) {
		            var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
		            paramStr += this.getUrlStringWithParam(param[i], k, encode);
		        }
		    }
		    return paramStr;
		},
		getList() {
			http('api/report/anicer/rebate/summary', {
				method: 'get',
				params: {
					startTime: this.formData.startTime,
					endTime: this.formData.endTime,
					optionType : this.formData.optionType,
					optionTypeText : this.formData.optionTypeText,
					advertiserType : this.formData.advertiserType,
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			})
			.then((res) => {
				if (res.data.code == 200) {
					this.list = res.data.data.list;
					this.page.totalCount = res.data.data.totalCount;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		},
		pageSizeChange(size) {
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page) {
			this.page.currentPage = page;
			this.getList();
		},
		search() {
			var that = this;
			this.$refs.formData.validate(function(valid){
				if (!valid) {
					return;
				}
				that.getList();
			});
		}
	},
	watch: {
		exportEndPageRange: {
			handler: function(value) {
				this.exportData.endPage = this.exportData.endPage > value[0] ? this.exportData.endPage : value[0];
			},
			deep: true
		},
		'formData.dateRange': {
			handler: function(value) {
				if (value && value.length > 0 && value[0] !== null) {
					this.formData.startTime = moment(value[0]).startOf('day').valueOf();
					this.formData.endTime = moment(value[1]).endOf('day').valueOf();
				} else {
					this.formData.startTime = moment().subtract(1, 'months').subtract(1, 'days').startOf('day').valueOf();
					this.formData.endTime = moment().subtract(1, 'days').endOf('day').valueOf();
				}
			},
			deep: true
		}
	}
};
</script>
<style scoped>
.amp-account .account-set-new .table-content .table{
	min-width: 100%;
}
.el-custom-con{
	overflow: hidden;
	position: relative;
}
.el-custom-con_wrap{
	margin-bottom: -8px;
	overflow: scroll;
}
.el-custom-con_list{
	position: relative;
	list-style: none;
	padding: 6px 0;
	margin: 0;
	width: 100%;
	max-height: 274px;
	box-sizing: border-box;
	overflow-y: auto;
}
.el-custom-group_wrap{
	list-style: none;
	margin: 0;
	padding: 0;
}
.el-custom-group_wrap-title{
	padding-left: 10px;
    font-size: 12px;
    color: #999;
    height: 30px;
    line-height: 30px;
}
.el-custom-group{
	margin: 0;
    padding: 0;
}
.el-custom-group .el-custom-dropdown__item{
	padding-left: 20px;
	font-size: 14px;
    padding: 3px 10px;
    position: relative;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    color: #475669;
    line-height: 1.5;
    box-sizing: border-box;
    cursor: pointer;
    padding-left: 30px;
}
.el-custom-dropdown__item:hover {
    background-color: #e5e9f2;
}
.el-custom-dropdown__item span {
    line-height: 1.5 !important;
}
.el-custom-dropdown__item.selected::before {
    position: absolute;
    left: 10px;
    top: 50%;
    color: #20a0ff;
    line-height: 1.5;
    margin-top: -8px;
    font-family: icomoon !important;
    content: "\e917";
    font-size: 12px;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}
.table{table-layout:auto;}
.amp-account .account-set-new{
	margin-top: 30px;
}
.btn-light-blue{
	padding: 0px 10px;
    height: 34px;
    line-height: 34px;
    font-size: 12px;
    color: #fff;
    background: #58B7FF;
}
.btn-blue{
	margin-left: 10px;
	padding: 0px 10px;
    height: 34px;
    line-height: 34px;
    font-size: 12px;
    color: #fff;
    background: #1D8CE0;
}
</style>