<template>
  <a-button type="outline" @click="handleClick">AI 生成题目</a-button>
  <a-drawer
    :width="340"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title> AI 生成题目</template>
    <div>
      <p>注意：AI生成并不完全可靠，建议得分类应用手动编写题目！</p>
      <a-form
        :model="form"
        label-align="left"
        auto-label-width
        @submit.prevent="handleSubmit"
      >
        <a-form-item label="应用 id">
          {{ appId }}
        </a-form-item>
        <a-form-item field="questionNumber" label="题目数量">
          <a-input-number
            v-model="form.questionNumber"
            min="0"
            max="20"
            placeholder="请输入题目数量"
          />
        </a-form-item>
        <a-form-item field="optionNumber" label="选项数量">
          <a-input-number
            v-model="form.optionNumber"
            min="0"
            max="6"
            placeholder="请输入选项数量"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button
              :loading="submitting"
              type="primary"
              html-type="button"
              @click="handleSubmit"
              style="width: 120px"
            >
              {{ submitting ? "生成中..." : "一键生成" }}
            </a-button>
            <a-button
              :loading="Ssesubmitting"
              @click="handleSSETime"
              style="width: 120px"
            >
              {{ Ssesubmitting ? "生成中..." : "实时生成" }}
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </a-drawer>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults } from "vue";
import API from "@/api";
import { aiGenerateQuestionUsingPost } from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";

interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
  onSSESuccess?: (result: API.QuestionContentDTO) => void;
  onSSEStart?: (event: any) => void;
  onSSEClose?: (event: any) => void;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const visible = ref(false);
const submitting = ref(false);
const Ssesubmitting = ref(false);
const form = reactive({
  questionNumber: 10,
  optionNumber: 2,
} as API.AIGenerateQuestionRequest);

const handleClick = () => {
  visible.value = true;
};

const handleOk = async () => {
  if (!props.appId) {
    return;
  }
  // 进行生成操作
  await handleSubmit();
  visible.value = false;
};

const handleCancel = () => {
  visible.value = false;
};

/**
 * 提交
 */
const handleSubmit = async () => {
  if (!props.appId) {
    return;
  }
  submitting.value = true;
  const res = await aiGenerateQuestionUsingPost({
    appId: props.appId as any,
    ...form,
  });
  if (res.data.code === 0 && res.data.data.length > 0) {
    if (props.onSuccess) {
      props.onSuccess(res.data.data);
    } else {
      message.success("生成成功");
    }
    // 关闭抽屉
    handleCancel();
  } else {
    message.error("操作失败，" + res.data.message);
  }
  submitting.value = false;
};

/**
 * 实时生成
 */
const handleSSETime = async () => {
  if (!props.appId) {
    return;
  }
  Ssesubmitting.value = true;
  // 创建SSE请求
  const eventSource = new EventSource(
    `http://localhost:8101/api/question/ai_generate/sse?appId=${props.appId}&questionNumber=${form.questionNumber}&optionNumber=${form.optionNumber}`,
    {
      withCredentials: true,
    }
  );
  let first = true;
  // 接收消息
  eventSource.onmessage = function (event) {
    if (first) {
      props.onSSEStart?.(event);
      handleCancel();
      first = !first;
    }
    props.onSSESuccess?.(JSON.parse(event.data));
    console.log(event.data);
    Ssesubmitting.value = false;
  };
  // 关闭连接
  eventSource.onerror = function (event) {
    if (event?.eventPhase === EventSource.CLOSED) {
      console.log("连接关闭");
      props.onSSEClose?.(event);
      eventSource.close();
    }
  };
  // 打开连接
  eventSource.onopen = function (event) {
    console.log("连接打开");
    props.onSSEStart?.(event);
  };
};
</script>
