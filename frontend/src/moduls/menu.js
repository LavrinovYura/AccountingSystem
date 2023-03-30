
export default {
    namespaced: true,
    state: {
        items: [
            {
                name: 'contract',
                item: 'Договоры'
            },
            {
                name: 'organization',
                item: 'Kонтрагенты'
            },
            {
                name: 'report',
                item: 'Отчеты'
            },
            {
                name: 'admin',
                item: 'Администрирование'
            },
        ]

    },
    getters: {
        menuList(state) {
            return state.items;
        }
        
    },
    


}