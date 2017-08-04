<template>
	<div class="amp-form amp-form03">
		<div class="form-column">
			<el-form :model="formData" :rules="formRules" ref="formData" label-width="124px">
				<el-form-item label="创意名称：" prop="name">
					<el-input v-model="formData.name" placeholder="请输入创意名称" :disabled="actionType === 'modify'"></el-input>
				</el-form-item>
				<el-form-item v-if="isShop || isVideo || isWebpage" label="URL地址：" prop="landingPage">
					<el-row>
						<el-col :span="16">
							<el-input v-model="formData.landingPage" placeholder="请从自建页面选择url" :readonly="true" style="width:100%;display:inline-block;margin-right:20px;" :title="formData.landingPage" :disabled="isUrlModifyDisabled"></el-input>
						</el-col>
						<el-col :span="1">&nbsp;</el-col>
						<el-col :span="7">
							<el-button type="primary" @click="isShowAddUrl = true" :disabled="isUrlModifyDisabled">从自建页面获取</el-button>
						</el-col>
					</el-row>
				</el-form-item>
				<div v-if="isShop" data-type="好店">
					<el-form-item v-if="formData.landingPage.length > 0" label="店铺名称：" prop="title">
						<el-input v-model="formData.title" placeholder="店铺名称" :disabled="true"></el-input>
					</el-form-item>
					<el-form-item v-if="formData.landingPage.length > 0" label="店铺文案：" prop="description">
						<el-input v-model="formData.description" type="textarea" :autosize="{minRows: 3}" placeholder="店铺文案" :disabled="true" resize="none"></el-input>
					</el-form-item>
					<el-form-item label="图片(1~9张)" prop="image" :required="true">
						<div class="row-photo-s" style="width: 100%;" @click="shop_selectImage">
							<input id="shopPicture" type="file" @change="shop_imageChange" accept="image/jpg,image/jpeg,image/png" >
							<div class="imgupload">点击上传</div>
							<div class="el-upload__tip" style="width:100%">只能上传长宽比为1:1的图片文件</div>
						</div>
						<el-upload
							class="upload-demo"
							:action="uploadUrl"
							:on-remove="shop_remove"
							:file-list="shopImageList"
							list-type="picture"
						>
							<!-- <el-button size="small" type="primary">点击上传</el-button> -->
							<!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
						</el-upload>
					</el-form-item>
				</div>
				<div v-if="isVideo" data-type="视频">
					<el-form-item v-if="formData.landingPage.length > 0" label="广告标题：" prop="title">
						<el-input v-model="formData.title" placeholder="广告标题" :disabled="true"></el-input>
					</el-form-item>
					<el-form-item v-if="formData.landingPage.length > 0" label="封面图片：" prop="image" :required="true">
						<div class="content-box">
							<img class="material-image" :src="formData.image.length > 0 ? formData.image[0] : ''">
						</div>
					</el-form-item>
				</div>
				<div v-if="isWebpage" data-type="清单">
					<el-form-item v-if="formData.landingPage.length > 0" label="清单名称：" prop="title">
						<el-input v-model="formData.title" placeholder="清单名称" :disabled="true"></el-input>
					</el-form-item>
					<el-form-item v-if="formData.landingPage.length > 0" label="创意图片：" prop="image">
						<div class="content-box">
							<img class="material-image" :src="formData.image[0]">
						</div>
					</el-form-item>
				</div>
				<div v-if="isBothTopicAndWebpage || isGood || isTopic" data-type="精选、有腔调和好东西合集">
					<!-- -->
					<el-form-item label="链接类型：" v-if="isBothTopicAndWebpage || isGood">
						<el-select v-model="formData.linkType" :disabled="isLinkTypeModifyDisabled">
							<el-option v-for="item in linkType" :label="item.name" :value="item.value" :disabled="item.disabled"></el-option>
						</el-select>
					</el-form-item>
					<!-- 链接类型选了话题时显示此项 -->
					<el-form-item v-if="formData.linkType === 4" label="选择话题：" prop="topicName" :disabled="actionType === 'modify'">
						<el-select v-model="formData.topicName" style="display: inline-block; width:342px; margin: 0 5px 0 0;" :disabled="actionType === 'modify'" remote filterable clearable placeholder="请输入话题名称"
								   :remote-method="topic_remoteMethod" no-match-text="未找到结果"  id="topicNameSelect" popper-class="optionList"
								   ref="select" @change="topic_selectChange" v-enter @enter.native="topic_log">
							<el-option  v-for="item in topicUseData.topicList" :key="item.value" :label="item.label +'-- '+ item.author"
										:value="item.label +'-- '+ item.author"  >
								<span @click="topic_optionChange(item.value,item.label +'-- '+ item.author)"
									  class="optionSPAN"  >
									{{item.label.length > 12 ? item.label.substring(0,12) + '...' : item.label }} -- {{item.author}}
								</span>
							</el-option>
						</el-select>
						<div class="seeTopic"><a :disabled="topicUseData.previewUrl === ''" target="_blank" id="seeTopic"  >点击预览话题</a></div>
					</el-form-item>
					<!-- 链接类型选了url后显示此项 -->
					<!-- <el-form-item v-if="formData.linkType === 2" label="URL地址：" prop="name">
						<el-input v-model="formData.landingPage" placeholder="请输入url地址"></el-input>
					</el-form-item> -->
					<el-form-item v-if="formData.linkType === 2" label="URL地址：" prop="landingPage">
						<el-row>
							<el-col :span="16">
								<el-input v-model="formData.landingPage" placeholder="请从自建页面选择url" :readonly="true" style="width:100%;display:inline-block;margin-right:20px;" :disabled="isUrlModifyDisabled"></el-input>
							</el-col>
							<el-col :span="1">&nbsp;</el-col>
							<el-col :span="7">
								<el-button type="primary" @click="isShowAddUrl = true" :disabled="isUrlModifyDisabled">从自建页面获取</el-button>
							</el-col>
						</el-row>
					</el-form-item>
					<!-- 链接类型选了商品编号后显示此项 -->
					<el-form-item v-if="isGood" label="商品编号：" prop="promotionId" :rules="formRules.skuId">
						<el-input v-model="formData.promotionId" placeholder="请输入商品编号" @blur="good_getProductWithSkuId" :disabled="actionType === 'modify'"></el-input>
					</el-form-item>
					<el-form-item label="广告标题：" prop="title">
						<el-input v-model="formData.title" :placeholder="!isGood ? '广告标题' : '请输入正确的商品编号后修改'" :readonly="formData.linkType === 2" :disabled="isGood && !goodUseData.isSkuIdVisible"></el-input>
					</el-form-item>
					<el-form-item label="创意图片：" prop="image" v-if="isGood">
						<span v-if="!goodUseData.allImgList.length">无</span>
						<div v-else class="uploade-show uploade-select">
							<ul>
								<li @click.prevent.stop="good_selectImg(index)" v-for="(item, index) in goodUseData.allImgList" :class="{'actived': item.checked}"><span class="show-img"><img :src="item.url" alt=""></span></li>
							</ul>
						</div>
					</el-form-item>
					<el-form-item v-if="isBothTopicAndWebpage || isTopic" label="创意图片：" prop="image">
						<div class="row-photo-s" style="width: 100%;" @click="material_selectImage">
							<input id="topPicture" type="file" @change="material_imageChange" accept="image/jpg,image/jpeg,image/png" >
							<div class="imgupload">点击上传</div>
							<div class="el-upload__tip" style="width:100%">只能上传长宽比为2:1的图片文件</div>
						</div>
						<img v-if="formData.image.length > 0" class="material-image" :src="formData.image[0]" style="width: 100%;">
					</el-form-item>
					<el-form-item v-if="isGood || isTopic || isBothTopicAndWebpage" label="广告文案：" prop="description">
						<el-input v-model="formData.description" type="textarea" :autosize="{minRows: 4}" placeholder="广告文案" :disabled="!goodUseData.isSkuIdVisible && isGood"></el-input>
					</el-form-item>
				</div>
				<el-form-item v-if="!isGood" label="跟单商品：" prop="isSelectProducts">
					<el-checkbox v-model="isSelectProducts" :label="true" :disabled="isProModifyDisabled">
						添加跟单商品
						<el-tooltip placement="right" effect="light">
							<div slot="content">若您在跳转后的链接内也输入了跟单商品，<br/>则在统计效果时，此页面和在跳转后的链接<br/>内输入的商品SKU都会被统计到。</div>
							<span class="SKUtipicon">?</span>
						</el-tooltip>
					</el-checkbox>
				</el-form-item>
				<el-form-item v-if="isSelectProducts || (isGood && isRebate)" :label="isGood ? '联合推广商品：' : ''">
					<el-col :span="8" v-for="(product, index) in formData.products" style="padding: 0 5px 20px;">
						<el-form-item
							:key="index"
							:prop="'products.' + index + '.skuId'"
							:rules="formRules.formDataProductSkuRules"
						>
							<el-input v-model="product.skuId" placeholder="请输入商品SKU" :disabled="isProModifyDisabled"></el-input>
						</el-form-item>
					</el-col>
					<div class="detail-row detail-row-mr add-fn" v-if="selectProductCount < 9"><span class="btn btn-normal" @click="normal_addProducts"></span></div>
					<template v-if="isGood && isRebate">
						<div class="detail-row detail-row-mr">
							<el-checkbox v-model="goodUseData.isAutoAddProduct.category">允许自动填充本店铺同类目商品</el-checkbox>
						</div>
						<div class="detail-row detail-row-mr">
							<el-checkbox v-model="goodUseData.isAutoAddProduct.shop">允许自动填充本店铺商品</el-checkbox>
						</div>
					</template>
				</el-form-item>
			</el-form>
		</div>
		<transition name="drawer">
			<m-add-url v-if="isShowAddUrl" :templateId="formData.templateId"></m-add-url>
		</transition>
	</div>
