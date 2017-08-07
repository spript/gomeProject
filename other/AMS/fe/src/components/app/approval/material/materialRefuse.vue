<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="showRefuse" @click="closeView">></div>
		</transition>
		<transition name="drawer">
			<div class="amp-drawer" v-if="showRefuse" >
				<div class="drawer-part">
				 	<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">审核拒绝</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<div class="shop-cont">
						<div>审核拒绝原因(请至少选择一项)：</div>
						<br>
						<el-checkbox-group v-model="checkList">
							<el-checkbox label="资质审核未通过"></el-checkbox>
							<el-checkbox label="广告内容审核未通过"></el-checkbox>
							<br>
							<el-checkbox label="广告链接不合格"></el-checkbox>
							<el-checkbox label="其他"></el-checkbox>
						</el-checkbox-group>
						<el-input
						  type="textarea"
						  placeholder="请输入审核未通过的原因，不超过200个字符"
						  v-model="textarea"
						  @change="handleIpnutChange"
						  v-if="isOtherSelected">
						</el-input>
						<span style="color:red" v-if="reasonType === 'empty'">请输入审核未通过的原因</span>
						<span style="color:red" v-else-if="reasonType === 'maxLength'">审核原因，不超过200个字符</span>
						<span style="color:red" v-else-if="reasonType === 'space'">未包含汉字、字母、数字其中之一</span>
						<br>
						<span>附件:</span>
						 <el-upload
	                        :action="imageUrl"
	                        :accept="fileType"
							:on-success="upSuccess"
							:on-remove="upRemove"
							:before-upload="imageSize"
							:multiple=true
							:default-file-list="images.list">
	                          <el-button size="small" type="primary">选择文件</el-button>
							  <span class="form-gray-text">可以上传png、jpg、jpeg、gif文件，且不超过500kb</span>
	                        </el-upload>
					</div>
				</div>
				<div class="drawer-btn">
					<span class="btn btn-primary" @click="materialRefuseAction">确定</span>
					<span class="btn btn-gray" @click="closeView">取消</span>
				</div>
			</div>
		</transition>
	</div>
</template>
<script>
import Event from 'event';
import Http from 'utils/http';
import apiConfig from '../../../../config/api.config.js';
export default {
	name: 'app-material-refuse',
	props: ['showRefuse','materialId'],
	data() {
		return {
			imageUrl: `${apiConfig[process.env.NODE_ENV]}/api/image/upload`,
	        fileType: ".jpg, .png, .jpeg, .gif, |images/*",
	        size:0,
	        images: [],
	        imagesList: [],
	        checkList: [],
	        textarea: '',
	        reasonType: ''
		};
	},
	computed: {
		isOtherSelected() {
			for (let i = 0;i < this.checkList.length;i++) {
				if (this.checkList[i] === '其他') {
					return true;
				}
			}
			return false;
		}
	},
	methods: {
		handleIpnutChange(value) {
			this.reasonType = value.length > 0 ? '' : 'empty';
		},
		closeView() {
			this.images = [];
			this.imagesLis = [];
			this.checkList = [];
			this.textarea = '';
			this.reasonType = '';
			Event.$emit('close-refuse');
		},
		upSuccess(response, file, fileList){
			this.images.push(response.data.imageUrl);
		},
		upRemove(file, fileList){
			for(var key in this.images.list){
			    if(key == file.name){
			    	delete this.images[key];
			    }
			}
		},
		imageSize(file) {
			if (this.imagesList.length < 5) {
				if (file.size > 500 *1024) {
					this.$message({
						message: '您上传的图片太大，请重新上传！',
						type: 'warning'
					});
					return false;
				}
				this.imagesList.push(file.name);
			} else {
				this.$message({
					message: '您上传的图片超出5张！',
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
		},
		materialRefuseAction() {
			let vm = this;
			let str = '[\u4E00-\u9FA5A-Za-z0-9]{2,20}';
			let reg = new RegExp(str);
			if (this.checkList.length === 0) {
				this.$message({
					message: '请至少选择一项',
					type: 'warning'
				});
				return false;
			} else {
				if (this.checkList.find((value) => value === '其他')) {
					let text = this.textarea.trim();
					if (this.textarea.length > 0 && this.textarea.length <= 200) {
						if (!reg.test(text)) {
							this.reasonType = 'space';
						} else {
							this.refuseRequest();
						}
					} else {
						this.reasonType = this.textarea.length === 0 ? 'empty' : (this.textarea.length > 200 ? 'maxLength' : '') ;
					}
				} else {
					this.refuseRequest();
				}
			}
		},
		refuseRequest() {
			let vm = this;
			let otherIndex = this.checkList.findIndex((value) => value === '其他');
			if (otherIndex !== -1) {
				this.checkList[otherIndex] = this.textarea;
			}
			Http.put('/api/approval/materials/reject', {
				materialId: vm.materialId,
				rejectReasons:vm.checkList,
				attachments:vm.images
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.checkList = [];
					vm.images = [];
					vm.textarea = '';
					Event.$emit('refuse-material');
				}
			}).catch((error) => {
				console.log(error);
			});
		}
    },
    watch: {
    	'isOtherSelected': function(val) {
    		if (!val) {
    			this.reasonType = '';
    		}
    	}
    }
};
</script>
<style scoped>

.amp-drawer .drawer-btn{
	height: 80px;
}
.amp-drawer .drawer-btn:before{
	height: 80px;
}
.dialog-tab{
	padding: 0 20px;
}
.btn{
	padding: 0 15px;
	height: 30px;
	line-height: 30px;
	font-size: 14px;
}

</style>