<template>
  <a-row id="globalHeader" align="center" :wrap="false">
    <a-col flex="auto">
      <a-menu
        mode="horizontal"
        :default-selected-keys="selectedKeys"
        @menu-item-click="doMenuClick"
      >
        <a-menu-item
          key="0"
          :style="{ padding: 0, marginRight: '38px' }"
          disabled
        >
          <div class="tittleBar">
            <img class="logo" src="../assets/logo.png" />
            <div class="tittle">AiQuestion</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="250px">
      <div v-if="loginUserStore.loginUser.id">
        <a-space size="medium">
          <div>{{ loginUserStore.loginUser.userName ?? "用户" }}</div>
          <a-button type="primary" @click="logout" href="/user/login">
            退出
          </a-button>
        </a-space>
      </div>
      <div v-else>
        <a-button type="primary" href="/user/login">登录</a-button>
      </div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRouter } from "vue-router";
import { computed, ref } from "vue";
import { useLoginUserStore } from "@/store/userStore";
import checkAccess from "@/access/checkAccess";
import { userLogoutUsingPost } from "@/api/userController";

const loginUserStore = useLoginUserStore();

const router = useRouter();

// 当前选中的菜单项
const selectedKeys = ref(["/"]);

// 路由跳转时，自动更新选中的菜单项
router.afterEach((to) => {
  selectedKeys.value = [to.path];
});

// 展示在菜单栏的路由数组
const visibleRoutes = computed(() => {
  return routes.filter((item) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单
    if (!checkAccess(loginUserStore.loginUser, item.meta?.access as string)) {
      return false;
    }
    return true;
  });
});

// 点击菜单跳转到对应页面
const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};

const logout = () => {
  userLogoutUsingPost();
};
</script>

<style scoped>
#globalHeader {
}

.tittleBar {
  display: flex;
  align-items: center;
}

.tittle {
  margin-left: 16px;
  color: black;
}

.logo {
  height: 48px;
}
</style>
