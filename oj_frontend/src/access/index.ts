//权限管理
import store from "@/store";
import router from "@/router";
import checkAccess from "@/access/checkAccess";
import ACCESSENUM from "@/access/accessEnum";

router.beforeEach(async (to, from, next) => {
  let loginUser = store.state.user.loginUser;
  //如果之前沒登錄過，自動登錄
  if (!loginUser || !loginUser.userRole) {
    await store.dispatch("user/getLoginUser");
    loginUser = store.state.user.loginUser;
    console.log("beforeEach 的loginUser = ", loginUser);
  }
  const needAccess = (to.meta?.access as string) ?? ACCESSENUM.NOT_LOGIN;
  //跳转的页面需要登录
  if (needAccess !== ACCESSENUM.NOT_LOGIN) {
    //没登录
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === ACCESSENUM.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    if (!checkAccess(loginUser, needAccess as string)) {
      next("/noAuth");
    }
  }
  next();
});
