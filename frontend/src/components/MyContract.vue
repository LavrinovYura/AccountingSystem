<template class="main">
    <v-app>   
        <section >
        <my-menu></my-menu>
        </section>
        <section class="content" >
        <h1>Контракты</h1>
        <v-divider></v-divider>
            <v-container>
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
                                <v-divider></v-divider>
                                <section
                                    
                                    
                                    > 
                                </section>
                                <section> Организация Контрагент
                                    <v-row> 
                                        <v-col><v-text-field  label="name"></v-text-field></v-col>
                                        <v-col><v-text-field  label="address"></v-text-field></v-col>
                                        <v-col><v-text-field  label="inn"></v-text-field></v-col>
                                    </v-row>
                                    <v-btn outlined  icon color="blue"> 
                                        <v-icon >mdi-plus</v-icon>
                                    </v-btn>
                                </section>
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
                                {{ this.newContract.name }}
                                <v-btn
                                    color="blue darken-1"
                                    text                                
                                    @click="createNewContract(), sndNewContract(),  getContractt()"
                                    >Сохранить
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>               
                    <template>
                        <v-btn class="btn"
                            outlined  
                            icon color="blue" 
                            >
                                <v-icon >mdi-magnify</v-icon>
                        </v-btn>
                    </template>               
                </v-row>
            </v-container>        
            <v-divider></v-divider>
            <v-container>
                <v-simple-table height="700px" class="table" >
                    <template  v-slot:default>
                        <thead>
                            <th v-for="item in headers" :key="item.text">
                                {{ item.text }}
                            </th>
                        </thead>
                        <tbody>
                            <tr v-for="(item, name, index) in contracts"
                                :key="item.idndex"
                                @click="Show(item)"
                                >
                                <td>{{ item.name}}</td>
                                <td>{{ item.type }}</td>
                                <td>{{ item.plannedStartDate }}</td>
                                <td>{{ item.plannedEndDate }}</td>
                                <td>{{ item.actualStartDate }}</td>
                                <td>{{ item.actualEndDate }}</td>
                                <td>{{ item.amount }}</td>
                                <td>{{ item.phases.length }}</td>
                                <td>{{ item.contractCounterparties.length }}</td>
                                <td>
                                    <router-link class="btn" :to="{name:'contractPage', params: {name: `${item.name}`}}">
                                        <v-btn outlined small depressed>
                                            Открыть
                                        </v-btn>
                                    </router-link>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>           
            </v-container>
        </section>
        {{ $store.state.token }}
        <span>   
            <router-link class="btn" :to="{name: 'menu'}">
                <v-btn color=" light"> Назад в меню </v-btn>
            </router-link> 
        </span>       
        <span>
            <v-btn
            @click="getContractt()">
            download</v-btn>
        </span>  
        {{ testLocal }}
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
                    amount: '',
                    phases: [{ }],
                    contractCounterparties: [{ }]
                },
            allContracts:
                {
                    name: '',
                    type: '',
                    plannedStartDate: '',
                    plannedEndDate: '',
                    actualStartDate: '',
                    actualEndDate: '',
                    amount: '',
                    phases: [{ }],
                    contractCounterparties: [{ }],
                    },
            selected: [],
            testLocal: localStorage.tok,
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
            addNewContract: 'contracts/ADD_NEW_CONTRACT',
            addAllContracts: 'contracts/ADD_ALL_CONTRACTS'
        }),
        sndNewContract() {
            this.dialog1 = false;
            const res = {};
            for (let item in this.newContract) {
               res[item] = this.newContract[item];
            }
            this.addNewContract(res);
            for (let item in this.newContract) {
               this.newContract[item] = '';
            };
               
        },
        Show(item) {
            console.log(item.name)
        },

        async createNewContract() {
            try {
                const response = await axios.post(this.$store.state.url + '/api/menu/contracts/save',
                {
                    name: this.newContract.name,
                    type: "WORKS",
                    plannedStartDate: '2023-09-15',
                    plannedEndDate: '2023-09-30',
                    actualStartDate: '2023-05-01',
                    actualEndDate: '2023-07-15',
                    amount: 5000.0,
                    phases: [
                        {
                            name: "Phase 1",
                            plannedStartDate: "2023-09-15",
                            plannedEndDate: "2023-09-30",
                            actualStartDate: "2023-05-01",
                            actualEndDate: "2023-07-15",
                            phaseCost: 100000.00,
                            actualMaterialCosts: 25000.00,
                            plannedMaterialCosts: 20000.00,
                            actualSalaryExpenses: 55000.00,
                            plannedSalaryExpenses: 50000.00
                        }
                    ],
                                    contractCounterparties: [
                        {
                            name: "Contract 1",
                            type: "PROCUREMENT",
                            counterparty: {
                                name: "Counterparty 5",
                                address: "123 Main St",
                                inn: "555-5555"
                            },
                            amount: 100000.0,
                            plannedStartDate: "2023-04-01",
                            plannedEndDate: "2023-05-31",
                            actualStartDate: "2023-05-01",
                            actualEndDate: "2023-07-15"
                        }
                    ],
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

        async getContractt() {
            try {
                const response = await axios.get(this.$store.state.url + '/api/menu/contracts/show', 
                {headers: {
                    "Authorization":  "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbm5ubmEifQ.8uH8-TkwiqueuYwOaA7VpFpjQZyvXZbgJWyBc99tN2E",               
                }})
                console.log(response)
                for (let i = 0; i<response.data.length; i++)  {
                    console.log(i);
                    this.addAllContracts(response.data[i])
                    console.log(response.data[i]);                   
                }                         
            } 
            catch(e) {
                alert('Error is true')
            }      
        },

        

        
    },
}

</script>

<style scoped>

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
.table {
    border: 1px solid black;
}
th {
    width: 250px;
}
</style>

