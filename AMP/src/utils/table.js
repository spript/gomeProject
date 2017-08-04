/**
 * v-1.0.0
 * 使用方法（此版本为初步版本，后续会逐步完善）
 * 1、表格外层需有两层div，
 * <div class="data-table" id="con-table-change">  id:必需属性，值不必固定，该容器最终会包含所有动态append的元素
 * 	<div class="main-table-wapper"> 表格外层容器，
 *	 <table class="table main-table"> 主表格，所有浮动的元素由此clone，main-table类名必需
 *	 </table>
 *	</div>
 * </div>
 * 2、在组件内
 * 引入所需公共方法
 * import {tableHandler, offWindowEvent, initWindowResize} from 'utils/table';
 * tableHandler：添加所有与浮动相关的元素
 * initWindowResize：当窗口大小改变时，重新计算
 * offWindowEvent：移除添加的事件，避免不必要的错误
 * 3、在vue实例的生命周期内调用步骤2引入的方法
 * mounted() { 渲染页面时初始化事件
 *  initWindowResize('con-table-change', true);
 * },
 * updated() { 当页面数据有变化时绘制元素
 *  tableHandler('con-table-change', true);
 * },
 * destroyed() { 组件销毁时注销事件
 *  offWindowEvent('con-table-change');
 * }
 *
 * 使用时可以参考定价或竞价cpc三个列表
 */



import {domCreator, addClass, on, off} from './dom.js';

/**
 * 表格处理事件
 * @param  {string}  containerId      所有表格容器的id
 * @param  {Boolean} hasSelectHandler 是否有checkbox事件
 */
export const tableHandler = (containerId, hasSelectHandler) => {
	var scrollWapper = document.getElementsByClassName('table__fixed-scrollbar--wapper')[0];
	var scrollLeft = 0;
	if (scrollWapper) {
		scrollLeft = scrollWapper.scrollLeft;
	}
	var mainTableCon = document.getElementById(containerId);
	if (mainTableCon) {
		var mainTable = mainTableCon.getElementsByClassName('main-table')[0];
		var mainFixedTableHead = getTableHead(mainTableCon, mainTable, false, hasSelectHandler);
		var fixedColumnTableHead = getTableHead(mainTableCon, mainTable, true, hasSelectHandler);
		mainTableCon.appendChild(mainFixedTableHead);
		mainTableCon.appendChild(fixedColumnTableHead);
		var fixedColumnTable = getfixColumnTable(mainTableCon, mainTable, hasSelectHandler);
		mainTableCon.appendChild(fixedColumnTable);
		var scrollContainer = getScrollContainer(mainTableCon, mainTable);
		mainTableCon.appendChild(scrollContainer);
		scrollContainerHandler(mainTableCon, mainTable, mainFixedTableHead, scrollContainer);
		initWindowSroll(mainTableCon, mainFixedTableHead, fixedColumnTableHead);
		// scrollHon(mainTable.parentNode, mainFixedTableHead, scrollLeft);
		scrollContainer.scrollLeft = scrollLeft;
		scrollVertical(mainTableCon, mainFixedTableHead, fixedColumnTableHead);
	}
};

/**
 * 获取表格表头
 * @param  {dom element}  mainContainer    包裹所有表格元素的外容器
 * @param  {dom table}  tableElement       主表格
 * @param  {Boolean} isLeftFixed           是否为左边表头
 * @param  {Boolean} hasSelectHandler      是否有checkbox 选项
 * @return {dom element}                   包含表头的div
 */
