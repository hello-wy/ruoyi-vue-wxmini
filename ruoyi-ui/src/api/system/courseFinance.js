import request from '@/utils/request'

// 查询当前用户课程返现汇总
export function getMyCashbackSummary() {
  return request({
    url: '/system/course-finance/cashback/my-summary',
    method: 'get'
  })
}

// 查询当前用户课程返现记录
export function listMyCashbackRecords(query) {
  return request({
    url: '/system/course-finance/cashback/my-records',
    method: 'get',
    params: query
  })
}
