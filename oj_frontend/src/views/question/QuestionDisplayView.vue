<template>
  <div id="questionDisplayView">
    <a-form :model="pageParam" layout="inline">
      <a-form-item field="title" label="标题">
        <a-input
          v-model="pageParam.title"
          placeholder="please input title"
          style="min-width: 240px"
        />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag
          v-model="pageParam.tags"
          placeholder="please input tag"
          style="min-width: 240px"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSearch">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: pageParam.pageSize,
        current: pageParam.current,
        total: total,
      }"
      @page-change="onPageChange"
    >
      <template #tags="{ record }">
        <a-space wrap v-for="(tag, index) of record.tags" :key="index">
          <a-tag v-if="tag == '简单'" color="green">
            {{ tag }}
          </a-tag>
          <a-tag v-else-if="tag == '困难'" color="red">
            {{ tag }}
          </a-tag>
          <a-tag v-else color="blue"> {{ tag }}</a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${
            record.submitNum
              ? (record.acceptNum / record.submitNum) * 100 + "%"
              : "0%"
          }
          (${record.acceptNum}/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doQuestion(record)">做题</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";
import store from "@/store";

//表格数据
const dataList = ref([]);
//搜素条件分页数据
const pageParam = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
});

const doSearch = () => {
  /*console.log(pageParam.value.tags);*/
  pageParam.value = {
    //重置当前页数
    ...pageParam.value,
    current: 1,
  };
  //watchEffect监听了pageParam
  /*getLoad();*/
};

//返回的分页的总数
const total = ref(0);
//获取数据库表分页后的数据
const getLoad = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    pageParam.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
    console.log("res.data.records = ", res.data.records);
  } else {
    message.error("查询题目失败");
  }
};

const onPageChange = (page: number) => {
  /*  console.log(page);第几页
    pageParam.value.current = page;*/
  pageParam.value = {
    ...pageParam.value, //先整个属性复制过来
    current: page,
  };
};

//初始化组件页面
onMounted(() => {
  getLoad();
});

const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "题目",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];

const router = useRouter();
const doQuestion = async (record: Question) => {
  console.log("个人信息 = ", store.state.user.loginUser);
  const role = store.state.user.loginUser?.userRole ?? "notLogin";
  if (role === "notLogin") {
    message.error("请先登录");
    await router.push({
      path: "/user/login",
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    });
    return;
  }
  //转到做题页面
  router.push({
    path: `/view/question/${record.id}`,
  });
};
watchEffect(() => {
  getLoad();
});
</script>

<style scoped>
#questionDisplayView {
  max-width: 1350px;
  margin: 0 auto;
}
</style>
