<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="72px">
      <el-form-item label="学员" prop="studentName">
        <el-input
          v-model="queryParams.studentName"
          placeholder="请输入学员姓名/昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程" prop="lectureName">
        <el-input
          v-model="queryParams.lectureName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="剩余学籍" prop="remain">
        <el-input
          v-model="queryParams.remain"
          placeholder="请输入剩余学籍数"
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
          v-hasPermi="['system:enrollment:add']"
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
          v-hasPermi="['system:enrollment:edit']"
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
          v-hasPermi="['system:enrollment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:enrollment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="enrollmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学员" min-width="150" prop="studentName" show-overflow-tooltip>
        <template slot-scope="scope">
          <div>{{ scope.row.studentName || '-' }}</div>
          <div class="table-sub-text">ID：{{ scope.row.uid }}</div>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="phone" width="130" />
      <el-table-column label="课程" min-width="180" prop="lectureName" show-overflow-tooltip>
        <template slot-scope="scope">
          <div>{{ scope.row.lectureName || '-' }}</div>
          <div class="table-sub-text">ID：{{ scope.row.lectureId }}</div>
        </template>
      </el-table-column>
      <el-table-column label="课程时间" align="center" width="150">
        <template slot-scope="scope">
          {{ parseTime(scope.row.lectureTime, '{y}-{m}-{d} {h}:{i}') || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="地点" min-width="140" prop="location" show-overflow-tooltip />
      <el-table-column label="总学籍" align="center" prop="total" width="90" />
      <el-table-column label="剩余学籍" align="center" prop="remain" width="90" />
      <el-table-column label="已分享" align="center" prop="sharedCount" width="90" />
      <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-share"
            :disabled="!scope.row.remain"
            @click="handleShare(scope.row)"
            v-hasPermi="['system:enrollment:edit']"
          >分享</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:enrollment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:enrollment:remove']"
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

    <!-- 添加或修改学籍信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="绑定学员" prop="uid">
          <el-select
            v-model="form.uid"
            filterable
            remote
            clearable
            reserve-keyword
            :remote-method="searchStudents"
            :loading="studentLoading"
            placeholder="请输入学员姓名或手机号搜索"
            style="width: 100%"
          >
            <el-option
              v-for="item in studentOptions"
              :key="item.id"
              :label="formatStudentOption(item)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="绑定课程" prop="lectureId">
          <el-select
            v-model="form.lectureId"
            filterable
            remote
            clearable
            reserve-keyword
            :remote-method="searchLectures"
            :loading="lectureLoading"
            placeholder="请输入课程名称搜索"
            style="width: 100%"
          >
            <el-option
              v-for="item in lectureOptions"
              :key="item.id"
              :label="formatLectureOption(item)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="总学籍数" prop="total">
          <el-input-number v-model="form.total" :min="0" :precision="0" controls-position="right" style="width: 100%" @change="handleTotalChange" />
        </el-form-item>
        <el-form-item label="剩余学籍数" prop="remain">
          <el-input-number v-model="form.remain" :min="0" :max="form.total || 0" :precision="0" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分享学籍对话框 -->
    <el-dialog title="分享学籍" :visible.sync="shareOpen" width="560px" append-to-body>
      <el-form ref="shareForm" :model="shareForm" :rules="shareRules" label-width="96px">
        <el-form-item label="分享课程">
          <div>{{ shareSource.lectureName || '-' }}</div>
          <div class="form-tip">当前剩余 {{ shareSource.remain || 0 }} 个学籍</div>
        </el-form-item>
        <el-form-item label="接收学员" prop="targetUid">
          <el-select
            v-model="shareForm.targetUid"
            filterable
            remote
            clearable
            reserve-keyword
            :remote-method="searchStudents"
            :loading="studentLoading"
            placeholder="请输入学员姓名或手机号搜索"
            style="width: 100%"
          >
            <el-option
              v-for="item in shareStudentOptions"
              :key="item.id"
              :label="formatStudentOption(item)"
              :value="item.id"
              :disabled="item.id === shareForm.sourceUid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分享数量" prop="count">
          <el-input-number v-model="shareForm.count" :min="1" :max="shareSource.remain || 1" :precision="0" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShare">确 定</el-button>
        <el-button @click="shareOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEnrollment, getEnrollment, delEnrollment, addEnrollment, updateEnrollment, shareEnrollment } from "@/api/system/enrollment"
import { listStudent } from "@/api/system/student"
import { listLectures } from "@/api/system/lectures"
import { parseTime } from "@/utils/ruoyi"

export default {
  name: "Enrollment",
  data() {
    const validateRemain = (rule, value, callback) => {
      if (value === null || value === undefined) {
        callback(new Error("剩余学籍数不能为空"))
        return
      }
      if (this.form.total !== null && this.form.total !== undefined && Number(value) > Number(this.form.total)) {
        callback(new Error("剩余学籍数不能大于总学籍数"))
        return
      }
      callback()
    }
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      enrollmentList: [],
      title: "",
      open: false,
      studentLoading: false,
      lectureLoading: false,
      studentOptions: [],
      shareStudentOptions: [],
      lectureOptions: [],
      shareOpen: false,
      shareSource: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentName: null,
        phone: null,
        lectureName: null,
        remain: null
      },
      form: {},
      shareForm: {},
      rules: {
        uid: [
          { required: true, message: "绑定学员不能为空", trigger: "change" }
        ],
        lectureId: [
          { required: true, message: "绑定课程不能为空", trigger: "change" }
        ],
        total: [
          { required: true, message: "总学籍数不能为空", trigger: "change" }
        ],
        remain: [
          { validator: validateRemain, trigger: "change" }
        ]
      },
      shareRules: {
        targetUid: [
          { required: true, message: "接收学员不能为空", trigger: "change" }
        ],
        count: [
          { required: true, message: "分享数量不能为空", trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.searchStudents("")
    this.searchLectures("")
  },
  methods: {
    parseTime,
    /** 查询学籍信息列表 */
    getList() {
      this.loading = true
      listEnrollment(this.queryParams).then(response => {
        this.enrollmentList = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    searchStudents(query) {
      this.studentLoading = true
      const keyword = (query || '').trim()
      const params = { pageNum: 1, pageSize: 20 }
      if (/^\d+$/.test(keyword)) {
        params.phone = keyword
      } else {
        params.realName = keyword
      }
      listStudent(params).then(response => {
        const rows = response.rows || []
        this.studentOptions = rows
        this.shareStudentOptions = rows
      }).finally(() => {
        this.studentLoading = false
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
    formatStudentOption(item) {
      const name = item.displayName || item.realName || item.userName || `学员${item.id}`
      return item.phone ? `${name}（${item.phone}）` : `${name}（ID:${item.id}）`
    },
    formatLectureOption(item) {
      const time = parseTime(item.time, "{y}-{m}-{d} {h}:{i}")
      return time ? `${item.name || '未命名课程'}（${time}）` : (item.name || `课程${item.id}`)
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        uid: null,
        lectureId: null,
        total: 1,
        remain: 1,
        createTime: null,
        updateTime: null,
        isDeleted: 0
      }
      this.resetForm("form")
    },
    handleTotalChange(value) {
      if (this.form.remain === null || this.form.remain === undefined || Number(this.form.remain) > Number(value)) {
        this.form.remain = value
      }
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加学籍信息"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getEnrollment(id).then(response => {
        this.form = response.data
        this.ensureSelectedOptions(response.data)
        this.open = true
        this.title = "修改学籍信息"
      })
    },
    ensureSelectedOptions(row = {}) {
      if (row.uid && !this.studentOptions.some(item => item.id === row.uid)) {
        this.studentOptions.unshift({ id: row.uid, displayName: row.studentName, phone: row.phone })
      }
      if (row.lectureId && !this.lectureOptions.some(item => item.id === row.lectureId)) {
        this.lectureOptions.unshift({ id: row.lectureId, name: row.lectureName, time: row.lectureTime })
      }
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.remain === null || this.form.remain === undefined) {
            this.form.remain = this.form.total
          }
          const request = this.form.id != null ? updateEnrollment(this.form) : addEnrollment(this.form)
          request.then(() => {
            this.$modal.msgSuccess(this.form.id != null ? "修改成功" : "新增成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleShare(row) {
      this.shareSource = row
      this.shareForm = {
        sourceUid: row.uid,
        targetUid: null,
        lectureId: row.lectureId,
        count: 1
      }
      this.shareOpen = true
    },
    submitShare() {
      this.$refs["shareForm"].validate(valid => {
        if (!valid) return
        if (this.shareForm.sourceUid === this.shareForm.targetUid) {
          this.$modal.msgError("不能分享给当前学员")
          return
        }
        shareEnrollment(this.shareForm).then(() => {
          this.$modal.msgSuccess("分享成功")
          this.shareOpen = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除学籍信息编号为"' + ids + '"的数据项？').then(function() {
        return delEnrollment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/enrollment/export', {
        ...this.queryParams
      }, `enrollment_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.table-sub-text {
  margin-top: 2px;
  font-size: 12px;
  color: #909399;
}

.form-tip {
  font-size: 12px;
  color: #909399;
}
</style>
