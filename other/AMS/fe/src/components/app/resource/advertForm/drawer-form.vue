<template>
	<div class="media-form">
		<el-form :model="formData" :rules="rules" ref="formData" label-width="140px">
			<el-form-item label="广告形式名称：" prop="name">
				<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="广告元素：">
				<!--image-->
				<div v-for='(item,index) in formData.adElement.image'>
					<div class="media-form-detail">
						<em @click='close("image",index)' class="icon icon-close" style="margin-left:445px;cursor: pointer;"></em>
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>图片</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-10'>
							<el-input v-model="item.name" style='width:200px;'></el-input>
						</el-form-item>
						<el-form-item label="图片宽度：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.width.length==1?item.width=item.width.replace(/[^1-9]/g,''):item.width=item.width.replace(/\D/g,'')" v-model="item.width" class='w200 jugeInput'></input>&nbsp;&nbsp;px
						</el-form-item>
						<el-form-item label="图片高度：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.height.length==1?item.height=item.height.replace(/[^1-9]/g,''):item.height=item.height.replace(/\D/g,'')" v-model="item.height" class='w200 jugeInput'></input>&nbsp;&nbsp;px
						</el-form-item>
						<el-form-item label="最大体积：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.maxVolume.length==1?item.maxVolume=item.maxVolume.replace(/[^1-9]/g,''):item.maxVolume=item.maxVolume.replace(/\D/g,'')" v-model="item.maxVolume" class='w200 jugeInput'></input>&nbsp;&nbsp;kb
						</el-form-item>
						<el-form-item label="格式：" class='mrb-10'>
							<el-checkbox-group v-model="item.suffix">
							    <el-checkbox label="jpg">jpg</el-checkbox>
								<el-checkbox label="jpeg">jpeg</el-checkbox>
								<el-checkbox label="png">png</el-checkbox>
								<el-checkbox label="gif">gif</el-checkbox>
							  </el-checkbox-group>
						</el-form-item>
					</div>
				</div>
				<!--text-->
				<div v-for='(item,index) in formData.adElement.text'>
					<div class="media-form-detail">
						<em @click='close("text",index)' class="icon icon-close" style="margin-left:445px;cursor: pointer;"></em>
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>文字</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-20'>
							<el-input v-model="item.name" style='width:200px;'></el-input>
						</el-form-item>
						<el-form-item label="最大字符：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.max.length==1?item.max=item.max.replace(/[^1-9]/g,''):item.max=item.max.replace(/\D/g,'');jugeMax(item.max,item.min);" v-model="item.max" class='w200 jugeInput'></input>&nbsp;&nbsp;字符
							<p id='errorMax' class="error none" style="padding-left: 140px;padding-top: 3px;">最大字符不能小于最小字符</p>
						</el-form-item>
						<el-form-item label="最小字符：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.min.length==1?item.min=item.min.replace(/[^1-9]/g,''):item.min=item.min.replace(/\D/g,'');jugeMin(item.max,item.min)" v-model="item.min" class='w200 jugeInput'></input>&nbsp;&nbsp;字符
							<p id='errorMin' class="error none" style="padding-left: 140px;padding-top: 3px;">最小字符不能大于最大字符</p>
						</el-form-item>
					</div>
				</div>
				<!--video-->
				<div v-for='(item,index) in formData.adElement.video'>
					<div class="media-form-detail">
						<em @click='close("video",index)' class="icon icon-close" style="margin-left:445px;cursor: pointer;"></em>
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>视频</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-10'>
							<el-input v-model="item.name" style='width:200px;'></el-input>
						</el-form-item>
						<el-form-item label="码率：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.bitrate.length==1?item.bitrate=item.bitrate.replace(/[^1-9]/g,''):item.bitrate=item.bitrate.replace(/\D/g,'')"v-model="item.bitrate" class='w200 jugeInput'></input>&nbsp;&nbsp;kbps
						</el-form-item>
						<el-form-item label="大小：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.size.length==1?item.size=item.size.replace(/[^1-9]/g,''):item.size=item.size.replace(/\D/g,'')" v-model="item.size" class='w200 jugeInput'></input>&nbsp;&nbsp;MB
						</el-form-item>
						<el-form-item label="时长：" class='mrb-10'>
							<input onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="item.time.length==1?item.time=item.time.replace(/[^1-9]/g,''):item.time=item.time.replace(/\D/g,'')" v-model="item.time" class='w200 jugeInput'></input>&nbsp;&nbsp;s
						</el-form-item>
					</div>
				</div>
				<!--下拉菜单-->
				<el-dropdown menu-align='start' @command="handleCommand">
				    <el-button type='default' size='large'>
				                    添加   <i class="el-icon-caret-bottom el-icon--right"></i>
				    </el-button>
				    <el-dropdown-menu slot="dropdown">
					    <el-dropdown-item command='image'>图片</el-dropdown-item>
					    <el-dropdown-item command='text'>文字</el-dropdown-item>
					    <el-dropdown-item command='video'>视频</el-dropdown-item>
				    </el-dropdown-menu>
				</el-dropdown>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
