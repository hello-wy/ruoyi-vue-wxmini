<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联user表的主键ID" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入关联user表的主键ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入职称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="isCertified">
        <el-select v-model="queryParams.isCertified" placeholder="请选择审核状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_tutor_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="薪资要求" prop="salary">
        <el-input
          v-model="queryParams.salary"
          placeholder="请输入薪资要求"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input
          v-model="queryParams.major"
          placeholder="请输入专业"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="就读/毕业院校" prop="school">
        <el-input
          v-model="queryParams.school"
          placeholder="请输入就读/毕业院校"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学历枚举：0-本科, 1-硕士, 2-博士" prop="degree">
        <el-select v-model="queryParams.degree" placeholder="请选择学历枚举：0-本科, 1-硕士, 2-博士" clearable>
          <el-option
            v-for="dict in dict.type.sys_degree"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createDate">
        <el-date-picker clearable
          v-model="queryParams.createDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间" prop="updateDate">
        <el-date-picker clearable
          v-model="queryParams.updateDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择更新时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实名认证真实姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入实名认证真实姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号码" prop="idCard">
        <el-input
          v-model="queryParams.idCard"
          placeholder="请输入身份证号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生活区域" prop="live">
        <el-input
          v-model="queryParams.live"
          placeholder="请输入生活区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="work">
        <el-input
          v-model="queryParams.work"
          placeholder="请输入${comment}"
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
          v-hasPermi="['system:tutors:add']"
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
          v-hasPermi="['system:tutors:edit']"
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
          v-hasPermi="['system:tutors:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:tutors:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tutorsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教员表主键ID" align="center" prop="id" />
      <el-table-column label="关联user表的主键ID" align="center" prop="uid" />
      <el-table-column label="职称" align="center" prop="title" />
      <el-table-column label="证书图片url" align="center" prop="certificates" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.certificates" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="可授科目数组" align="center" prop="subjects">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_subject" :value="scope.row.subjects ? scope.row.subjects.split(',') : []"/>
        </template>
      </el-table-column>
      <el-table-column label="可授区域数组" align="center" prop="areas" />
      <el-table-column label="授课方式" align="center" prop="methods">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_methods" :value="scope.row.methods ? scope.row.methods.split(',') : []"/>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="isCertified">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_tutor_status" :value="scope.row.isCertified"/>
        </template>
      </el-table-column>
      <el-table-column label="薪资要求" align="center" prop="salary" />
      <el-table-column label="经历/履历" align="center" prop="experience" />
      <el-table-column label="专业" align="center" prop="major" />
      <el-table-column label="就读/毕业院校" align="center" prop="school" />
      <el-table-column label="学历枚举：0-本科, 1-硕士, 2-博士" align="center" prop="degree">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_degree" :value="scope.row.degree"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实名认证真实姓名" align="center" prop="realName" />
      <el-table-column label="身份证号码" align="center" prop="idCard" />
      <el-table-column label="个人评价" align="center" prop="selfJudge" />
      <el-table-column label="证书" align="center" prop="certificate" />
      <el-table-column label="生活区域" align="center" prop="live" />
      <el-table-column label="${comment}" align="center" prop="work" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tutors:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tutors:remove']"
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

    <!-- 添加或修改大学生/教员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="证书图片url" prop="certificates">
          <image-upload v-model="form.certificates"/>
        </el-form-item>
        <el-form-item label="可授科目数组" prop="subjects">
          <el-checkbox-group v-model="form.subjects">
            <el-checkbox
              v-for="dict in dict.type.sys_subject"
              :key="dict.value"
              :label="dict.value">
              {{dict.label}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="授课方式" prop="methods">
          <el-checkbox-group v-model="form.methods">
            <el-checkbox
              v-for="dict in dict.type.sys_methods"
              :key="dict.value"
              :label="dict.value">
              {{dict.label}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="审核状态" prop="isCertified">
          <el-radio-group v-model="form.isCertified">
            <el-radio
              v-for="dict in dict.type.sys_tutor_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="薪资要求" prop="salary">
          <el-input v-model="form.salary" placeholder="请输入薪资要求" />
        </el-form-item>
        <el-form-item label="经历/履历" prop="experience">
          <el-input v-model="form.experience" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="就读/毕业院校" prop="school">
          <el-input v-model="form.school" placeholder="请输入就读/毕业院校" />
        </el-form-item>
        <el-form-item label="学历枚举：0-本科, 1-硕士, 2-博士" prop="degree">
          <el-radio-group v-model="form.degree">
            <el-radio
              v-for="dict in dict.type.sys_degree"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="创建时间" prop="createDate">
          <el-date-picker clearable
            v-model="form.createDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updateDate">
          <el-date-picker clearable
            v-model="form.updateDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择更新时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实名认证真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入实名认证真实姓名" />
        </el-form-item>
        <el-form-item label="身份证号码" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号码" />
        </el-form-item>
        <el-form-item label="个人评价" prop="selfJudge">
          <el-input v-model="form.selfJudge" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="证书" prop="certificate">
          <el-input v-model="form.certificate" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="生活区域" prop="live">
          <el-input v-model="form.live" placeholder="请输入生活区域" />
        </el-form-item>
        <el-form-item label="${comment}" prop="work">
          <el-input v-model="form.work" placeholder="请输入${comment}" />
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
import { listTutors, getTutors, delTutors, addTutors, updateTutors } from "@/api/system/tutors"

export default {
  name: "Tutors",
  dicts: ['sys_tutor_status', 'sys_subject', 'sys_degree', 'sys_methods'],
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
      // 大学生/教员表格数据
      tutorsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uid: null,
        title: null,
        certificates: null,
        subjects: null,
        areas: null,
        methods: null,
        isCertified: null,
        salary: null,
        experience: null,
        major: null,
        school: null,
        degree: null,
        createDate: null,
        updateDate: null,
        realName: null,
        idCard: null,
        selfJudge: null,
        certificate: null,
        live: null,
        work: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        uid: [
          { required: true, message: "关联user表的主键ID不能为空", trigger: "blur" }
        ],
        title: [
          { required: true, message: "职称不能为空", trigger: "blur" }
        ],
        certificates: [
          { required: true, message: "证书图片url不能为空", trigger: "blur" }
        ],
        subjects: [
          { required: true, message: "可授科目数组不能为空", trigger: "blur" }
        ],
        major: [
          { required: true, message: "专业不能为空", trigger: "blur" }
        ],
        school: [
          { required: true, message: "就读/毕业院校不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询大学生/教员列表 */
    getList() {
      this.loading = true
      listTutors(this.queryParams).then(response => {
        this.tutorsList = response.rows
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
        uid: null,
        title: null,
        certificates: null,
        subjects: [],
        areas: null,
        methods: [],
        isCertified: null,
        salary: null,
        experience: null,
        major: null,
        school: null,
        degree: null,
        createDate: null,
        updateDate: null,
        realName: null,
        idCard: null,
        selfJudge: null,
        certificate: null,
        live: null,
        work: null
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
      this.title = "添加大学生/教员"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTutors(id).then(response => {
        this.form = response.data
        this.form.subjects = this.form.subjects.split(",")
        this.form.methods = this.form.methods.split(",")
        this.open = true
        this.title = "修改大学生/教员"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.subjects = this.form.subjects.join(",")
          this.form.methods = this.form.methods.join(",")
          if (this.form.id != null) {
            updateTutors(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTutors(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除大学生/教员编号为"' + ids + '"的数据项？').then(function() {
        return delTutors(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tutors/export', {
        ...this.queryParams
      }, `tutors_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
