import request from '@/utils/request'

// 查询学员列表
export function listStudent(query) {
  return request({
    url: '/system/student/list',
    method: 'get',
    params: query
  })
}