import Event from 'event';
import actions from 'actions';
import store from 'store';
import http from 'http';
import {
	objType,
	mixin,
	copyObj
} from 'utils/common';
export default {
	name: 'app-resource-advertForm',
	data() {
		return {
			showImage: false,
			showText: false,
			showVideo: false,
			formData: {},
			rules: {
				name: [{
					required: true,
					message: '请输入广告形式名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				imageName: [{
					required: true,
					message: '请输入元素名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				textName: [{
					required: true,
					message: '请输入元素名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}],
				videoName: [{
					required: true,
					message: '请输入元素名称',
					trigger: 'blur'
				}, {
					max: 60,
					message: '最大长度不超过60个字符',
					trigger: 'blur'
				}]
			},
			httpMethodMap: {
				new: 'post',
				modify: 'put'
			},
			showSlot: []
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config,
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		config: () => store.state.drawerCtrl
	},
	created() {
		this.formData = copyObj(this.drawerData);
		if (this.drawerCtrlAction == 'modify') {
			var arrImage = this.formData.adElement.image;
			for (var i = 0;i<arrImage.length;i++) {
				arrImage[i].suffix = arrImage[i].suffix.split(",");
			}
		}
	},
	methods: {
		jugeMax(max,min) {
			if (Number(max) < Number(min)) {
				var oP = document.getElementById('errorMax');
				var className = document.getElementById('errorMax').className;
				className = className.replace(" none","");
				oP.setAttribute('class', className);
			} else {
				var oP = document.getElementById('errorMax');
				var className = document.getElementById('errorMax').className;
				if (className.indexOf('none') == -1) {
					className = className.concat(" none");
					oP.setAttribute('class', className);
				}
				
				//移除min的error
				var oMin = document.getElementById('errorMin');
				if (oMin.className.indexOf('none') == -1) {
					oMin.className = oMin.className.concat(" none");
					oP.setAttribute('class', oMin.className);
				}
			}
		},
		jugeMin(max,min) {
			if (Number(min) > Number(max)) {
				var oP = document.getElementById('errorMin');
				var className = document.getElementById('errorMin').className;
				className = className.replace(" none","");
				oP.setAttribute('class', className);
			} else {
				var oP = document.getElementById('errorMin');
				var className = document.getElementById('errorMin').className;
				if (className.indexOf('none') == -1) {
					className = className.concat(" none");
					oP.setAttribute('class', className);
				}
				//移除max的error
				var oMin = document.getElementById('errorMax');
				if (oMin.className.indexOf('none') == -1) {
					oMin.className = oMin.className.concat(" none");
					oP.setAttribute('class', oMin.className);
				}
			}
		},
		handleCommand(command) {
          	var oImage = {
					name: "",
	                width: "",
	                height: "",
	                maxVolume: "",
	                suffix: []
                };
            var oVideo = {
					name: "",
	                bitrate: "",
	                size: "",
	                time: ""
                };
            var oText = {
					name: "",
	                max: "",
	                min: ""
                };
          	if (command == 'image') {
          		this.formData.adElement.image.push(oImage);
          	} else if (command == 'video') {
          		this.formData.adElement.video.push(oVideo);
          	} else if (command == 'text') {
          		this.formData.adElement.text.push(oText);
          	}
        },
        close(material,index) {
        	Array.prototype.del = function(dx) { 
				if ( isNaN(dx) || dx > this.length ) { return false;} 
				this.splice(dx,1); 
			};
        	if (material == 'image') {
        		this.formData.adElement.image.del(index);
        	} else if (material == 'video') {
        		this.formData.adElement.video.del(index);
        	} else if (material == 'text') {
        		this.formData.adElement.text.del(index);
        	}
        }
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			this.$refs.formData.validate((result) => {
				if(result) {
					var arrImage = this.formData.adElement.image;
				for (var i = 0;i<arrImage.length;i++) {
					arrImage[i].suffix = arrImage[i].suffix.join(",");
				}
				let params = this.formData;
				http({
						url:'/api/format',
						method: this.httpMethodMap[this.drawerCtrlAction],
						data: params
					})
					.then((res) => {
						console.log(res);
						if (!res.data.iserror) {
							Event.$emit('media-save-result', {
								error: 0,
								res: res.data
							});
						} else {
							Event.$emit('media-save-result', {
								error: 1,
								res: null
							});
						}
					})
					.catch((e) => {
						Event.$emit('media-save-result', {
							error: 1,
							res: null
						});
					});
				} else {
					Event.$emit('media-save-result', {
						error: 1,
						res: null
					});
				}
			});
		});
	}
};
</script>
