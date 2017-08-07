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
							<el-checkbox label="证件信息错误"></el-checkbox>
							<el-checkbox label="证件已过期"></el-checkbox>
							<br>
							<el-checkbox label="证件图片不清晰"></el-checkbox>
							<el-checkbox label="其他"></el-checkbox>
						</el-checkbox-group>
						<el-input
						  type="textarea"
						  placeholder="请输入审核未通过的原因，不超过200个字符"
						  v-model="textarea"
						  @change="handleIpnutChange"
						  v-if="isOtherSelected" :maxlength="200">
						</el-input>
						<span style="color:red" v-if="reasonType === 'empty'">请输入审核未通过的原因</span>
						<span style="color:red" v-else-if="reasonType === 'maxLength'">审核原因，不超过200个字符</span>
						<span style="color:red" v-else-if="reasonType === 'space'">未包含汉字、字母、数字其中之一</span>
						<br>
					</div>
				</div>
				<div class="drawer-btn">
					<span class="btn btn-primary" @click="advertiserRefuseAction">确定</span>
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
	name: 'app-advertiser-refuse',
	props: ['showRefuse','advertiserId'],
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
		advertiserRefuseAction() {
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
					if (text.length > 0 && text.length <= 200) {
						if (!reg.test(text)) {
							this.reasonType = 'space';
						} else {
							this.refuseRequest();
						}
					} else {
						if (text.length === 0) {
							this.reasonType = 'empty';
						} 
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
			Http.put('/api/approval/advertiser/reject', {
				advertiserId: vm.advertiserId,
				rejectReason:vm.checkList
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.checkList = [];
					vm.textarea = '';
					Event.$emit('refuse-advertiser');
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
