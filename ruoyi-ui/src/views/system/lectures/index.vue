<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="课程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入课程名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="上课地址" prop="location">
        <el-input v-model="queryParams.location" placeholder="请输入上课地址" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['system:lectures:add']" type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['system:lectures:edit']" type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPermi="['system:lectures:remove']" type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="lecturesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程ID" align="center" prop="id" width="90" />
      <el-table-column label="课程名称" align="center" prop="name" min-width="160" />
      <el-table-column label="开课时间" align="center" prop="time" width="170">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.time, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endDate" width="170">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上课地址" align="center" prop="location" min-width="220" show-overflow-tooltip />
      <el-table-column label="封面目录ID" align="center" prop="coverId" width="110" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button v-hasPermi="['system:lectures:edit']" size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button v-hasPermi="['system:lectures:remove']" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="92px">
        <el-form-item label="课程名称" prop="name">
          <el-select
            v-model="form.name"
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入课程名称"
            style="width: 100%"
            @change="handleCourseNameChange"
          >
            <el-option v-for="item in lectureTemplates" :key="item.name" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="开课时间" prop="time">
          <el-date-picker
            v-model="form.time"
            clearable
            type="datetime"
            value-format="yyyy-MM-dd HH:mm"
            placeholder="请选择开课时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker
            v-model="form.endDate"
            clearable
            type="datetime"
            value-format="yyyy-MM-dd HH:mm"
            placeholder="请选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="上课地址" prop="location">
          <el-input v-model="form.location" placeholder="请输入上课地址" />
        </el-form-item>
        <el-alert
          v-if="selectedTemplate"
          type="info"
          :closable="false"
          show-icon
          title="已匹配已有课程配置，封面、详情、讲师与费用信息会随课程名称一起保存。"
        />
        <el-alert
          v-else-if="form.name"
          type="warning"
          :closable="false"
          show-icon
          title="这是新的课程名称，系统不会自动匹配封面与详情配置。"
        />
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addLectures,
  delLectures,
  getLectures,
  listLectures,
  listLectureTemplates,
  updateLectures
} from '@/api/system/lectures'

const TEMPLATE_FIELDS = [
  'speaker',
  'cover',
  'coverId',
  'detail',
  'registrationFee',
  'deposit',
  'requiresEnrollment'
]

export default {
  name: 'Lectures',
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      lecturesList: [],
      lectureTemplates: [],
      title: '',
      open: false,
      selectedTemplate: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        location: null
      },
      form: {},
      rules: {
        name: [{ required: true, message: '课程名称不能为空', trigger: 'change' }],
        time: [{ required: true, message: '开课时间不能为空', trigger: 'change' }],
        endDate: [{ required: true, message: '结束时间不能为空', trigger: 'change' }],
        location: [{ required: true, message: '上课地址不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getTemplateList()
  },
  methods: {
    getList() {
      this.loading = true
      listLectures(this.queryParams).then(response => {
        this.lecturesList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getTemplateList() {
      listLectureTemplates().then(response => {
        this.lectureTemplates = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        name: null,
        time: null,
        endDate: null,
        location: null,
        speaker: null,
        cover: null,
        coverId: null,
        detail: null,
        registrationFee: null,
        deposit: null,
        requiresEnrollment: true
      }
      this.selectedTemplate = null
      this.resetForm('form')
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增课程排期'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLectures(id).then(response => {
        this.form = response.data
        this.selectedTemplate = this.findTemplateByName(this.form.name)
        this.open = true
        this.title = '修改课程排期'
      })
    },
    handleCourseNameChange(name) {
      const template = this.findTemplateByName(name)
      this.selectedTemplate = template
      if (template) {
        this.applyTemplate(template)
        return
      }
      this.clearTemplateFields()
    },
    findTemplateByName(name) {
      return this.lectureTemplates.find(item => item.name === name) || null
    },
    applyTemplate(template) {
      TEMPLATE_FIELDS.forEach(field => {
        this.$set(this.form, field, template[field])
      })
    },
    clearTemplateFields() {
      TEMPLATE_FIELDS.forEach(field => {
        this.$set(this.form, field, field === 'requiresEnrollment' ? true : null)
      })
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) return
        const template = this.findTemplateByName(this.form.name)
        if (template && !this.form.id) this.applyTemplate(template)
        const request = this.form.id != null ? updateLectures : addLectures
        request(this.form).then(() => {
          this.$modal.msgSuccess(this.form.id != null ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
          this.getTemplateList()
        })
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除课程排期编号为"' + ids + '"的数据项？').then(() => {
        return delLectures(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
