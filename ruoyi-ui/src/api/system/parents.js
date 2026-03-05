import request from '@/utils/request'

// 查询家教订单列表
export function listParents(query) {
  return request({
    url: '/system/parents/list',
    method: 'get',
    params: query
  })
}

// 查询家教订单详细
export function getParents(id) {
  return request({
    url: '/system/parents/' + id,
    method: 'get'
  })
}

// 新增家教订单
export function addParents(data) {
  return request({
    url: '/system/parents',
    method: 'post',
    data: data
  })
}

// 修改家教订单
export function updateParents(data) {
  return request({
    url: '/system/parents',
    method: 'put',
    data: data
  })
}

// 删除家教订单
export function delParents(id) {
  return request({
    url: '/system/parents/' + id,
    method: 'delete'
  })
}
