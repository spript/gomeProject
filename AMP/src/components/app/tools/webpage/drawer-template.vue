<template>
	<el-form :model="formData" ref="formData" :rules="formRules" label-width="100px" >
		<div class="amp-form">
			<div class="form-column">
				<el-form-item label="页面名称：" prop="name">
					<el-input v-model="formData.name" placeholder="请输入页面名称" style="width:170px;display:inline-block;"></el-input>
				</el-form-item>
				<el-form-item label="设备类型：" prop="platform">无线</el-form-item>
				<el-form-item label="模板：" prop="pageTemplateId">
					<el-input v-model="formData.pageTemplateId" style="display: none"></el-input>
					<div v-if class="uploade-show uploade-select">
						<ul>
							<li @click.prevent.stop="selectImg(index)" v-for="(img, index) in data.list" :class="{'actived': img.checked}">
								<span class="show-img"><img :src="img.preview" alt=""></span>
								<span class="selectImgTitle" v-if="img.pageTemplateId == 1">
									<h3>模板一</h3><span>(适用于有腔调中间页)</span>
								</span>
								<span class="selectImgTitle" v-if="img.pageTemplateId == 2">
									<h3>模板二</h3><span>(适用于店铺中间页)</span>
								</span>
								<span class="selectImgTitle" v-if="img.pageTemplateId == 3">
									<h3>模板三</h3><span>(适用于视频类型)</span>
								</span>
								<span class="selectImgTitle" v-if="img.pageTemplateId == 4">
									<h3>模板四</h3><span>(适用于清单中间页)</span>
								</span>
							</span>
							</li>
						</ul>
					</div>
				</el-form-item>
			</div>
		</div>
	</el-form>
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
			formData:{
				name:'',
				platform: '1',
				pageTemplateId:''
			},
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
			formRules:{
				name: [{
					validator: function (rule, value, callback) {
						if (value.trim() == '') {
							callback(new Error('请输入页面名称'));
						}else {
							callback();
						}
					}, trigger: 'blur'
				}],
				pageTemplateId: [{
					validator: (rule, value, callback) => {
					    if (this.formData.pageTemplateId == '') {
					        callback(new Error('请选择模板'))
						} else {
					        callback();
						}
					}
				}],
			}
		};
	},
	created() {
		this.getTemplate();
	},
	mounted() {
	    Event.$off('template-save');
		Event.$on('template-save', () => {
			this.$refs.formData.validate((result) => {
			    if(result){
					Event.$emit('template-save-result', this.formData);
				}
			})
		});
	},
	methods: {
		trim(type) {
			this.formData[type] = this.formData[type].replace(/(^\s*)|(\s*$)/g, "");
		},
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
			this.data.list.forEach((item) => {
				item.checked = false;
			});
			this.data.list[index].checked = true;
			this.formData.pageTemplateId = this.data.list[index].pageTemplateId;
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
<style lang="less" scoped>
	.error {
		color: #ff5151;
	}
	.amp-form {
		width:545px;
		margin: 0 auto;
		label{
			wigth:140px;
		}
	}
	.uploade-show{
		padding-top:0;
	}
	.uploade-show ul li .show-img {
		position: relative;
		display: block;
		width: 130px;
		height: 240px;
		border: 1px solid #d9dbde;
		border-radius: 4px;
	}
	.uploade-show ul li img:hover{
		border:2px solid #d30312;
		box-sizing: border-box;
	}
	.uploade-show ul li {
		float: left;
		margin: 10px 20px 0px 0px;
		width: 128px;
		height: 300px;
		&:hover{
		 	box-sizing: border-box;
		}
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
    .selectImgTitle h3{
		font-size: 14px;
		color:#23272c;
		line-height: 1;
		margin: 10px 0 -3px 0;
	}
	.selectImgTitle p{
		color:#89919c;
		font-size: 12px;
	}
	.selectImgTitle {
		text-align: center;
		display: block;
		font-size: 12px;
	}
</style>
