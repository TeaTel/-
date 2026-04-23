<template>
  <div class="register">
    <h2>注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="username">用户名</label>
        <input type="text" id="username" v-model="username" required />
        <div v-if="errors.username" class="error">{{ errors.username }}</div>
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="password" required />
        <div v-if="errors.password" class="error">{{ errors.password }}</div>
      </div>
      <div class="form-group">
        <label for="phone">手机号</label>
        <input type="tel" id="phone" v-model="phone" required />
        <div v-if="errors.phone" class="error">{{ errors.phone }}</div>
      </div>
      <div class="form-group">
        <label for="email">邮箱</label>
        <input type="email" id="email" v-model="email" required />
        <div v-if="errors.email" class="error">{{ errors.email }}</div>
      </div>
      <div v-if="registerError" class="error-message">{{ registerError }}</div>
      <div v-if="registerSuccess" class="success-message">注册成功！正在跳转到登录页面...</div>
      <button type="submit" :disabled="loading">
        {{ loading ? '注册中...' : '注册' }}
      </button>
      <div class="login-link">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import { userApi } from '../services/api'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const phone = ref('')
const email = ref('')
const loading = ref(false)
const registerError = ref('')
const registerSuccess = ref(false)
const errors = ref({
  username: '',
  password: '',
  phone: '',
  email: ''
})

function validateForm() {
  let isValid = true
  errors.value = { username: '', password: '', phone: '', email: '' }

  if (!username.value.trim()) {
    errors.value.username = '用户名不能为空'
    isValid = false
  } else if (username.value.length < 3) {
    errors.value.username = '用户名至少3个字符'
    isValid = false
  }

  if (!password.value) {
    errors.value.password = '密码不能为空'
    isValid = false
  } else if (password.value.length < 6) {
    errors.value.password = '密码至少6个字符'
    isValid = false
  }

  if (!phone.value) {
    errors.value.phone = '手机号不能为空'
    isValid = false
  } else if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    errors.value.phone = '请输入有效的手机号'
    isValid = false
  }

  if (!email.value) {
    errors.value.email = '邮箱不能为空'
    isValid = false
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
    errors.value.email = '请输入有效的邮箱地址'
    isValid = false
  }

  return isValid
}

async function handleRegister() {
  if (!validateForm()) {
    return
  }

  loading.value = true
  registerError.value = ''
  registerSuccess.value = false

  try {
    const result = await userApi.register({
      username: username.value,
      password: password.value,
      phone: phone.value,
      email: email.value
    })
    
    if (result.code === 200 || result.success) {
      registerSuccess.value = true
      registerError.value = ''
      
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      registerError.value = result.message || '注册失败，请稍后重试'
    }
  } catch (error) {
    console.error('注册错误:', error)
    registerError.value = error.message || '网络错误，请检查连接后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register {
  padding: 2rem;
  max-width: 400px;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  margin-bottom: 1.5rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.error {
  color: #f44336;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

.success-message {
  background-color: #e8f5e9;
  color: #2e7d32;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 1rem;
  font-size: 1rem;
  font-weight: 500;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.login-link {
  text-align: center;
  margin-top: 1rem;
  color: #666;
}

.login-link a {
  color: #4CAF50;
  text-decoration: none;
  font-weight: 500;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>