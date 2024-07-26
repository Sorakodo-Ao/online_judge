<template>
  <div id="manageQuestionView">
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
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ moment(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";
//表格数据
const dataList = ref([]);
//分页数据
const pageParam = ref({
  pageSize: 10,
  current: 1,
});
//返回的分页的总数
const total = ref(0);
//获取数据库表分页后的数据
const getLoad = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
    pageParam.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("查询所有题目失败");
  }
};

const onPageChange = (page: number) => {
  //console.log(page);第几页
  //pageParam.value.current = page;
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
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  /*  {
    title: "内容",
    dataIndex: "content",
  },*/
  {
    title: "标签",
    dataIndex: "tags",
  },
  /*  {
    title: "答案",
    dataIndex: "answer",
  },*/
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptNum",
  },
  /*  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "用户id",
    dataIndex: "userId",
  },*/
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "修改时间",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

const router = useRouter();
const doUpdate = async (record: Question) => {
  //转到更新页面
  router.push({
    path: "/update/question",
    query: {
      id: record.id,
    },
  });
  console.log(record);
};
const doDelete = async (record: Question) => {
  console.log(record);
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: record.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
    getLoad();
  } else {
    message.error("删除失败");
  }
};
watchEffect(() => {
  getLoad();
});
</script>

<style scoped>
#manageQuestionView {
}
</style>
