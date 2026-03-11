import request from '@/utils/request'

// 查询问卷调查配置列表
export function listQuestionnaire(query) {
  return request({
    url: '/system/questionnaire/list',
    method: 'get',
    params: query
  })
}

// 查询问卷调查配置详细
export function getQuestionnaire(id) {
  return request({
    url: '/system/questionnaire/' + id,
    method: 'get'
  })
}

// 新增问卷调查配置
export function addQuestionnaire(data) {
  return request({
    url: '/system/questionnaire',
    method: 'post',
    data: data
  })
}

// 修改问卷调查配置
export function updateQuestionnaire(data) {
  return request({
    url: '/system/questionnaire',
    method: 'put',
    data: data
  })
}

// 删除问卷调查配置
export function delQuestionnaire(id) {
  return request({
    url: '/system/questionnaire/' + id,
    method: 'delete'
  })
}
