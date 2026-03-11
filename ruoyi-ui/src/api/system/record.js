import request from '@/utils/request'

// 查询讲座签到记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询讲座签到记录详细
export function getRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'get'
  })
}

// 新增讲座签到记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改讲座签到记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除讲座签到记录
export function delRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'delete'
  })
}
