import request from '@/utils/request'

// 查询课程活动/讲座列表
export function listLectures(query) {
  return request({
    url: '/system/lectures/list',
    method: 'get',
    params: query
  })
}

// 查询已有课程模板
export function listLectureTemplates() {
  return request({
    url: '/system/lectures/templates',
    method: 'get'
  })
}

// 查询课程活动/讲座详细
export function getLectures(id) {
  return request({
    url: '/system/lectures/' + id,
    method: 'get'
  })
}

// 新增课程活动/讲座
export function addLectures(data) {
  return request({
    url: '/system/lectures',
    method: 'post',
    data: data
  })
}

// 修改课程活动/讲座
export function updateLectures(data) {
  return request({
    url: '/system/lectures',
    method: 'put',
    data: data
  })
}

// 删除课程活动/讲座
export function delLectures(id) {
  return request({
    url: '/system/lectures/' + id,
    method: 'delete'
  })
}
