import request from '@/utils/request'

// 查询沙龙活动信息主列表
export function listInfo(query) {
  return request({
    url: '/system/info/list',
    method: 'get',
    params: query
  })
}

// 查询沙龙活动信息主详细
export function getInfo(id) {
  return request({
    url: '/system/info/' + id,
    method: 'get'
  })
}

// 新增沙龙活动信息主
export function addInfo(data) {
  return request({
    url: '/system/info',
    method: 'post',
    data: data
  })
}

// 修改沙龙活动信息主
export function updateInfo(data) {
  return request({
    url: '/system/info',
    method: 'put',
    data: data
  })
}

// 删除沙龙活动信息主
export function delInfo(id) {
  return request({
    url: '/system/info/' + id,
    method: 'delete'
  })
}
