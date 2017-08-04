<template>
	<div>
		<div class="account-set">
			<div class="set-fn"><span class="btn btn-normal" @click.prevent="newPage">新建页面</span></div>
			<br>
			<div class="set-fn">
				<div class="fn-plan">
					<span class="plan-title">页面名称： </span>
					<el-input v-model="search.name" :maxlength="30" placeholder="请输入页面名称" style="width:170px;display:inline-block;">
						<i slot="icon" class="el-input__icon el-icon-close" @click="handleClickIcon()" v-if="isName"></i>
					</el-input>
				</div>
				<div class="fn-plan fn-plan-new">
				<span class="plan-title">设备： </span>
					<el-select v-model="search.platform" style="width:130px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in platform.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="fn-plan fn-plan-new">
				<span class="plan-title">状态： </span>
					<el-select v-model="search.status" style="width:130px;display:inline-block;" placeholder="全部">
						<el-option v-for="item in state.options" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="fn-search"><span class="btn btn-primary" @click="searchHandle()">查询</span></div>
			</div>
		</div>
		<div class="amp-data">
			<div id="con-table-change" class="data-table">
				<table class="table-list">
					<thead class="list-header" style="text-align:left;">
						<th class="span-col-2"><i class="data-id">ID</i></th>
						<th class="span-col-2">页面名称</th>
						<th class="span-col-2">设备</th>
						<th class="span-col-2">模板类型</th>
						<th class="span-col-2">页面URL</th>
						<th class="span-col-2">创建时间</th>
						<th class="span-col-2">状态</th>
						<th class="span-col-2">操作</th>
					</thead>
					<tbody class="list-body">
						<tr class="body-row" v-for="item in list">
							<td class="span-col-4"><i class="data-id">{{item.pageId}}</i></td>
							<td class="span-col-4">{{item.name}}</td>
							<td class="span-col-4">{{item.platform}}</td>
							<td class="span-col-4">{{item.pageTemplateName}}</td>
							<td class="span-col-4">
								<span :title="item.landingPage" class="landingPage">
									{{item.landingPage}}
								</span>
							</td>
							<td class="span-col-4">{{item.createTime}}</td>
							<td class="span-col-4">{{item.status}}</td>
							<td class="span-col-4">
								<b class="operat-link">
									<a href="javascript:void(0)" @click.prevent="handlePreview(item.pageId)" style="font-weight:normal;margin-right:10px;">预览</a>
									<a href="javascript:void(0)" v-show="item.status =='草稿' || item.status =='发布中'" @click.prevent="modify(item.pageId)" style="font-weight:normal;">修改</a>
								</b>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<el-pagination v-show="Math.floor(page.totalCount/page.pageSize)>0"
		@size-change="pageSizeChange"
		@current-change="currentPageChange"
		:current-page="page.currentPage"
		:page-sizes="page.pageSizes"
		:page-size="page.pageSize"
		layout="total, sizes, prev, pager, next"
		:class="{'el-pagination-reset': true}"
		:total="page.totalCount">
		</el-pagination>
		<m-drawer :pageId="pageId"></m-drawer>
		<m-preview v-if="showChildComponents.pagePreview" :showPreview="showChildComponents.pagePreview" :pageId="pageId"></m-preview>
	</div>
</template>
<script>
import Vue from 'Vue';
import Http from 'utils/http';
import Event from 'event';
import { formatDate, debounce } from 'utils/common';
import actions from 'actions';
import Drawer from './drawer.vue';
import pagePreview from './pagePreview.vue';
export default {
	name: 'app-page-list',
	data() {
		return{
			showChildComponents: {
				pagePreview: false
			},
			pageId:'',
			previewId:'',
			state: {
				options: [{
					label: "全部",
					value: ""
				},{
					label: "草稿",
					value: "0"
				},{
					label: "发布",
					value: "1"
				},{
					label: "发布中",
					value: "2"
				}],
				value: ""
			},
			platform: {
				options: [{
					label: "全部",
					value: ""
				},{
					label: "无线",
					value: "1"
				},{
					label: "PC",
					value: "3"
				}],
				value: ""
			},
			page: {
				totalCount: 0,
				currentPage: 1,
				pageSizes: [20, 30, 50],
				pageSize: 30
			},
			list: [],
			search: {
				name: '',
				platform: '',
				status: ''
			},
		};
	},
	created() {
		this.getList();
	},
	computed: {
		isName() {
			return this.search.name.length > 0;
		}
	},
	components: {
	'm-drawer': Drawer,
	'm-preview': pagePreview
	},
	mounted() {
		let vm = this;
		Event.$on('close-preview', () => {
			vm.showChildComponents.pagePreview = false;
		});
		Event.$on('get-list', () =>{
			this.getList();
		});
		Event.$on('clear-pageId', () => {
			this.pageId = '';
		});
	},
	methods: {
		pageSizeChange(size) {
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page) {
			this.page.currentPage = page;
			this.getList();
		},
		handleClickIcon() {
			this.search.name = '';
		},
		handlePreview(id) {
			this.pageId = id;
			this.showChildComponents.pagePreview = true;
		},
		searchHandle() {
			this.getList();
		},
		getList() {
			let search = this.search, vm = this;
			Http.get('/api/pages', {
				params: {
					name: search.name,
					platform: search.platform ? search.platform : '',
					status: search.status ? search.status : '',
					page: vm.page.currentPage,
					number: vm.page.pageSize
				}
			}).then((res) => {
				vm.list = [];
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.list = res.data.data.list;
					vm.list.forEach((item, index) => {
					item.createTime = formatDate(item.createTime, 'yyyy年MM月dd日');
					item.platform = item.platform == 1 ? '无线' : 'PC';
					item.status = item.status == 0 ? '草稿' : (item.status == 1 ? '已发布' : (item.status == 2 ? '发布中' : '已删除'));
					});
					vm.page.totalCount = res.data.data.totalCount;
				} else {
					vm.$message({
						message: res.data.msg,
						type: 'error'
					});
				}
				}).catch((error) => {
					console.log(error);
				});
		},
		newPage() {
			Event.$emit('clear-pageId');
			actions.controlDrawer(this.$store, {
				show: true,
				action: 'new',
				type: 'template',
				config: {}
			});
		},
		modify(id){
			this.pageId = id;
			this.getData(id, (data) => {
				actions.controlDrawer(this.$store, {
					show: true,
					action: 'modify',
					type: 'edit',
					config: data
				});
			});
		},
		getData(id, cb){
			Http.get("/api/page",{
				params:{pageId: id}
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
	}
};
</script>
<style scoped>
th {line-height: 35px;}
tr {text-align: left;}
i {font-style: normal;}
.a_st {padding: 0 10px;}
.landingPage{
	text-overflow: ellipsis;
	overflow: hidden;
	white-space: nowrap;
	width: 280px;
	padding: 0 20px 0 0;
}
.table-list td.span-col-4 {
	width: 155px;
	height: 60px;
}
.table-list {
	margin-bottom: 8px;
	font-size: 14px;
	min-width: 100%;
	color: #23272c;
	width: 155px;
	height: 60px;
}
table{text-align: left;}
th:first-child, td:first-child{padding-left: 20px;}
</style>
