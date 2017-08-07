<template>
	<div class="dialogModel">
		<div class="modal">
			<div class="modal-dialog">
				<div class="modal-content">
					<!--头部-->
					<div class="modal-header" v-show="model.showHead">
						{{model.title}}
					</div>
					<!--内容区域-->
					<div class="modal-body">
						<div class="modalText" v-html="model.text"></div>
					</div>
					<!--尾部,操作按钮-->
					<div class="modal-footer" v-show="model.showDefaultButton">
						<a @click="update" class="defaultBtn">我知道了</a>
					</div>
				</div>
			</div>
			<div v-show="model.showButton" class="specBtn">
				<img @click='closeModel' src="../../../assets/images/gift-close.png">
			</div>
		</div>
		<div class="mask"></div>
	</div>
</template>

<script>
	import Event from 'event';
	export default {
		name: 'app-dialog-model',
		props: ['model'],
		data() {
			return {
			};
		},
		methods: {
			update() {
				Event.$emit('getList');
				this.closeModel();
			},
			closeModel() {
				Event.$emit('close-model');
			},
		}
	};
</script>
<style lang="less">
	.specBtn{
		position: absolute;
		width:100%;
		img{
			display: block;
			width:.84rem;
			height: .84rem;
			margin: .3rem auto 0;
		}
	}
	.modal {
		background: #fff;
		width: 90%;
		position: fixed;
		left: 50%;
		top: 50%;
		transform: translate(-50%,-50%);
		z-index:1001;
		border-radius:0.2rem;
		max-height:7rem;
		.modal-dialog{
			padding: .52rem .36rem;
			.modal-header{
				line-height: .3rem;
				width:100%;
				text-align: center;
				font-size: .32rem;
				color: #333;
				margin: 0 0 .3rem 0;

			}
			.modal-body{
				overflow:hidden;
				.modalText{
					min-height: 2rem;
					max-height: 5.2rem;
					overflow: auto;
					font-size: .3rem;
					line-height: .4rem;
					color:#333;
					word-wrap: break-word;
					font-size: .26rem;
					color: #666;
					img{
						width: 2rem;
						margin: 0 auto;
						display: block;
					}
					.modelDes{
						font-size: .36rem;
						color: #666;
						text-align: center;
						margin: .3rem 0 .4rem 0;
					}
				}
			}
			.modal-footer{
				width: 100%;
				text-align: center;
				min-height: .3rem;
				.defaultBtn{
					display: block;
					background: #ea5c5e;
					width:3rem;
					color: #fff;
					margin: .1rem auto;
					text-align: center;
					line-height: .8rem;
					border: none;
					border-radius: .5rem;
					font-size: .28rem;
				}
			}
		}

	}
	.mask {
		position: fixed;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		z-index: 1000;
		background: #23272c;
		opacity: 0.35;
		filter: alpha(opacity=35);
	}

</style>
