<template>
  <a-modal v-model:visible="visible" :footer="false" @cancel="closeModel">
    <template #title>
      {{ title }}
    </template>
    <h4 style="margin-top: 0">复制分享链接</h4>
    <a-typography-paragraph copyable>{{ link }}</a-typography-paragraph>
    <h4>手机扫码查看</h4>
    <img :src="codeImg" alt="分享二维码" />
  </a-modal>
</template>

<script setup lang="ts">
import { defineExpose, withDefaults, defineProps, ref } from "vue";
import QRCode from "qrcode";
import message from "@arco-design/web-vue/es/message";

/**
 * 定义组件属性类型
 */
interface Props {
  // 分享链接
  link: string;
  // 分享标题
  title: string;
}

/**
 * 给组件指定初始值
 */
const props = withDefaults(defineProps<Props>(), {
  link: () => "https://laoyujianli.com/share/yupi",
  title: () => "分享",
});

const codeImg = ref();
const visible = ref(false);

const openModel = () => {
  visible.value = true;
};

// 暴露函数给父组件
defineExpose({
  openModel,
});

const closeModel = () => {
  visible.value = false;
};

QRCode.toDataURL(props.link)
  .then((url: any) => {
    codeImg.value = url;
  })
  .catch((err: any) => {
    console.error(err);
    message.error("生成二维码失败" + err.message);
  });
</script>

<style scoped></style>
