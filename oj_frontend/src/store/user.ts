import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  //储存用户信息
  state: () => ({
    loginUser: {
      userName: "load",
    },
  }),
  // actions
  actions: {
    /*    async getLoginUser({ commit, state }, payload) {
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 0) {
        commit("update", res.data);
        console.log("getLoginUserUsingGet = ", res.data);
      } else {
        commit("update", {
          ...state.loginUser,
          userRole: ACCESSENUM.NOT_LOGIN,
        });
      }
    },*/
    updateUser({ commit }, payload) {
      localStorage.setItem("testKey", JSON.stringify({ test: "data" }));
      console.log("payload = ", payload);
      commit("setLoginUser", payload);
    },
  },

  // mutations
  // 更新用户状态
  mutations: {
    setLoginUser(state, payload) {
      console.log("payload = ", payload);
      console.log("loginUser = ", state);
      state.loginUser = payload;
    },
  },
} as StoreOptions<any>;
