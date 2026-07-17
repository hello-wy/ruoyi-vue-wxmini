<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree" />所属部门
                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>
            <el-tab-pane label="课程返现" name="cashback">
              <el-row :gutter="20" class="cashback-summary">
                <el-col :span="6" :xs="12">
                  <div class="cashback-stat"><div>累计获得</div><strong>{{ cashbackSummary.earned || 0 }}</strong></div>
                </el-col>
                <el-col :span="6" :xs="12">
                  <div class="cashback-stat"><div>已冲回</div><strong>{{ cashbackSummary.reversed || 0 }}</strong></div>
                </el-col>
                <el-col :span="6" :xs="12">
                  <div class="cashback-stat"><div>已扣减</div><strong>{{ cashbackSummary.deducted || 0 }}</strong></div>
                </el-col>
                <el-col :span="6" :xs="12">
                  <div class="cashback-stat"><div>可用返现</div><strong>{{ cashbackSummary.available || 0 }}</strong></div>
                </el-col>
              </el-row>
              <el-table v-loading="cashbackLoading" :data="cashbackRecords">
                <el-table-column label="时间" align="center" prop="createTime" width="180" />
                <el-table-column label="类型" align="center" prop="entryType" width="100" />
                <el-table-column label="金额" align="center" prop="amount" />
                <el-table-column label="订单号" align="center" prop="orderNo" min-width="160" />
                <el-table-column label="备注" align="center" prop="remark" min-width="160" show-overflow-tooltip />
              </el-table>
              <pagination
                v-show="cashbackTotal > 0"
                :total="cashbackTotal"
                :page.sync="cashbackQuery.pageNum"
                :limit.sync="cashbackQuery.pageSize"
                @pagination="getCashbackRecords"
              />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar"
import userInfo from "./userInfo"
import resetPwd from "./resetPwd"
import { getUserProfile } from "@/api/system/user"
import { getMyCashbackSummary, listMyCashbackRecords } from "@/api/system/courseFinance"

export default {
  name: "Profile",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo",
      cashbackLoading: false,
      cashbackSummary: {},
      cashbackRecords: [],
      cashbackTotal: 0,
      cashbackQuery: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data
        this.roleGroup = response.roleGroup
        this.postGroup = response.postGroup
      })
    },
    handleTabClick(tab) {
      if (tab.name === "cashback") {
        this.getCashbackSummary()
        this.getCashbackRecords()
      }
    },
    getCashbackSummary() {
      getMyCashbackSummary().then(response => {
        this.cashbackSummary = response.data || {}
      })
    },
    getCashbackRecords() {
      this.cashbackLoading = true
      listMyCashbackRecords(this.cashbackQuery).then(response => {
        this.cashbackRecords = response.rows
        this.cashbackTotal = response.total
      }).finally(() => {
        this.cashbackLoading = false
      })
    }
  }
}
</script>

<style scoped>
.cashback-summary {
  margin-bottom: 20px;
}

.cashback-stat {
  padding: 16px;
  text-align: center;
  background: #f5f7fa;
}

.cashback-stat strong {
  display: block;
  margin-top: 8px;
  font-size: 20px;
  color: #303133;
}
</style>
