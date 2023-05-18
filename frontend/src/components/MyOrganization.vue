<template class="main">
    <v-app>
    
        <section >
            <my-menu></my-menu>
        </section>
        <section class="content">
            <h1>Организации-Контрагенты</h1>
            <v-divider></v-divider>
            <v-container >
                <v-dialog v-model="dialog1"
                    persistent
                    max-width="600px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn 
                            outlined  
                            icon color="blue" 
                            v-bind="attrs" 
                            v-on="on"
                            >
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>                       
                    </template>
                    <v-card>
                        <v-card-title>Добавить Организации-Контрагенты</v-card-title>
                        <v-card-text v-for="(item, name, id) in Agents">
                            <v-text-field 
                                :name="name"
                                :key="id"
                                :placeholder="name"
                                v-model = "Agents[name]"                               
                                >
                            </v-text-field>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn
                                color="blue darken-1"
                                text
                                @click="dialog2 = true"
                                >Закрыть
                            </v-btn>
                            <v-row>
                                <v-dialog width="600px" justify-center
                                    v-model="dialog2"
                                    >
                                    <v-card >
                                        <v-card-title >
                                            <div>Вы уверены, что хотите закрыть окно?</div>
                                            <div>Все несохраненные данные будут утеряны</div>
                                        </v-card-title>
                                        <v-card-actions>
                                            <v-spacer></v-spacer>
                                                <v-btn color="blue darken-1"
                                                    text
                                                    @click="dialog1 = false"
                                                    >Закрыть
                                                </v-btn>
                                                <v-btn color="blue darken-1"
                                                    text
                                                    @click="dialog2 = false"
                                                    >Остаться
                                                </v-btn>
                                        </v-card-actions>
                                    </v-card>
                                </v-dialog>
                            </v-row>
                            <v-btn
                                color="blue darken-1"
                                text 
                                @click="dialog1 = false, sendNewOrganization(), createNewOrganization()"                               
                                >Сохранить
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </v-container>
            <v-divider></v-divider>
            <v-container>
                <v-simple-table class="table">
                    <template  v-slot:default>
                        <thead>
                            <th v-for="item in headers" :key="item.text">
                                {{ item }}
                            </th>
                        </thead>
                        <tbody>
                            <tr v-for="(item, name, index) in contragents"
                                :key="item.idndex"
                                @click="Show(item),NameDelete = item"
                                >
                                <td v-for="value in item">{{ value }}</td>
                                <td>
                                    
                                    <v-btn 
                                        outlined  
                                        icon color="blue" >
                                        <v-icon>mdi-pencil</v-icon>
                                    </v-btn>          
                                </td>
                                <td>
                                    <v-dialog v-model="dialogDelete" 
                                        persistent
                                        max-width="600px">
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn outlined  
                                                icon color="red"
                                                v-bind="attrs" 
                                                v-on="on">
                                                    <v-icon>mdi-delete</v-icon>
                                            </v-btn>
                                        </template>
                                        <v-card>
                                            <v-card-text>
                                                Вы хотите удалить только контрагента или связанные с ним контракты тоже?
                                            </v-card-text>
                                            <v-card-actions>
                                                <v-btn @click="dialogDelete=!dialogDelete">Отмена</v-btn>
                                                <v-btn @click="dialogDelete=!dialogDelete, deleteOrganization()">Только контрагента</v-btn>
                                                <v-btn @click="dialogDelete=!dialogDelete, deleteAllOrganization()">Вместе с контрактами</v-btn>
                                            </v-card-actions>
                                        </v-card>
                                    </v-dialog>
                                </td>
                            </tr>
                        </tbody>
                    </template>    
                </v-simple-table>
            </v-container>
            {{ Agents }} {{ a }} {{ NameDelete }}
        </section> 
        organization 
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад</v-btn>
            </router-link> 
            <v-btn @click="getOrganization()">Загрузить</v-btn>
        </span>
    
    </v-app>
</template>

<script>
import { VCardActions } from 'vuetify/lib';
import MyMenu from '../components/MyMenu.vue';
import { mapGetters, mapMutations } from 'vuex';
import axios from 'axios';
import { VListItemSubtitle } from 'vuetify/lib';
export default {
    name: 'MyOrganization',
    components: {
        MyMenu,
    },

    props: {
        
    },

    data() {
        return {
            headers:['Название', 'Адрес', 'ИНН'],
            dialog1: false,
            dialog2: false,
            dialog3: false,
            dialog4: false,
            dialogDelete: false,
            a: '',
            NameDelete: '',
            Agents: {
                name: '',
                address: '',
                inn: ''
            }
        }
    },
    computed:{
        ...mapGetters({                      
            contragents: "contragents/contragent"
        }),
        
    },

    methods: {
        ...mapMutations({
            
            addAllContragents: 'contragents/ADD_ALL_CONTRAGENTS',
            addNewContragents: 'contragents/ADD_NEW_CONTRAGENTS'
        }),

        sendNewOrganization() {
            const res = {};
            for (let item in this.Agents) {
               res[item] = this.Agents[item];
            }
            this.addNewContragents(res);
            for (let item in this.Agents) {
               this.Agents[item] = '';
            };
            console.log(res)   
        },

        async createNewOrganization() {
            try {
                const response = await axios.post(this.$store.state.url + '/api/menu/counterparties/save',
                {
                    name: this.Agents.name,
                    address: this.Agents.address,
                    inn: this.Agents.inn                   
                },
                {headers: {
                    "Authorization":  "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",
                }});
                console.log(response) 
                             
            } 
            catch(e) {
                alert('Неверно')
                }
            
        },

        async getOrganization() {
            try {
                const response = await axios.get(this.$store.state.url + '/api/menu/counterparties/show', 
                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",                   
                }})
                console.log(response)
                for (let i = 0; i<response.data.length; i++)  {
                    console.log(i);
                    this.addAllContragents(response.data[i])
                    console.log(response.data[i]);                   
                }                         
            } 
            catch(e) {
                alert('Error is true')
            }      
        },

        async deleteOrganization() {
            try {
                const response = await axios.delete(this.$store.state.url + '/api/menu/counterparties/delete',
                {
                    name: this.NameDelete
                },

                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",                  
                }})
                console.log(item.name)
                console.log(response)
                 
            }
            catch(e) {
                alert('Error is true')
            }
            
            
        },

        async deleteAllOrganization() {
            try {
                const response = await axios.delete(this.$store.state.url + '/api/menu/counterparties/deleteAll',
                {
                    name: this.NameDelete
                },

                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",                  
                }})
                console.log(item.name)
                console.log(response)
            }
            catch(e) {
                alert('Error is true')
            }
            
        },

        Show(item) {
            console.log(item.name)
            this.a=item.name
        },
    }
}
</script>



<style  scoped>
.main {
    max-height: 400px;  
    max-width: 250px; 
}
h1 {
    text-align: center;
}
.content {
    padding-left: 280px;
    padding-top: 20px;
    position: fixed;
}
.btn {
    margin-left:20px
}
</style>