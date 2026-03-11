<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工作标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入工作标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类：0-家教, 1-助教, 2-派发, 3-其他" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入分类：0-家教, 1-助教, 2-派发, 3-其他"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日结薪水" prop="salaryDay">
        <el-input
          v-model="queryParams.salaryDay"
          placeholder="请输入日结薪水"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作具体日期" prop="workDate">
        <el-date-picker clearable
          v-model="queryParams.workDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择工作具体日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="具体时间段" prop="workTime">
        <el-input
          v-model="queryParams.workTime"
          placeholder="请输入具体时间段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作详细地址" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入工作详细地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域区号" prop="districtId">
        <el-input
          v-model="queryParams.districtId"
          placeholder="请输入区域区号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系人姓名" prop="contacts">
        <el-input
          v-model="queryParams.contacts"
          placeholder="请输入联系人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:jobs:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:jobs:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:jobs:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:jobs:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="jobsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日结工作主键ID" align="center" prop="id" />
      <el-table-column label="工作标题" align="center" prop="title" />
      <el-table-column label="分类：0-家教, 1-助教, 2-派发, 3-其他" align="center" prop="category" />
      <el-table-column label="日结薪水" align="center" prop="salaryDay" />
      <el-table-column label="工作具体日期" align="center" prop="workDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="具体时间段" align="center" prop="workTime" />
      <el-table-column label="工作详细地址" align="center" prop="location" />
      <el-table-column label="区域区号" align="center" prop="districtId" />
      <el-table-column label="联系人姓名" align="center" prop="contacts" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="工作具体要求内容" align="center" prop="description" />
      <el-table-column label="状态：0-招募中, 1-已满员, 2-已结束" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:jobs:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:jobs:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改兼职日结工作对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工作标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入工作标题" />
        </el-form-item>
        <el-form-item label="分类：0-家教, 1-助教, 2-派发, 3-其他" prop="category">
          <el-input v-model="form.category" placeholder="请输入分类：0-家教, 1-助教, 2-派发, 3-其他" />
        </el-form-item>
        <el-form-item label="日结薪水" prop="salaryDay">
          <el-input v-model="form.salaryDay" placeholder="请输入日结薪水" />
        </el-form-item>
        <el-form-item label="工作具体日期" prop="workDate">
          <el-date-picker clearable
            v-model="form.workDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择工作具体日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="具体时间段" prop="workTime">
          <el-input v-model="form.workTime" placeholder="请输入具体时间段" />
        </el-form-item>
        <el-form-item label="工作详细地址" prop="location">
          <el-input v-model="form.location" placeholder="请输入工作详细地址" />
        </el-form-item>
        <el-form-item label="区域区号" prop="districtId">
          <el-input v-model="form.districtId" placeholder="请输入区域区号" />
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contacts">
          <el-input v-model="form.contacts" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="工作具体要求内容" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listJobs, getJobs, delJobs, addJobs, updateJobs } from "@/api/system/jobs"

export default {
  name: "Jobs",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 兼职日结工作表格数据
      jobsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        category: null,
        salaryDay: null,
        workDate: null,
        workTime: null,
        location: null,
        districtId: null,
        contacts: null,
        phone: null,
        description: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "工作标题不能为空", trigger: "blur" }
        ],
        salaryDay: [
          { required: true, message: "日结薪水不能为空", trigger: "blur" }
        ],
        workDate: [
          { required: true, message: "工作具体日期不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "工作详细地址不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询兼职日结工作列表 */
    getList() {
      this.loading = true
      listJobs(this.queryParams).then(response => {
        this.jobsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        category: null,
        salaryDay: null,
        workDate: null,
        workTime: null,
        location: null,
        districtId: null,
        contacts: null,
        phone: null,
        description: null,
        status: null,
        createTime: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加兼职日结工作"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getJobs(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改兼职日结工作"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateJobs(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addJobs(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除兼职日结工作编号为"' + ids + '"的数据项？').then(function() {
        return delJobs(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/jobs/export', {
        ...this.queryParams
      }, `jobs_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
