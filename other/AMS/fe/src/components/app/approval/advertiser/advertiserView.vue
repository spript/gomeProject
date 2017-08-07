<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="showView"></div>
		</transition>
		<transition name="drawer">
			<div class="amp-drawer" v-if="showView">
				<div class="drawer-part">
					<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">证件信息详情</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<div class="dialog-tab">
						<el-tabs type="card" @tab-click="handleClick">
							<el-tab-pane label="营业执照" name="0">
								<div class="unfold-content">
								<div class="column-group">
									<div class="group-aside">公司名称：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.companyName }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">注册号(营业执照号)：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.bizLicenseNumber }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">法人代表名称：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.legalReprName }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">身份证号：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.legalReprIdNumber }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">法人身份证电子版(正面)：</div>
									<div class="group-detail" width="60">
										<div class="detail-row">
											<div class="row-photo-l">
												<div class="photo" :resizable="false"><img :src="advertiser.legalReprIdImage " @click="handleImageClick(advertiser.legalReprIdImage)"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">法人身份证电子版(背面)：</div>
									<div class="group-detail">
										<div class="detail-row">
											<div class="row-photo-l">
												<div class="photo"><img :src="advertiser.legalReprIdBackImage" @click="handleImageClick(advertiser.legalReprIdBackImage)"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="column-group">
					                <div class="group-aside">营业执照详细地址：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.bizLicenseAddress }}</span></div>
					                </div>
								</div>
				                <div class="column-group">
					                <div class="group-aside">成立日期：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.companyFoundDate }}</span></div>
					                </div>
				                </div>
				                <div class="column-group">
					                <div class="group-aside">营业期限：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.bizStartDate }}~{{ advertiser.bizEndDate }}</span></div>
					                </div>
				                </div>
					            <div class="column-group">
					                <div class="group-aside">注册资本：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.regCapital }}</span></div>
					                </div>
					            </div>
								<div class="column-group">
				 					<div class="group-aside">经营范围：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.bizScope }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">营业执照副本电子版：</div>
									<div class="group-detail">
										<div class="detail-row">
					                    	<div class="row-photo-l">
					                      		<div class="photo"><img :src="advertiser.bizLicenseImage" @click="handleImageClick(advertiser.bizLicenseImage)"></div>
					                    	</div>
										</div>
									</div>
					            </div>
								<div class="column-group">
					                <div class="group-aside">公司详细地址：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.companyAddress }}</span></div>
					                </div>
								</div>
				        		<div class="column-group">
					                <div class="group-aside">公司电话：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.companyPhone }}</span></div>
					                </div>
								</div>
								<div class="column-group">
									<div class="group-aside">公司紧急联系人：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.emerContact }}</span></div>
									</div>
								</div>
								<div class="column-group">
									<div class="group-aside">公司紧急联系人手机号：</div>
									<div class="group-detail">
										<div class="detail-row"><span class="row-span">{{ advertiser.emerContactPhone }}</span></div>
									</div>
								</div>
								<div class="column-group">
					                <div class="group-aside">公司邮箱：</div>
					                <div class="group-detail">
					                  <div class="detail-row"><span class="row-span">{{ advertiser.companyEmail }}</span></div>
					                </div>
								</div>
							</div>
							</el-tab-pane>
							<el-tab-pane label="组织机构代码证" name="1">
								<div class="unfold-content-item">
									<div class="column-group">
										<div class="group-aside">组织机构代码：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.orgCode }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">组织机构代码副本电子版：</div>
										<div class="group-detail">
											<div class="detail-row">
											<div class="row-photo-l">
												<div class="photo"><img :src="advertiser.orgCodeImage" @click="handleImageClick(advertiser.orgCodeImage)"></div>
											</div>
											</div>
										</div>
									</div>
								</div>
							</el-tab-pane>
							<el-tab-pane label="税务登记证" name="2">
								<div class="unfold-content-item">
									<div class="column-group">
										<div class="group-aside">税号：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.taxNumber }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">纳税人类型：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.taxpayerType }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">纳税类型税码：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.taxCode }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">税务登记证电子版：</div>
										<div class="group-detail">
											<div class="detail-row">
												<div class="row-photo-1">
													<div class="photo"><img :src="advertiser.taxRegCertImage" @click="handleImageClick(advertiser.taxRegCertImage)"></div>
												</div>
											</div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">一般纳税人资格电子版：</div>
										<div class="group-detail">
											<div class="detail-row">
												<div class="row-photo-1">
													<div class="photo"><img :src="advertiser.taxpayerQualificationImage" @click="handleImageClick(advertiser.taxpayerQualificationImage	)"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</el-tab-pane>
							<el-tab-pane label="开户银行许可证" name="3">
								<div class="unfold-content-item">
									<div class="column-group">
										<div class="group-aside">银行开户名：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.bankAccountName}}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">公司银行账号：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.bankAccount }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">开户银行支行名称：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.bankBranchName }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">开户银行支行联行号：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.bankBranchLineNumber }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">开户银行支行地址：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.bankBranchAddress }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">银行开户许可证电子版：</div>
										<div class="group-detail">
											<div class="detail-row">
												<div class="row-photo-1">
													<div class="photo"><img :src="advertiser.bankAccountPermissionImage" @click="handleImageClick(advertiser.bankAccountPermissionImage)"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</el-tab-pane>
							<el-tab-pane label="联系人" name="4">
								<div class="unfold-content-item">
									<div class="column-group">
										<div class="group-aside">联系人：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.name }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">性别：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.gender }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">手机号：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.mobile }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">邮箱：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.email }}</span></div>
										</div>
									</div>
									<div class="column-group">
										<div class="group-aside">通讯地址：</div>
										<div class="group-detail">
											<div class="detail-row"><span class="row-span">{{ advertiser.address }}</span></div>
										</div>
									</div>
								</div>
							</el-tab-pane>
						</el-tabs>
					</div>
				</div>
				<div class="drawer-btn">
					<span class="btn btn-primary" v-if="approveStatus !== 1" @click="passDialogHandler">通过</span>
					<span class="btn btn-gray" v-if="approveStatus === 0" @click="viewRefuseHandler">拒绝</span>
					<span class="btn btn-gray" @click="closeView">取消</span>
				</div>
			</div>
		</transition>
		<m-image :showImage="showChildComponents.showViewImage" :img="imageUrl"></m-image>
	</div>
