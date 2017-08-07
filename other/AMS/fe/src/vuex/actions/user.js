export const setUserInfo = function(store, userInfo) {
	store.commit('SETUSERINFO', userInfo);
};
export const setUserName = function(store, userName) {
	store.commit('SETUSERNAME', userName);
};
export const setUserPrivilege = function(store, privileges) {
	store.commit('SETUSERPRIVILEGE', privileges);
};
