<template>
	<div>
		<transition name="fade">
			<div style="z-index:1300;" class="master" v-if="showImage"></div>
		</transition>
		<transition name="fade">
			<div class="dialog-image" v-if="showImage">
				<div class="pop-header">
					<span class="header-close" @click="closeImageHandler"><em class="icon icon-close"></em></span>
				</div>
				<img :src="img">
			</div>
		</transition>
	</div>
</template>
<script>
import Event from 'event';
import Http from 'utils/http.js';
export default {
	name: 'app-approval-advertiser-dialog',
	data() {
		return {
			msg: ''
		};
	},
	props: ['showImage', 'img'],
	methods: {
		closeImageHandler() {
			Event.$emit('close-image');
		}
	}

};
</script>
<style scoped>
.dialog-image{
	position: fixed;
    left: 50%;
    top: 50%;
    z-index: 1400;
    margin-left: -200px;
    margin-top: -200px;
    width: 400px;
    height: 400px;
    background: #fff;
    border-radius: 10px;
}
.dialog-image img{
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	width: auto;
    max-height: 100%;
    max-width: 100%;
    height: auto;
    margin: auto;
    vertical-align: middle;
}
.header-close{
	width: 28px;
	height: 28px;
	border-radius: 28px;
	background: rgba(0, 0, 0, 0.4);
	color: #fff;
	text-align: center;
	line-height: 24px;
}
.pop-header{z-index: 1400}
</style>