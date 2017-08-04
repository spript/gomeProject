import Vue from 'vue';

// element-ui 需要的引入 减小体积
import {
	Button,
	Pagination,
	DatePicker,
	Select,
	Option,
	Form,
	FormItem,
	Input,
	Radio,
	RadioGroup,
	Row,
	Col,
	Checkbox,
	Tooltip,
	Upload,
	Autocomplete,
	Switch,
	Message,
	Popover,
	Dialog,
	Table,
	TableColumn,
	Rate,
	Scrollbar,
	CheckboxGroup,
	Carousel,
	CarouselItem
} from 'element-ui';
import VueAwesomeSwiper from 'vue-awesome-swiper';
Vue.use(Button);
Vue.use(Pagination);
Vue.use(DatePicker);
Vue.use(Select);
Vue.use(Option);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(Row);
Vue.use(Col);
Vue.use(Checkbox);
Vue.use(Tooltip);
Vue.use(Upload);
Vue.use(Autocomplete);
Vue.use(Switch);
Vue.use(Popover);
Vue.use(Dialog);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Rate);
Vue.use(Scrollbar);
Vue.use(CheckboxGroup);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(VueAwesomeSwiper);
let message = Message;

Vue.prototype.$message = message;
