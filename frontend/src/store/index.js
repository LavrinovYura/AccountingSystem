import Vue from 'vue'
import Vuex from 'vuex'


import contractsModul from '../moduls/contractsModul';
import organization from '../moduls/organization'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    
    url: 'http://4.tcp.eu.ngrok.io:13408',   
    token: '',
    tokenType: 'Bearer',
    name: '',
    firstname: '',
    surname: '',
    dialog1: false,
    dialog8: false,
    dialog7: false,
    contragents: [{
      id: 0,
      name: 'ann',
      inn: "fgnhn",
      address: 'dfvdf'
      }],
    contracts: [{
      name: 'ann',
      type: 'WORKS',
      plannedStartDate: '',
      plannedEndDate: '',
      actualStartDate: '2023-05-07',
      actualEndDate: '2023-05-07',
      amount: 1000,
      phases: [{ 
          name: 'phasa1',
          amount: 50005,
          planAmount: 222,
          factAmount: 333,
          plannedStartDate: '',
          plannedEndDate: '',
          actualStartDate: '',
          actualEndDate: '',
          },
          {
            name: 'phasa2',
            amount: 50005,
            planAmount: 777,
            factAmount: 666,
            plannedStartDate: '',
            plannedEndDate: '',
            actualStartDate: '',
            actualEndDate: '',
          }
        ],
      contractCounterparties: [{ 
        name: 'ann1',
        amount: 777,
        organization: 'ann',
        type: 'WORKS',
        plannedStartDate: '',
        plannedEndDate: '',
        actualStartDate: '',
        actualEndDate: '',
      },
      { 
        name: 'ann2',
        amount: '',
        organization: '',
        type: '',
        plannedStartDate: '',
        plannedEndDate: '',
        actualStartDate: '',
        actualEndDate: '',
    }
    ]
  },],
    users: [],
  },

  getters: {
    contragent(state) {
      return state.contragents
    },
    contracts(state) {
      return state.contracts
    },
    users(state) {
      return state.users.map(user => {
        user.roles = user.roles.reduce((accum, role) => accum + role.roleType + ' ', '');
        return user;
      })
    }
  },


  mutations: {
    ADD_TOKEN(state, payload) {
      state.token = payload
      localStorage.setItem('token', state.token)      
    },

    ADD_FIRST_NAME(state,payload) {
      state.firstname = payload
      
      localStorage.setItem('name', state.firstname)
    },

    ADD_SUR_NAME(state,payload) {
      state.surname = payload
      
      localStorage.setItem('surname', state.surname)
    },

    CLOSE_DIALOG(state) {
      state.dialog1 = false
      state.dialog8 = false
      state.dialog7 = false
    },

    ADD_ALL_CONTRAGENTS(state, payload){
      state.contragents.push(payload)
    },

    ADD_NEW_CONTRAGENTS(state, payload){
      state.contragents.push(payload)
    },

      
    ADD_USERS(state,payload){
      state.users.push(payload)
    },

    ADD_NEW_CONTRACT(state, payload) {
      state.contracts.push(payload)
    },

    ADD_ALL_CONTRACTS(state, payload){
      state.contracts.push(payload)
    },

    DELETE_CONTRACT(state, index) {
      state.contracts.splice(index, 1);
    },
    DELETE_CONTRAGENT(state, index) {
      state.contragents.splice(index, 1);
    },
  },
  
  
  actions: {
    deleteContragent({ commit, state }, name) {
      const index = state.contragents.findIndex(c => c.name === name)
      if (index !== -1) {
        commit('DELETE_CONTRAGENT', index)
      }
    },
    deleteContract({ commit, state }, name) {
      const index = state.contracts.findIndex(c => c.name === name)
      if (index !== -1) {
        commit('DELETE_CONTRACT', index)
      }
    },
    deleteWithContragent({ commit, state }, name) {
      const index = state.contragents.findIndex(c => c.name === name)
      if (index !== -1) {
        commit('DELETE_CONTRAGENT', index)
      }
    },
    deleteWithContract({ commit, state }, name) {
      const index = state.contracts.findIndex(c => c.name === name)
      if (index !== -1) {
        commit('DELETE_CONTRACT', index)
      }
    },
  
  },

  modules: {
    
    contractsModul,
    organization,
   
  }
});

export default store;
