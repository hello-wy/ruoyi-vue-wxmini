import request from '@/utils/request'

// 查询学籍信息列表
export function listEnrollment(query) {
  return request({
    url: '/system/enrollment/list',
    method: 'get',
    params: query
  })
}

// 查询学籍信息详细
export function getEnrollment(id) {
  return request({
    url: '/system/enrollment/' + id,
    method: 'get'
  })
}

// 新增学籍信息
export function addEnrollment(data) {
  return request({
    url: '/system/enrollment',
    method: 'post',
    data: data
  })
}

// 修改学籍信息
export function updateEnrollment(data) {
  return request({
    url: '/system/enrollment',
    method: 'put',
    data: data
  })
}

// 删除学籍信息
export function delEnrollment(id) {
  return request({
    url: '/system/enrollment/' + id,
    method: 'delete'
  })
}

// 分享学籍信息
export function shareEnrollment(data) {
  return request({
    url: '/system/enrollment/share',
    method: 'post',
    data: data
  })
}
