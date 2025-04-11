<template>
  <div id="noAuthPage">
    <h1 align="center">
      {{
        "用户名" +
        JSON.stringify(loginUserStore.loginUser.userName) +
        "没有权限"
      }}
    </h1>
  </div>
</template>

<script setup lang="ts">
import { useLoginUserStore } from "@/store/userStore";

const loginUserStore = useLoginUserStore();
</script>

<style scoped>
#noAuthPage {
  align-items: center;
}
</style>
