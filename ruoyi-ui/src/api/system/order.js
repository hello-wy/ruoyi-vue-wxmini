import request from '@/utils/request'

// 查询通用交易订单（包含沙龙和讲座）列表
export function listOrder(query) {
  return request({
    url: '/system/order/list',
    method: 'get',
    params: query
  })
}

// 查询通用交易订单（包含沙龙和讲座）详细
export function getOrder(id) {
  return request({
    url: '/system/order/' + id,
    method: 'get'
  })
}

// 新增通用交易订单（包含沙龙和讲座）
export function addOrder(data) {
  return request({
    url: '/system/order',
    method: 'post',
    data: data
  })
}

// 修改通用交易订单（包含沙龙和讲座）
export function updateOrder(data) {
  return request({
    url: '/system/order',
    method: 'put',
    data: data
  })
}

// 删除通用交易订单（包含沙龙和讲座）
export function delOrder(id) {
  return request({
    url: '/system/order/' + id,
    method: 'delete'
  })
}
