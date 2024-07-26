<template>
  <div id="addQuestionView">
    <h2>创建题目</h2>
    <a-form :model="form" label-align="left">
      <a-form-item field="title" label="题目标题">
        <a-input v-model="form.title" placeholder="please enter title" />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag
          v-model="form.tags"
          placeholder="Please Enter tags"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="content" label="题目内容">
        <MdEditor :value="form.content" :handle-change="onContentChange" />
      </a-form-item>
      <a-form-item field="answer" label="答案">
        <MdEditor :value="form.answer" :handle-change="onAnswerChange" />
      </a-form-item>

      <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
        <a-space direction="vertical" style="min-width: 480px">
          <a-form-item field="judgeConfig.memoryLimit" label="内存限制">
            <a-input-number
              v-model="form.judgeConfig.memoryLimit"
              placeholder="Please Enter memoryLimit"
            />
            kb
          </a-form-item>
          <a-form-item field="judgeConfig.timeLimit" label="时间限制">
            <a-input-number
              v-model="form.judgeConfig.timeLimit"
              placeholder="please enter timeLimit"
            />
            ms
          </a-form-item>
          <a-form-item field="judgeConfig.stackLimit" label="堆栈限制">
            <a-input-number
              v-model="form.judgeConfig.stackLimit"
              placeholder="Please Enter stackLimit"
            />
            kb
          </a-form-item>
        </a-space>
      </a-form-item>
      <a-form-item label="题目用例" :content-flex="false" :merge-props="false">
        <a-form-item
          v-for="(judgeCaseItem, index) of form.judgeCase"
          :key="index"
          no-style
        >
          <a-space direction="vertical" style="min-width: 600px">
            <a-form-item
              :field="`judgeCase[${index}].input`"
              :label="`输入用例-${index}`"
              :key="index"
            >
              <a-input
                v-model="judgeCaseItem.input"
                placeholder="please enter inputCase"
              />
            </a-form-item>
            <a-form-item
              :field="`judgeCase[${index}].output`"
              :label="`输出用例-${index}`"
              :key="index"
            >
              <a-input
                v-model="judgeCaseItem.output"
                placeholder="please enter outputCase"
              />
            </a-form-item>
            <a-button status="danger" @click="handleDelete(index)"
              >删除
            </a-button>
          </a-space>
        </a-form-item>
      </a-form-item>
      <a-form-item>
        <div>
          <a-button status="success" @click="handleAdd">新增题目用例</a-button>
        </div>
      </a-form-item>

      <a-form-item>
        <a-button type="primary" @click="doSubmit">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

//路由包含update即为更新页面,submit提交判断
const updatePage = route.path.includes("update");
//console.log("updatePage = ", updatePage);

//更新要先加载对应id的题目
const loadData = async () => {
  const id = route.query.id;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
    id as any
  );
  if (res.code === 0) {
    console.log("修改对象", res.data);
    // Object.assign(form, res.data);
    form.value = res.data as any;
    if (!form.value.tags) {
      form.value.tags = [];
    } else {
      form.value.tags = JSON.parse(form.value.tags as any);
    }

    if (!form.value.judgeCase) {
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
      /*console.log("judgeCase = ", form.value.judgeCase);*/
    }

    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        timeLimit: 100,
        memoryLimit: 100,
        stackLimit: 100,
      };
    } else {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
  } else {
    message.error("加载失败");
  }
};
//初始化页面
onMounted(() => {
  loadData();
});

const form = ref({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
  judgeConfig: {
    timeLimit: 100,
    memoryLimit: 100,
    stackLimit: 100,
  },
});
const handleAdd = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1);
};
//提交方法
const doSubmit = async () => {
  if (updatePage) {
    //执行更新操作
    const res = await QuestionControllerService.updateQuestionUsingPost(
      form.value
    );
    console.log(form.value);
    if (res.code === 0) {
      message.success("更新成功");
      /*      router.push({
        path: "/manage/question",
        replace: true,
      });*/
      await router.replace("/manage/question");
    } else {
      message.error("更新失败");
    }
  } else {
    const res = await QuestionControllerService.addQuestionUsingPost(
      form.value
    );
    if (res.code === 0) {
      message.success("创建题目成功");
      await router.push({
        path: "/question",
        replace: true,
      });
    } else {
      message.error("创建失败" + res.message);
    }
  }
};

const onContentChange = (value: string) => {
  form.value.content = value;
};
const onAnswerChange = (value: string) => {
  form.value.answer = value;
};
</script>

<style scoped>
#addQuestionView {
}
</style>
