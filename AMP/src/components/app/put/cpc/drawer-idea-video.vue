<template>
	<div class="amp-form amp-form03">
		<div class="form-column">
			<el-form :model="formData" :rules="rules" ref="formData" label-width="124px">
				<el-form-item label="创意名称：" prop="name">
					<el-input v-model="formData.name" placeholder="请输入创意名称"></el-input>
				</el-form-item>

				<el-form-item label="广告标题：" prop="title">
					<el-input v-model="formData.title" placeholder="请输入广告标题"></el-input>
				</el-form-item>

				<el-form-item label="上传视频：" prop="video">
					<div class="row-photo-s" style="width: 100%;">
						<div id="flashContent">
							<div id="flashPic">
							</div>
						</div>
					    <input type="hidden" id="videoIds" value="33">
					</div>
				</el-form-item>
				<el-form-item label="点播">
					<el-button @click="videoPlay">点播</el-button>
				</el-form-item>

				<el-form-item label="封面图片：">
					<el-radio-group v-model="formData.videoImageType">
						<el-radio :label="1">默认图片</el-radio>
						<el-radio :label="2">自定义图片
							<span v-if="formData.videoImageType == 2">
                <input id="topPicture" type="file"
					   accept="image/jpg,image/jpeg,image/png"
					   style="width: 68px; height: 28px; display: block; position: absolute; opacity: 0">
                <div class="imgupload" style="display: inline-block; margin-left: 10px">点击上传</div>
              </span>
						</el-radio>
						<!-- :disabled="actionType === 'modify'"-->
					</el-radio-group>
					<div v-if="formData.videoImageType == 2">
						<div class="row-photo-s">
							<div class="photo-add photo-add-b">
								<img src="" style="width: 400px; height: 247px;">
							</div>
						</div>
					</div>
				</el-form-item>

				<el-form-item label="推广内容：" prop="videoPromoteDetail">
					<el-radio-group v-model="formData.videoPromoteDetail" @change="promoteDetailChange">
						<el-radio class="radio" v-model="formData.promoteDetail" :label="0" style="display: none">店铺
						</el-radio>
						<el-radio class="radio" v-model="formData.promoteDetail" :label="1" style="margin-left: 0px">
							店铺
						</el-radio>
						<el-radio class="radio" v-model="formData.promoteDetail" :label="2">商品</el-radio>
					</el-radio-group>
				</el-form-item>

				<div v-if="formData.videoPromoteDetail == 1">
					<el-form-item label="视频描述：" prop="videoDescription">
						<el-input v-model="formData.videoDescription" type="textarea"
								  :autosize="{ minRows: 2, maxRows: 4}"></el-input>
					</el-form-item>
					<el-form-item label="店铺LOGO：">
						<div>
							<div class="photo-add photo-add-b">
								<img v-model="formData.shopLogo" :src="formData.shopLogo"
									 style="width:400px; height:247px;">
							</div>
						</div>
					</el-form-item>
					<el-form-item label="店铺名称：">
						<div v-model="formData.shopName">1111</div>
					</el-form-item>
					<el-form-item label="店铺文案：" prop="shopDescription">
						<el-input v-model="formData.shopDescription" type="textarea"
								  :autosize="{ minRows: 2, maxRows: 4}"></el-input>
					</el-form-item>
					<el-form-item>
						<div class="SKUtip" v-show="SKUtip">若您在跳转后的链接内也输入了跟单商品，则在统计效果时，此页面和在跳转后的链接内输入的商品SKU都会被统计到。</div>
						<el-checkbox v-model="checked" @change='checkboxChange' class="addProducts" :disabled="!itemCan"
									 style="position: absolute">添加跟单商品
							<span @mouseenter="SKUtip = !SKUtip" @mouseleave="SKUtip = !SKUtip"
								  class="SKUtipicon">?</span> :
						</el-checkbox>
						<div v-show="checked">
							<el-row>
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[0].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(0)" @change="handleUnitProudctChange(0)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[0].isError">{{
											unitProductIds[0].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[1].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(1)" @change="handleUnitProudctChange(1)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[1].isError">{{
											unitProductIds[1].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[2].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(2)" @change="handleUnitProudctChange(2)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[2].isError">{{
											unitProductIds[2].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row v-show="unitProductCount > 3">
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[3].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(3)" @change="handleUnitProudctChange(3)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[3].isError">{{
											unitProductIds[3].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[4].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(4)" @change="handleUnitProudctChange(4)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[4].isError">{{
											unitProductIds[4].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[5].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(5)" @change="handleUnitProudctChange(5)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[5].isError">{{
											unitProductIds[5].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row v-show="unitProductCount > 6">
								<el-col :span="8" style="padding-right:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[6].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(6)" @change="handleUnitProudctChange(6)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[6].isError">{{
											unitProductIds[6].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding:0 5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[7].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(7)" @change="handleUnitProudctChange(7)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[7].isError">{{
											unitProductIds[7].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
								<el-col :span="8" style="padding-left:5px;">
									<el-form-item>
										<el-input v-model="unitProductIds[8].itemId" placeholder="请输入商品SKU"
												  @blur="handleUnitProductBlur(8)" @change="handleUnitProudctChange(8)"
												  :disabled="!itemCan"></el-input>
										<div class="el-form-item__error" v-if="unitProductIds[8].isError">{{
											unitProductIds[8].errorMsg }}
										</div>
									</el-form-item>
								</el-col>
							</el-row>
							<div class="add-fn" v-show="unitProductCount !== 9" @click="addUnitProductCount"><span
								class="btn btn-normal"></span></div>
						</div>
					</el-form-item>
				</div>
				<div v-if="formData.videoPromoteDetail == 2">
					<el-form-item label="视频描述：" prop="videoDescription">
						<el-input v-model="formData.videoDescription" type="textarea"
								  :autosize="{ minRows: 2, maxRows: 4}"></el-input>
					</el-form-item>
					<div>
						<el-row>
							<el-form-item label="商品SKU:" prop="">
								<el-input v-model="unitProductSkuIds[0].SKUId" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品名称:" prop="">
								<el-input v-model="unitProductSkuIds[0].SKUName" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品图片:" prop="">
								<el-input v-model="unitProductSkuIds[0].SKUPic" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品文案:" prop="">
								<el-input v-model="unitProductSkuIds[0].SKUDes" type="textarea"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<el-row v-show='unitProductSkuCount == 2'>
							<el-form-item label="商品SKU:" prop="">
								<el-input v-model="unitProductSkuIds[1].SKUId" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品名称:" prop="">
								<el-input v-model="unitProductSkuIds[1].SKUName" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品图片:" prop="">
								<el-input v-model="unitProductSkuIds[1].SKUPic" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品文案:" prop="">
								<el-input v-model="unitProductSkuIds[1].SKUDes" type="textarea"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<el-row v-show='unitProductSkuCount == 3'>
							<el-form-item label="商品SKU:" prop="">
								<el-input v-model="unitProductSkuIds[2].SKUId" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品名称:" prop="">
								<el-input v-model="unitProductSkuIds[2].SKUName" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品图片:" prop="">
								<el-input v-model="unitProductSkuIds[2].SKUPic" type="text"></el-input>
							</el-form-item>
							<el-form-item label="商品文案:" prop="">
								<el-input v-model="unitProductSkuIds[2].SKUDes" type="textarea"
										  :autosize="{ minRows: 2, maxRows: 4}">
								</el-input>
							</el-form-item>
						</el-row>
						<div class="add-fn" v-show="unitProductSkuCount !== 3" @click="addUnitProductSkuCount">
							<span class="btn btn-normal"></span>
						</div>
					</div>
				</div>
			</el-form>
		</div>
		<el-dialog title="" v-model="dialogVideoVisible" size="large" @close="closeVideo">
			<div class="videoContainer" id="videoContainer" style="width:455px; height: 300px;"></div>
		</el-dialog>
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
		isEmptyObj,
	} from 'utils/common';
	import Events from 'event';
	import Http from 'http';
	export default {
		name: 'app-put-new-video',
		data() {
			return {
				formData: {},
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
					title: [{
						required: true,
						message: '请输入广告标题',
						trigger: 'blur'
					}, {
						max: 30,
						message: '最大长度不超过30个字符',
						trigger: 'blur'
					}],
					video: [{
						required: true,
						message: '请上传视频',
						trigger: 'blur'
					}],
					videoDescription: [{
						required: true,
						message: '请输入视频描述',
						trigger: 'blur'
					}],
					shopDescription: [{
						required: true,
						message: '请输入店铺描述',
						trigger: 'blur'
					}]
				},
				SKUtip: false,
				unitProductCount: 3,
				unitProductSkuCount: 1,
				itemCan: true,
				checked: false,
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
				unitProductSkuIds: [
					{SKUId: '', SKUName: '', SKUPic: '', SKUDes: '', isError: false, errorMsg: ''},
					{SKUId: '', SKUName: '', SKUPic: '', SKUDes: '', isError: false, errorMsg: ''},
					{SKUId: '', SKUName: '', SKUPic: '', SKUDes: '', isError: false, errorMsg: ''},
				],
				dialogVideoVisible: false,

			};
		},
		computed: {
			drawerData: () => store.state.drawerCtrl.config, // from store
			drawerCtrlAction: () => store.state.drawerCtrl.action,
			isRebate: () => store.state.drawerCtrl.isRebate, // 是否返利，从 state 中来 如果被复写掉了为 false 达到知晓上一级是否返利的目的
			actionType: () => store.state.drawerCtrl.action,
		},
		created(){
			this.formData = copyObj(this.drawerData);
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

			let flashPic = document.getElementById('flashPic');
			let pageHost = ((document.location.protocol == "https:") ? "https://" : "http://");
			flashPic.innerHTML = "<a href='http://www.adobe.com/go/getflashplayer'><img src='"
				+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>";

			var head= document.getElementsByTagName('head')[0];
			var script= document.createElement('script');
			script.type= 'text/javascript';
			script.src= 'http://10.69.205.18:8000/static/js/uploadVideo.js';
			head.appendChild(script);
		},

		methods: {
	/*		uploadVideo(){
				let flashContent = document.getElementById('flashContent');
				let swfVersionStr = "11.4.0";
				let xiSwfUrlStr = "expressInstall.swf";
				var flashvars = {
					// env : "dev",
					// appname : "h5",
					// user_id : "1",
					// description : "测试数据",
					// tag : "视频,新闻,体育"
				};
				var params = {};
				params.quality = "high";
				params.bgcolor = "#fff";
				params.allowscriptaccess = "always";
				params.allowfullscreen = "true";
				var attributes = {};
				attributes.id = "video-upload";
				attributes.name = "video-upload";
				attributes.align = "middle";
				attributes.bgcolor="#fff";
				swfobject.embedSWF(
					"https://js.meixincdn.com/gvs-player/dist/utils/video-upload.swf?t=" + new Date().getTime(),
					"flashContent",
					"240", "80",
					swfVersionStr, xiSwfUrlStr,
					flashvars, params, attributes);
				swfobject.createCSS(flashContent, "display:block;text-align:left;");
				this.uploadPreparing();

			},
			uploadPreparing() {
				Http.get("api/video/token", {}).then((res) => {
					if (res.data.code == 200) {
						this.thisFlash("video-upload").setData({
							env: res.data.env,
							appname: res.data.appname,
							token: res.data.token,
							user_id: "11",
							description: "",
							tag: ""
						});
					}
				}).catch((error) => {
					console.log(error);
				})
			},
			thisFlash(flashName) {
				if (navigator.appName.indexOf("Microsoft") != -1) {
					return window[flashName];
				} else {
					return document[flashName];
				}
			},*/
			videoPlay(){
				//点播视频
				var v = new MeixinPlayer();
				this.dialogVideoVisible = true;
				v.init(10309, 'videoContainer',{
					showFullBtn:true
				});
			},
			closeVideo(){
			    document.getElementById('videoContainer').innerHTML='';
			},
			promoteDetailChange(obj){
				this.unitProductSkuCount = 1;
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
				this.unitProductIds.map(item => {
					if (item.itemId) {
						arr.push(item.itemId);
					}
					;
				});
				arr.map((item, index) => {
					if (arr.indexOf(item) !== index) {
						arrindex.push(index);
					}
					;
				});
				if (arrindex.length > 0) {
					arrindex.map(item => {
						this.unitProductIds[item].isError = true;
						this.unitProductIds[item].errorMsg = '请不要输入重复商品';
					});
				}
				;
				if (arrindex.indexOf(index) == -1) {
					Http.get('api/material/sku', {
						hideLoading: true,
						params: {skuId: items.itemId}
					}).then((res) => {
						if (res.data.code === 200 && res.data.data.skuId == null) {
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
			//SKU显示和隐藏，reset问题
			checkboxChange(){
				this.unitProductCount = 3;
				this.unitProductIds = [
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''},
					{itemId: '', isError: false, errorMsg: ''}
				];
			},
			//商品添加SKU 1=>2=>3
			addUnitProductSkuCount(){
				this.unitProductSkuCount = this.unitProductSkuCount === 3 ? this.unitProductSkuCount : this.unitProductSkuCount + 1;
			}

		}
	};

</script>
<style>
	.imgupload {
		background-image: none;
		text-align: center;
		font-size: 12px;
		border-radius: 4px;
		color: #fff;
		background-color: #20a0ff;
		border-color: #20a0ff;
		width: 68px;
		height: 28px;
		line-height: 28px;
		cursor: pointer;
	}

	#video {
		visibility: hidden;
		width: 400px;
		height: 247px;
	}
	.el-dialog__wrapper .el-dialog{
		width:500px;
	}
</style>
