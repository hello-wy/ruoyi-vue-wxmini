import request from '@/utils/request'

// 查询讲师风采列表
export function listProfile(query) {
  return request({
    url: '/system/profile/list',
    method: 'get',
    params: query
  })
}

// 查询讲师风采详细
export function getProfile(id) {
  return request({
    url: '/system/profile/' + id,
    method: 'get'
  })
}

// 新增讲师风采
export function addProfile(data) {
  return request({
    url: '/system/profile',
    method: 'post',
    data: data
  })
}

// 修改讲师风采
export function updateProfile(data) {
  return request({
    url: '/system/profile',
    method: 'put',
    data: data
  })
}

// 删除讲师风采
export function delProfile(id) {
  return request({
    url: '/system/profile/' + id,
    method: 'delete'
  })
}
