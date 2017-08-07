<template>
	<div>
		<div class="account-set">
			<div v-if="approveStatus === 0 || approveStatus === -1" class="set-fn">
				<div class="fn-search">
					<span class="btn btn-normal" @click='listHandle'>批量通过</span>
				</div>
			</div>
			<div class="set-fn">
				<div class="fn-plan set-fn-new">
					<span class="plan-title">广告位名称：</span>
					<el-input v-model="search.advertisementName" :maxlength="30" placeholder="请输入广告位名称" style="width:140px;">
						<i slot="icon"
							class="el-input__icon el-icon-close"
							@click="handleClickIcon('advertisement')"
							v-if="isAdvertisementName">
						</i>
					</el-input>
				</div>
				<div class="fn-plan">
					<span class="plan-title">广告主名称：</span>
					<el-input v-model="search.advertiserName" :maxlength="30" placeholder="请输入广告主名称" style="width:140px;">
						<i slot="icon"
							class="el-input__icon el-icon-close"
							@click="handleClickIcon('advertiser')"
							v-if="isAdvertiserName">
						</i>
					</el-input>
				</div>
				<div class="fn-plan">
					<span class="plan-title">送审时间：</span>
                	<el-date-picker v-model="searchDateRange" type="daterange" align="right" placeholder="选择日期范围" :picker-options="datePickOptions" :editable="false" style="width:220px;"></el-date-picker>
                </div>
                <div class="fn-plan">
                	<span class="plan-title">广告类型：</span>
                	<el-select v-model="search.advertisementType" placeholder="请选择"  style="width:80px;display:inline-block;">
    					<el-option v-for="item in advertisementTypeOptions" :label="item.label" :value="item.value"></el-option>
    				</el-select>
                </div>
                <div class="fn-plan">
                	<span class="plan-title">账号类型：</span>
                	<el-select v-model="search.accountType" placeholder="请选择"  style="width:130px;display:inline-block;">
    					<el-option v-for="item in accountTypeOptions" :label="item.label" :value="item.value"></el-option>
    				</el-select>
                </div>
                <div class="fn-search"><span class="btn btn-primary" @click='searchHandle'>查询</span></div>
			</div>
		</div>
		<div class="amp-data">
			<div class="data-table">
				<el-table :data="list" style="width: 100%" border @selection-change="handleSelectionChange" @sort-change="handleSortChange">
					<el-table-column v-if="approveStatus === 0 || approveStatus === -1" type="selection" width="48" :resizable="false"></el-table-column>
					<el-table-column label="素材来源ID/素材名称" width="100" :resizable="false">
						<template scope="scope">	
							<span :title="scope.row.materialId + '/' + scope.row.name"><i class="ellipsis">{{ scope.row.materialId }}/<br/>{{ scope.row.name }}</i></span>
						</template>
					</el-table-column>
					<el-table-column label="广告位ID/广告位名称" width="120" :resizable="false">
						<template scope="scope">	
							<span :title="(scope.row.advertisementId === 0 ? '' : scope.row.advertisementId) + '/' +scope.row.advertisementName"><i class="ellipsis">{{ scope.row.advertisementId }}/<br/>{{ scope.row.advertisementName }}</i></span>
						</template>
					</el-table-column>
					<el-table-column label="素材预览" width="110" :resizable="false">
						<template scope="scope">
							<img :src="scope.row.image" @click="handleImageClick(scope.row.image)">
						</template>
					</el-table-column>
					<el-table-column label="着陆页类型/地址" width="125" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.linkTypeString + '/' + (scope.row.linkType !== 2 ? scope.row.promotionId : scope.row.landingPage)">
								<i class="ellipsis">{{ scope.row.linkTypeString }}/<br/>{{ scope.row.linkType !== 2 ? scope.row.promotionId : scope.row.landingPage }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column label="广告主名称/账号类型" width="125" :resizable="false">
						<template scope="scope">
							<span :title="scope.row.companyName + '/' + scope.row.companyNature">
								<i class="ellipsis">{{ scope.row.companyName }}/<br/>{{ scope.row.companyNature }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column label="投放计划ID/计划有效期" width="125" :resizable="false">
						<template scope="scope">
							<span :title="(scope.row.campaignId === 0 ? '' : scope.row.campaignId) + '/' + scope.row.startTime + '~' + scope.row.endTime">
								<i class="ellipsis">{{ scope.row.campaignId }}/<br/>{{ scope.row.startTime }}~{{ scope.row.endTime }}</i>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="送审时间" width="125" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus === 0 && approveRole == 'advertisement_approve'" label="目前状态" width="125" :resizable="false" prop="approvalStatus"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="approveTime" label="操作时间" width="125" :resizable="false" :render-header="renderHeader"></el-table-column>
					<el-table-column v-if="approveStatus !== 0" prop="accountName" label="操作人" width="75" :resizable="false"></el-table-column>
					<el-table-column label="审核操作" width="120" :resizable="false">
						<template scope="scope">
							<i class="el-icon-search" style="padding:5px;cursor:pointer;" @click="handleView(scope.$index, scope.row)"></i>
							<i v-if="approveStatus !== 1" class="el-icon-check" style="padding:5px;cursor:pointer;" @click="handlePass(scope.$index, scope.row)"></i>
							<i v-if="approveStatus !== -1" class="el-icon-close" style="padding:5px;cursor:pointer;" @click="handleRefuse(scope.$index, scope.row)"></i>
					      </template>
					</el-table-column>
				</el-table>
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
		<m-view :showView="showChildComponents.showMaterialView" :materialId="viewMaterialId" :userId="viewUserId" :approveStatus="approveStatus" :approveRole="approveRole"></m-view>
		<m-dialog :showDialog="showChildComponents.showMaterialPassDialog" :passType="passType" :materialIds="materialIds"></m-dialog>
		<m-refuse :showRefuse="showChildComponents.showMaterialRefuse" :materialId="viewMaterialId" ></m-refuse>
		<m-image :showImage="showChildComponents.showViewImage" :img="imageUrl"></m-image>
	</div>
</template>
<script>
import Vue from 'Vue';
import Http from 'utils/http';
import Event from 'event';
import { formatDate, debounce } from 'utils/common';
import materialView from './materialView.vue';
import passDialog from './makeSureDialog.vue';
import viewImage from './viewImage.vue';
import materialRefuse from './materialRefuse.vue';
export default {
	name: 'app-material-not-list',
	data() {
		return {
			showChildComponents: {
				showMaterialView: false,
				showMaterialPassDialog: false,
				showMaterialRefuse: false,
				showViewImage: false
			},
			passType: 'single',
			viewMaterialId: '',
			viewUserId: '',
			imageUrl: '',
			approveRole:'',
			order: {
				order: 'ascending',
				orderBy: 'createTime'
			},
			search: {
				advertisementName: '',
				advertiserName: '',
				startTime: '',
				endTime: '',
				advertisementType: 0,
				accountType: 0,
				orderBy: '',
				order: ''
			},
			page: {
		        totalCount: 0,
		        currentPage: 1,
		        pageSizes: [20, 30, 50],
		        pageSize: 30
		    },
		    materialIds: [],
		    mutilMaterials: [],
			list: [],
			advertisementTypeOptions: [{
				value: 1,
				label: '图片'
			}, {
				value: 2,
				label: '图文'
			}, {
				value: 3,
				label: '视频'
			}, {
				value: 4,
				label: 'flash'
			}, {
				value: 5,
				label: '文字'
			}, {
				value: 0,
				label: '全部'
			}],
			accountTypeOptions: [{
				value: 3,
				label: '入驻商家'
			}, {
				value: 2,
				label: '代理商'
			}, {
				value: 1,
				label: '运营'
			}, {
				value: 0,
				label: '全部类型'
			}],
			searchDateRange: '',
			datePickOptions: {
				shortcuts: [{
					text: '今天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						picker.$emit('pick', [start, end]);
						console.log('click today');
					}
				}, {
					text: '最近3天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 2);
						picker.$emit('pick', [start, end]);
						console.log('click three days');
					}
				}, {
					text: '最近7天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 6);
						picker.$emit('pick', [start, end]);
						console.log('clcik seven days');
					}
				}, {
					text: '最近15天',
					onClick(picker) {
						let start = new Date();
						let end = new Date();
						start.setTime(start.getTime() - 3600 * 1000 * 24 * 14);
						picker.$emit('pick', [start, end]);
						console.log('clcik fifteen days');
					}
				}, {
					text: '全部',
					onClick(picker) {
						picker.$emit('pick', [null,null]);
						console.log('all');
					}
				}],
				disabledDate(time) {
					return time.getTime() > Date.now();
				}
			}
		};
	},
	components: {
		'm-view': materialView,
		'm-dialog': passDialog,
		'm-image': viewImage,
		'm-refuse': materialRefuse 
	},
	created() {
		let route = this.$route;
		if (this.approveStatus === 0 ) {
			this.search.order = 'asc';
			this.search.orderBy = 'createTime';
		} else {
			this.search.order = 'desc';
			this.search.orderBy = 'approveTime';
		}
		this.getList();
	},
	computed: {
		approveStatus() {
			let route = this.$route;
			return route.name === 'material-not' ? 0 : (route.name === 'material-passed' ? 1 : -1);
		},
		orderType() {
			return this.order.order === 'ascending' ? 'asc' : 'desc';
		},
		isAdvertisementName() {
			return this.search.advertisementName.length > 0;
		},
		isAdvertiserName() {
			return this.search.advertiserName.length > 0;
		}
	},
	mounted() {
		let vm = this;
		Event.$on('close-view', () => {
			vm.showChildComponents.showMaterialView = false;
			document.body.style.overflow = 'auto';

		});
		Event.$on('close-dialog', () => {
			vm.showChildComponents.showMaterialPassDialog = false;
			if (vm.passType === 'single') {
				vm.materialIds = [];
			}
		});
		Event.$on('close-refuse', () => {
			vm.showChildComponents.showMaterialRefuse = false;
			vm.materialId = '';
			document.body.style.overflow = 'auto';
		});
		Event.$on('show-dialog', () => {
			vm.showChildComponents.showMaterialPassDialog = true;
		});
		Event.$on('show-refuse', () => {
			vm.showChildComponents.showMaterialRefuse = true;
		});
		Event.$on('close-image', () => {
			vm.showChildComponents.showViewImage = false;
		});
		Event.$on('pass-material', () => {
			vm.showChildComponents.showMaterialView = false;
			vm.showChildComponents.showMaterialPassDialog = false;
			document.body.style.overflow = 'auto';
			vm.getList();
		});
		Event.$on('refuse-material', () => {
			vm.showChildComponents.showMaterialRefuse = false;
			vm.showChildComponents.showMaterialView = false;
			document.body.style.overflow = 'auto';
			vm.getList();
		});
	},
	methods: {
		handleClickIcon(type) {
			if (type === 'advertisement') {
				this.search.advertisementName = '';
			} else {
				this.search.advertiserName = '';
			}
		},
		handleSelectionChange(val) {
			this.mutilMaterials = [];
			val.forEach((item,index) => {
				this.mutilMaterials.push(item.materialId);
			});
		},
		handleSortChange(sort) {
			this.list = [];
			this.getList();
		},
		handleImageClick(image) {
			this.imageUrl = image;
			this.showChildComponents.showViewImage = true;
		},
		handleOrderClick(index, label) {
			if (label === '送审时间' && this.order.orderBy !== 'createTime'){
				this.order.orderBy = 'createTime';
				this.order.order = 'ascending';
			} else if(label === '操作时间' && this.order.orderBy !== 'approveTime') {
				this.order.orderBy = 'approveTime';
				this.order.order = 'ascending';
			}else {
				this.order.order = this.order.order === 'ascending' ? 'descending' : 'ascending';
			}
			this.search.order = this.orderType;
			this.search.orderBy = this.order.orderBy;
			this.getList();
		},
		renderHeader(h, {column, $index}) {
			if (column.label === '送审时间') {
				column.order = this.order.orderBy === 'createTime' ? this.order.order : '';
			} else if (column.label === '操作时间') {
				column.order = this.order.orderBy === 'approveTime' ? this.order.order : '';
			}
			return h('div', {
				on: {
					click: () => { this.handleOrderClick($index, column.label); }
				}
			}, [
					column.label,
					h('span', {'class': 'caret-wrapper'}, [
								h('i', {'class': 'sort-caret ascending'}),
								h('i', {'class': 'sort-caret descending'})
					])
				]);
		},
		handleView(index, item) {
			this.viewMaterialId = item.materialId;
			this.viewUserId = item.userId;
			this.approveRole = item.approveRole;
			this.materialIds = [this.viewMaterialId];
			this.passType = 'single';
			this.showChildComponents.showMaterialView = true;
			if (this.showChildComponents.showMaterialView = true) {
				document.body.style.overflow = 'hidden';
			}
		},
		handlePass(index, item) {
			this.viewMaterialId = item.materialId;
			this.materialIds = [this.viewMaterialId];
			this.passType = 'single';
			this.showChildComponents.showMaterialPassDialog = true;
		},
		handleRefuse(index, item) {
			this.viewMaterialId = item.materialId;
			this.showChildComponents.showMaterialRefuse = true;
			document.body.style.overflow = 'hidden';
    	},
		searchHandle() {
			this.getList();
		},
		listHandle() {
			this.materialIds = this.mutilMaterials;
			if (this.materialIds.length > 0) {
				this.passType = 'mutil';
				this.showChildComponents.showMaterialPassDialog = true;
			} else {
				this.$message({
					message: '您没有选中任何素材！',
					type: 'warning'
				});
			}
		},
		getList() {
			let search = this.search, vm = this;
			Http.get('/api/approval/materials', {
				params: {
					advertisementName: search.advertisementName,
					advertiserName: search.advertiserName,
					startTime: search.startTime ? search.startTime : '',
					endTime: search.endTime ? search.endTime : '',
					advertisementType: search.advertisementType === 0 ? '' : search.advertisementType,
					accountType: search.accountType === 0 ? '' : search.accountType,
					page: vm.page.currentPage,
					number: vm.page.pageSize,
					approveStatus: vm.approveStatus,
					order: !!search.order ? search.order : 'asc',
					orderBy: !!search.orderBy ? search.orderBy : 'createTime'
				}
			}).then((res) => {
				vm.list = [];
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.list = res.data.data.list;
					vm.list.forEach((item, index) => {
						item.startTime = item.campaignId === 0 ? '' : formatDate(item.startTime, 'yyyy.MM.dd');
						item.endTime = item.campaignId === 0 ? '' : (item.isUnlimited == 1 ? '不限' : formatDate(item.endTime, 'yyyy.MM.dd'));
						item.createTime = formatDate(item.createTime, 'yyyy年MM月dd日');
						item.approveTime = formatDate(item.approveTime, 'yyyy年MM月dd日');
						item.linkTypeString = vm.linkTypeString(item.linkType);
						item.companyNature = item.companyNature === 0 ? '' : (item.companyNature == 1 ? '品牌商' : (item.companyNature == 2 ? '代理商' : '入驻商家'));
						if (item.createFrom == 'aos') {
							item.approvalStatus = item.approveLogs.length > 0 ? item.approveLogs[0].approvalStatus === 1 ? '平台运营通过' : (item.approveLogs[0].approvalStatus == -1 ? '平台运营拒绝' : '未审核') : '未审核';

						} else {
							item.approvalStatus = '未审核';
						}
					});
					vm.approveRole = vm.list[0].approveRole;
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
		linkTypeString(type) {
			return type === 0 ? '店铺ID' : (type === 1 ? '商品ID' : (type === 2 ? 'URL' : (type === 3 ? '圈子ID' : (type === 4 ? '话题ID' : '视频'))));
		},
		pageSizeChange(size){
			this.page.pageSize = size;
			this.getList();
		},
		currentPageChange(page){
			this.page.currentPage = page;
			this.getList();
		},
		timeClearHours(date) {
			const cloneDate = new Date(date);
		    cloneDate.setHours(0, 0, 0, 0);
		    return cloneDate.getTime();
		},
		setTimeHours(date) {
			const cloneDate = new Date(date);
			cloneDate.setHours(23, 59, 59, 999);
			return cloneDate.getTime();
		}
	},
	watch: {
		'searchDateRange': {
			handler: function(val) {
				if (!val[0] && !val[1]) {
					this.search.startTime = '';
					this.search.endTime = '';
				} else {
					let start = new Date(val[0]);
					let end = new Date(val[1]);
					this.search.startTime = this.timeClearHours(start);
					this.search.endTime = this.setTimeHours(end);
				}
			},
			deep: true
		},
		'approveStatus': function(val) {
			this.search = {
				advertisementName: '',
				advertiserName: '',
				startTime: '',
				endTime: '',
				advertisementType: 0,
				accountType: 0,
				orderBy: '',
				order: ''
			};
			this.searchDateRange = '';
			let route = this.$route;
			this.materialIds = [];
			this.mutilMaterials = [];
			this.search.order = this.approveStatus === 0 ? 'asc' : 'desc';
			this.search.orderBy = this.approveStatus === 0 ? 'createTime' : 'approveTime';
			this.getList();
		}
	}
};
</script>
<style scoped>
img{cursor: pointer;width:100%;margin: 10px 0;}
table{width: 100% !important;}
.btn{margin-bottom: 20px;}
.ellipsis{font-style: normal;}
.amp-data{margin-top: 0;}
.account-set{margin-top: 40px;}
.fn-plan{margin-bottom:20px;}
.amp-account .account-set {
    padding: 10px 10px;
    background: #fff;
}
</style>
