<template>
	<div>
		<div class="unfold-content" v-if ="material.createFrom == 'aos'" v-for="(materialComponent, index) in material.materialComponents" style="margin-bottom: 20px;">
			<div class="column-group" v-show="index == 0">
				<div class="group-aside">素材名称：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ materialComponent.name }}</span></div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">链接类型：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ materialComponent.linkType }}</span></div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">链接地址：</div>
				<div class="group-detail">
					<div class="detail-row">
						<span class="row-span" v-if="isHttpUrl(materialComponent.linkUrl)">
							<a :href="materialComponent.linkUrl" target="_blank">{{ materialComponent.linkUrl }}</a>
						</span>
						<span class="row-span" v-else>{{ materialComponent.linkUrl }}</span>
					</div>
				</div>
			</div>
			<div class="column-group" v-if ="materialComponent.type == '1' ">
				<div class="group-aside">文字：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ materialComponent.textWords }}</span></div>
				</div>
			</div>
			<div class="column-group" v-if ="materialComponent.type == '2'">
				<div class="group-aside">图片：</div>
				<div class="group-detail">
					<div class="detail-row">
						<div class="row-photo-l">
							<div class="photo"><img :src="materialComponent.url"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="column-group" v-if ="materialComponent.type == '2'">
				<div class="group-aside">图片尺寸：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ materialComponent.width }}*{{ materialComponent.height }}（宽高比）</span></div>
				</div>
			</div>
		</div>
		<div class="unfold-content" v-if ="material.createFrom == 'amp'">
			<div class="column-group">
				<div class="group-aside">素材名称：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ material.name }}</span></div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">链接类型：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ material.linkType }}</span></div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">链接地址：</div>
				<div class="group-detail">
					<div class="detail-row">
						<span class="row-span" v-if="material.isHttpUrl">
							<a :href="material.landingPage" target="_blank">{{ material.landingPage }}</a>
						</span>
						<span class="row-span" v-else>{{ material.landingPage }}</span>
					</div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">广告标题：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ material.title }}</span></div>
				</div>
			</div>
			<div class="column-group">
				<div class="group-aside">广告文案：</div>
				<div class="group-detail">
					<div class="detail-row"><span class="row-span">{{ material.description }}</span></div>
				</div>
			</div>
			<template v-for="(img, index) in material.images">
				<div class="column-group">
					<div class="group-aside">图片{{ index + 1 }}：</div>
					<div class="group-detail">
						<div class="detail-row">
							<div class="row-photo-l">
								<div class="photo"><img :src="img.url"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="column-group">
					<div class="group-aside">图片{{ index + 1}}尺寸：</div>
					<div class="group-detail">
						<div class="detail-row"><span class="row-span">{{ img.width }}*{{ img.height }}（宽高比）</span></div>
					</div>
				</div>
			</template>
		</div>
	</div>
</template>
<script>
import Http from 'utils/http.js';
export default {
	name: 'material-dialog-detail',
	data() {
		return {
			material: {}
		};
	},
	props: ['materialId'],
	created() {
		this.getMaterialDetail();
	},
	methods: {
		isHttpUrl(url) {
			if (url != null) {
				return url.indexOf('http') > -1;
			}
		},
		linkTypeString(linkType) {
			return linkType === 0 ? '店铺ID' : (linkType === 1 ? '商品编号' : (linkType === 2 ? '自定义URL' : (linkType === 3 ? '圈子ID' : (linkType === 4 ? '话题ID' : (linkType === 5 ? '带分享' : (linkType === 6 ? '活动ID' : (linkType === 7 ? '旗舰店' : (linkType === 8 ? 'cms活动' : '视频'))))))));
		},
		getMaterialDetail() {
			let vm = this;
			Http.get('/api/approval/material', {
				params: {
					materialId: vm.materialId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					vm.material = res.data.data;
					if (vm.material.materialComponents != null && vm.material.materialComponents.length > 0 ) {
						vm.material.materialComponents.forEach((item) => {
						item.linkType = item.linkType === 0 ? '店铺ID' : (item.linkType === 1 ? '商品编号' : (item.linkType === 2 ? '自定义URL' : (item.linkType === 3 ? '圈子ID' : (item.linkType === 4 ? '话题ID' : (item.linkType === 5 ? '带分享' : (item.linkType === 6 ? '活动ID' : (item.linkType === 7 ? '旗舰店' : (item.linkType === 8 ? 'cms活动' : '视频'))))))));
						});
					}
					if (vm.material.createFrom === 'amp') {
						vm.material.linkType = vm.linkTypeString(vm.material.linkType);
						vm.material.isHttpUrl = vm.isHttpUrl(vm.material.landingPage);
					}
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
	margin-top: 12px;
}
</style>