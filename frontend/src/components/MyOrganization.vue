<template class="main">
    <v-app>
    
        <section >
            <my-menu></my-menu>
        </section>
        <section class="content">
            <h1>Организации-Контрагенты</h1>
            <v-divider></v-divider>
            <span class="btn">
                <v-dialog v-model="dialog1"
                    persistent
                    max-width="600px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn 
                            outlined  
                            icon color="green" 
                            v-bind="attrs" 
                            v-on="on"
                            >
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>                       
                    </template>
                    <v-card>
                        <v-card-title>Добавить Организации-Контрагенты</v-card-title>
                        <v-card-text v-for="(item, name, id) in Agents">
                            <label>{{ name }}</label>
                            <v-text-field 
                                :name="name"
                                :key="id"
                                
                                v-model = "Agents[name]"                               
                                >
                            </v-text-field>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn
                                color="blue darken-1"
                                text
                                @click="dialog2 = !dialog2"
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
                                @click="dialog1 = !dialog1, //sendNewOrganization(), 
                                createNewOrganization()"                               
                                >Сохранить
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </span>
            <span class="btn">
                <v-dialog v-model="dialogDelete" 
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
                    <v-card>
                        <v-card-text>
                            Вы уверены, что хотите удалить контрагента?
                        </v-card-text>
                        <v-card-actions>
                            <v-btn @click="dialogDelete=!dialogDelete">Отмена</v-btn>
                            <v-btn @click="dialogDelete=!dialogDelete, deleteOrganization()">Удалить</v-btn>
                            
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </span>
            <span class="btn">
                <v-dialog v-model="dialog3"
                    persistent
                    max-width="600px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn 
                            outlined  
                            icon color="blue" 
                            v-bind="attrs" 
                            v-on="on"
                            >
                            <v-icon >mdi-pencil</v-icon>
                        </v-btn>                       
                    </template>
                    <v-card>
                        <v-card-title> Редактировать Организацию-Контрагента</v-card-title>{{ NameDelete }}
                        <v-card-text v-if="name!='id'" v-for="(item, name, id) in  NameDelete">
                            
                            <label> {{ name }}
                            <v-text-field 
                                :name="name"
                                :key="id"
                                v-model="NameDelete[name]"                              
                                > {{ item }}
                            </v-text-field> 
                        </label>
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
                                                    @click="dialog3 = false"
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
                                @click="dialog3 = !dialog3, updateOrganization()"                               
                                >Сохранить
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </span>
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
                            <tr v-for="(item, name,id) in contragents"
                                :key="item.idndex"
                                @click="Show(item),NameDelete = item, setActive=item.name"
                                :class = "{'blue lighten-5': setActive===item.name}"
                                >
                                <td v-for="(value,name) in item"
                                v-if="name!='id'" 
                                >{{ value }}</td>
                                 
                            </tr>
                        </tbody>
                    </template>    
                </v-simple-table>
            </v-container>
        </section> 
        <span>   
            <v-btn @click="getOrganization()">Загрузить</v-btn>
        </span>
    
    </v-app>
</template>

<script>
import { VCardActions } from 'vuetify/lib';
import MyMenu from '../components/MyMenu.vue';
import { mapGetters, mapMutations, mapActions } from 'vuex';
import axios from 'axios';
import { VListItemSubtitle } from 'vuetify/lib';
export default {
    name: 'MyOrganization',
    components: {
        MyMenu,
    },

    data() {
        return {
            headers:['Название', 'Адрес', 'ИНН'],
            dialog1: false,
            dialog2: false,
            dialog3: false,
            dialogDelete: false,
            setActive: '',
            NameDelete: {
                id: '',
                name: '',
                address: '',
                inn: ''
            },
            Agents: {
                 
                name: '',
                address: '',
                inn: ''
            },
            NameContractDelete: '',
        }
    },
    computed:{
        ...mapGetters({                      
            contragents : 'contragent'
        }),        
    },

    methods: {
        ...mapMutations({           
            addAllContragents: 'ADD_ALL_CONTRAGENTS',
            addNewContragents: 'ADD_NEW_CONTRAGENTS',           
        }),

        ...mapActions( ['deleteContragent']),
        ...mapActions(['deleteWithContract']),

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

        Show(item) {
            console.log(item.name)
            this.NameDelete = item
            console.log(this.NameDelete.id)
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
                    "Authorization":  "Bearer " + localStorage.token,
                }});
                console.log(response)                             
            } 
            catch(e) {
                alert('Неверно')
                }            
        },

        async getOrganization() {
            try {
                const Body = {};
                const response = await axios.post(this.$store.state.url + '/api/menu/counterparties/show', 
                    Body,
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,                   
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
                const response = await axios.delete(this.$store.state.url + '/api/menu/counterparties/' + this.NameDelete.id + '/delete',
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,                  
                }})
                console.log(item.name)
                console.log(NameDelete.name)
                console.log(response)                
            }
            catch(e) {
                alert('Error is true')
                console.log(this.NameDelete.name)
            }
        },

        async deleteAllOrganization() {
            try {
                const response = await axios.delete(this.$store.state.url + '/api/menu/counterparties/' + this.NameDelete.id + '/deleteAll',
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,                  
                }})
                console.log(item.name)
                console.log(response)
            }
            catch(e) {
                alert('Error is true')
            }           
        },
 
        async updateOrganization() {
            try {
                const response = await axios.put(this.$store.state.url + '/api/menu/counterparties/'+ this.NameDelete.id + '/update' ,
                {
                    name:NameDelete.name,
                    address: NameDelete.address,                    
                },
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,                  
                }})
                console.log(item.name)
                console.log(response)
            }
            catch(e) {
                alert('Error is trrue')
            }           
        },
    }, 

    activated() {         
        this.getOrganization()
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
    margin-left:15px;
}
.table {
    border: 1px solid black;
    height: 314px;
    overflow-y: auto;
    text-align: center;
}


</style>