<template>
	<div>
		<div class="unfold-content">
			<template v-for="certificate in certificates">
				<div class="column-group">
					<div class="group-aside">资质名称：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ certificate.name }}</span></div>
					</div>
				</div>
				<div class="column-group">
					<div class="group-aside">资质类型：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">商家资质</span></div>
					</div>
				</div>
				<div class="column-group">
					<div class="group-aside">资质有效期：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ certificate.startTime }}~{{ certificate.endTime }}</span></div>
					</div>
				</div>
				<div class="column-group">
					<div class="group-detail no-left">
						<div class="detail-row">
							<div class="row-photo-l" v-for="img in certificate.images">
								<div class="photo"><img :src="img"></div>
							</div>
						</div>
					</div>
				</div>
			</template>
		</div>
	</div>
</template>
<script>
import Http from 'utils/http.js';
import { formatDate } from 'utils/common.js';
export default {
	name: 'material-dialog-certificate',
	data() {
		return {
			certificates: []
		};
	},
	props: ['userId'],
	created() {
		this.getMaterialCertificate();
	},
	methods: {
		getMaterialCertificate() {
			let vm = this;
			Http.get('/api/certificate', {
				params: {
					userId: vm.userId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.certificates = res.data.data;
					vm.certificates.forEach((item, index) => {
						let imgArr = [];
						if (item.images.indexOf(',') > -1) {
							imgArr = item.images.split(',');
						} else {
							imgArr.push(item.images);
						}
						item.images = imgArr;
						item.startTime = formatDate(item.startTime, 'yyyy年MM月dd日');
						item.endTime = formatDate(item.endTime, 'yyyy年MM月dd日');
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
.photo{
	width: 350px;
}
.photo img{
	width: 100%;
	height: auto;
	margin-bottom: 12px;
}
.no-left{margin-left: 128px;}
</style>