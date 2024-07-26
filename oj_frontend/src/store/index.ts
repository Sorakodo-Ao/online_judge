import { createStore } from "vuex";
import user from "./user";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  getters: {},
  mutations: {},
  actions: {},
  modules: { user },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
});
