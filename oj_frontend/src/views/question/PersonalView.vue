<template>
  <div id="personalView">
    <h1>{{ store.state.user.loginUser.userName }}的历史做题记录</h1>
    <a-table :columns="columns" :data="dataList">
      <template #tags="{ record }">
        <!--        <a-space wrap>
                  <a-tag v-for="(tag, index) of record.tags" :key="index" color="green"
                  >
                    <a-space v-if="tag == '简单'">{{ tag }}</a-space>
                    <a-space v-if="tag == '简单'">{{ tag }}</a-space>
                  </a-tag>
                </a-space>-->
        <a-space wrap v-for="(tag, index) of record.tags" :key="index">
          <a-tag v-if="tag == '简单'" color="green">
            {{ tag }}
          </a-tag>
          <a-tag v-else-if="tag == '困难'" color="red">
            {{ tag }}
          </a-tag>
          <a-tag v-else color="blue">
            {{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #judgeInfoMessage="{ record }">
        <a-button
          v-if="record.judgeInfoMessage === '成功'"
          status="success"
          style="width: 75px"
          >{{ record.judgeInfoMessage }}
        </a-button>
        <a-button
          v-else-if="record.judgeInfoMessage == '答案错误'"
          status="danger"
          style="width: 75px"
          >{{ record.judgeInfoMessage }}
        </a-button>
        <a-button v-else status="warning" style="width: 75px"
          >{{ record.judgeInfoMessage }}
        </a-button>
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { UserControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import moment from "moment";
import store from "../../store";

const columns = [
  {
    title: "题目",
    dataIndex: "title",
    sortable: {
      sortDirections: ["ascend", "descend"],
    },
  },
  {
    title: "语言",
    dataIndex: "language",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "状态",
    dataIndex: "status",
  },
  {
    title: "判题情况",
    slotName: "judgeInfoMessage",
  },
  {
    title: "做题时间",
    slotName: "createTime",
    sortable: {
      defaultSortOrder: ["descend"],
    },
  },
];
const dataList = ref([]);
// eslint-disable-next-line @typescript-eslint/no-loss-of-precision
const userId = store.state.user.loginUser.id;
const getPersonalQuestion = async () => {
  const res = await UserControllerService.getPersonalQuestionVoUsingGet(userId);
  if (res.code === 0) {
    dataList.value = res.data;
    console.log("res.data = ", res.data);
  } else {
    message.error("获取做题信息失败！");
  }
};
//初始化组件页面
onMounted(() => {
  getPersonalQuestion();
});
</script>

<style>
#personalView {
}
</style>