</template>
<script>
import Event from 'event';
import Http from 'utils/http';
import { formatDate, debounce } from 'utils/common';
import viewImage from './viewImage.vue';
export default {
	name: 'advertiser-view-dialog',
	props: ['showView', 'advertiserId', 'approveStatus'],
	data() {
		return {
			selected: 0,
			advertiser:{},
			showChildComponents: {
				showViewImage: false
			},
			imageUrl: '',
		};
	},
	created() {
		this.getAdvertiser();
	},
	components: {
		'm-image': viewImage
	},
	mounted() {
		let vm = this;
		Event.$on('close-image', () => {
			vm.showChildComponents.showViewImage = false;
		});
	},
	methods: {

		handleClick(tab, event) {
			console.log(tab, event);
		},

		passDialogHandler() {
			Event.$emit('show-dialog');
		},
		viewRefuseHandler() {
			Event.$emit('show-refuse');
		},
		handleImageClick(image) {
			this.imageUrl = image;
			this.showChildComponents.showViewImage = true;
		},
		closeView() {
			Event.$emit('close-view');
			this.selected = 0;
		},
		clickLeftTab(index) {
			this.selected = index;
		},
		taxPayerString(type) {
			return type === 1 ? '一般纳税人': (type === 2 ? '小规模纳税人': '非增值税纳税人');
		},
		genderString(gender) {
			return gender === 1 ? '女':'男';
		},
		getAdvertiser() {
			let vm = this;
			Http.get('/api/approval/advertiser', {
				params: {
					advertiserId: vm.advertiserId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.advertiser = res.data.data;
					vm.advertiser.taxpayerType = vm.taxPayerString(vm.advertiser.taxpayerType);
					vm.advertiser.gender = vm.genderString(vm.advertiser.gender);
					vm.advertiser.companyFoundDate = formatDate(vm.advertiser.companyFoundDate, 'yyyy年MM月dd日');
					vm.advertiser.bizStartDate = formatDate(vm.advertiser.bizStartDate, 'yyyy年MM月dd日');
					vm.advertiser.bizEndDate = formatDate(vm.advertiser.bizEndDate, 'yyyy年MM月dd日');
				} else {
					vm.$message({
						message: res.data.msg,
						type: 'error'
					});
				}
			}).catch((error) => {
				console.log(error);
			});
		}
	}
};
</script>
<style scoped>
.dialog-tab {
    padding: 0 20px;
}
.left-menu{height: 60px;cursor: pointer; text-align: center;line-height: 60px;border-bottom: 1px #ddd solid;border-right: 1px #ddd solid;background: #f0f3f6;}
.part-header {text-align: center;border-bottom: 1px #ddd solid;border-right: 1px #ddd solid;}
.header-text {float: none!important;}
.unfold-content {float: left;}
.unfold-content{width: 850px;}
.column-header{height: 40px;line-height: 40px;text-align: center;border-bottom: 1px #ddd solid;}
.column-header span{display: inline-block;}
div .column-group {text-align: left;}
.group-aside{width: 180px;}
img{cursor: pointer;width:20%;height:20%;margin: 10px 0;}
</style>
