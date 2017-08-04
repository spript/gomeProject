<template>
	<div>
		<div class="error-body">
			<div class="body-scaffold error-refuse"></div>
		</div>
		<div class="error-footer">
			<p class="error-title">对不起！您提交的信息未通过审核</p>
			<p class="error-detail">由于{{advertiser.rejectReasons}}，系统管理员不能通过您的入驻申请，您可以在本页面点击修改按钮修改相应的信息并重新上传，重新上传后系统管理员会重新审核您的入驻申请，谢谢！</p>
			<a href="" @click.prevent="getData" class="action-back" style="margin-bottom: 10px">修改信息</a>
		</div>
	</div>
</template>
<script>
import store from 'store';
window.store = store;
import actions from 'actions';
window.actions = actions;
import {copyObj} from 'utils/common';
import Http from 'http';
import router from '../../../route';
import RegisterCompany from './register-company.vue';
export default {
	name: 'app-approval-refused',
	data() {
		return {
			userId:'',
			advertiser:{},
			companyData:{
				bankAccount: '',
				bankAccountName: '',
				bankAccountPermissionImage: '',
				bankBranchAddress: '',
				bankBranchLineNumber: '',
				bankBranchName: '',
				bizEndDate: '',
				bizLicenseAddress: '',
				bizLicenseImage: '',
				bizLicenseNumber: '',
				bizScope: '',
				bizStartDate: '',
				companyAddress: '',
				companyEmail: '',
				companyFoundDate: '',
				companyName: '',
				companyPhone: '',
				emerContact: '',
				emerContactPhone: '',
				legalReprIdBackImage: '',
				legalReprIdImage: '',
				legalReprIdNumber: '',
				legalReprName: '',
				orgCode: '',
				orgCodeImage: '',
				regCapital: '',
				taxCode: '',
				taxNumber: '',
				taxRegCertImage: '',
				taxpayerQualificationImage: '',
				taxpayerType: ''
			}
		};
	},
	computed:{
	    userInfo: () => store.state.userInfo,
	    config:() => store.state.anicerControl
	},
	components: {
		'company': RegisterCompany
	},
	created() {
		this.getRejectReson();
	},
	methods:{
		getRejectReson() {
			let vm = this;
		    Http.get('/api/anicer/reason', {
				params: {
					userId: this.userInfo.userId
				}
			}) .then(function(res){
				if(res.data.code == 200){
					vm.advertiser = res.data.data;
				}
			}).catch(function(error){
				console.log(error);
			});
		},
		getData() {
			let vm = this;
			Http.get('/api/anicer/info')
			.then(function(response) {
				if(response.status === 200 && response.data.code === 200 && response.data.iserror === 0){
					let data = response.data.data;
					vm.companyData.companyName = data.corpName;
					vm.companyData.bizLicenseNumber = data.corpLicenseNumber;
					vm.companyData.legalReprName = data.legalPerson;
					vm.companyData.legalReprIdNumber = data.idCard;
					vm.companyData.legalReprIdImage = data.legalPersonFileFront;
					vm.companyData.legalReprIdBackImage = data.legalPersonFileReverse;
					vm.companyData.bizLicenseAddress = data.businessAddress;
					vm.companyData.companyFoundDate = data.foundDate;
					vm.companyData.bizStartDate = data.businessDateStart;
					vm.companyData.bizEndDate = data.businessDateEnd;
					vm.companyData.regCapital = data.registerCapital + '';
					vm.companyData.bizScope = data.busineScope;
					vm.companyData.bizLicenseImage = data.corpLicenceFilePath;
					vm.companyData.companyAddress = data.corpAddress;
					vm.companyData.companyPhone = data.corpPhone;
					vm.companyData.emerContact = data.manageName;
					vm.companyData.emerContactPhone = data.managePhone;
					vm.companyData.companyEmail = data.corpEmail;
					vm.companyData.orgCode = data.orgnizationCode;
					vm.companyData.orgCodeImage = data.orgnizationFilePath;
					vm.companyData.taxNumber = data.valueAddedTaxno;
					vm.companyData.taxpayerType = data.taxpayer;
					vm.companyData.taxCode = data.taxRate;
					vm.companyData.taxRegCertImage = data.taxpayerLicenseFile;
					vm.companyData.taxpayerQualificationImage = data.taxpayerPersonFile;
					vm.companyData.bankAccountName = data.bankAccountName;
					vm.companyData.bankAccount = data.bankAccount;
					vm.companyData.bankBranchName = data.bankName;
					vm.companyData.bankBranchLineNumber = data.bankCode;
					vm.companyData.bankBranchAddress = data.bankAddress;
					vm.companyData.bankAccountPermissionImage = data.bankImage;
					actions.setAnicerData(vm.$store,vm.companyData);
					actions.controlAnicer(vm.$store,{hasCompanyInfo:false,step:'company'});
					router.push({name: 'register'});
				}
			})
			.catch(function(error) {
				console.log(error);
			});
		}	
	}
};
</script>
<style scoped>
.body-scaffold {
    width: 350px;
    margin: 50px auto 0;
}
.error-detail {
    font-size: 14px;
    line-height: 30px;
    width: 544px;
    text-align: left;
    margin: 0 auto;
}
</style>
