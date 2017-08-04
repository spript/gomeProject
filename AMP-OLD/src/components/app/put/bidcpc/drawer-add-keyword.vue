<template>
	<div class="amp-drawer">
		<div class="drawer-part">
			<h2 class="part-header"><span class="header-text">添加关键词</span><span class="header-fn"><em @click="hide()" class="icon icon-close"></em></span></h2>
			<div class="amp-form amp-form02">
				<div class="amp-bidcpc">
					<div class="bidcpc-inner">
						<el-input size="small" style="width:260px;display:inline-block;" placeholder="输入商品SKU ID" v-model="search.skuId"></el-input>
						<el-button size="small" style="padding:8px 9px;" @click="getKeyWordsBySkuId">搜索</el-button>
						<el-select v-model="search.type" size="small" style="width:100px;display:inline-block;" placeholder="请选择">
						    <el-option label="PC端" :value="0"></el-option>
						    <el-option label="无线端" :value="1"></el-option>
						</el-select>
						<p class="bidcpc-text">已为您找到{{keywords.length}}个关键词</p>
					</div>
					<el-table :data="keywords" border height="82%" @selection-change="handleLeftChange" style="position: absolute;top: 70px;bottom: 10px;width: 100%;height:82%;" empty-text="无相关搜索结果">
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
				<div class="amp-bidcpc-btn"><el-button size="small" @click="addKeywordsToRight">添加至右侧</el-button></div>
				<div class="amp-bidcpc">
					<div class="bidcpc-inner">
						<el-input size="small" style="width:200px;display:inline-block;" placeholder="输入自定义关键词" v-model="customKeyword"></el-input>
						<el-button size="small" style="padding:8px 9px;" @click="addCustomKeyWords">添加</el-button>
						<p class="bidcpc-text">已选关键词 {{selectRightKeywords.length}}/{{selectKeywords.length}}</p>
					</div>
					<el-table :data="selectKeywords" border height="82%" @selection-change="handleRightChange"  style="position: absolute;top: 70px;bottom: 40px;width: 100%;height:75%;">
						<el-table-column type="selection" width="55" :resizable="false"></el-table-column>
						<el-table-column label="关键词" width="130" :resizable="false">
							<template scope="scope">
								<span :title="scope.row.name"><i class="ellipsis">{{ scope.row.name }}</i></span>
							</template>
						</el-table-column>
						<el-table-column label="PC端出价" width="152" :resizable="false">
							<template scope="scope">
								<div class="ele-input ele-input04" style="height: 30px;padding-top: 0px;padding-bottom: 0px;">
									<input type="text" value="" :id="'pcBid-' + scope.$index" v-model="scope.row.pcBid" @blur="pcBidBlurCheck('pcBid-', scope.$index)" @mouseout="pcBidMouseOutCheck('pcBid-', scope.$index)" @keyup="pcBidKeyUpCheck('pcBid-', scope.$index)">
								</div>
							</template>
						</el-table-column>
					</el-table>
					<div class="bidcpc-inner">
						<el-button size="small" style="padding:8px 9px;" @click="delKeyWordsBatch">批量删除</el-button>
						<div class="ele-input ele-input04" style="height: 30px;padding-top: 0px;padding-bottom: 0px;width: 200px;">
							<input type="text" value="" id="customPcBid" v-model="customPcBid" @blur="batchPcBidBlurCheck('customPcBid')" @mouseout="batchPcBidMouseOutCheck('customPcBid')" @keyup="batchPcBidKeyUpCheck('customPcBid')" placeholder="批量出价">
						</div>
						<el-button size="small" style="padding:8px 9px;" @click="setPcBidBatch" :disabled="batchPcBidFlag">确定</el-button>
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
			batchPcBidFlag: true
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
				}
			}).catch((error) => {
				console.log(error);
			});
		},
		addKeywordsToRight() {
			if (this.selectLeftKeywords.length === 0) {
				this.$message({
					message: '您没有选中任何关键词，请选择后继续操作！',
					type: 'warning'
				});
			} else {
				this.selectLeftKeywords.forEach((item, indet) => {
					let isCon = this.selectKeywords.find((selItem) => {
						return selItem.id === item.id;
					});
					console.log(isCon);
					if (isCon === undefined) {
						this.$set(item, 'pcBid', 0);
						this.selectKeywords.push(item);
					}
				});
				// this.selectKeywords.push(this.selectLeftKeywords);
			}
		},
		setPcBidBatch() {
			if (this.selectRightKeywords.length === 0) {
				this.$message({
					message: '您没有选中任何关键词，请选择后继续操作！',
					type: 'warning'
				});
				return false;
			}
			this.selectKeywords.forEach((item) => {
				let isCon = this.selectRightKeywords.find((selItem) => {
					return item.id === selItem.id;
				});
				if (isCon) {
					item.pcBid = this.customPcBid;
				}
			});
		},
		delKeyWordsBatch() {
			if (this.selectRightKeywords.length === 0) {
				this.$message({
					message: '您没有选中任何关键词，请选择后继续操作！',
					type: 'warning'
				});
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
		addCustomKeyWords() {
			if (strTrim(this.customKeyword).length === 0) {
				this.$message({
					message: '您没有输入任何字符！',
					type: 'warning'
				});
				return false;
			}
			let item = {
				id: '',
				name: '',
				searchStar: 0,
				purchaseStar: 0,
				averagePrice: 0,
				pcBid: 0,
				source: 0
			};
			Http.post('/api/keywords', {
				name: this.customKeyword
			}).then((res) => {
				if (res.status === 200 && res.data.code === 200 && res.data.iserror === 0) {
					item.id = res.data.data.id;
					item.name = res.data.data.name;
					let isCon = this.selectKeywords.find((selItem) => {
						return item.id === selItem.id;
					});
					if (!isCon) {
						this.selectKeywords.push(item);
					} else {
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
		},
		save() {
		    if (this.selectKeywords.length > 0) {
		        for (var i = 0; i < this.selectKeywords.length; i++) {
		            if (!this.pcBidBlurCheck("pcBid-", i)) {
		                return;
					}
				}

			}
			if (this.selectKeywords.length >200) {
				this.$message({
					message: '最多只可以选择200个关键词。',
					type: 'warning'
				});
				return;
			}
			Event.$emit('drawer-save-add-keyword', this.selectKeywords);
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
				this.$message({message: result.msg, type: 'error'});
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
				return {"success": false, "msg": "请输入" + title};
			} else if (value < 0.10) {
				return {"success": false, "msg": title + "最小为0.10"};
			} else if (value > 999.99) {
				return {"success": false, "msg": title + "最大不超过999.99"};
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
	top: 60px;
    right: 0;
    bottom: 0;
	.amp-bidcpc{
		width: 340px;
		float: left;
		height: 100%;
		position: relative;
		overflow: auto;
		.bidcpc-text{
			margin: 10px 0;
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
			position: absolute;
			bottom: 0;
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
</style>
