<template>
  <a-row id="gobalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :selected-keys="selectesKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="title-bar">
            <img class="logo" src="../assets/Online-Judge-logo.png" />
            <div class="title">OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>{{ store.state.user?.loginUser?.userName ?? "未登录" }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAccess from "@/access/checkAccess";

const router = useRouter();
const store = useStore();
const loginUser = store.state.user.loginUser;
/*/!*console.log(store.state.user.loginUser);*!/
setTimeout(() => {
  store.dispatch("user/getLoginUser");
}, 3000);*/

// 展示可见页面
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.isHide) {
      return false; //不展示页面
    }
    //用户权限校验
    if (!checkAccess(loginUser, item.meta?.access as string)) {
      console.log("可见页面 = ", loginUser);
      return false;
    }
    return true;
  });
});

//默认主页
const selectesKeys = ref(["/"]);

//点击路由，跳转后，更新选中的菜单项高亮
router.afterEach((to, from) => {
  selectesKeys.value = [to.path];
});
//点击子菜单跳转到对应路由
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<style scoped>
#gobalHeader {
  box-shadow: #eee 5px 5px 5px;
}

.title-bar {
  display: flex;
  align-items: center;
}

.title-bar .logo {
  height: 48px;
  height: 48px;
}

.title-bar .title {
  color: aqua;
  margin-left: 8px;
  margin-top: 10px;
}
</style>
