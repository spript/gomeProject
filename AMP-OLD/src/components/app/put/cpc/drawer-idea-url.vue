<template>
	<div class="amp-form amp-form03">
		<div class="form-column">
			<el-form :model="formData" :rules="rules" ref="formData" label-width="124px">
				<el-form-item label="创意名称：" prop="name">
					<el-input v-model="formData.name" placeholder="请输入创意名称"></el-input>
				</el-form-item>
				<el-form-item label="链接类型：">
					<el-select v-model="formData.linkType" @change="linkTypeChange" :disabled="!itemCan">
						<el-option v-for="item in linkType" :label="item.name" :value="item.value" :disabled="item.disabled"></el-option>
					</el-select>
				</el-form-item>
				<!--URL-->
				<div  v-if="formData.linkType == 2">
					<el-form-item label="URL地址：" prop="landingPage">
						<el-input v-model="formData.landingPage" placeholder="请输入URL地址" :disabled="actionType === 'modify'"></el-input>
					</el-form-item>
					<el-form-item label="广告标题：" prop="title">
						<el-input  v-model="formData.title" placeholder="请输入广告标题"></el-input>
					</el-form-item>
					<el-form-item label="创意图片：" prop="image">

						<div class="detail-row"  style="width: 100%;margin: 0 20px 0 0;float: left;">
							<div class="row-photo-s" @click="changeFile"  style="width: 100%;">
								<input id="topPicture" type="file" style="width: 146px;height: 100px;display:block;position:absolute;visibility: hidden;"
									   @change="topPictureChange" accept="image/jpg,image/jpeg,image/png"  >
								<div class="imgupload">点击上传</div>
								<div class="el-upload__tip" style="width:100%">只能上传长宽比为2:1的图片文件</div>
							</div>
						</div>

						<!-- <el-upload :action="uploadUrl" accept="image/*" :multiple="false" :on-preview="handlePreview" :on-remove="handleRemove" :on-success="handleSuccess" :show-upload-list="false" :before-upload="handleBeforeUpload"> -->
					<!--	<el-upload :action="uploadUrl" accept="image/jpg,image/jpeg,image/png" :before-upload="imageSize" :multiple="false" :on-success="handleUploadImageSuccess" :show-file-list="false">
							<el-button size="small" type="primary">点击上传</el-button>
							<div class="el-upload__tip" slot="tip">只能上传长宽比为2:1的图片文件</div>
						</el-upload>
                      -->
						<div class="row-photo-s">
							<div class="photo-add photo-add-b"><img v-model="formData.image" :src="formData.image" style="width:400px; height:247px;">
							</div>
						</div>
					</el-form-item>
					<el-form-item label="广告文案：" prop="description">
						<el-input  v-model="formData.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
					</el-form-item>
				 </div>
				<!--话题-->
				<div  v-if="formData.linkType == 4">
					<el-form-item label="选择话题：" prop="topicName">
						<el-select v-model="formData.topicName" style="display: inline-block; width:342px; margin: 0 5px 0 0;"
								   remote filterable clearable placeholder="请输入话题名称"  :remote-method="remoteMethod"
								   no-match-text="未找到结果"  id="topicNameSelect" popper-class="optionList"  ref="select"
								   :disabled="!itemCan" @change="selectChange" v-enter @enter.native="log">
							<el-option  v-for="item in topicList" :key="item.value" :label="item.label +'-- '+ item.author"
									   :value="item.label +'-- '+ item.author"  >
								<span @click="optionChange(item.value,item.label +'-- '+ item.author)"
									  class="optionSPAN"  >
									{{item.label.length>12?item.label.substring(0,12)+'...' : item.label }} -- {{item.author}}
								</span>
							</el-option>
						</el-select>
						<div class="seeTopic"><a :disabled="previewUrl === ''" target="_blank" id="seeTopic"  >点击预览话题</a></div>
					</el-form-item>
					<el-form-item label="广告标题：" prop="topicTitle">
						<el-input  v-model="formData.topicTitle" placeholder="请输入广告标题"></el-input>
					</el-form-item>
					<el-form-item label="创意图片：" prop="image">
						<!-- <el-upload :action="uploadUrl" accept="image/*" :multiple="false" :on-preview="handlePreview" :on-remove="handleRemove" :on-success="handleSuccess" :show-upload-list="false" :before-upload="handleBeforeUpload"> -->
						<div class="detail-row"  style="width: 100%;margin: 0 20px 0 0;float: left;">
							<div class="row-photo-s" @click="changeFile"  style="width: 100%;">
								<input id="topPicture" type="file" style="width: 146px;height: 100px;display:block;position:absolute;visibility: hidden;"
									   @change="topPictureChange" accept="image/jpg,image/jpeg,image/png" >
								<div class="imgupload">点击上传</div>
								<div class="el-upload__tip" style="width:100%">只能上传长宽比为2:1的图片文件</div>
							</div>
						</div>
						<div class="row-photo-s">
							<div class="photo-add photo-add-b"><img v-model="formData.image" :src="formData.image" style="width:400px; height:247px;">
							</div>
						</div>
					</el-form-item>
					<el-form-item label="广告文案：" prop="topicDescription">
						<el-input  v-model="formData.topicDescription" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
					</el-form-item>

					<el-form-item >
						<div class="SKUtip" v-show="SKUtip">若您在跳转后的链接内也输入了跟单商品，则在统计效果时，此页面和在跳转后的链接内输入的商品SKU都会被统计到。</div>
						<el-checkbox v-model="checked" @change="checkboxChange" class="addProducts" :disabled="!itemCan" style="position: absolute">添加跟单商品
							<span @mouseenter="SKUtip = !SKUtip" @mouseleave="SKUtip = !SKUtip" class="SKUtipicon">?</span> :</el-checkbox>
						<div v-show="checked">
							<el-row>
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[0].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(0)" @change="handleUnitProudctChange(0)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[0].isError">{{ unitProductIds[0].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[1].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(1)" @change="handleUnitProudctChange(1)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[1].isError">{{ unitProductIds[1].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[2].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(2)" @change="handleUnitProudctChange(2)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[2].isError">{{ unitProductIds[2].errorMsg }}</div>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row v-show="unitProductCount > 3">
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[3].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(3)" @change="handleUnitProudctChange(3)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[3].isError">{{ unitProductIds[3].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[4].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(4)" @change="handleUnitProudctChange(4)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[4].isError">{{ unitProductIds[4].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[5].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(5)" @change="handleUnitProudctChange(5)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[5].isError">{{ unitProductIds[5].errorMsg }}</div>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row v-show="unitProductCount > 6">
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[6].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(6)" @change="handleUnitProudctChange(6)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[6].isError">{{ unitProductIds[6].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[7].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(7)" @change="handleUnitProudctChange(7)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[7].isError">{{ unitProductIds[7].errorMsg }}</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[8].itemId" placeholder="请输入商品SKU" @blur="handleUnitProductBlur(8)" @change="handleUnitProudctChange(8)" :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[8].isError">{{ unitProductIds[8].errorMsg }}</div>
									</el-form-item>
								</el-col>
							</el-row>
							<div class="add-fn"  v-show="unitProductCount !== 9" @click="addUnitProductCount"><span class="btn btn-normal"></span></div>
						</div>

					</el-form-item>
				</div>
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
import Events from 'event';
import {on} from 'utils/dom';
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
	name: 'app-put-new-idea',
	data() {
		return {
			formData: {},
			productId: '',
			nProductId: '',
			itemIdError: '',
			rules: {
				topicName:[{
					required: true,
					message: '请输入话题名称',
					trigger: 'change'
				}],
				topicTitle:[{
					required: true,
					message: '请输入话题名称',
					trigger: 'blur'
				},{
					max: 40,
					message: '最大长度不超过40个汉字',
					trigger: 'blur'
				}],
				name: [{
					required: true,
					message: '请输入创意名称',
					trigger: 'blur'
				}, {
					max: 30,
					message: '最大长度不超过30个汉字',
					trigger: 'blur'
				}],

				landingPage: [{
					required: true,
					message: '请输入url地址',
					trigger: 'blur'
				}, {
					validator: (rule, value, callback) => {
						if (!/^(http|https){1}:\/\/[a-zA-Z0-9\.\-]+\.(gomeplus.com|gome.com.cn)\//g.test(value)) {
							callback(new Error('您输入的url不合法！'));
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
					max: 30,
					message: '最大长度不超过30个字符',
					trigger: 'blur'
				}],
				image: [{
					required: true,
					message: '请选择创意图片',
					trigger: 'blur'
				}, {
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
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				topicDescription:[{
					required: true,
					message: '请输入广告文案',
					trigger: 'blur'
				}, {
					max: 100,
					message: '最大长度不超过100个字符',
					trigger: 'blur'
				}],
			},
			uploadUrl: `${$CONFIG['domain']}api/image/upload`,
			linkType: [{
				value: 1,
				name: '商品编号',
				disabled: true
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
				name: '话题',
				disabled: false
			}, {
				value: 2,
				name: 'URL',
				disabled: false
			}],
			httpMethodMap: {
				new: 'post',
				copy: 'post',
				modify: 'put'
			},
			unitProductCount: 3,
			unitProductIds: [
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''},
				{itemId: '', isError: false, errorMsg: ''}
			],
			topicList: [],
			checked: false,
			SKUtip: false,
			num: 1,
			selectQuery: '',
			skuIds: [],
			itemCan: true,
			previewUrl: '',
			count:0,
			isLoading: false,
			canRemote: true

		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config, // from store
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		isRebate: () => store.state.drawerCtrl.isRebate, // 是否返利，从 state 中来 如果被复写掉了为 false 达到知晓上一级是否返利的目的
		actionType: () => store.state.drawerCtrl.action,
	},
	created() {
		if(this.actionType=="modify"){
			this.itemCan = false;
		};
		this.formData = copyObj(this.drawerData);
		this.formData.linkType = this.formData.linkType==1 ? 2:this.formData.linkType;
		if(this.formData.linkType == 4){
			this.formData.topicTitle = this.formData.title;
			this.formData.topicDescription = this.formData.description;
			this.checked = this.formData.relatedItems.handSku.length > 0;
			this.unitProductCount = this.formData.relatedItems.handSku.length;
			this.previewUrl = this.formData.previewUrl;
			this.itemCan = false;
			if (this.formData.relatedItems.handSku.length > 0) {
				this.formData.relatedItems.handSku.map((item, index) => {
					this.unitProductIds[index].itemId = item.skuId;
				});
			};

			this.formData.title = "";
			this.formData.description = "";
		}
	},
	mounted() {
		Events.$off('put-save');
		Events.$on('put-save', () => {
			this.$refs.formData.validate((result) => {
				if (result) {
						this.saveMaterial();
				} else {
					Events.$emit('put-save-result', {
						error: 1,
						res: null
					});
				}
			});
		});
		if(this.formData.linkType == 4){
			document.getElementById('seeTopic').setAttribute('href', this.previewUrl);
		};
		if(this.formData.topicId){
			Http.get("/api/topic", {
				params: {
					hideLoading: true,
					topicId: this.formData.topicId
				}
			})
				.then(res=> {
					if (res.data.code === 200) {
						this.formData.topicName = res.data.data.name+'--'+res.data.data.user.nickname;
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
	},
	methods: {
		log(){
			this.formData.topicName = "";
			this.topicList = [];
		},
		linkTypeChange(){
			this.formData.title='';
			this.formData.topicName='';
			this.formData.topicId='';
			this.formData.topicDescription='';
			this.formData.topicTitle='';
			this.formData.description='';
			this.formData.landingPage='';
			this.formData.image='';
			this.previewUrl='';
			this.topicList='';
			this.num = 1;
		},
        //SKU显示和隐藏，reset问题
		checkboxChange(){
		    this.unitProductCount=3;
		    this.unitProductIds=[
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' },
				{ itemId: '', isError: false, errorMsg: '' }
			];
		},
        //话题搜索
		remoteMethod(query){
		    if (!this.canRemote || this.isLoading) return;
		    this.isLoading = true;
			let wrap = document.getElementsByClassName('optionList')[0].getElementsByClassName('el-select-dropdown__list')[0];
			if (query !== '') {
				if (this.selectQuery != query) {
					this.num = 1;
					wrap.scrollTop = 0;
					this.topicList = [];
				};
				this.selectQuery = query;
					Http.get("/api/topics", {
						params: {
							keyword: query,
							pageNum: this.num,
							pageSize: 8
						}
					})
						.then((res)=> {
							let list = [];
							if (res.data.code == 200) {
								this.isLoading = false;
                                this.count = Math.ceil(res.data.data.totalCount/8) ;
								list = res.data.data.topics.map(item => {
									return {value: item.topic.id, label: item.topic.name, author:item.user.nickname};
								});
								this.topicList = this.mergeJson(this.topicList,list);
								if (this.num === 1 ) {
									wrap.addEventListener('scroll', () => {
										if (wrap.scrollTop == (wrap.scrollHeight - wrap.offsetHeight) && this.num < this.count ) {
										    if (!this.isLoading) {
												this.num++;
												this.remoteMethod(this.selectQuery);
											}
										};

									});
								}
							}
						})
						.catch(function(error) {
							console.log(error);
						});
			} else {
				this.previewUrl='';
				document.getElementById('seeTopic').removeAttribute('href');
				this.num = 1;
				this.topicList=[];
				this.isLoading = false;
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
		selectChange(value, oldValue) {
			this.canRemote = false;
			if(this.formData.topicName == ""){
			    this.canRemote = true;
			}
		},
		optionChange(obj,obj1){
			this.num=1;
			this.topicList = [];
			this.formData.topicId = obj;
//			this.formData.topicTitle = obj1.substring(0,40) ;
			this.formData.topicName = obj1;
			if(obj != ''){
				Http.get("/api/topic", {
					params: {
						topicId: obj
					}
				})
					.then(res=> {
						if (res.data.code === 200) {
							this.previewUrl = res.data.data.previewUrl;
							document.getElementById('seeTopic').setAttribute('href', this.previewUrl);
							this.skuIds = res.data.data.skuIds;
							this.formData.topicTitle = (res.data.data.name +' --'+ res.data.data.user.nickname).substring(0,40) ;
							this.canRemote = true;
						}
					})
					.catch(function(error) {
						this.canRemote = true;
					});
			}else{
				this.previewUrl='';
			}

		},
		//添加SKU 3=>6=>9
		addUnitProductCount() {
			this.unitProductCount = this.unitProductCount === 9 ? this.unitProductCount : this.unitProductCount + 3;
		},
		handleUnitProudctChange(index) {
			this.unitProductIds[index].isError = false;
		},
		//判断SKU是否重复/合法
		handleUnitProductBlur(index) {
			let items = this.unitProductIds[index];
			let arr = [];
			let arrindex = [];
			this.unitProductIds.map(item =>{
				if(item.itemId){
					arr.push(item.itemId);
				};
			});
			arr.map((item,index)=>{
				if(arr.indexOf(item)!==index){
					arrindex.push(index);
				};
			});
			if(arrindex.length>0){
				arrindex.map(item =>{
					this.unitProductIds[item].isError = true;
					this.unitProductIds[item].errorMsg = '请不要输入重复商品';
				});
			};
			if(arrindex.indexOf(index) == -1){
				Http.get('api/material/sku', {
					hideLoading: true,
					params: { skuId: items.itemId }
				}).then((res) => {
					if (res.data.code === 200 && res.data.data.skuId == null ) {
						this.unitProductIds[index].isError = true;
						this.unitProductIds[index].errorMsg = '请输入正确商品SKU';
					} else {
						this.$message({
							message: res.data.msg,
							type: 'success'
						});
					}
				});
			}
		},
		//返回relatedItems
		getRelatedItems(){
		    let RelatedItems = {}, bsSku=[],handSku=[];
		    if(this.checked){
				this.unitProductIds.map(item =>{
					if(item.itemId!==''){
						let json =  {
						    skuId:item.itemId,
							topicName:this.formData.topicName.substring(0,this.formData.topicName.indexOf('--')),
							previewUrl:this.previewUrl,
							image: this.formData.image
						};
						handSku.push(json);
					}
				});
				this.skuIds.map(item=>{
				    if(item.itemId!==""){
						let json = {
							skuId: item,
							topicName: this.formData.topicName.substring(0,this.formData.topicName.indexOf('--')),
							previewUrl: this.previewUrl,
							image: this.formData.image
						};
						bsSku.push(json);
					};
				});
			};
			RelatedItems.bsSku = bsSku;
			RelatedItems.handSku = handSku;
			return RelatedItems;

		},
		saveMaterial() {
			let size = getImgSize(this.formData.image);
			let skuId = '';
			let params = {
				name: this.formData.name,
				landingPage: this.formData.landingPage,
				type: this.formData.type,
				linkType: this.formData.linkType,
				title: this.formData.title || this.formData.topicTitle,
				image: this.formData.image,
				description: this.formData.description || this.formData.topicDescription,
				width: size.width,
				height: size.height,
				skuId:'',
				topicId:this.formData.topicId,
				relatedItems:this.getRelatedItems(),
			};

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
						Events.$emit('put-save-result', {
							error: 0,
							res: res.data,
							isRebate: this.isRebate
						});
					} else {
						Events.$emit('put-save-result', {
							error: 1,
							res: null
						});
					}
				})
				.catch((e) => {
					Events.$emit('put-save-result', {
						error: 1,
						res: null
					});
				});
		},
		selectImg(index) {
			this.formData.image = this.allImgList[index].url;
			this.allImgList.forEach((item) => {
				item.checked = false;
			});
			this.allImgList[index].checked = true;
			this.$refs.formData.validateField('image');
		},

		changeFile() {
//		    if(this.formData.image!=="") return;
			let input = document.getElementById('topPicture');
			input.click();
		},
		topPictureChange(eve) {
			let files= eve.target.files;
			this.loadFile(files[0]);
		},
		loadFile(file) {
			if (file.size > 100 *1024) {
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
				//加载图片获取图片真实宽度和高度
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
						that.imageUpload(imageData);
					}
				};
				image.src= data;
			};
			reader.readAsDataURL(file);
		},
		imageUpload(data) {
			Http.post(this.uploadUrl, data)
				.then((res) => {
					if (res.data.code === 200) {
						this.formData.image = res.data.data.imageUrl;
					}
				})
				.catch((error) => {
					that.$message({
						message: '上传失败',
						type: 'warning'
					});
				});
		},
	},
};
</script>
<style lang="less">
	.optionList li{
		padding: 0;
	}
	.optionList .optionSPAN{
		display: block;
		width: 100%;
		height: 100%;
		padding: 8px 10px;
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
	.seeTopic{
		float: right;
		height: 36px;
		line-height: 36px;
	}
	.SKUtipicon{
		display: inline-block;
		background: #fff;
		border: 1px solid #b3b2b7;
		text-align: center;
		border-radius: 100%;
		color: #b3b2b7;
		font-size: 12px;
		width: 16px;
		height: 16px;
		line-height: 15px;
	}
	.SKUtip{
		padding: 10px;
		width: 300px;
		border: 1px solid #c0ccda;
		border-radius: 5px;
		position: absolute;
		top: -75px;
		left: -37px;
		z-index: 2;
		background: #fff;
		line-height: 1.5;
	}
	.addProducts {
		position: absolute;
		left: -153px;

	.el-checkbox__label {
		color: #5e6d82;
	}

	}
	a[disabled]{
		color: #bfcbd9;
		cursor: not-allowed;
	}
	.topicName{
		width: 355px;
		display: inline-block;
		input[type='text']{
			width: 355px;
			display: inline-block;
		}
	}
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
