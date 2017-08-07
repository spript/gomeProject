<template>
	<div class="media-form">
		<el-form :model="formData" ref="formData" label-width="166px">
			<el-form-item label="广告位名称：">
				<span>{{formData.name}}</span>
			</el-form-item>
			<el-form-item label="媒体：">
				<span>{{formData.summary.name}}</span>
			</el-form-item>
			<el-form-item label="频道：">
				<span class="path-col">{{formData.summary.levelone+' | '+formData.summary.leveltwo}}</span>
			</el-form-item>
			<el-form-item label="广告位尺寸：">
				<span class="path-col"><span>宽 </span>{{formData.width}} px&nbsp;&nbsp;&nbsp;&nbsp;<span>高 </span>{{formData.height}} px</span>
			</el-form-item>
			<el-form-item label="轮播数：">
				<span>{{formData.rotators}}</span>
			</el-form-item>
			<el-form-item label="外采流量：">
				<span>{{formData.outSourceFlow==0?'否':'是'}}</span>
			</el-form-item>
			<el-form-item label="素材多级审核：">
				<span>{{formData.approveFlowId== 1?'否':'是'}}</span>
			</el-form-item>
			<el-form-item label="描述：">
				<span>{{formData.description}}</span>
			</el-form-item>
			<el-form-item label="单次请求返回素材数量：">
				<span>{{formData.materialNumber}}</span>
			</el-form-item>
			<el-form-item label="状态：">
				<el-radio-group v-model="formData.status" @change="changeStatus">
					<el-radio :label="0">下线</el-radio>
					<el-radio :label="1">售卖</el-radio>
					<el-radio :label="2">非售卖</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="生效时间：" v-show='showTime'>
				<el-date-picker
				    v-model="effectTime"
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
			effectTime: new Date(),
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
	},
	methods: {
		changeStatus(data) {
			this.showTime = true;
		}
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			let params = {
				"slotId": this.formData.slotId,
				"status": this.formData.status,
				"compare": this.compare,
				"effectTime": this.effectTime
			};
			Event.$emit("changeStatus",params);
		});
	}
};
</script>