const getTableHead = (mainContainer, tableElement, isLeftFixed, hasSelectHandler) => {
	var tableHeadDivCon = mainContainer.getElementsByClassName('table__fixed-head--wapper');
	if (tableHeadDivCon.length > 0) {
		for (var i = 0;i < tableHeadDivCon.length;i++) {
			tableHeadDivCon[i].remove();
		}
	}
	tableHeadDivCon = domCreator('div', null, {class: 'table__fixed-head--wapper fixed-header'});
	var tableHeadTableCon = domCreator('table', null, {class: 'table fixedtablehead'});
	// var tableHeadDivCon = document.createElement('div', {className: 'fixed-head-wapper fixed-header', style: 'display:none;position: fixed;top: 0;right: 40px;width: 100%;z-index:90;overflow: hidden;'});
	// var tableHeadTableCon = document.createElement('table', {className: 'table fixedtablehead'});
	var tableHead = tableElement.getElementsByTagName('thead')[0].cloneNode(true);

	var sourceTableHeaThs = tableElement.getElementsByTagName('th');
	if (!isLeftFixed) {
		tableHeadTableCon.appendChild(tableHead);
		tableHeadDivCon.style.width = mainContainer.clientWidth + 'px';
	} else {
		tableHeadTableCon.appendChild(tableHead);
		var divWidth = 0;
		var fixThs = tableHead.getElementsByTagName('th');
		var tableThs = tableElement.getElementsByTagName('th');
		for (var i = 0;i < fixThs.length;i++) {
			if (i > 2) {
				addClass(fixThs[i], 'fixed-hidden');
			}
			fixThs[i].style.width = tableThs[i].offsetWidth + 'px';
		}
		divWidth = getFixColumnTableWidth(tableElement, 3);
		tableHeadDivCon.style.width = divWidth + 'px';
	}
	if (hasSelectHandler) {
		var currentEm = tableHead.getElementsByClassName('select-all')[0];
		var sourceEm = tableElement.getElementsByClassName('select-all')[0];
		on(currentEm, 'click', () => {
			sourceEm.click();
		});
	}
	tableHeadDivCon.style.left = mainContainer.offsetLeft + 'px';
	tableHeadDivCon.appendChild(tableHeadTableCon);
	return tableHeadDivCon;
};

/**
 * 获取左边固定列的表格
 * @param  {dom element}  mainContainer    包裹所有表格元素的外容器
 * @param  {dom table}  tableElement       主表格
 * @param  {Boolean} hasSelectHandler      是否有checkbox 选项
 * @return {dom element}                   包含表格的div
 */
const getfixColumnTable = (mainContainer, tableElement, hasSelectHandler) => {
	var tableColumnDivCon = mainContainer.getElementsByClassName('table__fixed-columns--wapper');
	if (!!tableColumnDivCon) {
		for (var i = 0;i < tableColumnDivCon.length;i++) {
			tableColumnDivCon[i].remove();
		}
	}
	tableColumnDivCon = domCreator('div', null, {class: 'table__fixed-columns--wapper'});
	var tableColumnTable = tableElement.cloneNode(true);
	var ths = tableColumnTable.getElementsByTagName('th');
	var tableThs = tableElement.getElementsByTagName('th');
	var tbodyTrs = tableColumnTable.getElementsByClassName('body-row');
	var sourceTableTbodyTrs = tableElement.getElementsByClassName('body-row');
	for (var j = 0;j < tbodyTrs.length;j++) {
		var trTds = tbodyTrs[j].children;//每一行的td
		for (var n = 0;n < trTds.length;n++) {
			if (n > 2) {
				addClass(trTds[n], 'fixed-hidden');
				addClass(ths[n], 'fixed-hidden');
			}
			if (n === 0 && hasSelectHandler) {
				let currentTdEm = trTds[n].getElementsByTagName('em')[0];
				on(currentTdEm, 'click', () => {
					let dataId = currentTdEm.getAttribute('data-id');
					var sourceEm = tableElement.getElementsByClassName('select-' + dataId)[0];
					sourceEm.click();
				});
			}
			trTds[n].style.width = tableThs[n].offsetWidth + 'px';
			ths[n].style.width = tableThs[n].offsetWidth + 'px';
		}
	}
	//复制左浮动全选事件
	if (hasSelectHandler) {
		var columnSelectAll = tableColumnTable.getElementsByClassName('select-all')[0];
		var sourceSelectAll = tableElement.getElementsByClassName('select-all')[0];
		on(columnSelectAll, 'click', () => {
			sourceSelectAll.click();
		});
	}
	//设置左浮动表格宽度
	var divWidth = getFixColumnTableWidth(tableElement, 3);
	tableColumnDivCon.style.width = divWidth + 'px';
	tableColumnDivCon.appendChild(tableColumnTable);
	return tableColumnDivCon;
};

