import Vue from 'vue'
import App from './App.vue'
import {store} from './store'
import vuetify from './plugins/vuetify'
import router from "./router/router"
import '@fortawesome/fontawesome-free/css/all.css'
import Vuetify from 'vuetify/lib'

Vue.config.productionTip = false

new Vue({
  store,
  router,
  vuetify,
  
  render: h => h(App)
}).$mount('#app')

