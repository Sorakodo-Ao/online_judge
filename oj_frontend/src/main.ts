import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import ArcoVue from "@arco-design/web-vue";
import "@arco-design/web-vue/dist/arco.css";
import "@/access/index";
import "@/plugins/axios";
import "bytemd/dist/index.css";

const app = createApp(App);
app.use(ArcoVue);
app.use(store);
app.use(router);
app.mount("#app");
