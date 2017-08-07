<template>
	<m-list :type="'topic'" :slotId="slotId" :isScroll="isScroll">
		<template slot="item-slot" scope="props">
			<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
				<div @click="light(props.item.click_url, props.item.landing_url)">
					<div class="head-box clearfix">
						<h2 class="title material-title" :style="{'margin-bottom': props.item.promotion_description.length === 0 ? 0 : '.12rem'}">{{props.item.promotion_title | formatTitle}}<span v-if="props.item.rebateStatus == '1'"></span></h2>
						<p class="material-desc" v-show="props.item.promotion_description.length > 0">{{ props.item.promotion_description }}</p>
					</div>
					<div class="image-box">
						<img class="lazy-image" v-lazy="props.item.promotion_images[0]"/>
					</div>
				</div>
			</a>
		</template>
	</m-list>
</template>
<script>
import jsonp from 'jsonp';
import List from '../../common/list.vue';
export default {
	name: 'topic-list',
	props: ['isScroll', 'isInGome'],
	computed: {
		slotId() {
			return this.isInGome ? $CONFIG['topicSlotId'] : $CONFIG['wapTopicSlotId'];
		}
	},
	components: {
		'm-list': List,
	},
	methods: {
		light(url, landingUrl)	{
			if (!this.isInGome) return;
			jsonp(url + '&contentType=3', null, function(err, data) {
				AppInterface.call('/common/localJump', {url: window.btoa(landingUrl)});
			});
		}
	}
};
</script>
