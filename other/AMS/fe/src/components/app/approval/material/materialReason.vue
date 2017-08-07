<template>
	<div>
		<div class="unfold-content">
			<template v-for="material in list">
				<div class="column-group"  style="margin-top:20px;">
					<div class="group-aside">审核状态：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ material.approvalStatus === 1 ? '审核已通过' : '审核未通过' }}</span></div>	
					</div>
				</div>
				<div class="column-group" v-if ="material.approvalStatus === -1">
					<div class="group-aside">审核拒绝原因：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ material.rejectReasons }}</span></div>	
					</div>
				</div>
				<div class="column-group">
					<div class="group-aside">操作人：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ material.accountName }}</span></div>
					</div>
				</div>
				<div class="column-group">
					<div class="group-aside">审核时间：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ material.createTime }}</span></div>
					</div>
				</div>
			</template>
		</div>
	</div>
</template>
<script>
import Http from 'utils/http.js';
import { formatDate, debounce } from 'utils/common';
export default {
	name: 'material-reject-reason',
	
	data() {
		return {
			list: {}
		};
	},
	props: ['materialId'],
	created() {
		this.getMaterialReason();
	},
	methods: {
		getMaterialReason() {
			let vm = this;
			Http.get('/api/approval/logs', {
				params: {
					materialId: vm.materialId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.list = res.data.data;
					vm.list.forEach((item, index) => {
						item.createTime = formatDate(item.createTime, 'yyyy年MM月dd日 hh:mm:ss');
					});
				} else {
					vm.$message({
						message: res.data.msg,
						type: 'error'
					});
				}
			}).catch((error) => {
				console.log(error);
			});
		}
	}
};
</script>
<style scoped>
.unfold-content {
    overflow: auto;
}
.group-detail {
    margin-left: 128px;
    padding-bottom: 0;
}
.group-aside {
    float: left;
    width: 140px;
    line-height: 40px;
    font-size: 14px;
    text-align: right;
    margin-left:30px;
}
</style>