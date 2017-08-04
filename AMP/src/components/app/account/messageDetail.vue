<template>
	<div class="account-main">
	<div class="msg-detail">
		<h2 class="detail-header">{{message.title}}</h2>
		<div class="detail-time">{{message.createTime | moment('YYYY/MM/DD HH:mm:ss')}}</div>
		<div v-html="message.content" class="detail-infor">
			{{message.content}}
		</div>
	</div>
	</div>
</template>
<script type="text/javascript">
import http from "http";
import {formatDate} from 'utils/common';
export default {
	name: "app-account-message-detail",
	data(){
		return {
			messageId: this.$route.params.id,
			message: {}
		};
	},
	created(){
		this.getList();
	},
	methods: {
		getList() {
			var that = this;
			http.get("/api/message", {
				params: {
					messageId: this.messageId
				}
			})
			.then(function(res) {
				if (res.data.code == 200) {
					that.message = res.data.data;
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		}
	}
};

</script>
