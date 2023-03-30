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
        contracts: [
            {
                name: 'ABC',
                type: 'procurment',
                plannedStartDate: '05.11.2002',
                plannedEndDate: '11.05.2002',
                actualStartDate: '11.05.2023',
                actualEndDate: '05.11.2023',
                amount: 50000.0,
                stage: 'active',
                contr: '1'
            },
            {
                name: 'hjo',
                type: 'some',
                plannedStartDate: '05.11.2005',
                plannedEndDate: '11.05.2006',
                actualStartDate: '11.05.2020',
                actualEndDate: '05.11.2021',
                amount: 60000.0,
                stage: 'active',
                contr: '1'
            },
            {
                name: 'DEF',
                type: 'some',
                plannedStartDate: '05.11.2003',
                plannedEndDate: '11.05.2004',
                actualStartDate: '11.05.2019',
                actualEndDate: '05.11.2019',
                amount: 60000.0,
                stage: 'active',
                contr: '1'
            
            },
        ],
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
        } 
    } 
    


}