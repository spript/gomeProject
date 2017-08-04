<template>
	<div class="amp-drawer">
		<div class="drawer-part">
			<h2 class="part-header"><span class="header-text">添加关键词</span><span class="header-fn"><em @click="hide()" class="icon icon-close"></em></span></h2>
			<div class="amp-form amp-form02" style="margin-top: 0;">
				<div class="amp-bidcpc">
					<div class="bidcpc-inner">
						<el-select v-model="search.type" style="width:100px;display:inline-block;margin-right: 20px;" placeholder="请选择">
						    <el-option label="PC端" :value="0"></el-option>
						    <el-option label="无线端" :value="1"></el-option>
						</el-select>
						<el-input style="width:170px;display:inline-block;margin-right: 20px;" placeholder="输入商品SKU ID" v-model="search.skuId"></el-input>
						<a href="javascript:;" class="btn btn-primary" @click="getKeyWordsBySkuId" style="padding: 0 40px;">查询</a>
						<div class="amp-bidcpc-add clearfix">
							<button class="btn btn-normal" @click="addKeywordsToRight">添加</button>
							<p class="bidcpc-text">已选择{{selectLeftKeywords.length}}个关键词</p>
							<div v-show="showHintAdd" style="position: absolute;left: 1px;top: 54px;color: #ff4949;font-size: 12px;">请选择关键词后，再添加</div>
						</div>
					</div>
					<el-table :data="keywords" border height="82%" @selection-change="handleLeftChange" style="position: absolute;top: 110px;bottom: 10px;width: 100%;height:430px;" empty-text="无相关搜索结果">
						<el-table-column type="selection" width="55" :resizable="false"></el-table-column>
						<el-table-column label="关键词" width="110" :resizable="false">
							<template scope="scope">
								<span :title="scope.row.name"><i class="ellipsis">{{ scope.row.name }}</i></span>
							</template>
						</el-table-column>
						<el-table-column label="搜索指数" width="130" :resizable="false" class-name="table-rate">
							<template scope="scope">
								<el-rate v-model="scope.row.searchStar" disabled></el-rate>
							</template>
						</el-table-column>
						<el-table-column prop="purchaseStar" label="竞争指数" width="130" :resizable="false" class-name="table-rate">
							<template scope="scope">
								<el-rate v-model="scope.row.purchaseStar" disabled></el-rate>
							</template>
						</el-table-column>
						<el-table-column prop="averagePrice" label="历史均价" :resizable="false"></el-table-column>
					</el-table>
				</div>
				<div class="amp-bidcpc" style="margin-left: 50px;">
					<div class="bidcpc-inner">
						<p class="bidcpc-text" :class="selectKeywords.length>200?'bidcpc-text-hint':''">
							已添加关键词 {{selectKeywords.length}}/{{200}}
							<div v-show='selectKeywords.length>200' style="position: absolute;left: 0;top: 30px;color: #ff4949;">最多只能添加200个关键词，已为您添加前200个</div>
						</p>
						<div class="amp-bidcpc-del clearfix">
							<button class="btn btn-normal" @click="delKeyWordsBatch">删除</button>
							<div v-show='showHintDel' style="position: absolute;left: 1px;top: 48px;color: #ff4949;font-size: 12px;">请选择关键词后，再删除</div>
							<a class="batchBtn" href="javascript:;" @click="showMoney" :class="{borderChangeColor:!showMoneyBox}">
								批量出价
								<div v-show="showHintBatch" style="position: absolute;left: -70px;top: 26px;color: #ff4949;font-size: 12px;">请选择关键词后，再批量出价</div>
							</a>
							<div class="money-box" :class='{none:showMoneyBox}'>
								<div :class='{borderColor:showAllMoneyHint}' class="ele-input ele-input04" style="height: 40px;padding-top: 0px;padding-bottom: 0px;width: 98px;border-radius: 5px;">
									<input type="text" @input.prevent="clearNum" value="" id="customPcBid" v-model="customPcBid">
								</div>
								元
								<a href="javascript:;" class="btn btn-primary" @click="setPcBidBatch" style="padding: 0 14px;margin-left: 15px;">确定</a>
								<a href="javascript:;" class="btn btn-gray" @click="hideMoneyBox" style="height:36px;line-height:36px;padding: 0 14px;margin-left: 5px;">取消</a>
								<div :class='{none:!showAllMoneyHint}' style="position: absolute;left: 15px;top: 55px;color: #ff4949;font-size: 12px;">{{showAllMoneyTxt}}</div>
							</div>
						</div>
					</div>
					<el-table :data="selectKeywords" border height="82%" @selection-change="handleRightChange"  style="position: absolute;top: 110px;bottom: 40px;width: 100%;height:50%;">
						<el-table-column type="selection" width="55" :resizable="false"></el-table-column>
						<el-table-column label="关键词" width="186" :resizable="false">
							<template scope="scope">
								<span v-show='!scope.row.showCustomInput' :title="scope.row.name"><i class="ellipsis">{{ scope.row.name }}</i></span>
								<div :style='{borderColor:scope.row.showHintKeywords?"#ff4949":""}' v-show='scope.row.showCustomInput' class="ele-input ele-input04" style="position:relative;height: 30px;padding-top: 0px;padding-bottom: 0px;margin: 25px 0;overflow: visible;">
									<input type="text" v-model="scope.row.name" @blur="requestKeywords(scope.row.name, scope.$index)">
									<div v-show="scope.row.showHintKeywords" style="position: absolute;left: 0px;top: 20px;color: #ff4949;font-size: 12px;">{{ scope.row.showHintKeywordsTxt }}</div>
								</div>
							</template>
						</el-table-column>
						<el-table-column label="PC端出价" width="162" :resizable="false">
							<template scope="scope">
								<div :style='{borderColor:scope.row.showHintPc?"#ff4949":""}' class="ele-input ele-input04" style="position:relative;height: 30px;padding-top: 0px;padding-bottom: 0px;margin: 25px 0;overflow: visible;">
									<input type="text" value="" :id="'pcBid-' + scope.$index" v-model="scope.row.pcBid" @blur="pcBidBlurCheck('pcBid-', scope.$index)" @mouseout="pcBidMouseOutCheck('pcBid-', scope.$index)" @keyup="pcBidKeyUpCheck('pcBid-', scope.$index)">
									<div v-show="scope.row.showHintPc" style="position: absolute;left: 0px;top: 20px;color: #ff4949;font-size: 12px;">{{ scope.row.showHintPcTxt }}</div>
								</div>
							</template>
						</el-table-column>
					</el-table>
					<div class="bidcpc-inner">
						<a href="javascript:;" @click="addCustomKeyWords">+自定义关键词</a>
					</div>
				</div>
			</div>
		</div>
		<div class="drawer-btn">
			<button class="btn btn-primary" @click="save()">保存</button>
			<button class="btn btn-gray" @click="hide()">取消</button>
		</div>
	</div>
