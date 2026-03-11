import request from '@/utils/request'

// 查询用户实名认证列表
export function listAuth(query) {
  return request({
    url: '/system/auth/list',
    method: 'get',
    params: query
  })
}

// 查询用户实名认证详细
export function getAuth(id) {
  return request({
    url: '/system/auth/' + id,
    method: 'get'
  })
}

// 新增用户实名认证
export function addAuth(data) {
  return request({
    url: '/system/auth',
    method: 'post',
    data: data
  })
}

// 修改用户实名认证
export function updateAuth(data) {
  return request({
    url: '/system/auth',
    method: 'put',
    data: data
  })
}

// 删除用户实名认证
export function delAuth(id) {
  return request({
    url: '/system/auth/' + id,
    method: 'delete'
  })
}
