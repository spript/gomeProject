<template>
	<m-list :type="'video'" :slotId="slotId" :isScroll="isScroll">
		<template slot="item-slot" scope="props">
			<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
				<div @click="light(props.item.click_url, props.item.landing_url)">
					<div class="head-box clearfix">
						<h2 class="title material-title">{{ props.item.promotion_title | formatTitle }}<span v-if="props.item.rebateStatus == '1'"></span></h2>
					</div>
					<div class="image-box">
						<img class="videoIcon" src="../../../assets/images/shipin_icon.png"  alt="" />
						<img class="lazy-image" v-lazy="props.item.promotion_images[0]" alt=""/>
					</div>
				</div>
			</a>
		</template>
		<template slot="footer-left" scope="props">
			<div class="show clearfix" style="float: left;">
				<img class="videoShowIcon" src="../../../assets/images/video_watch.png"/>
				<p class="videoShowText">{{props.item.watchCount | formatCount}}</p>
			</div>
		</template>
	</m-list>
</template>

<script>
import List from '../../common/list.vue';
import jsonp from 'jsonp';
export default {
	name: 'video-list',
	props: ['isScroll', 'isInGome'],
	computed: {
		slotId() {
			return this.isInGome ? $CONFIG['videoSlotId'] : $CONFIG['wapVideoSlotId'];
		}
	},
	components: {
		'm-list': List
	},
	methods: {
		light(url, landingUrl, adId) {
			if (!this.isInGome) return;
			jsonp(url + '&contentType=3', null, function(err, data) {
				AppInterface.call('/common/localJump', {url: window.btoa(landingUrl)});
			});
		}
	}
};
</script>
