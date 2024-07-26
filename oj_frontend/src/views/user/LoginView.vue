<template>
  <div id="loginView">
    <h2 class="title">用户登录</h2>
    <a-form
      :model="form"
      label-align="left"
      auto-label-width
      style="max-width: 450px; margin: 0 auto"
      @submit="handleSubmit"
    >
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入密码"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" style="width: 100px" html-type="submit"
          >登录
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { UserControllerService, UserLoginRequest } from "../../../generated";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import message from "@arco-design/web-vue/es/message";

const router = useRouter();
const route = useRoute();
const store = useStore();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  //登陆成功，返回主页
  /*  if (res.code === 0) {
    message.success("登陆成功!");
    store.dispatch("user/getLoginUser");
    router.push({
      path: "/",
      replace: true,
    });
  } else {
    message.error("登录失败! " + res.message);
  }*/
  if (res.code === 0) {
    message.success("登陆成功!");
    await store.dispatch("user/updateUser", res.data);
    console.log("登录后 = " + store.state.loginUser);
    const redirectPath = route.query.redirect;
    console.log("登录，转发 directPath = " + redirectPath);
    await router.push({
      path: "/",
      replace: true,
    });
  }
};
</script>
<style>
#loginView .title {
  color: aliceblue;
  margin-bottom: 15px;
}
</style>
