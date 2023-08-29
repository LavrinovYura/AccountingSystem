<template class="main">
    <v-app>   
        <section >
        
        <my-menu></my-menu>
        </section>
        <section class="content" >
        <h1>Контракты</h1> {{ choiceName }}
        <v-divider></v-divider>
            <v-container>
                <v-row>
                    <v-dialog 
                        v-model="$store.state.dialog1"
                        persistent
                        max-width="1000px">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-btn 
                                outlined  
                                icon color="green" 
                                v-bind="attrs" 
                                v-on="on">
                                    <v-icon >mdi-plus</v-icon>
                            </v-btn>
                        </template>
                        <my-create-contract></my-create-contract>
                    </v-dialog>    
                    <v-dialog 
                        v-model="$store.state.dialog8"
                        persistent
                        max-width="1000px">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-btn class="btn"
                                outlined  
                                icon color="blue" 
                                v-bind="attrs" 
                                v-on="on">
                                    <v-icon >mdi-pencil</v-icon>
                            </v-btn>
                        </template>
                        <my-edit-contract
                            :EditContract='choiceName'
                        ></my-edit-contract>
                    </v-dialog>            
                    <template>
                        <v-btn class="btn"
                            outlined  
                            icon color="blue" 
                            @click="search=!search"
                            >
                                <v-icon >mdi-magnify</v-icon>
                        </v-btn>
                    </template>  
                    <v-dialog v-model="dialogDelete" 
                    persistent
                    max-width="350px">
                    <template v-slot:activator="{ on, attrs }">
                        <v-btn outlined
                        class="btn"  
                            icon color="red"
                            v-bind="attrs" 
                            v-on="on">
                                <v-icon>mdi-delete</v-icon>
                        </v-btn>
                    </template>
                    <v-card>
                        <v-card-text>
                            Вы уверены, что хотите удалить контракт?
                        </v-card-text>
                        <v-card-actions>
                            <v-btn @click="dialogDelete=!dialogDelete">Отмена</v-btn>
                            <v-btn @click="dialogDelete=!dialogDelete">Удалить</v-btn>
                        </v-card-actions>
                    </v-card>
                    </v-dialog>             
                </v-row>
                <section v-show="search" class="search">
                    <v-row>
                        <v-col v-for="(field, index) in fields" :key="index" cols="2,5">  
                                                     
                                <template class="input" v-if="field.type === 'select'">
                                    <v-select :items="type" v-model="forSearch[field.model]"></v-select>
                                </template>                               
                                <template class="input" v-else>
                                    <v-text-field  :type="field.type" v-model="forSearch[field.model]" ></v-text-field>
                                </template>
                                <label class="label"> {{ field.name }}</label> 
                            </v-col>
                    </v-row>
                    
                    <template>
                        <v-btn rounded  outlined color="green">Найти</v-btn>
                    </template>
                </section>
                <v-dialog 
                        v-model="$store.state.dialog7"
                        persistent
                        max-width="1000px">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-btn class="btn"
                                 
                                small
                                v-bind="attrs" 
                                v-on="on">
                                    Просмотр
                            </v-btn>
                        </template>
                        <my-edit-contract
                            :EditContract='choiceName'
                            :disabled="true"
                        ></my-edit-contract>
                    </v-dialog>     
            </v-container>        
            <v-divider></v-divider>
            <v-container>
                <v-simple-table height="300px" class="table" >
                    <template  v-slot:default>
                        <thead>
                            <th v-for="item in headers" :key="item.text">
                                {{ item.text }}
                            </th>
                        </thead>
                        <tbody>
                            <tr v-for="(item) in contracts"
                                :key="item.idndex"
                                @click="Show(item), setActive=item.name, choiceName= item"
                                :class = "{'blue lighten-5': setActive===item.name}"
                                >
                                <td>{{ item.name}}</td>
                                <td>{{ item.type }}</td>
                                <td>{{ item.plannedStartDate }}</td>
                                <td>{{ item.plannedEndDate }}</td>
                                <td>{{ item.actualStartDate }}</td>
                                <td>{{ item.actualEndDate }}</td>
                                <td>{{ item.amount }}</td>
                                <td><div v-for="(items,id) in item.phases" :key='id'>{{ items.name }}</div></td>
                                <td><div v-for="(items,id) in item.contractCounterparties" :key='id'>{{ items.name }}</div></td>
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
        {{ testLocal }}{{ $store.state.tokenType }}
    </v-app>
</template>

<script>
import { mapGetters, mapMutations,mapActions } from 'vuex';
import MyMenu from '../components/MyMenu.vue';
import MyEditContract from '../components/MyEditContract.vue';
import MyCreateContract from '../components/MyCreateContract.vue';
export default {
    
    components: {
        MyMenu,
        MyEditContract,
        MyCreateContract,
    },
    data() { 
        return { 
            choiceName: '', 
            setActive: '',    
            forSearch: {
                name: '',
                type: '',
                plannedStartDate: '',
                plannedEndDate: '',
                actualStartDate: '',
                actualEndDate: '',
                amount: '',
            },     
            dialog2: false,
            search: false,
            fields: 
            [{name: 'Название', model: 'name', type: ''},
            {name: 'Тип', model: 'type', type: 'select'},
            {name: 'Плановая Дата Начала', model: 'plannedStartDate', type: 'date'},
            {name: 'Плановая дата окончания', model: 'plannedEndDate', type: 'date'},
            {name: 'Фактическая дата начала', model: 'actualSartDate', type: 'date'},
            {name: 'Фактическая дата окончания', model: 'actualEndDate', type: 'date'},
            {name: 'Сумма', model: 'amount', type: ''}],
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
                testLocal: localStorage.tok,
                dialogDelete: false,
        }
    },
    computed:{
        ...mapGetters({
            headers: 'contractsModul/headers',           
            contracts: 'contracts',
            type: 'contractsModul/type',
        
        }),
        
    },

    methods: {
        ...mapMutations({
            addNewContract: 'ADD_NEW_CONTRACT',
            addAllContracts: 'ADD_ALL_CONTRACTS'
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

        async getContractt() {
            try {
                const Body = {};
                const response = await axios.post(this.$store.state.url + '/api/menu/contracts/show', 
                
                    Body,
                
                {headers: {
                    "Authorization":  "Bearer " + localStorage.token,               
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

    created() {
      this.getContractt()
    }
}

</script>

<style scoped>
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
    overflow-y: auto;
    width: 1500px;
}
th {
    width: 250px;
}
.search {
    padding-top: 20px;
}
.label {
    color: rgb(168, 165, 165);
    padding-bottom: 50px;
}
.input{
    padding-top: 50px;
}
.v-data-table_wrapper {
    overflow-y: auto;
}
</style>

