<template>
  <div id="viewDisplayView">
    <a-row :gutter="[24, 24]">
      <a-col :span="12" :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <template #extra>
                <a-link>
                  <a-space wrap>
                    <a-tag
                      v-for="(tag, index) of question.tags"
                      :key="index"
                      color="green"
                      >{{ tag }}
                    </a-tag>
                  </a-space>
                </a-link>
              </template>
              <a-descriptions title="题目条件">
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }}ms
                </a-descriptions-item>

                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}kb
                </a-descriptions-item>

                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}kb
                </a-descriptions-item>
              </a-descriptions>

              <MdView :value="question.content || ''" />
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论区" disabled> 评论区</a-tab-pane>
          <a-tab-pane key="answer" title="答案"> 暂时无法查看</a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :span="12" :md="12" :xs="24">
        <a-form-item field="language" label="编程语言">
          <a-select
            :style="{ width: '160px' }"
            placeholder="Select"
            v-model="form.language"
            :trigger-props="{ autoFitPopupMinWidth: true }"
          >
            <a-option>java</a-option>
            <a-option>c</a-option>
            <a-option>cpp</a-option>
            <a-option>python</a-option>
            <a-option>go</a-option>
          </a-select>
        </a-form-item>
        <CodeEditor
          :value="form.code as string"
          :language="form.language"
          :handle-change="onChange"
        />
        <a-divider size="0" />
        <a-button type="primary" style="width: 80px" @click="doSubmit">
          提交
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import { defineProps, onMounted, ref, withDefaults } from "vue";
import message from "@arco-design/web-vue/es/message";
import MdView from "@/components/MdView.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { useRouter } from "vue-router";

//题目信息
const question = ref<QuestionVO>();
console.log(question);

const router = useRouter();

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

//编程语言和代码的值
const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

const onChange = (val: string) => {
  console.log(val);
  form.value.code = val;
};
//代码提交
const doSubmit = async () => {
  const res = await QuestionSubmitControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value?.id,
  });
  if (res.code === 0) {
    message.success("提交成功");
    await router.push("/question");
  } else {
    message.error("提交失败");
  }
};

//根据id获取题目信息
const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error("加载失败");
  }
};

onMounted(() => {
  loadData();
});
</script>

<style>
#viewDisplayView {
}

#viewDisplayView .arco-space-horizontal .arco-space-item {
  margin-bottom: 0px !important;
}
</style>
