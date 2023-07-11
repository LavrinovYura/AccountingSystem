<template>
    <v-app class="main">
        <v-card class="card">
                <v-card-title>
                    <span class="text-h5"
                    > Редактировать контракт
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
                    <section> <h5>Этапы</h5>
                        <v-row> 
                            <v-col><v-text-field v-model="newContract.phases.name" label="Название"></v-text-field></v-col>
                            <v-col><v-text-field v-model="newContract.phases.amount" label="Сумма"></v-text-field></v-col>
                            <v-col><v-text-field v-model="newContract.phases.planAmount" label="План расходы"></v-text-field></v-col>
                            <v-col><v-text-field v-model="newContract.phases.factAmount" label="Факт расходы"></v-text-field></v-col>
                            
                            
                        </v-row>
                        <v-row>
                           
                            <v-col><label>План дата начала<v-text-field v-model="newContract.phases.plannedStartDate" type="Date" ></v-text-field></label></v-col>
                            <v-col><label>План дата окончания<v-text-field v-model="newContract.phases.plannedEndDate" type="Date"></v-text-field></label></v-col>
                            <v-col><label>Факт дата начала<v-text-field v-model="newContract.phases.actualStartDate" type="Date" ></v-text-field></label></v-col>
                            <v-col><label>Факт дата окончания<v-text-field v-model="newContract.phases.actualEndDate" type="Date"></v-text-field></label></v-col>
                        </v-row>
                        <v-btn outlined  icon color="blue"> 
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>
                    </section>
                    <v-divider></v-divider>
                    <section> <h5>Договор с контрагентом</h5>
                        <v-row> 
                            <v-col><v-text-field  label="Название"></v-text-field></v-col>
                            <v-col><v-text-field  label="Организация"></v-text-field></v-col>
                            <v-col><v-text-field  label="Сумма договора"></v-text-field></v-col>
                            <v-col><v-select :items="type" 
                                    :menu-props="{ top: true, offsetY: true }" 
                                    label="Тип договора">
                                </v-select>
                            </v-col>
                        </v-row>
                        <v-row>
                           
                           <v-col><label>План дата начала<v-text-field type="Date" ></v-text-field></label></v-col>
                           <v-col><label>План дата окончания<v-text-field type="Date"></v-text-field></label></v-col>
                           <v-col><label>Факт дата начала<v-text-field type="Date" ></v-text-field></label></v-col>
                           <v-col><label>Факт дата окончания<v-text-field type="Date"></v-text-field></label></v-col>
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
                                            @click="closeDialog()"
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
                    {{ newContract.name }} {{ newContract.phases.name}}
                    <v-btn
                        color="blue darken-1"
                        text                                
                        @click=" sndNewContract(), closeDialog()"
                        >Сохранить
                    </v-btn>
                </v-card-actions>
            </v-card>
            
    </v-app>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex';
export default {
    name: 'MyCreateContract',
    data() {
        return {
            newContract:
                {
                    name: '',
                    type: '',
                    plannedStartDate: '',
                    plannedEndDate: '',
                    actualStartDate: '',
                    actualEndDate: '',
                    amount: '',
                    phases: [{ 
                        name: '',
                        amount: '',
                        planAmount: '',
                        factAmount: '',
                        plannedStartDate: '',
                        plannedEndDate: '',
                        actualStartDate: '',
                        actualEndDate: '',
                    }],
                    contractCounterparties: [{ 
                        name: '',
                        amount: '',
                        organization: '',
                        type: '',
                        plannedStartDate: '',
                        plannedEndDate: '',
                        actualStartDate: '',
                        actualEndDate: '',
                    }]
                },
            
            dialog2: false,
            
        }
    },
    methods: {
        ...mapMutations({
            addNewContract: 'ADD_NEW_CONTRACT',
            addAllContracts: 'ADD_ALL_CONTRACTS',
            closeDialog: 'CLOSE_DIALOG'
        }),
        sndNewContract() {
            
            const res = {};
            for (let item in this.newContract) {
               res[item] = this.newContract[item];
            }
            this.addNewContract(res);
            for (let item in this.newContract) {
               this.newContract[item] = '';
            };
            
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
    computed:{
        ...mapGetters({
            headers: 'contractsModul/headers',           
            contracts: 'contracts',
            type: 'contractsModul/type',
            dialog1: 'index/dialog1'
        }),
        
    },
}


</script>

<style scoped>
.card {
    width: 100%;
}



</style>