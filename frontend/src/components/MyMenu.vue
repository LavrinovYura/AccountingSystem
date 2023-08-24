<template >
    <v-app class="main">
        <v-card>
            <v-navigation-drawer height="400px" absolute permanent >
                <template v-slot:prepend>
                    <v-list-item two-line>
                        <v-list-item-icon>
                            <v-icon>
                                mdi-account
                            </v-icon> 
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>
                                {{ name }}
                            </v-list-item-title>
                            <v-list-item-subtitle>
                                {{ surname}}
                            </v-list-item-subtitle>
                            <div>   
                                <router-link class="btn" :to="{name: 'FormEnter'}">
                                    <v-btn flat small
                                    @click="clearToken"> Выйти </v-btn>
                                </router-link> 
                            </div> 
                        </v-list-item-content>
                    </v-list-item>
                </template>
                
                <v-list dense v-for='(items, index) in menuList'>
                    <router-link class="btn" :to="{name: items.name}">
                        <v-list-item>{{ items.item }}</v-list-item>
                    </router-link>
                </v-list>               
            </v-navigation-drawer>
            
        </v-card>
         dfvgsde 
    </v-app>
</template>

<script>
export default {
    name: 'MyMenu',
    data() {
        return {
            name: '',
            surname: '',
            menuList: [
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
        }
    },

    activated() {
        this.name= localStorage.name
        this.surname= localStorage.surname
    },

    methods: {
        clearToken() {
            localStorage.setItem('token', " ");
            localStorage.setItem('name', " ");
            localStorage.setItem('surname', " ");
        }
    }
}


</script>

<style  scoped>
.main {
    max-height: 400px;  
    max-width: 250px; 
}

</style>