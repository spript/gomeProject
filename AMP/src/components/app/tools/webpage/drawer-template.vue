<template>
	<div class="amp-form">
		<div class="form-column">
			<div class="fn-plan">
			<span class="plan-title">页面名称：</span>
				<el-input v-model="search.name" :maxlength="30" placeholder="请输入页面名称" style="width:170px;display:inline-block;">
				</el-input>
			</div>
			<div class="fn-plan" style="margin-top:20px;">
				<span class="plan-title">设备类型： </span>
				<el-select v-model="search.platform" style="width:170px;display:inline-block;" placeholder="APP">
					 <el-option v-for="item in platform.options" :label="item.label" :value="item.value"></el-option>
				</el-select>
			</div>
			<div style="margin-top:20px;" prop="image">
				<div v-if class="uploade-show uploade-select">
					<ul>
						<li @click.prevent.stop="selectImg(index)" v-for="(img, index) in data.list" :class="{'actived': img.checked}">
							<span class="show-img"><img :src="img.preview" alt=""></span>
							<span style="" class="selectImgTitle">
								{{img.pageTemplateDesc}}
							</span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
import store from 'store';
import actions from 'actions';
import Event from 'event';
import Http from 'http';

export default {
	name: 'app-page-template',
	data() {
		return {
			platform: {
				options: [{
					label: "无线",
					value: "1"
				}]
			},
			search: {
				platform: '1',
				name:'',
				pageTemplateId: ''
			},
			list: [],
			data: {},
			formData: {},
		};
	},
	created() {
		this.getTemplate();
	},
	mounted() {
		Event.$off('template-save');
		Event.$on('template-save', () => {
			if (this.search.name.length === 0) {
				this.$message({
					message: '请填写页面名称',
					type: 'error'
				});
			} else if (this.search.pageTemplateId === '') {
				this.$message({
					message: '请填选择模板',
					type: 'error'
				});
			} else {
				Event.$emit('template-save-result', this.search);
			}
		});
	},
	methods: {
		getTemplate() {
			let search = this.search, vm = this;
			Http.get('/api/pageTemplates', {
				params: {
					platform: search.platform ? search.platform : '1'
				}
			}) .then(function(res){
				if(res.data.code == 200){
					vm.data = res.data.data;
					vm.data.list.forEach((item) => {
						vm.$set(item, 'checked', false);
					});
				}
			}).catch(function(error){
				console.log(error);
			});
		},
		selectImg(index,id) {
			this.formData.image = this.data.list[index].preview;
			this.data.list.forEach((item) => {
				item.checked = false;
			});
			this.data.list[index].checked = true;
			this.search.pageTemplateId = this.data.list[index].pageTemplateId;
		}
	},
	watch: {
		'search.platform': function(val) {
			this.getTemplate();
			// Event.$emit('select-platform', this.search.platform);
		}
	}
};
</script>
<style scoped>
	.error {
		color: #ff5151;
	}

	.amp-form {
		margin: 0px 400px 0px;
	}

	.uploade-show ul li .show-img {
		position: relative;
		display: block;
		width: 130px;
		height: 240px;
		border: 1px solid #d9dbde;
		border-radius: 4px;
	}

	.uploade-show ul li {
		float: left;
		margin: 10px 10px 0px 0px;
		width: 140px;
		height: 240px;
	}

	.form-column .fn-plan .plan-title {
		display: inline-block;
		*display: inline;
		*zoom: 1;
		vertical-align: top;
		margin-right: 1px;
		height: 36px;
		line-height: 36px;
		font-size: 14px;
		color: #23272c;
	}

	.selectImgTitle {
		text-align: center;
		display: block;
		font-size: 12px;
	}
	.uploade-select ul li .show-img:after, .uploade-select ul li .show-img:before{
		bottom: 4px;
	}
</style>
