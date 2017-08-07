<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="showCpcPrice" @click="closeView">></div>
		</transition>
		<transition name="drawer">
			<div class="ams-stock" v-show="showCpcPrice" >
				<div class="drawer-part">
				 	<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">价格</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<div class="shop-cont">
						<el-form :model="Data" :rules="rules" ref="Data" label-width="166px">
							<el-form-item label="广告名称：">
								<span>{{summary.name}}</span>
							</el-form-item>
							<el-form-item label="定价CPC价格：" prop='cpcPrice'>
								<el-input v-model="Data.cpcPrice" class='w200' placeholder='定价CPC价格'></el-input>&nbsp;&nbsp;&nbsp;次/点击
							</el-form-item>
							<el-form-item style='background: #f5f5f5;height: 80px;padding-top: 20px;'>
								<el-button size='large' @click="submit()" style='background: #32c5d2;color: white;'>提交</el-button>
							</el-form-item>
						</el-form>
						<!--日历部分-->
					</div>
				</div>
			</div>
		</transition>
	</div>
</template>

<script>
import Event from 'event';
import http from 'utils/http';
import apiConfig from '../../../../config/api.config.js';
export default {
	name: 'app-advert-stock',
	props: ['showCpcPrice','advertisementId'],
	data() {
		return {
			rules: {
				cpcPrice: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						}else {
							callback();
						}
					},
					trigger: 'blur'
				}]
			},
			Data: {
				cpcPrice: ''
			},
			summary: {
				name: ''
			}
		};
	},
	methods: {
		closeView() {
			Event.$emit('close-Cpc-price');
		},
		getSummary() {
	    	var summary = this.summary;
	    	//获取单个广告位的详情
	    	http.get("/api/advertisement", {
	            params: {
	                advertisementId: this.advertisementId
	            }
	        })
	        .then(function(res) {
	      	    if (res.data.code == 200) {
	      	    	summary.name = res.data.data.name;
	      	    }
	        })
	        .catch(function(error) {
	            console.log(error);
	        });
	   },
		submit() {
			var vm = this;
			http.post('/api/price/cpc', {
				advertisementId: vm.advertisementId,
				cpcPriceFloat: Number(vm.Data.cpcPrice)
			})
			.then(function(res) {
				if (res.data.code == 200) {
						vm.$message({
						message: res.data.msg,
						type: 'success',
						duration: 3000
					});
					vm.Data.cpcPrice = '';
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		}
	},
	watch: {
		'showCpcPrice' : function() {
			if (this.showCpcPrice) {
				this.getSummary();
			}
		}
	}
};
</script>

<style>
</style>