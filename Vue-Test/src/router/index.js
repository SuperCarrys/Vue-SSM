import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
    path: '/',
    name: '员工信息管理',
    component: () => import(/* webpackChunkName: "about" */ '../views/index.vue'),
    redirect: '/queryDept',
    children: [
      {
        path: '/queryDept',
        name: '员工信息',
        component: () => import(/* webpackChunkName: "about" */ '../views/QueryDept.vue')
      },
      {
        path: '/addDept',
        name: '添加员工',
        component: () => import(/* webpackChunkName: "about" */ '../views/AddDept.vue')
      },
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
