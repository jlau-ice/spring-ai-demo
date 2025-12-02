import { defineStore } from 'pinia'
import { ref } from 'vue'
import { type LoginUserVO } from '@/api'
import { ACCESS_ENUM } from '@/access/accessEnum'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const loginUser = ref<LoginUserVO>({
    userName: '未登录',
    userRole: ACCESS_ENUM.NO_LOGIN,
  })
  async function fetchLoginUser() {
    loginUser.value = {
      userName: 'admin',
      userRole: ACCESS_ENUM.USER,
    }
  }
  async function fetchLogoutUser() {
    loginUser.value = {
      userName: '未登录',
      userRole: ACCESS_ENUM.NO_LOGIN,
    }
    await router.push('/login')
  }
  return { loginUser, fetchLoginUser,fetchLogoutUser }
})
