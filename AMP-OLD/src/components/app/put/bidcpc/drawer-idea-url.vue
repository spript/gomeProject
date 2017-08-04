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
					<el-form-item label="URL地址：" prop="landingPage">
						<el-input v-model="formData.landingPage" placeholder="请输入URL地址" :disabled="actionType === 'modify'"></el-input>
					</el-form-item>
					<el-form-item label="广告标题：" prop="title">
						<el-input  v-model="formData.title" placeholder="请输入广告标题"></el-input>
					</el-form-item>
					<el-form-item label="创意图片：" prop="image">
						<!-- <el-upload :action="uploadUrl" accept="image/*" :multiple="false" :on-preview="handlePreview" :on-remove="handleRemove" :on-success="handleSuccess" :show-upload-list="false" :before-upload="handleBeforeUpload"> -->
						<el-upload :action="uploadUrl" accept="image/*" :before-upload="imageSize" :multiple="false" :on-success="handleUploadImageSuccess" :show-file-list="false">
							<el-button size="small" type="primary">点击上传</el-button>
							<div class="el-upload__tip" slot="tip">只能上传图片文件</div>
						</el-upload>
						<div class="row-photo-s">
							<div class="photo-add photo-add-b"><img v-model="formData.image" :src="formData.image" style="width:400px; height:247px;">
							</div>
						</div>
					</el-form-item>
					<el-form-item label="广告文案：" prop="description">
						<el-input  v-model="formData.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}"></el-input>
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
	name: 'app-put-bidcpc-new-idea-url',
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
				imageUrl: {
					validator: (rule, value, callback) => {
						if (this.formData.imageUrl === '') {
							callback(new Error('请选择创意图片'));
						} else {
							callback();
						}
					}
				},
				description: [{
					required: true,
					message: '请输入广告文案',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}]
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
				name: '话题ID',
				disabled: true
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
		this.formData.linkType = 2; //链接类型为 URL
	},
	mounted() {

		Event.$off('put-save');
		Event.$on('put-save', () => {
			this.$refs.formData.validate((result) => {
				if (result) {
						this.saveMaterial();
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
		saveMaterial() {
			let size = getImgSize(this.formData.image);
			let params = {
				name: this.formData.name,
				landingPage: this.formData.landingPage,
				type: this.formData.type,
				linkType: this.formData.linkType,
				title: this.formData.title,
				image: this.formData.image,
				description: this.formData.description,
				width: size.width,
				height: size.height
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
		},

		selectImg(index) {
			this.formData.image = this.allImgList[index].url;
			this.allImgList.forEach((item) => {
				item.checked = false;
			});
			this.allImgList[index].checked = true;
			this.$refs.formData.validateField('image');
		},

		imageSize(file) {
			if (file.size > 500 *1024) {
				this.$message({
					message: '您上传的图片太大，请重新上传！',
					type: 'warning'
				});
				return false;
			}
		},

		handleUploadImageSuccess(response, file, fileList) {
		    if(response.code === 200){
					console.log("upload success");
		    	this.formData.image = response.data.imageUrl;
					console.log("this.formData.image:");
					console.log(this.formData.image);
			}
		},

		handleRemoveImageSuccess() {
				this.formData.image = '';
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
