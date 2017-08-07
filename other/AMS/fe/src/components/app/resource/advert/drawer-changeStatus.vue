<template>
	<div class="media-form">
		<el-form :model="formData" ref="formData" label-width="166px">
			<el-form-item label="广告名称：">
				<span>{{formData.name}}</span>
			</el-form-item>
			<el-form-item label="广告位名称：">
				<span>{{formData.summary.advertisingName}}</span>
			</el-form-item>
			<el-form-item label="计费模式：">
				<span>{{formData.saleMode==1?"定价CPC":(formData.saleMode==2?"竞价CPM":(formData.saleMode==3?"定价CPM(合约)":(formData.saleMode==4?"定价CPM(非合约)":(formData.saleMode==5?"CPD":"其他"))))}}</span>
			</el-form-item>
			<el-form-item label="广告形式：">
				<span class="path-col">{{formData.formatName}}</span>
			</el-form-item>
			<el-form-item label="模板：">
				<span class="path-col">{{formData.templateName}}</span>
			</el-form-item>
			<el-form-item label="状态">
				<el-radio-group v-model="formData.status" @change="changeStatus">
					<el-radio :label="0">下线</el-radio>
					<el-radio :label="1">上线</el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="预期下线时间：" v-show='showTime'>
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
			compare: ''
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
		if (this.formData.status == 1) {
			this.showTime = false;
	    } else {
			this.showTime = true;
		}
	},
	methods: {
		changeStatus(data) {
			if (data == 1) {
				this.showTime = false;
			} else {
				this.showTime = true;
			}
		}
	},
	mounted() {
		Event.$off('media-save');
		Event.$on('media-save', () => {
			let params = {
				"advertisementId": this.formData.advertisementId,
				"status": this.formData.status,
				"effectTime": this.effectTime,
				"compare": this.compare
			};
			Event.$emit("changeStatus",params);
		});
	}
};
</script>
