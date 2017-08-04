<template>
	<div class="amp-drawer" style="padding-bottom:0px">
		<div class="drawer-part">
			<h2 class="part-header"><span class="header-text">预览</span><span class="header-fn"><em
				@click="closePreview()" class="icon icon-close"></em></span></h2>
			<div class="questionPreviewBg">
				<!--<iframe :srcdoc="dataHtml.html" width="360" height="708" frameborder="0"></iframe>-->
				<iframe :srcdoc="dataHtml.html" width="280" height="483" frameborder="0" class="questioniframe"></iframe>
			</div>
		</div>

	</div>
</template>
<script>
	import store from 'store';
	import actions from 'actions';
	import Event from 'event';
	import Http from 'utils/http';

	export default {
		name:'app-survey-preview',
		props:['surveyId'],
		data(){
			return {
				dataHtml:{}
			}
		},
		computed: {
			config: () => store.state.drawerCtrl
		},
		created(){
			this.getList();
		},
		methods:{
			closePreview() {
				Event.$emit('close-preview');
			},
			getList() {
				let vm = this;
				Http.get("/api/survey/preview", {
					params:{
						surveyId: this.surveyId
					}
				})
					.then(function(res){
						if(res.data.code == 200){
							vm.dataHtml = res.data.data;
						}
					})
					.catch(function(error){
						console.log(error);
					});
			}
		}
	}
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
