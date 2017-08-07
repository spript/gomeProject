<template>
	<div style="height: 100%;" id="menu">
		<div class="mint-navbar" :class="{'scroll': isScroll}">
			<a :class="{'mint-tab-item': true, 'router-link-active': selectIndex === 0}" @click.prevent="selectIndex = 0">
				<div class="mint-tab-item-icon"></div>
				<div class="mint-tab-item-label">发现</div>
			</a>
			<a :class="{'mint-tab-item': true, 'router-link-active': selectIndex === 1}" @click.prevent="selectIndex = 1">
				<div class="mint-tab-item-icon"></div>
				<div class="mint-tab-item-label">好店</div>
			</a>
			<a :class="{'mint-tab-item': true, 'router-link-active': selectIndex === 2}" @click.prevent="selectIndex = 2">
				<div class="mint-tab-item-icon"></div>
				<div class="mint-tab-item-label">清单</div>
			</a>
			<a :class="{'mint-tab-item': true, 'router-link-active': selectIndex === 3}" @click.prevent="selectIndex = 3">
				<div class="mint-tab-item-icon"></div>
				<div class="mint-tab-item-label">视频</div>
			</a>
			<a :class="{'mint-tab-item': true, 'router-link-active': selectIndex === 4}" @click.prevent="selectIndex = 4">
				<div class="mint-tab-item-icon"></div>
				<div class="mint-tab-item-label">好物</div>
			</a>
		</div>
		<keep-alive>
			<m-topic v-if="selectIndex === 0" :isScroll="isScroll" :isInGome="isInGome"></m-topic>
			<m-shop  v-if="selectIndex === 1" :isScroll="isScroll" :isInGome="isInGome"></m-shop>
			<m-item  v-if="selectIndex === 2" :isScroll="isScroll" :isInGome="isInGome"></m-item>
			<m-video v-if="selectIndex === 3" :isScroll="isScroll" :isInGome="isInGome"></m-video>
			<m-product v-if="selectIndex === 4" :isScroll="isScroll" :isInGome="isInGome"></m-product>
		</keep-alive>
		<m-common-share></m-common-share>
		<div class="missionEnter" v-show="isShowMission" >
			<a @click="light">
				<img src="../../assets/images/missionEnter.png">
			</a>
		</div>
	</div>
</template>

<script>
import actions from 'actions';
import store from 'store';
import jsonp from 'jsonp';
import http from 'utils/http';
import Topic from './topic/topicList.vue';
import Shop from './shop/shopList.vue';
import Item from './item/itemList.vue';
import Video from './video/videoList.vue';
import Product from './product/productList.vue';
import CommonShare from '../common/h5Share.vue';
require('../../assets/js/appInterface.js');
export default {
	name: 'main',
	data() {
		return {
			selectIndex: 0,
			isScroll: false,
			isShowMission: false
		};
	},
	computed: {
		isInGome() {
			return /gomeplus/g.test(navigator.userAgent);
		},
		userId: () => store.state.userId
	},
	components: {
		'm-topic': Topic,
		'm-shop': Shop,
		'm-item': Item,
		'm-video': Video,
		'm-product': Product,
		'm-common-share': CommonShare
	},
	mounted() {
		let that = this;
		document.getElementsByClassName('mint-navbar')[0].addEventListener('touchmove', function(e) {
			e.preventDefault();
		}, false);
		let startY = 0;
		window.addEventListener('touchstart', (e) => {
			startY = e.changedTouches[0].pageY;
		});
		window.addEventListener('touchmove', (e) => {
			that.isScroll = true;
			let moveEndY = e.changedTouches[0].pageY;
			let Y = moveEndY - startY;
			if (Y < 0) {
				that.isScroll = true;
			} else {
				that.isScroll = false;
			}
		});
		if (that.isInGome) {
			AppInterface.call('/common/getLoginStatus', function(data) {
				if (data.success) {
					actions.setUserId(that.$store, data.data.userId);
					// that.getMissionEntry();
				}
			});
		}
	},
	methods: {
		light()	{
			AppInterface.call('/common/localJump', {url: window.btoa($CONFIG['missionUrl'])});
		},
		getMissionEntry() {
			let vm = this;
			let url = $CONFIG['missionDoMain'] + 'missionCount?uid=' + this.userId ;
			jsonp(url, null, function(err, data) {
				if (data.message == 'success') {
					vm.isShowMission = true;
				} else {
					vm.isShowMission = false;
				}
			 });
		}
	}
};

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less">
	.missionEnter{
		position: fixed;
		right:.4rem;
		bottom:2.5rem;
		width: .8rem;
		height: .8rem;
		a,img{
			width: .8rem;
			height: .8rem;
			display: block;
		}
	}
	#menu .scroll{ height:.9rem;}
	#menu .scroll a{ padding: 0;}
	#menu .scroll .mint-tab-item .mint-tab-item-icon{ transition: all 1s;width:0rem; height:0rem;}
	#menu .scroll .mint-tab-item-label{
		line-height: 0.9rem;
		padding: 0 .3rem;
		font-size: .32rem;
	}
	#menu .scroll .mint-tab-item.router-link-active{
		background-image: url(../../assets/images/red_selected_pullup.png);
		background-repeat: no-repeat;
		background-position: center bottom;
		.mint-tab-item-label{
			color: #ea5c5e;
		}
	}
	#menu .scroll .mint-tab-item  .mint-tab-item-label{
		background-image: none;
	}
	.tab {
		margin: 0 auto;
	}
  .mint-navbar {
	width:100%;
	z-index:7;
	position:fixed;
	top:0;
    background-color: #fff;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    box-sizing: border-box;
    -moz-box-sizing:border-box;
    -webkit-box-sizing:border-box;
    text-align: center;
    height: 1.2rem;
    border-bottom: 1px solid #ddd;
    overflow: hidden;
    /*padding: 0 .22rem;*/
    .mint-tab-item-label {
      font-size: 0.28rem;
      width: .66rem;
      margin: 0 auto;
      line-height: 0.5rem;
      color: #333;
    }
    .mint-tab-item {
      display: block;
      -webkit-box-flex: 1;
      -ms-flex: 1;
      flex: 1;
      text-decoration: none;
		padding-top: .19rem;
		padding-bottom: .16rem;
      &.router-link-active{
         .mint-tab-item-label{
            border-radius: 0.015rem;
         }
      }
      &.router-link-active{
	       .mint-tab-item-label{
            color: #ea5c5e;
			background-image: url(../../assets/images/red_selected.png);
			background-repeat: no-repeat;
			background-position: center bottom;
           }
      }
      .mint-tab-item-icon {
        width: .5rem;
        height: .5rem;
        display: block;
        background-size: 100% 100%;
        margin: 0 auto;
      }
      &:nth-child(1) .mint-tab-item-icon {
        background-image: url(../../assets/images/selected_tab_pic.png);
      }
      &:nth-child(2) .mint-tab-item-icon {
        background-image: url(../../assets/images/goodShop_tab_pic.png);
      }
      &:nth-child(3) .mint-tab-item-icon {
        background-image: url(../../assets/images/inventory_tab_pic.png);
      }
      &:nth-child(4) .mint-tab-item-icon {
        background-image: url(../../assets/images/video_tab_pic.png);
      }
      &:nth-child(5) .mint-tab-item-icon {
        background-image: url(../../assets/images/product_tab_pic.png);
      }
    }
  }
</style>
