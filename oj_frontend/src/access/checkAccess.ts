import ACCESSENUM from "@/access/accessEnum";

/**
 *
 * @param loginUser 当前用户
 * @param needAccess 当前访问页面需要的权限
 */
const checkAccess = (loginUser: any, needAccess = ACCESSENUM.NOT_LOGIN) => {
  const loginUserAccess = loginUser?.userRole ?? ACCESSENUM.NOT_LOGIN;
  if (needAccess === ACCESSENUM.NOT_LOGIN) {
    return true;
  }
  //需要用户登录
  if (needAccess === ACCESSENUM.USER) {
    if (loginUserAccess === ACCESSENUM.NOT_LOGIN) {
      console.log("用户未登录");
      //用户未登录
      return false;
    }
  }
  //管理员
  if (needAccess === ACCESSENUM.ADMIN) {
    if (loginUserAccess != ACCESSENUM.ADMIN) {
      console.log("no Admin");
      return false;
    } else {
      console.log("Admin");
      return true;
    }
  }
  return true;
  /*  // 获取当前登录用户具有的权限（如果没有 loginUser，则表示未登录）
    const loginUserAccess = loginUser?.userRole ?? ACCESSENUM.NOT_LOGIN;
    if (needAccess === ACCESSENUM.NOT_LOGIN) {
      return true;
    }
    // 如果用户登录才能访问
    if (needAccess === ACCESSENUM.USER) {
      // 如果用户没登录，那么表示无权限
      if (loginUserAccess === ACCESSENUM.NOT_LOGIN) {
        return false;
      }
    }
    // 如果需要管理员权限
    if (needAccess === ACCESSENUM.ADMIN) {
      // 如果不为管理员，表示无权限
      if (loginUserAccess !== ACCESSENUM.ADMIN) {
        return false;
      }
    }
    return true;*/
};
export default checkAccess;
