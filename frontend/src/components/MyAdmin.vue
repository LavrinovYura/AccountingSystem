<template class="main">
    <v-app>    
        <section >
            <my-menu></my-menu>
        </section>
        <section class="content">
            <h1>Пользователи</h1>
            <v-divider></v-divider>
            <v-dialog v-model="dialog1"
                persistent
                max-width="400px">
                <template v-slot:activator="{ on, attrs }">
                    <v-btn outlined  
                        icon color="red"
                        v-bind="attrs" 
                        v-on="on">
                            <v-icon>mdi-delete</v-icon>
                    </v-btn>
                </template>
                <v-card class="card">
                    <v-card-text>
                        Вы уверены, что хотите удалить пользователя?
                    </v-card-text>
                    <v-card-actions>
                        <v-btn @click="dialog1=!dialog1">Отмена</v-btn>
                        <v-btn @click="dialog1=!dialog1">Удалить</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>

            <v-dialog v-model="dialog2"
                persistent
                max-width="300px">
                <template v-slot:activator="{ on, attrs }">
                    <v-btn outlined  class="btn"
                        small
                        v-bind="attrs" 
                        v-on="on">
                            сделать админом
                    </v-btn>
                </template>
                <v-card class="card">
                    <v-card-text>
                        Сделать пользователя админом?
                    </v-card-text>
                    <v-card-actions>
                        <v-btn @click="dialog2=!dialog2">Отмена</v-btn>
                        <v-btn @click="dialog2=!dialog2">Сделать</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>

            <v-dialog v-model="dialog3"
                persistent
                max-width="300px">
                <template v-slot:activator="{ on, attrs }">
                    <v-btn outlined  class="btn"
                        small
                        v-bind="attrs" 
                        v-on="on">
                            Удалить роль админа
                    </v-btn>
                </template>
                <v-card class="card">
                    <v-card-text>
                        Удалить у пользователя роль админа?
                    </v-card-text>
                    <v-card-actions>
                        <v-btn @click="dialog3=!dialog3">Отмена</v-btn>
                        <v-btn @click="dialog3=!dialog3">Удалить</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <v-divider></v-divider>
            <v-simple-table class="table">
                <template v-slot:default>
                    <thead>
                        <th v-for="name in thead">
                            {{ name }}
                        </th>
                    </thead>
                    <tbody>
                        <tr v-for="(item, id, key) in users"
                        :key="item.name"
                        @click="username=item.name"
                        :class = "{'blue lighten-5': username===item.name}"
                        >
                            <td v-for="items  in item">
                                {{ items }} {{ username }}
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
            username: '',    
            dialog1: false,  
            dialog2: false,
            dialog3: false,
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
        
    },
    computed:{
        ...mapGetters({
            users: 'users',       
        }),
        
    },
    activated() {
            this.getUsers()
    }
}
</script>

<style scoped>
.content {
    padding-left: 25%;
    padding-top: 20px;
    position: fixed;
}
.table {
    width: 700px;
    height: 400px;
    border: 1px solid black;
    overflow-y: auto;
    text-align: center;
}
.card{
    text-align: center;
}
.btn {
    margin-left:20px
}
</style>