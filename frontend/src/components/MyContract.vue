<template class="main">
    <v-app>   
        <section >
        <my-menu></my-menu>
        </section>
        <section class="content">
        <h1>Контракты</h1>
        <v-divider></v-divider>
            <v-container >
                <v-row>
                    <v-dialog 
                        v-model="dialog1"
                        persistent
                        max-width="600px">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-btn 
                                outlined  
                                icon color="blue" 
                                v-bind="attrs" 
                                v-on="on">
                                    <v-icon >mdi-plus</v-icon>
                            </v-btn>
                        </template>
                        <v-card>
                            <v-card-title>
                                <span class="text-h5"
                                >Добавить новый контракт
                                </span>
                            </v-card-title>
                            <v-card-text>
                                <v-text-field 
                                    label="Название"
                                    v-model= 'newContract.name'>
                                </v-text-field>
                                <v-select
                                    :items="type" 
                                    :menu-props="{ top: true, offsetY: true }"
                                    label="Тип"
                                    v-model= "newContract.type">
                                </v-select>
                                <label>Плановая дата начала
                                    <v-text-field                                
                                        type="Date"
                                        v-model="newContract.plannedStartDate">
                                    </v-text-field>
                                </label>
                                <hr>
                                <label>Плановая дата окончания
                                    <v-text-field                                 
                                        type="Date"
                                        v-model="newContract.plannedEndDate">
                                    </v-text-field>
                                </label>
                                <hr>
                                <label>Фактическая дата начала
                                    <v-text-field                                 
                                        type="Date"
                                        v-model="newContract.actualStartDate">
                                    </v-text-field>
                                </label>
                                <hr>
                                <label>Фактическая дата окончания
                                    <v-text-field                                 
                                        type="Date"
                                        v-model="newContract.actualEndDate">
                                    </v-text-field>
                                </label> 
                                <v-text-field 
                                    label="Сумма договора"
                                    v-model="newContract.amount">
                                </v-text-field>
                                <v-select
                                    :items="stage" 
                                    :menu-props="{ top: true, offsetY: true }"
                                    label="Этап"
                                    v-model= "newContract.stage"> 
                                </v-select>
                                <v-text-field 
                                    label="Договор с Контрагентом"
                                    v-model="newContract.contr">
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
                                    <v-dialog
                                        v-model="dialog2"
                                        >
                                        <v-card>
                                            <v-card-title>
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
                                    @click="sendNewContract()"
                                    >Сохранить
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>               
                    <template>
                        <v-btn class="btn"
                            outlined  
                            icon color="blue" 
                            @click="showSearch=!showSearch"
                            >
                                <v-icon >mdi-magnify</v-icon>
                        </v-btn>
                    </template>               
                </v-row>
            </v-container>        
            <v-container>
                <v-card-title v-if="showSearch">
                    <v-text-field v-model="search"
                        append-icon="mdi-magnify"
                        label="Search"
                        single-line
                        hide-details>
                    </v-text-field>
                </v-card-title>
                <v-data-table
                    v-model="selected"
                    item-key="name"               
                    :headers="headers"
                    :items="contracts"
                    :search="search"
                    @click="cons()"
                    >
                    <template v-slot:item="props">
                        <tr>                                                         
                            <td v-for="value in props.item" 
                                @click.stop="cons(props.item)"                  
                                >{{ value }}
                            </td> 
                            <td >
                                <router-link  class="btn" :to="{name:'contractPage', params: {name: `${props.item.name}`}}">
                                    <v-btn outlined  
                                        icon color="blue" >
                                        <v-icon >mdi-eye</v-icon>
                                    </v-btn>
                                </router-link>
                            </td>                 
                        </tr>
                    </template>
                </v-data-table>           
            </v-container>
        </section>
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад в меню </v-btn>
            </router-link> 
        </span>
        
        <span>
            <v-btn
            @click="getContract()">
            download</v-btn>
        </span>    
    </v-app>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex';
import MyMenu from '../components/MyMenu.vue';
export default {
    name: 'MyContract',
    components: {
        MyMenu,
    },

    props: {
        
    },

    data() {
        return {
            search: '',
            dialog1: false,
            dialog2: false,
            newContract:
                {
                    name: '',
                    type: '',
                    plannedStartDate: '',
                    plannedEndDate: '',
                    actualStartDate: '',
                    actualEndDate: '',
                    amount: 0,
                    stage: '',
                    contr: ''
                },
            selected: [],
            showSearch: false,
        }
    },
    computed:{
        ...mapGetters({
            headers: 'contracts/headers',           
            contracts: 'contracts/contracts',
            type: 'contracts/type',
            stage: 'contracts/stage'
        }),
        
    },

    methods: {
        ...mapMutations({
            addContract: 'contracts/ADD_NEW_CONTRACT'
        }),
        sendNewContract() {
            this.dialog1 = false;
            const res = {};
            for (let item in this.newContract) {
               res[item] = this.newContract[item];
            }
            this.addContract(res);
            for (let item in this.newContract) {
               this.newContract[item] = '';
            }     
        },
        Show(item) {
            console.log(item.name)
        },
        
        ShowID(items) {
            console.log(items.name)
        },
        cons(item) {
            console.log(item.name)
        },

        async getContract() {
            try {
                const response = await axios.get(this.$store.state.url + this.$store.state.urlGetContracts, 
                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYXZyMSJ9.8sgbArBQORNApYXDECX88W_UZ0rHzFLY0Yt-HWcgvEU",
                    "Cache-Control": null,
                    "X-Requested-With": null
                    
                }})

                console.log(response)
                           
                                     
            } 
            catch(e) {
                alert('Error is true')
            }      
        },
    }
}

</script>

<style>

.main {
    max-height: 400px;  
    max-width: 250px; 
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

