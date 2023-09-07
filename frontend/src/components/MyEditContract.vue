<template>
    <v-app class="main">
        <v-card class="card">
                <v-card-title>
                    <span class="text-h5"
                    > Редактировать  {{ EditContract.id }} контракт
                    
                    </span>
                </v-card-title>
                <v-card-text>
 
                    <v-divider></v-divider>
                    <template>
                        <v-row>
                            <v-col v-for="(field, names, index) in fields" :key="index" cols="3">
                                <label> {{ field.label }}</label>
                                    <template v-if="field.type === 'select'">
                                        <v-select :readonly="disabled" :items="type" v-model="EditContract[field.model]"></v-select>
                                    </template>
                                
                                <template v-else>
                                    <v-text-field :readonly="disabled" :type="field.type" v-model="EditContract[field.model]" ></v-text-field>
                                </template>
                            </v-col>
                        </v-row>
                        </template>

                    <v-divider></v-divider>
                    <section > 
                        <h4>Этапы</h4>
                        <v-row  v-for="(phase, index) in EditContract.phases" :key=" phase.id"> 
                            <v-divider></v-divider>
                            <h6>Этап {{ index+1 }}</h6>
                            <v-col v-for="name in textPhases" cols="3">
                                <label>{{ name.name1 }}
                                    <v-text-field :readonly="disabled" :type="name.type1" v-model="phase[name.model1]">
                                    </v-text-field>
                                </label>
                            </v-col>
                        </v-row>
                        <v-btn :readonly="disabled" outlined  icon color="blue" 
                            
                            @click="addPhase"
                        > 
                            <v-icon >mdi-plus</v-icon>
                        </v-btn>
                        
                    </section>
                    <v-divider></v-divider>
                    
                    <section>
                        <h4>Договор с контрагентом</h4>
                        <v-divider></v-divider>

                        <v-row v-for="(agent, id) in EditContract.contractCounterparties" :key="agent.id">
                            <h6>Контрагент {{ id+1 }}</h6>
                            <v-col v-for="(name, index) in textPhases" cols="3">
                                <label>{{ name.name2 }}</label>
                                <template v-if="name.model2 === 'type'">
                                    <v-select 
                                    clearable :items="type" 
                                    :readonly="disabled"
                                    v-model="agent[name.model2]"></v-select>
                                    
                                </template>
                                <template v-else-if="name.model2==='organization'">
                                    <v-autocomplete 
                                        
                                    :readonly="disabled"
                                        :items="filteredWords"
                                        hide-no-data
                                        claerable
                                        v-model.lazy="agent[name.model2]"
                                    ></v-autocomplete>

                                </template>
                                <template v-else>
                                    <v-text-field :readonly="disabled" :type="name.type1" v-model="agent[name.model2]"></v-text-field>
                                </template>
                            </v-col>
                            
                        </v-row>
                        
                        <v-btn :readonly="disabled" outlined  icon color="blue" 
                            
                            @click="addContragent"> 
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
                   
                    <v-btn
                        color="blue darken-1"
                        text  
                        :disabled="disabled"                              
                        @click=" closeDialog()"
                        >Сохранить
                    </v-btn>
                </v-card-actions>
            </v-card>
            
    </v-app>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex';
export default {
    name: 'MyEditContract',
    props: {EditContract: Object, 
        disabled: Boolean,
        
    },
    data() {
        return {
            EditName: '',
            NewPhase: '',
            NewAgent: '',
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
            selectedWord: '',
            filteredWords: [],
            
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
            
            this.EditContract.phases = [...this.EditContract.phases, phases];
            this.NewPhase = [...this.NewPhase, phases];
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
            
            this.EditContract.contractCounterparties.push(contractCounterparties)
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
    created() {
        this.EditName=this.EditContract.name
    }
}


</script>

<style scoped>
.card {
    width: 100%;
}
.btn {
    margin-left:20px
}


</style>