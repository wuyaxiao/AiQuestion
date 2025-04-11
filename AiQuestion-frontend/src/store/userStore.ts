import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 登陆用户信息全局状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  // <>是对ref的参数校验
  const loginUser = ref<API.LoginUserVO>({
    userName: "未登录",
  });

  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser;
  }

  // fetch是远程获取，说白了就是get
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      loginUser.value = { userRole: ACCESS_ENUM.NOT_LOGIN };
      // setTimeout(() => {
      //   loginUser.value = {
      //     userName: "一键登录",
      //     id: 1,
      //     userRole: ACCESS_ENUM.ADMIN,
      //   };
      // }, 3000);
    }
    return loginUser.value;
  }

  return { loginUser, setLoginUser, fetchLoginUser };
});
