import { RouteRecordRaw } from "vue-router";
import NoAuthView from "@/views/NoAuthView.vue";
import ACCESSENUM from "@/access/accessEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import LoginView from "@/views/user/LoginView.vue";
import RegisterView from "@/views/user/RegisterView.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionDisplayView from "@/views/question/QuestionDisplayView.vue";
import ViewQuestionView from "@/views/question/ViewQuestionView.vue";
import PersonalView from "@/views/question/PersonalView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "user",
    component: UserLayout,
    meta: {
      isHide: true, //隐藏
    },
    children: [
      {
        path: "/user/login",
        name: "login",
        component: LoginView,
      },
      {
        path: "/user/register",
        name: "register",
        component: RegisterView,
      },
    ],
  },
  {
    path: "/question",
    name: "题目总览",
    component: QuestionDisplayView,
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      access: ACCESSENUM.ADMIN,
    },
  },
  {
    path: "/update/question",
    name: "更新题目",
    component: AddQuestionView,
    meta: {
      isHide: true, //隐藏
      access: ACCESSENUM.USER,
    },
  },
  {
    path: "/manage/question",
    name: "管理题目",
    component: ManageQuestionView,
    meta: {
      access: ACCESSENUM.ADMIN,
    },
  },
  {
    path: "/view/question/:id",
    name: "在线做题",
    component: ViewQuestionView,
    props: true,
    meta: {
      isHide: true, //隐藏
      access: ACCESSENUM.USER,
    },
  },
  {
    path: "/",
    name: "主页",
    component: PersonalView,
    /*    meta: {
      isHide: true, //隐藏
      access: ACCESSENUM.USER,
    },*/
  },

  // {
  //   path: "/hide",
  //   name: "hide",
  //   component: HomeView,
  //   meta: {
  //     isHide: true, //隐藏
  //   },
  // },
  {
    path: "/noAuth",
    name: "无权限",
    component: NoAuthView,
    meta: {
      isHide: true, //隐藏
    },
  },
  /*  {
      path: "/admin",
      name: "admin",
      component: AdminView,
      meta: {
        access: ACCESSENUM.ADMIN,
      },
    },*/
  {
    path: "/about",
    name: "about",
    component: () => import("../views/AboutView.vue"),
  },
];
