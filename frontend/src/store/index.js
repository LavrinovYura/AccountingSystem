import Vue from 'vue'
import Vuex from 'vuex'

import menu from '../moduls/menu';
import contracts from '../moduls/contracts';

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    urlEnter: '/api/auth/login',
    urlRegistr: '/api/auth/register',
    url: 'http://5.tcp.eu.ngrok.io:13492',
    urlGetContracts: '/api/menu/contracts/show',
    token: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXZyaSJ9.MLyGoKbKG8DuOR7QL_wIwEqLg-Q9yVTU6aVYjucpE84',
    tokenType: 'Bearer'
  },
  getters: {
  },
  mutations: {
    

    
  },
  actions: {
  },

  modules: {
    menu,
    contracts,
  }
});

export default store;
