import Vue from 'vue';
Vue.directive('focus', {
	update: (el, {value}) => {
		if (value) {
			el.focus();
		}
	}
});
