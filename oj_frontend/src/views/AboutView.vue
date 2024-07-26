<template>
  <div class="about">
    <h1>This is an about page</h1>
    <h3 class="title">原神 , 启动！！！点击下方图片一键安装原神</h3>
    <div>
      <img :src="require('@/assets/pointdown.jpg')" class="point" />
    </div>
    <a href="https://ys.mihoyo.com/main/">
      <img
        class="Genshin"
        :src="require('@/assets/Genshin_Impact.jpg')"
        alt="Genshin"
    /></a>
  </div>
</template>
<script setup></script>
<style>
.about .title {
  color: red;
}

.about .point {
  width: 35px;
  height: 45px;
}

.Genshin {
  width: 380px;
  height: 350px;
}
</style>
