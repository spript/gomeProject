<template>
	<m-list :type="'item'" :slotId="slotId" :isScroll="isScroll">
		<template slot="item-slot" scope="props">
			<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
				<div @click="light(props.item.click_url, props.item.landing_url)">
					<div class="image-box">
						<img class="lazy-image" v-lazy="props.item.promotion_images[0]" alt="" />
					</div>
					<div class="inventory_scroll" id="swiperWapper">
						<swiper :options="swiperOption"  ref="mySwiper">
							<swiper-slide v-for='(item, index) in props.item.products' :key="item.index">
								<img class="lazy-image" v-lazy="getThumbnailsUrl(item.product_image, 260, 260)">
							</swiper-slide>
						</swiper>
					</div>
					<div class="inventory_title">
						<h2 class="material-title">{{props.item.promotion_title | formatTitle}}<span v-if="props.item.rebateStatus == '1'"></span></h2>
					</div>
				</div>
			</a>
		</template>
	</m-list>
</template>
<script>
import {getThumbnails} from '../../../utils/common.js';
import {swiper, swiperSlide} from 'vue-awesome-swiper';
import List from '../../common/list.vue';
import jsonp from 'jsonp';
export default {
	name: 'item-list',
	props: ['isScroll', 'isInGome'],
	data() {
		return {
			swiperOption: {
				notNextTick: true,
				pagination: '.swiper-pagination',
				slidesPerView: 3.4,
				paginationClickable: true,
				freeMode: true,
				loop: false,
				loopAdditionalSlides:3,
				onSlideChangeEnd: swiper => {
					this.page = swiper.realIndex + 1;
					this.index = swiper.realIndex;
				}
			}
		};
	},
	computed: {
		slotId() {
			return this.isInGome ? $CONFIG['itemSlotId'] : $CONFIG['wapItemSlotId'];
		}
	},
	components: {
		'm-list': List,
		swiper,
		swiperSlide
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
<style lang="less" scoped>
	.inventory_scroll .swiper-slide a{
		display: block;
		width:100%;
		height:100%;
	}
	.swiper-slide{
		width: 2rem !important;
	}
	.inventory_title{
		font-size: 0;
		text-align: center;
	}
	.inventory_title h2{
		display: inline-block;
	}
</style>