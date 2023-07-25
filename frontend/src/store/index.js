import Vue from 'vue'
import Vuex from 'vuex'


import contractsModul from '../moduls/contractsModul';
import organization from '../moduls/organization'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    
    url: 'http://212.118.43.99:8080',   
    token: '',
    tokenType: 'Bearer',
    name: '',
    fullname: '',
    dialog1: false,
    dialog8: false,
    contragents: [{
      name: 'ann',
      inn: "fgnhn",
      address: 'dfvdf'
      }],
    contracts: [{
      name: 'ann',
      type: '',
      plannedStartDate: '',
      plannedEndDate: '',
      actualStartDate: '',
      actualEndDate: '',
      amount: '',
      phases: [{ 
          name: 'phasa1',
          amount: '50005',
          planAmount: '',
          factAmount: '',
          plannedStartDate: '',
          plannedEndDate: '',
          actualStartDate: '',
          actualEndDate: '',
          },
          {
            name: 'phasa2',
            amount: '50005',
            planAmount: '',
            factAmount: '',
            plannedStartDate: '',
            plannedEndDate: '',
            actualStartDate: '',
            actualEndDate: '',
          }
        ],
      contractCounterparties: [{ 
          name: 'ann',
          amount: '',
          organization: '',
          type: '',
          plannedStartDate: '',
          plannedEndDate: '',
          actualStartDate: '',
          actualEndDate: '',
      }]
  },],

  },

  getters: {
    contragent(state) {
      return state.contragents
    },
    contracts(state) {
      return state.contracts
    },
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
    },

    CLOSE_DIALOG(state) {
      state.dialog1 = false
      state.dialog8 = false
    },

    ADD_ALL_CONTRAGENTS(state, payload){
      state.contragents.push(payload)
    },

    ADD_NEW_CONTRAGENTS(state, payload){
        state.contragents.push(payload)
    },

    DELETE_CONTRAGENT(state, index) {
        state.contragents.splice(index, 1);
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
