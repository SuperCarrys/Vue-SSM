<template>
    <div>
        <el-header style="text-align: right; font-size: 12px">
            <el-dropdown>
                <i class="el-icon-setting" style="margin-right: 15px"></i>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>xx</el-dropdown-item>
                    <el-dropdown-item>xx</el-dropdown-item>
                    <el-dropdown-item>xx</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
            <span>xxx</span>
        </el-header>
        <el-table
                :data="tableData"
                border
                style="width: 100%">
            <el-table-column
                    fixed
                    prop="empId"
                    label="empId"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="empName"
                    label="empName"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="gender"
                    label="gender"
                    width="120"><template slot-scope="scope">{{ scope.row.gender === 'M' ? '男' : '女' }}</template>
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="email"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="department.deptName"
                    label="deptName"
                    width="120">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="100">
                <template slot-scope="scope">
                    <el-button @click="handleClick(scope.row),dialogVisible = true" type="text" size="small">编辑</el-button>
                    <el-button @click="deleteDept(scope.row)" type="text" size="small">删除</el-button>
                </template>

            </el-table-column>

        </el-table>

        <el-pagination
                background
                layout="prev, pager, next"
                :page-size="pageSize"
                :total= "total"
                @current-change="page"><!--绑click事件-->
        </el-pagination>


        <!--保存员工信息模态框-->
        <el-dialog title="员工修改" :visible.sync="dialogVisible" width="30%"  :close-on-click-modal="false">
            <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="80px" class="demo-ruleForm">
                <el-form-item label="empName:" prop="empName" label-width="30%">
                    {{ruleForm.empName}}
                </el-form-item>

                <el-form-item label="email:" prop="email" label-width="30%">
                    <el-input type="text" v-model="ruleForm.email"></el-input>
                </el-form-item>

                <el-form-item label="gender:" prop="gender" label-width="30%">
                    <el-radio-group v-model="ruleForm.gender">
                        <el-radio v-model="ruleForm.gender" label="M">男</el-radio>
                        <el-radio v-model="ruleForm.gender" label="F">女</el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item label="deptName" prop="dId" label-width="30%">
                    <el-select v-model="ruleForm.dId"  class="filter-item">
                        <el-option v-for="(item, index) in deptNames"
                                   :key="index"
                                   :value="item.deptId"
                                   :label="item.deptName"
                                   >{{item.deptName}}</el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary"  @click="submitForm('ruleForm') ,dialogVisible = false">保存</el-button>
                <el-button @click="closeDialog() ,dialogVisible = false">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                dialogVisible: false,
                pageSize: 5,
                total: 1,
                tableData: [], //当前页信息
                deptNames: [], //部门下拉框
                ruleForm:{     //保存员工信息模态框
                    empId:'',
                    empName: '',
                    email: '',
                    gender: '',
                    dId: '',
                },
                ruleForm2:{

                },
                rules: {
                    email: [
                        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                    ],
                    gender: [
                        { required: true, message: '请选择性别', trigger: 'change' }
                    ],
                    dId: [
                        { required: true, message: '请选择部门', trigger: 'change' }
                    ],
                }
            }
        },


        methods: {
            //保存模态框中的数据
            submitForm(formName){
                const _this = this /*_this表示当前的vue对象*/
                this.$refs[formName].validate((valid) => { /*本行的this表示submitForm方法*/
                    if (valid) {
                        _this.$delete( _this.ruleForm, 'department' )
                        var empId = _this.ruleForm.empId
                        _this.$delete( _this.ruleForm, 'empId' )
                        console.log(_this.ruleForm)
                        console.log(this.$qs.stringify(_this.ruleForm, { indices: false }))
                        var string = this.$qs.stringify(_this.ruleForm, { indices: false })

                        axios.put('http://localhost:8081/emp/' + empId , string).then(function (resp) {
                            if (resp.data.code == 100){
                                console.log(resp)

                                location.reload();

                            }else {
                                if (resp.data.extend.errorFields.email != undefined) {
                                    //显示邮箱错误信息
                                    alert(resp.data.extend.errorFields.email)
                                }
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //为保存员工信息模态框赋值
            handleClick(row) {
                /*this.$router.push('/updateDept')*/
                console.log(row)
                const vm = this
                vm.ruleForm = row
                console.log(vm.ruleForm);
            },
            //跳转页面
            page(currentPage){
                const vm = this
                axios.get('http://localhost:8081/emps?pn='+currentPage).then(function(resp){
                    console.log(resp)
                    vm.tableData = resp.data.extend.pageInfo.list
                    vm.pageSize = resp.data.extend.pageInfo.pageSize
                    vm.total = resp.data.extend.pageInfo.total
                }).catch(function (error) {
                    console.log(error)
                })
            },
            //关闭模态框
            closeDialog() {
                this. xxx = '';//清空数据
            },

            //删除操作
            deleteDept(row){
                console.log(row)
                var empId = row.empId
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    axios.delete("http://localhost:8081/emp/"+ empId ).then(function (resp) {
                        console.log(resp)
                        location.reload();
                    }).catch(function (error) {
                        console.log(error)
                    })
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },


        created() {
            const vm = this
            axios.get("http://localhost:8081/depts").then(function (resp) {
                console.log(resp)
                vm.deptNames = resp.data.extend.depts
            }).catch(function (error) {
                console.log(error)
            })

            axios.get("http://localhost:8081/emps?pn=1").then(function (resp) {
                console.log(resp)
                vm.tableData = resp.data.extend.pageInfo.list
                vm.pageSize = resp.data.extend.pageInfo.size
                vm.total = resp.data.extend.pageInfo.total
                console.log(vm.tableData)
            }).catch(function (error) {
                console.log(error)
            })
        },
    }
</script>