import request from '@/utils/request'

// 查询兼职日结工作列表
export function listJobs(query) {
  return request({
    url: '/system/jobs/list',
    method: 'get',
    params: query
  })
}

// 查询兼职日结工作详细
export function getJobs(id) {
  return request({
    url: '/system/jobs/' + id,
    method: 'get'
  })
}

// 新增兼职日结工作
export function addJobs(data) {
  return request({
    url: '/system/jobs',
    method: 'post',
    data: data
  })
}

// 修改兼职日结工作
export function updateJobs(data) {
  return request({
    url: '/system/jobs',
    method: 'put',
    data: data
  })
}

// 删除兼职日结工作
export function delJobs(id) {
  return request({
    url: '/system/jobs/' + id,
    method: 'delete'
  })
}
