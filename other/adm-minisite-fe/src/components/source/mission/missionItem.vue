<template>
	<div class="task-item">
		<div class="item-head clearfix">
			<h2 class="item-head-text">{{taskItem.name}}</h2>
			<div class="item-head-help" @click="showMissionDes(taskItem.description)">任务说明</div>
		</div>
		<div class="task-tip clearfix">
			<p class="item-reward">奖励&nbsp;:&nbsp;<span>{{taskItem.reward_money}}</span>&nbsp;国美币</p>
			<p class="item-endtime">{{taskItem.last}}</p>
		</div>
		<div class="task-detial clearfix" v-for="pro in taskItem.processes">
			<div class="task-progress-container" :style="{ 'borderColor': pro.finish==100 ? '#4cbe7c' : '#e9c84f' }">
				<div class="task-progress-finish" :style="{width: pro.finish + '%','background': pro.finish==100 ? '#78d7a0' : '#fbe07f'}" ></div>
			</div>
			<div class="task-progress-count">{{pro.finished_num > pro.required_num ? pro.required_num : pro.finished_num}}/{{pro.required_num}}<span>{{pro.name}}</span></div>
			<!--<div class="task-progress-count">{{pro.finished_num}}/{{pro.required_num}}<span>{{pro.name}}</span></div>-->
		</div>
		<div class="task-reward-surplus clearfix" v-html="taskItem.left_reward_num == 0 ? '抢光啦！' : '剩余<span>'+taskItem.left_reward_num+'</span>份奖励'"></div>
		<div class="item-footer clearfix">
			<button v-show="taskItem.last !== '已结束'" class="task-action"  @click="receive($event)"
					:disabled="taskItem.receive_status == 1 || taskItem.left_reward_num == 0 || isUnfinished()">
				{{taskItem.receive_status == 1 ? '已领取' : (taskItem.left_reward_num == 0 && !isUnfinished() ? '未领取' : msg)}}
			</button>
		</div>
	</div>
</template>
<script>
	import Vue from 'vue';
	import http from 'utils/http';
	import Event from 'event';
	import jsonp from 'jsonp';
	import Img from '../../../assets/images/gift-none.png';
	require('utils/jquery-1.8.3.min.js');
	const JSEncrypt = require('utils/jsencrypt.js');

	export default {
		name: 'task-item',
		props: ['taskItem', 'taskId', 'userId'],
		data() {
			return {
			    msg:'领取奖励',
				publicKey: 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCepOdTh59TgMZ1fOig7GZEyh2tc4KGikwMoUu0ZZeDHnNxr8p28TKvrJ/l64W1lH7QzVJY1N6FhLGDDdO1LZ6I+AdGLvA6XKNlwMiQmWc3+bAIqrFRNLvIFXLLIjHV2Io4aQ+EG7m0Y43whnDS9sp38nib6v1AKBZWyxXiKCD4CwIDAQAB'
			};
		},
		methods: {
			receive(e) {
			    let target = e.currentTarget;
			    let vm = this;
				const encrypt = new JSEncrypt.JSEncrypt(); // 实例化加密对象
				encrypt.setPublicKey(this.publicKey); // 设置公钥
				const p = 'uid=' + this.userId + '&mid=' + this.taskId + '&csd=' + this.taskItem.cycle_start_date + '&ced=' + this.taskItem.cycle_end_date;
				const encryptoPasswd = encrypt.encrypt(p); // 加密明文
				const encodePasswd = encodeURIComponent(encryptoPasswd);
				let url = $CONFIG['missionDoMain'] + 'missionReceive?p=' + encodePasswd;
				jsonp(url, null, function(err, data) {
					if (data.message == 'success') {
						Event.$emit('refresh');
					} else if (data.message == 'failed') {
						Event.$emit('open-model', {options: {
							title:'',
							text: '<img src=' + Img + '>' +
							'<p class="modelDes">奖励被领光啦~</p>',
							showHead:false,
							showDefaultButton:true,
							showButton:false
						}});
					}
				});
				/*http.get($CONFIG['domain'] + 'missionReceive', {
					params: {
						uid: this.userId,
						mid: this.taskId,
						csd: this.taskItem.cycle_start_date, //任务周期开始时间戳
						ced: this.taskItem.cycle_end_date,   //任务周期结束时间戳
					}
				}).then((res) => {
					if (res.data.message === 'success') {
						target.setAttribute('disabled', true);
						this.taskItem.receive_status = 1;
						this.taskItem.left_reward_num--;
					} else if (res.data.message === 'failed') {
						Event.$emit('open-model', {options: {
						    title:'',
							text: '<img src=' + Img + '>' +
							'<p class="modelDes">奖励被领光啦~</p>',
							showHead:false,
							showDefaultButton:true,
							showButton:false
						}});
					}
				}).catch((err) => {
					console.log(err);
				});*/
			},
			isUnfinished() {
			    let pro = this.taskItem.processes;
				if (pro.length == 1) {
				    return pro[0].required_num > pro[0].finished_num ; //true:没完成任务  false：完成任务
				}else {
				    let isFinish = false;
					for (let i = 0; i < pro.length; i++) {
						if (pro[i].required_num > pro[i].finished_num) {
							isFinish = true;
							break;
						}
					}
					return isFinish;
				}
			},
			showMissionDes(content) {
				Event.$emit('open-model', {options: {
					title:'任务说明',
					text:content,
					showHead:true,
					showDefaultButton:false,
					showButton:true
				}});
			}
		}
	};
</script>
