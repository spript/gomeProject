<template>
	<div>
		<m-breadcrumb :config="breadcrumbConfig"></m-breadcrumb>
		<div class="account-set">
			<div class="set-option">
			<!-- 选择文件-->
			<div class="uplode-file"><a href="#" class="btn btn-normal">上传商家资质</a>
				<input type="button" class="file-input" @click="create">
			</div>
		</div>
		<div class="set-fn">
			<div class="fn-plan"><span class="plan-title">资质名称：</span>
				<el-input v-model="so.keyword" placeholder="请输入资质名称" style="width:170px;display:inline-block;">
				</el-input>
			</div>
			<div class="fn-search"><a href="#" class="btn btn-primary" @click.prevent="getList">查询</a></div>
		</div>
	</div>
	<div class="shop-wrap">
		<table class="table table-zebra table-align-left">
			<tbody>
				<tr>
					<th :width="100"><span class="first-child">ID</span></th>
					<th :width="219"><span>资质名称</span></th>
					<th :width="219"><span>图片</span></th>
					<th :width="219"><span>有效期</span></th>
					<th :width="219"><span>操作</span></th>
				</tr>
				<tr v-for="certificate in list">
					<td><span class="first-child">{{certificate.certificateId}}</span></td>
					<td><span>{{certificate.name}}</span></td>
					<td>
						<span class="table-imgs">
							<img :src="image" v-for="image in certificate.images.split(',')">
						</span>
					</td>
					<td><span>{{certificate.startTime | moment('YYYY/MM/DD')}} - {{certificate.endTime | formatTime('YYYY/MM/DD', maxTime, maxString)}}</span></td>
					<td><span>
						<b class="operat-link">
						<a href="" @click.prevent="modify(certificate.certificateId)">修改</a>
						<a href="" @click.prevent="del(certificate.certificateId)">删除</a>
						</b>
					</span></td>
				</tr>
			</tbody>
		</table>
	</div>
	<el-pagination style="margin-top:20px;" v-show="Math.floor(page.totalCount/page.pageSize)>0"
		@size-change="pageSizeChange"
		@current-change="currentPageChange"
		:current-page="page.currentPage"
		:page-sizes="page.pageSizes"
		:page-size="page.pageSize"
		layout="total, sizes, prev, pager, next"
		:class="{'el-pagination-reset': true}"
		:total="page.totalCount">
	</el-pagination>
	<m-create-drawer :config="createDrawerConfig"></m-create-drawer>
	<m-modify-drawer :config="modifyDrawerConfig"></m-modify-drawer>
	</div>
</template>
<script>
import http from "http";
import store from 'store';
import actions from 'actions';
import Event from 'event';
import {formatDate,mixin} from 'utils/common';
import Breadcrumb from '../../common/breadcrumb.vue';
import createDrawer from "./certificateCreateDrawer";
import modifyDrawer from "./certificateModifyDrawer";
import CONST from '../../../help/CONST';
import {TEXT_MESSAGE} from '../../../help/textMessage';

export default {
	name: 'app-account-certificate',
	data() {
		return {
			createDrawerConfig: {
				show: false,
				certificate: {
					name: "",
					images: [],
					startTime: "",
					endTime: "",
					state: {
						options: [{
							label: "商家资质",
							value: 1
						},{
							label: "品牌权限",
							value: 2
						}],
						value: 1
					}
				}
			},
			modifyDrawerConfig: {
				show:false,
				certificate:{
					name: "",
					images: "",
					startTime: "",
					endTime: "",
				}
			},
			breadcrumbConfig: {
				msg: '商家资质',
				showBack: false
			},
			so: {
				keyword: "",
			},
			list: [],
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			},
			maxTime: CONST.TIMESTAMP_2038_01_01,
			maxString: TEXT_MESSAGE.commonMessage.longTermEffective,
		};
	},
	components: {
		'm-breadcrumb': Breadcrumb,
		'm-create-drawer': createDrawer,
		'm-modify-drawer': modifyDrawer
	},
	created() {
		var that = this;
		Event.$on("certificate-save-success", function(){
			that.getList();
		});
		this.getList();
	},
	methods: {
		getList() {
			var so = this.so, that = this;
			http.get("/api/certificates", {
				params: {
					keyword: so.keyword,
					page: this.page.currentPage,
					number: this.page.pageSize
				}
			})
			.then(function(res) {
				if (res.data.code == 200) {
					that.list = res.data.data.list;
					that.page.totalCount = res.data.data.totalCount;
				}
			})
			.catch(function(error) {
				alert("list fail");
			});
		},
		modify(id){
			var that = this;
			this.modifyDrawerConfig.show = true;
			http.get("/api/certificate", {
				params: {
					certificateId: id
				}
			})
			.then(function(res) {
				if (res.data.code == 200) {
					that.modifyDrawerConfig.certificate = res.data.data;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		},
		del(id){
			var that = this;
			http.delete("/api/certificate", {
				params: {
					certificateId: id
				}
			})
			.then(function(res) {
				if (res.data.code == 200) {
					that.getList();
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		},
		create(){
			this.createDrawerConfig.show = true;
		},
		pageSizeChange(size){
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page){
			this.page.currentPage = page;
			this.getList();
		}
	}
};
</script>
