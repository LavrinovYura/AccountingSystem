export default {
    namespaced: true,
    state: {       
        headers: [
            {text: 'Название',
                align: 'start',
                value: 'name',
            },
            {text: 'Тип',
                value: 'type',
            },
            {text: 'Плановая дата начала',
                value: 'plannedStartDate',
            },
            {text: 'Плановая дата окончания',
                value: 'plannedEndDate',
            },
            {text: 'Фактическая дата начала',
                value: 'actualStartDate',
            },
            {text: 'Фактическая дата окончания',
                value: 'actualEndDate',
            },
            {text: 'Сумма договора',
                value: 'amount',
            },
            {text: 'Этапы',
                value: 'stage'
            },
            {text: 'Договор с контрагентом ',
                value: 'contract'
            }

        ],
        contracts: [],
        type: ['WORKS', "DELIVERY", "PROCEREMENT"],
        

        
    },
    getters: {
        headers(state) {
            return state.headers;
        },
        
        
        type(state) {
            return state.type
        },
        
    },
    mutations: {
        
    },

    actions: {
        
        
    }
    


}