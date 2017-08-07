<template>
	<div style="height: 100%;">
		<m-list :type="'shop'" :slotId="slotId" :isScroll="isScroll">
			<template slot="item-slot" scope="props">
				<a :href="isInGome ? 'javascript:void(0)' : props.item.click_url" target="_blank">
					<div class="goodShop-header clearfix" @click='light(props.item.click_url, props.item.landing_url)'>
						<div class="shop-image-box">
							<img class="head" v-lazy="props.item.shop_logo" @error="error($event)"/>
						</div>
						<div class="goodShop-title">
							<div class="goodShop-title-share clearfix">
								<h2 class="material-title">{{props.item.shop_name | formatTitle}}<span v-if="props.item.rebateStatus == '1'"></span></h2>
							</div>
							<h3>{{props.item.promotion_description}}</h3>
						</div>
					</div>
				</a>
				<!--显示不同的格式的分割线-->
				<div class="goodShop-content-small" v-if='props.item.promotion_images.length == 1'>
					<img class="lazy-image" v-lazy="getThumbnailsUrl(props.item.promotion_images[0], 360, 360)" @click='showBig(1, props.listItemIndex,  props.item);'/>
				</div>
				<!--图片大于一张后-->
				<div class="goodShop-content-big" v-if='props.item.promotion_images.length > 1'>
					<ul class="clearfix gridImg">
						<li v-for='(item, index2) in props.item.promotion_images'>
							<a href="javascript:;" @click='showBig(index2, props.listItemIndex, props.item);'>
								<img class="lazy-image" v-lazy="getThumbnailsUrl(item, 260, 260)">
							</a>
						</li>
					</ul>
				</div>
			</template>
			<template slot="footer-left" scope="props">
				<div class="collect clearfix" style="float: left;margin-top: .1rem;display:none;">
					<img src="../../../assets/images/star.png"/>
					<p>{{props.item.promotion_collection_num | formatCount}}</p>
				</div>
			</template>
		</m-list>
		<m-BigCarousel v-if='bigShow' :index='index' :listImg='listImg' :listIndex='listIndex' :goLook='goLook' :lightArr='lightArr'></m-BigCarousel>
	</div>
</template>
<script>
import Event from 'event';
import jsonp from 'jsonp';
import List from '../../common/list.vue';
import BigCarousel from './bigCarousel.vue';
import {getThumbnails} from '../../../utils/common.js';
import Url from '../../../assets/images/goodshop-head.jpg';//图片显示错误的时候应该显示的图片。
export default {
	name: 'shop-list',
	props: ['isScroll', 'isInGome'],
	data() {
		return {
			bigShow: false,
			index: '',
			listImg: [],
			listIndex: '',
			goLook: [],
			lightArr: {clickUrl: '', landingUrl: ''},
			Url: Url
		};
	},
	computed: {
		slotId() {
			return this.isInGome ? $CONFIG['shopSlotId'] : $CONFIG['wapShopSlotId'];
		}
	},
	components: {
		'm-list': List,
		'm-BigCarousel': BigCarousel
	},
	mounted() {
		var vm = this;
		Event.$on('closeBig', function() {
			vm.bigShow = false;
		});
	},
	methods: {
		error(event) {
			event.currentTarget.setAttribute('src', this.Url);
		},
		light(url, landingUrl)	{
			if (!this.isInGome) return;
			jsonp(url + '&contentType=3', null, function(err, data) {
				AppInterface.call('/common/localJump', {url: window.btoa(landingUrl)});
			});
		},
		getThumbnailsUrl(imgUrl, width, height) {
			return getThumbnails(imgUrl, width, height);
		},
		showBig(index1, index2, item) {
			this.listImg = item.promotion_images;
			this.lightArr.clickUrl = item.click_url;
			this.lightArr.landingUrl = item.landing_url;
			this.index = index1;
			this.listIndex = index2;
			this.bigShow = true;
		}
	}
};
</script>
