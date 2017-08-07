	var utils = {
		isGome: /gomeplus/g.test(navigator.userAgent),
		isWeiXin: /MicroMessenger/g.test(navigator.userAgent),
		isQQ: /QQ/g.test(navigator.userAgent),
		isWeibo: /Weibo/g.test(navigator.userAgent),
		isIos: /iPhone/g.test(navigator.userAgent),
		isAndroid: /Android/g.test(navigator.userAgent),
		userId: '',
		getUserId: function() {
			if (utils.isGome) {
				AppInterface.call('/common/getLoginStatus', function(data) {
					if (data.success) {
						utils.userId = data.data.userId;
					}
				});
			}
		},
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
		getAppVersion: function() {
			var ua = navigator.userAgent;
			return 30;
		},
		formatCurrency: function(currency) {
			var result = parseInt(currency, 10) * 0.01;
			return result.toFixed(2);
		},
        encrypt: function(word){
            var key = CryptoJS.enc.Utf8.parse('abcdefgabcdefg12')
            var srcs = CryptoJS.enc.Utf8.parse(word);
            var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
            return encrypted.toString();
        },
        decrypt: function(word){
            var key = CryptoJS.enc.Utf8.parse("abcdefgabcdefg12");
            var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
            return CryptoJS.enc.Utf8.stringify(decrypt).toString();
        },
        getUncryptParams: function() {
            var params = utils.commonParams();
            if (params.length == 2 && params['p'] && params['q']) {
                var decryptP = utils.decrypt(params['p']);
                var decryptQ = utils.decrypt(params['q']);
                var paramsP = utils.getKeyValue(decryptP);
                var paramsQ = utils.getKeyValue(decryptQ);
                params = paramsP;
                params['logParam'] = paramsQ['logParam'];
            }
            return params;
        }
	}

