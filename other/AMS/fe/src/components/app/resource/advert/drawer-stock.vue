<template>
	<div>
		<transition name="fade">
			<div style="z-index:1200;" class="master" v-show="showStock" @click="closeView">></div>
		</transition>
		<transition name="drawer">
			<div class="ams-stock" v-show="showStock" >
				<div class="drawer-part">
				 	<h2 class="part-header" style="margin-bottom:10px;">
						<span class="header-text">库存</span>
						<span class="header-fn" @click="closeView"><em class="icon icon-close"></em></span>
					</h2>
					<div class="shop-cont">
						<el-form :model="Data" :rules="rules" ref="Data" label-width="166px">
							<el-form-item label="广告位名称：">
								<span>{{summary.name}}</span>
							</el-form-item>
							<el-form-item label="轮播数：">
								<span>{{summary.carouselNum}}</span>
							</el-form-item>
							<el-form-item label="CPD库存：">
								<input placeholder='CPD库存' onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="Data.cpdAmount.length==1?Data.cpdAmount=Data.cpdAmount.replace(/[^1-9]/g,''):Data.cpdAmount=Data.cpdAmount.replace(/\D/g,'')" v-model="Data.cpdAmount" class='w200 jugeInput'>
							</el-form-item>
							<el-form-item label="运营CPD库存：">
								<input placeholder='运营CPD库存' onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="Data.limitedCpdAmount.length==1?Data.limitedCpdAmount=Data.limitedCpdAmount.replace(/[^1-9]/g,''):Data.limitedCpdAmount=Data.limitedCpdAmount.replace(/\D/g,'')" v-model="Data.limitedCpdAmount" class='w200 jugeInput'>
							</el-form-item>
							<el-form-item label="CPM库存：">
								<input placeholder='CPM库存' onblur="this.style.borderColor = '#BFCBD9'" onfocus="this.style.borderColor = '#20A0FF'" @keyup="Data.cpmAmount.length==1?Data.cpmAmount=Data.cpmAmount.replace(/[^1-9]/g,''):Data.cpmAmount=Data.cpmAmount.replace(/\D/g,'')" v-model="Data.cpmAmount" class='w200 jugeInput'>
							</el-form-item>
							<el-form-item style='background: #f5f5f5;height: 80px;padding-top: 20px;'>
								<el-button size='large' @click="submit()" style='background: #32c5d2;color: white;'>提交</el-button>
							</el-form-item>
						</el-form>
						
						<!--日历部分-->
						<div style="padding:20px 20px 0 90px;">
							<el-button type="default" icon="arrow-left" class='w80' @click='prev'></el-button>
							<el-button type="default" class='w80' @click='next'><i class="el-icon-arrow-right el-icon--right"></i></el-button>
							<p style="color: #ed6b75;display: inline-block;float:right;margin-left:20px;">CPM</p><p style="color: #337ab7;display: inline-block;float:right;">CPD</p>
						</div>
						<div id="calendar" class="pt-20">
							<div v-for='(item,index) in formData'>
								<div style="width:430px;height:450px;display: inline-block;margin: 0px 0px 0px 20px;float:left;">
									<table border="0" cellpadding="0" cellspacing="0">
										<thead>
											<tr><td id="monthTop" data-item="1" @click='selectAll(item.thisMonth)' class="month-title" :class="item.jugeCursorHead?'':'cursor-pointer'" colspan="8" align="center" :data-year="item.thisYear" :data-month="item.thisMonth">{{item.thisYear}}年{{item.thisMonth}}月</td></tr>
											<tr>
												<td style="border: 1px solid #ccc"></td><td data-item="1" id="monthTitle" @click='monthTitleClick(1,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" data-year="" :data-month="item.thisMonth" data-weekday="1">一</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(2,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="2">二</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(3,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="3">三</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(4,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="4">四</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(5,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="5">五</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(6,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="6">六</td>
												<td data-item="1" id="monthTitle" @click='monthTitleClick(7,item.thisMonth,$event)' :data-week="item.weekOfYear" :class="item.jugeCursorHead?'':'cursor-pointer'" class="month-header" :data-year="item.thisYear" :data-month="item.thisMonth" data-weekday="7">日</td>
											</tr>
										</thead>
										<tbody>
											<tr v-for='(data,index) in item.dayOfMonth'>
												<td :data-weekday="dayWeekDay" data-item="1" id="monthLeft" @click='monthLeft($event)'class="month-left cursor-pointer" :data-year="item.thisYear" :data-month="item.thisMonth" :data-week="item.weekOfYear[index]">{{item.weekOfYear[index]}}</td>
												<td @click='monthOne($event)' v-for='(dataIn,index2) in data' :class="item.month[index][index2]!=item.thisMonth || item.dayMonthBefore[index][index2] == 'true'?'month-item-disable':'month-item'" :data-year="item.thisYear" :data-month="item.thisMonth" :data-day="item.dayOfMonth[index][index2]" :data-week="item.weekOfYear[index]" :data-weekday="index2+1" data-item="1" :id="item.thisYear+'-'+item.month[index][index2]+'-'+item.dayOfMonth[index][index2]">
													<div class="date-content">
														<span style="float: right;">{{dataIn}}</span><br>
														<span date-number='1' class="date-number date-cpd" :id="item.month[index][index2] == item.thisMonth?'cpd-'+item.thisYear+'-'+item.month[index][index2]+'-'+item.dayOfMonth[index][index2]:''"></span><br>
														<span date-number='1' class="date-number date-cpm" :id="item.month[index][index2] == item.thisMonth?'cpm-'+item.thisYear+'-'+item.month[index][index2]+'-'+item.dayOfMonth[index][index2]:''"></span>
													</div>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
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
import moment from 'moment/moment.js';
export default {
	name: 'app-advert-stock',
	props: ['showStock','slotId'],
	data() {
		return {
			summary: {
				name: '',
				carouselNum: '',
				saleMode: ''
			},
			monthTable: '',
			formData: [],
			Data: {
				cpdAmount: '',
				cpmAmount: '',
				limitedCpdAmount: ''
			},
			time: moment(),
			rules: {
				cpdAmount: [{
					validator: function(rule, value, callback) {
						if (!/^$|^\d+$/.test(value)) {
							callback(new Error('只能输入正整数'));
						}else {
							callback();
						}
					},
					trigger: 'blur'
				}],
				cpmAmount: [{
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
			allTime: [],
			list: [],
			rowNum: '',
			monthLeftArr: [],
			monthTitle: [],
			dayWeekDay: [1,2,3,4,5,6,7],
			monthAll: [],
			selectedDateRangeArray: []
		};
	},
	created() {
    	this.buildCalendar('init', this.time);
	},
	methods: {
		closeView() {
			Event.$emit('close-stock');
			this.clearAll();
		},
		getSummary() {
	    	var summary = this.summary;
	    	//获取单个广告位的详情
	    	http.get("/api/slot", {
	            params: {
	                slotId: this.slotId
	            }
	        })
	        .then(function(res) {
	      	    if (res.data.code == 200) {
	      	    	summary.name = res.data.data.name;
	      	    	summary.carouselNum = res.data.data.rotators;
	      	    }
	        })
	        .catch(function(error) {
	            console.log(error);
	        });
	    },
	    submit() {
	    	this.$refs.Data.validate((result) => {
				if (result) {
					var vm = this;
					var compareArr = [];
					var arr = [];
					vm.selectedDateRangeArray = [];
					var allMonthArr = document.querySelectorAll('[class="month-item selected"]');
					for (var i = 0;i < allMonthArr.length;i++) {
						compareArr.push(allMonthArr[i].getAttribute("id"));
					}
					this.getDates(compareArr);
					if (vm.Data.cpdAmount == 0 && vm.Data.cpmAmount == 0 && vm.Data.limitedCpdAmount == 0) {
						vm.$message({
				            message: '添加库存失败,请填写有效的CPD和CPM的库存!',
				            type: 'warning'
				        });
					} else if (vm.selectedDateRangeArray.length == 0) {
						vm.$message({
				            message: '添加库存失败！请选择时间!',
				            type: 'warning'
				        });
					} else {
						http.post("/api/inventory", {
							slotId: vm.slotId,
							dates: vm.selectedDateRangeArray,
							cpdAmount: vm.Data.cpdAmount == '' ? 0 : Number(vm.Data.cpdAmount),
							cpmAmount: vm.Data.cpmAmount == '' ? 0 : Number(vm.Data.cpmAmount),
							limitedCpdAmount: vm.Data.limitedCpdAmount == '' ? 0 : Number(vm.Data.limitedCpdAmount)
						})
						.then(function(res) {
							if (res.data.code == 200) {
								vm.$message({
									message: '添加库存成功',
									type: 'success',
									duration: 3000
								});
								vm.Data.cpdAmount = '';
								vm.Data.cpmAmount = '';
								vm.Data.limitedCpdAmount = '';
								var allClassName = document.querySelectorAll('[data-item="1"]');
						    	for (var i = 0;i<allClassName.length;i++) {
						    		allClassName[i].className = allClassName[i].className.replace(" selected","");
						    		allClassName[i].setAttribute("class",allClassName[i].className);
						    	}
						    	vm.getList();
							}
						})
						.catch(function(error) {
							alert("fail");
						});
					}
				}
			});
	    },
	    getDates(compareArr) {
			var sortSelectedDates = compareArr.sort();
			var selectedDateRangeArray = new Array();
	
			for (var index = 0, begin = 0; index < sortSelectedDates.length; index++) {
				if (moment(sortSelectedDates[index]).add(1, 'days').isSame(moment(sortSelectedDates[index + 1]))) {
	
				} else if (!moment(sortSelectedDates[index]).subtract(1, 'days')
								.isSame(moment(sortSelectedDates[index - 1]))) {
					selectedDateRangeArray.push(sortSelectedDates[index]);
					begin = index + 1;
				} else {
					selectedDateRangeArray.push(sortSelectedDates[begin] + '~' + sortSelectedDates[index]);
					begin = index + 1;
				}
			}
	
			this.selectedDateRangeArray = selectedDateRangeArray;
		},
	    getList() {
	    	var vm = this;
	    	var prevMonth = moment(new Date(new Date(vm.allTime[3]).getTime()-86400000)).format("MM");
	    	var nowMonth = moment(new Date(new Date(vm.allTime[3]).getTime())).format("MM");
	    	var continueMonth;
	    	if (prevMonth == nowMonth) {
	    		continueMonth = nowMonth;
	    	}
	    	console.log(continueMonth);
	    	http.get("/api/inventories", {
	    		params: {
	    			slotId: vm.slotId,
	    			startTime: new Date(vm.allTime[0]).getTime(),
	    			endTime: new Date(vm.allTime[3]).getTime()-86400000
	    		}
	    	})
	    	.then(function(res) {
	    		if (res.data.code == 200) {
	    			var data = res.data.data;
	    			vm.list = data;
	    			for (var i in vm.list) {
	    				if (moment(new Date(vm.list[i].time)).format("MM") == continueMonth){
	    					continue;
	    				}
	    				document.getElementById("cpd-"+moment(new Date(vm.list[i].time)).format("YYYY-MM-DD")).innerHTML = vm.list[i].limitedCpdAmount+'/'+vm.list[i].cpdAmount;
	    				document.getElementById("cpm-"+moment(new Date(vm.list[i].time)).format("YYYY-MM-DD")).innerHTML = vm.list[i].cpmAmount;
	    			}
	    		}
	    	})
	    	.catch(function(error) {
	    		console.log(error);
	    	});
	    },
	    prev() {
	    	this.clearAll();
	   		this.buildCalendar('prev',this.time);
	   		this.getList();
	    },
	    next() {
	    	this.clearAll();
	    	this.buildCalendar('next',this.time);
	    	this.getList();
	    },
	    clearAll() {
	    	var allClassName = document.querySelectorAll('[data-item="1"]');
	    	var spanArr = document.querySelectorAll('[date-number="1"]');
	    	this.Data.cpdAmount = '';
			this.Data.cpmAmount = '';
			this.Data.limitedCpdAmount = '';
	    	for (var i = 0;i<spanArr.length;i++) {
	    		spanArr[i].innerHTML = '';
	    	}
	    	for (var i = 0;i<allClassName.length;i++) {
	    		allClassName[i].className = allClassName[i].className.replace(" selected","");
	    		allClassName[i].setAttribute("class",allClassName[i].className);
	    	}
	    },
	    allCheck(event) {
	    	var month = event.currentTarget.getAttribute('data-month');
	    	this.monthAll = document.querySelectorAll('[data-month="' + month + '"][class="month-item selected"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][class="month-item-disable"]');
    		if (this.monthAll.length+disableArr.length >= 35 && this.monthAll.length > 0) {
    			var current = document.querySelectorAll('[data-month="' + month + '"][id="monthTop"]')[0];
    			current.className = current.className.concat(" selected");
	    		current.setAttribute("class",current.className);
	    		this.monthAll = [];
    		} else {
    			var current = document.querySelectorAll('[data-month="' + month + '"][id="monthTop"]')[0];
    			current.className = current.className.replace(" selected","");
    			current.setAttribute("class",current.className);
    		}
	    },
	    rowCheck(event) {
	    	//点击单个选中横行
	    	var dataWeek = event.currentTarget.getAttribute('data-week');
	    	var month = event.currentTarget.getAttribute('data-month');
	    	this.monthLeftArr[dataWeek] = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"][class="month-item selected"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"][class="month-item-disable"]');
    		if (this.monthLeftArr[dataWeek].length+disableArr.length == 7 && this.monthLeftArr[dataWeek].length>0) {
    			var current = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"][id="monthLeft"]')[0];
    			current.className = current.className.concat(" selected");
	    		current.setAttribute("class",current.className);
	    		this.monthLeftArr[dataWeek] = [];
    		} else {
    			var current = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"][id="monthLeft"]')[0];
    			current.className = current.className.replace(" selected","");
    			current.setAttribute("class",current.className);
    		}
	    },
	    lineRowCheck(event) {
	    	//点击单个选中横行
	    	var dataWeek = event.currentTarget.getAttribute('data-week');
	    	var arr = dataWeek.split(",");
	    	for (var z = 0;z<arr.length;z++) {
			    	var month = event.currentTarget.getAttribute('data-month');
			    	this.monthLeftArr[arr[z]] = document.querySelectorAll('[data-month="' + month + '"][data-week="' + arr[z] + '"][class="month-item selected"]');
			    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-week="' + arr[z] + '"][class="month-item-disable"]');
		  		if (this.monthLeftArr[arr[z]].length+disableArr.length == 7 && this.monthLeftArr[arr[z]].length>0) {
		  			var current = document.querySelectorAll('[data-month="' + month + '"][data-week="' + arr[z] + '"][id="monthLeft"]')[0];
		  			if (current.className.indexOf("selected") == -1) {
			  			current.className = current.className.concat(" selected");
				    	current.setAttribute("class",current.className);
				    	this.monthLeftArr[arr[z]] = [];
			    	}
		  		} else {
		  			var current = document.querySelectorAll('[data-month="' + month + '"][data-week="' + arr[z] + '"][id="monthLeft"]')[0];
		  			current.className = current.className.replace(" selected","");
		  			current.setAttribute("class",current.className);
		  		}
			}
	    },
	    lineCheck(event) {
	    	//点击单个选中竖行
    		var week = event.currentTarget.getAttribute('data-weekday');
	    	var month = event.currentTarget.getAttribute('data-month');
	    	this.monthTitle[week] = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"][class="month-item selected"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"][class="month-item-disable"]');
    		if (this.monthTitle[week].length+disableArr.length >= 5 && this.monthTitle[week].length > 0) {
    			var current = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"][id="monthTitle"]')[0];
    			current.className = current.className.concat(" selected");
	    		current.setAttribute("class",current.className);
	    		this.monthTitle[week] = [];
    		} else {
    			var current = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"][id="monthTitle"]')[0];
    			current.className = current.className.replace(" selected","");
    			current.setAttribute("class",current.className);
    		}
	    },
	    rowLineCheck(event) {
	    	//点击单个选中竖行
    		var week = event.currentTarget.getAttribute('data-weekday');
    		var arr = week.split(",");
    		for (var z = 0;z<arr.length;z++) {
    			var month = event.currentTarget.getAttribute('data-month');
		    	this.monthTitle[arr[z]] = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + arr[z] + '"][class="month-item selected"]');
		    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + arr[z] + '"][class="month-item-disable"]');
	    		if (this.monthTitle[arr[z]].length+disableArr.length >= 5 && this.monthTitle[arr[z]].length>0) {
	    			var current = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + arr[z] + '"][id="monthTitle"]')[0];
	    			if (current.className.indexOf("selected") == -1) {
		    			current.className = current.className.concat(" selected");
			    		current.setAttribute("class",current.className);
			    		this.monthTitle[arr[z]] = [];
		    		}
	    		} else {
	    			var current = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + arr[z] + '"][id="monthTitle"]')[0];
	    			current.className = current.className.replace(" selected","");
	    			current.setAttribute("class",current.className);
	    		}
    		}
	    },
	    monthOne(event) {
	    	//单个格选中
	    	var className = event.currentTarget.className;
	    	if (className == 'month-item' || className == 'month-item selected') {
	    		if (className == 'month-item') {
	    			className = className.concat(" selected");
	    			event.currentTarget.setAttribute("class",className);
	    		} else {
	    			className = className.replace(" selected","");
	    			event.currentTarget.setAttribute("class",className);
	    		}
	    	}
	    	this.rowCheck(event);
	    	this.lineCheck(event);
	    	this.allCheck(event);
	    },
	    monthLeft(event) {
	    	//行选中
	    	var dataWeek = event.currentTarget.getAttribute('data-week');
	    	var month = event.currentTarget.getAttribute('data-month');
	    	var monthLeftArr = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-week="' + dataWeek + '"][class="month-item-disable"]');
	    	var open = event.currentTarget.className.indexOf('selected');
    		for (var i = 0; i < monthLeftArr.length; i++) {
    			if (open == -1) {
    				if (monthLeftArr[i].className.indexOf('selected') == -1 && monthLeftArr[i].className.indexOf('month-item-disable') == -1 && disableArr.length !=7) {
		    			monthLeftArr[i].className = monthLeftArr[i].className.concat(" selected");
		    			monthLeftArr[i].setAttribute("class",monthLeftArr[i].className);
		    		}
	    		} else {
	    			monthLeftArr[i].className = monthLeftArr[i].className.replace(" selected","");
	    			monthLeftArr[i].setAttribute("class",monthLeftArr[i].className);
	    		}
	    	}
    		this.rowLineCheck(event);
    		this.allCheck(event);
	    },
	    monthTitleClick(week,month,event) {
	    	//列选中
	    	var monthTitleArr = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][data-weekday="' + week + '"][class="month-item-disable"]');
	    	var open = monthTitleArr[0].className.indexOf('selected');
    		for (var i = 0; i < monthTitleArr.length; i++) {
    			if (open == -1) {
    				if (monthTitleArr[i].className.indexOf('selected') == -1 && monthTitleArr[i].className.indexOf('month-item-disable') == -1 && disableArr.length<5) {
		    			monthTitleArr[i].className = monthTitleArr[i].className.concat(" selected");
		    			monthTitleArr[i].setAttribute("class",monthTitleArr[i].className);
		    		}
	    		} else {
	    			monthTitleArr[i].className = monthTitleArr[i].className.replace(" selected","");
	    			monthTitleArr[i].setAttribute("class",monthTitleArr[i].className);
	    		}
	    	}
    		this.lineRowCheck(event);
    		this.allCheck(event);
	    },
	    selectAll(month) {
	    	//选中所有
	    	var allArr = document.querySelectorAll('[data-month="' + month + '"]');
	    	var disableArr = document.querySelectorAll('[data-month="' + month + '"][class="month-item-disable"]');
	    	var open = allArr[0].className.indexOf('selected');
	    	for (var i = 0; i < allArr.length; i++) {
    			if (open == -1) {
    				if (allArr[i].className.indexOf('selected') == -1 && allArr[i].className.indexOf('month-item-disable') == -1 && disableArr.length < 35) {
		    			allArr[i].className = allArr[i].className.concat(" selected");
		    			allArr[i].setAttribute("class",allArr[i].className);
		    		}
	    		} else {
	    			allArr[i].className = allArr[i].className.replace(" selected","");
	    			allArr[i].setAttribute("class",allArr[i].className);
	    		}
	    	}
	    },
	    buildCalendar(operator,time) {
	    	this.formData = [];
	    	this.allTime = [];
	    	if (operator == 'init') {
				this.drawMonth(time);
				for (var i = 0; i < 1; i++) {
					this.drawMonth(time.add(1, "M"));
				}
			} else if (operator == 'next') {
				this.drawMonth(time);
				this.drawMonth(time.add(1, "M"));
			} else if (operator == 'prev') {
				this.drawMonth(time.subtract(2, "M"));
				for (var i = 0; i < 1; i++) {
					this.drawMonth(time.add(1, "M"));
				}
			}
			this.time = time;//赋值给当前的时间点
	    },
	    drawMonth(monthMoment) {
	    	var oCalendar = {
	    		thisYear: '',
	    		thisMonth: '',
	    		weekOfYear: [],
	    		dayOfMonth: [],
	    		month: [],
	    		dayMonthBefore: [],
	    		jugeCursorHead: true
	    	};
	    	//当前年
			oCalendar.thisYear = monthMoment.format("YYYY");
			oCalendar.thisMonth = monthMoment.format("MM");
			oCalendar.jugeCursorHead = monthMoment.isBefore(moment(), "month");
			var thisYear = monthMoment.format("YYYY");
			//当前月
			var thisMonth = monthMoment.format("MM");
			//当前月第一天
			var startMoment = moment(monthMoment).startOf("month").startOf("isoWeek");
			this.allTime.push(moment(startMoment).format("YYYY-MM-DD"));
			//下月第一天
			var endMoment = moment(monthMoment).endOf("month").endOf("isoWeek").add(1, "d");
			this.allTime.push(moment(endMoment).format("YYYY-MM-DD"));
			var thisMoment = moment(startMoment);
			var str = '';
			var str2 = '';
			var str3 = '';
			while (thisMoment.isBefore(endMoment, "day")) {
				//年
				var year = thisMoment.format("YYYY");
				//月
				var month = thisMoment.format("MM");
				//日
				var dayOfMonth = thisMoment.format("DD");
				
				//星期
				var dayOfWeek = thisMoment.format("E");
				//一年中的第几个星期
				var weekOfYear = thisMoment.format("W");
				
				var disabled = moment().isAfter(thisMoment);
				
				if (dayOfWeek == 1) {
					oCalendar.weekOfYear.push(thisMoment.format("W"));
					str += "";
					str2 += "";
					str3 += "";
				}
				str += thisMoment.format("DD") + '.';
				str2 += thisMoment.format("MM") + '.';
				str3 += disabled + '.';
				if (dayOfWeek == 7) {
					str += ",";
					str2 += ",";
					str3 += ",";
				}
				thisMoment.add(1, "d");
			}
			var arr;
			var arr2;
			var arr3;
			for(var i = 0;i<str.split(",").length;i++) {
				arr = str.split(",")[i].split(".");
				arr.pop();
				if (i != str.split(",").length-1){
					oCalendar.dayOfMonth.push(arr);
				}
			}
			for(var i = 0;i<str2.split(",").length;i++) {
				arr2 = str2.split(",")[i].split(".");
				arr2.pop();
				if (i != str2.split(",").length-1){
					oCalendar.month.push(arr2);
				}
			}
			for(var i = 0;i<str3.split(",").length;i++) {
				arr3 = str3.split(",")[i].split(".");
				arr3.pop();
				if (i != str3.split(",").length-1){
					oCalendar.dayMonthBefore.push(arr3);
				}
			}
			this.formData.push(oCalendar);
	    }
	},
	mounted() {
		
	},
	watch: {
		'showStock' : function() {
			if (this.showStock) {
				this.getSummary();
				this.getList();
			}
		}
	}
};
</script>