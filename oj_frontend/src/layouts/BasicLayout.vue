<!--基础布局-->
<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <!--      头部导航栏-->
      <a-layout-header class="header">
        <GobalHeader />
      </a-layout-header>
      <!--      内容区域-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!--      页尾-->
      <a-layout-footer class="footer">
        Footer online-judge by
        <a href="https://github.com/Sorakodo-Ao">sorako</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#basicLayout .header {
}

#basicLayout .content {
  padding: 20px;
}

#basicLayout .footer {
  background-color: aquamarine;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
<script>
import GobalHeader from "@/components/GobalHeader";

export default {
  components: { GobalHeader },
};
</script>
