
(function(win) {
	var H5 = {
		images: [
			'img/loading-bg.jpg',
			'img/loading-cloud.png',
			'img/loading-finish.png',
			'img/loading-text.png',
			'img/loading-wk1.png',
			'img/loading-wk2.png',
			'img/loading-wk3.png',
			'img/item-back.png',
			'img/beef.png',
			'img/blower.png',
			'img/car.png',
			'img/dis-bottom.png',
			'img/download-btn.png',
			'img/download-btn.png',
			'img/find-bg.jpg',
			'img/find-dis-text.png',
			'img/find-img.png',
			'img/find-title.png',
			'img/flower-after.png',
			'img/flower.png',
			'img/fourPeopleAll.png',
			'img/meal.png',
			'img/pants.png',
			'img/person-eight.png',
			'img/person-five.png',
			'img/person-four.png',
			'img/person-nine.png',
			'img/person-one.png',
			'img/person-seven.png',
			'img/person-six.png',
			'img/person-three.png',
			'img/person-two.png',
			'img/pill.png',
			'img/sha.png',
			'img/shampoo.png',
			'img/share-arrow.png',
			'img/share-back.png',
			'img/share-btn.png',
			'img/share-img.png',
			'img/share-toast.png',
			'img/sun.png',
			'img/shoes.png',
			'img/text-eight.png',
			'img/text-five.png',
			'img/text-four.png',
			'img/text-nine.png',
			'img/text-one.png',
			'img/text-seven.png',
			'img/text-six.png',
			'img/text-three.png',
			'img/text-two.png',
			'img/water.png'
		],
		loaded: 0,
		imageLength: 0,
		loadingElement: $('.progress'),
		loadingWK: $('.loading-wk'),
		isWeiXin: /MicroMessenger/g.test(navigator.userAgent),
		isQQ: /QQ/g.test(navigator.userAgent),
		isWeiBo: /Weibo/g.test(navigator.userAgent),
		init: function() {
			H5.imageLength = H5.images.length;
			H5.imageLoad();
		},
		imageLoad: function() {
			if (H5.imageLength > 0) {
				var image = document.createElement('img');
				image.src = H5.images[H5.loaded];
				image.onload = function() {
					H5.loaded++;
					H5.loadingElement.css('width', Math.ceil((H5.loaded / H5.imageLength) * 100) + '%');
					H5.loadingWK.css('left', Math.ceil((H5.loaded / H5.imageLength) * 100) + '%');
					if (H5.loaded < H5.imageLength) {
						H5.imageLoad();
					} else {
						H5.loadingWK.hide();
						$('.loading-cloud').addClass('none');
						$('.loading-finish').show();
						$('.loading-progress').css('width', '6.7rem');
						setTimeout(function() {
							$('.loading').addClass('none');
							Event.init();
						}, 1500);
					}
				}
			} else {
				// $('.loading-finish').show();
				$('.loading').hide();
				Event.init();
			}
		}
	};
	var Event = {
		init: function() {
			$('.find').removeClass('none');
			Event.afterLoading();
			Event.listItemClickHandler();
			Event.btnClickHandler();
			Event.close();
		},
		afterLoading: function() {
			var time;
			time = setTimeout(function(){
				$(".find").addClass("none");
				$(".productList").removeClass("none");
				$(".productList").addClass("fadeIn");
			}, 4000)
		},
		listItemClickHandler: function() {
			$(".productList .content li").on('tap', function(event){
				console.log(event);
				$(".product-item").removeClass("none");
				$(".product-item").addClass("show");
				var index = $(this).index();
				$(".product-item .item-con").removeClass("show");
				$(".product-item .item"+index+"").addClass('show');
				_hmt.push(['_trackEvent', 'gome_h5_item', 'click', index]);
			});
		},
		closeBtnHandler: function(targetEle) {
			$('.close-btn').on('tap', function(e) {
				targetEle.addClass('none');
				$(this).off('tap');
			});
		},
		btnClickHandler: function() {
			$('.find-img').on('tap', function() {
				$('.find-discover').removeClass('none');
				Event.closeBtnHandler($('.find-discover'));
				_hmt.push(['_trackEvent', 'gome_h5_mm', 'click']);
			});
			$('.share-img, .share-btn').on('tap', function() {
				if (H5.isWeiXin || H5.isQQ || H5.isWeiBo) {
					$('.share-guide').removeClass('none').on('tap', function() {
						$(this).addClass('none').off('tap');
						$('.share-arrow').addClass('none');

					});
					$('.share-arrow').removeClass('none');
				} else {
					if ($('.find-discover').hasClass('none')) {
						$('.share-guide').removeClass('none');
						$('.share-toast').removeClass('none');
						setTimeout(function() {
							$('.share-toast').addClass('none');
							$('.share-guide').addClass('none')
						}, 3000);
					} else {
						$('.share-toast').removeClass('none');
						setTimeout(function() {
							$('.share-toast').addClass('none');
						}, 3000);
					}
				}
				if ($(this).hasClass('share-img')) {
					_hmt.push(['_trackEvent', 'gome_h5_share', 'click', 'outer']);
				} else {
					_hmt.push(['_trackEvent', 'gome_h5_share', 'click', 'inner']);
				}
			})
		},
		close: function() {
			$(".item-close-btn").on('tap', function(){
				$(".product-item").removeClass("show");
				$(".product-item").addClass("none");
			})
		}
	}
	H5.init();
})(window);

