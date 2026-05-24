<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="微信用户ID" prop="wechatUid">
        <el-input
          v-model="queryParams.wechatUid"
          placeholder="请输入微信用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台用户ID" prop="systemUid">
        <el-input
          v-model="queryParams.systemUid"
          placeholder="请输入平台用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地理位置文本" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入地理位置文本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经纬度位置" prop="geo">
        <el-input
          v-model="queryParams.geo"
          placeholder="请输入经纬度位置"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区域" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入区域"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="家教单的名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入家教单的名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年级" prop="grade">
        <el-select v-model="queryParams.grade" placeholder="请选择年级" clearable>
          <el-option
            v-for="dict in dict.type.sys_class"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="辅导方式" prop="methods">
        <el-select v-model="queryParams.methods" placeholder="请选择辅导方式" clearable>
          <el-option
            v-for="dict in dict.type.sys_methods"
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
      <el-form-item label="请家教订单状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择请家教订单状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_parent_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="每周几到几" prop="dayOfWeek">
        <el-input
          v-model="queryParams.dayOfWeek"
          placeholder="请输入每周几到几"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
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
          v-hasPermi="['system:parents:add']"
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
          v-hasPermi="['system:parents:edit']"
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
          v-hasPermi="['system:parents:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:parents:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="parentsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教员表主键ID" align="center" prop="id" />
      <el-table-column label="微信用户ID" align="center" prop="wechatUid" />
      <el-table-column label="平台用户ID" align="center" prop="systemUid" />
      <el-table-column label="地理位置文本" align="center" prop="location" />
      <el-table-column label="经纬度位置" align="center" prop="geo" />
      <el-table-column label="区域" align="center" prop="region" />
      <el-table-column label="家教单的名称" align="center" prop="name" />
      <el-table-column label="年级" align="center" prop="grade">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_class" :value="scope.row.grade"/>
        </template>
      </el-table-column>
      <el-table-column label="科目" align="center" prop="subject">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_subject" :value="scope.row.subject ? scope.row.subject.split(',') : []"/>
        </template>
      </el-table-column>
      <el-table-column label="辅导方式" align="center" prop="methods">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_methods" :value="scope.row.methods"/>
        </template>
      </el-table-column>
      <el-table-column label="家长家教需求文本" align="center" prop="requirements" />
      <el-table-column label="家长孩子情况简介" align="center" prop="brief" />
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
      <el-table-column label="请家教订单状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_parent_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="每周几到几" align="center" prop="dayOfWeek" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:parents:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-shopping-cart-full"
            @click="handleCreateOrder(scope.row)"
            v-hasPermi="['system:parents:edit']"
          >生成订单</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:parents:remove']"
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

    <!-- 添加或修改家教订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="微信用户ID" prop="wechatUid">
          <el-input v-model="form.wechatUid" placeholder="请输入微信用户ID" />
        </el-form-item>
        <el-form-item label="平台用户ID" prop="systemUid">
          <el-input v-model="form.systemUid" placeholder="请输入平台用户ID" />
        </el-form-item>
        <el-form-item label="地理位置文本" prop="location">
          <el-input v-model="form.location" placeholder="请输入地理位置文本" />
        </el-form-item>
        <el-form-item label="经纬度位置" prop="geo">
          <el-input v-model="form.geo" placeholder="请输入经纬度位置" />
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" placeholder="请输入区域" />
        </el-form-item>
        <el-form-item label="家教单的名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入家教单的名称" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-radio-group v-model="form.grade">
            <el-radio
              v-for="dict in dict.type.sys_class"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="科目" prop="subject">
          <el-checkbox-group v-model="form.subject">
            <el-checkbox
              v-for="dict in dict.type.sys_subject"
              :key="dict.value"
              :label="dict.value">
              {{dict.label}}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="辅导方式" prop="methods">
          <el-radio-group v-model="form.methods">
            <el-radio
              v-for="dict in dict.type.sys_methods"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="家长家教需求文本" prop="requirements">
          <el-input v-model="form.requirements" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="家长孩子情况简介" prop="brief">
          <el-input v-model="form.brief" type="textarea" placeholder="请输入内容" />
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
        <el-form-item label="请家教订单状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_parent_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="每周几到几" prop="dayOfWeek">
          <el-input v-model="form.dayOfWeek" placeholder="请输入每周几到几" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="生成家教待支付订单" :visible.sync="orderOpen" width="800px" append-to-body>
      <el-form :model="orderQueryParams" ref="orderQueryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="姓名" prop="realName">
          <el-input
            v-model="orderQueryParams.realName"
            placeholder="请输入教员姓名"
            clearable
            @keyup.enter.native="getTutorList"
          />
        </el-form-item>
        <el-form-item label="科目" prop="subjects">
          <el-input
            v-model="orderQueryParams.subjects"
            placeholder="请输入科目"
            clearable
            @keyup.enter.native="getTutorList"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="getTutorList">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetTutorQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="orderLoading" :data="tutorList">
        <el-table-column label="教员ID" align="center" prop="id" width="160" />
        <el-table-column label="用户ID" align="center" prop="uid" width="120" />
        <el-table-column label="姓名" align="center" prop="realName" width="100" />
        <el-table-column label="学校" align="center" prop="school" />
        <el-table-column label="科目" align="center" prop="subjects" />
        <el-table-column label="操作" align="center" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="submitTutoringOrder(scope.row)"
            >选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="tutorTotal>0"
        :total="tutorTotal"
        :page.sync="orderQueryParams.pageNum"
        :limit.sync="orderQueryParams.pageSize"
        @pagination="getTutorList"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listParents, getParents, delParents, addParents, updateParents, createTutoringPendingOrder } from "@/api/system/parents"
