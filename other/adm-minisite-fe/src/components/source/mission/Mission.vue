<template>
	<div class="scrollload-container inventory_list main">
		<div class="scrollload-content">
			<m-item v-for="(item,index) in taskList" :key="item.mid" :taskItem="item" :taskId="item.mid" :userId="userId"></m-item>
		</div>
		<transition name="model">
			<m-dialog v-show="showDialog" :model="modelOptions"></m-dialog>
		</transition>
	</div>
</template>
<script>
	import http from 'utils/http';
	import jsonp from 'jsonp';
	import actions from 'actions';
	import moment from 'moment';
	import Event from 'event';
	import Scrollload from 'scrollload';
	import TaskItem from './missionItem.vue';
	import DialogModel from './dialogModel.vue';
	import {getUrlInfoWithDeviceId, setStyles} from 'utils/common';
	import {traditionalMovingHtml, traditionalLoadingHtml, noMoreDataHtml} from 'utils/initScrollload';
	require('../../../assets/js/appInterface.js');
	export default {
		name: 'app-task',
		data() {
			return {
				loading: false,
				ids: [],
				pageSize: 10,
				taskList: [],
				showDialog: false,
				modelOptions: {},
				dialogMove: false,
				scrollTop: 0,
				userId: '',
				cScrollload: {},
				loadNumber: 0,
			};
		},
		components: {
			'm-item': TaskItem,
			'm-dialog': DialogModel
		},
		updated() {
			if (this.cScrollload.isLock) {
				this.cScrollload.unLock();
			}
			if (this.loading) {
			    this.loading = false;
			}
			if(this.showDialog) {
			    this.isPullRefresh = false;
			}
			if (this.ids.length > 0 && !this.cScrollload.hasMoreData) {
				this.cScrollload.refreshData();
			}
		},
		mounted() {
			let vm = this;
			Event.$off('open-model');
			Event.$on('open-model', (avg) => {
				document.getElementsByClassName('main')[0].style.overflow = 'hidden';
				vm.cScrollload.isRefreshing = true;
				vm.showDialog = true;
				vm.modelOptions = avg.options;
			});
			Event.$on('close-model', () => {
				vm.showDialog = false;
				document.getElementsByClassName('main')[0].setAttribute('style', 'overflow:auto');
				vm.cScrollload.isRefreshing = false;
			});
			Event.$on('getList', () => {
				vm.getList(vm.pageSize);
			});
			Event.$on('refresh', () => {
				vm.cScrollload.triggerPullResfresh();
			});
			vm.cScrollload = new Scrollload({
				loadingHtml: traditionalLoadingHtml,
				notEnoughRefreshPortHtml: traditionalMovingHtml,
				overRefreshPortHtml: traditionalMovingHtml,
				refreshingHtml: traditionalLoadingHtml,
				noMoreDataHtml: noMoreDataHtml,
				container: document.querySelector('.scrollload-container'),
				content: document.querySelector('.scrollload-content'),
				window: document.querySelector('.inventory_list'),
				threshold: 50,
				enableLoadMore: true,
				enablePullRefresh: true,
				loadMore: vm.infinite,
				isInitLock: true,
				useLocalScrollFix: true,
				pullRefresh: vm.refresh,
				arrivedRefreshPortHandler(sl) {
					// 强制浏览器重绘
					const scrollloadMovingDom1 = sl.notEnoughRefreshPortDom.querySelector('.scrollload-movingHtml');
					const scrollloadMovingDom2 = sl.overRefreshPortDom.querySelector('.scrollload-movingHtml');
					if (sl.isMovingDown) {
						setStyles([scrollloadMovingDom1, scrollloadMovingDom2], {transform: 'rotate(180deg) translate3d(0,0,0)'});
					} else {
						setStyles([scrollloadMovingDom1, scrollloadMovingDom2], {transform: 'rotate(0deg) translate3d(0,0,0)'});
					}
				},
				touchEnd(sl) {
					const scrollloadMovingDom1 = sl.notEnoughRefreshPortDom.querySelector('.scrollload-movingHtml');
					const scrollloadMovingDom2 = sl.overRefreshPortDom.querySelector('.scrollload-movingHtml');
					setStyles([scrollloadMovingDom1, scrollloadMovingDom2], {transform: 'rotate(0deg) translate3d(0,0,0)'});
				}
			});
			vm.cScrollload.triggerPullResfresh();
		},
		methods: {
			refresh() {
				let vm = this;
				if(vm.userId == '') {
					AppInterface.call('/common/getLoginStatus', function(data) {
						if (data.success) {
							vm.userId = data.data.userId + '';
							vm.getList(vm.pageSize);
						}
					});
				}else{
					vm.getList(vm.pageSize);
				}
			},
			infinite() {
				if (!this.loading && this.ids.length > 0) {
					this.getListLoading();
				} else {
					this.cScrollload.noMoreData();
					return;
				}
			},
			getList(count) {
				let vm = this;
				vm.loadNumber = 0;
				vm.cScrollload.refreshData();
				let url = $CONFIG['missionDoMain'] + 'missionList?uid=' + this.userId + '&count=' + count;
				jsonp(url, null, function(err, data) {
					vm.taskListHandler(data.data, true);
					vm.loadNumber ++;
					vm.cScrollload.refreshComplete();
				});
				/*http.get($CONFIG['domain'] + 'missionList', {
					params: {
						uid: '123',
						count: count
					}
				}).then((res) => {
					if (res.status === 200 && res.data.message === 'success') {
						vm.taskListHandler(res.data.data, true);
						vm.loadNumber++;
						vm.cScrollload.refreshComplete();
					}
				}).catch((err) => {
					console.log(err);
				});*/
			},
			taskListHandler(taskLists, isRefresh) {
				let vm = this;
				let taskList = taskLists.missions;
				if (isRefresh) {
					vm.ids = taskLists.ids;
					vm.taskList = [];
				}
				for (let i = 0; i < taskList.length; i++) {
					let item = taskList[i];
					item.reward_money = (item.reward_money * 0.01).toFixed(2);
					item.last = vm.getLastTimeString(item.cycle_end_date, item.end_date);
					let processes = [];
					for (let j = 0; j < item.processes.length; j++) {
						let pro = item.processes[j];
						pro.name = vm.getProcessName(pro.type);
						pro.finish = (pro.finished_num / pro.required_num) * 100 > 100 ? 100 : (pro.finished_num / pro.required_num) * 100;
						processes.push(pro);
					}
					item.processes = processes;
					vm.taskList.push(item);
				}
			},
			getListLoading() {
				let vm = this;
				vm.cScrollload.lock();
				if (vm.loading) return;
				vm.loading = true;
				if (vm.loadNumber > 5) {
					vm.cScrollload.noMoreData();
					return false;
				}
				vm.loadNumber++;
				let loadingIds = vm.ids.splice(0, 10);
				let url = $CONFIG['missionDoMain'] + 'missionList?uid=' + this.userId + '&ids=' + loadingIds.join();
				jsonp(url, null, function(err, data) {
					vm.taskListHandler(data.data, false);
					vm.cScrollload.unLock();
				});
				/*http.get($CONFIG['domain'] + 'missionList', {
					params: {
						uid: this.userId,
						ids: loadingIds.join()
					}
				}).then((res) => {
					if (res.status === 200 && res.data.message === 'success') {
						vm.taskListHandler(res.data.data, false);
						vm.cScrollload.unLock();
					}
				}).catch((err) => {
					console.log(err);
				});*/
			},
			getProcessName(type) {
				if (type === 0) {
					return '点赞';
				} else if (type === 1) {
					return '分享';
				} else if (type === 2) {
					return '调研';
				} else {
					return '视频';
				}
			},
			getLastTimeString(cycleEndTime, endTime) {
				let currentDate = moment(); //当前时间
				let endDate = moment(cycleEndTime);
				let sub = endDate.diff(currentDate);//和现在时间的时差
				let duration = moment.duration(sub);
				if (endTime < currentDate) {
					return '已结束';
				}
				// let subDays = endDate.diff(currentDate, 'days', true);
				// let subHours = endDate.diff(currentDate, '');
				let days = duration.days();
				let hours = duration.hours();
				let minutes = duration.minutes();
				let str = '';
				str += days > 0 ? days + '天' : '';
				str += hours > 0 ? hours + '小时' : '';
				str += minutes > 0 ? minutes + '分' : '';
				return str + '后结束';
			},
		}
	};
</script>
<style lang="less">
	@import '../../../assets/css/task.less';
</style>
