<template>
  <div id="appDetailPage">
    <a-card>
      <a-row style="margin-bottom: 16px">
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ data.appName }}</h2>
          <p>{{ data.appDesc }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data.appType as number] }}</p>
          <p>
            评分策略：{{
              APP_SCORING_STRATEGY_MAP[data.scoringStrategy as number]
            }}
          </p>
          <p>
            <a-space>
              作者：
              <div :style="{ display: 'flex', alignItems: 'center' }">
                <a-avatar
                  :size="24"
                  :image-url="data.user?.userAvatar"
                  :style="{ marginRight: '8px' }"
                />
                <a-typography-text>
                  {{ data.user?.userName ?? "用户" }}
                </a-typography-text>
              </div>
            </a-space>
          </p>
          <p>
            创建时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button type="primary" :href="`/answer/do/${id}`">
              开始答题
            </a-button>
            <a-button @click="doShare">分享应用</a-button>
            <a-button v-if="isMy" :href="`/add/question/${id}`">
              设置题目
            </a-button>
            <a-button v-if="isMy" :href="`/add/scoring_result/${id}`">
              设置评分
            </a-button>
            <a-button v-if="isMy" :href="`/add/app/${id}`">修改应用</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.appIcon" />
        </a-col>
      </a-row>
    </a-card>
  </div>
  <ShareModel :link="shareLink" title="应用分享" ref="shareModelRef" />
</template>

<script setup lang="ts">
import { computed, defineProps, ref, watchEffect, withDefaults } from "vue";
import API from "@/api";
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { useLoginUserStore } from "@/store/userStore";
import ShareModel from "@/components/ShareModel.vue";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

const data = ref<API.AppVO>({});

// 获取登录用户
const loginUserStore = useLoginUserStore();
const loginUserId = loginUserStore.loginUser?.id;
// 是否为本人创建
const isMy = computed(() => {
  return loginUserId && loginUserId === data.value.userId;
});

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
    id: props.id as any,
  });
  if (res.data.code === 0) {
    data.value = res.data.data as any;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 监听 serrchParams 变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

// 分享弹窗的引用
const shareModelRef = ref();
// 分享链接
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.id}`;

// 分享
const doShare = (e: Event) => {
  if (shareModelRef.value) {
    shareModelRef.value.openModel();
  }
  // 阻止冒泡，防止跳转到详情页
  e.stopPropagation();
};
</script>

<style scoped>
#appDetailPage {
}

#appDetailPage .content-wrapper > * {
  margin-bottom: 24px;
}
</style>
