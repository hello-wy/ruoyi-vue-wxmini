import request from '@/utils/request'

// 查询资料中心数据列表
export function listMaterial(query) {
  return request({
    url: '/system/material/list',
    method: 'get',
    params: query
  })
}

// 查询资料中心数据详细
export function getMaterial(id) {
  return request({
    url: '/system/material/' + id,
    method: 'get'
  })
}

// 新增资料中心数据
export function addMaterial(data) {
  return request({
    url: '/system/material',
    method: 'post',
    data: data
  })
}

// 修改资料中心数据
export function updateMaterial(data) {
  return request({
    url: '/system/material',
    method: 'put',
    data: data
  })
}

// 删除资料中心数据
export function delMaterial(id) {
  return request({
    url: '/system/material/' + id,
    method: 'delete'
  })
}
