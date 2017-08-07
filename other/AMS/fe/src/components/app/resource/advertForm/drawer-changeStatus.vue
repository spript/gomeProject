<template>
	<div class="media-form">
		<el-form :model="formData" label-width="140px">
			<el-form-item label="广告形式名称：" prop="name">
				<span>{{formData.name}}</span>
			</el-form-item>
			<el-form-item label="广告元素：" prop="name">
				<!--image-->
				<div v-for='(item,index) in formData.adElement.image'>
					<div class="media-form-detail">
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>图片</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-10'>
							<span>{{item.name}}</span>
						</el-form-item>
						<el-form-item label="图片宽度：" class='mrb-10'>
							<span>{{item.width}}</span>&nbsp;&nbsp;px
						</el-form-item>
						<el-form-item label="图片高度：" class='mrb-10'>
							<span>{{item.height}}</span>&nbsp;&nbsp;px
						</el-form-item>
						<el-form-item label="最大体积：" class='mrb-10'>
							<span>{{item.maxVolume}}</span>&nbsp;&nbsp;kb
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
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>文字</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-10'>
							<span>{{item.name}}</span>
						</el-form-item>
						<el-form-item label="最大字符：" class='mrb-10'>
							<span>{{item.max}}</span>&nbsp;&nbsp;字符
						</el-form-item>
						<el-form-item label="最小字符：" class='mrb-10'>
							<span>{{item.min}}</span>&nbsp;&nbsp;字符
						</el-form-item>
					</div>
				</div>
				<!--video-->
				<div v-for='(item,index) in formData.adElement.video'>
					<div class="media-form-detail">
						<el-form-item label="元素类型：" class='mrb-10'>
							<span>视频</span>
						</el-form-item>
						<el-form-item label="元素名称：" class='mrb-10'>
							<span>{{item.name}}</span>
						</el-form-item>
						<el-form-item label="码率：" class='mrb-10'>
							<span>{{item.bitrate}}</span>&nbsp;&nbsp;kbps
						</el-form-item>
						<el-form-item label="大小：" class='mrb-10'>
							<span>{{item.size}}</span>&nbsp;&nbsp;MB
						</el-form-item>
						<el-form-item label="时长：" class='mrb-10'>
							<span>{{item.time}}</span>&nbsp;&nbsp;s
						</el-form-item>
					</div>
				</div>
			</el-form-item>
			<el-form-item label="状态">
				<el-radio-group v-model="formData.status" @change="changeStatus">
					<el-radio :label="2">下线</el-radio>
					<el-radio :label="1">上线</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="预期下线时间：">
				 <el-date-picker
				    v-model="offline_time"
				    v-show='showTime'
				    type="date"
				    :editable="false"
				    :picker-options="pickerOptions0"
				    placeholder="选择日期时间">
			    </el-date-picker>
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
	name: 'app-resource-advertising-drawer-changestatus',
	data() {
		return {
			pickerOptions0: {
	            disabledDate(time) {
	                return time.getTime() < Date.now() - 8.64e7;
	            }
	        },
			formData: {},
			offline_time: new Date(),
			showTime: false,
			compare: '',
			dialogVisible: false,
		    title : '提示',
		    body_html : '',
		    btn_state : ''
		};
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config,
		drawerCtrlAction: () => store.state.drawerCtrl.action,
		config: () => store.state.drawerCtrl
	},
	created() {
		this.formData = copyObj(this.drawerData);
		this.compare = this.formData.status;
		if (this.formData.adElement.image) {
			var arrImage = this.formData.adElement.image;
			for (var i = 0;i<arrImage.length;i++) {
				arrImage[i].suffix = arrImage[i].suffix.split(",");
			}
		}
	},
	methods: {
		changeStatus(data) {
			if (data == 2) {
				this.showTime = true;
			} else {
				this.showTime = false;
			}
		}
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			let params = {
				"formatId": this.formData.formatId,
				"status": this.formData.status,
				"offline_time": this.offline_time
			};
			Event.$emit("changeStatus",params);
		});
	}
};
</script>