</template>
<script>
	import Vue from "vue";
	import apiConfig from '../../../../config/api.config.js';
	import {copyObj, isEmptyObj, getImgSize, mixin,limitLen} from 'utils/common';
	import Http from 'http';
	import Event from 'event';
	import store from 'store';
	import DrawerAddUrl from './drawer-add-url.vue';

	Vue.directive('enter', {
		bind: function(el) {
			const Input = el.getElementsByTagName('input')[0];
			Input.addEventListener('keyup',function(e){
				if(e.keyCode == 40 || e.keyCode == 38) {
					el.dispatchEvent(new Event('enter'));
				}
			});
		}
	});
	export default {
		name: 'app-put-idea-new',
		data() {
			return {
				formData: {},
				products: [],
				isShowAddUrl: false,
				isSelectProducts: false,
				httpMethodMap: {
					new: 'post',
					copy: 'post',
					modify: 'put'
				},
				webpageId: '',
				goodUseData: {
					shopId: '',
					categoryId: '',
					allImgList: '',
					isSkuIdVisible: false,
					isAutoAddProduct: {
						category: true,
						shop: true
					},
					visibleProducts: []
				},
				productError: {
					isError: false,
					errorMsg: ''
				},
				topicUseData: {
					topicList: [],
					previewUrl: '',
					isLoading: false,
					canRemote: true,
					skuIds: [],
					topicCount: 0,
					page: 1,
					selectQuery: ''
				},
				isUseDefaultImage: 0,
				isUploadImage: false,
				isShowTooltip: false,
				uploadUrl: `${$CONFIG['domain']}api/image/upload`,
				shopImageList: [],
				advertId: 1,
				adverts: [],
				selectProductCount: 0,
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
					disabled: false
				}, {
					value: 2,
					name: 'URL',
					disabled: false
				}],
				formRules: {
					name: [{
						required: true,
						message: '请输入创意名称',
						trigger: 'blur'
					}, {validator: function(rule, value, callback) {
						if(limitLen(value, 30)){
							callback();
						}else{
							callback(new Error('最大长度不超过15个汉字'));
						}
					}, trigger: 'blur' }],
					title: [{
						required: true,
						message: '请输入广告标题',
						trigger: 'blur'
					}],
					image: [{
						validator: (rule, value, callback) => {
							if (this.isVideo) {
								if (this.isUseDefaultImage !== 0 && this.formData.image.length === 0) {
									callback(new Error('请上传创意图片'));
								} else {
									callback();
								}
							} else if (this.formData.image.length === 0) {
								callback(new Error('请上传创意图片'));
							} else {
								callback();
							}
						},
						trigger: 'blur'
					}],
					skuId: [{
						required: true,
						message: '请输入商品编号',
						trigger: 'blur'
					}],
					landingPage: [{
						required: true,
						message: '请从自建页选取',
						trigger: 'blur'
					}],
					topicName: [{
						required: true,
						message: '请选择话题',
						trigger: 'blur'
					}],
					formDataProductSkuRules: [{
						validator: (rule, value, callback) => {
							let index = Number(rule.field.split('.')[1]);
							if (this.productError.isError) {
								callback(new Error(this.productError.errorMsg));
							} else {
								let res = this.normal_checkSkuIdVisible(value, index);
								if (res.isError) {
									callback(new Error(res.errorMsg));
								} else {
									callback();
								}
							}
						},
						trigger: 'blur'
					}],
					'description':[{validator: (rule, value, callback) => {
						if(!limitLen(value, 100) && (this.isTopic || (this.isBothTopicAndWebpage && this.formData.linkType === 4))){
							callback(new Error('最大长度不超过50个汉字'));
						} else {
							callback();
						}
					}, trigger: 'blur' }]
				}
			};
		},
		components: {
			'm-add-url': DrawerAddUrl
		},
		computed: {
			drawerData: () => store.state.drawerCtrl.config, // from store
			isRebate: () => store.state.drawerCtrl.isRebate, // 是否返利，从 state 中来 如果被复写掉了为 false 达到知晓上一级是否返利的目的
			actionType: () => store.state.drawerCtrl.action,
			flightLinkType() {
				return this.formData.flightLinkType;
			},
			//类型区分
			isShop() {
				return this.flightLinkType.length === 1 && this.flightLinkType[0] === 0
			},
			isVideo() {
				return this.flightLinkType.length === 1 && this.flightLinkType[0] === 9
			},
			isWebpage() {
				return this.flightLinkType.length === 1 && this.flightLinkType[0] === 2
			},
			isTopic() {
				return this.flightLinkType.length === 1 && this.flightLinkType[0] === 4
			},
			isGood() {
				return this.flightLinkType.length === 1 && this.flightLinkType[0] === 1
			},
			isBothTopicAndWebpage() {
				let isContainTopic = this.flightLinkType.find((type) => {
					return type === 4;
				});
				let isContainWebpage = this.flightLinkType.find((type) => {
					return type === 2;
				});
				return !!isContainTopic && !!isContainWebpage;
			},
			good_visibleProducts() {
				let products = [];
				this.formData.products.forEach((product) => {
					if (!!product.skuId && (product.hasOwnProperty('shpoId') || product.hasOwnProperty('productId'))) {
						products.push(product);
					}
				});
				return products;
			},
			isProModifyDisabled() {
				return this.actionType === 'modify' && !this.isGood;
			},
			isUrlModifyDisabled() {
				return this.actionType === 'modify';
			},
			isLinkTypeModifyDisabled() {
				return this.actionType === 'modify' && this.formData.approveStatus === 1;
			}
		},
		created() {
			this.formData = copyObj(this.drawerData);
			if (this.actionType === 'modify') {
				// this.formData.flightLinkType = [this.formData.linkType];
				if (this.isShop) {
					this.formData.image.forEach((item) => {
						this.shopImageList.push({name: item, url: item});
					});
				}
				if (this.isGood) {
					// Vue.set(this.formData, "prodcuts", this.formData.relatedItems);
					let remainder = this.formData.products.length % 3;
					let integer = Math.floor(this.formData.products.length / 3);
					this.selectProductCount = integer * 3;
					if (remainder !== 0) {
						this.selectProductCount += 3;
						let subNumber = 3 - (this.formData.products.length % 3);
						for(let i = 0; i < subNumber; i++) {
							this.formData.products.push({skuId: ''});
						}
					}
				}
				this.formData.products.forEach((product) => {
					if (!product.skuId) {
						Vue.set(product, 'skuId', product.itemId);
					}
				});
			}
			if (this.isGood) {
				if (!this.isRebate) {
					this.goodUseData.isAutoAddProduct.shop = false;
					this.goodUseData.isAutoAddProduct.category = false;
				} else {
					this.isSelectProducts = true;
				}
			}
			if (this.isBothTopicAndWebpage) {
				this.formData.linkType = this.actionType !== 'modify' ? 4 : this.formData.linkType;
				this.linkType.forEach((type) => {
					type.disabled = type.value !== 4 && type.value !== 2;
				});
			} else {
				this.formData.linkType = this.flightLinkType[0];
				this.linkType.forEach((type) => {
					type.disabled = type.value !== this.flightLinkType[0];
				});
			}
			// this.normal_getAdverts();
		},
		mounted() {
			if(this.formData.linkType === 4 && this.actionType === 'modify'){
				this.topicUseData.skuIds = [];
				this.formData.products = [];
				this.formData.relatedItems.forEach((item) => {
					if (item.type === 0) {
						this.formData.products.push(item);
					} else if (item.type === 1) {
						this.topicUseData.skuIds.push(item);
					}
				});
				Http.get("/api/topic", {
					params: {
						hideLoading: true,
						topicId: this.formData.promotionId
					}
				}).then(res=> {
					if (res.data.code === 200) {
						this.formData.topicName = res.data.data.name + '--' + res.data.data.user.nickname;
					}else{
						this.$message({
							message: "此话题已删除或审核未通过!",
							type: 'error'
						});
					}
				})
					.catch(function(error) {
						console.log(error);
					});
			};
			if (this.actionType === 'modify' && this.isGood && this.formData.promotionId !== '') {
				Http.get('api/material/sku', {
					params: {
						skuId: this.formData.promotionId
					}
				}).then((res) => {
					if (res.data.code === 200 && res.data.iserror === 0) {
						this.goodUseData.shopId = res.data.data.shopId;
						this.goodUseData.categoryId = res.data.data.categoryId;
						this.formData.productId = res.data.data.productId;
						this.goodUseData.allImgList = [];
						res.data.data.images.forEach((item) => {
							this.goodUseData.allImgList.push({
								url: item,
								checked: item === this.formData.image[0]
							});
						});
					}
				});
			}
			// 隐藏添加广告位
			Event.$off('drawer-hide-add-url');
			Event.$on('drawer-hide-add-url', () => {
				this.isShowAddUrl = false;
			});
			// 保存广告位
			Event.$off('drawer-save-add-url');
			Event.$on('drawer-save-add-url', (result) => {
				// this.formData.advertisements = result;
				if (result) {
					this.formData.landingPage = result.landingPage;
					this.formData.image = !!result.image && !this.isShop ? [result.image] : [];
					if (this.isShop) {
						this.formData.promotionId = result.shopId;
					} else if (this.isVideo) {
						this.formData.promotionId = result.videoId;
						this.isUseDefaultImage = result.useDefaultImage;
					}
					this.webpageId = result.pageId;
					this.formData.title = result.title;
					this.formData.description = result.description;
					this.$refs.formData.validateField('landingPage');
				}
				this.isShowAddUrl = false;
			});
			//保存创意
			Event.$off('put-save');
			Event.$on('put-save', () => {
				this.$refs.formData.validate((result) => {
					if (result) {
						if (this.isGood && this.good_visibleProducts.length < this.selectProductCount && (this.goodUseData.isAutoAddProduct.category || this.goodUseData.isAutoAddProduct.shop)) {
							Http.get('api/material/items', {
								params: this.goodUseData.isAutoAddProduct.category ? {
									shopId: this.goodUseData.shopId,
									categoryId: this.goodUseData.categoryId,
									skuId: this.formData.promotionId
								} : {
									shopId: this.goodUseData.shopId,
									skuId: this.goodUseData.formData.promotionId
								}
							}).then((res) => {
								if(res.status === 200 && res.data.code === 200 && res.data.iserror === 0){
									let lackLen = this.selectProductCount - this.good_visibleProducts.length;
									let list = res.data.data.list.slice(0,lackLen);
									this.formData.relatedItems = [...this.good_visibleProducts,...list];
									this.normal_saveMaterial();
								}
							});
						} else {
							this.formData.relatedItems = this.good_visibleProducts;
							this.normal_saveMaterial();
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
			normal_getAdverts() {
				Http.get('api/adverts', {
					params: {
						advertId: this.advertId
					}
				}).then((res) => {
					if (res.data.code === 200 && res.data.iserror === 0) {
						let data = res.data.data;
						let adverts= data.adverts;
						adverts.forEach((item) => {
							item.value = '';
							if (item.type === 0) {
								item.advertText = '图片：';
							} else if (item.type === 1) {
								item.advertText = '文案：';
							} else if (item.type === 2) {
								item.advertText = '视频：';
							}
						});
						this.formData.adverts = adverts;
					}
				})
			},
			normal_addProducts() {
				this.selectProductCount = this.selectProductCount === 9 ? this.selectProductCount : this.selectProductCount + 3;
				for(let i = 0; i < 3; i++) {
					this.formData.products.push({skuId: ''});
				}
			},
			normal_checkSkuIdVisible(skuId, index) {
				if (skuId === '') {
					this.formData.products[index] = {skuId: ''};
					return {isError: false, errorMsg: ''};
				}
				let isError = false;
				let product = this.formData.products.find((product, i) => {
					return i !== index && product.skuId === skuId;
				});
				let errorMsg = '';
				if (this.isGood && skuId === this.formData.promotionId) {
					return {isError: true, errorMsg: '不能与创意主商品相同'};
				} else if (!!product) {
					return {isError: true, errorMsg: '请填写不同的商品编号'};
				} else {
					Http.get('api/material/sku', {
						hideLoading: true,
						params: { skuId: skuId }
					}).then((res) => {
						if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
							if (isEmptyObj(res.data.data)) {
								isError = true;
								errorMsg = '请填写有效的商品编号';
								this.productError.isError = isError;
								this.productError.errorMsg = errorMsg;
								this.$refs.formData.validateField('products.' + index + '.skuId');
							} else {
								this.formData.products[index] = Object.assign({}, res.data.data, this.formData.products[index], {type: 0});
							}
						} else {
							this.productError.isError = true;
							this.productError.errorMsg = res.data.msg;
							this.$refs.formData.validateField('products.' + index + '.skuId');
						}
					});
					return {isError: false, errorMsg: ''};
				}
			},
			normal_saveMaterial() {
				if (this.isUploadImage) {
					this.$message({
						message: '有图片正在上传',
						type: 'warning'
					});
					return false;
				}
				let size = getImgSize(this.formData.image[0]);
				let params = {
					name: this.formData.name,
					promotionId: this.formData.promotionId,
					productId : this.formData.productId,
					type: this.formData.type,
					linkType: this.formData.linkType,
					landingPage: this.formData.landingPage,
					title: this.formData.title,
					image: this.formData.image,
					description: this.formData.description,
					relatedItems: this.formData.linkType === 4 ? [...this.topicUseData.skuIds, ...this.formData.relatedItems] : this.formData.relatedItems,
					width: size.width,
					height: size.height,
					productLine: 2,
					webpageId: this.webpageId !== '' ? this.webpageId : this.formData.webpageId
				};
				Http({
					url: 'api/material',
					method: this.httpMethodMap[this.actionType],
					data: this.actionType === 'modify' ? mixin({
						// 修改 创意ID
						materialId: this.formData.materialId
					}, params) : mixin({
						// 新建复制 单元ID
						flightId: this.formData.flightId
					}, params)
				}).then((res) => {
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
				}).catch((e) => {
					Event.$emit('put-save-result', {
						error: 1,
						res: null
					});
				});
			},
			shop_selectImage() {
				let input = document.getElementById('shopPicture');
				input.click();
			},
			shop_imageChange(eve) {
				let files= eve.target.files;
				this.shop_loadFile(files[0]);
			},
			shop_loadFile(file) {
				if (this.formData.image.length === 9) {
					this.$message({
						message: '最多只能上传9张图片',
						type: 'warning'
					});
					return false;
				}
				if (file.size > 500 *1024) {
					this.$message({
						message: '您上传的图片太大，请重新上传！',
						type: 'warning'
					});
					return false;
				}
				let that = this;
				let imageData = new FormData();
				let reader = new FileReader();
				reader.onload = function(e) {
					let data = e.target.result;
					let image = new Image();
					image.onload=function(){
						let width = image.width;
						let height = image.height;
						let scale = width / height;
						if(scale !== 1) {
							that.$message({
								message: '请上传长宽比为1:1的图片!',
								type: 'warning'
							});
						} else {
							imageData.append('file', file);
							that.shop_imageUpload(imageData);
						}
					};
					image.src= data;
				};
				reader.readAsDataURL(file);
			},
			shop_imageUpload(data) {
				Http.post(this.uploadUrl, data)
					.then((res) => {
						if (res.data.code === 200) {
							let imageUrl = res.data.data.imageUrl;
							this.formData.image.push(imageUrl);
							this.shopImageList.push({name: imageUrl, url: imageUrl});
							this.$refs.formData.validateField('image');
							// this.formData.image = [res.data.data.imageUrl];
						}
					}).catch((error) => {
					that.$message({
						message: '上传失败',
						type: 'warning'
					});
				});
			},
			material_selectImage() {
				let input = document.getElementById('topPicture');
				input.click();
			},
			material_imageChange(eve) {
				let files= eve.target.files;
				this.material_loadFile(files[0]);
			},
			material_loadFile(file) {
				if (file.size > 500 *1024) {
					this.$message({
						message: '您上传的图片太大，请重新上传！',
						type: 'warning'
					});
					return false;
				}
				let that = this;
				let imageData = new FormData();
				let reader = new FileReader();
				reader.onload = function(e) {
					let data = e.target.result;
					let image = new Image();
					image.onload=function(){
						let width = image.width;
						let height = image.height;
						let scale = width / height;
						if(scale !== 2) {
							that.$message({
								message: '请上传长宽比为2:1的图片!',
								type: 'warning'
							});
						} else {
							imageData.append('file', file);
							that.material_imageUpload(imageData);
						}
					};
					image.src= data;
				};
				reader.readAsDataURL(file);
			},
			material_imageUpload(data) {
				Http.post(this.uploadUrl, data)
					.then((res) => {
						if (res.data.code === 200) {
							this.formData.image = [res.data.data.imageUrl];
						}
					}).catch((error) => {
					that.$message({
						message: '上传失败',
						type: 'warning'
					});
				});
			},
			shop_uploadSuccess(response) {
				this.isUploadImage = false;
				if (response.code === 200 && response.iserror === 0) {
					this.formData.image.push(response.data.imageUrl);
					this.$refs.formData.validateField('image');
				}
			},
			shop_uploadProgress() {
				this.isUploadImage = true;
			},
			shop_beforeUpload(file) {
				if (this.formData.image.length === 9) {
					this.$message({
						message: '最多只能上传9张图片',
						type: 'warning'
					});
					return false;
				}
			},
			shop_remove(file, fileList) {
				console.log(fileList);
				this.formData.image = [];
				fileList.forEach((file) => {
					this.formData.image.push(file.url);
				});
			},
			topic_log(){
				this.formData.topicName = "";
				this.topicUseData.topicList = [];
			},
			//话题搜索
			topic_remoteMethod(query) {
				if (!this.topicUseData.canRemote || this.topicUseData.isLoading) return;
				this.topicUseData.isLoading = true;
				console.log(query);
				let wrap = document.getElementsByClassName('optionList')[0].getElementsByClassName('el-select-dropdown__list')[0];
				if (query !== '') {
					if (this.selectQuery != query) {
						this.topicUseData.page = 1;
						wrap.scrollTop = 0;
						this.topicUseData.topicList = [];
					};
					this.topicUseData.selectQuery = query;
					Http.get("/api/topics", {
						params: {
							keyword: query,
							pageNum: this.topicUseData.page,
							pageSize: 8
						}
					}).then((res)=> {
						let list = [];
						if (res.data.code == 200) {
							this.topicUseData.isLoading = false;
							this.topicUseData.topicCount = Math.ceil(res.data.data.totalCount/8) ;
							list = res.data.data.topics.map(item => {
								return {value: item.topic.id, label: item.topic.name, author:item.user.nickname};
							});
							this.topicUseData.topicList = this.mergeJson(this.topicUseData.topicList, list);
							if (this.topicUseData.page === 1 ) {
								wrap.addEventListener('scroll', () => {
									if (wrap.scrollTop == (wrap.scrollHeight - wrap.offsetHeight) && this.topicUseData.page < this.topicUseData.topicCount ) {
										if (!this.topicUseData.isLoading) {
											this.topicUseData.page++;
											this.remoteMethod(this.topicUseData.selectQuery);
										}
									};

								});
							}
						}
					}).catch(function(error) {
						console.log(error);
					});
				} else {
					this.topicUseData.previewUrl='';
					document.getElementById('seeTopic').removeAttribute('href');
					this.topicUseData.page = 1;
					this.topicUseData.topicList=[];
					this.topicUseData.isLoading = false;
				}
			},
			//合并2个json对象
			mergeJson(str1, str2) {
				var str3 = [];

				for (var i = 0; i < str1.length; i++) {
					str3.push(str1[i]);
				}

				for (var i = 0; i < str2.length; i++) {
					str3.push(str2[i]);
				}
				return str3;
			},
			//话题名称改变 获取话题详情
			topic_selectChange(value, oldValue) {
				this.topicUseData.canRemote = false;
				if(this.formData.topicName == ""){
					this.topicUseData.canRemote = true;
				}
			},
			topic_optionChange(topicId, topicName) {
				this.topicUseData.page = 1;
				this.topicUseData.topicList = [];
				this.formData.promotionId = topicId;
				this.formData.title = topicName;
				if(topicId != ''){
					Http.get("/api/topic", {
						params: {
							topicId: topicId
						}
					}).then(res=> {
						if (res.data.code === 200) {
							this.topicUseData.previewUrl = res.data.data.previewUrl;
							document.getElementById('seeTopic').setAttribute('href', this.topicUseData.previewUrl);
							this.topicUseData.skuIds = [];
							res.data.data.skuIds.forEach((item) => {
								this.topicUseData.skuIds.push({skuId: item, type: 1});
							});
							this.formData.title = res.data.data.name;
							this.topicUseData.canRemote = true;
						}
					}).catch(function(error) {
						this.topicUseData.canRemote = true;
					});
				}else{
					this.topicUseData.previewUrl = '';
				}
			},
			good_getProductWithSkuId(event) {
				if (this.formData.skuId === '') {
					return;
				}
				Http.get('api/material/sku', {
					params: {
						skuId: this.formData.promotionId
					}
				}).then((res) => {
					if (res.data.iserror) {
						this.goodUseData.isSkuIdVisible = false;
						this.formData.title = '';
						this.formData.description = '';
						this.goodUseData.shopId = '';
						this.goodUseData.categoryId = '';
						this.goodUseData.allImgList = [];
						this.formData.image = [];
						this.itemIdError = res.data.msg;
						this.$refs.formData.validateField('promotionId');
					} else {
						this.formData.title = res.data.data.name;
						this.formData.description = res.data.data.description;
						this.formData.productId = res.data.data.productId;
						this.goodUseData.shopId = res.data.data.shopId;
						this.goodUseData.categoryId = res.data.data.categoryId;
						this.goodUseData.allImgList = [];
						res.data.data.images.forEach((item) => {
							this.goodUseData.allImgList.push({
								url: item,
								checked: false
							});
						});
						this.goodUseData.allImgList[0].checked = true;
						this.formData.image = [this.goodUseData.allImgList[0].url];
						this.goodUseData.isSkuIdVisible = true;
						this.itemIdError = '';
						this.$refs.formData.validateField('promotionId');
					}
				});
			},
			good_selectImg(index) {
				this.formData.image =  [];
				this.formData.image.push(this.goodUseData.allImgList[index].url);
				this.goodUseData.allImgList.forEach((item) => {
					item.checked = false;
				});
				this.goodUseData.allImgList[index].checked = true;
				// this.$refs.formData.validateField('image');
			}
		},
		watch: {
			'isSelectProducts': {
				handler: function(val, oldVal) {
					if (this.actionType !== 'modify') {
						if (val) {
							this.normal_addProducts();
						} else {
							this.selectProductCount = 0;
							this.formData.products = [];
						}
					}
				},
				deep: true

			}
		}
	};
</script>
<style lang="less" scoped>
	.content-box{
		position: relative;
		width: 100%;
		height: 250px;
		margin: 0 auto;
		text-align: center;
		background: #eee;
	.material-image{
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		width: auto;
		height: auto;
		max-width: 100%;
		max-height: 100%;
		margin: auto;
		vertical-align: middle;
	}
	}
	.add-fn{
		clear: both;
	}
	.el-upload{
		width: 100%;
	}
	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 100%;
		height: 100%;
		line-height: 250px;
		text-align: center;
	}
	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
	.seeTopic{
		float: right;
		height: 36px;
		line-height: 36px;
	}
	a[disabled]{
		color: #bfcbd9;
		cursor: not-allowed;
	}
	.topicName{
		width: 355px;
		display: inline-block;

	}
	.topicName input[type='text']{
		width: 355px;
		display: inline-block;
	}
	.imgupload{
		background-image: none;
		text-align: center;
		font-size: 12px;
		border-radius: 4px;
		color: #fff;
		background-color: #20a0ff;
		border-color: #20a0ff;
		width:68px;
		height:28px;
		line-height: 28px;
		cursor: pointer;
	}
	#topPicture, #shopPicture{
		width: 146px;
		height: 100px;
		display:block;
		position:absolute;
		visibility: hidden;
	}
</style>
