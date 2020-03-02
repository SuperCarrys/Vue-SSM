<template>
    <div>
        <table>
            <tr>
                <th>#</th>
                <th>empName</th>
                <th>gender</th>
                <th>email</th>
                <th>deptName</th>
            </tr>
            <tr v-for="item in books">
                <th>{{item.empId}}</th>
                <th>{{item.empName}}</th>
                <th>{{item.gender}}</th>
                <th>{{item.email}}</th>
                <th>{{item.department.deptName}}</th>
            </tr>
        </table>
    </div>
</template>

<script>
    export default {
        name: "Book",
        data(){
            return{
                books:[
                ]
            }
        },
        /*初始化*/
        created() {
            const vm = this
            axios.get("http://localhost:8081/empss").then(function (resp) {
                vm.books = resp.data
            })
        }
    }
</script>

<style scoped>

</style>