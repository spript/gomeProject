$(function() {
	var utils = {
        isGome: /gomeplus/g.test(navigator.userAgent),
        isWeiXin: /MicroMessenger/g.test(navigator.userAgent),
        isQQ: /QQ/g.test(navigator.userAgent),
        isWeibo: /Weibo/g.test(navigator.userAgent),
        isIos: /iPhone/g.test(navigator.userAgent),
        isAndroid: /Android/g.test(navigator.userAgent),
        userId: '',
        getDeviceIdFromAgent: function() {
            var deviceId = '';
            var agentArr = navigator.userAgent.split('/');
            if (agentArr.length > 0) {
                if (utils.isIos) {
                    deviceId = agentArr[agentArr.length - 6];
                } else if (utils.isAndroid) {
                    deviceId = agentArr[agentArr.length - 7];
                } else {
                    deviceId = agentArr[agentArr.length -1];
                }
            }
            return deviceId;
        },
        commonParams: function() {
            var url = window.location;
            var result = {};
            var reg = new RegExp('([\\?|&])(.+?)=([^&?]*)', 'ig');
            var arr = reg.exec(url);
            while (arr) {
                result[arr[2]] = arr[3];
                arr = reg.exec(url);
            }
            return result;
        },
        getKeyValue: function(url) {
            var result = {};
            var reg = new RegExp('([\\?|&])(.+?)=([^&?]*)', 'ig');
            var arr = reg.exec(url);
            while (arr) {
                result[arr[2]] = arr[3];
                arr = reg.exec(url);
            }
            return result;
        },
        getDistrictIdByUrl: function(url) {
            var urlParam = this.getKeyValue(decodeURIComponent(url));
            var cityJson = urlParam.cityjson ? JSON.parse(urlParam.cityjson) : {};
            return cityJson.districtId ? cityJson.districtId : '';
        },
        urlEncode: function(param, key, encode) {
            if(param==null) return '';
            var paramStr = '';
            var t = typeof (param);
            if (t == 'string' || t == 'number' || t == 'boolean') {
                paramStr += '&' + key + '=' + ((encode==null||encode) ? encodeURIComponent(param) : param);
            } else {
                for (var i in param) {
                    var k = key == null ? i : key + (param instanceof Array ? '[' + i + ']' : '.' + i);
                    paramStr += utils.urlEncode(param[i], k, encode);
                }
            }
            return paramStr;
        },
        isNewAppVersion: function() {
            var reg = /\/\d{2}\//g;
            var appVerMatch = navigator.userAgent.match(reg);
            if (appVerMatch != null && appVerMatch.length === 1) {
                var appVersion = appVerMatch[0];
                appVersion = appVersion.slice(1, appVersion.length - 1);
                return appVersion >= 35;
            }
            return false;
        },
        getAppVersion: function() {
            var reg = /\/\d{2}\//g;
            var appVerMatch = navigator.userAgent.match(reg);
            if (appVerMatch != null && appVerMatch.length === 1) {
                var appVersion = appVerMatch[0];
                appVersion = appVersion.slice(1, appVersion.length - 1);
                return appVersion;
            }
            return 0;
        },
        formatCurrency: function(currency) {
            var result = parseInt(currency, 10) * 0.01;
            return result.toFixed(2);
        },
        encrypt: function(word){
            var key = CryptoJS.enc.Utf8.parse("abcdefgabcdefg12");
            var srcs = CryptoJS.enc.Utf8.parse(word);
            var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
            return encrypted.toString();
        },
        decrypt: function(word){
            var key = CryptoJS.enc.Utf8.parse("abcdefgabcdefg12");
            var decrypt = CryptoJS.AES.decrypt(decodeURIComponent(word), key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
            return CryptoJS.enc.Utf8.stringify(decrypt).toString();
        }
    };
    var commonHandler = {
        getUserId: function() {
            if (utils.isGome) {
                AppInterface.call('/common/getLoginStatus', function(data) {
                    if (data.success) {
                        utils.userId = data.data.userId;
                        commonParise.getPariseInfo();
                        // 曝光时因为需要拼接userId
                        commonShare.doShareImpressionAction();
                    } else {
                        video.initLoginStyle();
                    }
                    commonRebate.init();
                });
            } else {
                $('.m-downapp').show();
                $('.share-container').hide();
                $('.bottom_tools').hide();
                $('.detail-title').css('margin-bottom', '0.1rem');
                $('.detail_footer p').css('padding-bottom', '0');
                commonShare.doShareImpressionAction();
            }
        },
        getUncryptParams: function() {
            var params = utils.commonParams();
            if (params['p'] && params['q']) {
                var decryptP = utils.decrypt(params['p']);
                var decryptQ = utils.decrypt(params['q']);
                var paramsP = utils.getKeyValue('?' + decryptP);
                var paramsQ = decryptQ.split('=')[1];
                params = paramsP;
                params['logParam'] = paramsQ;
            }
            return params;
        }
    };
    var commonShare = {
        shareOptions: {
            title: $CONFIG['cardTitle'].replace(/[<>@# {}\[\]^`\\%~|]/g, ''),
            desc: $CONFIG['cardDescription'].replace(/[<>@# {}\[\]^`\\%~|]/g, ''),
            imgUrl: /.jpg$|.png$/g.test($CONFIG['cardImage']) ? window.btoa($CONFIG['cardImage']) : window.btoa($CONFIG['cardImage'] + '.jpg'),
            link: '',
            type: 'default',
            btnType: '2,10,11,12'
        },
        shareBtn: $(".toolsItem[data-type='share']"),
        questionBtn: $(".toolsItem[data-type='question']"),
        init: function() {
            //分享曝光 移动到获取userId成功后
            // commonShare.doShareImpressionAction();
            commonShare.shareBtn.on('click', function() {
                if (utils.isGome) {
                    if (utils.isNewAppVersion()) {
                        commonShare.shareInGome('default');
                    } else {
                        $('.common-share-tips').show(200);
                        $('.share-container').addClass('slide-in');
                    }
                } else {
                    commonShare.shareInWarp();
                }
            });
            $('.common-share-tips, .share-cancel').on('click', function() {
                $('.common-share-tips').hide();
                $('.share-container').removeClass('slide-in');
            });
            $('.share-item').on('click', function() {
                var shareType = $(this).attr('data-type');
                commonShare.shareInGome(shareType);
            });
        },
        shareInGome: function(type) {
            if (utils.userId !== '') {
                commonShare.doShareAction(type);
            } else {
                AppInterface.call('/common/logout', function(data) {
                    if (data.success) {
                        AppInterface.call('/common/login', function(data) {
                            if (data.success) {
                                console.log("share login success" + data.data);
                                utils.userId = data.data.userId;
                                commonShare.doShareAction(type);
                            }
                        });
                    }
                });
            }
        },
        doShareAction: function(type) {
            // commonShare.shareOptions.link = (window.location.href).split('?')[0];
            var doShareOptions = commonHandler.getUncryptParams();
            var appShareOptions = commonShare.shareOptions;
            appShareOptions.type = type;
            var date = new Date().getTime();
            if (!!doShareOptions['relationId']) {
                var relationIdArr = decodeURIComponent(doShareOptions['relationId']).split(',');
                var level = Number(relationIdArr[relationIdArr.length - 1]) + 1;
                if (relationIdArr.length === 3) {
                    doShareOptions['relationId'] = relationIdArr[1] + ',' + utils.userId + ',' + level;
                } else {
                    doShareOptions['relationId'] = relationIdArr[0] + ',' + utils.userId + ',' + level;
                }
            } else {
                doShareOptions['relationId'] = utils.userId + ',' + 0;
            }
            if (!doShareOptions['uniqueId']) {
                doShareOptions['uniqueId'] = utils.userId + '_' + doShareOptions['adId'] + '_' + date;
            }
            doShareOptions['slotId'] = $CONFIG['slotId'];
            doShareOptions['deviceId'] = utils.getDeviceIdFromAgent();
            doShareOptions['shareId'] = md5(date + utils.userId);
            doShareOptions['slotType'] = 1;
            doShareOptions['shareTime'] = date;
            var logParam = doShareOptions['logParam'];
            delete doShareOptions['logParam'];
            var cryptParam = {
                p: utils.encrypt(utils.urlEncode(doShareOptions).slice(1)),
                q: utils.encrypt('logParam=' + logParam)
            }
            var sUrl = (window.location.href).split('?')[0] + '?' + utils.urlEncode(cryptParam).slice(1);
            console.log("sUrl is" + sUrl);

            commonShare.share(cryptParam); //分享上报
            $.ajax({
                 url: 'https://re.gomeplus.com/short/get',
                 type: 'post',
                 dataType: 'text',
                 data: sUrl,
                 success: function(res) {
                    appShareOptions.link = window.btoa(res);
                    AppInterface.call('/common/share', appShareOptions);
                 }
            });
        },
        share: function(params) {
            $.ajax({
                url: $CONFIG['discoveryShare'],
                type: 'get',
                data: params,
                dataType: 'jsonp',
                success: function(result) {}
            })
        },
        shareInWarp: function() {
            if (utils.isWeiXin || utils.isQQ || utils.isWeibo) {
                commonShare.showWrapShareMessage('请点击右上角使用自带分享功能分享');
            } else {
                commonShare.showWrapShareMessage('请用浏览器自带分享功能分享');
            }
        },
        showWrapShareMessage: function(msg) {
            var $div = $('<div class="alert_second" id="alertSecond"></div>');
            $div.text(msg);
            $("body").append($div);
            setTimeout(function() {
                $div.remove();
            }, 1500);
        },
        doShareImpressionAction: function() {
            var params = commonHandler.getUncryptParams();
            if (params['shareId'] !== undefined) {
                params['flag'] = utils.isGome ? 1 : 0;
                if (utils.isGome) {
                    var relationIdArr = decodeURIComponent(params['relationId']).split(',');
                    var level = Number(relationIdArr[relationIdArr.length - 1]);
                    if (relationIdArr.length === 3) {
                        params['relationId'] = relationIdArr[0] + ',' + relationIdArr[1] + ',' + utils.userId + ',' + level;
                    } else {
                        params['relationId'] = relationIdArr[0] + ',' + utils.userId + ',' + level;
                    }
                }
                var paramQ = utils.encrypt('logParam=' + params['logParam']);
                delete params['logParam'];
                var shareRebateParams = {
                    p: utils.encrypt(utils.urlEncode(params).slice(1)),
                    q: paramQ
                };
                $.ajax({
                    url: $CONFIG['shareRebateUrl'],
                    type: 'get',
                    data: shareRebateParams,
                    dataType: 'jsonp',
                    success: function(result) {}
                });
            }
        }
    };
    var commonParise = {
        pariseBtn: $(".toolsItem[data-type='parise']"),
        isParised: false,
        init: function() {
            if (utils.isGome) {
                commonParise.pariseBtn.on('click', function() {
                    if (utils.userId !== '') {
                        commonParise.pariseAction();
                    } else {
                        AppInterface.call('/common/logout', function(data) {
                            if (data.success) {
                                AppInterface.call('/common/login', function(data) {
                                    if (data.success) {
                                        utils.userId = data.data.userId;
                                        commonParise.pariseAction();
                                    }
                                });
                            }
                        });
                    }
                });

                // commonParise.pariseInfoAction();
            }
        },
        getPariseInfo: function() {
            var params = commonHandler.getUncryptParams();
            $.ajax({
                url: $CONFIG['pariseInfoUrl'] + '?userId=' + utils.userId + '&adId=' + params['adId'] + '&slotId=' + $CONFIG['slotId'],
                type: 'get',
                dataType: 'jsonp',
                success: function(result) {
                    if (result.parise_status == 1) {
                        // commonParise.pariseBtn.text('已赞');
                        commonParise.isParised = true;
                        $(".toolsItem[data-type='parise']").find('.tool_icon').addClass('parised').text('已赞');
                    }
                }
            });
        },
        pariseAction: function() {
            if (commonParise.isParised) {
                return;
            }
            var params = commonHandler.getUncryptParams();
            params['slotId'] = $CONFIG['slotId'];
            params['userId'] = utils.userId;
            var logParam = params['logParam'];
            delete params['logParam'];
            var cryptParam = {
                p: utils.encrypt(utils.urlEncode(params).slice(1)),
                q: utils.encrypt('logParam=' + logParam)
            }
            $.ajax({
                url: $CONFIG['pariseUrl'],
                type: 'get',
                data: cryptParam,
                dataType: 'jsonp',
                success: function(result) {
                    if (result.parise_status == 1) {
                        // commonParise.pariseBtn.text('已赞');
                        commonParise.isParised = true;
                        $(".toolsItem[data-type='parise']").find('.tool_icon').addClass('parised').text('已赞');
                    }
                }
            });
        },
        pariseInfoAction: function() {
            if (utils.userId == '') {
                return;
            }
            var params = commonHandler.getUncryptParams()
            var pariseInfoParams = {};
            pariseInfoParams['userId'] = utils.userId;
            pariseInfoParams['adId'] = params['adId'];
            pariseInfoParams['slotId'] = $CONFIG['slotId'];
            $.ajax({
                url: $CONFIG['pariseInfoUrl'],
                type: 'get',
                data: pariseInfoParams,
                dataType: 'jsonp',
                success: function(result) {
                    if (result.parise_status == 1) {
                        commonParise.isParised = true;
                        $(".toolsItem[data-type='parise']").find('.tool_icon').addClass('parised').text('已赞');
                    }
                }
            });
        },
    };
    var middlePage = {
        init: function() {
            var params = commonHandler.getUncryptParams();
            params['slotType'] = 1;
            if (!!params['shareId']) {
                params['sourceType'] = 2;
            } else {
                params['sourceType'] = 1;
            }
            params['slotId'] = !!params['slotId'] ? params['slotId'] : (utils.isGome ? $CONFIG['slotId'] : $CONFIG['wapSlotId']);
            $.ajax({
                url: $CONFIG['middlePageImpressionUrl'],
                type: 'get',
                dataType: 'jsonp',
                data: params,
                success: function() {}
            });
            if (!utils.isGome) {
                $('.m-downapp').show();
                $('.share-container').hide();
                $('.bottom_tools').hide();
                $('.detail-title').css('margin-bottom', '0.1rem');
                $('.detail_footer p').css('padding-bottom', '0');
            }
        }
    };
    var commonRebate = {
        isWatchRebate: false,
        init: function() {
            if (!utils.isGome) {
                return;
            }
            var param = commonHandler.getUncryptParams();

            if (param['relationId']) {
                var relation = decodeURIComponent(param['relationId']).split(',');
                var level = Number(relation[relation.length - 1]);
                if (level >= 1) {
                    return;
                }
                if (relation.length == 2 && relation[0] == utils.userId) {
                    return;
                }
            }

            var flightJobId = param['flightJobId'];
            param['slotId'] = $CONFIG['slotId'];
            $.ajax({
                url: $CONFIG['rebateBudgetUrl'],
                data: param,
                type: 'GET',
                dataType: 'jsonp',
                success: function(result) {
                    var status = result['data']['status'];
                    if (status != 1) {
                        return;
                    }
                    // 分享返利价格
                    var shareRebateBid = parseInt(result['data']['rebatePrice']['shareRebateBid']);
                    // 调研问卷返利价格
                    var researchRebateBid = parseInt(result['data']['rebatePrice']['researchRebateBid']);
                    // 视频返利价格
                    var watchRebateBid = parseInt(result['data']['rebatePrice']['watchRebateBid']);
                    // 有返利时显示返利标签
                    if (shareRebateBid > 0 || researchRebateBid > 0 || watchRebateBid > 0) {
                        $(".share_tips").show();
                        $('.share_tips .rebate-btn').on('click', function() {
                            $('.common-share-tips').css('display', 'block');
                            $('.rebate-rule').css('opacity', 1).addClass('slide-in');
                            $('body').css('overflow', 'hidden');
                        });
                        $('.common-share-tips').on('click', function() {
                            $('.common-share-tips').css('display', 'none');
                            $('.rebate-rule').removeClass('slide-in').css('opacity', 0);
                            $('body').css('overflow', 'auto');
                        });
                    }
                    if (shareRebateBid > 0) {
                        $("#J_shareRebat").text("分享赚" + utils.formatCurrency(shareRebateBid) + "元");
                        $("#J_rebateBudget").text("成功得￥" + utils.formatCurrency(shareRebateBid));
                        $('.rebate-rule .share-rebate').show();
                    } else {
                        $("#J_shareRebat").hide();
                        $(".rebateBudget").hide();
                        $('.rebate-rule .share-rebate').hide();
                    }
                    if (researchRebateBid > 0) {
                        commonQuestion.init();
                        $("#J_researchRebate").text("调研赚" + utils.formatCurrency(researchRebateBid) + "元");
                        $('.rebate-rule .survey-rebate').show();
                    } else {
                        $("#J_researchRebate").hide();
                        $('.rebate-rule .survey-rebate').hide();
                    }
                    if (watchRebateBid > 0) {
                        commonRebate.isWatchRebate = true;
                        $("#J_watchRebate").text("视频赚" + utils.formatCurrency(watchRebateBid) + "元");
                        $('.rebate-rule .video-rebate').show();
                    } else {
                        commonRebate.isWatchRebate = false;
                        $("#J_watchRebate").hide();
                        $('.video-login-box').hide();
                        $('.rebate-rule .video-rebate').hide();
                    }
                    // commonHandler.getUserId();
                }
            });
        }
    };

    var commonPrice = {
        productIdList: [],
        districtId: '',
        getAreaCode: function() {
            commonPrice.districtId = utils.getDistrictIdByUrl(window.location);
        },
        initProductConList: function() {
            var items = $('.item-price');
            items.forEach(function(item, index, array) {
                var itemDataId = item.getAttribute('data-id');
                commonPrice.productIdList.push(itemDataId);
            });
        },
        getProductsPrice: function() {
            if (commonPrice.productIdList.length == 0) {
                return;
            }
            var priceUrl = "/api/prices" + '?ids=' + commonPrice.productIdList.join(',') + '&areaCode=' + commonPrice.districtId;
            $.ajax({
                url: priceUrl,
                type: 'GET',
                dataType: 'jsonp',
                success: function(result) {
                    var priceArr = result['data'];
                    if (priceArr == null) {
                        return;
                    }
                    priceArr.forEach(function(item, index, array) {
                        $(".price_"+item.skuId).html('<span>￥' + item.price + '</span>');
                    });
                }
            });
        },
        init: function() {
            commonPrice.getAreaCode();
            commonPrice.initProductConList();
            commonPrice.getProductsPrice();
        }
    };

    var commonShop = {
        init: function() {
            var shopId = $CONFIG['shopId'];
            if (shopId == undefined) {
                return;
            }
            var shopUrl = "/api/shop" + '?id=' + shopId;
            $.ajax({
                url: shopUrl,
                type: 'GET',
                dataType: 'jsonp',
                success: function(result) {
                    var data = result['data'];
                    $("#J_shopCollection_" + shopId).text(data['collectionQuantity']);
                }
            });
        }
    };
    var commonQuestion = {
        userId : 0,
        appearFlag : 0,
        isAnswer : 0,
        hasGetIsAnswer : false,

        // 根据计划状态判断是否显示问卷入口
        init: function() {
            // commonQuestion.getQuestionInfo('init');
            commonQuestion.questionHandler('init');
        },
        questionHandler: function(type) {
            var params = commonHandler.getUncryptParams();
            var questionData;
            if (type === 'init') {
                questionData = {
                    logParam: params['logParam'],
                    flightJobId: params['flightJobId'],
                    slotId: $CONFIG['slotId']
                };
                commonQuestion.getQuestionInfo('init', questionData);
            } else if (type === 'click') {
                questionData = {
                    userId: utils.userId,
                    logParam: params['logParam'],
                    flightJobId: params['flightJobId'],
                    slotId: $CONFIG['slotId']
                };
                commonQuestion.getQuestionInfo('click', questionData);
            }
        },
        getQuestionInfo: function(type, data) {
            if (commonQuestion.isAnswer == 1) {
                commonQuestion.questionHasAnswered();
            } else {
                $.ajax({
                    url: $CONFIG['questionInvestAppearUrl'],
                    type: 'get',
                    data: data,
                    dataType: 'jsonp',
                    success: function(res) {
                        commonQuestion.appearFlag = res.appearFlag;
                        commonQuestion.isAnswer = res.isAnswer;
                        commonQuestion.questionId = res.questionId;
                        if (type === 'init' && commonQuestion.appearFlag == 1) {
                            $(".toolsItem[data-type='question']").css('display', 'block');
                        } else if (type === 'click' && commonQuestion.isAnswer != 1) {
                            var params = commonHandler.getUncryptParams();
                            params['slotId'] = $CONFIG['slotId'];
                            var surveyUrl = $CONFIG['mdomain'] + 'survey/' + commonQuestion.questionId + '.html?' + utils.urlEncode(params).slice(1);
                            window.location.href = surveyUrl;
                        } else if (type === 'click' && commonQuestion.isAnswer == 1) {
                            commonQuestion.questionHasAnswered();
                        }
                    }
                });
            }
        },
        questionHasAnswered: function() {
            $("#answered").removeClass('none');
            $("#answered .bottom").click(function() {
                $("#answered").addClass('none');
            });
            $("#answered")[0].addEventListener('touchmove', function(e) {
                e.preventDefault();
            }, false);
        },
        // getQuestionInfo: function(callback) {
        //     commonQuestion.userId = utils.userId;
        //     if (commonQuestion.userId) {
        //         commonQuestion.hasGetIsAnswer = true;
        //     }

        //     var params = utils.commonParams();
        //     var questionData;
        //     if (type === 'init') {
        //         questionData = {
        //             questionId: 1,
        //             logParam: params['logParam'],
        //             flightJobId: params['flightJobId']
        //         };
        //     } else {
        //         questionData = {
        //             questionId: 1,
        //             userId: utils.userId,
        //             logParam: params['logParam'],
        //             flightJobId: params['flightJobId']
        //         };
        //     }
        //     $.ajax({
        //         url: $CONFIG['questionInvestAppearUrl'],
        //         type: 'get',
        //         data: questionData,
        //         dataType: 'jsonp',
        //         success: function(res) {
        //             commonQuestion.appearFlag = res.appearFlag;
        //             commonQuestion.isAnswer = res.isAnswer;
        //             if (commonQuestion.appearFlag == 1) {
        //                 $(".toolsItem[data-type='question']").css('display', 'block');
        //             }
        //         }
        //     });
        // },
        tap: function() {
            if (utils.userId !== '') {
                // commonQuestion.doQuestionAction();
                commonQuestion.questionHandler('click');
            } else {
                AppInterface.call('/common/logout', function(data) {
                    if (data.success) {
                        AppInterface.call('/common/login', function(data) {
                            if (data.success) {
                                utils.userId = data.data.userId;
                                // commonQuestion.doQuestionAction();
                                commonQuestion.questionHandler('click');
                            }
                        });
                    }
                });
            }
        },
        // 问卷跳转
        doQuestionAction: function() {
            if (!commonQuestion.hasGetIsAnswer) {
                commonQuestion.getQuestionInfo();
            }
            if (commonQuestion.isAnswer == 1) {
                // @todo 提示已答过题
                $("#answered").removeClass('none');
                $("#answered .bottom").click(function() {
			    	$("#answered").addClass('none');
			    });
			    $("#answered")[0].addEventListener('touchmove', function(e) {
			        e.preventDefault();
			    }, false);
            } else {
                var params = utils.commonParams();
                params['slotId'] = $CONFIG['slotId'];
                var surveyUrl = $CONFIG['surveyUrl'] + '?' + utils.urlEncode(params).slice(1);
                window.location.href = surveyUrl;
                // AppInterface.call('/common/localJump',{url:window.btoa(surveyUrl)});
                // @todo wap页面跳转
            }
        }

    };
    var video = {
        init: function() {
            // 非视频页面不执行播放逻辑
            // @todo
            if ($CONFIG['videoId'] == undefined) {
                return;
            }

            var played = false;
            var v = new MeixinPlayer();
            var config = {
                autoplay : 1,
                autoSize: 1,
                env: $CONFIG['env'],
                poster: $CONFIG['videoImage'],
            }

            v.init($CONFIG['videoId'], 'videoContainer', config);
            // 开始播放时调用视频计数接口
            v.on('videoPlayed', function() {
                console.log("video played");
                console.log("add video count");
                $.ajax({
                    url: $CONFIG['setVideoCountUrl'] + $CONFIG['videoId'],
                    type: 'GET',
                    dataType: 'jsonp',
                });
            }, null);
            v.on('playPause', function() {
                console.log("video playPause");
            }, null);
            v.on('playStop', function() {
                console.log("video playStop");

                // 多次播放只上报一次
                if (!played) {
                    console.log("video Rebate Url");
                    var commonParams = commonHandler.getUncryptParams();

                    var videoParams = {};
                    videoParams['videoId'] = $CONFIG['videoId'];
                    videoParams['requestId'] = commonParams['requestId'];
                    videoParams['flightJobId'] = commonParams['flightJobId'];
                    // videoParams['logParam'] = commonParams['logParam'];
                    videoParams['deviceId'] = utils.getDeviceIdFromAgent();
                    videoParams['slotId'] = $CONFIG['slotId'];
                    // @todo 判断类型
                    videoParams['slotType'] = 1;
                    videoParams['userId'] = utils.userId !== '' ? utils.userId : 0;

                    var params = {
                        p: utils.encrypt(utils.urlEncode(videoParams).slice(1)),
                        q: utils.encrypt('logParam=' + commonParams['logParam'])
                    }

                    $.ajax({
                        url: $CONFIG['videoShareUrl'],
                        type: 'GET',
                        data: params,
                        dataType: 'jsonp',
                        success: function(result) {
                            console.log("success");
                        },
                    });
                    played = true;
                }
            }, null);
        },
        initLoginStyle: function() {
            if ($CONFIG['videoId'] == undefined) {
                return;
            }
            if (utils.userId == '') {
                $('.detail_footer p').css('padding-bottom', '1.84rem');
                $('.video_templet_shop').css('margin-bottom', '1.84rem');
                if (commonRebate.isWatchRebate) {
                    $('.video-login-box').show();
                    $('.video-login-box').on('click', function() {
                        AppInterface.call('/common/logout', function(data) {
                            if (data.success) {
                                AppInterface.call('/common/login', function(data) {
                                    if (data.success) {
                                        utils.userId = data.data.userId;
                                        $('.video-login-box').hide();
                                        $('.detail_footer p').css('padding-bottom', '1rem');
                                        $('.video_templet_shop').css('margin-bottom', '1rem');
                                    }
                                });
                            }
                        });
                    });
                }
            }
        }
    };
    var weixin = {
        weixinConfig: [],
        weixinSDKUrl: 'https://res.wx.qq.com/open/js/jweixin-1.2.0.js',
        url: '/api/weixin',
        init: function() {
            var script = document.createElement('script');
            var parentNode = document.getElementsByTagName('head')[0] || document.body;
            script.async = true;
            script.src = weixin.weixinSDKUrl;
            script.onload = weixin.getWeixinConfigInfor;
            parentNode.appendChild(script);
        },
        getWeixinConfigInfor: function() {
            $.ajax({
                url: weixin.url,
                type: 'GET',
                data: {shareUrl: encodeURIComponent(window.location.href.split('#')[0])},
                dataType: 'jsonp',
                success: function(result) {
                    var data = result['data'];
                    weixin.initWeixin(data);
                }
            });
            // utils.requestJsonp(requestUrl, weixin.initWeixin);
        },
        initWeixin: function(result) {
            wx.config({
                debug: false,
                appId: result['wx_appid'],
                timestamp: result['wx_timestamp'],
                nonceStr: result['wx_noncestr'],
                signature: result['wx_signature'],
                jsApiList: [
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
                    'onMenuShareQZone'
                ]
            });
            wx.ready(function() {
                wx.onMenuShareTimeline({
                    title: commonShare.shareOptions.title,
                    link: window.location.href,
                    imgUrl: window.atob(commonShare.shareOptions.imgUrl),
                });
                wx.onMenuShareAppMessage({
                    title: commonShare.shareOptions.title,
                    desc: commonShare.shareOptions.desc,
                    link: window.location.href,
                    imgUrl: window.atob(commonShare.shareOptions.imgUrl),
                });
                wx.onMenuShareQQ({
                    title: commonShare.shareOptions.title,
                    desc: commonShare.shareOptions.desc,
                    link: window.location.href,
                    imgUrl: window.atob(commonShare.shareOptions.imgUrl),
                });
                wx.onMenuShareQZone({
                    title: commonShare.shareOptions.title,
                    desc: commonShare.shareOptions.desc,
                    link: window.location.href,
                    imgUrl: window.atob(commonShare.shareOptions.imgUrl),
                });
            });
        }
    };

    $(".toolsItem[data-type='question']").bind("tap", commonQuestion.tap);

    // utils.getUserId();
    commonHandler.getUserId();
    video.init();
    // commonRebate.init();
    commonShop.init();
    commonPrice.init();
    commonParise.init();
    commonShare.init();
    middlePage.init();
    if (utils.isWeiXin) {
        weixin.init();
    }
});
