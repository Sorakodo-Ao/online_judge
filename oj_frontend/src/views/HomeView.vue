<template>
  <div class="home">
    <!--    <img alt="Vue logo" src="../assets/logo.png" />-->
    <CodeEditor :value="codeValue" :handle-change="onCodeChange" />
    <MdEditor :value="mdValue" :handle-change="onMdChange" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue";

const codeValue = ref();
const mdValue = ref();
const onCodeChange = (v: string) => {
  console.log(v);
  codeValue.value = v;
};
const onMdChange = (v: string) => {
  console.log(v);
  mdValue.value = v;
};
</script>
<style scoped></style>