const getFixColumnTableWidth = (tableElement, fixColumnCounts) => {
	var ths = tableElement.getElementsByTagName('th');
	var divWidth = 0;
	for (let i = 0;i < fixColumnCounts;i++) {
		divWidth += ths[i].offsetWidth;
	}
	// var divWidth = ths[0].offsetWidth + ths[1].offsetWidth + ths[2].offsetWidth;
	return divWidth + 1; //加1是为了显示最右边的边框
};

const getScrollContainer = (mainContainer, tableElement) => {
	var scrollContainerDiv = mainContainer.getElementsByClassName('table__fixed-scrollbar--wapper');
	if (!!scrollContainerDiv) {
		for (var i = 0;i < scrollContainerDiv.length;i++) {
			scrollContainerDiv[i].remove();
		}
	}
	var scrollContainerDiv = domCreator('div', null, {class: 'table__fixed-scrollbar--wapper'});
	scrollContainerDiv.style.width = mainContainer.clientWidth + 'px';
	scrollContainerDiv.style.left = mainContainer.offsetLeft + 'px';

	var scrollContentDiv = domCreator('div', null, {class: 'table__fixed-scrollbar--content'});
	scrollContentDiv.style.width = tableElement.clientWidth + 'px';
	scrollContainerDiv.appendChild(scrollContentDiv);
	return scrollContainerDiv;
};

const scrollContainerHandler = (mainContainer, tableElement, fixHeaderWapper, scrollWapper) => {
	if (tableElement.clientWidth > mainContainer.clientWidth) {
		var mainTableWapper = tableElement.parentNode;
		on(scrollWapper, 'scroll', e => {
			// var scrollLeft = scrollWapper.scrollLeft;
			// mainTableWapper.scrollLeft =  scrollLeft;
			// fixHeaderWapper.scrollLeft =  scrollLeft;
			scrollHon(mainTableWapper, fixHeaderWapper, scrollWapper.scrollLeft);
		});
	} else {
		scrollWapper.style.display = 'none';
	}
};

const scrollHon = (mainTableWapper, fixHeaderWapper, scrollLeft) => {
	mainTableWapper.scrollLeft =  scrollLeft;
	fixHeaderWapper.scrollLeft =  scrollLeft;
};
const initWindowSroll = (mainContainer, fixHeaderWapper, fixLeftColumnHeadWapper) => {
	on(window, 'scroll', (e) => {
		// var scrollTop = document.body.scrollTop;
		// if ((mainContainer.offsetTop - scrollTop) <= 0) {
		// 	fixHeaderWapper.style.display = 'block';
		// 	fixLeftColumnHeadWapper.style.display = 'block';
		// } else {
		// 	fixHeaderWapper.style.display = 'none';
		// 	fixLeftColumnHeadWapper.style.display = 'none';
		// }
		scrollVertical(mainContainer, fixHeaderWapper, fixLeftColumnHeadWapper);
	});
};

const scrollVertical = (mainContainer, fixHeaderWapper, fixLeftColumnHeadWapper) => {
	var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
	if ((mainContainer.offsetTop - scrollTop) <= 0) {
		fixHeaderWapper.style.display = 'block';
		fixLeftColumnHeadWapper.style.display = 'block';
	} else {
		fixHeaderWapper.style.display = 'none';
		fixLeftColumnHeadWapper.style.display = 'none';
	}
};

export const initWindowResize = (containerId, hasSelectHandler) => {
	on(window, 'resize', () => {tableHandler(containerId, hasSelectHandler);});
};

export const offWindowEvent = (containerId, hasSelectHandler = true) => {
	var mainContainer = document.getElementById(containerId);
	off(window, 'scroll', () => {
		var scrollWapper = mainContainer.getElementsByClassName('table__fixed-scrollbar--wapper')[0];
		off(scrollWapper, 'scroll');
	});
	off(window, 'resize', tableHandler(containerId, hasSelectHandler));
};