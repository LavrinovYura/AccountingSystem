export default {
    namespaced: true,
    state: {       
        headers: [
            {
                text: 'Название',
                align: 'start',
                value: 'name',
            },
            {
                text: 'Тип',
                value: 'type',
            },
            {
                text: 'Плановая дата начала',
                value: 'plannedStartDate',
            },
            {
                text: 'Плановая дата окончания',
                value: 'plannedEndDate',
            },
            {
                text: 'Фактическая дата начала',
                value: 'actualStartDate',
            },
            {
                text: 'Фактическая дата окончания',
                value: 'actualEndDate',
            },
            {
                text: 'Сумма договора',
                value: 'amount',
            },
            {
                text: 'Этапы',
                value: 'stage'
            },
            {
                text: 'Договор с контрагентом ',
                value: 'contract'
            }

        ],
        contracts: [],
        type: ['Закупка', "Поставка", "Работы"],
        stage: ["Завершен", "Активен"]
    },
    getters: {
        headers(state) {
            return state.headers;
        },
        
        contracts(state) {
            return state.contracts
        },
        type(state) {
            return state.type
        },
        stage(state) {
            return state.stage
        }
       
    },
    mutations: {
        ADD_NEW_CONTRACT(state, payload) {
            state.contracts.push(payload)
        },
        ADD_ALL_CONTRACTS(state, payload){
            state.contracts.push(payload)
        }
    } 
    


}