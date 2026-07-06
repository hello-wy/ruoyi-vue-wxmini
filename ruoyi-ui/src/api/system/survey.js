import request from '@/utils/request'

export function listSurvey(query) {
  return request({
    url: '/system/survey/list',
    method: 'get',
    params: query
  })
}

export function getSurvey(formId) {
  return request({
    url: '/system/survey/' + formId,
    method: 'get'
  })
}

export function searchSurveyUsers(query) {
  return request({
    url: '/system/survey/wx-users',
    method: 'get',
    params: query
  })
}

export function distributeSurvey(formId, data) {
  return request({
    url: '/system/survey/' + formId + '/distribute',
    method: 'post',
    data
  })
}

export function listSurveyAssignments(formId, query) {
  return request({
    url: '/system/survey/' + formId + '/assignments',
    method: 'get',
    params: query
  })
}

export function getSurveyAssignmentDetail(assignmentId) {
  return request({
    url: '/system/survey/assignments/' + assignmentId,
    method: 'get'
  })
}
