<template>
	<div >
		<div >
			<div class="questionPreviewBg">
				<iframe :srcdoc="dataHtml.html" width="280" height="483" frameborder="0" class="questioniframe"></iframe>
			</div>
		</div>
	</div>
</template>
<script>
import Event from 'event';
import Http from 'http';
export default {
	name:'app-survey-preview',
	props:['surveyId'],
	data(){
		return {
			dataHtml:{}
		};
	},
	created(){
		this.getList();
	},
	methods:{
		closePreview() {
			Event.$emit('close-view');
		},
		getList() {
			let vm = this;
			Http.get("/api/survey/preview", {
				params:{
					surveyId: this.surveyId
				}
			}).then(function(res){
				if(res.data.code == 200){
					vm.dataHtml = res.data.data;
					console.log(vm.dataHtml);
				}
			})
			.catch(function(error){
				console.log(error);
			});
		}
	}
};
</script>
<style>
	.questioniframe{
		margin: 82px 0 0 29px;
	}
	.questionPreviewBg{
		background-image:url('../../../../assets/img/iphonebg.png');
		background-repeat: no-repeat;
		background-size: 100%;
		width: 336px;
		height: 700px;
		margin: 0 auto;
	}
</style>