</template>
<script>
import Vue from 'vue';
import store from 'store';
import {
	copyObj,
	strTrim
} from 'utils/common';
import Event from 'event';
import Http from 'http';
export default {
	name: 'app-put-bidcpc-new-add-keyword',
	data() {
		return {
			keywords: [],
			getKeyWords: {
				pc: [],
				wap: []
			},
			selectKeywords: [],
			selectLeftKeywords: [],
			selectRightKeywords: [],
			customKeyword: '',
			customPcBid: '',
			search: {
				skuId: '',
				type: 0
			},
			checkAllSelect: false,
			batchPcBidFlag: true,
			showMoneyBox: true,
			showAllMoneyHint: false,
			showAllMoneyTxt: '',
			showHintBatch: false,
			showHintDel: false,
			showHintAdd: false,
			isConstum: false,
			isKeywordsNull: false,
		};
	},
	props: {
		unitId: Number,
		kwds: {
			type: Array,
			default: []
		}
	},
	computed: {
		drawerData: () => store.state.drawerCtrl.config, // from store
	},
	created() {
		if (this.unitId !== undefined) {
			Http.get('/api/keywords/flightId', {
				params: {
					flightId: this.unitId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200) {
					this.selectKeywords = res.data.data.keywords;
				}
			}).catch((error) => {
				console.log(error);
			});
		}
		this.selectKeywords.push.apply(this.selectKeywords,this.kwds);
		// this.selectKeywords.push(this.kwds);
	},
	methods: {
		clearNum() {
			if (this.customPcBid.length) {
				this.customPcBid = this.customPcBid.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  
			    this.customPcBid = this.customPcBid.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的  
			    this.customPcBid = this.customPcBid.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
			    this.customPcBid = this.customPcBid.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数  
			    if(this.customPcBid.indexOf(".")< 0 && this.customPcBid !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额 
			        this.customPcBid= parseFloat(this.customPcBid); 
			    }
			}
			if (this.customPcBid > 999.99) {
				this.showAllMoneyHint = true;
				this.showAllMoneyTxt = '出价最大不超过999.99';
			} else if (this.customPcBid < 0.10) {
				this.showAllMoneyHint = true;
				this.showAllMoneyTxt = '出价最小为0.10';
			} else {
				this.showAllMoneyHint = false;
				this.showAllMoneyTxt = '';
			}
		},
		showMoney() {
			if (this.selectRightKeywords.length === 0) {
				this.showHintBatch = true;
			} else {
				this.showMoneyBox = !this.showMoneyBox;
				this.showHintBatch = false;
				if (this.showMoneyBox) {
					this.showAllMoneyHint = false;
					this.showAllMoneyTxt = '';
				}
			}
		},
		hideMoneyBox() {
			this.showMoneyBox = true;
			this.showAllMoneyHint = false;
			this.showAllMoneyTxt = '';
		},
		handleLeftChange(selectItems) {
			this.selectLeftKeywords = selectItems;
		},
		handleRightChange(selectItems) {
			this.selectRightKeywords = selectItems;
		},
		getKeyWordsBySkuId() {
			let vm = this;
			Http.get('/api/keywords/skuId', {
				params: {
					skuId: this.search.skuId
				}
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200) {
					let kws = res.data.data.keywords;
					let pcKws = [], wapKws = [];
					kws.forEach((item) => {
						//1-pc  2-无线
						if (item.source === 1) {
							pcKws.push(item);
						} else {
							wapKws.push(item);
						}
					});
					this.getKeyWords.pc = pcKws;
					this.getKeyWords.wap = wapKws;
					this.keywords = this.search.type === 0 ? this.getKeyWords.pc : this.getKeyWords.wap;
					this.duplicate(this.selectKeywords);
				}
			}).catch((error) => {
				console.log(error);
			});
		},
		addKeywordsToRight() {
			if (this.selectLeftKeywords.length === 0) {
				this.showHintAdd = true;
				return false;
			} else {
				this.selectLeftKeywords.forEach((item, indet) => {
					this.$set(item, 'pcBid', 0);
					this.$set(item, 'showHintPc', false);
					this.$set(item, 'showHintPcTxt', '');
					this.$set(item, 'showCustomInput', false);
					this.$set(item, 'showHintKeywords', false);
					this.$set(item, 'showHintKeywordsTxt', '');
					this.selectKeywords.push(item);
				});
				this.duplicate(this.selectLeftKeywords);
			}
		},
		duplicate(arr) {
			arr.forEach((item, indet) => {
				if (item.source == 1) {
					this.getKeyWords.pc.forEach((itemPc, indexPc) => {
						if (itemPc.id == item.id) {
							this.getKeyWords.pc.splice(indexPc, 1);
						}
					});
				} else {
					this.getKeyWords.wap.forEach((itemWap, indexWap) => {
						if (itemWap.id == item.id) {
							this.getKeyWords.wap.splice(indexWap, 1);
						}
					});
				}
			});
		},
		setPcBidBatch() {
			if (this.customPcBid == '') {
				this.showAllMoneyHint = true;
				this.showAllMoneyTxt = '请输入出价';
			} else {
				this.selectKeywords.forEach((item) => {
					let isCon = this.selectRightKeywords.find((selItem) => {
						return item.id === selItem.id;
					});
					if (isCon) {
						item.pcBid = this.customPcBid;
					}
				});
				this.showMoneyBox = true;
			}
		},
		delKeyWordsBatch() {
			if (this.selectRightKeywords.length === 0) {
				this.showHintDel = true;
				return false;
			}
			let newArr = [];
			this.selectKeywords.forEach((item, index) => {
				let isCon = this.selectRightKeywords.find((selItem) => {
					return item.id === selItem.id;
				});
				if (isCon === undefined) {
					newArr.push(item);
				}
			});
			this.selectKeywords = newArr;
		},
		addCustomKeyWords(value, index) {
			let item = {
				id: '',
				name: '',
				searchStar: 0,
				purchaseStar: 0,
				averagePrice: 0,
				pcBid: 0,
				source: 0,
				showHintPc: false,
				showHintPcTxt: '',
				showCustomInput: true,
				showHintKeywords: false,
				showHintKeywordsTxt: ''
			};
			this.selectKeywords.push(item);
		},
		requestKeywords(value, index) {
			if (value == '') {
				this.selectKeywords[index].showHintKeywords = true;
				this.selectKeywords[index].showHintKeywordsTxt = '请输入关键词';
			} else if (value.length > 50) {
				this.selectKeywords[index].showHintKeywords = true;
				this.selectKeywords[index].showHintKeywordsTxt = '关键词字数不超过50个汉字';
			} else {
				this.selectKeywords[index].showHintKeywords = false;
				this.selectKeywords[index].showHintKeywordsTxt = '';
				this.isConstum = false;
				Http.post('/api/keywords', {
					name: value
				}).then((res) => {
					if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
						this.selectKeywords[index].id = res.data.data.id;
						this.selectKeywords[index].name = res.data.data.name;
						for(let i = 0; i < this.selectKeywords.length; i++) {
							if (i == index) {
								continue;
							}
							if (this.selectKeywords[index].id === this.selectKeywords[i].id) {
								this.isConstum = true;
							}
						}
						if (this.isConstum) {
							this.$message({
								message: '该关键词已存在，请添加其它关键词。',
								type: 'warning'
							});
						}
					} else {
						this.$message({
							message: res.data.msg,
							type: 'error'
						});
					}
				}).catch((error) => {
					console.log(error);
				});
			}
		},
		save() {
			let keyWordsArr = [];
		    if (this.selectKeywords.length > 0) {
		        for (var i = 0; i < this.selectKeywords.length; i++) {
		            if (!this.pcBidBlurCheck("pcBid-", i)) {
		                return;
					}
				}
		        for (var j = 0; j < this.selectKeywords.length; j++) {
		        	if (this.selectKeywords[j].showHintKeywords) {
		        		return;
		        	} else if (this.selectKeywords[j].name == '') {
		        		this.selectKeywords[j].showHintKeywords = true;
						this.selectKeywords[j].showHintKeywordsTxt = '请输入关键词';
		        		return;
		        	}
		        }

			}
		    if (this.isConstum) {
		    	this.$message({
					message: '已添加关键词列表中，存在重复关键词。',
					type: 'warning'
				});
				return;
		    }
		    if (this.isKeywordsNull) {
		    	return;
		    }
			if (this.selectKeywords.length > 200) {
				keyWordsArr = this.selectKeywords.slice(0, 200);
			} else {
				keyWordsArr = this.selectKeywords;
			}
			Event.$emit('drawer-save-add-keyword', keyWordsArr);
		},
		hide() {
			Event.$emit('drawer-hide-add-keyword');
		},



		pcBidMouseOutCheck(idPrefix, index) {
			var inputId = idPrefix + index;
			var value = this.blurCheck(inputId);
			this.selectKeywords[index].pcBid = value;
		},
		pcBidBlurCheck(idPrefix, index) {
			var inputId = idPrefix + index;
			var value = this.blurCheck(inputId);
			this.selectKeywords[index].pcBid = value;

			var result = this.limitedCondition("关键词[ ", " ]的PC端出价", this.selectKeywords[index].name, value);
			if (!result.success) {
				this.selectKeywords[index].showHintPc = true;
				this.selectKeywords[index].showHintPcTxt = result.msg;
			} else {
				this.selectKeywords[index].showHintPc = false;
				this.selectKeywords[index].showHintPcTxt = '';
			}

			return result.success;
		},
		batchPcBidMouseOutCheck(inputId) {
			var value = this.blurCheck(inputId);
			this.customPcBid = value;

			var result = this.limitedCondition("批量出价", "", "", value);
			this.batchPcBidFlag = !result.success;
		},
		batchPcBidBlurCheck(inputId) {
			var value = this.blurCheck(inputId);
			this.customPcBid = value;

			var result = this.limitedCondition("批量出价", "", "", value);
			if (!result.success) {
				this.$message({message: result.msg, type: 'error'});
			}

			this.batchPcBidFlag = !result.success;

			return result.success;
		},



		pcBidKeyUpCheck(idPrefix, index) {
			var inputId = idPrefix + index;
			var value = this.floatFormat(inputId);
			this.selectKeywords[index].pcBid = value;

		},
		batchPcBidKeyUpCheck(inputId) {
			var value = this.floatFormat(inputId);
			this.customPcBid = value;

			var result = this.limitedCondition("批量出价", "", "", value);
			this.batchPcBidFlag = !result.success;

		},

		blurCheck(inputId) {
			var value = document.getElementById(inputId).value;

			// 处理一个或者多个0开头 的字符
			if (value != 0) {
				value = value.replace(/^0+/, "");
			}
			if (value.indexOf(".") == 0) {
			    value = "0" + value;
			}
			// 处理以小数点结尾的字符
			value = value.replace(/\.$/, "");

			document.getElementById(inputId).value = value;

			return value;
		},
		floatFormat(inputId) {

			var value = document.getElementById(inputId).value;

			// 处理非数字 非小数点 的字符
			value = value.replace(/[^0-9\.]+/, "");
			// 处理以小数点开头的字符
			value = value.replace(/^\.+/, "");

			// 处理不小数点开头但出现多次小数点的字符
			var regObject = value.match(/\.+/g);
			var count = 0;
			if (regObject != null && regObject.length > 0) {
				for (var i = 0; i < regObject.length; i++) {
					count += regObject[i].length;
				}
			}

			if (value.indexOf(".") > 0) {
				if (count > 1) {
					value = value.substring(0, value.indexOf(".") + 1) + value.substring(value.indexOf(".")).replace(/\./g, "");
				}

				value = value.substring(0, value.indexOf(".") + 3);
			}

			document.getElementById(inputId).value = value;

			return value;
		},
		limitedCondition(prefix, suffix, name, value) {
		    var title = prefix + name + suffix;

			if (value === '') {
				return {"success": false, "msg": "请输入出价"};
			} else if (value < 0.10) {
				return {"success": false, "msg": "出价最小为0.10"};
			} else if (value > 999.99) {
				return {"success": false, "msg": "出价最大不超过999.99"};
			} else {
				return {"success": true, "msg": ""};
			}
		}
	},
	watch: {
		'search.type': {
			handler: function(value) {
				this.keywords = value === 0 ? this.getKeyWords.pc : this.getKeyWords.wap;
			},
			deep: true
		},
		'selectRightKeywords': function() {
			if (this.selectRightKeywords.length != 0) {
				this.showHintBatch = false;
				this.showHintDel = false;
			} else {
				this.showMoneyBox = true;
			}
		},
		'selectLeftKeywords': function() {
			if (this.selectLeftKeywords.length != 0) {
				this.showHintAdd = false;
			}
		}
	}
};
</script>
<style lang="less" scoped>
.drawer-part{
	position: relative;
}
.amp-form{
	position: absolute;
	top: 100px;
    right: 0;
    bottom: 0;
	.amp-bidcpc{
		width: 405px;
		float: left;
		height: 100%;
		position: relative;
		overflow: auto;
		.bidcpc-text{
			margin: 10px 0;
			position: relative;
		}
		p.bidcpc-text-hint{
			color: #ff4949;
		}
	}
	.amp-bidcpc:first-child{
		width: 540px;
	}
	.amp-bidcpc-btn{
		width: 80px;
		margin: 80px 10px;
		float: left;
	}
	.amp-bidcpc:last-child{
		.bidcpc-inner:last-child{
			width:100%;
			position: absolute;
			bottom: 115px;
			a{
				display: block;
				width:100%;
				height: 30px;
				border: 1px solid #d9dbde;
				border-radius: 5px;
				text-align: center;
				line-height: 30px;
				color: #89919c;
			}
		}
	}
}
.amp-form:after{
	content: "";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
}
table{width: 100% !important;}
.amp-bidcpc-add{
	position: relative;
	padding-top: 19px;
	button{
		float: left;
	}
	p{
		width:432px;
		float: left;
		padding-left: 8px;
	}
}
.amp-bidcpc-del{
	padding-top: 13px;
	position: relative;
	z-index: 2000;
	button{
		position: relative;
		float: left;
	}
	.batchBtn{
		display: block;
		width:88px;
		height: 35px;
		line-height: 35px;
		border: 1px solid #d9dbde;
		border-radius: 5px;
		text-align: center;
		float: right;
		color: #89919c;
		position: relative;
	}
	a.borderChangeColor{
		border-bottom: 1px solid #fff;
		border-radius: 5px 5px 0 0;
	}
	/*span:hover{
		border-bottom: 1px solid #fff;
	}
	span:hover .money-box{
		display: block;
	}*/
	.money-box{
		position: absolute;
		right: 0;
		top: 47px;
		width:340px;
		height: 80px;
		padding-top: 15px;
		padding-left: 15px;
		border: 1px solid #d9dbde;
		border-radius: 5px 0 5px 5px;
		background: #fff;
		z-index: -1;
	}
	.none{
		display: none;
	}
	.borderColor{
		border-color: #ff4949;
	}
}
</style>
