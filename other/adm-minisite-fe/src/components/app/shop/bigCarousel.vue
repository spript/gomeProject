<template>
	<div class="grid-big">
		<!--图片为一张图片-->
		<swiper :options="swiperOptionBigSimple"  ref="mySwiperBig" v-if="listImg.length == 1">
			<swiper-slide v-for='(item, index) in listImg' :key="item.index" style='font-size: 13px;'>
				<img class="swiper-lazy" :data-src="item"/>
				<div class="swiper-lazy-preloader swiper-lazy-preloader-black"></div>
			</swiper-slide>
			<div class="swiper-pagination" slot="pagination"></div>
		</swiper>
		<!--图片为多张图片-->
		<swiper :options="swiperOptionBig"  ref="mySwiperBig" v-if="listImg.length > 1">
			<swiper-slide v-for='(item, index) in listImg' :key="item.index" style='font-size: 13px;'>
				<img class="swiper-lazy" :data-src="item"/>
				<div class="swiper-lazy-preloader swiper-lazy-preloader-black"></div>
			</swiper-slide>
			<div class="swiper-pagination" slot="pagination"></div>
		</swiper>
		<div class="close" @click='closeBig()'>×</div>
		<div class="goSee">
			<template v-if='isInGome'>
				<a @click='light(lightArr)'>去看看</a>
			</template>
			<template v-else>
				<a :href="lightArr.clickUrl" target="_blank" @click="closeBig()">去看看</a>
			</template>
		</div>
		</div>
</template>

<script>
import {swiper, swiperSlide} from 'vue-awesome-swiper';
import Event from 'event';
import jsonp from 'jsonp';
require('../../../assets/js/appInterface.js');
export default {
	name: 'bigCarousel',
	props: ['index', 'listImg', 'listIndex', 'goLook', 'lightArr'],
	data() {
		return {
			swiperOptionBigSimple: {
				notNextTick: true,
				pagination: '.swiper-pagination',
				slidesPerView: 'auto',
				initialSlide: 0,
				paginationType : 'fraction',
				autoplayDisableOnInteraction: true,
				loop: false,
				lazyLoading: true,
			},
			swiperOptionBig: {
				notNextTick: true,
				pagination: '.swiper-pagination',
				slidesPerView: 'auto',
				centeredSlides: true,
				initialSlide: this.index,
				paginationType : 'fraction',
				autoplayDisableOnInteraction: false,
				loop: false,
				lazyLoading: true,
				onSlideChangeEnd: swiperBig => {
				}
			}
		};
	},
	components: {
		swiper,
		swiperSlide
	},
	computed: {
		isInGome() {
			return /gomeplus/g.test(navigator.userAgent);
		}
	},
	mounted() {
		document.getElementsByClassName('grid-big')[0].addEventListener('touchmove', function(e) {
			e.preventDefault();
		}, false);
	},
	methods: {
		closeBig() {
			Event.$emit('closeBig');
		},
		light(lightItem) {
			jsonp(lightItem.clickUrl + '&contentType=3', null, function(err, data) {
				AppInterface.call('/common/localJump', {url: window.btoa(lightItem.landingUrl)});
			});
		}
	}
};
</script>

<style lang="less">
	.grid-big{
		width:100%;
		height: 100%;
		background: #fff;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 999;
		.swiper-container{
			height:100%;
   			img{
				width: 100%;
				height: auto;
				max-height: 9rem;
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%,-50%);
   			}
   			.swiper-pagination{
   				top: .7rem;
   				font-size: .32rem;
   				height: .32rem;
   				line-height: .32rem;
   				color: #999;
   				span{
   					color: #999;
   				}
   			}
   			.swiper-lazy-preloader{
   				top:35%;
   			}
   		}
   		.close{
   			position: absolute;
   			top: .45rem;
   			right:.2rem;
   			z-index: 10;
   			font-size: .8rem;
   			color: #999;
   			width:.8rem;
   			height: .8rem;
   			line-height: .8rem;
   			text-align: center;
   		}
   		.goSee a{
   			position: absolute;
   			left: 50%;
   			margin-left: -1.6rem;
   			bottom: .9rem;
   			width:3.2rem;
   			height: .88rem;
   			color:#fff;
   			background: #ea5c5e;
   			text-align: center;
   			line-height: .88rem;
   			border-radius: .44rem;
   			font-size: .32rem;
   			z-index: 10;
   		}
	}
</style>
