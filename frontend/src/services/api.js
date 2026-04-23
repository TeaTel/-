import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL + '/api' || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加JWT令牌
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      let message = '请求失败'
      
      if (data && data.message) {
        message = data.message
      } else if (status === 401) {
        message = '未授权，请重新登录'
        // 清除本地存储的token
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        // 跳转到登录页
        window.location.href = '/login'
      } else if (status === 403) {
        message = '权限不足'
      } else if (status === 404) {
        message = '请求的资源不存在'
      } else if (status >= 500) {
        message = '服务器内部错误'
      }
      
      console.error(`请求错误 ${status}:`, message)
      return Promise.reject({ message, status })
    } else if (error.request) {
      // 请求已发出但没有收到响应
      console.error('网络错误:', error.message)
      return Promise.reject({ message: '网络连接失败，请检查网络设置' })
    } else {
      // 请求配置错误
      console.error('请求配置错误:', error.message)
      return Promise.reject({ message: '请求配置错误' })
    }
  }
)

// 用户相关API
export const userApi = {
  // 用户注册
  register(data) {
    return api.post('/users/register', data)
  },
  
  // 用户登录
  login(data) {
    return api.post('/users/login', data)
  },
  
  // 获取用户信息
  getUserInfo() {
    return api.get('/users/info')
  },
  
  // 更新用户资料
  updateProfile(data) {
    return api.put('/users/profile', data)
  },
  
  // 修改密码
  changePassword(data) {
    return api.put('/users/password', data)
  }
}

// 商品相关API
export const productApi = {
  // 获取商品列表
  getProducts(params) {
    return api.get('/products', { params })
  },
  
  // 获取商品详情
  getProductDetail(id) {
    return api.get(`/products/${id}`)
  },
  
  // 发布商品
  createProduct(data) {
    return api.post('/products', data)
  },
  
  // 更新商品
  updateProduct(id, data) {
    return api.put(`/products/${id}`, data)
  },
  
  // 删除商品
  deleteProduct(id) {
    return api.delete(`/products/${id}`)
  },
  
  // 获取我的商品
  getMyProducts() {
    return api.get('/products/my')
  }
}

// 订单相关API
export const orderApi = {
  // 创建订单
  createOrder(data) {
    return api.post('/orders', data)
  },
  
  // 获取订单详情
  getOrderDetail(id) {
    return api.get(`/orders/${id}`)
  },
  
  // 获取买家订单
  getBuyerOrders(params) {
    return api.get('/orders/buyer', { params })
  },
  
  // 获取卖家订单
  getSellerOrders(params) {
    return api.get('/orders/seller', { params })
  },
  
  // 更新订单状态
  updateOrderStatus(id, status) {
    return api.put(`/orders/${id}/status`, { status })
  },
  
  // 取消订单
  cancelOrder(id) {
    return api.put(`/orders/${id}/cancel`)
  },
  
  // 确认订单
  confirmOrder(id) {
    return api.put(`/orders/${id}/confirm`)
  }
}

// 消息相关API
export const messageApi = {
  // 发送消息
  sendMessage(data) {
    return api.post('/messages', data)
  },
  
  // 获取对话记录
  getConversation(otherUserId) {
    return api.get(`/messages/conversation/${otherUserId}`)
  },
  
  // 获取联系人列表
  getContacts() {
    return api.get('/messages/contacts')
  },
  
  // 获取未读消息
  getUnreadMessages() {
    return api.get('/messages/unread')
  },
  
  // 标记消息为已读
  markAsRead(id) {
    return api.put(`/messages/${id}/read`)
  },
  
  // 标记对话为已读
  markConversationAsRead(senderId) {
    return api.put(`/messages/conversation/${senderId}/read`)
  },
  
  // 获取未读消息数量
  getUnreadCount() {
    return api.get('/messages/unread/count')
  }
}

// 分类相关API
export const categoryApi = {
  // 获取分类列表
  getCategories(params) {
    return api.get('/categories', { params })
  },
  
  // 获取分类树
  getCategoryTree() {
    return api.get('/categories/tree')
  },
  
  // 获取分类详情
  getCategoryDetail(id) {
    return api.get(`/categories/${id}`)
  },
  
  // 创建分类
  createCategory(data) {
    return api.post('/categories', data)
  },
  
  // 更新分类
  updateCategory(id, data) {
    return api.put(`/categories/${id}`, data)
  },
  
  // 删除分类
  deleteCategory(id) {
    return api.delete(`/categories/${id}`)
  }
}

export default api