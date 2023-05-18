export default {
    namespaced: true,
    state: {       
        
        contragents: [],
       
    },
    getters: {
        
        
        contragent(state) {
            return state.contragents
        },
        
       
    },
    mutations: {
        
        ADD_ALL_CONTRAGENTS(state, payload){
            state.contragents.push(payload)
        },
        ADD_NEW_CONTRAGENTS(state, payload){
            state.contragents.push(payload)
        }
    } 
    


}