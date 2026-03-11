import request from '@/utils/request'

// 查询大学生/教员列表
export function listTutors(query) {
  return request({
    url: '/system/tutors/list',
    method: 'get',
    params: query
  })
}

// 查询大学生/教员详细
export function getTutors(id) {
  return request({
    url: '/system/tutors/' + id,
    method: 'get'
  })
}

// 新增大学生/教员
export function addTutors(data) {
  return request({
    url: '/system/tutors',
    method: 'post',
    data: data
  })
}

// 修改大学生/教员
export function updateTutors(data) {
  return request({
    url: '/system/tutors',
    method: 'put',
    data: data
  })
}

// 删除大学生/教员
export function delTutors(id) {
  return request({
    url: '/system/tutors/' + id,
    method: 'delete'
  })
}
