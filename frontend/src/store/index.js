import Vue from 'vue'
import Vuex from 'vuex'

import menu from '../moduls/menu';
import contracts from '../moduls/contracts';
import contragents from '../moduls/contragents'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    
    url: 'http://212.118.43.99:8080',
    
    token: '',
    tokenType: 'Bearer',
    name: '',
    fullname: '',

  },
  getters: {
  },
  mutations: {
      ADD_TOKEN(state, payload) {
        state.token = payload
        localStorage.setItem('token', "50")
        
      },
      ADD_NAME(state,payload) {
        state.name = payload
        state.fullname = state.name.split(' ', 2)
        localStorage.setItem('name', state.fullname)

      }

    
  },
  actions: {
  },

  modules: {
    menu,
    contracts,
    contragents,
  }
});

export default store;
