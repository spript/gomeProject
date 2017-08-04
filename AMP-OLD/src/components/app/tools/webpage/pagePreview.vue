<template>
	<div>
		<transition name="fade">
			<div style="z-index:1300;" class="master" v-if="showPreview"></div>
		</transition>
		<transition name="fade">
			<div class="dialog-pop" v-if="showPreview">
				<div class="pop-header">
					<span class="header-close" @click="closeDialogHandler" id="header-close-position"><em class="icon icon-close"></em></span>
					<iframe :srcdoc="data.html" width="320" height="600" frameborder="0"></iframe>
				</div>
			</div>
		</transition>
	</div>
</template>
<script>
import Event from 'event';
import http from 'utils/http.js';
export default {
	name: 'app-page-preview',
	data() {
		return {
			data: {}
		};
	},
	props: ['showPreview', 'pageId', 'previewId'],
	created() {
		this.getList();
	},
	methods: {
		closeDialogHandler() {
			Event.$emit('close-preview');
		},
		getList(){
			var that = this;
			http.get("/api/page/preview", {
				params:{
					pageId: this.pageId
				}
			})
			.then(function(res){
				if(res.data.code 	== 200){
					that.data = res.data.data;
				}
			})
			.catch(function(error){
				console.log(error);
			});
		}
	}
};
</script>
<style scoped>
.dialog-pop {
	position: fixed;
	left: 50%;
	top: 0;
	z-index: 1400;
	margin-left: -280px;
	width: 360px;
	height: 640px;
	margin-top: 0px;
	background: #fff;
	border-radius: 10px;
}

#header-close-position {
	right: 6px;
	top: 3px;
}

</style>
