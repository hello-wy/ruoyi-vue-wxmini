<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="外部展示及支付网关使用的订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入外部展示及支付网关使用的订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="购买用户的唯一标识" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入购买用户的唯一标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联的沙龙ID" prop="salonId">
        <el-input
          v-model="queryParams.salonId"
          placeholder="请输入关联的沙龙ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联的讲座ID" prop="lectureId">
        <el-input
          v-model="queryParams.lectureId"
          placeholder="请输入关联的讲座ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际支付金额" prop="payAmount">
        <el-input
          v-model="queryParams.payAmount"
          placeholder="请输入实际支付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付方式，如：wechat_pay, alipay, offline" prop="payMethod">
        <el-input
          v-model="queryParams.payMethod"
          placeholder="请输入支付方式，如：wechat_pay, alipay, offline"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主要的用途/备注，如：报名听课、赞助商" prop="purpose">
        <el-input
          v-model="queryParams.purpose"
          placeholder="请输入主要的用途/备注，如：报名听课、赞助商"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际完成支付的时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择实际完成支付的时间">
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
          v-hasPermi="['system:order:add']"
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
          v-hasPermi="['system:order:edit']"
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
          v-hasPermi="['system:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:order:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="内部订单ID，主键" align="center" prop="id" />
      <el-table-column label="外部展示及支付网关使用的订单号" align="center" prop="orderNo" />
      <el-table-column label="购买用户的唯一标识" align="center" prop="userId" />
      <el-table-column label="业务类型：1-沙龙(salon订单)，2-讲座(lecture订单)" align="center" prop="orderType" />
      <el-table-column label="关联的沙龙ID" align="center" prop="salonId" />
      <el-table-column label="关联的讲座ID" align="center" prop="lectureId" />
      <el-table-column label="实际支付金额" align="center" prop="payAmount" />
      <el-table-column label="支付方式，如：wechat_pay, alipay, offline" align="center" prop="payMethod" />
      <el-table-column label="主要的用途/备注，如：报名听课、赞助商" align="center" prop="purpose" />
      <el-table-column label="支付状态：0-待支付，1-已支付，2-已退款，3-已取消" align="center" prop="payStatus" />
      <el-table-column label="实际完成支付的时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:order:remove']"
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

    <!-- 添加或修改通用交易订单（包含沙龙和讲座）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="外部展示及支付网关使用的订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入外部展示及支付网关使用的订单号" />
        </el-form-item>
        <el-form-item label="购买用户的唯一标识" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入购买用户的唯一标识" />
        </el-form-item>
        <el-form-item label="关联的沙龙ID" prop="salonId">
          <el-input v-model="form.salonId" placeholder="请输入关联的沙龙ID" />
        </el-form-item>
        <el-form-item label="关联的讲座ID" prop="lectureId">
          <el-input v-model="form.lectureId" placeholder="请输入关联的讲座ID" />
        </el-form-item>
        <el-form-item label="实际支付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入实际支付金额" />
        </el-form-item>
        <el-form-item label="支付方式，如：wechat_pay, alipay, offline" prop="payMethod">
          <el-input v-model="form.payMethod" placeholder="请输入支付方式，如：wechat_pay, alipay, offline" />
        </el-form-item>
        <el-form-item label="主要的用途/备注，如：报名听课、赞助商" prop="purpose">
          <el-input v-model="form.purpose" placeholder="请输入主要的用途/备注，如：报名听课、赞助商" />
        </el-form-item>
        <el-form-item label="实际完成支付的时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择实际完成支付的时间">
          </el-date-picker>
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
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/system/order"

export default {
  name: "Order",
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
      // 通用交易订单（包含沙龙和讲座）表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        orderType: null,
        salonId: null,
        lectureId: null,
        payAmount: null,
        payMethod: null,
        purpose: null,
        payStatus: null,
        payTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "外部展示及支付网关使用的订单号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "购买用户的唯一标识不能为空", trigger: "blur" }
        ],
        orderType: [
          { required: true, message: "业务类型：1-沙龙(salon订单)，2-讲座(lecture订单)不能为空", trigger: "change" }
        ],
        payAmount: [
          { required: true, message: "实际支付金额不能为空", trigger: "blur" }
        ],
        payStatus: [
          { required: true, message: "支付状态：0-待支付，1-已支付，2-已退款，3-已取消不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "订单创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "订单更新时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询通用交易订单（包含沙龙和讲座）列表 */
    getList() {
      this.loading = true
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows
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
        orderNo: null,
        userId: null,
        orderType: null,
        salonId: null,
        lectureId: null,
        payAmount: null,
        payMethod: null,
        purpose: null,
        payStatus: null,
        payTime: null,
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
      this.title = "添加通用交易订单（包含沙龙和讲座）"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改通用交易订单（包含沙龙和讲座）"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除通用交易订单（包含沙龙和讲座）编号为"' + ids + '"的数据项？').then(function() {
        return delOrder(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
