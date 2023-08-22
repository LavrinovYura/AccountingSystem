<template class="main">
    <v-app>    
        <section >
            <my-menu></my-menu>
        </section>
        <section class="content">
            <h1>Пользователи</h1>
            <v-divider></v-divider>
            <v-simple-table class="table">
                <template v-slot:default>
                    <thead>
                        <th v-for="name in thead">
                            {{ name }}
                        </th>
                    </thead>
                    <tbody>
                        <tr v-for="(item, id, key) in users" :key="item.name">
                            <td v-for="items  in item">
                                {{ items }} 
                            </td>
                        </tr>
                    </tbody>
                    
                </template>
            </v-simple-table>
            {{ users }}
            <v-btn @click="getUsers">download</v-btn>
        </section>
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад</v-btn>
            </router-link> 
        </span>
    
</v-app>
</template>

<script>
import {mapMutations, mapGetters} from 'vuex';
import MyMenu from '../components/MyMenu.vue';
export default {
    name: 'MyAdmin',
    components: {
        MyMenu,
    },
    data() {
        return {
            thead: {               
                text1: 'Имя',
                text2: 'Фамилия',
                text3: 'Отчесвто',
                text4: "Логин",
                text5: 'Роль',
                text6: 'Дата регистраци'
            },           
        }
    },
    methods: {
        ...mapMutations({
            addUsers: 'ADD_USERS',
        }),

        async getUsers() {
            try {
                
                const response = await axios.get(this.$store.state.url + '/api/menu/administration/users', 
                
                    
                
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,               
                }})
                console.log(response)
                console.log(response.data[2])
                for (let i = 0; i<response.data.length; i++)  {
                    console.log(i);
                    this.addUsers(response.data[i])
                    console.log(response.data[i]);                   
                }                         
            } 
            catch(e) {
                alert('Error is true')
            }      
        },
        activated() {
            this.getUsers()
    }
    },
    computed:{
        ...mapGetters({
            users: 'users',       
        }),
        
    },
}
</script>

<style scoped>
.content {
    padding-left: 25%;
    padding-top: 20px;
    position: fixed;
}
.table {
  
    height: 400px;
    border: 1px solid black;
    overflow-y: auto;
    text-align: center;
}
</style>