import { listTutors } from "@/api/system/tutors"

export default {
  name: "Parents",
  dicts: ['sys_parent_status', 'sys_subject', 'sys_class', 'sys_methods'],
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
      // 家教订单表格数据
      parentsList: [],
      // 是否显示生成订单弹出层
      orderOpen: false,
      // 生成订单遮罩层
      orderLoading: false,
      // 当前生成订单的家长需求
      orderParent: null,
      // 教员表格数据
      tutorList: [],
      // 教员总条数
      tutorTotal: 0,
      // 教员查询参数
      orderQueryParams: {
        pageNum: 1,
        pageSize: 10,
        realName: null,
        subjects: null,
        status: 1
      },
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wechatUid: null,
        systemUid: null,
        location: null,
        geo: null,
        region: null,
        name: null,
        grade: null,
        subject: null,
        methods: null,
        requirements: null,
        brief: null,
        createDate: null,
        updateDate: null,
        status: null,
        dayOfWeek: null,
        startTime: null,
        endTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        wechatUid: [
          { required: true, message: "微信用户ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "家教单的名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "请家教订单状态不能为空", trigger: "change" }
        ],
        dayOfWeek: [
          { required: true, message: "每周几到几不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询家教订单列表 */
    getList() {
      this.loading = true
      listParents(this.queryParams).then(response => {
        this.parentsList = response.rows
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
        wechatUid: null,
        systemUid: null,
        location: null,
        geo: null,
        region: null,
        name: null,
        grade: null,
        subject: [],
        methods: null,
        requirements: null,
        brief: null,
        createDate: null,
        updateDate: null,
        status: null,
        dayOfWeek: null,
        startTime: null,
        endTime: null
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
      this.title = "添加家教订单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getParents(id).then(response => {
        this.form = response.data
        this.form.subject = this.form.subject.split(",")
        this.open = true
        this.title = "修改家教订单"
      })
    },
    /** 生成待支付订单按钮操作 */
    handleCreateOrder(row) {
      this.orderParent = row
      this.orderOpen = true
      this.orderQueryParams.pageNum = 1
      this.getTutorList()
    },
    /** 查询可绑定教员列表 */
    getTutorList() {
      this.orderLoading = true
      listTutors(this.orderQueryParams).then(response => {
        this.tutorList = response.rows
        this.tutorTotal = response.total
      }).finally(() => {
        this.orderLoading = false
      })
    },
    /** 重置教员查询 */
    resetTutorQuery() {
      this.resetForm("orderQueryForm")
      this.orderQueryParams.pageNum = 1
      this.getTutorList()
    },
    /** 选择教员并生成待支付订单 */
    submitTutoringOrder(row) {
      if (!this.orderParent || !this.orderParent.id) {
        this.$modal.msgError("请选择家长需求")
        return
      }
      createTutoringPendingOrder(this.orderParent.id, row.id).then(response => {
        const order = response.data || {}
        this.$modal.msgSuccess("已生成待支付订单：" + (order.orderNo || ""))
        this.orderOpen = false
        this.getList()
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.subject = this.form.subject.join(",")
          if (this.form.id != null) {
            updateParents(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addParents(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除家教订单编号为"' + ids + '"的数据项？').then(function() {
        return delParents(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/parents/export', {
        ...this.queryParams
      }, `parents_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
