<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="问卷标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入问卷标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="surveyList">
      <el-table-column label="问卷标题" align="left" prop="title" min-width="260" show-overflow-tooltip />
      <el-table-column label="编码" align="center" prop="code" min-width="220" show-overflow-tooltip />
      <el-table-column label="题数" align="center" prop="totalQuestions" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '启用' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发送数" align="center" prop="sentCount" width="90" />
      <el-table-column label="作答数" align="center" prop="answeredCount" width="90" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="170" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleContent(scope.row)" v-hasPermi="['system:survey:query']">内容</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-promotion" @click="handleDistribute(scope.row)" v-hasPermi="['system:survey:distribute']">分发</el-button>
          <el-button size="mini" type="text" icon="el-icon-user" @click="handleAssignments(scope.row)" v-hasPermi="['system:survey:query']">用户</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-drawer title="问卷内容" :visible.sync="contentOpen" size="50%" append-to-body>
      <div v-if="currentSurvey" class="drawer-body">
        <h3>{{ currentSurvey.title }}</h3>
        <p class="muted">{{ currentSurvey.description }}</p>
        <el-divider />
        <div v-for="question in currentSurvey.questions" :key="question.questionId" class="question-block">
          <div class="question-title">
            <span>{{ question.questionNo }}. {{ question.content }}</span>
            <el-tag size="mini">{{ question.questionType }}</el-tag>
          </div>
          <div v-if="question.options && question.options.length" class="option-list">
            <el-tag v-for="option in question.options" :key="option.value" size="mini" class="option-tag">{{ option.label }}</el-tag>
          </div>
          <div v-if="question.children && question.children.length" class="child-list">
            <div v-for="child in question.children" :key="child.questionId" class="child-question">
              <span>{{ child.questionNo }}. {{ child.content }}</span>
              <div class="option-list">
                <el-tag v-for="option in child.options" :key="option.value" size="mini" class="option-tag">{{ option.label }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>

    <el-dialog title="分发问卷" :visible.sync="distributeOpen" width="760px" append-to-body>
      <el-form :model="distributeForm" label-width="80px" size="small">
        <el-form-item label="问卷">
          <span>{{ currentSurvey && currentSurvey.title }}</span>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="distributeForm.lectureId" filterable remote clearable placeholder="搜索课程" :remote-method="searchLectures" :loading="lectureLoading" style="width: 100%">
            <el-option v-for="item in lectureOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户">
          <el-input v-model="userQuery.keyword" placeholder="输入手机号或真实姓名" clearable style="width: 260px" @keyup.enter.native="searchUsers" />
          <el-button type="primary" size="mini" icon="el-icon-search" @click="searchUsers">搜索</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="userTable" v-loading="userLoading" :data="userList" @selection-change="handleUserSelection" height="260">
        <el-table-column type="selection" width="55" />
        <el-table-column label="姓名" prop="realName" min-width="120" />
        <el-table-column label="昵称" prop="userName" min-width="120" />
        <el-table-column label="手机号" prop="phone" min-width="140" />
      </el-table>
      <pagination v-show="userTotal > 0" :total="userTotal" :page.sync="userQuery.pageNum" :limit.sync="userQuery.pageSize" @pagination="searchUsers" />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDistribute">确 定</el-button>
        <el-button @click="distributeOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-drawer title="分发用户" :visible.sync="assignmentOpen" size="65%" append-to-body>
      <div class="drawer-body">
        <el-form :model="assignmentQuery" size="small" :inline="true">
          <el-form-item label="课程">
            <el-select v-model="assignmentQuery.lectureId" filterable remote clearable placeholder="搜索课程" :remote-method="searchLectures" :loading="lectureLoading" style="width: 220px">
              <el-option v-for="item in lectureOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="assignmentQuery.status" clearable placeholder="全部" style="width: 120px">
              <el-option label="未提交" :value="0" />
              <el-option label="已提交" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="用户">
            <el-input v-model="assignmentQuery.keyword" placeholder="手机号/真实姓名" clearable @keyup.enter.native="loadAssignments" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="loadAssignments">搜索</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="assignmentLoading" :data="assignmentList">
          <el-table-column label="课程" prop="courseName" min-width="160" show-overflow-tooltip />
          <el-table-column label="姓名" prop="realName" min-width="100" />
          <el-table-column label="昵称" prop="userName" min-width="100" />
          <el-table-column label="手机号" prop="phone" min-width="130" />
          <el-table-column label="状态" prop="statusLabel" width="90" />
          <el-table-column label="分发时间" prop="assignedAt" width="170" />
          <el-table-column label="提交时间" prop="submittedAt" width="170" />
          <el-table-column label="操作" width="90" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleAnswerDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="assignmentTotal > 0" :total="assignmentTotal" :page.sync="assignmentQuery.pageNum" :limit.sync="assignmentQuery.pageSize" @pagination="loadAssignments" />
      </div>
    </el-drawer>

    <el-drawer title="答题详情" :visible.sync="answerOpen" size="55%" append-to-body>
      <div v-if="answerDetail" class="drawer-body">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="问卷">{{ answerDetail.form && answerDetail.form.title }}</el-descriptions-item>
          <el-descriptions-item label="课程">{{ answerDetail.course && answerDetail.course.name }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ answerDetail.user && answerDetail.user.realName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ answerDetail.user && answerDetail.user.phone }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ answerDetail.statusLabel }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ answerDetail.submittedAt || '-' }}</el-descriptions-item>
        </el-descriptions>
        <el-table :data="answerDetail.answers" style="margin-top: 16px">
          <el-table-column label="题号" prop="questionNo" width="80" />
          <el-table-column label="题目" prop="questionContent" min-width="260" show-overflow-tooltip />
          <el-table-column label="答案" min-width="180">
            <template slot-scope="scope">
              {{ scope.row.answerLabel || scope.row.answerText || scope.row.answerValue || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listLectures } from '@/api/system/lectures'
import { distributeSurvey, getSurvey, getSurveyAssignmentDetail, listSurvey, listSurveyAssignments, searchSurveyUsers } from '@/api/system/survey'

export default {
  name: 'Survey',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      surveyList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: null
      },
      currentSurvey: null,
      contentOpen: false,
      distributeOpen: false,
      distributeForm: { lectureId: null, userInfoIds: [] },
      lectureLoading: false,
      lectureOptions: [],
      userLoading: false,
      userList: [],
      userTotal: 0,
      userQuery: { pageNum: 1, pageSize: 10, keyword: null },
      assignmentOpen: false,
      assignmentLoading: false,
      assignmentList: [],
      assignmentTotal: 0,
      assignmentQuery: { pageNum: 1, pageSize: 10, lectureId: null, status: null, keyword: null },
      answerOpen: false,
      answerDetail: null
    }
  },
  created() {
    this.getList()
    this.searchLectures('')
  },
  methods: {
    getList() {
      this.loading = true
      listSurvey(this.queryParams).then(response => {
        this.surveyList = response.rows || []
        this.total = response.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleContent(row) {
      getSurvey(row.formId).then(response => {
        this.currentSurvey = response.data
        this.contentOpen = true
      })
    },
    handleDistribute(row) {
      getSurvey(row.formId).then(response => {
        this.currentSurvey = response.data
        this.distributeForm = { lectureId: null, userInfoIds: [] }
        this.userQuery = { pageNum: 1, pageSize: 10, keyword: null }
        this.userList = []
        this.userTotal = 0
        this.distributeOpen = true
      })
    },
    searchLectures(query) {
      this.lectureLoading = true
      listLectures({ pageNum: 1, pageSize: 20, name: query }).then(response => {
        this.lectureOptions = response.rows || []
      }).finally(() => {
        this.lectureLoading = false
      })
    },
    searchUsers() {
      this.userLoading = true
      searchSurveyUsers(this.userQuery).then(response => {
        this.userList = response.rows || []
        this.userTotal = response.total || 0
      }).finally(() => {
        this.userLoading = false
      })
    },
    handleUserSelection(selection) {
      this.distributeForm.userInfoIds = selection.map(item => item.userInfoId)
    },
    submitDistribute() {
      if (!this.distributeForm.lectureId) {
        this.$modal.msgWarning('请选择课程')
        return
      }
      if (!this.distributeForm.userInfoIds.length) {
        this.$modal.msgWarning('请选择用户')
        return
      }
      distributeSurvey(this.currentSurvey.formId, this.distributeForm).then(response => {
        const data = response.data || {}
        this.$modal.msgSuccess(`分发完成：新增${data.createdCount || 0}人，跳过${data.skippedCount || 0}人`)
        this.distributeOpen = false
        this.getList()
      })
    },
    handleAssignments(row) {
      this.currentSurvey = row
      this.assignmentQuery = { pageNum: 1, pageSize: 10, lectureId: null, status: null, keyword: null }
      this.assignmentOpen = true
      this.loadAssignments()
    },
    loadAssignments() {
      this.assignmentLoading = true
      listSurveyAssignments(this.currentSurvey.formId, this.assignmentQuery).then(response => {
        this.assignmentList = response.rows || []
        this.assignmentTotal = response.total || 0
      }).finally(() => {
        this.assignmentLoading = false
      })
    },
    handleAnswerDetail(row) {
      getSurveyAssignmentDetail(row.assignmentId).then(response => {
        this.answerDetail = response.data
        this.answerOpen = true
      })
    }
  }
}
</script>

<style scoped>
.drawer-body {
  padding: 0 24px 24px;
}
.muted {
  color: #909399;
  line-height: 1.6;
}
.question-block {
  margin-bottom: 18px;
}
.question-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
  margin-bottom: 8px;
}
.option-list {
  margin-top: 6px;
}
.option-tag {
  margin-right: 6px;
  margin-bottom: 6px;
}
.child-list {
  margin-top: 10px;
  padding-left: 16px;
  border-left: 2px solid #ebeef5;
}
.child-question {
  margin-bottom: 10px;
}
</style>
