<template>
  <div class="profile">
    <NavBar />
    
    <main class="main-content">
      <div class="container">
        <div class="page-header">
          <h1 class="page-title">个人资料</h1>
          <p class="page-subtitle">管理您的个人信息和账户设置</p>
        </div>
        
        <div v-if="loading" class="loading-state">
          <div class="spinner"></div>
          <p>加载中...</p>
        </div>
        
        <div v-else-if="user" class="profile-container">
          <!-- 用户头像卡片 -->
          <div class="avatar-card">
            <div class="avatar-section">
              <div class="avatar-wrapper">
                <img :src="user.avatar || '/default-avatar.png'" :alt="user.username" class="avatar-image" />
                <button @click="changeAvatar" class="change-avatar-btn">
                  <span>更换头像</span>
                </button>
              </div>
              <h2 class="username">{{ user.username }}</h2>
              <p class="user-role">{{ user.isStudent ? '学生' : '教师' }}</p>
            </div>
          </div>
          
          <!-- 个人信息表单 -->
          <div class="info-card">
            <h3 class="card-title">基本信息</h3>
            <form @submit.prevent="updateProfile" class="info-form">
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">用户名</label>
                  <input type="text" :value="user.username" disabled class="form-input" />
                  <p class="form-hint">用户名不可修改</p>
                </div>
                
                <div class="form-group">
                  <label class="form-label">手机号</label>
                  <input type="tel" v-model="formData.phone" class="form-input" placeholder="请输入手机号" />
                </div>
                
                <div class="form-group">
                  <label class="form-label">邮箱</label>
                  <input type="email" v-model="formData.email" class="form-input" placeholder="请输入邮箱" />
                </div>
                
                <div class="form-group">
                  <label class="form-label">学校</label>
                  <input type="text" v-model="formData.school" class="form-input" placeholder="请输入学校名称" />
                </div>
                
                <div class="form-group full-width">
                  <label class="form-label">专业</label>
                  <input type="text" v-model="formData.major" class="form-input" placeholder="请输入专业名称" />
                </div>
                
                <div class="form-group full-width">
                  <label class="form-label">微信号</label>
                  <input type="text" v-model="formData.wechat" class="form-input" placeholder="选填，方便其他用户联系您" />
                </div>
                
                <div class="form-group full-width">
                  <label class="form-label">QQ号</label>
                  <input type="text" v-model="formData.qq" class="form-input" placeholder="选填" />
                </div>
              </div>
              
              <div v-if="successMessage" class="alert alert-success">
                {{ successMessage }}
              </div>
              
              <div v-if="errorMessage" class="alert alert-error">
                {{ errorMessage }}
              </div>
              
              <button type="submit" class="btn btn-primary btn-lg" :disabled="saving">
                {{ saving ? '保存中...' : '保存修改' }}
              </button>
            </form>
          </div>
          
          <!-- 账户统计 -->
          <div class="stats-card">
            <h3 class="card-title">我的统计</h3>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-value">{{ stats.productCount || 0 }}</div>
                <div class="stat-label">发布商品</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ stats.orderCount || 0 }}</div>
                <div class="stat-label">交易订单</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ stats.messageCount || 0 }}</div>
                <div class="stat-label">消息数量</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../store/auth'
import { userApi } from '../services/api'
import NavBar from '../components/NavBar.vue'

const authStore = useAuthStore()
const loading = ref(true)
const saving = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

const user = computed(() => authStore.user)

const formData = ref({
  phone: '',
  email: '',
  school: '',
  major: '',
  wechat: '',
  qq: ''
})

const stats = ref({
  productCount: 0,
  orderCount: 0,
  messageCount: 0
})

onMounted(() => {
  if (user.value) {
    formData.value.phone = user.value.phone || ''
    formData.value.email = user.value.email || ''
    formData.value.school = user.value.school || ''
    formData.value.major = user.value.major || ''
    formData.value.wechat = user.value.wechat || ''
    formData.value.qq = user.value.qq || ''
  }
  loading.value = false
})

function changeAvatar() {
  alert('头像上传功能开发中...')
}

async function updateProfile() {
  saving.value = true
  successMessage.value = ''
  errorMessage.value = ''
  
  try {
    const response = await userApi.updateProfile(formData.value)
    
    if (response.code === 200 || response.success) {
      successMessage.value = '个人资料更新成功！'
      
      const updatedUser = { ...user.value, ...formData.value }
      localStorage.setItem('user', JSON.stringify(updatedUser))
      authStore.user = updatedUser
      
      setTimeout(() => {
        successMessage.value = ''
      }, 3000)
    } else {
      errorMessage.value = response.message || '更新失败，请稍后重试'
    }
  } catch (error) {
    console.error('更新个人资料失败:', error)
    errorMessage.value = error.message || '网络错误，请稍后重试'
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.profile {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content {
  padding: 2rem 0;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 1rem;
}

.page-header {
  text-align: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2rem;
  color: #333;
  margin-bottom: 0.5rem;
}

.page-subtitle {
  color: #666;
  font-size: 1rem;
}

.loading-state {
  text-align: center;
  padding: 4rem;
  color: #666;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #4CAF50;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.profile-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.avatar-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
}

.avatar-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #4CAF50;
}

.change-avatar-btn {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(76, 175, 80, 0.9);
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: background 0.2s;
}

.change-avatar-btn:hover {
  background: #45a049;
}

.username {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.user-role {
  color: #666;
  margin: 0;
  font-size: 0.95rem;
}

.info-card, .stats-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.card-title {
  font-size: 1.25rem;
  color: #333;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f0f0f0;
}

.info-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-label {
  font-weight: 500;
  color: #555;
  font-size: 0.95rem;
}

.form-input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: #4CAF50;
}

.form-input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.form-hint {
  font-size: 0.85rem;
  color: #999;
  margin: 0;
}

.alert {
  padding: 1rem;
  border-radius: 8px;
  font-size: 0.95rem;
}

.alert-success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.alert-error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.btn-lg {
  padding: 0.875rem 2rem;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #45a049;
  transform: translateY(-1px);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  text-align: center;
}

.stat-item {
  padding: 1.5rem;
  background: #f9f9f9;
  border-radius: 8px;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: 700;
  color: #4CAF50;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #666;
  font-size: 0.95rem;
}
</style>
