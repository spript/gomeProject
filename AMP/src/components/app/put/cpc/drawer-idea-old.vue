<template>
	<div class="amp-form amp-form03">
		<div class="form-column">
			<el-form :model="formData" :rules="rules" ref="formData" label-width="124px">
				<el-form-item label="创意名称：" prop="name">
					<el-input v-model="formData.name" placeholder="请输入创意名称"></el-input>
				</el-form-item>
				<el-form-item label="链接类型：">
					<el-select v-model="formData.linkType">
						<el-option v-for="item in linkType" :label="item.name" :value="item.value" :disabled="item.disabled"></el-option>
					</el-select>
				</el-form-item>
				<div>
					<el-form-item :label="linkType[formData.linkType - 1].name + '：'" prop="itemId">
						<!-- <el-autocomplete v-model="productId" :fetch-suggestions="querySearchProductAsync" custom-item="autocomplete-item" placeholder="请输入商品编号" :trigger-on-focus="false" @select="handleProductSelect"></el-autocomplete> -->
						<el-input v-model="formData.itemId" placeholder="请输入商品编号" @blur="queryItemId" :disabled="actionType === 'modify'"></el-input>
					</el-form-item>
					<el-form-item label="广告标题：" prop="title">
						<el-input :disabled="!itemCan" placeholder="请输入正确的商品编号后修改" v-model="formData.title"></el-input>
					</el-form-item>
					<el-form-item label="创意图片：" prop="image">
						<!-- <el-upload :action="uploadUrl" accept="image/*" :multiple="false" :on-preview="handlePreview" :on-remove="handleRemove" :on-success="handleSuccess" :show-upload-list="false" :before-upload="handleBeforeUpload">
							<el-button size="small" type="primary">点击上传</el-button>
							<div class="el-upload__tip" slot="tip">只能上传图片文件</div>
						</el-upload> -->
						<span v-if="!allImgList.length">无</span>
						<div v-else class="uploade-show uploade-select">
							<ul>
								<li @click.prevent.stop="selectImg(index)" v-for="(item, index) in allImgList" :class="{'actived': item.checked}"><span class="show-img"><img :src="item.url" alt=""></span></li>
							</ul>
						</div>
					</el-form-item>
					<el-form-item label="广告文案：" prop="description">
						<el-input :disabled="!itemCan" v-model="formData.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" placeholder="请输入正确的商品编号后修改"></el-input>
					</el-form-item>
					<el-form-item label="联合推广商品：" v-show="isRebate">
						<el-row>
							<el-col :span="8" style="padding-right:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[0].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(0)" @change="handleUnitProudctChange(0)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[0].isError">{{ unitProductIds[0].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding:0 5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[1].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(1)" @change="handleUnitProudctChange(1)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[1].isError">{{ unitProductIds[1].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding-left:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[2].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(2)" @change="handleUnitProudctChange(2)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[2].isError">{{ unitProductIds[2].errorMsg }}</div>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-show="unitProductCount > 3">
							<el-col :span="8" style="padding-right:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[3].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(3)" @change="handleUnitProudctChange(3)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[3].isError">{{ unitProductIds[3].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding:0 5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[4].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(4)" @change="handleUnitProudctChange(4)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[4].isError">{{ unitProductIds[4].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding-left:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[5].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(5)" @change="handleUnitProudctChange(5)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[5].isError">{{ unitProductIds[5].errorMsg }}</div>
								</el-form-item>
							</el-col>
						</el-row>
						<el-row v-show="unitProductCount > 6">
							<el-col :span="8" style="padding-right:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[6].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(6)" @change="handleUnitProudctChange(6)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[6].isError">{{ unitProductIds[6].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding:0 5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[7].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(7)" @change="handleUnitProudctChange(7)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[7].isError">{{ unitProductIds[7].errorMsg }}</div>
								</el-form-item>
							</el-col>
							<el-col :span="8" style="padding-left:5px;">
								<el-form-item>
									<el-input v-model="unitProductIds[8].itemId" placeholder="请输入商品编号" @blur="handleUnitProductBlur(8)" @change="handleUnitProudctChange(8)" :disabled="!itemCan"></el-input>
									<div class="el-form-item__error" v-if="unitProductIds[8].isError">{{ unitProductIds[8].errorMsg }}</div>
								</el-form-item>
							</el-col>
						</el-row>
						<div class="add-fn" v-show="unitProductCount !== 9" @click="addUnitProductCount"><span class="btn btn-normal"></span></div>
						<div class="detail-row detail-row-mr">
							<el-checkbox v-model="isAutoAddUnitProduct.category">允许自动填充本店铺同类目商品</el-checkbox>
							<!-- <span class="form-text"><em class="icon icon-help"></em></span> -->
						</div>
						<div class="detail-row detail-row-mr">
							<el-checkbox v-model="isAutoAddUnitProduct.shop">允许自动填充本店铺商品</el-checkbox>
							<!-- <span class="form-text"><em class="icon icon-help"></em></span> -->
						</div>
					</el-form-item>
					<!--
					<el-form-item label="联合推广商品：" v-show="isRebate">
						<el-autocomplete class="autocomplete" v-model="unitePopularId" popper-class="" custom-item="autocomplete-item" :disabled="!itemCan" :trigger-on-focus="false" :fetch-suggestions="querySearchUnitProAsync" placeholder="请输入商品编号" @select="handleSelect"></el-autocomplete>
					</el-form-item>
					<el-form-item style="margin-top: -15px;" v-show="isRebate">
						<ul class="autocomplete list">
							<li v-for="(item, index) in unitePopularArray">
								<div class="title">{{item.name}}({{item.itemId}})</div>
								<div class="desc">{{item.description}}</div>
								<span class="delete" @click="delItem(index)">删除</span>
							</li>
						</ul>
					</el-form-item>
					<el-form-item label="" style="margin-top: -20px;" v-show="isRebate">
						<el-col :span="14">
							<el-button @click="autoAddItem(1)" :disabled="!itemCan">自动填充本店铺同类目商品</el-button>new-->
					<!-- <el-tooltip effect="light" placement="top-end">
                        <div slot="content">
                            <div class="dialog-white dialog-tip" style="font-size: 14px;">
                                <p class="dialog-text"></p>
                            </div>
                        </div>
                        <em class="icon icon-help"></em>
                    </el-tooltip> --><!--new
						</el-col>
						<el-col :span="10">
							<el-button @click="autoAddItem(2)" :disabled="!itemCan">自动填充本店铺商品</el-button>
							<!-- <el-tooltip effect="light" placement="top-end">
								<div slot="content">
									<div class="dialog-white dialog-tip" style="font-size: 14px;">
										<p class="dialog-text"></p>
									</div>
								</div>
								<em class="icon icon-help"></em>
							</el-tooltip> -->
					<!-- </el-col>
                </el-form-item>
-->				</div>
			</el-form>
		</div>
	</div>
</template>
<script>
	import Vue from 'vue';
	import apiConfig from '../../../../config/api.config.js';
	import store from 'store';
	import {
		copyObj,
		objType,
		formatDate,
		mixin,
		debounce,
		getImgSize,
		isEmptyObj
	} from 'utils/common';
	import Event from 'event';
	import Http from 'http';
	Vue.component('autocomplete-item', {
		functional: true,
		render: function(h, ctx) {
			var item = ctx.props.item;
			return h('li', ctx.data, [
				h('div', { attrs: { class: 'title' } }, [item.name + '(' + item.skuId + ')'])
			]);
		},
		props: {
			item: {
				type: Object,
				required: true
			}
		}
	});
	export default {
		name: 'app-put-new-idea',
		data() {
			return {
				formData: {},
				productId: '',
				nProductId: '',
				itemIdError: '',
				rules: {
					name: [{
						required: true,
						message: '请输入创意名称',
						trigger: 'blur'
					}, {
						max: 30,
						message: '最大长度不超过30个汉字',
						trigger: 'blur'
					}],
					itemId: [{
						validator: (rule, value, callback) => {
							if (value === '') {
								this.itemCan = false;
								this.formData.title = '';
								this.formData.description = '';
								this.allImgList = [];
								callback(new Error('请输入' + this.linkType[this.formData.linkType - 1].name));
							} else {
								callback();
							}
						},
						trigger: 'blur',
					}, {
						validator: (rule, value, callback) => {
							if (this.itemIdError !== '') {
								callback(new Error(this.itemIdError));
							} else {
								callback();
							}
						}
					}],
					title: [{
						required: true,
						message: '请输入广告标题',
						trigger: 'blur'
					}, {
						max: 16,
						message: '最大长度不超过16个汉字',
						trigger: 'blur'
					}],
					image: [{
						validator: (rule, value, callback) => {
							if (this.formData.image === '') {
								callback(new Error('请选择创意图片'));
							} else {
								callback();
							}
						}
					}],
					description: [{
						required: true,
						message: '请输入广告文案',
						trigger: 'blur'
					}, {
						max: 80,
						message: '最大长度不超过80个汉字',
						trigger: 'blur'
					}]
				},
				itemCan: false, // 商品编号查询正确与否
				uploadUrl: `${apiConfig[process.env.NODE_ENV]}api/material/imageUpload`,
				allImgList: [], // 某商品所有图片列表
				shopId: '', // 商品所属店铺 ID
				categoryId: '', // 商品所属类目 ID
				linkType: [{
					value: 1,
					name: '商品编号',
					disabled: false
				}, {
					value: 0,
					name: '店铺ID',
					disabled: true
				}, {
					value: 3,
					name: '圈子ID',
					disabled: true
				}, {
					value: 4,
					name: '话题ID',
					disabled: true
				}, {
					value: 2,
					name: 'URL',
					disabled: true
				}],
				unitePopularId: '', // 搜索的联合推广商品 ID
				unitePopularArray: [], // 已选择的联合推广商品列表
				httpMethodMap: {
					new: 'post',
					copy: 'post',
					modify: 'put'
				},
				unitProductIds: [
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' },
					{ itemId: '', isError: false, errorMsg: '' }
				],
				isAutoAddUnitProduct: {
					shop: true,
					category: true
				},
				unitProductCount: 3
			};
		},
		computed: {
			drawerData: () => store.state.drawerCtrl.config, // from store
			drawerCtrlAction: () => store.state.drawerCtrl.action,
			isRebate: () => store.state.drawerCtrl.isRebate, // 是否返利，从 state 中来 如果被复写掉了为 false 达到知晓上一级是否返利的目的
			actionType: () => store.state.drawerCtrl.action
		},
		created() {
			this.formData = copyObj(this.drawerData);
			this.unitePopularArray = [...this.drawerData.relatedItems];
			this.productId = this.formData.itemId;
			// this.formData.uintProductIds = this.uintProductIds
			this.initUnitProduct(this.unitePopularArray);
			if (!this.isRebate) {
				this.isAutoAddUnitProduct.shop = false;
				this.isAutoAddUnitProduct.category = false;
			}
		},
		mounted() {
			if (this.formData.itemId !== '') {
				Http.get('api/material/sku', {
					params: {
						skuId: this.formData.skuId
					}
				}).then((res) => {
					if (res.data.iserror) {
						this.itemCan = false;
					} else {
						console.log(res);
						// this.formData.title = res.data.data.name;
//					this.formData.description = res.data.data.description;
						this.shopId = res.data.data.shopId;
						this.categoryId = res.data.data.categoryId;
						this.productId = res.data.data.productId;
						this.allImgList = [];
						res.data.data.images.forEach((item) => {
							this.allImgList.push({
								url: item,
								checked: item === this.formData.image
							});
						});
						this.itemCan = true;
					}
				});
			}
			Event.$off('put-save');
			Event.$on('put-save', () => {
				this.$refs.formData.validate((result) => {
					if (result) {
						if (this.unitePopularArray.length < this.unitProductCount && (this.isAutoAddUnitProduct.category || this.isAutoAddUnitProduct.shop)) {
							Http.get('api/material/items', {
								params: this.isAutoAddUnitProduct.category ? {
									shopId: this.shopId,
									categoryId: this.categoryId,
									skuId: this.formData.itemId
								} : {
									shopId: this.shopId,
									skuId: this.formData.itemId
								}
							}).then((res) => {
								if(res.status === 200 && res.data.code === 200 && res.data.iserror === 0){
									let lackLen = this.unitProductCount - this.unitePopularArray.length;
									let list = res.data.data.list.slice(0,lackLen);
									this.unitePopularArray = [...this.unitePopularArray,...list];
									this.saveMaterial();
								}
							});
						} else {
							this.saveMaterial();
						}
					} else {
						Event.$emit('put-save-result', {
							error: 1,
							res: null
						});
					}
				});
			});
		},
		methods: {
			addUnitProductCount() {
				this.unitProductCount = this.unitProductCount === 9 ? this.unitProductCount : this.unitProductCount + 3;
			},
			handleUnitProudctChange(index) {
				this.unitProductIds[index].isError = false;
			},
			handleUnitProductBlur(index) {
				let item = this.unitProductIds[index];
				let isContainItem = false;
				for (let i = 0; i < this.unitProductIds.length; i++) {
					if (i !== index) {
						let checkItem = this.unitProductIds[i];
						if (checkItem.itemId === item.itemId && item.itemId.length > 0) {
							isContainItem = true;
							break;
						}
					}
				}
				if (item.itemId === this.formData.itemId) {
					this.unitProductIds[index].isError = true;
					this.unitProductIds[index].errorMsg = '联合推广商品不能包含创意主商品';
				} else if (isContainItem) {
					this.unitProductIds[index].isError = true;
					this.unitProductIds[index].errorMsg = '请填写不同的商品编号';
				} else {
					Http.get('api/material/sku', {
						hideLoading: true,
						params: { skuId: item.itemId }
					}).then((res) => {
						if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
							if (isEmptyObj(res.data.data)) {
								this.unitProductIds[index].isError = true;
								this.unitProductIds[index].errorMsg = '请填写本店铺有效商品编号';
							} else {
								let resultItem = res.data.data;
								this.unitePopularArray.push({
									itemId: resultItem.skuId,
									name: resultItem.name,
									image: resultItem.images[0]
								});
							}
						} else {
							this.$message({
								message: res.data.msg,
								type: 'error'
							});
						}
					});
				}
			},
			queryItemId() {
				if (this.formData.itemId === '') {
					return;
				}
				Http.get('api/material/sku', {
					params: {
						skuId: this.formData.itemId
					}
				})
					.then((res) => {
						if (res.data.iserror) {
							this.itemCan = false;
							this.formData.title = '';
							this.formData.description = '';
							this.shopId = '';
							this.categoryId = '';
							this.allImgList = [];
							this.formData.image = '';
							this.itemIdError = res.data.msg;
							this.$refs.formData.validateField('itemId');
						} else {
							// this.materialId = res.data.data.materialId;
							this.formData.title = res.data.data.name;
							this.formData.description = res.data.data.description;
							this.shopId = res.data.data.shopId;
							this.categoryId = res.data.data.categoryId;
							this.productId = res.data.data.productId;
							this.allImgList = [];
							res.data.data.images.forEach((item) => {
								this.allImgList.push({
									url: item,
									checked: false
								});
							});
							this.allImgList[0].checked = true;
							this.formData.image = this.allImgList[0].url;
							this.itemCan = true;
							this.itemIdError = '';
							this.$refs.formData.validateField('itemId');
						}
					});
			},
			initUnitProduct(unitDatas) {
				for (let i = 0; i < unitDatas.length; i++) {
					this.unitProductIds[i].itemId = unitDatas[i].itemId;
				}
				this.unitProductCount = unitDatas.length > 0 ? unitDatas.length : 3;
			},
			saveMaterial() {
				let size = getImgSize(this.formData.image);
				let params = {
					name: this.formData.name,
					skuId: this.formData.itemId,
					productId : this.productId,
					type: this.formData.type,
					linkType: this.formData.linkType,
					title: this.formData.title,
					image: this.formData.image,
					description: this.formData.description,
					relatedItems: this.unitePopularArray,
					width: size.width,
					height: size.height,
					shopId: this.shopId
				};
				/**去重
				 if (this.unitProductIdsContainMain()) {
				this.$message({
					message: '联合推广商品不能包含创意主商品',
					type: 'error'
				});
			} else {*/
				Http({
					url: 'api/material',
					method: this.httpMethodMap[this.drawerCtrlAction],
					data: this.drawerCtrlAction === 'modify' ? mixin({
						// 修改 创意ID
						materialId: this.formData.materialId
					}, params) : mixin({
						// 新建复制 单元ID
						flightId: this.formData.flightId
					}, params)
				})
					.then((res) => {
						if (!res.data.iserror) {
							Event.$emit('put-save-result', {
								error: 0,
								res: res.data,
								isRebate: this.isRebate
							});
						} else {
							Event.$emit('put-save-result', {
								error: 1,
								res: null
							});
						}
					})
					.catch((e) => {
						Event.$emit('put-save-result', {
							error: 1,
							res: null
						});
					});
				// }
			},
			unitProductIdsContainMain() {
				let isContain = false;
				for (let i = 0; i < this.unitProductIds.length; i++) {
					if (this.unitProductIds[i].itemId === this.formData.itemId) {
						isContain = true;
						break;
					}
				}
				return isContain;
			},
			/**
			 querySearchAsync: debounce(function(value, cb) {
			if (value === '') {
				cb([]);
			} else {
				this.extraSearchTime = Date.now();
				Http.get('api/material/item', {
					hideLoading: true,
					params: {
						itemId: value
					}
				})
				.then((res) => {
					if (!res.data.iserror) {
						cb([{
							...res.data.data,
							value: res.data.data.itemId.toString()
						}]);
					} else {
						cb([{title: '无符合要求的商品', value: '无符合要求的商品'}]);
					}
				});
			}
		}, 500),
			 getSkusByProductId(id, cb) {
			Http.get('/api/material/product', {
					hideLoading: true,
					params: {
						productId: id
					}
				}).then((res) => {
					if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
						this.nProductId = res.data.data.productId;
						let results = res.data.data.sku;
						results.forEach((item, index) => {
							item.value = item.name;
						});
						cb(results);
					} else {
						cb([{title: '无符合要求的商品', value: '无符合要求的商品'}]);
					}
				}).catch((error) => {
					console.log(error);
				});
		},
			 querySearchUnitProAsync: debounce(function(queryString, cb){
			if (queryString === '') {
				cb([]);
			} else {
				this.getSkusByProductId(queryString, cb);
			}
		}, 500),
			 querySearchProductAsync: debounce(function(queryString, cb) {
			if (queryString === '') {
				cb([]);
			} else {
				this.getSkusByProductId(queryString, cb);
			}
		}, 500),
			 handleProductSelect(item) {
			this.formData.itemId = item.skuId;
			this.formData.title = item.name;
			this.formData.description = item.description;
			this.shopId = item.shopId;
			this.categoryId = item.categoryId;
			this.allImgList = [];
			item.images.forEach((item) => {
				this.allImgList.push({
					url: item,
					checked: false
				});
			});
			this.allImgList[0].checked = true;
			this.formData.image = this.allImgList[0].url;
			this.itemCan = true;
			this.itemIdError = '';
			this.productId = this.nProductId;
		},
			 handleSelect(item) {
			if (this.unitePopularArray.length === 9) {
				return this.$message({
					message: '最多选择9个联合商品',
					type: 'error',
					duration: 3000
				});
			}
			for (let i = 0; i < this.unitePopularArray.length; i++) {
				if (this.unitePopularArray[i].skuId === item.skuId) {
					return this.$message({
						message: '商品已存在',
						type: 'error',
						duration: 3000
					});
				}
			}
			if (item.value !== '无符合要求的商品') {
				// this.formData.relatedItems.push(item.itemId);
				this.unitePopularArray.push({
					itemId: item.skuId,
					name: item.name,
					url: '',
					image: item.images[0]
				});
			}
		},
			 delItem(index) {
			// this.formData.relatedItems.splice(index, 1);
			this.unitePopularArray.splice(index, 1);
		},
			 autoAddItem(type) {
			Http.get('api/material/items', {
				params: type === 1 ? {
					shopId: this.shopId,
					categoryId: this.categoryId
				} : {
					shopId: this.shopId
				}
			})
			.then((res) => {
				if(res.status === 200 && res.data.code === 200 && res.data.iserror === 0){
					let lackLen = 9 - this.unitePopularArray.length;
					let list = res.data.data.list.slice(0,lackLen);
					list.forEach((item) => {
						item.skuId = item.itemId;
					});
					this.unitePopularArray = [...this.unitePopularArray,...list];
				}
				// let uniqueIds = this.unitePopularArray.map((item) => {
				// 	return item.itemId;
				// });

				// for (let i = 0; i < res.data.data.list.length; i++) {
				// 	if (uniqueIds.indexOf(item.itemId) === -1 && this.unitePopularArray.length < 9) {
				// 		// this.formData.relatedItems.push(item.itemId);
				// 		this.unitePopularArray.push(item);
				// 	}
				// }
			});
		},*/
			selectImg(index) {
				this.formData.image = this.allImgList[index].url;
				this.allImgList.forEach((item) => {
					item.checked = false;
				});
				this.allImgList[index].checked = true;
				this.$refs.formData.validateField('image');
			}
		}
	};
</script>
<style lang="less">
	.hideList {display: none;}
	.autocomplete {
	li {
		line-height: 24px!important;
		padding: 7px;
		position: relative;
		transition: all ease 0.3s;
	.desc {
		font-size: 12px;
		color: #b4b4b4;
	}

	.highlighted .addr {
		color: #ddd;
	}
	}
	}
	.el-row {
		margin-bottom: 20px;
	&:last-child {
		 margin-bottom: 0;
	 }
	}
	.add-fn {margin-right: 0;}
	.row-check, .form-text {line-height: 1;}
	.autocomplete.list {
		margin-top: 10px;
		font-size: 0;
	li:hover {
		color: #20a0ff;
		background: #eff2f7;
	}
	li:hover .delete {
		display: block;

	}
	li {
		border-top-left-radius: 4px;
		border-top-right-radius: 4px;
		line-height: 18px!important;
		border-bottom: 1px solid #ddd;
		display: inline-block;width: 49%;
	.title {
		font-size: 14px;
		text-overflow: ellipsis;
		overflow: hidden;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
		height: 18px;
	}
	.desc {
		font-size: 12px;
		overflow : hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
		height: 18px;
	}
	.delete {
		position: absolute;right: 3px;top: 17.5px;font-size: 12px;color: #20a0ff;display: none;cursor: pointer;line-height: 16px;
	}
	}
	li:nth-child(2n) {
		margin-left: 2%;
	}
	}
</style>
