<template>
    <v-app class="main">
        <v-card class="card">
                <v-card-title>
                    <span class="text-h5"
                    >Добавить новый контракт
                    </span>
                </v-card-title>
                <v-card-text>

                    <v-divider></v-divider>
                    <template>
                        <v-row>
                            <v-col v-for="(field, index) in fields" :key="index" cols="3">
                                <label> {{ field.label }}</label>
                                    <template v-if="field.type === 'select'">
                                        <v-select :items="type" v-model="newContract[field.model]"></v-select>
                                    </template>
                                
                                <template v-else>
                                    <v-text-field :type="field.type" v-model="newContract[field.model]" ></v-text-field>
                                </template>
                            </v-col>
                        </v-row>
                        </template>

                    <v-divider></v-divider>
                    <section > 
                        <h4>Этапы</h4>
                        <v-row  v-for="(phase, index) in newContract.phases" :key=" phase.id"> 
                            <v-divider></v-divider>
                            <h6>Этап {{ index+1 }}</h6>
                            <v-col v-for="name in textPhases" cols="3">
                                <label>{{ name.name1 }}
                                    <v-text-field :type="name.type1" v-model="phase[name.model1]">
                                    </v-text-field>
                                </label>
                            </v-col>
                        </v-row>
                        <v-btn outlined  icon color="blue" @click="addPhase"> 
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>
                    </section>
                    <v-divider></v-divider>
                    
                    <section>
                        <h4>Договор с контрагентом</h4>
                        <v-divider></v-divider>

                        <v-row v-for="(agent, id) in newContract.contractCounterparties" :key="agent.id">
                            <h6>Контрагент {{ id+1 }}</h6>
                            <v-col v-for="(name, index) in textPhases" cols="3">
                                <label>{{ name.name2 }}</label>
                                <template v-if="name.model2 === 'type'">
                                    <v-select :items="type"  v-model="agent[name.model2]"></v-select>
                                    
                                </template>
                                <template v-else>
                                    <v-text-field :type="name.type1" v-model="agent[name.model2]"></v-text-field>
                                </template>
                            </v-col>
                            
                        </v-row>
                        <v-btn outlined  icon color="blue" @click="addContragent"> 
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>
                        <v-divider></v-divider>
                    </section>
                    {{ newContract.contractCounterparties }}
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
                        @click="sndNewContract(), closeDialog()"
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
            newContract:{
                name: '',
                type: '',
                plannedStartDate: '',
                plannedEndDate: '',
                actualStartDate: '',
                actualEndDate: '',
                amount: '',
                phases: [{ 
                    id: '',
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
            phase: { 
                id: '',
                name: '',
                amount: '',
                planAmount: '',
                factAmount: '',
                plannedStartDate: '',
                plannedEndDate: '',
                actualStartDate: '',
                actualEndDate: '',
            }    ,
            
            textPhases: [
                {name1: 'Название',name2: 'Название',type1: '', model1: 'name',model2: 'name'},
                {name1: 'Сумма',name2: 'Сумма договора',type1: '',model1: 'amount',model2: 'amount'},
                {name1: 'Плановые расходы',name2: 'Организация ',type1: '',model1: 'planAmount', model2: 'organization'},
                {name1: 'Фактические расходы',name2: 'Тип договора',type1: '',model1: 'factAmount',model2: 'type'},
                {name1: 'Плановая дата начала',name2: 'Плановая дата начала',type1: 'Date',model1: 'plannedStartDate',model2: 'plannedStartDate'},
                {name1: 'Плановая дата окончания',name2: 'Плановая дата окончания',type1: 'Date',model1: 'plannedEndDate',model2: 'plannedEndDate'}, 
                {name1: 'Фактическая дата начала',name2: 'Фактическая дата начала',type1: 'Date',model1: 'actualStartDate',model2: 'actualStartDate'},
                {name1: 'Фактическая дата окончания', name2: 'Фактическая дата окончания', type1: 'Date', model1: 'actualEndDate', model2: 'actualEndDate'},
            ],
            fields: [
                { label: 'Название', model: 'name', type: 'text' },
                { label: 'Тип', model: 'type', type: 'select', options: ['option1', 'option2'] },
                { label: 'Плановая дата начала', model: 'plannedStartDate', type: 'date' },
                { label: 'Плановая дата окончания', model: 'plannedEndDate', type: 'date' },
                { label: 'Фактическая дата начала', model: 'actualStartDate', type: 'date' },
                { label: 'Фактическая дата окончания', model: 'actualEndDate', type: 'date' },
                { label: 'Сумма договора', model: 'amount', type: 'text' }
      ],
            
            dialog2: false,
            
        }
    },
    methods: {
        ...mapMutations({
            addNewContract: 'ADD_NEW_CONTRACT',
            addAllContracts: 'ADD_ALL_CONTRACTS',
            closeDialog: 'CLOSE_DIALOG'
        }),

        addPhase() {
            const phases= { 
                id: Date.now(),
                name: '',
                amount: '',
                planAmount: '',
                factAmount: '',
                plannedStartDate: '',
                plannedEndDate: '',
                actualStartDate: '',
                actualEndDate: '',
            }    
            
            this.newContract.phases = [...this.newContract.phases, phases];
},

        addContragent() {
            const contractCounterparties= { 
                id: Date.now(),
                name: '',
                amount: '',
                organization: '',
                type: '',
                plannedStartDate: '',
                plannedEndDate: '',
                actualStartDate: '',
                actualEndDate: '',           
            }
            
            this.newContract.contractCounterparties.push(contractCounterparties)
        },


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