<template>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

        <el-form-item label="empName" prop="empName" style="width: 20%">
            <el-input v-model="ruleForm.empName"></el-input>
        </el-form-item>

        <el-form-item label="email" prop="email" style="width: 20%">
            <el-input v-model="ruleForm.email"></el-input>
        </el-form-item>

        <el-form-item label="gender" prop="gender">
            <el-radio-group v-model="ruleForm.gender">
                <el-radio label="M" >男</el-radio>
                <el-radio label="F" >女</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item label="deptName" prop="dId">
            <el-select v-model="ruleForm.dId" placeholder="请选择">
                <el-option v-for="(item, index) in deptNames"
                           :key="index"
                           :value="item.deptId"
                           :label="item.deptName">{{item.deptName}}</el-option>
            </el-select>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        data() {
            return {
                deptNames: [],
                ruleForm: {
                    empName: '',
                    email: '',
                    gender: '',
                    dId: '',
                },
                rules: {
                    empName: [
                        { required: true, message: '请输入员工姓名', trigger: 'blur' },
                        {  min: 2, max: 5, pattern: (/^[\u4E00-\u9FA5]+$/), message: '用户名必须是2到5位中文', trigger: 'blur' }

                    ],
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
            };
        },
        methods: {
            submitForm(formName) {
                const _this = this /*_this表示当前的vue对象*/
                this.$refs[formName].validate((valid) => { /*本行的this表示submitForm方法*/
                    if (valid) {
                        console.log(_this.ruleForm)
                        console.log(this.$qs.stringify(_this.ruleForm, { indices: false }))
                        var string = this.$qs.stringify(_this.ruleForm, { indices: false })

                        axios.post('http://localhost:8081/checkuser?empName='+_this.ruleForm.empName).then(function (resp) {
                            if (resp.data.code==100){
                                axios.post('http://localhost:8081/emp', string).then(function (resp) {
                                    if (resp.data.code == 100){
                                        console.log(resp)
                                        _this.$router.push('/queryDept')
                                    }else {
                                        if (resp.data.extend.errorFields.email != undefined) {
                                            //显示邮箱错误信息
                                            alert(resp.data.extend.errorFields.email)
                                        }
                                        if (resp.data.extend.errorFields.empName != undefined) {
                                            //显示员工姓名错误信息
                                            alert(resp.data.extend.errorFields.empName)
                                        }
                                    }
                                })
                            }else{
                                alert("用户名不可用")
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
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
        },
    }
</script>