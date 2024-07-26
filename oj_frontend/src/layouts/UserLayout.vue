<!--基础布局-->
<template>
  <div id="userLayout">
    <a-layout style="min-height: 100vh">
      <!--      头部导航栏-->
      <a-layout-header class="header">
        <a-space>
          <img src="@/assets/Online-Judge-logo.png" class="logo" />
          <div class="logo-content">Online-Judge</div>
        </a-space>
      </a-layout-header>
      <!--      内容区域-->
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <!--      页尾-->
      <a-layout-footer class="footer">
        online-judge by
        <a href="https://github.com/Sorakodo-Ao">sorako</a>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<style scoped>
#userLayout {
  text-align: center;
  //background-image: url("@/assets/LoginBackground.bmp");
  //background-image: url("@/assets/bkg.png");
}

#userLayout .header a-space {
  background: aquamarine;
}

#userLayout .header .logo {
  width: 64px;
  height: 64px;
}

#userLayout .header .logo-content {
  color: aquamarine;
  margin-top: 13px;
}

#userLayout .content {
  padding: 20px;
  padding-top: 0;
}

#userLayout .footer {
  color: aquamarine;
  padding: 16px;
  position: sticky;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
<script setup lang="ts"></script>
