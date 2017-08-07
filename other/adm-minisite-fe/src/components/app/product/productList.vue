<template>
	<m-list :type="'product'" :slotId="slotId" :isScroll="isScroll">
		<template slot="item-slot" scope="props">
			<div class="product-img" @click="light(props.item.click_url, props.item.landing_url)">
				<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
					<img class="lazy-image" v-lazy="getThumbnailsUrl(props.item.promotion_images[0], 360, 360)"/>
				</a>
			</div>
			<div class="product-info">
				<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
					<h2 class="product-title material-title">{{props.item.promotion_title}}<span v-if="props.item.rebateStatus == '1'" @click="light(props.item.click_url, props.item.landing_url)"></span></h2>
					<p class="proudct-desc" @click="light(props.item.click_url, props.item.landing_url)">{{props.item.promotion_description}}</p>
				</a>
				<div class="footer-box clearfix">
					<div class="comment clearfix" style="float: left;">
						<img src="../../../assets/images/message.png"/>
						<span v-show="props.item.commentCount > 0">{{props.item.commentCount | formatCount}}</span>
					</div>
					<m-upvote v-if="isInGome" :praise-status='props.item.praiseStatus' :id="props.item.ad_id" :parise-num='props.item.pariseNum' :request-id='props.item.requestId' :log-param="props.item.logParam" :flight-job-id="props.item.flight_job_id" :slot-id="slotId"></m-upvote>
					<m-share v-if="isInGome" :slotId="slotId" :item="props.item"></m-share>
				</div>
			</div>
		</template>
	</m-list>
</template>
<script>
import {getThumbnails} from '../../../utils/common.js';
import jsonp from 'jsonp';
import List from '../../common/list.vue';
import Share from '../../common/share.vue';
import Upvote from '../../common/upvote.vue';
export default {
	name: 'product-list',
	props: ['isScroll', 'isInGome'],
	data() {
		return {
			msg: 'hello world'
		};
	},
	computed: {
		slotId() {
			return this.isInGome ? $CONFIG['productSlotId'] : $CONFIG['wapProductSlotId'];
		}
	},
	components: {
		'm-list': List,
		'm-upvote': Upvote,
		'm-share': Share
	},
	methods: {
		light(url, landingUrl)	{
			if (!this.isInGome) return;
			jsonp(url + '&contentType=3', null, function(err, data) {
				AppInterface.call('/common/localJump', {url: window.btoa(landingUrl)});
			});
		},
		getThumbnailsUrl(imgUrl, width, height) {
			return getThumbnails(imgUrl, width, height);
		}
	}
};
</script>