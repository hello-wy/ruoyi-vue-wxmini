<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="用户ID" prop="uid">
        <el-input v-model="queryParams.uid" placeholder="请输入 user_id" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="queryParams.realName" placeholder="请输入真实姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="身份" prop="identity">
        <el-select v-model="queryParams.identity" placeholder="请选择身份" clearable>
          <el-option v-for="item in identityOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable>
          <el-option v-for="dict in dict.type.sys_tutor_status" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
        </el-select>
      </el-form-item>
      <el-form-item label="学校" prop="school">
        <el-input v-model="queryParams.school" placeholder="请输入学校" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input v-model="queryParams.major" placeholder="请输入专业" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学历" prop="degree">
        <el-select v-model="queryParams.degree" placeholder="请选择学历" clearable>
          <el-option v-for="dict in dict.type.sys_degree" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
        </el-select>
      </el-form-item>
      <el-form-item label="城市" prop="city">
        <el-input v-model="queryParams.city" placeholder="请输入城市" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:tutors:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:tutors:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:tutors:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:tutors:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tutorsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="160" />
      <el-table-column label="用户ID" align="center" prop="uid" width="120" />
      <el-table-column label="真实姓名" align="center" prop="realName" width="100" />
      <el-table-column label="身份" align="center" prop="identity" width="110">
        <template slot-scope="scope">
          <span>{{ getIdentityLabel(scope.row.identity) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="status" width="110">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_tutor_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="学校" align="center" prop="school" />
      <el-table-column label="专业" align="center" prop="major" />
      <el-table-column label="学历" align="center" prop="degree" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_degree" :value="scope.row.degree" />
        </template>
      </el-table-column>
      <el-table-column label="城市" align="center" prop="city" width="120" />
      <el-table-column label="授课方式" align="center" prop="methods" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_methods" :value="scope.row.methods" />
        </template>
      </el-table-column>
      <el-table-column label="可授科目" align="center" prop="subjects" min-width="180">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_subject" :value="scope.row.subjects ? scope.row.subjects.split(',') : []" />
        </template>
      </el-table-column>
      <el-table-column label="授课区域" align="center" prop="areas" min-width="160" show-overflow-tooltip />
      <el-table-column label="证书图片" align="center" prop="certificates" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.certificates" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="证书列表" align="center" prop="certificateList" min-width="180" show-overflow-tooltip />
      <el-table-column label="自我评价" align="center" prop="selfJudge" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:tutors:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleReview(scope.row, 1)" v-hasPermi="['system:tutors:review']">通过</el-button>
          <el-button size="mini" type="text" icon="el-icon-close" @click="handleReview(scope.row, 2)" v-hasPermi="['system:tutors:review']">拒绝</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:tutors:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="640px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户ID" prop="uid">
              <el-input v-model="form.uid" placeholder="请输入 user_info.user_id" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份" prop="identity">
              <el-select v-model="form.identity" placeholder="请选择身份" style="width: 100%">
                <el-option v-for="item in identityOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择审核状态" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_tutor_status" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历" prop="degree">
              <el-select v-model="form.degree" placeholder="请选择学历" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_degree" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学校" prop="school">
              <el-input v-model="form.school" placeholder="请输入学校" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业" prop="major">
              <el-input v-model="form.major" placeholder="请输入专业" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城市" prop="city">
              <el-input v-model="form.city" maxlength="15" placeholder="请输入城市" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授课方式" prop="methods">
              <el-select v-model="form.methods" placeholder="请选择授课方式" style="width: 100%">
                <el-option v-for="dict in dict.type.sys_methods" :key="dict.value" :label="dict.label" :value="parseInt(dict.value)" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="可授科目" prop="subjects">
              <el-checkbox-group v-model="form.subjects">
                <el-checkbox v-for="dict in dict.type.sys_subject" :key="dict.value" :label="String(dict.value)">{{ dict.label }}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="授课区域" prop="areas">
              <el-input v-model="form.areas" placeholder="请输入区域编码，逗号分隔" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="教学经历" prop="experience">
              <el-input v-model="form.experience" type="textarea" :rows="3" placeholder="请输入教学经历" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="证书列表" prop="certificateList">
              <el-input v-model="form.certificateList" type="textarea" :rows="2" placeholder="请输入证书列表" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="自我评价" prop="selfJudge">
              <el-input v-model="form.selfJudge" type="textarea" :rows="2" placeholder="请输入自我评价" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="证书图片" prop="certificates">
              <image-upload v-model="form.certificates" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTutors, getTutors, delTutors, addTutors, updateTutors, reviewTutors } from '@/api/system/tutors'

export default {
  name: 'Tutors',
  dicts: ['sys_tutor_status', 'sys_subject', 'sys_degree', 'sys_methods'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      tutorsList: [],
      title: '',
      open: false,
      identityOptions: [
        { label: '大学生教员', value: 0 },
        { label: '在职教师', value: 1 },
        { label: '其他', value: 2 }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uid: null,
        realName: null,
        identity: null,
        status: null,
        school: null,
        major: null,
        degree: null,
        city: null
      },
      form: {},
      rules: {
        uid: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
        identity: [{ required: true, message: '身份不能为空', trigger: 'change' }],
        status: [{ required: true, message: '审核状态不能为空', trigger: 'change' }],
        school: [{ required: true, message: '学校不能为空', trigger: 'blur' }],
        major: [{ required: true, message: '专业不能为空', trigger: 'blur' }],
        city: [{ required: true, message: '城市不能为空', trigger: 'blur' }],
        methods: [{ required: true, message: '授课方式不能为空', trigger: 'change' }],
        subjects: [{ required: true, message: '可授科目不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getIdentityLabel(value) {
      const found = this.identityOptions.find(item => item.value === Number(value))
      return found ? found.label : '未知'
    },
    getList() {
      this.loading = true
      listTutors(this.queryParams).then(response => {
        this.tutorsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        uid: null,
        identity: 0,
        certificates: null,
        subjects: [],
        areas: null,
        methods: null,
        status: 0,
        experience: null,
        major: null,
        school: null,
        degree: null,
        selfJudge: null,
        certificateList: null,
        city: null
      }
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
      this.title = '添加教员'
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTutors(id).then(response => {
        const data = response.data || {}
        this.form = {
          ...this.form,
          ...data,
          identity: data.identity === undefined || data.identity === null ? 0 : Number(data.identity),
          status: data.status === undefined || data.status === null ? 0 : Number(data.status),
          methods: data.methods === undefined || data.methods === null || data.methods === '' ? null : Number(data.methods),
          degree: data.degree === undefined || data.degree === null || data.degree === '' ? null : Number(data.degree),
          subjects: data.subjects ? String(data.subjects).split(',').map(item => item.trim()).filter(Boolean) : []
        }
        this.open = true
        this.title = '修改教员'
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const payload = {
          ...this.form,
          subjects: (this.form.subjects || []).join(',')
        }
        const request = payload.id != null ? updateTutors(payload) : addTutors(payload)
        request.then(() => {
          this.$modal.msgSuccess(payload.id != null ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleReview(row, status) {
      const statusText = status === 1 ? '通过' : '拒绝'
      this.$modal.confirm('是否确认' + statusText + '教员编号为"' + row.id + '"的申请？').then(() => {
        return reviewTutors({ id: row.id, status })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('审核操作成功')
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除教员编号为"' + ids + '"的数据项？').then(() => {
        return delTutors(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/tutors/export', {
        ...this.queryParams
      }, `tutors_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
